package io.github.sunlaud.findticket.api.service.impl.feign;


import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jaxrs.JAXRSContract;
import feign.slf4j.Slf4jLogger;
import io.github.sunlaud.findticket.api.Apis;
import io.github.sunlaud.findticket.api.service.TicketSearchService;
import io.github.sunlaud.findticket.api.util.AuthService;
import io.github.sunlaud.findticket.api.util.Utils;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.stream.Collectors;

public class FeignTicketSearchServiceBuilder {

    @SneakyThrows
    public static TicketSearchService getTicketSearchService() {
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


    public static class HeadersInterceptor implements RequestInterceptor {
        private final String cookie;
        private final AuthService authService;

        public HeadersInterceptor(AuthService authService, String cookie) {
            this.authService = authService;
            this.cookie = cookie;
        }

        @Override
        public void apply(RequestTemplate template) {
            if (!"/".equals(template.url())) {
                template.header("GV-Token", authService.getToken());
            }
            template.header("GV-Ajax", "1");
            template.header("GV-Screen", "1366x768");
            template.header("Content-Type", "application/x-www-form-urlencoded");
            template.header("GV-Referer", Apis.BASE_URL);
            template.header("Referer", Apis.BASE_URL);
            template.header("Cookie", cookie);
            template.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            template.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
        }
    }


    public static class UrlEncodingEncoder implements Encoder {

        @Override
        public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
            template.body(Utils.asUrlEncodedString(object));
        }

    }
}
