import java.util.*;
import java.io.*;
import java.nio.file.Paths;
import utils.CleanDir;
import utils.InputTxt;

public class Jrepl
{
	//some useful Strings
	static String header="\nJrepl v.2.8 - A read-eval-print-loop for java \n";
	static String path=Paths.get(".").toAbsolutePath().normalize().toString();

	//main method, just cleans the directory, displays a "splash screen" and starts the actual repl
	public static void main(String[] args) throws Exception
	{
		CleanDir cleaner=new CleanDir();
		cleaner.run();	

		System.out.println(header);
		System.out.println("\tRun -about to learn more about this project");
		System.out.println("\tRun -help for a list of JRepl specific commands and example usage");

		repl();
	}
	
	//the REPL
	public static void repl() throws Exception
	{
		//create all files, initialize some strings and LinkedList to hold function names
		PrintWriter pw = new PrintWriter("Test.java");
		pw.close();
		pw = new PrintWriter("functions.java");
		pw.close();
		pw = new PrintWriter("results.txt");
		pw.close();
		pw = new PrintWriter("compileerrs.txt");
		pw.close();
		
		String file="import utils.CleanDir;\nimport utils.InputTxt;\npublic class Test{\npublic static void main(String[] args)throws Exception{\n";
		Scanner input=new Scanner(System.in);
		String cmd;
		String functions="";
		String classes="";
		String imports="";
		String altfunction="";
		String altclass="";
		String loadedfunctions="";
		String loadedclasses="";
		String loadedimports="";
		String temp="";
		String dontprintme="class Dontprintme{}";
		LinkedList <String> functionlist= new LinkedList<>();
		LinkedList <String> classlist= new LinkedList<>();
		LinkedList <String> classnames= new LinkedList<>();
		LinkedList <String> loadedfiles= new LinkedList<>();

		//main loop
		while(true)
		{
			//prompt and getting input
			System.out.print("> ");
			input=new Scanner(System.in);
			cmd="";
			cmd=input.nextLine();
			System.out.println();
			
			//Storing functions in a string
			try
			{
				functions=readfile("functions.java");
			}
			catch (Exception e)
			{
				pw = new PrintWriter("functions.java");
				pw.close();
			}

			//Storing classes in a string
			try
			{
				classes=readfile("classes.java");
			}
			catch (Exception e)
			{
				pw = new PrintWriter("classes.java");
				pw.close();
			}

			//ensuring that the program doesn't break if user hits enter without typing
			if(cmd.isEmpty())
			{
				cmd="";
			}

			//checks for class declaration
			else if(cmd.startsWith("::"))
			{
				if(classes==null)
				{
					classes="";
				}
				else
				{

					if(classes.contains(cmd.substring(2, cmd.length())))
					{
						System.out.println("Class already exists!");
						cmd="";
					}
					else
					{
						System.out.println("Please define the class: ");

						//adds the class to a list to make it searchable
						classlist.add(cmd.substring(2, cmd.length()));
						//creates a second class without print statements
						altclass="class "+cmd.substring(2, cmd.length())+"Dontprintme{";
						cmd="class "+cmd.substring(2, cmd.length())+"{";
						


						while(left(cmd)!=right(cmd))
						{
							layeredprompt(cmd);
							input=new Scanner(System.in);
							temp=input.nextLine();
							cmd+=temp;
							if(temp.contains(" "+classlist.get(classlist.size()-1)))
							{
								
								//creates the constructor for the altclass as well
								altclass+=temp.substring(0, temp.indexOf(classlist.get(classlist.size()-1)))+classlist.get(classlist.size()-1)+"Dontprintme"
									+temp.substring(temp.indexOf("("), temp.length());

							}
							else
								altclass+=temp;			
						}
						
						if(cmd.contains(classlist.get(classlist.size()-1)+"("))
							//creates an alternate constructor with no print statements
							cmd=cmd.substring(0, cmd.length()-1)
							+cmd.substring((cmd.lastIndexOf("}", cmd.indexOf(classlist.get(classlist.size()-1)+"("))>-1?
								cmd.lastIndexOf("}", cmd.indexOf(classlist.get(classlist.size()-1)+"(")):cmd.indexOf("{", cmd.indexOf("class "+classlist.get(classlist.size()-1))))+1, 
									cmd.indexOf(classlist.get(classlist.size()-1)+"("))
							+classlist.get(classlist.size()-1)+"("
							+(cmd.indexOf(")", cmd.indexOf("(", cmd.indexOf(classlist.get(classlist.size()-1)+"(")))==cmd.indexOf("(", cmd.indexOf(classlist.get(classlist.size()-1)+"("))+1?
											("Dontprintme a"+noprint(cmd.substring(cmd.indexOf("(", cmd.indexOf(classlist.get(classlist.size()-1)+"("))+1, cmd.indexOf("}", cmd.indexOf(classlist.get(classlist.size()-1)+"("))+1))):
												("Dontprintme a, "+noprint(cmd.substring(cmd.indexOf("(", cmd.indexOf(classlist.get(classlist.size()-1)+"("))+1, cmd.indexOf("}", cmd.indexOf(classlist.get(classlist.size()-1)+"("))+1))))+"}";
						
						altclass=noprint(altclass);
						classes+=cmd+"\n"+altclass+"\n";
						
						write("classes.java", classes);				
				   		
				   		cmd="";
					}

				}	
			}

			//checks for function declaration
			else if(cmd.charAt(0)==':')
			{
				if(functions==null)
				{
					functions="";
					pw = new PrintWriter("functions.java");
					pw.close();
				}

				if (!cmd.contains("("))
				{
					cmd+="()";
				}

				if (cmd.indexOf("int ")==1||cmd.indexOf("char ")==1||cmd.indexOf("String ")==1||cmd.indexOf("double ")==1
					||cmd.indexOf("float ")==1||cmd.indexOf("boolean ")==1) 
				{
					//check to make sure function is not already existing
					if(functions.contains(cmd.substring(cmd.indexOf(" ")+1, cmd.indexOf("(")-1)))
					{
						System.out.println("Function already exists! Run it now?(y/n)");
						input=new Scanner(System.in);
						String choice=input.nextLine();
						System.out.print(">> ");
						if (choice.charAt(0)=='y')
						{
							cmd=cmd.substring(cmd.indexOf(" ")+1, cmd.length());
						}
						else
						{
							cmd="";
						}
					}
					else
					{
						cmd=cmd.substring(1, cmd.length());
						functionlist.add(cmd.substring(cmd.indexOf(" "), cmd.indexOf("(")+1));
						cmd+=" ";
						System.out.println("please define the function: ");
						input=new Scanner(System.in);
						System.out.print(">> ");
						cmd+=input.nextLine();
						while((!cmd.contains(";")&&!cmd.equals(""))||(left(cmd)!=right(cmd))||(cmd.contains("(")&&!cmd.contains(")")))
						{
							layeredprompt(cmd);
							input=new Scanner(System.in);
							cmd+=input.nextLine();
						}
						if(!cmd.contains("{"))
						{
							cmd=cmd.substring(0, cmd.indexOf(")")+1)+"{"+cmd.substring(cmd.indexOf(")")+1, cmd.length())+"}";

						}
					
						
						if(cmd.indexOf("(")+1==cmd.indexOf(")"))
						{
							altfunction=noprint(cmd).substring(0, noprint(cmd).indexOf("(")+1)+"Dontprintme a"+
								noprint(cmd).substring(noprint(cmd).indexOf(")"), noprint(cmd).length());
						}
						else
						{
							altfunction=noprint(cmd).substring(0, noprint(cmd).indexOf("(")+1)+"Dontprintme a, "+
								noprint(cmd).substring(noprint(cmd).indexOf("(")+1, noprint(cmd).length());
						}
						
   						functions+="public static "+cmd;
   						altfunction="public static "+altfunction;

   						write("functions.java", functions+"\n"+altfunction);
   						cmd="";
					}
				}
				else
				{

					if(functions.contains(cmd.substring(1, cmd.length())))
					{
						System.out.println("Function already exists! Run it now?(y/n)");
						input=new Scanner(System.in);
						String choice=input.nextLine();
						System.out.print(">> ");
						if (choice.charAt(0)=='y')
						{
							cmd=cmd.substring(1, cmd.length());
						}
						else
						{
							cmd="";
						}
					}
					else
					{
						cmd=cmd.substring(1, cmd.length());
						functionlist.add(cmd.substring(0, cmd.indexOf("(")+1));
						System.out.println("Please enter a return type for the function: ");
						input=new Scanner(System.in);
						System.out.print(">> ");
						String type=input.nextLine();
						if (type.isEmpty())
						{
							type="void";
						}
						System.out.println("please define the function: ");
						input=new Scanner(System.in);
						System.out.print(">> ");
						cmd+=input.nextLine();
						while((!cmd.contains(";")&&!cmd.equals(""))||(left(cmd)!=right(cmd))||(cmd.contains("(")&&!cmd.contains(")")))
						{
							layeredprompt(cmd);
							input=new Scanner(System.in);
							cmd+=input.nextLine();
							
						}
					
						if(!cmd.contains("{"))
						{
							cmd=cmd.substring(0, cmd.indexOf(")")+1)+"{"+cmd.substring(cmd.indexOf(")")+1, cmd.length())+"}";
						}
						
						if(cmd.indexOf("(")+1==cmd.indexOf(")"))
						{
							altfunction=noprint(cmd).substring(0, noprint(cmd).indexOf("(")+1)+"Dontprintme a"+
								noprint(cmd).substring(noprint(cmd).indexOf(")"), noprint(cmd).length());
						}
						else
						{
							altfunction=noprint(cmd).substring(0, noprint(cmd).indexOf("(")+1)+"Dontprintme a, "+
								noprint(cmd).substring(noprint(cmd).indexOf("(")+1, noprint(cmd).length());
						}
						
						functions+="public static "+type+" "+cmd;
   						altfunction="public static "+type+" "+altfunction;
   						write("functions.java", functions+"\n"+altfunction);
   						cmd="";
					}
				}
			}		
			

			//checks for Jrepl command
			else if(cmd.charAt(0)=='-')
			{
				cmd=cmd.substring(1, cmd.length());
				//allows the user to import packages
				if (cmd.startsWith("import"))
				{
					if(!cmd.endsWith(";"))
						cmd+=";";
					imports+=cmd+"\n";
				}

				//displays the current commands in Test.java
				else if(cmd.equals("buffer"))
				{
					System.out.println(readfileclean("Test.java"));
				}

				//displays Jrepl's header
				else if(cmd.equals("version"))
				{
					System.out.println("\t"+header+"\n\t\t-Aneesh Durg");
			   		
				}

				//displays help.txt
				else if(cmd.equals("help"))
				{
					System.out.println(header);
					System.out.println(readfile(path+"/docs/help.txt"));
				}
				
				//displays InputTxt.txt
				else if(cmd.equals("InputTxt"))
				{
					System.out.println(readfile(path+"/docs/InputTxt.txt"));
				}
				
				//resets all files and local vars to initial state 
				else if(cmd.equals("reset"))
				{	
						//reseting local variables
					file="import utils.CleanDir;\nimport utils.InputTxt;\npublic class Test{\npublic static void main(String[] args)throws Exception{\n";
					functions="";
					altfunction="";
					classes="";
					altclass="";
					imports="";
					loadedfunctions="";
					loadedclasses="";
					loadedimports="";
					functionlist=new LinkedList<>();
					classlist=new LinkedList<>();
					classnames=new LinkedList<>();
					loadedfiles=new LinkedList<>();

						//resets Test.java
					file = file.replaceAll("\\n\\n+", "");
					write("Test.java", imports+loadedimports+file+"\n}\n"+functions+"\n"+loadedfunctions+"\n}\n"+classes+"\n"+loadedclasses);
			   		
			   			//resets functions.java
			   		write("functions.java", functions);
			   			//resets Classes.java
			   		write("classes.java", classes);
					
			   		//resets results.txt
			   		pw = new PrintWriter("results.txt");
			   		pw.close();
				}

				//loads a file
				else if(cmd.startsWith("load"))
				{
					cmd=cmd.substring(5, cmd.length())+".jrepl";
					
					//checks if file exists
					File existance=new File(cmd);
					if (existance.exists())
					{
						//ensures that file isn't already loaded
						if(loadedfiles.contains(cmd))
						{
							System.out.println("File is already loaded! Unload it now?(y/n) ");
							System.out.print(">");
							input=new Scanner(System.in);
							temp=input.nextLine();
							temp=temp.toLowerCase();
							if(temp.charAt(0)=='y')
							{
								if (loadedfunctions.contains(cmd))
									loadedfunctions=loadedfunctions.substring(0, loadedfunctions.indexOf("//::"+cmd))+
										(loadedfunctions.indexOf("//::", loadedfunctions.indexOf("//::"+cmd)+1)>-1?
											loadedfunctions.substring(loadedfunctions.indexOf("//::", loadedfunctions.indexOf("//::"+cmd)+1), loadedfunctions.length()):"");
								if (loadedclasses.contains(cmd))
									loadedclasses=loadedclasses.substring(0, loadedclasses.indexOf("//::"+cmd))+
										(loadedclasses.indexOf("//::", loadedclasses.indexOf("//::"+cmd)+1)>-1?
											loadedclasses.substring(loadedclasses.indexOf("//::", loadedclasses.indexOf("//::"+cmd)+1), loadedclasses.length()):"");	
								if (loadedimports.contains(cmd))					
									loadedimports=loadedimports.substring(0, loadedimports.indexOf("//::"+cmd))+
										(loadedimports.indexOf("//::", loadedimports.indexOf("//::"+cmd)+1)>-1?
											loadedimports.substring(loadedimports.indexOf("//::", loadedimports.indexOf("//::"+cmd)+1), loadedimports.length()):"");	
								
								loadedfiles.remove(cmd);	
							}
						}

						//loads file 
						else
						{
							loadedfiles.add(cmd);
							loader(cmd);
							loadedfunctions+=readfile("loadedfunctions.java");
							loadedclasses+=readfile("loadedclasses.java");
							loadedimports+=readfile("loadedimports.java");
							file = file.replaceAll("\\n\\n+", "");
							write("Test.java", imports+loadedimports+file+"\n}\n"+functions+"\n"+loadedfunctions+"\n}\n"+classes+"\n"+loadedclasses+dontprintme);
						}
					}
					else
					{
						System.out.println("Could not find file!");
					}
				}

				//saves a file
				else if(cmd.startsWith("save"))
				{
					if(!cmd.trim().equals("save"))
					{
						cmd=cmd.substring(5, cmd.length())+".jrepl";	
						
						pw = new PrintWriter(cmd);
						pw.close();

						write(cmd, "//:functions\n"+functions+"\n//:classes\n"+classes);
					
					}
					else
					{
						System.out.println("Please specify a filename!");
					}
				}

				//displays saveload.txt
				else if(cmd.equals("saveload"))
				{
					System.out.println(readfile(path+"/docs/saveload.txt"));
				}

				//displays about.txt
				else if(cmd.equals("about"))
				{
					System.out.println(readfile(path+"/docs/about.txt"));
					System.out.println("######*END OF ABOUT*#####");
				}

				//displays this file
				else if(cmd.equals("quine"))
				{
					System.out.println(readfile("Jrepl.java"));
					System.out.println("######*END OF QUINE*#####");
				}

				else
				{
					System.out.println("Unknown command! Please try again or enter -help.");
				}
				
				//sets cmd to an empty string so that it isn't interpreted as a command to be written to a file
				cmd="";
			}
			
			//Handling normal input
			else
			{
				//prints the output of logical or mathematical statements
				if((((cmd.contains("+")||cmd.contains("-")||cmd.contains("/")||cmd.contains("*")||cmd.contains("%"))&&!cmd.contains("="))
					||(cmd.contains("==")||cmd.contains("||")||cmd.contains("&&")||cmd.contains("|")||cmd.contains("^")||cmd.contains("&")))&&!cmd.contains("System.out.print")&&!cmd.contains("print"))
					{
						if (cmd.endsWith(";"))
						{
							cmd=cmd.substring(0, cmd.length()-1);
						}
						cmd="System.out.println("+cmd+");";
					}
				if(cmd.startsWith("print("))
				{
					cmd = "System.out.println("+cmd.substring(cmd.indexOf("(")+1, cmd.indexOf(")")+1)+";";
				}
				//Exits program, not placed under Jrepl commands as I didn't want to prefix exit. 	
				if(cmd.equals("exit"))
				{
					CleanDir cleaner=new CleanDir();
					cleaner.run();
					System.exit(0);
				}
				
				//Waits for a valid statement
				while((!cmd.contains(";")&&!cmd.equals(""))||left(cmd)!=right(cmd)||(cmd.contains("(")&&!cmd.contains(")")))
				{
					//prompt that changes with number of blocks opened
					layeredprompt(cmd);

					//accounts for layeredprompt not accounting for ( and )
					if(left(cmd)==right(cmd)&&!cmd.isEmpty()&&!(cmd.contains("(")&&cmd.contains(")")))
					{
						System.out.print(">>");
					}

					//if the user enter a if/for/while/etc... statement and doesn't start the block on the same line
					if(cmd.contains("(")&&cmd.contains(")")&&!cmd.contains("{")&&!cmd.contains(";"))
					{
						System.out.print("> ");
						input=new Scanner(System.in);
						cmd+=input.nextLine();
						//breaks if there isn't anything to open the block, only 1 line statement
						if(!cmd.contains("{"))
						{
							break;
						}
					}

					//removes stray } if accidentally entered
					if(right(cmd)>left(cmd))
					{
						System.out.println("\nRemoving stray '}'\n");
						cmd=cmd.substring(0, cmd.lastIndexOf("}"))+cmd.substring(cmd.lastIndexOf("}")+1, cmd.length());
						continue;
					}

					//if the user enter ";" and nothing else, break after setting cmd to ""
					if(cmd.equals(";"))
					{
						cmd="";
						break;
					}

					//a break accounting for the obscure case where a user enter someting in the form:
					//	<something>(<something>){<something>} with no ";"
					if(cmd.contains("(")&&cmd.contains(")")&&left(cmd)==right(cmd))
					{
						break;
					}

					//gets the next line of input
					input=new Scanner(System.in);
					cmd+=input.nextLine();
				}
			}

			//only appends to file if there is a normal command
			if(!cmd.equals(""))
			{
				for(int i=0; i<classnames.size(); i++)
				{
					if((cmd.contains("print(")||cmd.contains("println("))&&cmd.contains(classnames.get(i)))
						cmd=cmd.substring(0, cmd.indexOf(classnames.get(i)))+classnames.get(i)+"Dontprintme"+cmd.substring(cmd.indexOf(classnames.get(i))+classnames.get(i).length(), cmd.length());
				}
				file+="\n\t"+cmd;
			}
			file=file.trim();
			//ensures that Test.java always exists
			pw = new PrintWriter("Test.java");
			pw.close();
			
			
			//ensures that functions is updated and not null
			functions=readfile("functions.java");
			if (functions.equals(null))
				functions=" ";
		   	
		   	//writes everything to Test.java
		   	file = file.replaceAll("\\n\\n+", "");
			write("Test.java", imports+loadedimports+file+"\n}\n"+functions+"\n"+loadedfunctions+"\n}\n"+classes+"\n"+loadedclasses+dontprintme);
			
			if(!cmd.equals(""))
			{

			   	compile();

			   	//reading errors
			   	String errors="";
			   	try
			   	{
			   		errors=readfile("compileerrs.txt");
			   	}
			   	catch (Exception e)
			   	{
			   		pw = new PrintWriter("compileerrs.txt");
			   		pw.close();
			   		errors="";
			   	}
		   		if (errors.length()<=0)
		   		{
		   			System.out.println("No compile errors!");
		   		}
		  		else
		   		{
		   			System.out.println(errors);
		   			//removes command that caused the error, if the most recent command caused the error
		   			if(errors.contains(cmd))
		   				file = file.substring(0, file.length() - cmd.length()).trim();
		   			
		   			for (int i=0; i<functionlist.size(); i++)
			   		{
			   			if (errors.contains(" "+functionlist.get(i)))
			   			{
			   					//removes the funtion that caused the error
			   				System.out.println("Deleting function "+functionlist.get(i).substring(0, functionlist.get(i).length()-1)+"!");
			   				
			   				functions=functions.substring(0, functions.indexOf(functionlist.get(i)))+
			   					functions.substring(functions.indexOf("}", functions.indexOf(functionlist.get(i)))+1, functions.length());
			   				functions=functions.substring(0, functions.indexOf(functionlist.get(i)+"Dontprintme"))+
			   					functions.substring(functions.indexOf("}", functions.indexOf(functionlist.get(i)))+1, functions.length());		
			   							
			   							//updates functions.java	
						   		write("functions.java", functions);
						   			//cleans up the file and updates again
		   						functions=functioncleaner("functions.java");
		   						write("functions.java", functions);
		   								//updates Test.java
						   		file=file.trim();
						   		file = file.replaceAll("\\n\\n+", "");
		   						write("Test.java", imports+loadedimports+file+"\n}\n"+functions+"\n"+loadedfunctions+"\n}\n"+classes+"\n"+loadedclasses+dontprintme);
					   			//compiles the file again, just to find out if the most recent command caused the error.
					  				compile();
					  				errors=readfile("compileerrs.txt");
					   				//removes command that caused the error, if the most recent command caused the error
					   			if(errors.contains(cmd))
					   				file = file.substring(0, file.length() - cmd.length());
	   					}
	   				}
	   				for (int i=0; i<classlist.size(); i++)
			   		{
			   			if (errors.contains(" "+classlist.get(i)))
			   			{
			   					//removes the class that caused the error
			   				System.out.println("Deleting class "+classlist.get(i)+"!");
			   				
			   				classes=classes.substring(0, classes.indexOf("class "+classlist.get(i)))+
			   					classes.substring(classes.lastIndexOf("}", classes.indexOf("class ", classes.indexOf(classlist.get(i))))+1, classes.length());
			   				classes=classes.substring(0, classes.indexOf("class "+classlist.get(i)+"Dontprintme"))+
			   					classes.substring(classes.lastIndexOf("}", classes.indexOf("class ", classes.indexOf(classlist.get(i)))>-1?classes.indexOf("class ", classes.indexOf(classlist.get(i))):classes.length())+1, classes.length());		
			   							
			   							//updates functions.java	
						   		write("classes.java", classes);
									
									//updates Test.java
						   		file=file.trim();
						   		file = file.replaceAll("\\n\\n+", "");
		   						write("Test.java", imports+loadedimports+file+"\n}\n"+functions+"\n"+loadedfunctions+"\n}\n"+classes+"\n"+loadedclasses);
					   				//compiles the file again, just to find out if the most recent command caused the error.
				   				compile();
				   				errors=readfile("compileerrs.txt");
					   				//removes command that caused the error, if the most recent command caused the error
					   			if(errors.contains(cmd))
					   				file = file.substring(0, file.length() - cmd.length());
   						}
   					}			   				
	   	   		}
		   		//reading output
	   			try
	   			{
	  				System.out.println(readfile("results.txt"));
		   			if (readfile("results.txt").contains("Exception in thread \"main\""))
		   			{
		   				file = file.substring(0, file.length() - cmd.length()).trim();	
		   			}

	  			}
	  			catch (Exception e)
	  			{
	  				pw = new PrintWriter("results.txt");
	  				pw.close();
	  			}	
   			}
	   		else
	   		{
	   			System.out.println("\n[Done processing!]\n");
	   		} 	
	   		System.out.println("-----\nEnd of output");
	   		//resets some files	
	   		pw = new PrintWriter("functions.java");
			pw.close();
				pw = new PrintWriter("classes.java");
				pw.close();

			//removes print statements and other unwanted statements
		   		file=noprint(file).trim();
		   		
		   		file=noprintf(file, functionlist).trim();
		   		//adds a new object that calls the class without print statements
		   		file+="\n\t"+altclasscall(cmd, classlist);
		   		//updates list of objects to make them searchable
		   		classnames=altclassnames("Test.java", classlist);
		   		//scans file for objects to call the print-less ones instead.
		   		for(int i=0; i<classnames.size(); i++)
		   		{
		   			if (file.contains(classnames.get(i)+"."))
		   			{
		   				file=file.substring(0, file.indexOf(".", file.indexOf(classnames.get(i)+".")))+"Dontprintme"+file.substring(file.indexOf(".", file.indexOf(classnames.get(i)+".")), file.length());
		   			}
		   		}
		   		//scans file for constructors to call the print-less ones instead
		   		for(int i=0; i<classlist.size(); i++)
		   		{
		   			
		   				if (file.lastIndexOf(classlist.get(i)+"(")!=file.lastIndexOf(classlist.get(i)+"(new Dontprintme()"))
		   					file=file.substring(0, file.indexOf("(", file.lastIndexOf(classlist.get(i)+"("))+1)+"new Dontprintme()"+
		   						(file.lastIndexOf(classlist.get(i)+"()")>-1?(file.substring(file.indexOf(")", file.lastIndexOf(classlist.get(i)+"(")), file.length())):
		   							(" ,"+file.substring(file.indexOf("(", file.lastIndexOf(classlist.get(i)+"("))+1, file.length())));
		   		}

		   		
		   		file = file.replaceAll("\\n\\n+", "");
		   		write("Test.java", imports+loadedimports+file+"\n}\n"+functions+"\n"+loadedfunctions+"\n}\n"+classes+"\n"+loadedclasses);
				
		   	//updates functions.java	
		   		write("functions.java", functions);
				
		   	//updates classes.java	
		   		write("classes.java", classes);
		}
	}

