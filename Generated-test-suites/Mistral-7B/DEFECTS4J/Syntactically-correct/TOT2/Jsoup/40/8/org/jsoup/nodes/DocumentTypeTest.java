import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import org.junit.Assert.*;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "{-//W3C//DTD HTML 4.01//EN}";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com/";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testEmptyName() {
        String publicId = "html";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com/";

        Validate.notEmpty(name); // Validate.notEmpty called in the constructor
    }

    @Test
    public void testNullName() {
        String publicId = "html";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com/";

        assertThrows(NullPointerException.class, () -> new DocumentType(null, publicId, systemId, baseUri));
    }

    @Test
    public void testEmptyPublicId() {
        String name = "html";
        String publicId = "";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com/";

        assertThrows(IllegalArgumentException.class, () -> new DocumentType(name, publicId, systemId, baseUri));
    }

    @Test
    public void testNullPublicId() {
        String name = "html";
        String publicId = null;
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com/";

        assertThrows(NullPointerException.class, () -> new DocumentType(name, publicId, systemId, baseUri));
    }

    @Test
    public void testEmptySystemId() {
        String name = "html";
        String publicId = "html";
        String systemId = "";
        String baseUri = "http://example.com/";

        assertThrows(IllegalArgumentException.class, () -> new DocumentType(name, publicId, systemId, baseUri));
    }

    @Test
    public void testNullSystemId() {
        String name = "html";
        String publicId = "html";
        String systemId = null;
        String baseUri = "http://example.com/";

        assertThrows(NullPointerException.class, () -> new DocumentType(name, publicId, systemId, baseUri));
    }

    @Test
    public void testDocumentTypeToString() {
        String name = "html";
        String publicId = "{-//W3C//DTD HTML 4.01//EN}";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com/";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Document document = new Document();
        document.setBaseUri(baseUri);
        document.appendChild(documentType);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", documentType.outerHtml());
    }
}