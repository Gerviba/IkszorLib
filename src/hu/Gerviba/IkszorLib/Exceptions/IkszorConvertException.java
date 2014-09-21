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
package hu.Gerviba.IkszorLib.Exceptions;

/**
 * IkszorConvertException
 * @author Gerviba
 */
public class IkszorConvertException extends Exception {
	
	private static final long serialVersionUID = 4454507886831084347L;

	public IkszorConvertException(Throwable t) {
    	super("An Exception occurred while converting!", t);
    }
	
	public IkszorConvertException(boolean encodeing, Throwable t) {
    	super("An Exception occurred while "+(encodeing ? "encoding!" : "decoding!"), t);
    }
}
