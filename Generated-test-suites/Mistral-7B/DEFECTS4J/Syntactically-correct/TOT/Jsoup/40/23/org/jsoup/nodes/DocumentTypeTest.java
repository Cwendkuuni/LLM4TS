import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeCreation() {
        DocumentType doctype = new DocumentType("html", "publicID", "systemID", null);
        assertEquals("#doctype", doctype.nodeName());
        assertTrue(doctype.hasAttr("name"));
        assertTrue(doctype.hasAttr("publicId"));
        assertTrue(doctype.hasAttr("systemId"));
    }
}
