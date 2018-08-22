package co.il.nmh.easy.utils.reflection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Maor Hamami
 */
@Slf4j
public class PackageUtils
{
	public static List<Class<?>> getPackageClasses(String packagePath)
	{
		return getPackageClasses(packagePath, ClassLoader.getSystemClassLoader());
	}

	public static List<Class<?>> getPackageClasses(String packagePath, ClassLoader classLoader)
	{
		String relPath = packagePath.replace('.', '/');

		URL resource = classLoader.getResource(relPath);

		if (resource == null)
		{
			return null;
		}

		List<Class<?>> classes = new ArrayList<>();

		String fullPath = resource.getFile();

		File directory = new File(fullPath);

		if (directory != null && directory.exists())
		{
			investigateDir(directory, packagePath, classes);
		}

		else
		{
			try
			{
				String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
				jarPath = jarPath.replaceAll("%20", " ");
				JarFile jarFile = new JarFile(jarPath);

				investigateJar(jarFile, relPath, classes);
			}
			catch (IOException e)
			{
			}
		}

		return classes;
	}

	private static void investigateDir(File directory, String packagePath, List<Class<?>> classes)
	{
		if (directory != null && directory.exists())
		{
			// Get the list of the files contained in the package
			File[] files = directory.listFiles();

			for (File file : files)
			{
				String name = file.getName().replace("$", ".");

				if (file.isDirectory())
				{
					investigateDir(file, packagePath + "." + name, classes);
				}

				else if (name.endsWith(".class"))
				{
					// removes the .class extension
					String className = packagePath + '.' + name.substring(0, name.length() - 6);

					try
					{
						classes.add(Class.forName(className));
					}

					catch (ClassNotFoundException e)
					{
						log.warn("warning failed to load {}", className);
					}
				}
			}
		}
	}

	private static void investigateJar(JarFile jarFile, String relPath, List<Class<?>> classes)
	{
		try
		{
			Enumeration<JarEntry> entries = jarFile.entries();

			while (entries.hasMoreElements())
			{
				JarEntry entry = entries.nextElement();
				String entryName = entry.getName();

				if (entryName.startsWith(relPath) && entryName.length() > (relPath.length() + "/".length()) && entryName.endsWith(".class"))
				{
					String className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");

					try
					{
						classes.add(Class.forName(className));
					}

					catch (ClassNotFoundException e)
					{
						log.warn("warning failed to load {}", className);
					}
				}
			}

			jarFile.close();
		}
		catch (IOException e)
		{
		}
	}
}