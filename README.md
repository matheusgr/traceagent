To prepare JAR:
- mvn clean compile assembly:single

To run your app:
- java -javaagent:.\target\traceagent-1-jar-with-dependencies.jar ex2.Main