# JRepl
![Jrepl](http://i.imgur.com/u7l4q2G.png "Jrepl")

Jrepl - A read-eval-print-loop for Java 

Why?

	A read–eval–print loop (REPL), also known as an interactive toplevel or language shell, is a 
	simple, interactive computer programming environment that takes single user inputs 
	(i.e. single expressions), evaluates them, and returns the result to the user; 
	a program written in a REPL environment is executed piecewise. 
														  -Wikipedia

	REPLs are great for testing out some logic quickly and consicly. 
	Why waste time compiling something you don't need?
	In java if you need to test a simple for loop or a mere piece of code, 
	it's quite a hassel to create a new file, ensuring a valid class name, a main function, etc. 
	
What and How?

	This project is intended to create a read-eval-print-loop for java with some additional features.
	Jrepl intends to create a REPL environment which takes care of some of the less exciting 
	aspects of writing a quick test program in Java by creating and compiling temporary files for you.
	
Some instructions and notes.
	
	JRepl requires java 1.7 or above. You can check your java version by running:
			java -version
	from the command line.
	-------------------------------------------------------------------------------------------------------
	To view a list of commands and their usage, as well as a brief over view of Jrepl specific syntax, 
	start Jrepl and use the command 
			-help		
	-------------------------------------------------------------------------------------------------------	
	JRepl can be launched on windows with Jrepl.bat or by running java Jrepl from the command line.
	If you edit Jrepl.java, you can compile and run by using:

			javac Jrepl.java
			java Jrepl
		or
			jrepl -update
		or 
			jrepl -updateo
			(if you only want to compile and not run)
		
	-------------------------------------------------------------------------------------------------------
	If you make an error while typing in a command, don't worry about it! JRepl will automatically rollback 
	to before you entered that command.
	However, if you make a mistake while declaring a function or a class, there is currently no support to
	rollback, and hence, the program must be restarted.
	Furthermore, all print statements will be deleted after they are run once, but operations, assignments
	and other functions will persist within a session so that you can still use your variable across 
	commands.
	-------------------------------------------------------------------------------------------------------
	Some features that I want to implement in the future:
		*A faster compiling system
		*View command history
		*Better system for handling variables
		*Option to save work to file
Author: Aneesh Durg
	
>v1.0 made at BoilerMake2015

