package com.phyriak.filter;

import java.lang.ScopedValue;

/**
 * Stores request-scoped context using {@link ScopedValue}.
 *
 * <p>Before Java 21, {@link ThreadLocal} was commonly used for this purpose.
 * {@link ScopedValue} automatically clears the context when the execution scope
 * ends, removing the need to manually call {@code ThreadLocal.remove()}.</p>
 */
public final class RequestContext {

    public static final ScopedValue<String> TRACE_ID =
            ScopedValue.newInstance();

    private RequestContext() {
    }


    public static String getTraceId() {
        return TRACE_ID.get();
    }

    /*
     * Legacy approach (pre-Java 21).
     *
     * private static final ThreadLocal<String> TRACE_ID =
     *         new ThreadLocal<>();
     */


    /*
     * Legacy ThreadLocal API.
     *
     * public static void set(String traceId) {
     *     TRACE_ID.set(traceId);
     * }
     */


    /*
     * Legacy ThreadLocal API.
     *
     * public static void clear() {
     *     TRACE_ID.remove();
     * }
     */
}