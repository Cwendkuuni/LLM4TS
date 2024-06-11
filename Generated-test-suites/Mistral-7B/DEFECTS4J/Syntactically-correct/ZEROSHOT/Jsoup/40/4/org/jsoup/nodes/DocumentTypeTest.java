package org.jsoup.nodes.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Nodes;
import org.jsoup.parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DocumentTypeTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document();
    }

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"",
                "http://www.w3.org/TR/html4/strict.dtd", null);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr("systemId"));
        assertEquals(null, documentType.attributes());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyValues() {
        DocumentType documentType = new DocumentType("html", "", "", null);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertEquals(0, documentType.numChildren());
    }

    @Test
    public void testDocumentTypeCreationWithOnlyPublicId() {
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC", null, null);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("HTML PUBLIC", documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertEquals(0, documentType.numChildren());
    }

    @Test
    public void testDocumentTypeCreationWithOnlySystemId() {
        DocumentType documentType = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", null);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr("systemId"));
        assertEquals(0, documentType.numChildren());
    }

    @Test
    public void testDocumentTypeCreationWithNullValues() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertEquals(0, documentType.numChildren());
    }

    @Test
    public void testDocumentTypeToString() {
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"",
                "http://www.w3.org/TR/html4/strict.dtd", null);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", documentType.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"",
                "http://www.w3.org/TR/html4/strict.dtd", null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"",
                "http://www.w3.org/TR/html4/strict.dtd", null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"",
                "http://www.w3.org/TR/html4/strict.dtd", null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtml(accum, null);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeIsEmptyElement() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        assertEquals(true, documentType.isEmptyElement());
    }

    @Test
    public void testDocumentTypeIsBlockElement() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        assertEquals(false, documentType.isBlockElement());
    }

    @Test
    public void testDocumentTypeIsInlineElement() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        assertEquals(false, documentType.isInlineElement());
    }

    @Test
    public void testDocumentTypeHasAttributes() {
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"",
                "http://www.w3.org/TR/html4/strict.dtd", null);

        assertNull(documentType.attributes());
    }

    @Test
    public void testDocumentTypeHasAttribute() {
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"",
                "http://www.w3.org/TR/html4/strict.dtd", null);

        Attribute publicIdAttribute = documentType.attr("publicId");

        assertNotNull(publicIdAttribute);
        assertEquals("HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", publicIdAttribute.attrValue());
    }

    @Test
    public void testDocumentTypeHasAttributeWithNullValue() {
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC", null, null);

        Attribute publicIdAttribute = documentType.attr("publicId");

        assertNull(publicIdAttribute);
    }

    @Test
    public void testDocumentTypeHasAttributeWithEmptyValue() {
        DocumentType documentType = new DocumentType("html", "", null, null);

        Attribute publicIdAttribute = documentType.attr("publicId");

        assertNull(publicIdAttribute);
    }

    @Test
    public void testDocumentTypeHasChildren() {
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"",
                "http://www.w3.org/TR/html4/strict.dtd", null);

        assertEquals(0, documentType.numChildren());
    }

    @Test
    public void testDocumentTypeAddChild() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        Element element = new Element("html");

        documentType.add(element);

        assertEquals(1, documentType.numChildren());
        assertEquals("html", documentType.child(0).nodeName());
    }

    @Test
    public void testDocumentTypeParse() {
        String html = "<!DOCTYPE html>";

        Document parsedDocument = Parser.parseBodyFragment(html, "");

        assertEquals(1, parsedDocument.numChildren());
        assertEquals("#doctype", parsedDocument.child(0).nodeName());
    }
}