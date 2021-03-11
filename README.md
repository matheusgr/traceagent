To prepare JAR:
- mvn clean compile assembly:single

To trace the example app (Windows):
- java -javaagent:.\target\traceagent-1-jar-with-dependencies.jar ex2.Main

To trace the example app (Linux/Mac):
- java -javaagent:./target/traceagent-1-jar-with-dependencies.jar ex2.Main
