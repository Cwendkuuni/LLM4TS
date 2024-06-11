import static org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;

public class DocumentTypeTest {
    // 
    @Test
    public void testDocumentTypeConstructor() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "/baseUri";

        Document doc = new Document(baseUri);
        DocumentType doctype = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", doctype.nodeName());
        assertEquals(name, doctype.attr("name"));
        assertEquals(publicId, doctype.attr("publicId"));
        assertEquals(systemId, doctype.attr("systemId"));
        assertEquals(baseUri, doctype.attributes().get("baseUri"));
    }
    // 
}