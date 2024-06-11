import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseURI");
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
    }
}