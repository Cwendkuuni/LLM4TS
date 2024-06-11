import static org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;

public class DocumentTypeTest {
    // 
    @Test
    public void testDocumentTypeConstructor() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr(DocumentType.NAME));
        assertEquals(publicId, documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals(systemId, documentType.attr(DocumentType.SYSTEM_ID));
        assertEquals(baseUri, documentType.baseUri());
    }
    // 
}