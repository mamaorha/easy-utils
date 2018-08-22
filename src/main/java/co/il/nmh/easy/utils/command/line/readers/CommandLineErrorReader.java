package co.il.nmh.easy.utils.command.line.readers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import co.il.nmh.easy.utils.EasyThread;
import co.il.nmh.easy.utils.command.line.EasyCommandLine;
import co.il.nmh.easy.utils.command.line.data.enums.OutputTypeEnum;

/**
 * @author Maor Hamami
 *
 */

public class CommandLineErrorReader extends EasyThread
{
	protected EasyCommandLine easyCommandLine;
	protected BufferedReader inputStream;
	protected String promt;

	private StringBuilder builder;
	private int tries;

	public CommandLineErrorReader(EasyCommandLine easyCommandLine, InputStream inputStream, String promt)
	{
		super("CommandLineErrorReader");

		this.easyCommandLine = easyCommandLine;
		this.inputStream = new BufferedReader(new InputStreamReader(inputStream));
		this.promt = promt;
	}

	@Override
	protected void init()
	{
		builder = new StringBuilder();
		tries = 0;
	}

	@Override
	public boolean loopRun()
	{
		try
		{
			if (inputStream.ready())
			{
				String readLine = inputStream.readLine();

				if (null != readLine && !readLine.isEmpty())
				{
					builder.append(readLine).append(System.lineSeparator());
					tries = 0;
				}
			}

			else if (builder.length() > 0)
			{
				if (tries > 5)
				{
					easyCommandLine.pushMessage(builder.toString(), OutputTypeEnum.FAILURE);
					builder.setLength(0);
					tries = 0;
				}

				else
				{
					Thread.sleep(10);
					tries++;
				}
			}

			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
