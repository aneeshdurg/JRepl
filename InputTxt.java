import java.io.*;

public class InputTxt
{
	private String input="";
	public void main(String[] args) throws Exception
	{
		writeLine();
	}
	public void writeLine() throws Exception
	{
		Process p=Runtime.getRuntime().exec("cmd /c start /wait input.bat");
		p.waitFor();
	}
	public String getLine() throws Exception
	{
		input=readfile("input.txt");
		return input;
	}

	public String readfile (String file) throws Exception
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