package co.il.nmh.easy.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Maor Hamami
 *
 */

public class EasyThrottle
{
	private int maxCallsPerSecond;
	private AtomicInteger count;
	private AtomicLong prevTime;
	private Semaphore semaphore;

	public EasyThrottle()
	{
		this(0);
	}

	public EasyThrottle(int maxCallsPerSecond)
	{
		this.maxCallsPerSecond = maxCallsPerSecond;

		if (maxCallsPerSecond > 0)
		{
			this.semaphore = new Semaphore(1, true);
			this.count = new AtomicInteger(0);
			this.prevTime = new AtomicLong(-1L);
		}
	}

	public <T> T run(Callable<T> callable) throws Exception
	{
		boolean released = false;

		try
		{
			semaphore.acquire();

			while (true)
			{
				long prevTimeValue = prevTime.get();

				Long now = System.currentTimeMillis();
				Long deltaMs = now - prevTimeValue;

				if (prevTimeValue == -1L || deltaMs >= 1000)
				{
					prevTime.set(now);
					count.set(0);
				}

				if (count.get() < maxCallsPerSecond)
				{
					count.incrementAndGet();
					semaphore.release();
					released = true;

					return callable.call();
				}

				else
				{
					Thread.sleep(1000 - deltaMs);
				}
			}
		}
		finally
		{
			if (!released)
			{
				semaphore.release();
			}
		}
	}
}
