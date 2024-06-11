import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.StringUtil;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    Document document;

    @Before
    public void setUp() {
        document = new Document();
    }

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "DTD HTML 5";
        String baseUri = "http://example.com/";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "HTML", "DTD HTML 5", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML\" DTD HTML 5>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdOnly() {
        DocumentType documentType = new DocumentType("html", "HTML", "", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML\"";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemIdOnly() {
        DocumentType documentType = new DocumentType("html", "", "DTD HTML 5", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html SYSTEM \"DTD HTML 5\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBothPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC", "DTD HTML 5", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML PUBLIC\" DTD HTML 5>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        // The outerHtmlTail method does not append anything
        assertEquals("", accum.toString());
    }
}