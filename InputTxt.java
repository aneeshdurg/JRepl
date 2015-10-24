import java.io.*;

public class InputTxt
{
	private String input="";
   private static int id=0;
   private int name=0;
   public InputTxt()
   {
      name=id;
      id++;
   }
	public String getLine() throws Exception
	{
      Process p=null;

      if (System.getProperty("os.name").startsWith("Windows"))
      {
         p=Runtime.getRuntime().exec("cmd /c start /wait input.bat "+id);
         p.waitFor();
      }
      else
      {
         p=Runtime.getRuntime().exec("cmd /c start /wait sh input_unix.sh "+id);
         p.waitFor();
      }
		
		
		input=readfile(id+".txt");
		return input;
	}
   public int getInt() throws Exception
   {
      input=getLine();
      int num=0;
      try
      {
         num=Integer.parseInt(input);
         return num;
      }
      catch(Exception e)
      {
         return num;
      }
   }
   public char getChar() throws Exception
   {
      input=getLine().trim();
      return input.charAt(0);
   }
	private String readfile (String file) throws Exception
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

   public String gotLine() throws Exception
   {
	   input=readfile(id+".txt");

      return input;
   }
   public int gotInt() throws Exception
   {
      input=readfile(id+".txt");
      input=input.trim();
      int num=0;
      try
      {
         num=Integer.parseInt(input);
         return num;
      }
      catch(Exception e)
      {
         System.out.println("Not a number! Returning 0.");
         return num;
      }
   }
   public char gotChar() throws Exception
   {
      input=readfile(id+".txt");
      return input.charAt(0);
   }
}