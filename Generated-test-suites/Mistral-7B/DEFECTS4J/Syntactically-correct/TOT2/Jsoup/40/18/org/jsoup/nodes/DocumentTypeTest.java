import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeConstructor() {
        String name = "html";
        String publicId = "publicID";
        String systemId = "systemID";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertNotNull(documentType.attr("baseUri"));
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" systemID>\n", accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructorWithNullName() {
        String name = null;
        String publicId = "";
        String systemId = "";
        String baseUri = "";

        try {
            DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        } catch (IllegalArgumentException e) {
            assertEquals("Name is empty", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructorWithEmptyPublicId() {
        String name = "html";
        String publicId = "";
        String systemId = "";
        String baseUri = "";

        try {
            DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        } catch (IllegalArgumentException e) {
            assertEquals("Public ID is empty", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructorWithEmptySystemId() {
        String name = "html";
        String publicId = "publicID";
        String systemId = "";
        String baseUri = "";

        try {
            DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        } catch (IllegalArgumentException e) {
            assertEquals("System ID is empty", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructorWithEmptyBaseUri() {
        String name = "html";
        String publicId = "publicID";
        String systemId = "systemID";
        String baseUri = "";

        try {
            DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        } catch (IllegalArgumentException e) {
            assertEquals("Base URI is empty", e.getMessage());
            throw e;
        }
    }
}