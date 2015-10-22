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
		Process p=Runtime.getRuntime().exec("cmd /c start /wait sh input_unix.sh "+id);
		p.waitFor();
		input=readfile(id+".txt");
		return input;
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
}