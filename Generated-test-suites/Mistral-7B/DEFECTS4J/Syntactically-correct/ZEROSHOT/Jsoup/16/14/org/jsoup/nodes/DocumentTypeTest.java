import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class DocumentTypeTest {

    private Document document;

    @Before
    public void setup() {
        document = Mockito.mock(Document.class);
    }

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "HTML_PUBLIC";
        String systemId = "DTD_HTML_1.0";
        String baseUri = "http://example.com/";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "HTML_PUBLIC", "DTD_HTML_1.0", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML_PUBLIC\" DTD_HTML_1.0>\n";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemIdEmpty() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html>\n";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdButSystemIdEmpty() {
        DocumentType documentType = new DocumentType("html", "HTML_PUBLIC", "", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML_PUBLIC\">\n";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemIdButPublicIdEmpty() {
        DocumentType documentType = new DocumentType("html", "", "DTD_HTML_1.0", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html SYSTEM \"DTD_HTML_1.0\">\n";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "HTML_PUBLIC", "DTD_HTML_1.0", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }

    @Test
    public void testCreateElement() {
        DocumentType documentType = new DocumentType("html", "HTML_PUBLIC", "DTD_HTML_1.0", "");
        Element parent = Mockito.mock(Element.class);

        Node child = documentType.createElement(document);

        assertNotNull(child);
        assertEquals(documentType.nodeName(), child.nodeName());
        assertEquals(documentType, child.parent());
        assertEquals(document, child.document());
        assertEquals(documentType.baseUri(), child.baseUri());

        Mockito.verify(parent).appendChild(child);
    }

    @Test
    public void testCreateTextNode() {
        DocumentType documentType = new DocumentType("html", "HTML_PUBLIC", "DTD_HTML_1.0", "");

        Node child = documentType.createTextNode("Test text");

        assertNotNull(child);
        assertEquals(TextNode.class, child.getClass());
        assertEquals(documentType.nodeName(), child.parentNode().nodeName());
        assertEquals(documentType, child.parentNode());
        assertEquals(document, child.document());
        assertEquals(documentType.baseUri(), child.baseUri());
    }
}