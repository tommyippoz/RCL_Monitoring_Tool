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

}
