package co.il.nmh.easy.utils.rest.data;

import java.util.List;
import java.util.Map;

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
	private Map<String, List<String>> headerFields;
	private byte[] response;
}
