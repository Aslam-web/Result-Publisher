package result_publisher.sender;

import result_publisher.models.Student;

class MessageBody {

	public static String getEmailBody(Student s) {
		String result = s.getPassed().equals("YES") ? "PASSED" : "FAILED";
		String message = "Dear Mr/Mrs."+s.getParentName()+",\n"
				+ "\tThe results have been pulished for the academic year 2020-2021 for all the students. "
				+ "your son/daughter "+s.getStudentName()+" has scored "+s.getTotalMarks()+" and have "+result+" their secondary school. "
				+ "Report card and further details have been attached as an excel file below"
				
				+ "\n\n\nThanks & Regards"
				+ "\nController of Examination,"
				+ "\nWebsite : https://www.myschool.com/"
				+ "\nEmail : myschool@school.com,"
				+ "\nPhone: +91 98765 4321";
		
		return message;
	}

	public static String getSMSBody(Student s) {
		String message = "Dear Mr/Mrs."+s.getParentName()+","
				+ "\nResults Published. "
				+ "Kindly Check your email and use the following OTP to open the attachment"
				+ "\n"+ s.getOTP();
		
		return message;
	}
}
