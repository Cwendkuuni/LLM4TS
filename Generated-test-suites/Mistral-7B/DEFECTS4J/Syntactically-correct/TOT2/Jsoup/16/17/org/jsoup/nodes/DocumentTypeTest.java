import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Node;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        Node node = documentType;

        assertEquals("#doctype", node.nodeName());
        assertNotNull(node.attr("name"));
        assertNotNull(node.attr("publicId"));
        assertNotNull(node.attr("systemId"));
        assertNotNull(node.attr("baseUri"));
    }
}