import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class DocumentTypeTest {
    // 

    @Test
    public void testDocumentTypeConstructor() {
        String name = "html";
        String publicId = "publicid";
        String systemId = "systemid";
        String baseUri = "http://example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertNotNull(docType);
        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    // 
}