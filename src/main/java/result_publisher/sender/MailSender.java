package result_publisher.sender;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * Java mail api - for sending email
 */
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import result_publisher.models.Student;

public class MailSender {

	Properties props;
	Set<Student> studentDetails;
	Session session;

	public MailSender() {
		props = new Properties();
	}

	/*
	 * gets the set of students creates session initiates the sending process by
	 * calling startOperation() method
	 */
	public void sendMail(Set<Student> studentDetails) throws Exception {
		this.studentDetails = studentDetails;

		// loads the neccessary properties to connect to the smtp server from file
		props.load(new FileInputStream("config.properties"));

		session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(props.getProperty("EMAIL"), props.getProperty("PASSWORD"));
			}
		});

		// starts operation
		startOperation();
	}

	// for each student the mail is sent, the looping is achieved with the help of
	// iterator
	private void startOperation() {

		System.out.println("Preparing to send the reports...");

		Iterator<Student> iter = this.studentDetails.iterator();
		while (iter.hasNext()) {
			send(iter.next());
		}

		System.out.println("----------------------------------------");
	}

	// Creates a message [i.e via createMessage()] and sends message
	private void send(Student s) {

		try {
			Message message = createMessage(s); // calls the createMessage() method
			Transport.send(message);
			System.out.printf("Message successfully sent to <%s>,\t%s\n", s.getParentEmail(), s.getParentName());
		} catch (Exception e) {
			System.out.printf("Failed to send message to : <%s>\t\t\t", s.getParentEmail());
			System.out.println("Problem : " + e.getMessage());
		}
	}

	// Constructs(creates) the message
	private Message createMessage(Student s) {

		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(props.getProperty("EMAIL")));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(s.getParentEmail()));
			message.setSubject("Results Have been Published for the academic year 2020-2021");

			Multipart multiPart = new MimeMultipart();

			// text part
			MimeBodyPart text = new MimeBodyPart();
			text.setText(MessageBody.getEmailBody(s)); // gets the message body from MessageBody class

			// excel attachment
			MimeBodyPart excel = new MimeBodyPart();
			excel.attachFile(s.getExcelFileLocation());

			
			/**
			 *  if the student is qualified then attach certificate
			 */
			{
				if (s.getPassed().equalsIgnoreCase("YES")) {

					// certificate attachment
					MimeBodyPart file2 = new MimeBodyPart();
					file2.attachFile(s.getCertificateLocation());
					multiPart.addBodyPart(file2);
				}
			}

			multiPart.addBodyPart(text);
			multiPart.addBodyPart(excel);

			message.setContent(multiPart);

			return message;
		} catch (Exception e) {
			System.out.printf("SOME ERROR OCCURED IN CREATING EMAIL FOR %s!!!", s.getParentEmail());
			e.printStackTrace();
		}

		return message;
	}

}