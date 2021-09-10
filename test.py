import subprocess
from os import sep

run = lambda x: subprocess.run(x, shell=True, check=True)

print("MVN...")
run("mvn clean compile assembly:single")  # -B

print("JAVAC...")
run("javac ex1\*.java")
run("javac ex2\*.java")

print("=== AGENT...")
jarfile = "target" + sep + "traceagent-1-jar-with-dependencies.jar"

print("=== NO ARGS...")

run("java -javaagent:" + jarfile + " ex2.Main")

print("=== EXCLUDING PKG... EX1")
run("java -javaagent:" + jarfile + "=\"-e util.,easyaccept.,ex1.\" ex2.Main")