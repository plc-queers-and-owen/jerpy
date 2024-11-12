javac -d build $(find src -name "*.java")

for file in phase3testcases/*
do
  echo -e "$file:\n"
  java -cp build Jott $file
  echo "---"
done
