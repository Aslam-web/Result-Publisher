package result_publisher.models;

public class Student {
	// private - secure
	private String studentName;
	private long studentRoll;

	private int mathMark,scienceMark,socialMark,englishMark,language2Mark,totalMarks;
	private String passed;
	
	private String parentName,parentEmail;
	private long parentMobile;
	private String lockerNum,lockerPass,OTP;
	private String excelFileLocation,certificateLocation;

	// getters
	public String getStudentName() {
		return studentName;
	}

	public long getStudentRoll() {
		return studentRoll;
	}

	public int getMathMark() {
		return mathMark;
	}

	public int getScienceMark() {
		return scienceMark;
	}

	public int getSocialMark() {
		return socialMark;
	}

	public int getEnglishMark() {
		return englishMark;
	}

	public int getLanguage2Mark() {
		return language2Mark;
	}

	public int getTotalMarks() {
		return totalMarks;
	}

	public String getParentName() {
		return parentName;
	}

	public String getParentEmail() {
		return parentEmail;
	}

	public String getLockerNum() {
		return lockerNum;
	}

	public String getLockerPass() {
		return lockerPass;
	}

	public String getPassed() {
		return passed;
	}

	public String getExcelFileLocation() {
		return excelFileLocation;
	}

	public String getOTP() {
		return OTP;
	}

	public long getParentMobile() {
		return parentMobile;
	}

	public String getCertificateLocation() {
		return certificateLocation;
	}
	
	
	

	// setters
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public void setStudentRoll(long studentRoll) {
		this.studentRoll = studentRoll;
	}

	public void setMathMark(int mathMark) {
		this.mathMark = mathMark;
	}

	public void setScienceMark(int scienceMark) {
		this.scienceMark = scienceMark;
	}

	public void setSocialMark(int socialMark) {
		this.socialMark = socialMark;
	}

	public void setEnglishMark(int englishMark) {
		this.englishMark = englishMark;
	}

	public void setLanguage2Mark(int language2Mark) {
		this.language2Mark = language2Mark;
	}

	public void setTotalMarks(int totalMarks) {
		this.totalMarks = totalMarks;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public void setParentEmail(String parentEmail) {
		this.parentEmail = parentEmail;
	}

	public void setLockerNum(String lockerNum) {
		this.lockerNum = lockerNum;
	}

	public void setLockerPass(String lockerPass) {
		this.lockerPass = lockerPass;
	}

	public void setPassed(String passed) {
		this.passed = passed;
	}

	public void setExcelFileLocation(String excelFile) {
		this.excelFileLocation = excelFile;
	}

	public void setOTP(String oTP) {
		OTP = oTP;
	}

	public void setParentMobile(long parentMobile) {
		this.parentMobile = parentMobile;
	}

	public void setCertificateLocation(String certificateLocation) {
		this.certificateLocation = certificateLocation;
	}
}
