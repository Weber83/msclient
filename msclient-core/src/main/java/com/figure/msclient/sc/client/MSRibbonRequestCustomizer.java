package com.figure.msclient.sc.client;

import java.util.List;

/**
 * Created by chuanbo.wei on 2017/3/19.
 */
public interface MSRibbonRequestCustomizer<B> {

    boolean accepts(Class builderClass);
    void customize(B builder);

    class Runner {

        @SuppressWarnings("unchecked")
        public static void customize(List<MSRibbonRequestCustomizer> customizers, Object builder) {
            for (MSRibbonRequestCustomizer customizer : customizers) {
                if (customizer.accepts(builder.getClass())) {
                    customizer.customize(builder);
                }
            }
        }
    }
}
