package io.github.sunlaud.findticket;


import io.github.sunlaud.findticket.api.Apis;
import io.github.sunlaud.findticket.api.model.FreeSeatsSummary;
import io.github.sunlaud.findticket.api.model.Train;
import io.github.sunlaud.findticket.api.request.FindTrainRequest;
import io.github.sunlaud.findticket.api.service.TicketSearchService;
import io.github.sunlaud.findticket.api.service.impl.ApacheHttpClientTicketSearchService;
import io.github.sunlaud.findticket.api.service.impl.RetrofitTicketSearchService;
import io.github.sunlaud.findticket.api.util.AuthService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Main {
    private    TicketSearchService ticketSearchService = new ApacheHttpClientTicketSearchService();


    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private void run() throws IOException {
        //System.out.println(findStationsMatchingPrefix("Київ"));

        FindTrainRequest findTrainRequest = FindTrainRequest.builder()
                .departureDate("24.06.2016")
                .departureTime("22:00")
                .stationIdFrom(2210700)
                .stationIdTill(2200001)
                .build();

        searchTrainsViaApacheHttpClientImpl(findTrainRequest, "079П");
        searchTrainsViaApacheHttpClientImpl(findTrainRequest
                .withSwappedStations()
                .departureDate("02.07.2016")
                .build(), "080К");
    }

    private void searchTrainsViaApacheHttpClientImpl(FindTrainRequest findTrainRequest, String trainNumber) throws IOException {
        System.out.println("\n==========================================");
        List<Train> trains = ticketSearchService.findTrains(findTrainRequest).getTrains();
        System.out.println("Found trains:");
        trains.stream().forEach(System.out::println);
        List<FreeSeatsSummary> freeSeats = trains.stream()
                .filter(train -> trainNumber == null || train.getNumber().equals(trainNumber))
                .map(Train::getFreeSeats)
                .findFirst().get();
        System.out.println("Free seats: ");
        freeSeats.stream().forEach(System.out::println);
        System.out.println("==========================================\n");
    }

    private void searchTrainsViaRetrofitImpl(FindTrainRequest findTrainRequest, String trainCode) throws IOException {
        AuthService authService = new AuthService();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("GV-Token", authService.getToken());
        headersMap.put("GV-Ajax", "1");
        headersMap.put("Content-Type", "application/x-www-form-urlencoded");
        headersMap.put("Referer", Apis.BASE_URL);
        Headers headers = Headers.of(headersMap);

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().headers(headers).build();
            return chain.proceed(request);
        });
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Apis.BASE_URL)
                .addCallAdapterFactory(SynchronousCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();

        RetrofitTicketSearchService service = retrofit.create(RetrofitTicketSearchService.class);

        System.out.println(service.findStations("Киї"));
        System.out.println(service.findTrains(findTrainRequest));
    }


    public static class SynchronousCallAdapterFactory extends CallAdapter.Factory {
        public static CallAdapter.Factory create() {
            return new SynchronousCallAdapterFactory();
        }

        @Override
        public CallAdapter<Object> get(final Type returnType, Annotation[] annotations, Retrofit retrofit) {
            // if returnType is retrofit2.Call, do nothing
            if (returnType.getTypeName().contains("retrofit2.Call")) {
                return null;
            }

            return new CallAdapter<Object>() {
                @Override
                public Type responseType() {
                    return returnType;
                }

                @Override
                public <R> Object adapt(Call<R> call) {
                    try {
                        return call.execute().body();
                    } catch (IOException e) {
                        throw new RuntimeException(); // do something better
                    }
                }
            };
        }
    }
}
