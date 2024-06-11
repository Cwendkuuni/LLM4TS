package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

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
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", document.baseUri());
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("publicID", documentType.attr("publicId"));
        assertEquals("systemID", documentType.attr("systemId"));
        assertEquals(document.baseUri(), documentType.baseUri());
    }

    @Test
    public void testEmptyDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "", "", document.baseUri());
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertEquals(document.baseUri(), documentType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", document.baseUri());
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE htmlPUBLIC \"publicID\" systemID>\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithPublicIdOnly() {
        DocumentType documentType = new DocumentType("html", "publicID", document.baseUri());
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicID\"\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithSystemIdOnly() {
        DocumentType documentType = new DocumentType("html", document.baseUri(), "systemID", document.baseUri());
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html SYSTEM systemID>\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithBothPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", document.baseUri());
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" systemID>\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "", "", document.baseUri());
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }

    @Test
    public void testParseDocumentType() {
        String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
        Document parsedDocument = Parser.parseBodyFragment(html, "");
        Node documentTypeNode = parsedDocument.body();

        assertNotNull(documentTypeNode);
        assertEquals(DocumentType.class, documentTypeNode.getClass());
        DocumentType documentType = (DocumentType) documentTypeNode;
        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr("systemId"));
        assertEquals(document.baseUri(), documentType.baseUri());
    }
}