package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

/**
 * Test class for DocumentType.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", "html", "http://www.w3.org/TR/html4/strict.dtd", doc.baseUri());

        assertNotNull(doctype);
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.attr("name"));
        assertEquals("html", doctype.getElementsByTag("name").text());
        assertEquals("html", doctype.outerHtml());
        assertEquals("PUBLIC \"html\" http://www.w3.org/TR/html4/strict.dtd", doctype.toString());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyAttributes() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", "", "", doc.baseUri());

        assertNotNull(doctype);
        assertEquals("#doctype", doctype.nodeName());
        assertNull(doctype.attr("name"));
        assertNull(doctype.attr("publicId"));
        assertNull(doctype.attr("systemId"));
        assertEquals("", doctype.outerHtml());
        assertEquals("<!DOCTYPE html>", doctype.toString());
    }

    @Test
    public void testDocumentTypeCreationWithQuirksMode() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", "HTML_MINIMAL", "", doc.baseUri());

        assertEquals("HTML_MINIMAL", doctype.attr("publicId"));
        assertTrue(doctype.toString().contains("QUIRKS"));
    }

    @Test
    public void testDocumentTypeCreationWithNullBaseUri() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", null, null, null);

        assertNotNull(doctype);
        assertEquals("#doctype", doctype.nodeName());
        assertNull(doctype.attr("name"));
        assertNull(doctype.attr("publicId"));
        assertNull(doctype.attr("systemId"));
        assertEquals("", doctype.outerHtml());
        assertEquals("<!DOCTYPE html>", doctype.toString());
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", null, null, doc.baseUri());
        Element root = doc.parseBodyElements("<html><head><title>Test</title></head><body></body></html>").first();

        root.appendElement(doctype);

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, null);
        doctype.outerHtmlTail(accum, 0, null);

        assertEquals("<!DOCTYPE html>\n", accum.toString());
    }

    @Test
    public void testDocumentTypeToString() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", null, null, doc.baseUri());

        assertEquals("<!DOCTYPE html>\n", doctype.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithPublicIdAndSystemId() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", "html", "http://www.w3.org/TR/html4/strict.dtd", doc.baseUri());

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, null);
        doctype.outerHtmlTail(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"html\" \"http://www.w3.org/TR/html4/strict.dtd\">\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithQuirksMode() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", "HTML_MINIMAL", null, doc.baseUri());

        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, null);
        doctype.outerHtmlTail(accum, 0, null);

        assertEquals("<!DOCTYPE html SYSTEM>\n<!ENTITY % html SYSTEM \"about:legacy-compat\">\n<!ENTITY % html SYSTEM \"about:quirks\">\n<!ENTITY % html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">\n<!ENTITY % html SYSTEM \"http://www.w3.org/TR/html4/transitional.dtd\">\n<!ENTITY % html SYSTEM \"http://www.w3.org/TR/html4/frameset.dtd\">\n<!ENTITY % html SYSTEM \"http://www.w3.org/TR/html4/xhtml1-transitional.dtd\">\n<!ENTITY % html SYSTEM \"http://www.w3.org/TR/html4/loose.dtd\">\n", accum.toString());
    }
}