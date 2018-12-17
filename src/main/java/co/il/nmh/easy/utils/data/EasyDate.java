package co.il.nmh.easy.utils.data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Maor Hamami
 *
 */

public class EasyDate implements Comparable<EasyDate>
{
	private Long millis;
	private String dateString;

	public EasyDate()
	{
		setMillis(System.currentTimeMillis());
	}

	public EasyDate(Long millis)
	{
		setMillis(millis);
	}

	private void setMillis(Long millis)
	{
		this.millis = millis;
		this.dateString = new SimpleDateFormat("dd-MM-yyyy HH:mm:ssZ").format(new Date(millis));
	}

	public long getMillis()
	{
		return millis;
	}

	@Override
	public int compareTo(EasyDate other)
	{
		return millis.compareTo(other.millis);
	}

	public boolean before(EasyDate other)
	{
		return millis < other.millis;
	}

	public boolean after(EasyDate other)
	{
		return millis > other.millis;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((millis == null) ? 0 : millis.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EasyDate other = (EasyDate) obj;
		if (millis == null)
		{
			if (other.millis != null)
				return false;
		}
		else if (!millis.equals(other.millis))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return dateString;
	}
}
