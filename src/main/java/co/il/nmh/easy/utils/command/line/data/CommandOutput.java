package co.il.nmh.easy.utils.command.line.data;

import co.il.nmh.easy.utils.command.line.data.enums.OutputTypeEnum;
import lombok.Data;

/**
 * @author Maor Hamami
 *
 */

@Data
public class CommandOutput
{
	private OutputTypeEnum outputType;
	private String message;
}
