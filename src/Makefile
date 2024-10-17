tTC=build/tokenizerTestCases
pTC=build/parserTestCases
p3TC=build/phase3testcases

main: compile_src $(tTC) $(pTC)


$(p3TC): build
	cp -r ../phase3testcases build/

$(pTC): build
	cp -r ../parserTestCases build/

$(tTC): build
	cp -r ../tokenizerTestCases build/

compile_src: compile_provided compile_internal compile_testers 
compile_testers: build
	javac -d build testers/*.java 

compile_internal: build
	javac -d build internal/*.java
	javac -d build internal/nodes/*.java

compile_provided: build
	javac -d build provided/*


build:
	mkdir build

clean: 
	rm -r build
