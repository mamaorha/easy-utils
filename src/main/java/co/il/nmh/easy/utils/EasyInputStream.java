package co.il.nmh.easy.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Maor Hamami
 */

public class EasyInputStream extends InputStream
{
	private InputStream inputStream;
	private ByteArrayOutputStream buffer;

	private int position;
	private int inputStreamPosition;
	private ByteArrayInputStream subBuffer;

	public EasyInputStream(InputStream inputStream)
	{
		this.inputStream = inputStream;
		this.buffer = new ByteArrayOutputStream();
		this.position = 0;
		this.inputStreamPosition = 0;
	}

	@Override
	public int read() throws IOException
	{
		if (position < inputStreamPosition)
		{
			int data = subBuffer.read();

			if (data > -1)
			{
				position++;
			}

			return data;
		}
		else
		{
			int data = inputStream.read();

			if (data > -1)
			{
				position++;
				inputStreamPosition++;

				buffer.write(data);
			}

			return data;
		}
	}

	@Override
	public int read(byte b[]) throws IOException
	{
		return read(b, 0, b.length);
	}

	@Override
	public int read(byte b[], int off, int len) throws IOException
	{
		if (position < inputStreamPosition)
		{
			int nRead = subBuffer.read(b, off, len);
			position += nRead;

			if (nRead < len)
			{
				return read(b, nRead, b.length - nRead);
			}

			return nRead;
		}
		else
		{
			int nRead = inputStream.read(b, off, len);

			if (nRead > -1)
			{
				position += nRead;
				inputStreamPosition += nRead;
				buffer.write(b, off, nRead);
			}

			return nRead;
		}
	}

	@Override
	public long skip(long n) throws IOException
	{
		throw new IOException("skip is not supported");
		// return inputStream.skip(n);
	}

	@Override
	public int available() throws IOException
	{
		if (position < inputStreamPosition && null != subBuffer)
		{
			return subBuffer.available() + inputStream.available();
		}
		else
		{
			return inputStream.available();
		}
	}

	@Override
	public void close() throws IOException
	{
		try
		{
			inputStream.close();
		}
		catch (Exception e)
		{
		}

		try
		{
			buffer.close();
		}
		catch (Exception e)
		{
		}

		if (null != subBuffer)
		{
			try
			{
				subBuffer.close();
			}
			catch (Exception e)
			{
			}
		}
	}

	@Override
	public synchronized void mark(int readlimit)
	{
		// inputStream.mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException
	{
		if (position > 0)
		{
			position = 0;
			subBuffer = new ByteArrayInputStream(buffer.toByteArray());
		}
		// inputStream.reset();
	}

	@Override
	public boolean markSupported()
	{
		return false;
		// return inputStream.markSupported();
	}

	public byte[] getAllBytes() throws IOException
	{
		position = inputStreamPosition;

		byte[] data = new byte[16384];

		while (read(data, 0, data.length) != -1)
		{
		}

		buffer.flush();

		return buffer.toByteArray();
	}
}