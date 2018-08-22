package co.il.nmh.easy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * @author Maor Hamami
 */

public class EasyUtils
{
	public static String readInputStream(InputStream resourceAsStream)
	{
		if (null == resourceAsStream)
		{
			return null;
		}

		Scanner scanner = null;

		try
		{
			scanner = new Scanner(resourceAsStream, "UTF-8");
			String resource = scanner.useDelimiter("\\A").next();

			return resource;
		}
		finally
		{
			scanner.close();

			try
			{
				resourceAsStream.close();
			}
			catch (IOException e)
			{
			}
		}
	}
}
