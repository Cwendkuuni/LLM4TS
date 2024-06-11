import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeConstruction() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        assertEquals("#doctype", docType.nodeName());
    }
}