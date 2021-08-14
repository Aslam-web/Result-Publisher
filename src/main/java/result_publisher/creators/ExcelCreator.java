package result_publisher.creators;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * apache poi - for reading/writing excel
 */

import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import result_publisher.App;
import result_publisher.models.Student;

public class ExcelCreator {

	public static final String EXCEL_DATA_STORE = App.DATA_STORE + "excel_files/";
	public static int lockNum = 44;

	private Set<Student> studentEntries = new HashSet<>();
	private String filename;

	/*
	 * The method that calls all other methods internally and completes all the
	 * excel operations
	 */
	public Set<Student> createExcelForStudents(String filename) throws IOException {
		this.filename = filename;

		System.out.println("Reading data...");

		storeDataFromExcel();
		createExcelForAll();
		generateOTPForAll();
		setPasswordForAll();

		System.out.println("\t\tSuccessfully created !");
//		System.out.println("-----------------------------------------------------");
		return this.studentEntries;
	}

	/*
	 * reads from an excel file and stores each entry as a Student and stores in a
	 * Set
	 */
	private void storeDataFromExcel() {

		try (Workbook workbook = new HSSFWorkbook(new FileInputStream(filename))) {

			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> iter = sheet.rowIterator();

			// ignoring the first row - the headers
			iter.next();

			Row row;
			Student student;
			while (iter.hasNext()) {

				// got the whole row
				row = iter.next();
				student = new Student();
				// assigning values to all fields of StudentGradeTen object
				student.setStudentName(row.getCell(0).getStringCellValue());
				student.setStudentRoll((long) row.getCell(1).getNumericCellValue());

				student.setMathMark((int) row.getCell(2).getNumericCellValue());
				student.setScienceMark((int) row.getCell(3).getNumericCellValue());
				student.setSocialMark((int) row.getCell(4).getNumericCellValue());
				student.setEnglishMark((int) row.getCell(5).getNumericCellValue());
				student.setLanguage2Mark((int) row.getCell(6).getNumericCellValue());
				student.setTotalMarks((int) row.getCell(7).getNumericCellValue());

				student.setPassed(row.getCell(8).getStringCellValue());

				student.setParentName(row.getCell(9).getStringCellValue());
				student.setParentEmail(row.getCell(10).getStringCellValue());
				student.setParentMobile((long) row.getCell(11).getNumericCellValue());

				this.studentEntries.add(student);
			}

//			System.out.print("All entries stored ");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// creates excel for all the students that was previously stored in the Set
	private void createExcelForAll() throws IOException {

		System.out.print("Creating excel file for all the students :");
		
		Workbook wb = null;
		FileOutputStream output = null;
		Student s;
		String destination;
		Sheet sheet;
		Row row;
		Cell cell;

		Iterator<Student> iter = studentEntries.iterator();
		while (iter.hasNext()) {
			int i = 0;
			// get the student and set the excel file location on that object
			s = iter.next();
			String fileName = s.getStudentName() + "'s Results.xls";
			destination = EXCEL_DATA_STORE + fileName;
			s.setExcelFileLocation(destination);
			studentEntries.add(s);

			// create a workbook in the same location and add data to it
			wb = new HSSFWorkbook();
			sheet = wb.createSheet(s.getStudentName());

			row = sheet.createRow(i);
			row.createCell(0).setCellValue("Name");
			row.createCell(1).setCellValue(s.getStudentName());

			row = sheet.createRow(++i);
			row.createCell(0).setCellValue("Role");
			row.createCell(1).setCellValue(s.getStudentRoll());

			row = sheet.createRow(++i);
			row.createCell(0).setCellValue("Maths");
			row.createCell(1).setCellValue(s.getMathMark());

			row = sheet.createRow(++i);
			row.createCell(0).setCellValue("Science");
			row.createCell(1).setCellValue(s.getScienceMark());

			row = sheet.createRow(++i);
			row.createCell(0).setCellValue("Social");
			row.createCell(1).setCellValue(s.getSocialMark());

			row = sheet.createRow(++i);
			row.createCell(0).setCellValue("English");
			row.createCell(1).setCellValue(s.getEnglishMark());

			row = sheet.createRow(++i);
			row.createCell(0).setCellValue("Language");
			row.createCell(1).setCellValue(s.getLanguage2Mark());

			row = sheet.createRow(++i);
			row.createCell(0).setCellValue("Total");
			row.createCell(1).setCellValue(s.getTotalMarks());

			// operations based on the result
			if (s.getPassed().equals("YES")) {

				// set result
				row = sheet.createRow(++i);
				row.createCell(0).setCellValue("Result");
				row.createCell(1).setCellValue("PASSED");

				// set locker number - (L44)
				String lockerNumber = String.valueOf("L" + lockNum);
				s.setLockerNum(lockerNumber);
				row = sheet.createRow(++i);
				row.createCell(0).setCellValue("Locker Number");
				row.createCell(1).setCellValue(lockerNumber);

				// set locker password : 32091	-	(19023 + lockNum)
				long roll = s.getStudentRoll();
				int reverseRoll=0;
				while (roll != 0) {
					int remainder = (int) roll % 10;
					reverseRoll = reverseRoll * 10 + remainder;
					roll = roll / 10;
				}
				String password = String.valueOf(reverseRoll+lockNum);
				s.setLockerPass(password);
				row = sheet.createRow(++i);
				row.createCell(0).setCellValue("Locker Password");
				row.createCell(1).setCellValue(password); // set locker password

				lockNum++;
			} else {
				// set result
				row = sheet.createRow(++i);
				row.createCell(0).setCellValue("Result");
				row.createCell(1).setCellValue("FAILED");
			}

			// formatting the columns
			CellStyle style = wb.createCellStyle();
			style.setWrapText(true);
			style.setAlignment(HorizontalAlignment.RIGHT);
			for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
				cell = sheet.getRow(j).getCell(1);
				cell.setCellStyle(style);
			}
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);

			// writing to the workbook
			output = new FileOutputStream(destination);
			wb.write(output);
		}
		

		output.close();
		wb.close();
	}

