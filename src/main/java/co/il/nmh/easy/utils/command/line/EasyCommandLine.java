package co.il.nmh.easy.utils.command.line;

import java.io.Closeable;
import java.io.File;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

import co.il.nmh.easy.utils.command.line.data.CommandOutput;
import co.il.nmh.easy.utils.command.line.data.enums.OutputTypeEnum;
import co.il.nmh.easy.utils.command.line.readers.CommandLineErrorReader;
import co.il.nmh.easy.utils.command.line.readers.CommandLineInputReader;
import co.il.nmh.easy.utils.exceptions.CommandLineException;

/**
 * @author Maor Hamami
 *
 */

public class EasyCommandLine implements Closeable
{
	protected Process cmd;
	protected String promt;
	protected CommandLineWriter commandLineWriter;
	protected CommandLineInputReader commandLineInputReader;
	protected CommandLineErrorReader commandLineErrorReader;

	protected BlockingQueue<CommandOutput> messages;

	public EasyCommandLine() throws CommandLineException
	{
		try
		{
			Runtime rt = Runtime.getRuntime();
			cmd = rt.exec(new String[] { "cmd.exe" });

			promt = "--" + UUID.randomUUID().toString().substring(0, 8) + ": ";

			commandLineWriter = new CommandLineWriter(cmd.getOutputStream(), promt);
			messages = new LinkedBlockingQueue<>();

			commandLineInputReader = new CommandLineInputReader(this, cmd.getInputStream(), promt);
			commandLineInputReader.start();

			commandLineErrorReader = new CommandLineErrorReader(this, cmd.getErrorStream(), promt);
			commandLineErrorReader.start();
		}
		catch (Exception e)
		{
			throw new CommandLineException(e);
		}
	}

	public void addPath(String path) throws CommandLineException
	{
		File pathFile = new File(path);
		path = pathFile.getParent().replaceAll(Pattern.quote("\\"), "/");

		CommandOutput commandOutput = writeCommand("path");

		if (commandOutput.getOutputType() == OutputTypeEnum.NORMAL)
		{
			commandOutput.setMessage(commandOutput.getMessage().replaceAll(Pattern.quote("\\"), "/"));

			if (!commandOutput.getMessage().contains(path))
			{
				commandOutput = writeCommand("PATH %PATH%;" + path);
			}
		}
	}

	public void pushMessage(String message, OutputTypeEnum outputType)
	{
		CommandOutput commandOutput = new CommandOutput();
		commandOutput.setOutputType(outputType);
		commandOutput.setMessage(message);

		messages.add(commandOutput);
	}

	public CommandOutput writeCommand(String command) throws CommandLineException
	{
		try
		{
			while (null != takeIfReady())
			{
			}

			commandLineWriter.writeCommand(command);
			CommandOutput commandOutput = messages.take();

			int tries = 0;

			while ("\n".equals(commandOutput.getMessage()) && tries < 5)
			{
				tries++;

				if (!messages.isEmpty())
				{
					commandOutput = messages.take();
				}

				else if (tries < 5)
				{
					Thread.sleep(50);
				}
			}

			return commandOutput;
		}

		catch (Exception e)
		{
			throw new CommandLineException(e);
		}
	}

	public CommandOutput take() throws CommandLineException
	{
		try
		{
			return messages.take();
		}
		catch (Exception e)
		{
			throw new CommandLineException(e);
		}
	}

	public CommandOutput takeIfReady() throws CommandLineException
	{
		try
		{
			if (!messages.isEmpty())
			{
				return take();
			}

			return null;
		}
		catch (Exception e)
		{
			throw new CommandLineException(e);
		}
	}

	@Override
	public void close()
	{
		commandLineInputReader.interrupt();
		commandLineErrorReader.interrupt();
		cmd.destroy();
	}
}
