package com.orpheum.orchestrator.backstage.portal;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class ApiFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            //logRequest(httpRequest);

            chain.doFilter(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void logRequest(HttpServletRequest request) {
        log.info("Incoming Request: [{}] {}, {}", request.getMethod(), request.getRequestURI(), request.getParameterMap().entrySet().stream().map(entry -> entry.getKey() + "=" + Stream.of(entry.getValue()).collect(Collectors.joining(","))).collect(Collectors.joining(",")));
        request.getHeaderNames().asIterator().forEachRemaining(header ->
                log.info("Header: {} = {}", header, request.getHeader(header))
        );
    }
}
