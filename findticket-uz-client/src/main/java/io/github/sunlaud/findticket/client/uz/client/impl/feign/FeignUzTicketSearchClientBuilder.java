package io.github.sunlaud.findticket.client.uz.client.impl.feign;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import feign.*;
import feign.Response;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jaxrs.JAXRSContract;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import io.github.sunlaud.findticket.client.uz.client.Apis;
import io.github.sunlaud.findticket.client.uz.response.deserialize.SearchResponseInstantiationProblemHandler;
import io.github.sunlaud.findticket.client.uz.client.UzTicketSearchClient;
import io.github.sunlaud.findticket.client.uz.util.Utils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.http.RealResponseBody;
import okio.Buffer;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Slf4j
public class FeignUzTicketSearchClientBuilder {

    private static final boolean USE_CACHE = true;

    public static UzTicketSearchClient getTicketSearchService() {
//        Client client = new Client.Default(null, null);
        final File cacheRootDir = new File("/tmp/uz-findtiket/cache");
        okhttp3.OkHttpClient.Builder clientBuilder = new okhttp3.OkHttpClient.Builder();

        if (USE_CACHE) {
            clientBuilder
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            okhttp3.Request request = chain.request();
                            String responseCacheDirName = getRequestHash(request);
                            Path responseCacheDir = cacheRootDir.toPath().resolve(responseCacheDirName);

                            okhttp3.Response response;
                            if (responseCacheDir.toFile().exists()) {
                                try (InputStream bodyIs = new BufferedInputStream(Files.newInputStream(responseCacheDir.resolve("body")));
                                     BufferedReader metaInfoReader = Files.newBufferedReader(responseCacheDir.resolve("metainfo"), StandardCharsets.UTF_8);
                                ) {
                                    ByteArrayOutputStream bodyOs = new ByteArrayOutputStream();
                                    IOUtils.copy(bodyIs, bodyOs);
                                    Buffer bodyBuffer = new Buffer();
                                    bodyBuffer.write(bodyOs.toByteArray());

                                    String url = metaInfoReader.readLine();
                                    String statusCode = metaInfoReader.readLine();

                                    okhttp3.Headers.Builder headersBuilder = new okhttp3.Headers.Builder();
                                    for (String header; (header = metaInfoReader.readLine()) != null; headersBuilder.add(header))
                                        ;

                                    response = new okhttp3.Response.Builder()
                                            .request(request)
                                            .body(new RealResponseBody(headersBuilder.build(), bodyBuffer))
                                            .protocol(Protocol.HTTP_1_1)
                                            .code(Integer.valueOf(statusCode))
                                            .build();
                                    log.info("Resolved from cache: {}", request);
                                }
                            } else {
                                response = chain.proceed(request);
                                responseCacheDir.toFile().mkdirs();
                                try (OutputStream bodyOs = new BufferedOutputStream(Files.newOutputStream(responseCacheDir.resolve("body")));
                                     PrintWriter metaInfoWriter = new PrintWriter(responseCacheDir.resolve("metainfo").toFile(), "UTF-8");
                                ) {
                                    metaInfoWriter.println(request.url().toString());
                                    metaInfoWriter.println(String.valueOf(response.code()));
                                    for (Map.Entry<String, List<String>> nameValue : response.headers().toMultimap().entrySet()) {
                                        metaInfoWriter.printf("%s:%s\n", nameValue.getKey(), nameValue.getValue());
                                    }

                                    byte[] bodyBytes = response.body() == null ? new byte[0] : response.body().bytes();
                                    if (bodyBytes.length > 0) {
                                        IOUtils.copy(new ByteArrayInputStream(bodyBytes), bodyOs);
                                        Buffer bodyBuffer = new Buffer();
                                        bodyBuffer.write(bodyBytes);
                                        response = response.newBuilder().body(new RealResponseBody(response.headers(), bodyBuffer)).build();
                                    }
                                } catch (Exception ex) {
                                    responseCacheDir.toFile().delete();
                                    throw ex;
                                }
                            }
                            return response;
                        }
                    });
        }

        okhttp3.OkHttpClient okHttpClient = clientBuilder.build();

        Client client = new OkHttpClient(okHttpClient);
        return getTicketSearchService(client);
    }

    private static String getRequestHash(okhttp3.Request request) throws IOException {
        Hasher hasher = Hashing.sha1().newHasher();
        hasher
            .putString(request.url().toString(), Charsets.UTF_8)
            .putString(request.method(), Charsets.UTF_8);
        if (request.body() != null) {
            Buffer buffer = new Buffer();
            request.newBuilder().build().body().writeTo(buffer);
            String bodyString = buffer.readString(Charsets.UTF_8);
            hasher.putString(bodyString, Charsets.UTF_8);
        }
        return hasher.hash().toString();
    }

    @SneakyThrows
    public static UzTicketSearchClient getTicketSearchService(Client client) {
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
                .target(UzTicketSearchClient.class, Apis.BASE_URL);
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
