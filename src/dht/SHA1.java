package dht;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class SHA1 {
	public static String digest(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String sha1 = null;
		MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");
		msdDigest.update(input.getBytes("UTF-8"), 0, input.length());
		sha1 = DatatypeConverter.printHexBinary(msdDigest.digest());
		return sha1;

	}

}
