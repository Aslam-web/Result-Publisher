package result_publisher;

import java.util.Set;

import result_publisher.creators.CertificateCreator;
import result_publisher.creators.ExcelCreator;
import result_publisher.models.Certificate_One;
import result_publisher.models.Student;
import result_publisher.sender.MailSender;
import result_publisher.sender.SMSSender;

public class App {
	
	public final static String DATA_STORE = "data/";

	public static void main(String[] args) throws Exception {

		// 1. create excel file for all 	the Strudents
		ExcelCreator excelCreator = new ExcelCreator();
		Set<Student> studentEntries = excelCreator.createExcelForStudents("data/data2.xls");
		
		// 2. create certificates for the qualified students
		CertificateCreator certificateCreator = new CertificateCreator(new Certificate_One());
		certificateCreator.create(studentEntries);

		// 3. send the files to all the parents
		MailSender mailSender = new MailSender();
		mailSender.sendMail(studentEntries);
		
		// 4. Send the password for that file as a SMS
		SMSSender smsSender = new SMSSender();
		smsSender.sendMessage(studentEntries);

//		// printing read data
//		ExcelCreator.print(studentEntries);
//
//		// For checking balance
//		new SMSSender().checkBalance();
	}
}