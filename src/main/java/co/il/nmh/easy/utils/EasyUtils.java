package co.il.nmh.easy.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
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

	public static byte[] inputStreamToBytes(InputStream inputStream) throws IOException
	{
		BufferedInputStream reader = new BufferedInputStream(inputStream);

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = reader.read(data, 0, data.length)) != -1)
		{
			buffer.write(data, 0, nRead);
		}

		buffer.flush();
		reader.close();

		byte[] response = buffer.toByteArray();

		return response;
	}
}
