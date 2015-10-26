title Jrepl - A read-eval-print-loop
echo off
cls
if "%1"=="" GOTO 2
if %1==-update GOTO 1
if %1==-updateo GOTO 0
GOTO 2
:0
	javac Jrepl.java
	exit	
:1
	javac Jrepl.java
:2
	java Jrepl


