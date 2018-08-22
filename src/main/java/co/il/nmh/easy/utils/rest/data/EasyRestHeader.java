package co.il.nmh.easy.utils.rest.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
		headers = new ConcurrentHashMap<>();
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
