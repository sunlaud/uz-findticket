package io.github.sunlaud.findticket.api.service.impl.feign;

import feign.Response;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public interface FeignUzRootContentProvider {
    @GET
    @Path("/")
    Response getRootResource();
}
