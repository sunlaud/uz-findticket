package io.github.sunlaud.findticket.api.service.impl.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.github.sunlaud.findticket.api.Apis;
import io.github.sunlaud.findticket.api.util.AuthService;

public class HeadersInterceptor implements RequestInterceptor {
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
        template.header("Content-Type", "application/x-www-form-urlencoded");
        template.header("Referer", Apis.BASE_URL);
        template.header("Cookie", cookie);
    }
}
