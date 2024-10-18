# JERPY

# Environment:

To correctly execute, the host system MUST be running Java 21.

# Running:
Compile all the java files, then either run the desired tester or run the `Jott` class on the desired file.
On Linux (posix-compliant shells), the following commands can be used:

```bash
javac -d build $(find ./src -name "*.java") # Build all java files to build/
java -cp build testers.JottParserTester # Run the desired class (ie testers.JottParserTester)
```

The run scripts in `binaries/` can also be used to run the code, although I haven't been able to test them on multiple platforms so YMMV

# Original README

This file contains provided code, testers, and test cases.

provided should be a top level package in your src directory
JottParserTester and JottTokenizerTester classes should be in a package call testers.

the testCases directories should be in the working directory of your project. 
