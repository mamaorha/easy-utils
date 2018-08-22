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
		return ClassLoader.getSystemClassLoader().getResource(name);
	}

	public static String readResource(String fileName)
	{
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();

		InputStream resourceAsStream = classLoader.getResourceAsStream(fileName);

		return EasyUtils.readInputStream(resourceAsStream);
	}
}