	//prevents print statements from being executed multiple times and from getLine methods from being called multiple times.
	public static String noprint(String s)
	{
		while(s.indexOf("System.out.print")>-1)
	   		s = s.substring(0, s.indexOf("System.out.print"))+s.substring(s.indexOf(";", s.indexOf("System.out.print"))+1, s.length());
	   	
		while(s.indexOf(".getLine()")>-1)
	   		s = s.substring(0, s.indexOf(".getLine();"))+".gotLine();"+s.substring(s.indexOf("getLine();")+10, s.length());
	   	while(s.indexOf(".getInt()")>-1)
	   		s = s.substring(0, s.indexOf(".getInt();"))+".gotInt();"+s.substring(s.indexOf("getInt();")+9, s.length());
	   	while(s.indexOf(".getChar()")>-1)
	   		s = s.substring(0, s.indexOf(".getChar();"))+".gotChar();"+s.substring(s.indexOf("getChar();")+10, s.length());
		
		return s;
	}

	//prevents print statements in a function from being executed multiple times need to fix getLine methods being called multiple times.
	public static String noprintf(String s, LinkedList<String> functionlist)
	{
		for(int i=0; i<functionlist.size(); i++)
		{
			int temp=1;
			while(temp>0)
			{
				temp=s.indexOf(functionlist.get(i), temp+1);
				if(s.contains(functionlist.get(i)))
				{
					if(s.indexOf("(", temp)+1==s.indexOf(")", temp))
					{
						if(!s.substring(s.indexOf("(", temp),s.indexOf(")", temp)).contains("Dontprintme"))
						{
							s = s.substring(0, s.indexOf("(", temp-1)+1)+"new Dontprintme()"+s.substring(s.indexOf(")", temp), s.length());	
							
						}
						
					}
					else
					{
						if(temp==-1)
						{
							break;
						}
						if(!s.substring(s.indexOf("(", temp),s.indexOf(")", temp)).contains("Dontprintme"))
						{
							s = s.substring(0, s.indexOf("(", temp-1)+1)+"new Dontprintme(), "+s.substring(s.indexOf("(", temp-1)+1, s.length());
						}	
					
					}
				}
			}
		}
		return s;
	}

