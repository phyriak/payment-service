package com.phyriak.filter;

/**
 * Holds request-scoped data using {@link ThreadLocal}.
 *
 * <p>Provides access to contextual information (e.g. trace ID)
 * for the current thread during request processing.</p>
 */
public class RequestContext {

    private static final ThreadLocal<String> TRACE_ID =
            new ThreadLocal<>();

    public static void set(String traceId) {
        TRACE_ID.set(traceId);
    }

    public static String getTradeId() {
        return TRACE_ID.get();
    }

    public static void clear() {
        TRACE_ID.remove();
    }

}