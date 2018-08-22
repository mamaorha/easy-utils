package co.il.nmh.easy.utils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Maor Hamami
 */

public class ImageUtils
{
	public static BufferedImage scale(BufferedImage img, int targetWidth, int targetHeight)
	{
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = img;
		BufferedImage scratchImage = null;
		Graphics2D g2 = null;

		int w = img.getWidth();
		int h = img.getHeight();

		int prevW = w;
		int prevH = h;

		do
		{
			if (w > targetWidth)
			{
				w /= 2;
				w = (w < targetWidth) ? targetWidth : w;
			}

			else if (w < targetWidth)
			{
				w *= 2;
				w = (w > targetWidth) ? targetWidth : w;
			}

			if (h > targetHeight)
			{
				h /= 2;
				h = (h < targetHeight) ? targetHeight : h;
			}

			else if (h < targetHeight)
			{
				h *= 2;
				h = (h > targetHeight) ? targetHeight : h;
			}

			if (scratchImage == null)
			{
				scratchImage = new BufferedImage(w, h, type);
				g2 = scratchImage.createGraphics();
			}

			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);

			prevW = w;
			prevH = h;
			ret = scratchImage;
		} while (w != targetWidth || h != targetHeight);

		if (g2 != null)
		{
			g2.dispose();
		}

		if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight())
		{
			scratchImage = new BufferedImage(targetWidth, targetHeight, type);
			g2 = scratchImage.createGraphics();
			g2.drawImage(ret, 0, 0, null);
			g2.dispose();
			ret = scratchImage;
		}

		return ret;
	}

	public static BufferedImage copyImage(BufferedImage source)
	{
		BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
		Graphics g = b.getGraphics();
		g.drawImage(source, 0, 0, null);
		g.dispose();
		return b;
	}

	public static BufferedImage byteArrayToBufferImage(byte[] imageData)
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(imageData);

		try
		{
			return ImageIO.read(bais);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
