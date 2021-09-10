package com.matheusgr;

import java.lang.instrument.Instrumentation;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class TraceAgent {
	public static void premain(String agentArgs, Instrumentation inst) throws ParseException {
		if (agentArgs == null) {
			agentArgs = "";
		}
		TraceConfig traceConfig = new TraceConfig();

		Options options = new Options();
		options.addOption("e", true, "exclude packages separated by comma");
		
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, agentArgs.split(" "));

		if (cmd.hasOption("e")) {
			String packages = cmd.getOptionValue("e");
			traceConfig.configurePackages(packages);
		}

		AtmTransformer atmTransformer = new AtmTransformer(traceConfig);
		inst.addTransformer(atmTransformer, true);
	}

}