	//compiles Test.java by calling compile.bat or compile_unix.sh in /scripts/
	public static void compile() throws Exception
	{
		System.out.println("[processing......]");
		Process p = null;
		if (System.getProperty("os.name").startsWith("Windows"))
		{
			p = Runtime.getRuntime().exec(path+"/scripts/compile.bat /c start /wait");
		}
		else
		{
			p = Runtime.getRuntime().exec("sh "+path+"/scripts/compile_unix.sh /wait");
		}
		p.waitFor();
		System.out.println("");
		System.out.println("[Done processing!]");
	}

	//prints n >s where n=difference between no of { and no of }
	public static void layeredprompt(String cmd)
	{
		for (int i=0; i<=left(cmd)-right(cmd); i++)
		{
			System.out.print(">");
		}
		System.out.print(" ");
	}

	//counts the number of {
	public static int left(String s)
	{
		int counter = 0;
		for( int i=0; i<s.length(); i++ ) 
		{
    		if( s.charAt(i) == '{' ) 
    		{
        		counter++;
    		}	 
		}
		return counter;
	}
	//counts the number of }
	public static int right(String s)
	{
		int counter = 0;
		for( int i=0; i<s.length(); i++ ) 
		{
    		if( s.charAt(i) == '}' ) 
    		{
        		counter++;
    		}	 
		}
		return counter;
	}

