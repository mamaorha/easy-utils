package co.il.nmh.easy.utils.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import co.il.nmh.easy.utils.EasyInputStream;
import co.il.nmh.easy.utils.exceptions.RestException;
import co.il.nmh.easy.utils.rest.data.EasyRestHeader;
import co.il.nmh.easy.utils.rest.data.RestClientResponse;

/**
 * @author Maor Hamami
 */

public class EasyRestClient
{
	public static RestClientResponse execute(String url, String method, EasyRestHeader headers) throws RestException
	{
		return executeRest(url, method, headers.getHeaders(), null);
	}

	public static RestClientResponse execute(String url, String method, EasyRestHeader headers, EasyInputStream payload) throws RestException
	{
		return executeRest(url, method, headers.getHeaders(), payload);
	}

	public static RestClientResponse execute(String url, String method, EasyRestHeader headers, byte[] payload) throws RestException
	{
		return executeRest(url, method, headers.getHeaders(), payload);
	}

	public static RestClientResponse execute(String url, String method, Map<String, List<String>> headers) throws RestException
	{
		return executeRest(url, method, headers, null);
	}

	public static RestClientResponse execute(String url, String method, Map<String, List<String>> headers, EasyInputStream payload) throws RestException
	{
		return executeRest(url, method, headers, payload);
	}

	public static RestClientResponse execute(String url, String method, Map<String, List<String>> headers, byte[] payload) throws RestException
	{
		return executeRest(url, method, headers, payload);
	}

	private static RestClientResponse executeRest(String url, String method, Map<String, List<String>> headers, Object payload) throws RestException
	{
		try
		{
			URL urlObj = new URL(url);

			HttpURLConnection httpCon = (HttpURLConnection) urlObj.openConnection();
			httpCon.setRequestMethod(method);

			writeHeaders(headers, httpCon);

			if (null != payload)
			{
				if (payload instanceof EasyInputStream)
				{
					EasyInputStream easyInputStream = (EasyInputStream) payload;
					writePayload(httpCon, easyInputStream);
				}
				else
				{
					byte[] payloadBytes = (byte[]) payload;
					writePayload(httpCon, payloadBytes);
				}
			}

			int responseCode = httpCon.getResponseCode();

			EasyInputStream response = extractResponse(httpCon);

			Map<String, List<String>> headerFields = httpCon.getHeaderFields();

			return new RestClientResponse(responseCode, headerFields, response);
		}
		catch (Exception e)
		{
			throw new RestException(e);
		}
	}

	private static void writeHeaders(Map<String, List<String>> headers, HttpURLConnection httpCon)
	{
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
	}

	private static void writePayload(HttpURLConnection httpCon, EasyInputStream easyInputStream) throws IOException
	{
		easyInputStream.reset();

		if (easyInputStream.available() > 0)
		{
			httpCon.setDoOutput(true);

			OutputStream outputStream = httpCon.getOutputStream();

			int nRead;
			byte[] data = new byte[16384];

			while ((nRead = easyInputStream.read(data, 0, data.length)) != -1)
			{
				outputStream.write(data, 0, nRead);
			}

			outputStream.flush();
			outputStream.close();
		}
	}

	private static void writePayload(HttpURLConnection httpCon, byte[] payload) throws IOException
	{
		if (payload.length > 0)
		{
			httpCon.setDoOutput(true);

			OutputStream outputStream = httpCon.getOutputStream();
			outputStream.write(payload);
			outputStream.flush();
			outputStream.close();
		}
	}

	private static EasyInputStream extractResponse(HttpURLConnection httpCon) throws IOException
	{
		InputStream inputStream = null;

		try
		{
			inputStream = httpCon.getInputStream();
		}
		catch (Exception e)
		{
			inputStream = httpCon.getErrorStream();
		}

		return new EasyInputStream(inputStream);
	}
}
