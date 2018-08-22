package co.il.nmh.easy.utils.exceptions;

/**
 * @author Maor Hamami
 */

public class RestException extends Exception
{
	private static final long serialVersionUID = 86043900510751516L;

	public RestException(Throwable e)
	{
		super(e);
	}
}
