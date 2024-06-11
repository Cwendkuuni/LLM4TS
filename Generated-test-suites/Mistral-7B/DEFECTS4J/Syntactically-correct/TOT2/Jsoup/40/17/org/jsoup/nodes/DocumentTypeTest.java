import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {
    @Test
    public void testEmptyDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html");

        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithBlankAttributes() {
        DocumentType docType = new DocumentType("html", "", "", "");

        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeCreationWithAllAttributes() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testNullName() {
        String name = null;
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        try {
            new DocumentType(name, publicId, systemId, baseUri);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNullPublicId() {
        String name = "html";
        String publicId = null;
        String systemId = "systemId";
        String baseUri = "baseUri";

        try {
            new DocumentType(name, publicId, systemId, baseUri);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNullSystemId() {
        String name = "html";
        String publicId = "publicId";
        String systemId = null;
        String baseUri = "baseUri";

        try {
            new DocumentType(name, publicId, systemId, baseUri);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIllegalArgumentExceptionForNullName() {
        String name = null;
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        try {
            new DocumentType(name, publicId, systemId, baseUri);
        } catch (IllegalArgumentException e) {
            assertEquals("name is null", e.getMessage());
        }
    }

    @Test
    public void testIllegalArgumentExceptionForNullPublicId() {
        String name = "html";
        String publicId = null;
        String systemId = "systemId";
        String baseUri = "baseUri";

        try {
            new DocumentType(name, publicId, systemId, baseUri);
        } catch (IllegalArgumentException e) {
            assertEquals("publicId is null", e.getMessage());
        }
    }

    @Test
    public void testIllegalArgumentExceptionForNullSystemId() {
        String name = "html";
        String publicId = "publicId";
        String systemId = null;
        String baseUri = "baseUri";

        try {
            new DocumentType(name, publicId, systemId, baseUri);
        } catch (IllegalArgumentException e) {
            assertEquals("systemId is null", e.getMessage());
        }
    }
}