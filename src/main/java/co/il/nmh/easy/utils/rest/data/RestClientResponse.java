package co.il.nmh.easy.utils.rest.data;

import co.il.nmh.easy.utils.EasyInputStream;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Maor Hamami
 *
 */

@Data
@AllArgsConstructor
public class RestClientResponse
{
	private int httpStatus;
	private EasyRestHeader headerFields;
	private EasyInputStream responseBody;
}
