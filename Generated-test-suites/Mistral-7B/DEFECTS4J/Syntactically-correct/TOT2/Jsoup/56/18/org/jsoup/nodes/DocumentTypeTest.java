package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.jsoup.helper.StringUtil.isBlank;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        String name = "html";
        String publicId = "W3C";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "file:/path/to/base.html";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr(DocumentType.NAME));
        assertEquals(publicId, documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals(systemId, documentType.attr(DocumentType.SYSTEM_ID));
        assertNotNull(documentType.attr(DocumentType.BASE_URI));
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "W3C", "http://www.w3.org/TR/html4/strict.dtd", null);
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType documentType = new DocumentType("html", "W3C", "http://www.w3.org/TR/html4/strict.dtd", null);

        Appendable accum = new StringBuffer();
        documentType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

        assertEquals("<!doctype html PUBLIC \"W3C DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\" >", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadIOException() throws IOException {
        DocumentType documentType = new DocumentType("html", "W3C", "http://www.w3.org/TR/html4/strict.dtd", null);

        Appendable accum = new StringWriter();
        documentType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.xml));
        fail("IOException should be thrown.");
    }

    @Test
    public void testHas() {
        DocumentType documentType = new DocumentType("html", "W3C", "http://www.w3.org/TR/html4/strict.dtd", null);

        assertTrue(documentType.has(DocumentType.NAME));
        assertTrue(documentType.has(DocumentType.PUBLIC_ID));
        assertTrue(documentType.has(DocumentType.SYSTEM_ID));

        String emptyAttribute = "";
        assertFalse(documentType.has(emptyAttribute));
    }

    @Test
    public void testDocumentTypeEmptyStrings() {
        DocumentType documentType = new DocumentType("", "", "", "");

        assertEquals("#doctype", documentType.nodeName());
        assertEquals("", documentType.attr(DocumentType.NAME));
        assertEquals("", documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals("", documentType.attr(DocumentType.SYSTEM_ID));
        assertNull(documentType.attr(DocumentType.BASE_URI));
    }

    @Test
    public void testNodeNameNull() {
        DocumentType documentType = new DocumentType(null, "W3C", "http://www.w3.org/TR/html4/strict.dtd", null);
        assertEquals("#doctype", documentType.nodeName());
    }
}