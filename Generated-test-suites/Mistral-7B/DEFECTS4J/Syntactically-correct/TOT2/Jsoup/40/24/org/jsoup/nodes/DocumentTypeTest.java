import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "publicId1", "systemId1", "baseUri1");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("publicId1", documentType.attr("publicId"));
        assertEquals("systemId1", documentType.attr("systemId"));
        assertEquals("baseUri1", documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "publicId1", "systemId1", "baseUri1");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId1", "systemId1", "baseUri1");
        StringBuilder accum = new StringBuilder();
        // Assuming that the out parameter is not used here, we will manually check the output
        documentType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"publicId1\" \"systemId1\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "publicId1", "systemId1", "baseUri1");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);
        assertTrue(accum.toString().isEmpty());
    }
}