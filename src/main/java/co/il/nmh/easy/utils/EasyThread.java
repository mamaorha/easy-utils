package co.il.nmh.easy.utils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Maor Hamami
 */

public abstract class EasyThread extends Thread
{
	private String threadName;
	private AtomicBoolean aliveIndicator;
	private long sleepMS = 5;

	public EasyThread(String threadName)
	{
		this.threadName = threadName;
		this.aliveIndicator = new AtomicBoolean(true);
	}

	@Override
	public boolean isInterrupted()
	{
		return super.isInterrupted() || !aliveIndicator.get();
	}

	@Override
	public void interrupt()
	{
		if (this.aliveIndicator.getAndSet(false))
		{
			super.interrupt();
		}
	}

	public void softInterrupt()
	{
		this.aliveIndicator.set(false);
	}

	public void setSleepMS(long sleepMS)
	{
		if (sleepMS > 0)
		{
			this.sleepMS = sleepMS;
		}
	}

	@Override
	public final void run()
	{
		Thread.currentThread().setName(threadName);

		init();

		while (!isInterrupted())
		{
			try
			{
				if (!loopRun())
				{
					break;
				}

				Thread.sleep(sleepMS);
			}
			catch (InterruptedException e)
			{
				break;
			}
		}

		runEnded();
	}

	protected void init()
	{
	}

	protected abstract boolean loopRun() throws InterruptedException;

	protected void runEnded()
	{

	}
}
