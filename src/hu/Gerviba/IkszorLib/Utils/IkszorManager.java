/**
 * This file is under Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International license
 * (CC BY-NC-SA 4.0)
 * @author Szabó Gergely (Gerviba)
 * @date 2014-09-21
 * 
 * You are free to:
 *	Share — Copy and redistribute the material in any medium or format.
 *	Adapt — Remix, transform, and build upon the material.
 *	
 * Under the following terms:
 *	Attribution — You must give appropriate credit, provide a link to the license, and indicate 
 *	if changes were made. You may do so in any reasonable manner, but not in any way that suggests 
 *	the licensor endorses you or your use.
 *	NonCommercial — You may not use the material for commercial purposes.
 *	ShareAlike — If you remix, transform, or build upon the material, you must 
 *	distribute your contributions under the same license as the original.
 *
 * @see http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */
package hu.Gerviba.IkszorLib.Utils;

import hu.Gerviba.IkszorLib.Exceptions.IkszorConvertException;
import hu.Gerviba.IkszorLib.Exceptions.IkszorException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.MessageDigest;

import com.sun.istack.internal.NotNull;

/**
 * Ikszor Manager Util 
 * @author Gerviba
 * @see IkszorManager#convertManual
 */
public class IkszorManager {
	
	/**
	 * The valid hash types
	 * @see IkszorKeyGenerator#generateHashedKey
	 */
	public static final String[] HASHTYPES = new String[] {"SHA-1", "SHA-256", "SHA-384", "SHA-512", "MD2", "MD5"};
	
	/**
	 * Convert String to Boolean Array
	 * @param convertable The input String
	 * @return A boolean array
	 * @throws IkszorConvertException 
	 */
	public static boolean[] string2BooleanArray(@NotNull String convertable) throws IkszorConvertException {
		boolean[] result = new boolean[convertable.length() * 8];
		
		try {
			int real = 0;
			for(byte b : convertable.getBytes()) {
				int val = b; 
			    for(int i = 0; i < 8; i++) {
			    	result[real+i] = (val & 128) == 0 ? false : true;
			        val <<= 1;
			    }
			    real += 8;
			}
		} catch(Exception e) {
			throw new IkszorConvertException(e);
		}

		return result;
	}

	/**
	 * Convert Boolean Array to String
	 * @param input The input Boolean array
	 * @throws IkszorConvertException
	 * @return The converted String
	 */
	public static String booleanArray2String(boolean[] input) throws IkszorConvertException {
		String result = "";
		for(int i = 0;i < input.length;i += 8) {
			int currentChar = 0;
			for (int bit = 0;bit < 8;++bit) {
			    currentChar = (currentChar << 1) + (input[i+bit] ? 1 : 0);
			}
			result += (char) currentChar;
		}
		return result;
	}
	
	/**
	 * Convert Boolean Array to String
	 * @param input The input Boolean array
	 * @param skip Number of the skipped bytes
	 * @throws IkszorConvertException
	 * @return The converted String
	 */
	public static String booleanArray2String(boolean[] input, int skip) throws IkszorConvertException {
		skip = input.length - (IkszorManager.booleanArray2Integer(input, 32) * 8);
		String result = "";
		for(int i = skip;i < input.length;i += 8) {
			int currentChar = 0;
			for (int bit = 0;bit < 8;++bit) {
			    currentChar = (currentChar << 1) + (input[i+bit] ? 1 : 0);
			}
			result += (char) currentChar;
		}
		return result;
	}
	
	/**
	 * Convert boolean arrax to Integer
	 * @param input The input array
	 * @param length The number of the booleans
	 * @return The selected Integer
	 */
	public static int booleanArray2Integer(boolean[] input, int length) {
		int integer = 0;
		for (int bit = 0;bit < length;++bit)
		    integer = (integer << 1) + (input[bit] ? 1 : 0);
		//System.out.println("booleanArray2Integer: "+integer);
		return integer;
	}

	/**
	 * It creates the array to be recurring.
	 * @param input The input array
	 * @param i The position od the boolean
	 * @return The correct boolean
	 */
	public static boolean safeKey(boolean[] input, int i) {
		/*while(i > input.length) 
			i -= input.length;
		return input[i];*/
		return input[i % input.length];
	}
	
