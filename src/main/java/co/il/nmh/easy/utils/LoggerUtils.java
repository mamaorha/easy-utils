package co.il.nmh.easy.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Maor Hamami
 */

public class LoggerUtils
{
	public static final LoggerUtils INSTANCE = new LoggerUtils();

	private ILoggerFactory loggerContext;
	private Method setLevel;
	private Map<String, Object> levelMap;

	private LoggerUtils()
	{
		loggerContext = LoggerFactory.getILoggerFactory();
		levelMap = new ConcurrentHashMap<>();
	}

	public void overrideLoggerLevel(String loggerPackage, String level)
	{
		Logger logger = loggerContext.getLogger(loggerPackage);

		if (null == setLevel)
		{
			synchronized (this)
			{
				if (null == setLevel)
				{
					Method[] methods = logger.getClass().getMethods();

					for (Method method : methods)
					{
						if ("setLevel".equals(method.getName()))
						{
							setLevel = method;
							break;
						}
					}
				}
			}
		}

		if (null != setLevel)
		{
			Object levelObj = levelMap.get(level);

			if (null == levelObj)
			{
				synchronized (this)
				{
					levelObj = levelMap.get(level);

					if (null == levelObj)
					{
						try
						{
							Class<?> levelClass = setLevel.getParameterTypes()[0];
							Field[] declaredFields = levelClass.getDeclaredFields();

							for (Field field : declaredFields)
							{
								if (Modifier.isStatic(field.getModifiers()) && field.getName().equalsIgnoreCase(level) && field.getType().equals(levelClass))
								{
									levelObj = field.get(null);
									break;
								}
							}
						}
						catch (Exception e)
						{
						}
					}
				}
			}

			if (null != levelObj)
			{
				try
				{
					setLevel.invoke(logger, levelObj);
				}
				catch (Exception e)
				{
				}
			}
		}
	}
}
