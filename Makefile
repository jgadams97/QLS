general:
	rm -rf com
	rm -rf res
	cp src/core/*.java src/pc/java/com/rmayco/qsim/
	cp -r src/core/res src/pc/
	cp -r src/core/res src/main/
	cp -r src/pc/java/com com
	cp -r src/pc/res res
	javac com/rmayco/qsim/*.java
	jar cmvf res/META-INF/MANIFEST.MF qsim.jar com/rmayco/qsim/* res
	rm -r com
	rm -r res
	rm -rf build
	rm -r src/main/res/META-INF/
	mkdir build
	mv qsim.jar build

android:
	cp src/core/*.java src/main/java/com/rmayco/qsim/
	git config --global user.name "rmayco"
	git add *
	git commit -m "`date`"
	git push --force -u origin master
	echo Finish building on AIDE...

clean:
	rm -r build

run:
	java -jar build/qsim.jar

