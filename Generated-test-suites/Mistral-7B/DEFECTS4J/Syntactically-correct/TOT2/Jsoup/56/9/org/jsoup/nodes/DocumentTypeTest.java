import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "HTML", "HTML5", null);
        assert documentType.nodeName().equals("#doctype");
        assert documentType.has(DocumentType.NAME);
        assert documentType.has(DocumentType.PUBLIC_ID);
        assert documentType.has(DocumentType.SYSTEM_ID);
    }
}