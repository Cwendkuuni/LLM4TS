import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testNodeName() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        Document doc = new Document(baseUri);
        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);
        doc.appendChild(docType);

        StringBuilder expectedAccum = new StringBuilder();
        expectedAccum.append("<!DOCTYPE html");
        if (!StringUtil.isBlank(publicId))
            expectedAccum.append(" PUBLIC \"");
        expectedAccum.append(publicId);
        expectedAccum.append("\"");
        if (!StringUtil.isBlank(systemId))
            expectedAccum.append(' ').append(systemId).append("\"");
        expectedAccum.append(">");

        StringBuilder actualAccum = new StringBuilder();
        docType.outerHtmlHead(actualAccum, 0, null);

        assertEquals(expectedAccum.toString(), actualAccum.toString());
    }

    @Test
    public void testErrorScenario_NullName() {
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        try {
            DocumentType docType = new DocumentType(null, publicId, systemId, baseUri);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testErrorScenario_NullPublicId() {
        String name = "html";
        String systemId = "systemId";
        String baseUri = "baseUri";

        try {
            DocumentType docType = new DocumentType(name, null, systemId, baseUri);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testErrorScenario_NullSystemId() {
        String name = "html";
        String publicId = "publicId";
        String baseUri = "baseUri";

        try {
            DocumentType docType = new DocumentType(name, publicId, null, baseUri);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testErrorScenario_NullBaseUri() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";

        try {
            DocumentType docType = new DocumentType(name, publicId, systemId, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}