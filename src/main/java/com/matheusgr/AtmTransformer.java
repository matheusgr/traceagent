package com.matheusgr;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;

public class AtmTransformer implements ClassFileTransformer {

  private TraceConfig traceConfig;

  public AtmTransformer(TraceConfig traceConfig) {
    this.traceConfig = traceConfig;
  }

  public boolean ignoreClass(String className) {
    className = className.replaceAll("/", ".");
    for (String pkg : this.traceConfig.ignoredPackages()) {
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
    System.out.println("CLASS: " + className);
    try {
      ClassPool cp = ClassPool.getDefault();
      cp.insertClassPath(new LoaderClassPath(loader));
      CtClass cc = cp.get(className.replaceAll("/", "."));
      addIdField(cc);
      for (CtMethod m : cc.getMethods()) {
        // Ignore methods that are not overloaded from ignored packages. Example: java.lang.Object.wait.
        if (this.ignoreClass(m.getLongName())) {
          continue;
        }
        addMethodOperation(className, cc, m);
      }
      cc.detach();
      return cc.toBytecode();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private void addIdField(CtClass cc) throws CannotCompileException {
	cc.addField(new CtField(CtClass.doubleType, "traceObjectId", cc), "Math.random()");	
}

private void addMethodOperation(String className, CtClass cc, CtMethod m)
      throws CannotCompileException, IOException {
    m.addLocalVariable("traceAgentStartTime", CtClass.longType);
    String id = Modifier.isStatic(m.getModifiers()) ? "\"static\"" : "traceObjectId";
    m.insertBefore("System.out.println(" + id + " + \" [START] " + m.getLongName() + "\");");   	
    m.insertBefore("traceAgentStartTime = System.currentTimeMillis();");
    m.insertAfter("System.out.println(" + id + " + \" [END] " + m.getLongName() + " time: " + "\" + (System.currentTimeMillis() - traceAgentStartTime) + \" ms!\");");
  }

}