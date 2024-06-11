import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTypeTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document();
    }

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
        assertEquals(document.baseUri().toString(), documentType.baseUri().toString());
    }

    @Test
    public void testEmptyDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", null, null, document.baseUri());
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertEquals(document.baseUri().toString(), documentType.baseUri().toString());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\"systemId\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicId() {
        DocumentType documentType = new DocumentType("html", null, "systemId", document.baseUri());
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html SYSTEM \"systemId\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptySystemId() {
        DocumentType documentType = new DocumentType("html", "publicId", null, document.baseUri());
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", null, null, document.baseUri());
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        String expected = "";
        assertEquals(expected, accum.toString());
    }
}