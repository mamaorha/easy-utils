package co.il.nmh.easy.utils.command.line;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import co.il.nmh.easy.utils.exceptions.CommandLineException;

/**
 * @author Maor Hamami
 *
 */

public class CommandLineWriter
{
	private BufferedWriter writer;

	public CommandLineWriter(OutputStream outputStream, String promt) throws IOException
	{
		writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		writer.write("prompt " + promt + "\n");
		writer.flush();
	}

	public void writeCommand(String command) throws CommandLineException
	{
		try
		{
			writer.write(command + System.lineSeparator());
			writer.flush();
		}
		catch (Exception e)
		{
			throw new CommandLineException(e);
		}
	}
}
