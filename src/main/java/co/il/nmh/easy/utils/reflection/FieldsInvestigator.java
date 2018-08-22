package co.il.nmh.easy.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Maor Hamami
 */

@Slf4j
public class FieldsInvestigator
{
	public static final FieldsInvestigator INSTANCE = new FieldsInvestigator();

	private Map<Class<?>, Map<String, Field>> classFieldMap;

	private FieldsInvestigator()
	{
		classFieldMap = new ConcurrentHashMap<>();
	}

	public Collection<Field> getClassFields(Class<?> clazz)
	{
		return getClassFieldsMap(clazz).values();
	}

	public Map<String, Field> getClassFieldsMap(Class<?> clazz)
	{
		if (!classFieldMap.containsKey(clazz))
		{
			synchronized (classFieldMap)
			{
				if (!classFieldMap.containsKey(clazz))
				{
					classFieldMap.put(clazz, investigateClass(clazz));
				}
			}
		}

		return classFieldMap.get(clazz);
	}

	private Map<String, Field> investigateClass(Class<?> clazz)
	{
		Map<String, Field> fieldsMap = new HashMap<>();

		Class<?> tempClazz = clazz;

		do
		{
			Field[] declaredFields = tempClazz.getDeclaredFields();

			for (Field field : declaredFields)
			{
				if ((field.getModifiers() & Modifier.FINAL) != Modifier.FINAL && (field.getModifiers() & Modifier.STATIC) != Modifier.STATIC)
				{
					if (!fieldsMap.containsKey(field.getName()))
					{
						fieldsMap.put(field.getName(), field);
					}
				}
			}

			tempClazz = tempClazz.getSuperclass();

		} while (tempClazz != null);

		return fieldsMap;
	}

	public Object getFieldValue(Field field, Object obj)
	{
		if (null == field || null == obj)
		{
			return null;
		}

		Object fieldValue = null;

		try
		{
			if (!field.isAccessible())
			{
				field.setAccessible(true);
			}

			fieldValue = field.get(obj);
		}
		catch (Exception e)
		{
			log.error("failed to extract field [{}] value from given object: [{}], error: [{}]", field.getName(), obj, e);
		}

		return fieldValue;
	}

	public void setFieldValue(Field field, Object obj, Object value)
	{
		if (null == field || null == obj)
		{
			return;
		}

		try
		{
			if (!field.isAccessible())
			{
				field.setAccessible(true);
			}

			field.set(obj, value);
		}
		catch (Exception e)
		{
			log.error("failed to set field [{}] value [{}] for given object: [{}], error: [{}]", field.getName(), value, obj, e);
		}
	}
}
