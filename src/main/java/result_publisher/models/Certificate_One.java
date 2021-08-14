package result_publisher.models;

import com.itextpdf.kernel.geom.Rectangle;

public class Certificate_One {

	public Rectangle getNamePosition() {
		return new Rectangle(65, 240, 764, 110);
	}

	public Rectangle getDescriptionPosition() {
		return new Rectangle(65, 400, 764, 80);
	}

//	public Rectangle getMarkPosition() {
//		return new Rectangle();
//	}
	
	public Rectangle getSignaturePosition() {
		return new Rectangle(540, 140, 250, 110);
	}

	public Rectangle getDatePosition() {
		return new Rectangle(125, 80, 250, 110);
	}

	public Rectangle getStampPosition() {
		return new Rectangle(377, 80, 150, 150);
	}

	public String getCertificateAsPdf() {
		return "data/others/c_1_pdf_template.pdf";
	}
}
