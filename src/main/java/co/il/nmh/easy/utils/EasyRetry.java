package co.il.nmh.easy.utils;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Maor Hamami
 *
 */

@Slf4j
public abstract class EasyRetry<T>
{
	public T run(Integer retries, Integer sleepSeconds)
	{
		if (retries < 1)
		{
			retries = 1;
		}

		if (sleepSeconds < 1)
		{
			sleepSeconds = 1;
		}

		for (int i = 0; i < retries; i++)
		{
			try
			{
				return execute();
			}

			catch (Exception e)
			{
				if (i + 1 < retries)
				{
					log.warn("failed to execute action, trying again in {} seconds", sleepSeconds);

					try
					{
						Thread.sleep(TimeUnit.SECONDS.toMillis(sleepSeconds));
					}
					catch (InterruptedException e1)
					{
					}
				}

				else
				{
					throw e;
				}
			}
		}

		return null;
	}

	protected abstract T execute();
}
