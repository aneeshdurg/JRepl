import java.util.*;
import java.io.*;
import java.nio.file.Paths;
import utils.CleanDir;
import utils.InputTxt;

public class Jrepl
{
	//some useful Strings
	static String header="\nJrepl v.1.10 - A read-eval-print-loop for java \n";
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
		BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter("Test.java"));
		String file="import utils.CleanDir;\nimport utils.InputTxt;\npublic class Test{\npublic static void main(String[] args)throws Exception{\n";
		Scanner input=new Scanner(System.in);
		String cmd;
		String functions="";
		String classes="";
		String imports="";
		String altfunction="";
		String dontprintme="class Dontprintme{}";
		LinkedList <String> functionlist= new LinkedList<>();
		LinkedList <String> classlist= new LinkedList<>();

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
						classlist.add(cmd.substring(2, cmd.length()));
						cmd="class "+cmd.substring(2, cmd.length())+"{";
						while(left(cmd)!=right(cmd))
						{
							layeredprompt(cmd);
							input=new Scanner(System.in);
							cmd+=input.nextLine();
						}

						classes+=cmd;
						
						bufferedWriter=new BufferedWriter(new FileWriter("classes.java"));
   						bufferedWriter.write(classes);
   						bufferedWriter.newLine();
   						bufferedWriter.flush();
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
					
						
						bufferedWriter=new BufferedWriter(new FileWriter("functions.java"));
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
   						bufferedWriter.write(functions+"\n"+altfunction);
   						bufferedWriter.newLine();
   						bufferedWriter.flush();
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
						bufferedWriter=new BufferedWriter(new FileWriter("functions.java"));

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
   						bufferedWriter.write(functions+"\n"+altfunction);
   						bufferedWriter.newLine();
   						bufferedWriter.flush();
				   		cmd="";
					}
				}		
			}

			//checks for Jrepl command
			else if(cmd.charAt(0)=='-')
			{
				cmd=cmd.substring(1, cmd.length());
				if (cmd.startsWith("import"))
				{
					if(!cmd.endsWith(";"))
						cmd+=";";
					imports+=cmd+"\n";
				}
				else if(cmd.equals("buffer"))
				{
					System.out.println(readfileclean("Test.java"));
				}
				else if(cmd.equals("version"))
				{
					System.out.println("\t"+header+"\n\t\t-Aneesh Durg");
			   		
				}
				else if(cmd.equals("help"))
				{
					System.out.println(header);
					System.out.println(readfile(path+"/docs/help.txt"));
				}
				
				else if(cmd.equals("InputTxt"))
				{
					System.out.println(readfile(path+"/docs/InputTxt.txt"));
				}
				
				else if(cmd.equals("reset"))
				{
					file="import utils.CleanDir;\nimport utils.InputTxt;\npublic class Test{\npublic static void main(String[] args)throws Exception{\n";
					functions="";
					classes="";
					imports="";
					functionlist=new LinkedList<>();

					bufferedWriter=new BufferedWriter(new FileWriter("Test.java"));
					bufferedWriter.write(imports+file+"\n}\n"+functions+"\n}\n"+classes);
			   		bufferedWriter.newLine();
			   		bufferedWriter.flush();

			   		bufferedWriter=new BufferedWriter(new FileWriter("functions.java"));
					bufferedWriter.write(functions);
			   		bufferedWriter.newLine();
			   		bufferedWriter.flush();
	
			   		bufferedWriter=new BufferedWriter(new FileWriter("classes.java"));
					bufferedWriter.write(classes);
			   		bufferedWriter.newLine();
			   		bufferedWriter.flush();
			   		
			   		pw = new PrintWriter("results.txt");
			   		pw.close();
				}
				else if(cmd.equals("about"))
				{
					System.out.println(readfile(path+"/docs/about.txt"));
					System.out.println("######*END OF ABOUT*#####");
				}
				else if(cmd.equals("quine"))
				{
					System.out.println(readfile("Jrepl.java"));
					System.out.println("######*END OF QUINE*#####");
				}
				else
				{
					System.out.println("Unknown command! Please try again or enter -help.");
				}
				cmd="";
			}
			
			//Handling normal input
			else
			{
				//prints the output of logical or mathematical statements
				if((((cmd.contains("+")||cmd.contains("-")||cmd.contains("/")||cmd.contains("*")||cmd.contains("%"))&&!cmd.contains("="))
					||(cmd.contains("==")||cmd.contains("||")||cmd.contains("&&")))&&!cmd.contains("System.out.print"))
					{
						if (cmd.endsWith(";"))
						{
							cmd=cmd.substring(0, cmd.length()-1);
						}
						cmd="System.out.println("+cmd+");";
					}
				//Exits program, not placed under Jrepl commands as I didn't want to prefix exit. 	
				if(cmd.equals("exit"))
				{
					bufferedWriter.close();
					CleanDir cleaner=new CleanDir();
					cleaner.run();
					System.exit(0);
				}
				
				//Waits for a valid statement
				while((!cmd.contains(";")&&!cmd.equals(""))||cmd.contains("){")&&(left(cmd)!=right(cmd))||cmd.contains("(")&&!cmd.contains(")"))
				{
					for (int i=0; i<=left(cmd)-right(cmd); i++)
					{
						System.out.print(">");
					}
					System.out.print(" ");
					input=new Scanner(System.in);
					cmd+=input.nextLine();
				}
			}

			//only appends to file if there is a normal command
			if(!cmd.equals(""))
				file+="\n\t"+cmd;
			
			//ensures that Test.java always exists
			pw = new PrintWriter("Test.java");
			pw.close();
			
			bufferedWriter=new BufferedWriter(new FileWriter("Test.java"));
			
			//ensures that functions is updated and not null
			functions=readfile("functions.java");
			if (functions.equals(null))
				functions=" ";
		   	
		   	//writes everything to Test.java
		   	bufferedWriter.write(imports+file+"\n}\n"+functions+"\n}\n"+classes+dontprintme);
			bufferedWriter.newLine();
		   	bufferedWriter.flush();
			
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
		   			System.out.println("No errors!");
		   		}
		  		else
		   		{
		   			System.out.println(errors);
		   			//removes command that caused the error
		   			file = file.substring(0, file.length() - cmd.length());
		   			
		   				for (int i=0; i<functionlist.size(); i++)
		   				{
		   					if (errors.contains(" "+functionlist.get(i)))
		   					{
		   						System.out.println("Deleting function "+functionlist.get(i).substring(0, functionlist.get(i).length()-1)+"!");
		   						functions=functions.substring(0, functions.indexOf(functionlist.get(i)))+
		   							functions.substring(functions.indexOf("}", functions.indexOf(functionlist.get(i)))+1, functions.length());
		   						functions=functions.substring(0, functions.indexOf(functionlist.get(i)+"Dontprintme"))+
		   							functions.substring(functions.indexOf("}", functions.indexOf(functionlist.get(i)))+1, functions.length());		

		   								//updates functions.java	
							   		bufferedWriter=new BufferedWriter(new FileWriter("functions.java"));
									bufferedWriter.write(functions);
									bufferedWriter.newLine();
							   		bufferedWriter.flush();
		   								//cleans up the file and updates again
		   							functions=functioncleaner("functions.java");
		   							bufferedWriter=new BufferedWriter(new FileWriter("functions.java"));
									bufferedWriter.write(functions);
									bufferedWriter.newLine();
							   		bufferedWriter.flush();

		   							bufferedWriter=new BufferedWriter(new FileWriter("Test.java"));
									bufferedWriter.write(imports+file+"\n}\n"+functions+"\n}\n"+classes);
							   		bufferedWriter.newLine();
							   		bufferedWriter.flush();
		   					}
		   				}
		   			
		   		}

		   		//reading output
		   		try
		   		{
		  			System.out.println(readfile("results.txt"));
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

	   		//resets some files	
		   		pw = new PrintWriter("functions.java");
				pw.close();
				pw = new PrintWriter("classes.java");
				pw.close();

			//removes print statements and other unwanted statements
		   		file=noprint(file);
		   		file=noprintf(file, functionlist);
		   		
		   	//updates functions.java	
		   		bufferedWriter=new BufferedWriter(new FileWriter("functions.java"));
				bufferedWriter.write(functions);
				bufferedWriter.newLine();
		   		bufferedWriter.flush();

		   	//updates classes.java	
		   		bufferedWriter=new BufferedWriter(new FileWriter("classes.java"));
				bufferedWriter.write(classes);
				bufferedWriter.newLine();
		   		bufferedWriter.flush();  
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

	//reads a file
	public static String readfile (String file) throws Exception
	{
		String result="";
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String line="";
		int counter=0;
		

		while (line !=null)
   		{
   			
   			
   			line=bufferedReader.readLine();
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
   				if(!line.contains("Dontprintme"))
   					result+=spaces+line+"\n";
   			}
   			
   			
 
   		}
   		
   		
  		result+="";
   		return result;
	}

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
   				if(!line.contains("(")&&(((result.lastIndexOf("{")<result.lastIndexOf("}"))&&result.lastIndexOf("}")!=-1)||result.lastIndexOf("{")==-1))
   					result=result;
   				else	
   					result+=spaces+line+"\n";
   			}
   			
   			
 
   		}
   		
   		
  		result+="";
   		return result;
	}
}