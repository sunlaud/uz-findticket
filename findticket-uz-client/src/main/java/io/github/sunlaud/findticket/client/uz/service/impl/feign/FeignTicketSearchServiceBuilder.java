package io.github.sunlaud.findticket.client.uz.service.impl.feign;


import com.fasterxml.jackson.databind.ObjectMapper;
import feign.*;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jaxrs.JAXRSContract;
import feign.slf4j.Slf4jLogger;
import io.github.sunlaud.findticket.client.uz.Apis;
import io.github.sunlaud.findticket.client.uz.response.deserialize.SearchResponseInstantiationProblemHandler;
import io.github.sunlaud.findticket.client.uz.service.UzTicketSearchService;
import io.github.sunlaud.findticket.client.uz.util.Utils;
import lombok.SneakyThrows;

import java.lang.reflect.Type;

public class FeignTicketSearchServiceBuilder {

    public static UzTicketSearchService getTicketSearchService() {
        return getTicketSearchService(new Client.Default(null, null));
    }

    @SneakyThrows
    public static UzTicketSearchService getTicketSearchService(Client client) {
        HeadersInterceptor requestInterceptor = new HeadersInterceptor();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setConfig(mapper.getDeserializationConfig()
                .withHandler(new SearchResponseInstantiationProblemHandler()));

        return Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .encoder(new UrlEncodingEncoder())
                .decoder(new JacksonDecoder(mapper))
                .contract(new JAXRSContract())
                .requestInterceptor(requestInterceptor)
                .errorDecoder(new IllegalArgumentExceptionOn503Decoder())
                .client(client)
                .target(UzTicketSearchService.class, Apis.BASE_URL);
    }


    private static class HeadersInterceptor implements RequestInterceptor {

        @Override
        public void apply(RequestTemplate template) {
            template.header("GV-Ajax", "1");
            template.header("GV-Screen", "1366x768");
            template.header("Content-Type", "application/x-www-form-urlencoded");
            template.header("GV-Referer", Apis.BASE_URL);
            template.header("Referer", Apis.BASE_URL);
            template.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            template.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
        }
    }


    private static class UrlEncodingEncoder implements Encoder {

        @Override
        public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
            template.body(Utils.asUrlEncodedString(object));
        }

    }

    static class IllegalArgumentExceptionOn503Decoder implements ErrorDecoder {
        private final Default defaultDecoder = new ErrorDecoder.Default();

        @Override
        public Exception decode(String methodKey, Response response) {
            //Strange UZ API seems to respond by 503 to any client error
            if (response.status() == 503)
                throw new IllegalArgumentException("Looks like Bad Request (UZ API seems to respond by 503 to any client error)");
            return defaultDecoder.decode(methodKey, response);
        }

    }
}
