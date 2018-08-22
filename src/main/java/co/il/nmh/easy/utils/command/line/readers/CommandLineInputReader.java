package co.il.nmh.easy.utils.command.line.readers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import co.il.nmh.easy.utils.EasyThread;
import co.il.nmh.easy.utils.command.line.EasyCommandLine;
import co.il.nmh.easy.utils.command.line.data.enums.OutputTypeEnum;
import co.il.nmh.easy.utils.exceptions.CommandLineException;

/**
 * @author Maor Hamami
 *
 */

public class CommandLineInputReader extends EasyThread
{
	protected EasyCommandLine easyCommandLine;
	protected BufferedReader inputStream;
	protected String promt;
	private char[] temp;

	private String lineSeparator;
	private StringBuilder message;
	private StringBuilder line;
	private boolean ready;
	private boolean commandLine;

	public CommandLineInputReader(EasyCommandLine easyCommandLine, InputStream inputStream, String promt)
	{
		super("CommandLineInputReader");

		this.easyCommandLine = easyCommandLine;
		this.inputStream = new BufferedReader(new InputStreamReader(inputStream));
		this.promt = promt;
		this.temp = new char[1];
	}

	@Override
	protected void init()
	{
		lineSeparator = System.lineSeparator();
		message = new StringBuilder();
		line = new StringBuilder();

		ready = false;
		commandLine = false;
	}

	@Override
	public boolean loopRun()
	{
		try
		{
			char curr = read();

			line.append(curr);

			String currLine = line.toString();

			if (currLine.startsWith(promt) && !commandLine)
			{
				if (ready)
				{
					if (message.length() > 0)
					{
						easyCommandLine.pushMessage(message.toString(), OutputTypeEnum.NORMAL);
					}
				}

				else
				{
					ready = true;
				}

				commandLine = true;
				message.setLength(0);
			}

			if (currLine.endsWith(lineSeparator))
			{
				if (commandLine)
				{
					commandLine = false;
				}

				else
				{
					message.append(line);
				}

				line.setLength(0);
			}

			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	private char read() throws CommandLineException
	{
		try
		{
			int read = inputStream.read(temp);

			if (read == -1)
			{
				throw new RuntimeException("empty buffer");
			}

			char curr = temp[0];
			return curr;
		}
		catch (Exception e)
		{
			throw new CommandLineException(e);
		}
	}
}