	/**
	 * Generate the hash of the intered String
	 * @param algo Algorithm of the hash (e.g.: MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512)
	 * @param hash The input String
	 * @return The hashed String
	 * @throws IkszorException
	 */
	public static String getHashHex(@NotNull String algo, @NotNull String hash) throws IkszorException {
		try {
			MessageDigest md = MessageDigest.getInstance(algo);
			md.update(hash.getBytes());
			
			byte byteData[] = md.digest();
			 
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		    }
		 
			return sb.toString().toUpperCase();
		} catch(Exception e) {
			throw new IkszorException("Error while getting hash! (algo="+algo+")", e);
		}
	}
	
	/**
	 * Generate the hash of the intered String
	 * @param algo Algorithm of the hash (e.g.: MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512)
	 * @param hash The input String
	 * @return The hashed String
	 * @throws IkszorException
	 */
	public static String getHashStr(@NotNull String algo, @NotNull String hash) throws IkszorException {
		try {
			MessageDigest md = MessageDigest.getInstance(algo);
			md.update(hash.getBytes());
			String result = "";
			
			for(byte b : md.digest()) {
				result += (char) b;
			}
			 
			return result;
		} catch(Exception e) {
			throw new IkszorException("Error while getting hash! (algo="+algo+")", e);
		}
	}
	
	/**
	 * Rotate the input String
	 * @param input The string
	 * @param rotate The rotation number
	 * @return The rotated String (e.g.: (rotate=3) ABCDEF -> DEFABC)
	 */
	public static String rotate(@NotNull String input, long rotate) {
		rotate = rotate % input.length();
		if(rotate == 0 || rotate > Integer.MAX_VALUE) 
			return input;
		return input.substring((int) (input.length()-rotate)) + input.substring(0, (int) (input.length()-rotate));
	}
	
	/**
	 * Convert inputs manual (without Object)
	 * @param input ENCODED or DECODED value
	 * @param key SYMMETRIC KEY
	 * @return The converted String
	 * @throws IkszorConvertException 
	 */
	public static String convertManual(@NotNull String input, @NotNull String key) throws IkszorConvertException {
		try {
			boolean[] symmetric = IkszorManager.string2BooleanArray(key);
			boolean[] value = IkszorManager.string2BooleanArray(input);
			boolean[] result = new boolean[value.length];
			
			for(int i = 0;i < value.length;i++)
				result[i] = value[i] != IkszorManager.safeKey(symmetric, i);
			
			return IkszorManager.booleanArray2String(result);
		} catch(NullPointerException e) {
			throw new IkszorConvertException(true, e);
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new IkszorConvertException(true, e);
		}
	}
	
	/**
	 * Convert inputs manual (without Object)
	 * @param input ENCODED or DECODED value
	 * @param key SYMMETRIC KEY
	 * @return The converted String
	 * @throws IkszorConvertException 
	 */
	public static String convertManual(boolean[] input, boolean[] key) throws IkszorConvertException {
		try {
			boolean[] result = new boolean[input.length];
			
			for(int i = 0;i < input.length;i++)
				result[i] = input[i] != IkszorManager.safeKey(key, i);
			
			return IkszorManager.booleanArray2String(result);
		} catch(NullPointerException e) {
			throw new IkszorConvertException(true, e);
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new IkszorConvertException(true, e);
		}
	}
	
	/**
	 * Convert inputs manual (without Object)
	 * @param input ENCODED or DECODED value
	 * @param key SYMMETRIC KEY
	 * @return The converted String
	 * @throws IkszorConvertException 
	 */
	public static String convertByte(byte input, @NotNull String key) throws IkszorConvertException {
		try {
			boolean[] symmetric = IkszorManager.string2BooleanArray(key);
			boolean[] value = IkszorManager.byte2BooleanArray(input);
			boolean[] result = new boolean[8];
			
			for(int i = 0;i < 8;i++)
				result[i] = value[i] != IkszorManager.safeKey(symmetric, i);
			
			return IkszorManager.booleanArray2String(result);
		} catch(NullPointerException e) {
			throw new IkszorConvertException(true, e);
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new IkszorConvertException(true, e);
		}
	}
	
	/**
	 * Convert String to hex
	 * @param input The input String
	 * @return Converted HEX String
	 * @throws UnsupportedEncodingException
	 */
	public static String toHex(@NotNull String input) throws UnsupportedEncodingException {
		return String.format("%040x", new BigInteger(1, input.getBytes("UTF-8")));
	}
	
	/**
	 * Convert boolean[] to hex
	 * @param input The input array
	 * @return Converted HEX String
	 */
	public static String toHex(boolean[] input) {
		return String.format("%040x", new BigInteger(1, booleanArray2ByteArray(input)));
	}
	
	/**
	 * Convert boolean[] to byte[]
	 * @param input The input array
	 * @return The converted byte array
	 */
	public static byte[] booleanArray2ByteArray(boolean[] input) {
		byte[] result = new byte[input.length / 8];
		
		for(int i = 0;i < input.length;i += 8) {
			//result[i / 8] = 0;
		    for(int j = 0; j < 8; j++) {
		    	if(input[i+j])
		    		result[i / 8] |= (1 << Math.abs(j-7));
		    	else
		    		result[i / 8] &= ~(1 << Math.abs(j-7));
		    }
		}
		return result;
	}
	
	/**
	 * Convert byte to boolean array
	 * @param input The input byte
	 * @return The converted boolean array
	 */
	public static boolean[] byte2BooleanArray(byte input){
	    boolean[] result = new boolean[8];
		for(int i = 0; i < 8; i++) {
	    	result[i] = (input & 128) == 0 ? false : true;
	    	input <<= 1;
	    }
		return result;
	}

	/**
	 * Convert binary String to boolean array
	 * @param input The input String (only contains: 0 and or 1)
	 * @return The converted boolean array
	 */
	public static boolean[] binary2BooleanArray(@NotNull String input) {
		boolean[] result = new boolean[input.length()];
		int id = 0;
		for(char c : input.toCharArray()) {
			result[id] = c == '1'; 
			++id;
	    }
		return result;
	}

	/**
	 * Fill the useless part of the code
	 * @param value The useable part
	 * @param symmetricKey The symmetric encryption key
	 * @param uselessPart The useless part (null = generate, using usable part)
	 * @param length The used length (e.g.: First 4 byte = 32 bit)
	 * @return The filled full String
	 * @throws IkszorException
	 */
	public static String fillValue(@NotNull String value, @NotNull String symmetricKey, String uselessPart, byte length) throws IkszorException {
		if(uselessPart == null) {
			uselessPart = IkszorKeyGenerator.generateHashedKey(value, symmetricKey.length()-value.length()-length);
		}
		return uselessPart + value;
	}
	
	/**
	 * Fill the useless part of the code
	 * @param value The useable part
	 * @param symmetricKey The symmetric encryption key
	 * @param uselessPart The useless part (null = generate, using usable part)
	 * @param length The used length (e.g.: First 4 byte = 32 bit)
	 * @return The filled full boolean array
	 * @throws IkszorException
	 * @throws IkszorConvertException 
	 */
	public static boolean[] fillValue(@NotNull boolean[] value, @NotNull boolean[] symmetricKey, boolean[] uselessPart, byte length) throws IkszorException, IkszorConvertException {
		if(uselessPart == null) {
			uselessPart = IkszorManager.string2BooleanArray(IkszorKeyGenerator.generateHashedKey(IkszorManager.booleanArray2String(value), (symmetricKey.length-value.length) / 8 - length));
		} else if(uselessPart.length > symmetricKey.length - (length * 8)) {
			uselessPart = IkszorManager.cutArray(uselessPart, uselessPart.length - ((symmetricKey.length-value.length) / 8 - length));
		} else if(uselessPart.length < symmetricKey.length - (length * 8)) {
			boolean[] temp = uselessPart;
			int len = 0;
			do {
				uselessPart = IkszorManager.mergeArrays(uselessPart, uselessPart);
			} while(len < ((symmetricKey.length-value.length) / 8 - length));
			uselessPart = IkszorManager.cutArray(temp, temp.length - ((symmetricKey.length-value.length) / 8 - length));
		}
		return IkszorManager.mergeArrays(uselessPart, value);
	}

	/**
	 * Convert a boolean array to String
	 * @param input The convertable array
	 * @return The converted String
	 */
	public static String booleanArray2BinaryString(boolean[] input) {
		if(input == null) return null;
		String result = "";
		for(boolean b : input)
			result += b ? "1" : "0";
		return result;
	}
	
	/**
	 * Cut the beginning of the array
	 * @param input The input array
	 * @param skip Skipped bits
	 * @return The cut array
	 */
	public static boolean[] cutArray(boolean[] input, int skip) {
		boolean[] result = new boolean[input.length-skip];
		for(int i = 0;i < input.length-skip;i++)
			result[i] = input[skip+i];
		return result;
	}
	
	/**
	 * Merge Arrays
	 * @param firstArray
	 * @param secondArray
	 * @return The merged arrays
	 */
	public static boolean[] mergeArrays (boolean[] firstArray, boolean[] secondArray) {
	    int fLen = firstArray.length;
	    int sLen = secondArray.length;

	    boolean[] result = (boolean[]) Array.newInstance(firstArray.getClass().getComponentType(), fLen+sLen);
	    System.arraycopy(firstArray, 0, result, 0, fLen);
	    System.arraycopy(secondArray, 0, result, fLen, sLen);

	    return result;
	}

}
