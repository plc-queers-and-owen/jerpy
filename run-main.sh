javac -d build $(find src -name "*.java")
java -cp build Jott $1