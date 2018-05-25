package io.github.sunlaud.findticket.advancedplanner;

import com.google.common.base.Optional;
import io.github.sunlaud.findticket.advancedplanner.filter.ConnectionTimeTransportRouteFilter;
import io.github.sunlaud.findticket.advancedplanner.filter.FreeSeatsRouteFilter;
import io.github.sunlaud.findticket.advancedplanner.filter.RouteSeatsCountChecker;
import io.github.sunlaud.findticket.client.uz.UzTrainRouteSearchService;
import io.github.sunlaud.findticket.model.Station;
import io.github.sunlaud.findticket.util.RouteFormatter;
import org.joda.time.Duration;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Ignore("integration")
public class RouteCreatorTest {

    @Test
    public void bar() {
        Duration duration = new Duration(60*1000);
        System.out.println(RouteFormatter.formatDuration(duration));
    }

    @Test
    public void foo() {
        String[] stations = {
                "Бердичів",
                "Бердянськ",
                "Біла Церква",
                "Вінниця",
                "Горлівка",
                "Дніпро-Головний",
                "Житомир",
                "Жмеринка-Пас.",
                "Запоріжжя 1",
                "Івано-Франківськ",
                "Кам'янець-Подільський",
                "Київ",
                "Кропивницький" /* aka Кіровоград */,
                "Кременчук",
                "Кривий Ріг-Гол.",
                "Луцьк",
                "Львів",
                "Маріуполь",
                "Микитівка",
                "Мелітополь",
                "Миколаїв Пас.",
                "Мукачево",
                "Нікополь",
                "Одеса",
                "Полтава Київська",
                "Полтава-Півд.",
                "Рівне",
                "Суми",
                "Тернопіль",
                "Трускавець",
                "Ужгород",
                "Харків",
                "Херсон",
                "Хмельницький",
                "Черкаси",
                "Чернігів",
                "Чернівці"
        };


        String departureStationName = "Тернопіль";
        String arrivalStationName = "Запоріжжя 1";

        UzTrainRouteSearchService routeSearchService = new UzTrainRouteSearchService();
        AvailableTransportFinder availableTransportFinder = new AvailableTransportFinder(routeSearchService);
        RouteCreator sut = new RouteCreator(stations, new StationsCache(routeSearchService));
        RouteSeatsCountChecker seatsCountChecker = new RouteSeatsCountChecker();
        FreeSeatsRouteFilter freeSeatsRouteFilter = new FreeSeatsRouteFilter();
        TransportRoutesComposer routesComposer = new TransportRoutesComposer();


        System.out.println("+++ searching routes");
        List<Route<Station>> routes = sut.findRoutes(departureStationName, arrivalStationName);
        System.out.println("+++ routes found, connecting");
        List<AvailableTransportForRoute> availableTransportOptions = new ArrayList<>(routes.size());
        for (Route<Station> route : routes) {
            System.out.println("---searching transport for " + route);
            Optional<AvailableTransportForRoute> maybeTransportOptions= availableTransportFinder.findTransport(route);
            if (maybeTransportOptions.isPresent()) {
                AvailableTransportForRoute routeTransportOption = maybeTransportOptions.get();
                System.out.println("---checking free seats");
                if (seatsCountChecker.hasFreeSeats(routeTransportOption, 3)) {
                    availableTransportOptions.add(freeSeatsRouteFilter.filter(routeTransportOption));
                }
            }
        }
        System.out.println("===composing routes");
        List<SingleConnectionTransportRoute> composedRoutes = routesComposer.compose(availableTransportOptions);
        ConnectionTimeTransportRouteFilter connectionTimeTransportRouteFilter = new ConnectionTimeTransportRouteFilter();
        System.out.println("===excluding too long and too short connections");
        composedRoutes = connectionTimeTransportRouteFilter.excludeTooShortAndTooLong(composedRoutes);
        System.out.println("**** done, printing ****\n\n");

        Collections.sort(composedRoutes, totalTravelTimeComparator());

        for (SingleConnectionTransportRoute composedRoute : composedRoutes) {
            System.out.println(composedRoute);
            System.out.println();
        }

    }

    public Comparator<SingleConnectionTransportRoute> totalTravelTimeComparator() {
        return new Comparator<SingleConnectionTransportRoute>() {
            @Override
            public int compare(SingleConnectionTransportRoute o1, SingleConnectionTransportRoute o2) {
                return o1.getTotalTravelTime().compareTo(o2.getTotalTravelTime());
            }
        };
    }
}