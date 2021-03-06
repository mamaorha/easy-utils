package co.il.nmh.easy.utils.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import co.il.nmh.easy.utils.EasyInputStream;
import co.il.nmh.easy.utils.exceptions.RestException;
import co.il.nmh.easy.utils.rest.data.EasyRestFormData;
import co.il.nmh.easy.utils.rest.data.EasyRestHeader;
import co.il.nmh.easy.utils.rest.data.RestClientResponse;

/**
 * @author Maor Hamami
 */

public class EasyRestClient
{
	private EasyRestClient()
	{
	}

	public static void allowAllSSL() throws KeyManagementException, NoSuchAlgorithmException
	{
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
		{
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers()
			{
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs, String authType)
			{
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs, String authType)
			{
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier()
		{
			@Override
			public boolean verify(String hostname, SSLSession session)
			{
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

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

	public static RestClientResponse execute(String url, String method, Map<String, List<String>> headers, EasyRestFormData easyRestFormData) throws RestException
	{
		if (null == headers)
		{
			headers = new HashMap<>();
		}

		List<String> contentType = new ArrayList<>();
		contentType.add("multipart/form-data; boundary=" + EasyRestFormData.BOUNDRY);

		List<String> contentLength = new ArrayList<>();
		contentLength.add(String.valueOf(easyRestFormData.length()));

		headers.put("content-type", contentType);
		headers.put("content-length", contentLength);

		return executeRest(url, method, headers, easyRestFormData.getData().getBytes());
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
			EasyRestHeader easyRestHeader = new EasyRestHeader(httpCon.getHeaderFields());

			return new RestClientResponse(responseCode, easyRestHeader, response);
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

				for (String value : values)
				{
					httpCon.addRequestProperty(headerName, value);
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
