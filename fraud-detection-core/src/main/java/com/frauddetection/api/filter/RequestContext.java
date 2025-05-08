package com.frauddetection.api.filter;

import jakarta.enterprise.context.RequestScoped;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@RequestScoped
public class RequestContext {
    private String requestId;
}