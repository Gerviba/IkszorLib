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
package hu.Gerviba.IkszorLib;

import hu.Gerviba.IkszorLib.Exceptions.IkszorConvertException;
import hu.Gerviba.IkszorLib.Exceptions.IkszorException;
import hu.Gerviba.IkszorLib.Utils.IkszorManager;

/**
 * IkszorSecureObject
 * @author Gerviba
 * @see encode
 * @see decode
 */
public class IkszorSecureObject {

	private String encodedValue = null;
	private String symmetricKey = null;
	private String decodedValue = null;
	
	private boolean isEncoded = false;
	private boolean isDecoded = false;

	/**
	 * IkszorObject constructor
	 * @param encoded The encoded String (or null, if you don't know)
	 * @param key The symmetric key of the Object (or null, if you don't know)
	 * @param decoded The decoded String (or null, if you don't know)
	 */
	public IkszorSecureObject(String encoded, String key, String decoded) {
		this.encodedValue = encoded;
		this.symmetricKey = key;
		this.decodedValue = decoded;
		this.isEncoded = encoded != null;
		this.isDecoded = decoded != null;
	}
	
	/**
	 * IkszorObject constructor
	 * @param encoded The encoded String (or null, if you don't know)
	 * @param key The symmetric key of the Object (or null, if you don't know)
	 * @param decoded The decoded String (or null, if you don't know)
	 * @param isEncoded true, if the decoded value has been encoded
	 * @param isDecoded true, if the encoded value has been decoded
	 */
	public IkszorSecureObject(String encoded, String key, String decoded, boolean isEncoded, boolean isDecoded) {
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
			boolean[] key = IkszorManager.string2BooleanArray(this.symmetricKey);
			boolean[] dValue = IkszorManager.string2BooleanArray(this.decodedValue);
			boolean[] result = new boolean[dValue.length];
			
			for(int i = 0;i < dValue.length;i++)
				result[i] = dValue[i] != IkszorManager.safeKey(key, i);
			
			this.encodedValue = IkszorManager.booleanArray2String(result, 32);
			this.isEncoded = true;
		} catch(NullPointerException e) {
			throw new IkszorConvertException(true, e);
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new IkszorConvertException(true, e);
		}
	}
	
	
	/**
	 * Decoding the encoded value
	 * @param uselessPart A generated String for the useless part (null = generate using the decodable String)
	 * @throws IkszorConvertException
	 */
	public void decode(String useleassPart) throws IkszorConvertException {
		this.isDecoded = false;
		try {
			boolean[] key = IkszorManager.string2BooleanArray(this.symmetricKey);
			boolean[] eValue = IkszorManager.string2BooleanArray(IkszorManager.fillValue(this.encodedValue, this.symmetricKey, useleassPart, (byte) 32));
			System.arraycopy(IkszorManager.binary2BooleanArray("00000000000000000000000000000000".
					substring(Integer.toBinaryString(this.encodedValue.length()).length())+
					Integer.toBinaryString(this.encodedValue.length())), 0, eValue, 0, 32);
			boolean[] result = new boolean[eValue.length];
			
			for(int i = 0;i < eValue.length;i++)
				result[i] = eValue[i] != IkszorManager.safeKey(key, i);
			
			this.decodedValue = IkszorManager.booleanArray2String(result);
			this.isDecoded = true;
		} catch(IkszorException e) {
			throw new IkszorConvertException(false, e);
		} catch(NullPointerException e) {
			throw new IkszorConvertException(false, e);
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new IkszorConvertException(false, e);
		}
	}
	
	/**
	 * Encoded value Getter
	 * @return The encoded value or null if there is no encoded value
	 */
	public String getEncodedValue() {
		return this.encodedValue;
	}

	/**
	 * Encoded value Setter
	 * @param encodedValue The value that will be decoded.
	 */
	public void setEncodedValue(String encodedValue) {
		this.encodedValue = encodedValue;
		this.isEncoded = true;
		this.isDecoded = false;
	}

	/**
	 * Symmetric key Getter
	 * @return The symmetric key or null if there is no key
	 */
	public String getSymmetricKey() {
		return this.symmetricKey;
	}

	/**
	 * Symmetric key Setter
	 * @param symmetricKey
	 */
	public void setSymmetricKey(String symmetricKey) {
		this.symmetricKey = symmetricKey;
	}

	/**
	 * Decoded value Setter
	 * @return The decoded value or null if there is no decoded value
	 */
	public String getDecodedValue() {
		return this.decodedValue;
	}

	/**
	 * Decoded value Setter
	 * @param decodedValue The value that will be encoded.
	 */
	public void setDecodedValue(String decodedValue) {
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
	 * Generate toString() value (for debugging)
	 * @return The values of this Object
	 */
	@Override
	public String toString() {
		return "IkszorSecureObject " +
				"[encodedValue=" + encodedValue + "[" + (decodedValue != null ? decodedValue.length() : "null")+"]" +
				", symmetricKey=" + symmetricKey + "[" + (symmetricKey != null ? symmetricKey.length() : "null")+"]" +
				", decodedValue=" + decodedValue + "[" + (decodedValue != null ? decodedValue.length() : "null")+"]" +
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
		result = prime * result + ((decodedValue == null) ? 0 : decodedValue.hashCode());
		result = prime * result + ((encodedValue == null) ? 0 : encodedValue.hashCode());
		result = prime * result + ((symmetricKey == null) ? 0 : symmetricKey.hashCode());
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
		IkszorSecureObject other = (IkszorSecureObject) obj;
		if (decodedValue == null) {
			if (other.decodedValue != null)
				return false;
		} else if (!decodedValue.equals(other.decodedValue))
			return false;
		if (encodedValue == null) {
			if (other.encodedValue != null)
				return false;
		} else if (!encodedValue.equals(other.encodedValue))
			return false;
		if (symmetricKey == null) {
			if (other.symmetricKey != null)
				return false;
		} else if (!symmetricKey.equals(other.symmetricKey))
			return false;
		return true;
	}
}