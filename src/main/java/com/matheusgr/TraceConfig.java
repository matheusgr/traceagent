package com.matheusgr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TraceConfig {

	private Map<String, String> cfg;
	private Set<String> ignoredPackages;

	public TraceConfig() {
		this.ignoredPackages = new HashSet<>();
		this.cfg = new HashMap<>();
		this.cfg.put("ignoredPkgs", "java.,sun.,jdk.");
		for (String pkg : cfg.get("ignoredPkgs").split(",")) {
			this.ignoredPackages.add(pkg);
		}
	}

	public Set<String> ignoredPackages() {
		return this.ignoredPackages;
	}

    public void configurePackages(String pkgs) {
		for (String pkg : pkgs.split(",")) {
			this.ignoredPackages.add(pkg);
		}
    }

}