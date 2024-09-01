package helperUtility;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import encryptionUtilities.AESEncryptDecryptUtility;
import encryptionUtilities.AESEncryptDecryptUtility.KeySize;

/**
 * Encrypt Decrypt
 * @author Niraj Tiwari
 */
public class EncryptDecrypt {

    private static SecretKey key;
	private static IvParameterSpec ivParameterSpec = AESEncryptDecryptUtility.generateIv();
	private static String algorithm = "AES/CBC/PKCS5Padding";
	
	static {
			key = AESEncryptDecryptUtility.generateKey(KeySize.OneTwentyEight);
	}
	
	/**
	 * Encrypt String
	 * @param textToBeEncrypted
	 * @return encrpyted text
	 */
	public static String encryptString(String textToBeEncrypted) {
		
			return AESEncryptDecryptUtility.encryptText(algorithm, textToBeEncrypted, key, ivParameterSpec);

	}
	
	/**
	 * Decrypt String
	 * @param textToBeDecrypted
	 * @return decrypted text
	 */
	public static String decryptString(String textToBeDecrypted) {
		
		return AESEncryptDecryptUtility.decryptText(algorithm, textToBeDecrypted, key, ivParameterSpec);
	}
	

}
