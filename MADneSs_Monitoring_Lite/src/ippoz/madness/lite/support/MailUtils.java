/**
 * 
 */
package ippoz.madness.lite.support;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author Tommy
 *
 */
public class MailUtils {
	
	public static boolean sendMailSSL(String recipentAddress, File attachment) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("rclmonitoringtool@gmail.com","unifirclaqs");
				}
			});

		try {

	         Message message = new MimeMessage(session);
	         message.setFrom(new InternetAddress("rclmonitoringtool@gmail.com"));
	         message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipentAddress));
	         message.setSubject("Log of RCL Monitoring Tool");

	         BodyPart messageBodyPart = new MimeBodyPart();
	         messageBodyPart.setText("Experiment executed in data " + new Date().toString());
	         
	         Multipart multipart = new MimeMultipart();
	         multipart.addBodyPart(messageBodyPart);
	         messageBodyPart = new MimeBodyPart();
	         DataSource source = new FileDataSource(attachment);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(attachment.getName());
	         multipart.addBodyPart(messageBodyPart);

	         message.setContent(multipart);
			Transport.send(message);

			return true;

		} catch (MessagingException e) {
			return false;
		}
	}

}
