import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DocumentType;

public class DocumentTypeTest {

    @org.junit.Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "publicID";
        String systemId = "systemID";
        String baseUri = "baseUri";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.attributes().baseUri());
    }

    @org.junit.Test
    public void testDocumentTypeOuterHtmlHead() {
        String name = "html";
        String publicId = "publicID";
        String systemId = "systemID";
        String baseUri = "baseUri";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expectedOutput = "<!DOCTYPE html PUBLIC \"publicID\" systemID>\n";
        if (!StringUtil.isBlank(publicId)) {
            expectedOutput += " PUBLIC \"\"";
        }
        if (!StringUtil.isBlank(systemId)) {
            expectedOutput += ' ' + systemId + "\"";
        }
        expectedOutput += '>';

        assertEquals(expectedOutput, accum.toString());
    }

    @org.junit.Test
    public void testDocumentTypeCreationWithNullValues() {
        String name = null;
        String publicId = null;
        String systemId = null;
        String baseUri = null;

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.attributes().baseUri());
    }

}