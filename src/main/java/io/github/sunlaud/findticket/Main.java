package io.github.sunlaud.findticket;


import feign.Feign;
import feign.Logger;
import feign.Response;
import feign.jackson.JacksonDecoder;
import feign.jaxrs.JAXRSContract;
import feign.slf4j.Slf4jLogger;
import io.github.sunlaud.findticket.api.Apis;
import io.github.sunlaud.findticket.api.model.Train;
import io.github.sunlaud.findticket.api.request.FindTrainRequest;
import io.github.sunlaud.findticket.api.service.TicketSearchService;
import io.github.sunlaud.findticket.api.service.impl.apache.ApacheHttpClientTicketSearchService;
import io.github.sunlaud.findticket.api.service.impl.feign.FeignUzRootContentProvider;
import io.github.sunlaud.findticket.api.service.impl.feign.HeadersInterceptor;
import io.github.sunlaud.findticket.api.service.impl.feign.UrlEncodingEncoder;
import io.github.sunlaud.findticket.api.util.AuthService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Main {
    private TicketSearchService apacheTicketSearchService = new ApacheHttpClientTicketSearchService();
    private TicketSearchService feignTicketSearchService = getFeignTicketSearchService();

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private void run() throws IOException {
        TicketSearchService ticketSearchService = feignTicketSearchService;

        FindTrainRequest findTrainRequest = FindTrainRequest.builder()
                .departureDate(/*LocalDate.of(2016, 8, 19)*/ "19.08.2016")
                .departureTime("00:00")
                .stationIdFrom(2210700)
                .stationIdTill(2208001)
                .build();

        searchTrains(ticketSearchService, findTrainRequest, "092");
        //        System.out.println(ticketSearchService.findStations("Киї"));
    }






    private void searchTrains(TicketSearchService service, FindTrainRequest findTrainRequest, String trainCode) throws IOException {
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
                                + ")").collect(Collectors.joining(", ")) + "====================");
                trains.stream()
                        .filter(train -> train.getNumber().startsWith(trainCode))
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
            } catch (Exception ex) {
                log.error("Error searching trains, maybe there is no trains", ex);
            }
        }
    }

    @SneakyThrows
    private TicketSearchService getFeignTicketSearchService() {
        FeignUzRootContentProvider authDataSource = Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.BASIC)
                .contract(new JAXRSContract())
                .target(FeignUzRootContentProvider.class, Apis.BASE_URL);

        Response rootResource = authDataSource.getRootResource();
        Collection<String> cookies = rootResource.headers().get("set-cookie");
        String cookie = cookies.stream()
                .map(c -> c.split(";")[0].trim())
                .collect(Collectors.joining("; "));

        String rootPageBody = IOUtils.toString(rootResource.body().asReader());
        AuthService autService = new AuthService(() -> rootPageBody);
        HeadersInterceptor requestInterceptor = new HeadersInterceptor(autService, cookie);

        return Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .encoder(new UrlEncodingEncoder())
                .decoder(new JacksonDecoder())
                .contract(new JAXRSContract())
                .requestInterceptor(requestInterceptor)
                .target(TicketSearchService.class, Apis.BASE_URL);
    }
}
