package io.github.sunlaud.findticket.advancedplanner;

import io.github.sunlaud.findticket.client.uz.UzTrainRouteSearchService;
import io.github.sunlaud.findticket.model.Station;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

@Ignore("integration")
public class RouteCreatorTest {

    @Test
    public void foo() {
        String[] stations = {
                "Бердичів-Житом.",
                "Бердянськ",
                "Вінниця",
                "Горлівка",
                "Дніпро-Головний",
                "Житомир",
                "Запоріжжя 1",
                "Івано-Франківськ",
                "Київ",
                "Кривий Ріг-Гол.",
                "Луцьк",
                "Львів",
                "Микитівка",
                "Миколаїв Пас.",
                "Одеса",
                "Полтава Київська",
//                "Полтава-Півд.",
                "Рівне",
                "Суми",
                "Тернопіль",
                "Ужгород",
                "Харків",
                "Херсон",
                "Хмельницький",
        };


        String departureStationName = "Тернопіль";
        String arrivalStationName = "Запоріжжя 1";

        UzTrainRouteSearchService routeSearchService = new UzTrainRouteSearchService();
        RouteChecker routeChecker = new RouteChecker(routeSearchService);
        RouteCreator sut = new RouteCreator(stations, new StationsCache(routeSearchService));
        RouteSeatsCountChecker seatsCountChecker = new RouteSeatsCountChecker(routeSearchService);
        List<Route<Station>> routes = sut.findRoutes(departureStationName, arrivalStationName);
        for (Route<Station> route : routes) {
//            System.out.println("checking route: " + route);
            ComplexRoute complexRoute = routeChecker.check(route);

            if (complexRoute != null && seatsCountChecker.hasFreeSeats(complexRoute)) {
                System.out.println(complexRoute);
            }
        }


        System.out.println(routes);
    }
}