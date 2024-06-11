import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {
    // ... Previous test cases

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullArguments() {
        new DocumentType(null, "HTML", "HTML", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyName() {
        new DocumentType("", "HTML", "HTML", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyPublicId() {
        new DocumentType("html", "", "HTML", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptySystemId() {
        new DocumentType("html", "HTML", "", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorBothPublicIdAndSystemId() {
        new DocumentType("html", "HTML", "HTML", null);
        new DocumentType("html", null, "HTML", null);
    }

    @Test(expected = IOException.class)
    public void testOuterHtmlHeadIOException() throws IOException {
        Document doc = new Document(null);
        DocumentType docType = new DocumentType("html", "HTML", "HTML", null);
        Appendable accum = new StringBuffer();
        docType.outerHtmlHead(accum, 0, doc.outputSettings());
        throw new IOException("Unexpected IOException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttrNullAttribute() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML", null);
        docType.attr(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttrEmptyAttribute() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML", null);
        docType.attr("");
    }
}