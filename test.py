import subprocess
from os import sep

run = lambda x: print("RUNNING... " + x) or subprocess.run(x, shell=True, check=True)

print("MVN...")
run("mvn clean compile assembly:single")

print("JAVAC...")
run("javac ex1" + sep + "*.java")
run("javac ex2" + sep + "*.java")

print("=== AGENT...")
java_cmd = "java -javaagent:target" + sep + "traceagent-1-jar-with-dependencies.jar"
print("=== NO ARGS...")

run(java_cmd + " ex2.Main")

print("=== EXCLUDING PKG... EX1")
run(java_cmd + "=\"-e util.,easyaccept.,ex1.\" ex2.Main")

print("=== INCLUDING PKG... EX1")
run(java_cmd + "=\"-i ex1.\" ex2.Main")