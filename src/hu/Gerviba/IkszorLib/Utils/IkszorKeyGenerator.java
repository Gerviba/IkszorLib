/**
 * This file is under Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International license
 * (CC BY-NC-SA 4.0)
 * @author Szabó Gergely (Gerviba)
 * @date 2014-09-21
 * 
 * You are free to:
 *	Share —			Copy and redistribute the material in any medium or format
 *	Adapt — 		Remix, transform, and build upon the material
 *	
 * Under the following terms:
 *	Attribution — 	You must give appropriate credit, provide a link to the license, and indicate 
 *					if changes were made. You may do so in any reasonable manner, but not in any way that suggests 
 *					the licensor endorses you or your use.
 *	NonCommercial — You may not use the material for commercial purposes.
 *	ShareAlike — 	If you remix, transform, or build upon the material, you must 
 *					distribute your contributions under the same license as the original.
 * @see http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */
package hu.Gerviba.IkszorLib.Utils;

import hu.Gerviba.IkszorLib.Exceptions.IkszorException;

import java.util.Random;

import com.sun.istack.internal.NotNull;

/**
 * Key Generator Util
 * @author Gerviba
 */
public class IkszorKeyGenerator {

	/**
	 * Generate a key with a seed
	 * @param seed The seed of the random generator
	 * @param length The length of the generated String
	 * @return The generated (!HEX) String
	 */
	public static String generateSeededKey(long seed, int length) {
		String result = "";
		Random r = new Random(seed);
		for(int i = 0;i < length;i++)
			result += (char) r.nextInt(255);
		return result;
	}
	
	/**
	 * Generate a key with the attached Random generator
	 * @param r The Random generator
	 * @param length The length of the generated String
	 * @return The generated (!HEX) String
	 */
	public static String generateSeededKey(Random r, int length) {
		String result = "";
		for(int i = 0;i < length;i++)
			result += (char) r.nextInt(255);
		return result;
	}
	
	/**
	 * Generate a hex key with a seed
	 * @param seed The seed of the random generator
	 * @param length The length of the generated String
	 * @return The generated (HEX) String
	 */
	public static String generateSeededHex(long seed, int length) {
		String result = "";
		Random r = new Random(seed);
		for(int i = 0;i < length;i++)
			result += Integer.toHexString(r.nextInt(15));
		return result;
	}
	
	/**
	 * Generate a hex key with the attached Random generator
	 * @param r The Random generator
	 * @param length The length of the generated String
	 * @return The generated (HEX) String
	 */
	public static String generateSeededHex(Random r, int length) {
		String result = "";
		for(int i = 0;i < length;i++)
			result += Integer.toHexString(r.nextInt(15));
		return result;
	}
	
	/**
	 * Generate Unique Hex String (64 char long)
	 * @param salt The salt of the generator (or null)
	 * @return Unique 64 long Hex String
	 * @throws IkszorException
	 */
	public static String generateUnique64Hex(String salt) throws IkszorException {
		if(salt == null) salt = "";
		return IkszorManager.getHashHex("SHA-256", generateSeededKey(System.nanoTime(), 16)+IkszorManager.getHashHex("MD2", ""+System.nanoTime())+IkszorManager.getHashHex("SHA-512", new Random().nextLong()+salt)+salt);
	}
	
	/**
	 * Generate Unique Hex String (128 char long)
	 * @param salt The salt of the generator (or null)
	 * @return Unique 128 long Hex String
	 * @throws IkszorException
	 */
	public static String generateUnique128Hex(String salt) throws IkszorException {
		if(salt == null) salt = "";
		return IkszorManager.getHashHex("SHA-512", generateSeededKey(System.nanoTime(), 32)+IkszorManager.getHashHex("MD5", ""+System.nanoTime())+IkszorManager.getHashHex("SHA-512", new Random().nextLong()+salt)+salt);
	}
	
	/**
	 * Generate Unique Hex String (256 char long)
	 * @param salt The salt of the generator (or null)
	 * @return Unique 256 long Hex String
	 * @throws IkszorException
	 */
	public static String generateUnique256Hex(String salt) throws IkszorException {
		if(salt == null) salt = "";
		return IkszorManager.getHashHex("SHA-256", generateSeededKey(System.nanoTime(), 64)+IkszorManager.getHashHex("SHA-384", ""+System.nanoTime())+IkszorManager.getHashHex("SHA-512", new Random().nextLong()+salt)+salt);
	}
	
