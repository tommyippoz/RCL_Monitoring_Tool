/**
 * 
 */
package ippoz.madness.lite.support;

import java.io.File;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * @author Tommy
 *
 */
public class MADneSsLiteSupport {
	
	public static boolean isValidEmailAddress(String email) {
		if (email == null || "".equals(email))
			return false;
		
		email = email.trim();
		
		EmailValidator ev = EmailValidator.getInstance();
		return ev.isValid(email);
	}

	public static boolean isValidShellCommand(String text) {
		return text != null && text.trim().length() > 0;
	}

	public static boolean isInteger(String text) {
		try { 
	        Integer.parseInt(text); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}

	public static boolean isValidPath(String pValue) {
		File file = new File(pValue);
		return file.exists();
	}

	public static boolean isBoolean(String text) {
		try { 
	        Boolean.parseBoolean(text); 
	    } catch(Exception e) { 
	        return false; 
	    }
	    return true;
	}

}
