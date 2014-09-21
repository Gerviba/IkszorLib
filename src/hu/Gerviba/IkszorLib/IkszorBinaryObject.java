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
package hu.Gerviba.IkszorLib;

import java.util.Arrays;

import hu.Gerviba.IkszorLib.Exceptions.IkszorConvertException;
import hu.Gerviba.IkszorLib.Exceptions.IkszorInvalidKeyLengthException;
import hu.Gerviba.IkszorLib.Utils.IkszorManager;

/**
 * IkszorBinaryObject
 * @author Gerviba
 * @see encode
 * @see decode
 */
public class IkszorBinaryObject {

	private boolean[] encodedValue = null;
	private boolean[] symmetricKey = null;
	private boolean[] decodedValue = null;
	
	private boolean isEncoded = false;
	private boolean isDecoded = false;

	/**
	 * IkszorBinaryObject constructor
	 * @param encoded The encoded boolean[] (or null, if you don't know)
	 * @param key The symmetric key of the Object (or null, if you don't know)
	 * @param decoded The decoded boolean[] (or null, if you don't know)
	 */
	public IkszorBinaryObject(boolean[] encoded, boolean[] key, boolean[] decoded) {
		this.encodedValue = encoded;
		this.symmetricKey = key;
		this.decodedValue = decoded;
		this.isEncoded = encoded != null;
		this.isDecoded = decoded != null;
	}
	
	/**
	 * IkszorBinaryObject constructor
	 * @param encoded The encoded boolean[] (or null, if you don't know)
	 * @param key The symmetric key of the Object (or null, if you don't know)
	 * @param decoded The decoded boolean[] (or null, if you don't know)
	 * @param isEncoded true, if the decoded value has been encoded
	 * @param isDecoded true, if the encoded value has been decoded
	 */
	public IkszorBinaryObject(boolean[] encoded, boolean[] key, boolean[] decoded, boolean isEncoded, boolean isDecoded) {
		this.encodedValue = encoded;
		this.symmetricKey = key;
		this.decodedValue = decoded;
		this.isEncoded = isEncoded;
		this.isDecoded = isDecoded;
	}
	
	/**
	 * Encoding the decoded value
	 * @throws IkszorConvertException
	 */
	public void encode() throws IkszorConvertException {
		this.isEncoded = false;
		try {
			boolean[] result = new boolean[this.decodedValue.length];
			
			for(int i = 0;i < this.decodedValue.length;i++)
				result[i] = this.decodedValue[i] != IkszorManager.safeKey(this.symmetricKey, i);
			
			this.encodedValue = result;
			this.isEncoded = true;
		} catch(NullPointerException e) {
			throw new IkszorConvertException(true, e);
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new IkszorConvertException(true, e);
		}
	}
	
	/**
	 * Encoding the decoded value
	 * @param fixKey The length of the key can't be different than the encodable String
	 * @throws IkszorConvertException
	 * @throws IkszorInvalidKeyLengthException
	 */
	public void encode(boolean fixKey) throws IkszorInvalidKeyLengthException, IkszorConvertException {
		if(fixKey && this.symmetricKey.length != this.decodedValue.length)
			throw new IkszorInvalidKeyLengthException(true);
		encode();
	}
	
	/**
	 * Decoding the encoded value
	 * @throws IkszorConvertException
	 */
	public void decode() throws IkszorConvertException {
		this.isDecoded = false;
		try {
			boolean[] result = new boolean[this.encodedValue.length];
			
			for(int i = 0;i < this.encodedValue.length;i++)
				result[i] = this.encodedValue[i] != IkszorManager.safeKey(this.symmetricKey, i);
			
			this.decodedValue = result;
			this.isDecoded = true;
		} catch(NullPointerException e) {
			throw new IkszorConvertException(false, e);
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new IkszorConvertException(false, e);
		}
	}
	
