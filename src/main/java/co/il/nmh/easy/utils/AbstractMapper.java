package co.il.nmh.easy.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.il.nmh.easy.utils.reflection.ClassInvestigator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Maor Hamami
 *
 */
@Slf4j
public abstract class AbstractMapper<T, V>
{
	private Type typeV;

	public AbstractMapper()
	{
		typeV = ClassInvestigator.INSTANCE.getClassTypes(getClass()).get(1);
	}

	public V map(T src)
	{
		return map(src, null, null);
	}

	public V map(T src, Map<String, Object> contextMap)
	{
		return map(src, null, contextMap);
	}

	public V map(T src, V dst)
	{
		return map(src, dst, null);
	}

	@SuppressWarnings("unchecked")
	public V map(T src, V dst, Map<String, Object> contextMap)
	{
		if (null == src)
		{
			return dst;
		}

		if (null == dst)
		{
			try
			{
				if (typeV instanceof ParameterizedType)
				{
					if (((ParameterizedType) typeV).getRawType() == List.class)
					{
						dst = (V) new ArrayList<>();
					}

					else
					{
						log.error("failed to instantiate complex type {}", typeV);
					}
				}

				else
				{
					dst = ((Class<V>) (typeV)).newInstance();
				}
			}
			catch (Exception e)
			{
				log.error("failed to instantiate class {}", ((Class<V>) typeV).getName());
				throw new RuntimeException(e);
			}
		}

		return applyMapping(src, dst, contextMap);
	}

	protected abstract V applyMapping(T src, V dst, Map<String, Object> contextMap);
}
