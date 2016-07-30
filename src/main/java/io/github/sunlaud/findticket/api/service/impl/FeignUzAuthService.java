package io.github.sunlaud.findticket.api.service.impl;

import feign.Response;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


/**
 * Implementation of {@link io.github.sunlaud.findticket.api.service.TicketSearchService}
 * using Netfix Feign library
 */
public interface FeignUzAuthService {
    @GET
    @Path("/")
    Response getRootResource();
}
