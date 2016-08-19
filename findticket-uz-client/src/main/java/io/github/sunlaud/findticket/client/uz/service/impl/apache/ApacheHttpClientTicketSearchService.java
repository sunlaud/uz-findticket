package io.github.sunlaud.findticket.client.uz.service.impl.apache;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.github.sunlaud.findticket.client.uz.Apis;
import io.github.sunlaud.findticket.client.uz.dto.TrainDto;
import io.github.sunlaud.findticket.client.uz.request.FindTrainRequest;
import io.github.sunlaud.findticket.client.uz.request.GetAvailableSeatsRequest;
import io.github.sunlaud.findticket.client.uz.request.GetCoachesRequest;
import io.github.sunlaud.findticket.client.uz.response.ApiResponse;
import io.github.sunlaud.findticket.client.uz.response.FindStationResponse;
import io.github.sunlaud.findticket.client.uz.response.GetAvailableSeatsResponse;
import io.github.sunlaud.findticket.client.uz.response.GetCoachesResponse;
import io.github.sunlaud.findticket.client.uz.service.TicketSearchService;
import io.github.sunlaud.findticket.client.uz.util.AuthService;
import io.github.sunlaud.findticket.client.uz.util.Utils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link TicketSearchService}
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
        String rawResponse = Request.Post(Apis.BASE_URL + url).execute().returnContent().asString(Consts.UTF_8);
        return mapper.readValue(rawResponse, FindStationResponse.class);
    }

    @SneakyThrows
    @Override
    public ApiResponse<List<TrainDto>> findTrains(FindTrainRequest findTrainRequest) {
        log.debug("Searching for trains satisfying params {}", findTrainRequest);
        String payload = Utils.asUrlEncodedString(findTrainRequest);
        String rawResponse = sendApiRequest(Apis.FIND_TRAINS_URL, payload);
//        String rawResponse = "{\"value\":[{\"num\":\"079\\u041f\",\"model\":0,\"category\":0,\"travel_time\":\"8:32\",\"from\":{\"station_id\":2210700,\"station\":\"\\u0414\\u043d\\u0456\\u043f\\u0440\\u043e\\u043f\\u0435\\u0442\\u0440\\u043e\\u0432\\u0441\\u044c\\u043a \\u0413\\u043e\\u043b\\u043e\\u0432\\u043d\\u0438\\u0439\",\"date\":1466796300,\"src_date\":\"2016-06-24 22:25:00\"},\"till\":{\"station_id\":2200001,\"station\":\"\\u041a\\u0438\\u0457\\u0432-\\u041f\\u0430\\u0441\\u0430\\u0436\\u0438\\u0440\\u0441\\u044c\\u043a\\u0438\\u0439\",\"date\":1466827020,\"src_date\":\"2016-06-25 06:57:00\"},\"types\":[{\"title\":\"\\u041b\\u044e\\u043a\\u0441\",\"letter\":\"\\u041b\",\"places\":19},{\"title\":\"\\u041a\\u0443\\u043f\\u0435\",\"letter\":\"\\u041a\",\"places\":12},{\"title\":\"\\u041f\\u043b\\u0430\\u0446\\u043a\\u0430\\u0440\\u0442\",\"letter\":\"\\u041f\",\"places\":1}]}],\"error\":null,\"data\":null,\"captcha\":null}";
        log.debug("Find trains raw response: {}", rawResponse);
        if (log.isDebugEnabled()) {
            Utils.prettyPrintResponse(rawResponse);
        }
        TypeReference<List<TrainDto>> ref = new TypeReference<List<TrainDto>>() {
        };
        return mapper.readValue(rawResponse, ref);
    }

    @Override
    public GetCoachesResponse getCoaches(GetCoachesRequest getCoachesRequest) {
        throw new NotImplementedException("Get coaches via apache http client not implemented");
    }

    @Override
    public GetAvailableSeatsResponse getFreeSeats(GetAvailableSeatsRequest getAvailableSeatsRequest) {
        throw new NotImplementedException("Get free seats via apache http client not implemented");
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
}
