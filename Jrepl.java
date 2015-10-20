import java.util.*;
import java.io.*;


public class Jrepl
{
	
	static String header="Jrepl v.1.0 - A read-eval-print-loop for java \n";
	public static void main(String[] args) throws Exception
	{
		Process p = null;
		if (System.getProperty("os.name").startsWith("Windows"))
		{
			p = Runtime.getRuntime().exec("delfiles.bat /c start /wait");
		}
		else
		{
			p = Runtime.getRuntime().exec("sudo ./delfiles_unix.sh /wait");
		}
		p.waitFor();
		while(true)
		{
			System.out.println("Welcome to Jrepl! A read-eval-print-loop for java.");
			System.out.println("Press 0 to read about this project");
			System.out.println("Press 1 to run a quine");
			System.out.println("Press any other key to begin.");
			System.out.print("> ");
			Scanner input=new Scanner(System.in);
			String choice="";
			choice+=input.nextLine();
			if (choice.equals("0"))
			{
				System.out.println(readfile("about.txt"));
				System.out.println("######*END OF ABOUT*#####");
				input.nextLine();
				for (int i = 0; i < 50; ++i) System.out.println();
			}
			else if(choice.equals("1"))
			{
				System.out.println(readfile("Jrepl.java"));
				System.out.println("######*END OF QUINE*#####");
				input.nextLine();
				for (int i = 0; i < 50; ++i) System.out.println();

			}
			else
			{
				repl();
			}
		}
	}
	
	public static void repl() throws Exception
	{
		
		PrintWriter pw = new PrintWriter("Test.java");
		pw.close();
		pw = new PrintWriter("functions.java");
		pw.close();
		BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter("Test.java"));
		String file="public class Test{public static void main(String[] args)throws Exception{";
		Scanner input=new Scanner(System.in);
		String cmd;
		String functions="";
		String classes="";
		String imports="";
		System.out.println(header);
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
			if(cmd.contains("::"))
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

				if (functions==null)
				{
					functions="";
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
						System.out.println("please define the function: ");
						input=new Scanner(System.in);
						cmd+=input.nextLine();
						while((!cmd.contains(";")&&!cmd.equals(""))&&(cmd.contains("{")&&!cmd.contains("}"))&&(cmd.contains("(")&&!cmd.contains(")")))
						{
							input=new Scanner(System.in);
							cmd+=input.nextLine();
						}
					
						functions+="public static "+cmd;
						bufferedWriter=new BufferedWriter(new FileWriter("functions.java"));
   						bufferedWriter.write(functions);
   						bufferedWriter.newLine();
   						// flush
				   		bufferedWriter.flush();
				   		cmd="";
					}
				}
				else if(functions!=null&&functions.contains(cmd.substring(1, cmd.indexOf("(")-1)))
				{
					System.out.println("Function already exists! Run it now?(y/n)");
					input=new Scanner(System.in);
					String choice=input.nextLine();
					if (choice.charAt(0)=='y')
					{
						cmd=cmd.substring(1, cmd.length())+";";
					}
					else
					{
						cmd="";
					}
				}
				else
				{
					if(functions.isEmpty())
					{
						pw = new PrintWriter("functions.java");
						pw.close();
						System.out.println("Please enter a return type for the function: ");
						input=new Scanner(System.in);
						String type=input.nextLine();
						
						System.out.println("please define the function: ");
						
						cmd=cmd.substring(1, cmd.length());
						while((!cmd.contains(";")&&!cmd.equals(""))||(cmd.contains("{")&&!cmd.contains("}"))||(cmd.contains("(")&&!cmd.contains(")")))
							{
								input=new Scanner(System.in);
								cmd+=input.nextLine();
							}
						functions="public static "+type+" "+cmd;
						bufferedWriter=new BufferedWriter(new FileWriter("functions.java"));
	   					bufferedWriter.write(functions);
	   					bufferedWriter.newLine();
	   					// flush
				   		bufferedWriter.flush();
				   		cmd="";
					}
					else
					{
						System.out.println("Specified function does not exist! Create it now?(y/n) ");
						input=new Scanner(System.in);
						String choice=input.nextLine();
						if (choice.charAt(0)=='y')
						{
							cmd=cmd.substring(1, cmd.length());
							System.out.println("Please enter a return type for the function: ");
							input=new Scanner(System.in);
							String type=input.nextLine();
							System.out.println("Please define the function: ");
							input=new Scanner(System.in);
							cmd+=input.nextLine();
							while((!cmd.contains(";")&&!cmd.equals(""))&&(cmd.contains("{")&&!cmd.contains("}"))&&(cmd.contains("(")&&!cmd.contains(")")))
							{
								input=new Scanner(System.in);
								cmd+=input.nextLine();
							}
						
							
							if(functions.isEmpty())
								functions="public static "+type+" "+cmd;
							else
								functions+="public static "+type+" "+cmd+"\n";
							bufferedWriter=new BufferedWriter(new FileWriter("functions.java"));
	   						bufferedWriter.write(functions);
	   						bufferedWriter.newLine();
	   						// flush
					   		bufferedWriter.flush();
					   		cmd="";
	   						
	   					}
	   					else
	   					{
	   						cmd="";
	   					}
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
					imports+=cmd;
				}
				else if(cmd.equals("buffer"))
				{
					System.out.println(readfile("Test.java"));
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
				else
				{
					System.out.println("Unknown command! Please try again or enter -help.");
				}
				cmd="";

			}
			else
			{
				/*if (cmd.contains("int ")||cmd.contains("char ")||cmd.contains("String ")||cmd.contains("double ")
					||cmd.contains("float ")||cmd.contains("boolean "))
				{

				}*/
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
					System.exit(0);
				}
				while((!cmd.contains(";")&&!cmd.equals(""))||cmd.contains("){")&&!cmd.contains("}")||cmd.contains("(")&&!cmd.contains(")"))
				{
					input=new Scanner(System.in);
					cmd+=input.nextLine();
				}
			}
				file+=cmd;
				
				//file=file.trim();
				//System.out.println(file);
				pw = new PrintWriter("Test.java");
				pw.close();
				bufferedWriter=new BufferedWriter(new FileWriter("Test.java"));
				functions=readfile("functions.java");
				if (functions.equals(null))
					functions=" ";
		   		bufferedWriter.write(imports+file+"}"+functions+"}"+classes);
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
		  		System.out.println(readfile("results.txt"));
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
		   		
		   		functions=noprint(functions);
		   		
		   		bufferedWriter=new BufferedWriter(new FileWriter("functions.java"));
				bufferedWriter.write(functions);
				// write a new line
		   		bufferedWriter.newLine();
		   		// flush
		   		bufferedWriter.flush();

		   		classes=noprint(classes);	
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
		while(s.indexOf("System.out.println(")>-1)
	   		s = s.substring(0, s.indexOf("System.out.println("))+s.substring(s.indexOf(";", s.indexOf("System.out.println(")-1)+1, s.length());
	   	while(s.indexOf("System.out.print(")>-1)
	   		s = s.substring(0, s.indexOf("System.out.print("))+s.substring(s.indexOf(";", s.indexOf("System.out.print(")-1), s.length());
		while(s.indexOf("writeLine()")>-1)
	   		s = s.substring(0, s.indexOf("writeLine("))+s.substring(s.indexOf(";", s.indexOf("writeLine(")-1), s.length());
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
			p = Runtime.getRuntime().exec("sudo ./compile_unix.sh /wait");
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
}