JDK_BIN = /usr/lib/jdk1.8.0_101/bin

all:
	mkdir -p out
	${JDK_BIN}/javac -cp lib/commons-cli-1.4.jar -d out -sourcepath src src/Main.java
	${JDK_BIN}/jar cmf MANIFEST.MF httpd.jar -C out .

clear:
	-rm out/*.class
	-rm httpd.jar
