package co.il.nmh.easy.utils;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import co.il.nmh.easy.utils.exceptions.EncryptionException;

/**
 * @author Maor Hamami
 */

public class EncryptionUtils
{
	public static String encrypt(String str, String key) throws EncryptionException
	{
		try
		{
			byte[] keyBytes = createKey(key);

			SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

			Cipher ecipher = Cipher.getInstance("AES");
			ecipher.init(Cipher.ENCRYPT_MODE, secretKey);

			byte[] utf8 = str.getBytes("UTF-8");
			byte[] enc = ecipher.doFinal(utf8);

			return Base64.getEncoder().encodeToString(enc);
		}
		catch (Exception e)
		{
			throw new EncryptionException(e);
		}
	}

	public static String decrypt(String str, String key) throws EncryptionException
	{
		try
		{
			byte[] keyBytes = createKey(key);

			SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

			Cipher dcipher = Cipher.getInstance("AES");
			dcipher.init(Cipher.DECRYPT_MODE, secretKey);

			byte[] dec = Base64.getDecoder().decode(str);
			byte[] utf8 = dcipher.doFinal(dec);

			return new String(utf8, "UTF-8");
		}
		catch (Exception e)
		{
			throw new EncryptionException(e);
		}
	}

	private static byte[] createKey(String key)
	{
		byte[] bytes = key.getBytes();
		byte[] keyBytes = new byte[16];

		int length = Math.min(bytes.length, keyBytes.length);

		for (int i = 0; i < length; i++)
		{
			keyBytes[i] = bytes[i];
		}

		return keyBytes;
	}
}