	//Writes content to filename
	public static void write(String filename, String content) throws Exception
	{
		BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(filename));
		bufferedWriter.write(content);
		bufferedWriter.newLine();
		bufferedWriter.flush();
		bufferedWriter.close();
	}
	//Below are three readfiles methods, one for just reading a file, one for 
	//skipping lines containing Dontprintme and one for cleaning
	//functions upon deletion	
	
	//reads a file
	public static String readfile (String file) throws Exception
	{
		String result="";
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String line="";
		while (line !=null)
   		{
   			//gets the line
   			line=bufferedReader.readLine();
   			//ensures no null lines
   			if(line==null)
   			{
   				result=result;
   			}
   			else
   			{
   				result+=line+"\n";
   			}
   		}

  		result+="";
   		return result;
	}

	//reads a file removing lines with  "Dontprintme" which are created by Jrepl 
	public static String readfileclean (String file) throws Exception
	{
		String result="";
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String line="";
		String spaces="";
		int counter=0;
		
		while (line !=null)
   		{
   			spaces="";
   			for(int i=0; i<=left(result)-right(result); i++)
   			{
   				spaces+="\t";
   			}
   			line=bufferedReader.readLine();
   			if(line==null)
   			{
   				result=result;
   			}
   			else
   			{
   				//skips lines that contain Dontprintme
   				if(!line.contains("Dontprintme"))
   					result+=spaces+line+"\n";
   			}
   		}
   	
  		result+="";
   		return result;
	}

	//loads a file
	public static void loader(String filename) throws Exception
	{
		write("loadedimports.java", "");
		write("loadedfunctions.java", "");
		write("loadedclasses.java", "");
		String temp=readfile(filename);
		String temp1;
		if(temp.indexOf("//:functions")==-1&&temp.indexOf("//:classes")==-1&&temp.indexOf("//imports")==-1)
		{
			System.out.println("Could not load file! Please mark functions and classes!");
		}
		else
		{

			if(temp.indexOf("//:imports")>-1)
			{
				temp1=temp.substring(temp.indexOf("//:imports")+"//:imports".length(), temp.indexOf("//", temp.indexOf("//:imports")+1)>-1?temp.indexOf("//", temp.indexOf("//:imports")+1):temp.length());
				write("loadedimports.java", "//::"+filename+"\n//loaded content\n"+temp1.trim()+"\n//loaded content\n");
			}
			if(temp.indexOf("//:functions")>-1)
			{
				temp1=temp.substring(temp.indexOf("//:functions")+"//:functions".length(), temp.indexOf("//:classes")>-1?temp.indexOf("//:classes"):temp.length());
				write("loadedfunctions.java", "//::"+filename+"\n//loaded content\n"+temp1.trim()+"\n//loaded content\n");
				//functionlist.add(cmd.substring(cmd.indexOf(" "), cmd.indexOf("(")+1));
			}
			if(temp.indexOf("//:classes")>-1)
			{
				temp1=temp.substring(temp.indexOf("//:classes")+"//:classes".length(), temp.length());
				
				write("loadedclasses.java", "//::"+filename+"\n//loaded content\n"+temp1.trim()+"\n//loaded content\n");
			}
		}

	}

	//removes some extra text left behind when functions are deleted.
	public static String functioncleaner (String file) throws Exception
	{
		String result="";
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String line="";
		String spaces="";
		int counter=0;
		
		while (line !=null)
   		{
   			spaces="";
   			for(int i=0; i<=left(result)-right(result); i++)
   			{
   				spaces+="\t";
   			}
   			line=bufferedReader.readLine();
   			if(line==null)
   			{
   				result=result;
   			}
   			else
   			{
   				//skips line if it is not inside the function
   				if(!line.contains("(")&&(((result.lastIndexOf("{")<result.lastIndexOf("}"))&&result.lastIndexOf("}")!=-1)||result.lastIndexOf("{")==-1))
   					result=result;
   				else	
   					result+=spaces+line+"\n";
   			}
   		}
   		
  		result+="";
   		return result;
	}

	//Dealing with classes
	//creates a new object which calls the corresponding class without print statements
	public static String altclasscall (String line, LinkedList<String> classlist) throws Exception
	{
		String result="";
		if(line==null)
   		{
   			result=result;
   		}
   		else
   		{
   			for(int i=0; i<classlist.size(); i++)
   			{
   				if(line.contains(classlist.get(i))&&line.contains("new "+classlist.get(i)))
   				{
   					if(line.contains(classlist.get(i)+" "))
   						result+=line.substring(0, line.indexOf(classlist.get(i)))+classlist.get(i)+"Dontprintme "+line.substring(line.indexOf(" "), line.indexOf("=")).replaceAll("\\s+","")
   						+"Dontprintme = new "+classlist.get(i)+"Dontprintme"+line.substring(line.indexOf("("), line.indexOf(";")+1)+"\n";		
   					/*else if(line.contains(classlist.get(i)+"("))
   						result+=line.substring(0, line.indexOf(classlist.get(i)))+classlist.get(i)+"Dontprintmeasdf"+line.substring(line.indexOf("("), line.indexOf(";")+1)+"\n";*/
   				}
   			}	
   		}
   		
  		result+="";
   		return result;
	}
	//finds names of objects and returns a list of them
	public static LinkedList<String> altclassnames (String file, LinkedList<String> classlist) throws Exception
	{
		String result="";
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String line="";
		LinkedList <String> altclassname = new LinkedList<>(); 

		while (line !=null)
   		{
   			line=bufferedReader.readLine();
   			
   			if(line!=null)
   			{
   				if(line.contains("new"))
   				{
   					for(int i=0; i<classlist.size(); i++)
   					{
		   				if(line.contains(classlist.get(i)+" ")&&!line.contains("Dontprintme"))
		   				{
		   					if(line.indexOf(" ")>-1&&line.indexOf("=")>-1)
		   						altclassname.add(line.substring(line.indexOf(" "), line.indexOf("=")).replaceAll("\\s+",""));
		   				}
   					}
   				}
   			}
   		}
   		return altclassname;
	}
}