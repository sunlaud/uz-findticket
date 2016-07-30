package io.github.sunlaud.findticket;


import com.fasterxml.jackson.annotation.JsonProperty;
import feign.Feign;
import feign.FeignException;
import feign.Logger;
import feign.Param;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jaxrs.JAXRSContract;
import feign.slf4j.Slf4jLogger;
import io.github.sunlaud.findticket.api.Apis;
import io.github.sunlaud.findticket.api.model.FreeSeatsSummary;
import io.github.sunlaud.findticket.api.model.Train;
import io.github.sunlaud.findticket.api.request.FindTrainRequest;
import io.github.sunlaud.findticket.api.service.TicketSearchService;
import io.github.sunlaud.findticket.api.service.impl.ApacheHttpClientTicketSearchService;
import io.github.sunlaud.findticket.api.service.impl.FeignTicketSearchService;
import io.github.sunlaud.findticket.api.service.impl.FeignUzAuthService;
import io.github.sunlaud.findticket.api.service.impl.RetrofitTicketSearchService;
import io.github.sunlaud.findticket.api.util.AuthService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.io.IOUtils;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class Main {
    private    TicketSearchService ticketSearchService = new ApacheHttpClientTicketSearchService();


    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private void run() throws IOException {
        //System.out.println(findStationsMatchingPrefix("Київ"));

        FindTrainRequest findTrainRequest = FindTrainRequest.builder()
                .departureDate(/*LocalDate.of(2016, 8, 19)*/ "19.08.2016")
                .departureTime("00:00")
                .stationIdFrom(2210700)
                .stationIdTill(2208001)
                .build();

        //System.out.println(ApacheHttpClientTicketSearchService.FindTrainRequestFormatter.format(findTrainRequest));
        //System.exit(0);

        //searchTrainsViaApacheHttpClientImpl(findTrainRequest, "228П");
//        searchTrainsViaApacheHttpClientImpl(findTrainRequest
//                .withSwappedStations()
//                .departureDate("02.07.2016")
//                .build(), "080К");

        searchTrainsViaFeignImpl(findTrainRequest, "228П");

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


    public static class UrlEncodingExpander implements Param.Expander {

        @Override
        public String expand(Object object) {
            StringBuilder sb = new StringBuilder();
            Arrays.stream(object.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(JsonProperty.class))
                    .map(field -> fieldToEncodedKeyValuePair(field, object))
                    .forEach(sb::append);
            //return sb.toString();
            return "date_dep=26.08.2016&time_dep=00%3A00&station_id_from=2210700&station_id_till=2200001&another_ec=0&search=&time_dep_till=";
        }


        @SneakyThrows
        private String fieldToEncodedKeyValuePair(Field field, Object obj) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value == null) {
                return "";
            }
            String key = field.getAnnotation(JsonProperty.class).value();
            return URLEncoder.encode(key) + "=" + URLEncoder.encode(value.toString());
        }


    }

    public static class UrlEncodedEncoder implements Encoder {

        @Override
        public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
            StringBuilder sb = new StringBuilder();
            Arrays.stream(object.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(JsonProperty.class))
                    .map(field -> fieldToEncodedKeyValuePair(field, object))
                    .forEach(kv -> sb.append(kv).append("&"));
            sb.deleteCharAt(sb.length() - 1);
            //template.body("date_dep=26.08.2016&time_dep=00%3A00&station_id_from=2210700&station_id_till=2200001&another_ec=0&search=&time_dep_till=");
            template.body(sb.toString());
        }


        @SneakyThrows
        private String fieldToEncodedKeyValuePair(Field field, Object obj) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value == null) {
                return "";
            }
            String key = field.getAnnotation(JsonProperty.class).value();
            return URLEncoder.encode(key) + "=" + URLEncoder.encode(value.toString());
        }
    }


    static class HeadersInterceptor implements RequestInterceptor {
        private final String cookie;
        private final AuthService authService;

        HeadersInterceptor(AuthService authService, String cookie) {
            this.authService = authService;
            this.cookie = cookie;
        }

        @Override
        public void apply(RequestTemplate template) {
            if(!"/".equals(template.url())) {
                template.header("GV-Token", authService.getToken());
            }
            template.header("GV-Ajax", "1");
            template.header("Content-Type", "application/x-www-form-urlencoded");
            template.header("Referer", Apis.BASE_URL);
            template.header("Cookie", cookie);
            template.header("Accept", "application/json");
            template.header("Accept-Encoding", "gzip,deflate");
            template.header("User-Agent", "Apache-HttpClient/4.5.2 (Java/1.8.0_45)");
            template.header("Connection", "Keep-Alive");

        }
    }

    private void searchTrainsViaFeignImpl(FindTrainRequest findTrainRequest, String trainCode) throws IOException {


        FeignUzAuthService authDataSource = Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.BASIC)
                .contract(new JAXRSContract())
                .target(FeignUzAuthService.class, Apis.BASE_URL);

        Response rootResource = authDataSource.getRootResource();
        Collection<String> cookies = rootResource.headers().get("set-cookie");
        String cookie = cookies.stream()
                .map(c -> c.split(";")[0].trim())
                .collect(Collectors.joining("; "));
        System.out.println(cookie);

        String rootPageBody = IOUtils.toString(rootResource.body().asReader());
        AuthService autService = new AuthService(() -> rootPageBody);
        HeadersInterceptor requestInterceptor = new HeadersInterceptor(autService, cookie);

        FeignTicketSearchService service = Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .encoder(new UrlEncodedEncoder())
                .decoder(new JacksonDecoder())
                .contract(new JAXRSContract())
                .requestInterceptor(requestInterceptor)
                .target(FeignTicketSearchService.class, Apis.BASE_URL);
        log.debug("test");


        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMM HH:mm");
        LocalDate date = LocalDate.of(2016, 8, 3);
        for (int i = 0; i < 10; i++) {
            date = date.plusDays(1);
            try {
                List<Train> trains = service.findTrains(findTrainRequest.withDate(date).build()).getTrains();
                System.out.println("======"  + date + ", found trains: "
                        + trains.stream().map(train ->
                            train.getNumber()
                                + " (" + train.getFreeSeats().stream()
                                    .map(seats -> seats.getLetter() + ": " + seats.getPlaces())
                                    .collect(Collectors.joining(", "))
                                +")").collect(Collectors.joining(", ")) + "====================");
                trains.stream()
                        .filter(train -> train.getNumber().startsWith("092"))
                        .forEach(train -> {
                            System.out.println(train.getNumber()
                                            + ": " + train.getFrom().getStation()
                                            + " -> " + train.getTill().getStation()
                                            + ", " + train.getFrom().getDate().format(dateFormatter)
                                            + " -> " + train.getTill().getDate().format(dateFormatter)
                            );
                            train.getFreeSeats().forEach(seats -> {
                                System.out.print(seats.getLetter() + ": " + seats.getPlaces() + "\t");
                            });
                            System.out.println();
                        });
            } catch (FeignException ex) {
                log.error("Error searching trains, maybe there is no trains", ex);
            }
        }



//        System.out.println(service.findStations("Киї"));
//        System.out.println(service.findTrains2(ApacheHttpClientTicketSearchService.FindTrainRequestFormatter.format(findTrainRequest)));

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
