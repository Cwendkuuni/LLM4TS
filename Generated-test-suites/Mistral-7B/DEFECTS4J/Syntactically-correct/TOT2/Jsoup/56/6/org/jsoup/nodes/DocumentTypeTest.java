import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeConstruction() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        Attributes attributes = documentType.attributes();

        assertNotNull(documentType);
        assertEquals(name, attributes.get(DocumentType.NAME));
        assertEquals(publicId, attributes.get(DocumentType.PUBLIC_ID));
        assertEquals(systemId, attributes.get(DocumentType.SYSTEM_ID));
        assertEquals(baseUri, documentType.baseUri());
    }
}