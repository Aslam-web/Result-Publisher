package result_publisher.sender;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import result_publisher.models.Student;

public class SMSSender {

	private Properties props;
	private Set<Student> studentEntries;

	public SMSSender() throws IOException {
		props = new Properties();
		props.load(new FileInputStream("config.properties"));
	}

	/*
	 * constructs the sending url and calls the sendHttpRequestMethod and pass the
	 * url to it
	 */
	public void sendMessage(Set<Student> studentEntries) throws IOException {
		this.studentEntries = studentEntries;

		String sendingLink = "https://www.fast2sms.com/dev/bulkV2?authorization=" + props.getProperty("MY_API_KEY");

		System.out.print("Sending SMS to parents... \t\t");

		Iterator<Student> iter = this.studentEntries.iterator();
		while (iter.hasNext()) {
			// get the students one by one
			Student s = iter.next();

			// build the message
			String ID = "&sender_id=TXTIND";
			String message = "&message=" + URLEncoder.encode(MessageBody.getSMSBody(s), "UTF-8");
			String language = "&language=english";
			String route = "&route=v3";
			String number = "&numbers=" + s.getParentMobile();
			
			// pass the link to sending method
			sendHttpRequest(sendingLink + ID + message + language + route + number);
		}

		System.out.println("Sent successfully !");
	}

	/*
	 * constructs the balance url and calls the sendHttpRequestMethod and pass the
	 * url to it
	 */
	public void checkBalance() throws IOException {
		final String balanceLink = "https://www.fast2sms.com/dev/wallet?authorization="
				+ props.getProperty("MY_API_KEY");

		// pass the link to sending method
		System.out.println("\n------------------------SMS Balance----------------------------");
		System.out.println(sendHttpRequest(balanceLink));
	}

	/*
	 * whatever link is passed to this method , it will send a http request to the
	 * server
	 */
	private StringBuffer sendHttpRequest(String link) throws IOException {
		URL url = new URL(link);

		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("cache-control", "no-cache");

		StringBuffer response = new StringBuffer();

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

		while (true) {
			String line = br.readLine();
			if (line == null) {
				break;
			}
			response.append(line);
		}

//			System.out.println(response);

		return response;
	}
}