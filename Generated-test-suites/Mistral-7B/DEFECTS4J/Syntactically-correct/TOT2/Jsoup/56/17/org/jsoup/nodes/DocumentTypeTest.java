import static org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeCreation() {
        Document document = new Document();
        DocumentType doctype = new DocumentType( "html", "HTML", "HTML5", "http://localhost" );
        document.appendDocment( doctype );
        assertEquals( "#doctype", doctype.nodeName() );
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType( "html", "HTML", "HTML5", "http://localhost" );
        assertEquals( "#doctype", doctype.nodeName() );
    }
}