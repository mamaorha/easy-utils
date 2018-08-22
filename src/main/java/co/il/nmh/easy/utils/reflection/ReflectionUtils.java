package co.il.nmh.easy.utils.reflection;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import co.il.nmh.easy.utils.exceptions.ReflectionException;

/**
 * @author Maor Hamami
 */

public class ReflectionUtils
{
	public static void loadJar(File file) throws ReflectionException
	{
		try
		{
			URL url = file.toURI().toURL();

			URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);
			method.invoke(classLoader, url);
		}
		catch (Exception e)
		{
			throw new ReflectionException(e);
		}
	}
}
