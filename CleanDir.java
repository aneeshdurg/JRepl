import java.io.*;
import java.nio.file.Paths;
public class CleanDir
{
	public void run()
	{
		String path=Paths.get(".").toAbsolutePath().normalize().toString();
		File f=new File(path);
		File[] fList=f.listFiles();
		
		//deletes any extra/unwanted files in directory
		for(int i=0; i<fList.length; i++)
		{
			if(fList[i].getName().endsWith(".class")&&!fList[i].getName().equals("Jrepl.class")&&!fList[i].getName().equals("InputTxt.class")
				&&!fList[i].getName().equals("CleanDir.class"))
				fList[i].delete();
			if(fList[i].getName().endsWith(".txt")&&!fList[i].getName().equals("about.txt")&&!fList[i].getName().equals("compileerrs.txt")
				&&!fList[i].getName().equals("InputTxt.txt")&&!fList[i].getName().equals("help.txt"))
				fList[i].delete();
			if(fList[i].getName().equals("Test.java")||fList[i].getName().equals("functions.java")||fList[i].getName().equals("classes.java"))
				fList[i].delete();
		}
	}
}