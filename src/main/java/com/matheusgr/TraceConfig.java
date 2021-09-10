package com.matheusgr;

import java.util.HashSet;
import java.util.Set;

public class TraceConfig {

	private Set<String> excludedPackages;
	private Set<String> includedPackages;

	public TraceConfig() {
		this.excludedPackages = new HashSet<>();
		this.includedPackages = new HashSet<>();
		for (String pkg : "java.,sun.,jdk.".split(",")) {
			this.excludedPackages.add(pkg);
		}
	}

	public Set<String> excludedPackages() {
		return this.excludedPackages;
	}

	public Set<String> includedPackages() {
		return this.includedPackages;
	}

    public void configureExcludedPackages(String pkgs) {
		for (String pkg : pkgs.split(",")) {
			this.excludedPackages.add(pkg);
		}
    }

	public void configureIncludedPackages(String pkgs) {
		for (String pkg : pkgs.split(",")) {
			this.includedPackages.add(pkg);
		}
    }

}