package co.il.nmh.easy.utils.rest;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import co.il.nmh.easy.utils.exceptions.RestException;
import co.il.nmh.easy.utils.rest.data.EasyRestHeader;
import co.il.nmh.easy.utils.rest.data.RestClientResponse;

/**
 * @author Maor Hamami
 */

public class EasyRestClient
{
	public static RestClientResponse execute(String url, String method, EasyRestHeader headers, byte[] payload) throws RestException
	{
		return execute(url, method, headers.getHeaders(), payload);
	}

	public static RestClientResponse execute(String url, String method, Map<String, List<String>> headers, byte[] payload) throws RestException
	{
		try
		{
			URL urlObj = new URL(url);

			HttpURLConnection httpCon = (HttpURLConnection) urlObj.openConnection();
			httpCon.setRequestMethod(method);

			for (Entry<String, List<String>> headerEntry : headers.entrySet())
			{
				List<String> values = headerEntry.getValue();

				if (values.isEmpty())
				{
					continue;
				}
				else
				{
					String headerName = headerEntry.getKey();

					if (values.size() == 1)
					{
						httpCon.setRequestProperty(headerName, values.get(0));
					}

					else
					{
						StringBuilder headerValue = new StringBuilder("[");

						for (String value : values)
						{
							headerValue.append(value).append(",");
						}

						headerValue.delete(headerValue.length() - 1, headerValue.length());
						headerValue.append("]");

						httpCon.setRequestProperty(headerName, headerValue.toString());
					}
				}
			}

			if (null != payload && payload.length > 0)
			{
				httpCon.setDoOutput(true);

				OutputStream outputStream = httpCon.getOutputStream();
				outputStream.write(payload);
				outputStream.flush();
				outputStream.close();
			}

			int responseCode = httpCon.getResponseCode();

			InputStream inputStream = null;

			try
			{
				inputStream = httpCon.getInputStream();
			}
			catch (Exception e)
			{
				inputStream = httpCon.getErrorStream();
			}

			BufferedInputStream reader = new BufferedInputStream(inputStream);

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[16384];

			while ((nRead = reader.read(data, 0, data.length)) != -1)
			{
				buffer.write(data, 0, nRead);
			}

			buffer.flush();
			reader.close();

			byte[] response = buffer.toByteArray();

			Map<String, List<String>> headerFields = httpCon.getHeaderFields();

			return new RestClientResponse(responseCode, headerFields, response);
		}
		catch (Exception e)
		{
			throw new RestException(e);
		}
	}
}
