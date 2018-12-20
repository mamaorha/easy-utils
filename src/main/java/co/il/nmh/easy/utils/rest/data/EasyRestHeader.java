package co.il.nmh.easy.utils.rest.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import lombok.ToString;

/**
 * @author Maor Hamami
 *
 */
@ToString
public class EasyRestHeader
{
	private Map<String, List<String>> headers;

	public EasyRestHeader()
	{
		this(null);
	}

	public EasyRestHeader(Map<String, List<String>> headerFields)
	{
		headers = new ConcurrentHashMap<>();

		if (null != headerFields)
		{
			for (Entry<String, List<String>> entry : headerFields.entrySet())
			{
				String key = entry.getKey();

				if (null == key)
				{
					continue;
				}

				addHeader(key, entry.getValue());
			}
		}
	}

	private void addHeader(String name, List<String> value)
	{
		if (!headers.containsKey(name))
		{
			synchronized (this)
			{
				if (!headers.containsKey(name))
				{
					headers.put(name, new ArrayList<String>());
				}
			}

			headers.get(name).addAll(value);
		}
	}

	public void addHeader(String name, String value)
	{
		if (!headers.containsKey(name))
		{
			synchronized (this)
			{
				if (!headers.containsKey(name))
				{
					headers.put(name, new ArrayList<String>());
				}
			}

			headers.get(name).add(value);
		}
	}

	public Map<String, List<String>> getHeaders()
	{
		return headers;
	}
}
