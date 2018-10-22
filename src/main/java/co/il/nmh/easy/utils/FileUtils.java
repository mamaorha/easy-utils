package co.il.nmh.easy.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

/**
 * @author Maor Hamami
 */

public class FileUtils
{
	public static String getBaseSignature(File file)
	{
		try
		{
			int maxLength = 15 * 1000000;

			if (file.length() < maxLength)
			{
				maxLength = (int) file.length();
			}

			byte[] buffer = new byte[maxLength];

			FileInputStream fis = new FileInputStream(file.getPath());
			fis.read(buffer, 0, buffer.length);
			fis.close();

			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] result = md.digest(buffer);

			String FileBit = DatatypeConverter.printBase64Binary(result);

			return FileBit;
		}

		catch (Exception e)
		{
		}

		return null;
	}

	public static String getFullSignature(String baseSignature, File file)
	{
		try
		{
			int maxLength = 15 * 1000000;

			if (file.length() <= maxLength)
			{
				return baseSignature;
			}

			StringBuilder stringBuilder = new StringBuilder();
			MessageDigest md = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(file.getPath());

			do
			{
				int available = fis.available();

				if (available < maxLength)
				{
					maxLength = available;
				}

				byte[] buffer = new byte[maxLength];

				fis.read(buffer, 0, buffer.length);

				byte[] result = md.digest(buffer);
				String FileBit = DatatypeConverter.printBase64Binary(result);

				stringBuilder.append(FileBit);
			} while (fis.available() > 0);

			fis.close();

			return stringBuilder.toString();
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static boolean createDir(String path)
	{
		path = path.replace("/", "\\");
		path = path.replace("\\", File.separator);

		String[] dirs = path.split(Pattern.quote(File.separator));

		String basePath = "";

		for (int i = 0; i < dirs.length; i++)
		{
			File dir = new File(basePath + dirs[i]);

			if ((!dir.exists() && !dir.mkdir()) || !dir.isDirectory())
			{
				return false;
			}

			basePath = dir.getPath() + File.separator;
		}

		return true;
	}

	public static boolean deleteDir(File dir)
	{
		// try 3 times
		for (int i = 0; i < 3 && dir.exists(); i++)
		{
			deleteDirectory(dir);
		}

		return !dir.exists();
	}

	private static boolean deleteDirectory(File dir)
	{
		if (dir.isDirectory())
		{
			String[] children = dir.list();

			for (int i = 0; i < children.length; i++)
			{
				boolean success = deleteDir(new File(dir, children[i]));

				if (!success)
				{
					return false;
				}
			}
		}

		return dir.delete();
	}
}
