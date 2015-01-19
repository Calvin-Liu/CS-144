import java.security.*;
import java.io.*;

public class ComputeSHA 
{
	static MessageDigest mD;
	static byte result[];
	FileInputStream fileStream = null;
	
	//////////////////Constructor/////////////////////////
	public ComputeSHA()
	{
		try
		{
			//Set message digest to be of type SHA-1 Hash
			mD = MessageDigest.getInstance("SHA-1");
		}
		catch(NoSuchAlgorithmException e)
		{
			e.getStackTrace();
		}
	}
	
	public void readInput(String fileName) throws FileNotFoundException, IOException
	{
		try
		{
			fileStream = new FileInputStream(fileName);
			int content;
			result = new byte[100000];
			while((content = fileStream.read(result)) != -1)
			{
				mD.update(result, 0, content);
			}
		}
		catch(FileNotFoundException f)
		{
			System.out.println("ERROR: No such file");
			throw new FileNotFoundException();
		}
		finally
		{
			try
			{
				if(fileStream != null)
				{
					fileStream.close();
				}
			}
			catch(IOException e)
			{
			}
		}
	}
	
	public static String convertToHex() 
	{
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < result.length; i++)
		{
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
	
	public static void main(String[] args) throws IOException
	{
		if(args.length != 1)
		{
			throw new IllegalArgumentException();
		}
		ComputeSHA comSHA = new ComputeSHA();
		try
		{
			comSHA.readInput(args[0]);
		}
		catch(FileNotFoundException e)
		{
			
		}

		result = mD.digest();
		System.out.println(convertToHex());
	}
}
