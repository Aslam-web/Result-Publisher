package result_publisher.creators;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 *  iText pdf (Adobe) - for creating pdf documents
 */
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import result_publisher.App;
import result_publisher.models.Certificate_One;
import result_publisher.models.Student;

public class CertificateCreator {
	private Certificate_One certificate;
	private String SRC;
	private PdfCanvas pdfCanvas;
	private Canvas canvas;

	public static final String PDF_DATA_STORE = App.DATA_STORE + "certificates/";
	private String description = "Successfully completing the academcic year 2020-2021 with excellent performance";
	private String signature = "data/others/sign_1.png";
	private String date = new SimpleDateFormat("dd MMMM yyyy").format(new Date());
	private String stamp = "data/others/stamp_1.png";

	public CertificateCreator(Certificate_One certificate) {
		this.certificate = certificate;
		this.SRC = certificate.getCertificateAsPdf();
	}

	public void create(Set<Student> students) throws Exception {

		System.out.print("Creating certificates for qualified students :");
		
		PdfDocument pdf = null;
		String destination;
		for (Student s : students) {
			
			//ignore creating certificates for the failed students 
			if (s.getPassed().equalsIgnoreCase("NO")) {
				continue;
			}
			
			// determine each students'certificate location
			destination = PDF_DATA_STORE + s.getStudentName() + "'s certificate.pdf";
			s.setCertificateLocation(destination);
			
			// create a pdf certificate based on the certificate template and destination
			pdf = new PdfDocument(new PdfReader(SRC), new PdfWriter(destination));

			// get pdfcanvas from the first page
			PdfPage page1 = pdf.getFirstPage();
			pdfCanvas = new PdfCanvas(page1);

			// start adding mini canvases (certificate elements)
			writeName(s.getStudentName());
			writeDescription();
//			writeMarks(s.getTotalMarks());
			writeSignature();
			writeDate();
			writeStamp();
			
			pdf.close();
		}
		System.out.println("\t\tSuccessfully created !");
		System.out.println("-----------------------------------------------------");
	}

	private void writeName(String studentName) throws IOException {

		PdfFont nameFont = PdfFontFactory.createFont(StandardFonts.COURIER_BOLDOBLIQUE);

		canvas = new Canvas(pdfCanvas, this.certificate.getNamePosition());
		canvas.setHorizontalAlignment(HorizontalAlignment.CENTER);
		Text name = new Text(studentName)
				.setFont(nameFont).setUnderline()
				.setFontColor(new DeviceRgb(0, 255, 255))
				.setFontSize(42)
				.setTextAlignment(TextAlignment.CENTER);
		Paragraph paragraph = new Paragraph().add(name);
		paragraph.setTextAlignment(TextAlignment.CENTER);
		canvas.add(paragraph);

		canvas.close();
	}

	private void writeDescription() throws IOException {

		PdfFont descFont = PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC);

		canvas = new Canvas(pdfCanvas, this.certificate.getDescriptionPosition());
		canvas.setHorizontalAlignment(HorizontalAlignment.CENTER);
		Text disc = new Text(this.description)
				.setFont(descFont)
				.setFontColor(new DeviceRgb(140, 140, 140))
				.setFontSize(24)
				.setTextAlignment(TextAlignment.CENTER);
		Paragraph paragraph = new Paragraph().add(disc);
		paragraph.setTextAlignment(TextAlignment.CENTER);
		canvas.add(paragraph);

		canvas.close();
	}

	private void writeSignature() throws MalformedURLException {

		Image signature = new Image(ImageDataFactory.create(this.signature));
		Rectangle rect = this.certificate.getSignaturePosition();

		canvas = new Canvas(pdfCanvas, rect);
		signature.scaleAbsolute(rect.getWidth(), rect.getHeight());
		canvas.add(signature);

		canvas.close();
	}

	private void writeDate() throws IOException {

		// font for date
		PdfFont dateFont = PdfFontFactory.createFont(FontProgramFactory.createFont("fonts/Fondamento-Regular.ttf"));

		canvas = new Canvas(pdfCanvas, this.certificate.getDatePosition());
		Text date = new Text(this.date)
				.setFont(dateFont)
				.setFontColor(new DeviceRgb(255, 0, 0))
				.setFontSize(25)
				.setTextAlignment(TextAlignment.CENTER);
		Paragraph paragraph = new Paragraph().add(date);
		paragraph.setTextAlignment(TextAlignment.CENTER);
		canvas.add(paragraph);

		canvas.close();
	}

	private void writeStamp() throws IOException {

		Image stamp = new Image(ImageDataFactory.create(this.stamp));
		Rectangle rect = this.certificate.getStampPosition();

		canvas = new Canvas(pdfCanvas, rect);
		stamp.scaleAbsolute(rect.getWidth(), rect.getHeight());
		canvas.add(stamp);

		canvas.close();
	}
}