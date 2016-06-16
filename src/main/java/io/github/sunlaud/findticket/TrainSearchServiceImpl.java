package io.github.sunlaud.findticket;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.jjencoder.JJencoder;
import io.github.sunlaud.findticket.api.model.Station;
import io.github.sunlaud.findticket.api.model.Train;
import io.github.sunlaud.findticket.api.request.FindTrainRequest;
import io.github.sunlaud.findticket.api.request.FindTrainRequestFormatter;
import io.github.sunlaud.findticket.api.response.FindStationResponse;
import io.github.sunlaud.findticket.api.response.FindTrainResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class TrainSearchServiceImpl {
    private String baseAddress = "http://booking.uz.gov.ua";
    private Pattern tokenEncodedDataPattern = Pattern.compile("\\$\\$_=.*~\\[\\];.*\"\"\\)\\(\\)\\)\\(\\);");
    private Pattern tokenPattern = Pattern.compile("[0-9a-f]{32}");
    //take care only of token, cookie with session_id will be passed by http client
    private String token;
    private ObjectMapper mapper;
    private CloseableHttpClient httpClient;

    public TrainSearchServiceImpl() {
        JsonFactory factory = new JsonFactory();
        mapper = new ObjectMapper(factory);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        httpClient = HttpClients.createDefault();
    }


    public List<Train> findTrains(FindTrainRequest findTrainRequest) throws IOException {
        String payload = FindTrainRequestFormatter.format(findTrainRequest);
        String rawResponse = sendSearchRequest("/purchase/search/", payload);
//        String rawResponse = "{\"value\":[{\"num\":\"079\\u041f\",\"model\":0,\"category\":0,\"travel_time\":\"8:32\",\"from\":{\"station_id\":2210700,\"station\":\"\\u0414\\u043d\\u0456\\u043f\\u0440\\u043e\\u043f\\u0435\\u0442\\u0440\\u043e\\u0432\\u0441\\u044c\\u043a \\u0413\\u043e\\u043b\\u043e\\u0432\\u043d\\u0438\\u0439\",\"date\":1466796300,\"src_date\":\"2016-06-24 22:25:00\"},\"till\":{\"station_id\":2200001,\"station\":\"\\u041a\\u0438\\u0457\\u0432-\\u041f\\u0430\\u0441\\u0430\\u0436\\u0438\\u0440\\u0441\\u044c\\u043a\\u0438\\u0439\",\"date\":1466827020,\"src_date\":\"2016-06-25 06:57:00\"},\"types\":[{\"title\":\"\\u041b\\u044e\\u043a\\u0441\",\"letter\":\"\\u041b\",\"places\":19},{\"title\":\"\\u041a\\u0443\\u043f\\u0435\",\"letter\":\"\\u041a\",\"places\":12},{\"title\":\"\\u041f\\u043b\\u0430\\u0446\\u043a\\u0430\\u0440\\u0442\",\"letter\":\"\\u041f\",\"places\":1}]}],\"error\":null,\"data\":null,\"captcha\":null}";
        log.debug("Find trains raw response: {}", rawResponse);
        if (log.isDebugEnabled()) {
            prettyPrintResponse(rawResponse);
        }
        return mapper.readValue(rawResponse, FindTrainResponse.class).getTrains();
    }

    public List<Station> findStationsMatchingPrefix(String stationNamePrefix) throws IOException {
        String path = "/purchase/station/" + URLEncoder.encode(stationNamePrefix, "UTF-8") + "/";
        String rawResponse = sendSearchRequest(path, "");
        return mapper.readValue(rawResponse, FindStationResponse.class).getStations();
    }

    private void prettyPrintResponse(String rawResponse) throws IOException {
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
        HashMap<String,Object> o = mapper.readValue(rawResponse, typeRef);
        MapUtils.debugPrint(System.out, "response", o);
    }

    private String getToken() throws IOException {
        if (token == null) {
            log.info("Getting token from root page");
            String rootPage = fetchRootPage();
            Matcher matcher = tokenEncodedDataPattern.matcher(rootPage);
            if (!matcher.find()) {
                throw new IllegalStateException("Can't find encoded token data");
            }
            String encodedTokenData = rootPage.substring(matcher.start(), matcher.end());
            log.debug("encodedTokenData={}", encodedTokenData);
            String decodedTokenData = new JJencoder().decode(encodedTokenData);
            log.debug("decodedTokenData={}", decodedTokenData);
            matcher = tokenPattern.matcher(decodedTokenData);
            if (!matcher.find()) {
                throw new IllegalStateException("Can't find token in decoded token data");
            }
            token = decodedTokenData.substring(matcher.start(), matcher.end());
            log.info("Got new token: {}", token);
        }
        return token;
    }

    private String fetchRootPage() throws IOException {
        return executeHttpRequest(new HttpGet(baseAddress));
    }

    private List<Header> buildHeaders() throws IOException {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("GV-Token", getToken()));
        headers.add(new BasicHeader("GV-Ajax", "1"));
        headers.add(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
        headers.add(new BasicHeader("Referer", baseAddress));
        //cookies are handled by http client
        return headers;
    }

    private String sendSearchRequest(String subUrl, String payload) throws IOException {
        HttpPost httpPost = new HttpPost(baseAddress + subUrl);
        httpPost.setEntity(new StringEntity(payload));
        httpPost.setHeaders(buildHeaders().toArray(new Header[]{}));
        try {
            return executeHttpRequest(httpPost);
        } catch (ClientProtocolException ex) {
            //most likely ClientProtocolExceptions is caused by expired token, try to renew it
            token = null;
            return executeHttpRequest(httpPost);
        }
    }

    private String executeHttpRequest(HttpUriRequest httpRequest) throws IOException {
        log.debug("Sending HTTP request to {}", httpRequest.getURI());
        CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        return response.toString();
    }

}
