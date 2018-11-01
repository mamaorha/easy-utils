package co.il.nmh.easy.utils;

import java.math.BigDecimal;

/**
 * @author Maor Hamami
 */

public class MathUtils
{
	public static Double roundDouble(Double value, int numbersAfterDot, boolean round)
	{
		String strValue = value.toString();

		int start = strValue.indexOf(".");

		if (start > -1)
		{
			int afterDotLeft = strValue.length() - start - 1;

			if (numbersAfterDot > afterDotLeft)
			{
				return value;
			}

			double rounder = 0;

			if (round)
			{
				if (afterDotLeft > numbersAfterDot + 1)
				{
					int next = Integer.valueOf(String.valueOf(strValue.charAt(start + numbersAfterDot + 1)));

					if (value >= 0)
					{
						if (next >= 5)
						{
							rounder = 1 / (Math.pow(10, numbersAfterDot));
						}
					}

					else
					{
						if (next <= 5)
						{
							rounder = -1 / (Math.pow(10, numbersAfterDot));
						}
					}
				}
			}

			strValue = strValue.substring(0, start + numbersAfterDot + 1);

			double doubleValue = new BigDecimal(strValue).add(new BigDecimal(rounder)).doubleValue();
			return doubleValue;
		}

		return value;
	}

	public static int random(int min, int max)
	{
		return (int) (Math.random() * (max - min) + min);
	}

	public static Boolean percentChance(double chance)
	{
		return Math.random() <= chance;
	}
}
