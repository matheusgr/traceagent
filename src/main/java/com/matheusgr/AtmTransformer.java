package com.matheusgr;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class AtmTransformer implements ClassFileTransformer {

  private Instrumentation inst;

  public AtmTransformer(Instrumentation inst) {
    this.inst = inst;
  }

  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain, byte[] classfileBuffer) {
    if (className.startsWith("java/") || className.startsWith("sun/") || className.startsWith("jdk/")) {
      return null;
    }
    System.out.println("CLASS: " + className);
    try {
      ClassPool cp = ClassPool.getDefault();
      cp.insertClassPath(".");
      CtClass cc = cp.get(className.replaceAll("/", "."));
      System.out.println(cc.getName());
      System.out.println(cc.getMethods());
      for (CtMethod m : cc.getMethods()) {
        System.out.println("TRANSFORMING: " + className + " -- " + m.getLongName());
        if (m.getLongName().startsWith("java.")) {
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

  private void addMethodOperation(String className, CtClass cc, CtMethod m)
      throws CannotCompileException, IOException {
    byte[] byteCode;
    m.addLocalVariable("startTime", CtClass.longType);
    m.insertBefore("startTime = System.currentTimeMillis();");

    StringBuilder endBlock = new StringBuilder();

    m.addLocalVariable("endTime", CtClass.longType);
    m.addLocalVariable("opTime", CtClass.longType);
    endBlock.append("endTime = System.currentTimeMillis();");
    endBlock.append("opTime = (endTime-startTime)/1000;");

    endBlock.append(
        "System.out.println(\"[Application] " + m.getLongName() + " time:" + "\" + opTime + \" seconds!\");");

    m.insertAfter(endBlock.toString());
  }
}