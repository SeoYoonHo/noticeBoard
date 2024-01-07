package com.study.boardExample.config;

import brave.internal.codec.HexCodec;
import brave.propagation.Propagation;
import brave.propagation.TraceContext;

import java.util.Arrays;
import java.util.List;

//@Component
class CustomPropagator extends Propagation.Factory implements Propagation<String> {

    @Override
    public List<String> keys() {
        return Arrays.asList("myCustomTraceId", "myCustomSpanId");
    }

    @Override
    public <R> TraceContext.Injector<R> injector(Setter<R, String> setter) {
        return (traceContext, request) -> {
            setter.put(request, "myCustomTraceId", traceContext.traceIdString());
            setter.put(request, "myCustomSpanId", traceContext.spanIdString());
        };
    }

    @Override
    public <R> TraceContext.Extractor<R> extractor(Getter<R, String> getter) {
        return request -> TraceContextOrSamplingFlags.create(TraceContext.newBuilder()
                                                                         .traceId(HexCodec.lowerHexToUnsignedLong(getter.get(request, "myCustomTraceId")))
                                                                         .spanId(HexCodec.lowerHexToUnsignedLong(getter.get(request, "myCustomSpanId"))).build());
    }

    /**
     * @deprecated end users and instrumentation should never call this, and instead use
     * {@link #get()}. This only remains to avoid rev-lock upgrading to Brave 6.
     */
    @Deprecated
    @Override
    public <K> Propagation<K> create(Propagation.KeyFactory<K> ignored) {
        throw new UnsupportedOperationException("As of Brave 5.12, call PropagationFactory.get()");
    }

    @Override
    public Propagation<String> get() {
        return this;
    }

}