package co.il.nmh.easy.utils.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Maor Hamami
 */

public class ClassInvestigator
{
	public static final ClassInvestigator INSTANCE = new ClassInvestigator();

	private Map<Class<?>, List<Type>> classTypeMap;

	private ClassInvestigator()
	{
		classTypeMap = new ConcurrentHashMap<>();
	}

	public List<Type> getClassTypes(Class<?> clazz)
	{
		if (null == clazz)
		{
			return null;
		}

		if (!classTypeMap.containsKey(clazz))
		{
			synchronized (classTypeMap)
			{
				if (!classTypeMap.containsKey(clazz))
				{
					List<Type> types = new ArrayList<>();

					Class<?> tempClass = clazz;

					Stack<Class<?>> classHierarchy = new Stack<>();

					do
					{
						classHierarchy.push(tempClass);
						tempClass = tempClass.getSuperclass();
					} while (null != tempClass);

					while (!classHierarchy.isEmpty())
					{
						tempClass = classHierarchy.pop();

						Type genericSuperclass = tempClass.getGenericSuperclass();

						if (genericSuperclass instanceof ParameterizedType)
						{
							ParameterizedType superclass = (ParameterizedType) tempClass.getGenericSuperclass();
							Type[] actualTypeArguments = superclass.getActualTypeArguments();

							for (Type type : actualTypeArguments)
							{
								if (!(type instanceof TypeVariable))
								{
									types.add(type);
								}
							}
						}
					}

					classTypeMap.put(clazz, types);
				}
			}
		}

		return classTypeMap.get(clazz);
	}
}