	/**
	 * Decoding the encoded value
	 * @param fixKey The length of the key can't be different than the decodable String
	 * @throws IkszorConvertException
	 * @throws IkszorInvalidKeyLengthException
	 */
	public void decode(boolean fixKey) throws IkszorInvalidKeyLengthException, IkszorConvertException {
		if(fixKey && this.symmetricKey.length != this.encodedValue.length)
			throw new IkszorInvalidKeyLengthException(false);
		decode();
	}

	/**
	 * Encoded value Getter
	 * @return The encoded value or null if there is no encoded value
	 */
	public boolean[] getEncodedValue() {
		return this.encodedValue;
	}

	/**
	 * Encoded value Setter
	 * @param encodedValue The value that will be decoded.
	 */
	public void setEncodedValue(boolean[] encodedValue) {
		this.encodedValue = encodedValue;
		this.isEncoded = true;
		this.isDecoded = false;
	}

	/**
	 * Symmetric key Getter
	 * @return The symmetric key or null if there is no key
	 */
	public boolean[] getSymmetricKey() {
		return this.symmetricKey;
	}

	/**
	 * Symmetric key Setter
	 * @param symmetricKey
	 */
	public void setSymmetricKey(boolean[] symmetricKey) {
		this.symmetricKey = symmetricKey;
	}

	/**
	 * Decoded value Setter
	 * @return The decoded value or null if there is no decoded value
	 */
	public boolean[] getDecodedValue() {
		return this.decodedValue;
	}

	/**
	 * Decoded value Setter
	 * @param decodedValue The value that will be encoded.
	 */
	public void setDecodedValue(boolean[] decodedValue) {
		this.decodedValue = decodedValue;
		this.isEncoded = false;
		this.isDecoded = true;
	}
	
	/**
	 * Is it a total object?
	 * @return true, if the decoded end the encoded value is correct (and the symmetric key != null)
	 */
	public boolean isTotal() {
		return this.isEncoded && this.isDecoded && this.symmetricKey != null;
	}

	/**
	 * Is this Object is already encoded?
	 * @return true, if it has been encoded
	 */
	public boolean isEncoded() {
		return this.isEncoded;
	}

	/**
	 * Is this Object is already decoded?
	 * @return true, if it has been decoded
	 */
	public boolean isDecoded() {
		return this.isDecoded;
	}

	/**
	 * Convert to (!Binary) Ikszor Object
	 * @return A new (cloned) IkszorObject 
	 * @throws IkszorConvertException
	 */
	public IkszorObject toIkszorObject() throws IkszorConvertException {
		return new IkszorObject(
				IkszorManager.booleanArray2String(encodedValue),
				IkszorManager.booleanArray2String(symmetricKey), 
				IkszorManager.booleanArray2String(decodedValue), isEncoded, isDecoded);
	}
	
	/**
	 * Generate toString() value (for debugging)
	 * @return The values of this Object
	 */
	@Override
	public String toString() {
		return "IkszorBinaryObject " +
				"[encodedValue=" + IkszorManager.booleanArray2BinaryString(encodedValue) + "["+(encodedValue != null ? encodedValue.length : "null")+"]" +
				", symmetricKey=" + IkszorManager.booleanArray2BinaryString(symmetricKey) + "["+(symmetricKey != null ? symmetricKey.length : "null")+"]" +
				", decodedValue=" + IkszorManager.booleanArray2BinaryString(decodedValue) + "["+(decodedValue != null ? decodedValue.length : "null")+"]" +
				", isEncoded=" + isEncoded +
				", isDecoded=" + isDecoded + "]";
	}

	/**
	 * Hash the Object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(decodedValue);
		result = prime * result + Arrays.hashCode(encodedValue);
		result = prime * result + Arrays.hashCode(symmetricKey);
		return result;
	}

	/**
	 * Check equality
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IkszorBinaryObject other = (IkszorBinaryObject) obj;
		if (!Arrays.equals(decodedValue, other.decodedValue))
			return false;
		if (!Arrays.equals(encodedValue, other.encodedValue))
			return false;
		if (!Arrays.equals(symmetricKey, other.symmetricKey))
			return false;
		return true;
	}
	
}
