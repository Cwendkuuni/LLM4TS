import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "http://www.w3.org/TR/REC-html40/strict.dtd";
        String baseUri = "/test";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "HTML", "http://www.w3.org/TR/REC-html40/strict.dtd", "");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
    }
}