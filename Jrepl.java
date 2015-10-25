import java.util.*;
import java.io.*;
import java.nio.file.Paths;


public class Jrepl
{
	
	static String header="\nJrepl v.1.7 - A read-eval-print-loop for java \n";
	public static void main(String[] args) throws Exception
	{
		CleanDir cleaner=new CleanDir();
		cleaner.run();	

		System.out.println(header);
		System.out.println("\tRun -about to learn more about this project");
		System.out.println("\tRun -help for a list of JRepl specific commands and example usage");

		repl();
		
	}
	
	public static void repl() throws Exception
	{
		
		PrintWriter pw = new PrintWriter("Test.java");
		pw.close();
		pw = new PrintWriter("functions.java");
		pw.close();
		pw = new PrintWriter("results.txt");
		pw.close();
		BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter("Test.java"));
		String file="public class Test{\npublic static void main(String[] args)throws Exception{\n";
		Scanner input=new Scanner(System.in);
		String cmd;
		String functions="";
		String classes="";
		String imports="";
		String altfunction="";
		String dontprintme="class Dontprintme{}";
		LinkedList <String> functionlist= new LinkedList<>();
		while(true)
		{
			//prompt and getting input
			System.out.print("> ");
			input=new Scanner(System.in);
			cmd="";
			cmd=input.nextLine();
			System.out.println();
			
			//dealing with functions
			functions=readfile("functions.java");
			try{
				classes=readfile("classes.java");
			}
			catch (Exception e)
			{
				pw = new PrintWriter("classes.java");
				pw.close();
			}

			if(cmd.isEmpty())
			{
				cmd="";
			}
			else if(cmd.contains("::"))
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
						cmd="class "+cmd.substring(2, cmd.length())+"{";
						while(left(cmd)!=right(cmd))
						{
							System.out.print(">> ");
							input=new Scanner(System.in);
							cmd+=input.nextLine();
						}
						classes+=cmd;
						bufferedWriter=new BufferedWriter(new FileWriter("classes.java"));
   						bufferedWriter.write(classes);
   						bufferedWriter.newLine();
   						// flush
				   		bufferedWriter.flush();
				   		cmd="";
					}

				}	
			}
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
							System.out.print(">> ");
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
   						// flush
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
							System.out.print(">> ");
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
   						// flush
				   		bufferedWriter.flush();
				   		cmd="";
					}
				}		
			}
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
					System.out.println(readfile("help.txt"));
				}
				else if(cmd.equals("InputTxt"))
				{
					System.out.println(readfile("InputTxt.txt"));
				}
				else if(cmd.equals("reset"))
				{
					file="public class Test{public static void main(String[] args)throws Exception{\n";
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
				}
				else if(cmd.equals("about"))
				{
					System.out.println(readfile("about.txt"));
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
			else
			{
				if((((cmd.contains("+")||cmd.contains("-")||cmd.contains("/")||cmd.contains("*")||cmd.contains("%"))&&!cmd.contains("="))
					||(cmd.contains("==")||cmd.contains("||")||cmd.contains("&&")))&&!cmd.contains("System.out.print"))
					{
						if (cmd.endsWith(";"))
						{
							cmd=cmd.substring(0, cmd.length()-1);
						}
						cmd="System.out.println("+cmd+");";
					}
					
				if(cmd.equals("exit"))
				{
					bufferedWriter.close();
					CleanDir cleaner=new CleanDir();
					cleaner.run();
					System.exit(0);
				}
				while((!cmd.contains(";")&&!cmd.equals(""))||cmd.contains("){")&&!cmd.contains("}")||cmd.contains("(")&&!cmd.contains(")"))
				{
					System.out.print(">> ");
					input=new Scanner(System.in);
					cmd+=input.nextLine();
					/*if*/System.out.println(cmd.charAt(cmd.length()-1));
					/*{
						input=new Scanner(System.in);
						cmd+=input.nextLine();
					}*/
				}
			}
				if(!cmd.equals(""))
					file+="\n\t"+cmd;
				
				//file=file.trim();
				//System.out.println(file);
				pw = new PrintWriter("Test.java");
				pw.close();
				bufferedWriter=new BufferedWriter(new FileWriter("Test.java"));
				functions=readfile("functions.java");
				if (functions.equals(null))
					functions=" ";
		   		bufferedWriter.write(imports+file+"\n}\n"+functions+"\n}\n"+classes+dontprintme);
				// write a new line
		   		bufferedWriter.newLine();
		   		// flush
		   		bufferedWriter.flush();
			if(!cmd.equals(""))
			{
			   	compile();
		   		String errors=readfile("compileerrs.txt");
		   		if (errors.length()<=0)
		   		{
		   			System.out.println("No errors!");
		   		}
		  		else
		   		{
		   			System.out.println(errors);
		   			
		   			file = file.substring(0, file.length() - cmd.length());
		   		}
		   		try
		   		{
		  			System.out.println(readfile("results.txt"));
		  		}
		  		catch (Exception e)
		  		{
		  			pw = new PrintWriter("results.txt");
		  			pw.close();
		  		}

		  		//System.out.println("Frozen here!");
		   		/*if (!cmd.isEmpty()&&cmd.charAt(0)=='S')
		   		{
		   			file = file.substring(0, file.length() - cmd.length());
		   		}*/
	   		}
	   		else
	   		{
	   			System.out.println("\n[Done processing!]\n");
	   		} 		
		   		pw = new PrintWriter("functions.java");
				pw.close();
				pw = new PrintWriter("classes.java");
				pw.close();

		   		file=noprint(file);
		   		file=noprintf(file, functionlist);
		   		
		   		//functions=noprint(functions);
		   		
		   		bufferedWriter=new BufferedWriter(new FileWriter("functions.java"));
				bufferedWriter.write(functions);
				// write a new line
		   		bufferedWriter.newLine();
		   		// flush
		   		bufferedWriter.flush();

		   		//classes=noprint(classes);	
		   		bufferedWriter=new BufferedWriter(new FileWriter("classes.java"));
				bufferedWriter.write(classes);
				// write a new line
		   		bufferedWriter.newLine();
		   		// flush
		   		bufferedWriter.flush();  
		}
	}
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
	public static void compile() throws Exception
	{
		System.out.println("[processing......]");
		Process p = null;
		if (System.getProperty("os.name").startsWith("Windows"))
		{
			p = Runtime.getRuntime().exec("compile.bat /c start /wait");
		}
		else
		{
			p = Runtime.getRuntime().exec("sh compile_unix.sh /wait");
		}
		p.waitFor();
		System.out.println("");
		System.out.println("[Done processing!]");
	}
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
}