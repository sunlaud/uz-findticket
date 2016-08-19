package io.github.sunlaud.findticket.client.uz.util;

import com.jjencoder.JJencoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public class AuthService {
    private Pattern tokenEncodedDataPattern = Pattern.compile("\\$\\$_=.*~\\[\\];.*\"\"\\)\\(\\)\\)\\(\\);");
    private Pattern tokenPattern = Pattern.compile("[0-9a-f]{32}");
    private String token = null;
    private final Supplier<String> contentProvider;

    public AuthService(Supplier<String> contentProvider) {
        this.contentProvider = contentProvider;
    }

    synchronized public String getToken() {
        if (token == null) {
            refresh();
        }
        return token;
    }

    @SneakyThrows
    public void refresh() {
        token = findToken(contentProvider.get());
    }

    private String findToken(String rootPageContent) throws IOException {
        Matcher matcher = tokenEncodedDataPattern.matcher(rootPageContent);
        if (!matcher.find()) {
            throw new IllegalStateException("Can't find encoded token data");
        }
        String encodedTokenData = rootPageContent.substring(matcher.start(), matcher.end());
        log.debug("encodedTokenData={}", encodedTokenData);
        String decodedTokenData = new JJencoder().decode(encodedTokenData);
        log.debug("decodedTokenData={}", decodedTokenData);
        matcher = tokenPattern.matcher(decodedTokenData);
        if (!matcher.find()) {
            throw new IllegalStateException("Can't find token in decoded token data");
        }
        String token = decodedTokenData.substring(matcher.start(), matcher.end());
        log.info("Got new token: {}", token);
        return token;
    }
}