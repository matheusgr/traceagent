To prepare JAR:
- mvn clean compile assembly:single

To trace the example app:
- java -javaagent:traceagent.jar ex2.Main

To exclude packages from trace:
- java -javaagent:traceagent.jar="-e util.,easyaccept." ex2.Main