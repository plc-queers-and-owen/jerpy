
main: comp_dir tTC

tTC: comp_dir/tokenizerTestCases
pTC: comp_dir/parserTestCases
p3TC: comp_dir/phase3testcases

$(p3TC): comp_dir
	cp -r ../phase3testcases comp_dir/

$(pTC): comp_dir
	cp -r ../parserTestCases comp_dir/

$(tTC): comp_dir
	cp -r ../tokenizerTestCases comp_dir/

comp_dir:
	javac testers/JottTokenizerTester.java internal/* provided/* -d comp_dir

clean: 
	rm -r comp_dir