	/**
	 * Generate Unique Hex String
	 * @param seed The seed of the Unique Key generation (0 = new Random())
	 * @param length The length of the generation
	 * @return Unique (!HEX) Key
	 * @throws IkszorException
	 */
	public static String generateUniqueKey(long seed, int length) throws IkszorException {
		String result = "";
		Random r = seed == 0 ? new Random() : new Random(seed);
		while(result.length() < length) {
			switch(r.nextInt(24)) {
				case 1:
					result += IkszorManager.getHashHex("SHA-1", System.currentTimeMillis()+"").substring(10);
					break;
				case 2:
					result += IkszorManager.getHashHex("SHA-1", System.nanoTime()+"").substring(10);
					break;
				case 3:
					result += IkszorManager.getHashHex("SHA-1", r.nextLong()+"").substring(10);
					break;
				case 4:
					result += IkszorManager.getHashHex("SHA-256", System.currentTimeMillis()+"").substring(22);
					break;
				case 5:
					result += IkszorManager.getHashHex("SHA-256", System.nanoTime()+"").substring(22);
					break;
				case 6:
					result += IkszorManager.getHashHex("SHA-256", r.nextLong()+"").substring(22);
					break;
				case 7:
					result += IkszorManager.getHashHex("SHA-384", System.currentTimeMillis()+"").substring(38);
					break;
				case 8:
					result += IkszorManager.getHashHex("SHA-384", System.nanoTime()+"").substring(38);
					break;
				case 9:
					result += IkszorManager.getHashHex("SHA-384", r.nextLong()+"").substring(38);
					break;
				case 10:
					result += IkszorManager.getHashHex("SHA-512", System.currentTimeMillis()+"").substring(54);
					break;
				case 11:
					result += IkszorManager.getHashHex("SHA-512", System.nanoTime()+"").substring(54);
					break;
				case 12:
					result += IkszorManager.getHashHex("SHA-512", r.nextLong()+"").substring(54);
					break;
				case 13:
					result += IkszorManager.getHashHex("MD5", System.currentTimeMillis()+"").substring(2);
					break;
				case 14:
					result += IkszorManager.getHashHex("MD5", System.nanoTime()+"").substring(2);
					break;
				case 15:
					result += IkszorManager.getHashHex("MD5", r.nextLong()+"").substring(2);
					break;
				case 16:
					result += IkszorManager.getHashHex("MD2", System.currentTimeMillis()+"").substring(2);
					break;
				case 17:
					result += IkszorManager.getHashHex("MD2", System.nanoTime()+"").substring(2);
					break;
				case 18:
					result += IkszorManager.getHashHex("MD2", r.nextLong()+"").substring(2);
					break;
				default:
					result += (char) r.nextInt(255);
			}
			
		}
		return result.substring(0, length);
	}
	
	/**
	 * Generate input based key (64 long)
	 * @param hashable The input String (e.g.: a password)
	 * @param rehash Nuber of rehashing
	 * @return The (64 long) input based hex key
	 * @throws IkszorException
	 */
	public static String generateHashed64Hex(@NotNull String hashable, int rehash) throws IkszorException {
		while(rehash != 0) {
			hashable = IkszorManager.getHashHex("SHA-256", hashable);
			hashable = IkszorManager.rotate(hashable, Long.parseLong(hashable.substring(49), 16));
			--rehash;
		}
		return hashable;
	}
	
	/**
	 * Generate input based key (128 long)
	 * @param hashable The input String (e.g.: a password)
	 * @param rehash Nuber of rehashing
	 * @return The (128 long) input based hex key
	 * @throws IkszorException
	 */
	public static String generateHashed128Hex(@NotNull String hashable, int rehash) throws IkszorException {
		while(rehash != 0) {
			hashable = IkszorManager.getHashHex("SHA-512", hashable);
			hashable = IkszorManager.rotate(hashable, Long.parseLong(hashable.substring(113), 16));
			--rehash;
		}
		return hashable;
	}
	
	/**
	 * Generate input based key (256 long)
	 * @param hashable The input String (e.g.: a password)
	 * @param rehash Nuber of rehashing
	 * @return The (256 long) input based hex key
	 * @throws IkszorException
	 */
	public static String generateHashed256Hex(@NotNull String hashable, int rehash) throws IkszorException {
		while(rehash != 0) {
			hashable = IkszorManager.getHashHex("SHA-512", hashable)+IkszorManager.getHashHex("SHA-384", hashable)+IkszorManager.getHashHex("MD5", hashable);
			hashable = IkszorManager.rotate(hashable, Long.parseLong(hashable.substring(241), 16));
			--rehash;
		}
		return hashable;
	}
	
	/**
	 * Generate input based key
	 * @param hashable The input String (e.g.: a password)
	 * @param length The length of the key
	 * @return The input based (!HEX) key
	 * @throws IkszorException
	 */
	public static String generateHashedKey(@NotNull String hashable, int length) throws IkszorException {
		String result = IkszorManager.getHashHex("MD2", hashable).substring(0, 1);
		do {
			result += IkszorManager.getHashHex(IkszorManager.HASHTYPES[((int)result.charAt(0)) % 6], result+hashable).charAt(0);
			result = IkszorManager.rotate(result, Long.parseLong(result.substring(0, 1), 16));
		} while(result.length() < length);
		return result.substring(0, length);
	}
	
	
}