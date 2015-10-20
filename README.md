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

	To create a new function use the following syntax:
		:[functionName]()
	If functionName has already been created, you will be prompted to run the function instead.
	Alternatively, the syntax
		:<type> [functionName]() 
	can be used to create a function of type <type>. If syntax 1 is used, the user will have to enter
	a type while defining the function. 
	Please note that through syntax 2 not all types are supported! 
	Only the following typed functions can be created:
		*int
		*char
		*double
		*float
		*boolean
		*String
	While defining the function, only enter the content between and including the {} in a function
	definition
	-------------------------------------------------------------------------------------------------------
	To create a new class use the following syntax:
		::[className]
	If className has already been created, a message stating so will appear. 
	While defining the class, only enter the content between the {} in a class definition, including the 
	closing brace, ommiting the opening one
	-------------------------------------------------------------------------------------------------------
	If you make an error while typing in a command, don't worry about! JRepl will automatically rollback 
	to before you entered that command.
	However, if you make a mistake while declaring a function or a class, there is currently no support to
	rollback, and hence, the program must be restarted.
	Furthermore, all print statements will be deleted after they are run once, but operations, assignments
	and other functions will persist within a session so that you can still use your variable across 
	commands.
	-------------------------------------------------------------------------------------------------------
	Some features that I want to implement in the future:
		*A faster compiling system
		*Cleaner syntax for class and function definitions
		*View command history
		*Better system for handling variables
		*Option to save work to file
		*Support for non-windows platforms
		*Error deletion for functions and classes
Author: Aneesh Durg
	
>v1.0 made at BoilerMake2015

