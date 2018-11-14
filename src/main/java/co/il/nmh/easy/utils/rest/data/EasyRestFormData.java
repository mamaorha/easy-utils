package co.il.nmh.easy.utils.rest.data;

import lombok.ToString;

/**
 * @author Maor Hamami
 *
 */

@ToString
public class EasyRestFormData
{
	public static String BOUNDRY = "--------------------------340547036457664700538556";

	private StringBuilder str = new StringBuilder();

	public void addFormData(String name, String value)
	{
		str.append("--").append(BOUNDRY).append("\r\n");
		str.append("Content-Disposition: form-data; name=\"").append(name).append("\"\r\n").append("\r\n");
		str.append(value).append("\r\n");
	}

	public String getData()
	{
		return str.toString() + "--" + BOUNDRY + "--\r\n";
	}

	public int length()
	{
		return getData().length();
	}
}
