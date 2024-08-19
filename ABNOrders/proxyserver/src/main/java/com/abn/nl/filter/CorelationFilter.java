package com.abn.nl.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorelationFilter implements Filter {
    private static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String correlationId = ((HttpServletRequest) servletRequest).getHeader(CORRELATION_ID_HEADER_NAME);
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }

        MDC.put(CORRELATION_ID_HEADER_NAME, correlationId);

        try {
            filterChain.doFilter(httpServletRequest, servletResponse);
        } finally {
            MDC.remove(CORRELATION_ID_HEADER_NAME);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
