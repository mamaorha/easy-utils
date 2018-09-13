package co.il.nmh.easy.utils;

import java.io.InputStream;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author Maor Hamami
 */

public class ResourceUtils
{
	public static Icon getIcon(String name)
	{
		URL url = getURL(name);

		return new ImageIcon(url);
	}

	public static URL getURL(String name)
	{
		return getDefaultClassLoader().getResource(name);
	}

	public static String readResource(String fileName)
	{
		InputStream resourceAsStream = getDefaultClassLoader().getResourceAsStream(fileName);
		return EasyUtils.readInputStream(resourceAsStream);
	}

	// this code was taken from org.springframework.util.ClassUtils
	public static ClassLoader getDefaultClassLoader()
	{
		ClassLoader cl = null;

		try
		{
			cl = Thread.currentThread().getContextClassLoader();
		}

		catch (Throwable ex)
		{
			// Cannot access thread context ClassLoader - falling back...
		}

		if (cl == null)
		{
			// No thread context class loader -> use class loader of this class.
			cl = ResourceUtils.class.getClassLoader();

			if (cl == null)
			{
				// getClassLoader() returning null indicates the bootstrap ClassLoader
				try
				{
					cl = ClassLoader.getSystemClassLoader();
				}
				catch (Throwable ex)
				{
					// Cannot access system ClassLoader - oh well, maybe the caller can live with null...
				}
			}
		}
		return cl;
	}
}
