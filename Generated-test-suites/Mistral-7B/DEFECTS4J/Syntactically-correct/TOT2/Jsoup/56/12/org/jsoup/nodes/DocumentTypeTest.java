import static org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "HTML", "HTML", null);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr(DocumentType.NAME));
        assertNull(documentType.attr(DocumentType.PUBLIC_ID));
        assertNull(documentType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testHtml5DocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        assertEquals("<!doctype html", documentType.outerHtmlHead(null, 0, OutputSettings.html5()).toString());
    }

    // Add test cases for other methods
}