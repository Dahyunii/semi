package utils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class EncryptUtils {

	public static String SHA512(String input) throws Exception {

		String toReturn = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			digest.reset();
			digest.update(input.getBytes("utf8"));
			toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {

		}

		return toReturn;

	}

}