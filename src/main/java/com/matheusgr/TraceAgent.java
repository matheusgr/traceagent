package com.matheusgr;

import java.lang.instrument.Instrumentation;

public class TraceAgent {
	public static void premain(String agentArgs, Instrumentation inst) {
		TraceConfig traceConfig = new TraceConfig();
		AtmTransformer atmTransformer = new AtmTransformer(traceConfig);
		inst.addTransformer(atmTransformer, true);
	}

}
