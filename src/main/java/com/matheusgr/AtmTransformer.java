package com.matheusgr;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Collection;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;

public class AtmTransformer implements ClassFileTransformer {

	private TraceConfig traceConfig;

	public AtmTransformer(TraceConfig traceConfig) {
		this.traceConfig = traceConfig;
	}

	public boolean ignoreClass(String className) {
		className = className.replace("/", ".");
		Collection<String> includedPackages = this.traceConfig.includedPackages();
		if (!includedPackages.isEmpty()) {
			for (String pkg : includedPackages) {
				if (className.startsWith(pkg)) {
					return false;
				}
			}
			return true;
		}
		for (String pkg : this.traceConfig.excludedPackages()) {
			if (className.startsWith(pkg)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) {
		if (this.ignoreClass(className)) {
			return null;
		}
		try {
			ClassPool cp = ClassPool.getDefault();
			cp.insertClassPath(new LoaderClassPath(loader));
			CtClass cc = cp.get(className.replace("/", "."));
			for (CtMethod m : cc.getMethods()) {
				// Ignore methods that are not overloaded from ignored packages. Example:
				// java.lang.Object.wait.
				if (this.ignoreClass(m.getLongName())) {
					continue;
				}
				if (!m.isEmpty()) {
					addMethodOperation(m);
				}
			}
			cc.detach();
			return cc.toBytecode();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void addMethodOperation(CtMethod m)
			throws CannotCompileException {
		String id = Modifier.isStatic(m.getModifiers()) ? "\"static\"" : "System.identityHashCode(this)";
		String msgTemplate = "System.out.println(" + "\"[TRACEAGENT] \" + " + id + " + \" [%s] " + m.getLongName() + " \" + System.currentTimeMillis());";
		m.insertBefore(msgTemplate.formatted("START"));
		m.insertAfter(msgTemplate.formatted("END"), true);
	}

}