package co.il.nmh.easy.utils;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Maor Hamami
 */

public class EasyInputStreamTest
{
	private int available;
	private int nextAvailable;
	private byte[] data;
	private byte[] nextData;
	private byte[] thirdData;
	private byte[] allBytes;

	private void init() throws IOException
	{
		data = new byte[4000];
		nextData = new byte[1000];
		thirdData = new byte[6000];

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream("words.txt");
		available = inputStream.available();
		inputStream.read(data, 0, data.length);
		inputStream.read(nextData, 0, nextData.length);
		nextAvailable = inputStream.available();

		for (int i = 0; i < data.length; i++)
		{
			thirdData[i] = data[i];
		}

		for (int i = 0; i < nextData.length; i++)
		{
			thirdData[i + data.length] = nextData[i];
		}

		inputStream.read(thirdData, nextData.length + data.length, thirdData.length - nextData.length - data.length);

		inputStream.close();

		inputStream = classLoader.getResourceAsStream("words.txt");
		allBytes = EasyUtils.inputStreamToBytes(inputStream);
		inputStream.close();
	}

	@Test
	public void validateFunctionality() throws IOException
	{
		init();

		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream("words.txt");

		EasyInputStream easyInputStream = new EasyInputStream(inputStream);

		Assert.assertEquals(available, easyInputStream.available());

		byte[] easyData = new byte[4000];
		byte[] easyNextData = new byte[1000];

		easyInputStream.read(easyData, 0, easyData.length);
		easyInputStream.read(easyNextData, 0, easyNextData.length);

		Assert.assertArrayEquals(data, easyData);
		Assert.assertArrayEquals(nextData, easyNextData);

		Assert.assertEquals(nextAvailable, easyInputStream.available());

		easyInputStream.reset();
		Assert.assertEquals(available, easyInputStream.available());

		easyData = new byte[4000];
		easyNextData = new byte[1000];

		easyInputStream.read(easyData, 0, easyData.length);
		easyInputStream.read(easyNextData, 0, easyNextData.length);

		Assert.assertArrayEquals(data, easyData);
		Assert.assertArrayEquals(nextData, easyNextData);

		Assert.assertEquals(nextAvailable, easyInputStream.available());

		easyInputStream.reset();

		easyData = new byte[6000];
		easyInputStream.read(easyData);

		Assert.assertArrayEquals(thirdData, easyData);

		Assert.assertArrayEquals(allBytes, easyInputStream.getAllBytes());
		Assert.assertArrayEquals(allBytes, easyInputStream.getAllBytes());

		easyInputStream.close();
	}
}
