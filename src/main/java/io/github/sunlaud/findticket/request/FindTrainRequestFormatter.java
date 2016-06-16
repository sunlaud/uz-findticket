package io.github.sunlaud.findticket.request;


import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class FindTrainRequestFormatter {

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
