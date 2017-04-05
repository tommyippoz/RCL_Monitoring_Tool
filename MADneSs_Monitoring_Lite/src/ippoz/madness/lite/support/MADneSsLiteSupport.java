/**
 * 
 */
package ippoz.madness.lite.support;

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
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return false;
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