	/*
	 * generates an OTP based on the student's name and roll number for all the
	 * students
	 */
	private void generateOTPForAll() {
		Iterator<Student> iter = this.studentEntries.iterator();
		Student s;

		while (iter.hasNext()) {
			s = iter.next();
			// set OTP for sending message = (last 3 letters + last 3 num)
			// e.g. aslam & 21009 = LAM009
			String OTP_1 = s.getStudentName().substring(s.getStudentName().length() - 3).toUpperCase();
			String rollString = String.valueOf(s.getStudentRoll());
			String OTP_2 = rollString.substring(rollString.length() - 3);
			s.setOTP(OTP_1 + OTP_2);
		}
	}

	// sets the password for all the excel files that were previously created
	private void setPasswordForAll() throws IOException {
		Iterator<Student> iter = this.studentEntries.iterator();
		Student s;
		while (iter.hasNext()) {
			s = iter.next();
//			System.out.println(s.getExcelFileLocation());
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(s.getExcelFileLocation()));
			Biff8EncryptionKey.setCurrentUserPassword(s.getOTP());
			workbook.write(new FileOutputStream(s.getExcelFileLocation()));
			workbook.close();
		}
	}

	// simply for printing
	public static void print(Set<Student> studentEntries) {

		System.out.println("\n---------------------Printing Data---------------------------\n");
		Iterator<Student> iter = studentEntries.iterator();
		while (iter.hasNext()) {
			Student s = iter.next();
			System.out.printf(
					"%s\t%s\t" + "%s\t%s\t%s\t%s\t%s\t" + "%s\t%s\t" + "%s\t%s\t%s\t" + "%s\t%s\t%s\t" + "%s\n",
					// name, roll
					s.getStudentName(), s.getStudentRoll(),
					// marks
					s.getMathMark(), s.getScienceMark(), s.getSocialMark(), s.getEnglishMark(), s.getLanguage2Mark(),
					s.getTotalMarks(), s.getPassed(),
					// parent's details
					s.getParentName(), s.getParentEmail(), s.getParentMobile(),
					// locker details
					s.getLockerNum(), s.getLockerPass(), s.getOTP(),
					// attachment
					s.getExcelFileLocation());

		}
	}
}
