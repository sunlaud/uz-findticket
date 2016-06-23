package io.github.sunlaud.findticket.api.service.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.github.sunlaud.findticket.api.Apis;
import io.github.sunlaud.findticket.api.model.Coach;
import io.github.sunlaud.findticket.api.model.Train;
import io.github.sunlaud.findticket.api.request.FindTrainRequest;
import io.github.sunlaud.findticket.api.response.FindStationResponse;
import io.github.sunlaud.findticket.api.response.FindTrainResponse;
import io.github.sunlaud.findticket.api.response.GetAvailableSeatsResponse;
import io.github.sunlaud.findticket.api.response.GetCoachesResponse;
import io.github.sunlaud.findticket.api.service.TicketSearchService;
import io.github.sunlaud.findticket.api.util.AuthService;
import io.github.sunlaud.findticket.api.util.Utils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link io.github.sunlaud.findticket.api.service.TicketSearchService}
 * using Apache HttpClient library
 */
@Slf4j
public class ApacheHttpClientTicketSearchService implements TicketSearchService {

    private ObjectMapper mapper;
    private AuthService authService = new AuthService();

    public ApacheHttpClientTicketSearchService() {
        JsonFactory factory = new JsonFactory();
        mapper = new ObjectMapper(factory);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    @SneakyThrows
    @Override
    public FindStationResponse findStations(String stationNameFirstLetters) {
        String url = Apis.FIND_STATIONS_URL.replace("{stationNameFirstLetters}", URLEncoder.encode(stationNameFirstLetters, "UTF-8"));
        String rawResponse = Request.Post(url).execute().returnContent().asString(Consts.UTF_8);
        return mapper.readValue(rawResponse, FindStationResponse.class);
    }

    @SneakyThrows
    @Override
    public FindTrainResponse findTrains(FindTrainRequest findTrainRequest) {
        log.debug("Searching for trains satisfying params {}", findTrainRequest);
        String payload = FindTrainRequestFormatter.format(findTrainRequest);
        String rawResponse = sendApiRequest(Apis.FIND_TRAINS_URL, payload);
//        String rawResponse = "{\"value\":[{\"num\":\"079\\u041f\",\"model\":0,\"category\":0,\"travel_time\":\"8:32\",\"from\":{\"station_id\":2210700,\"station\":\"\\u0414\\u043d\\u0456\\u043f\\u0440\\u043e\\u043f\\u0435\\u0442\\u0440\\u043e\\u0432\\u0441\\u044c\\u043a \\u0413\\u043e\\u043b\\u043e\\u0432\\u043d\\u0438\\u0439\",\"date\":1466796300,\"src_date\":\"2016-06-24 22:25:00\"},\"till\":{\"station_id\":2200001,\"station\":\"\\u041a\\u0438\\u0457\\u0432-\\u041f\\u0430\\u0441\\u0430\\u0436\\u0438\\u0440\\u0441\\u044c\\u043a\\u0438\\u0439\",\"date\":1466827020,\"src_date\":\"2016-06-25 06:57:00\"},\"types\":[{\"title\":\"\\u041b\\u044e\\u043a\\u0441\",\"letter\":\"\\u041b\",\"places\":19},{\"title\":\"\\u041a\\u0443\\u043f\\u0435\",\"letter\":\"\\u041a\",\"places\":12},{\"title\":\"\\u041f\\u043b\\u0430\\u0446\\u043a\\u0430\\u0440\\u0442\",\"letter\":\"\\u041f\",\"places\":1}]}],\"error\":null,\"data\":null,\"captcha\":null}";
        log.debug("Find trains raw response: {}", rawResponse);
        if (log.isDebugEnabled()) {
            Utils.prettyPrintResponse(rawResponse);
        }
        return mapper.readValue(rawResponse, FindTrainResponse.class);
    }

    @Override
    public GetCoachesResponse getCoachesByType(Train train, String coachType) {
        return null;
    }

    @Override
    public GetAvailableSeatsResponse getFreeSeats(Train train, Coach coach) {
        return null;
    }



    @SneakyThrows
    private String sendApiRequest(String subUrl, String payload) {
        log.debug("Sending HTTP request to {}", subUrl);
        Request request = Request.Post(Apis.BASE_URL + subUrl)
                .body(new StringEntity(payload))
                .setHeaders(buildHeaders().toArray(new Header[]{}));
        try {
            return request.execute().returnContent().asString(Consts.UTF_8);
        } catch (ClientProtocolException ex) {
            //most likely ClientProtocolExceptions is caused by expired token, try to renew it
            authService.refresh();
            return request.execute().returnContent().asString(Consts.UTF_8);
        }
    }

    private List<Header> buildHeaders() throws IOException {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("GV-Token", authService.getToken()));
        headers.add(new BasicHeader("GV-Ajax", "1"));
        headers.add(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
        headers.add(new BasicHeader("Referer", Apis.BASE_URL));
        //cookies are handled by http client
        return headers;
    }

    private static class FindTrainRequestFormatter {

        public static String format(FindTrainRequest findTrainRequest) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("date_dep", findTrainRequest.getDepartureDate())); //24.06.2016
            params.add(new BasicNameValuePair("time_dep", findTrainRequest.getDepartureTime())); //22:00
            params.add(new BasicNameValuePair("station_id_from", findTrainRequest.getStationIdFrom().toString()));
            params.add(new BasicNameValuePair("station_id_till", findTrainRequest.getStationIdTill().toString()));

            params.add(new BasicNameValuePair("another_ec", "0"));
            params.add(new BasicNameValuePair("search", ""));
            params.add(new BasicNameValuePair("time_dep_till", ""));
    //        params.add(new BasicNameValuePair("station_from", "Дніпропетровськ-Голов."));
    //        params.add(new BasicNameValuePair("station_till", "Київ"));
            return URLEncodedUtils.format(params, "UTF-8");
        }
    }
}
