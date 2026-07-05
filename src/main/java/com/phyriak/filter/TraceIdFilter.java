package com.phyriak.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Initializes request-scoped context before request processing.
 *
 * <p>Creates or retrieves a trace identifier, stores it in {@link RequestContext}
 * using {@link ThreadLocal}, and guarantees cleanup after request completion
 * to prevent context leakage between reused threads.</p>
 */
@Component
public class TraceIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {

            String traceId = request.getHeader("X-Trace-Id");

            if (traceId == null || traceId.isBlank()) {
                traceId = UUID.randomUUID().toString();
            }

            RequestContext.set(traceId);

            filterChain.doFilter(request, response);

        } finally {

            RequestContext.clear();
        }
    }
}