package com.matheusgr;

import java.lang.instrument.Instrumentation;

public class TraceAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        AtmTransformer atmTransformer = new AtmTransformer(inst);
        inst.addTransformer(atmTransformer, true);
    }

}
