package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

import org.junit.Test;

/**
 * Test class for {@link DocumentType} class.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstruction() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");

        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.getAttr("name"));
        assertEquals("publicId", documentType.getAttr("publicId"));
        assertEquals("systemId", documentType.getAttr("systemId"));
        assertEquals("baseUri", documentType.baseUri().toString());
    }

    @Test
    public void testDocumentTypeConstructionWithEmptyStrings() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.getAttr("name"));
        assertNull(documentType.getAttr("publicId"));
        assertNull(documentType.getAttr("systemId"));
        assertNull(documentType.baseUri());
    }

    @Test
    public void testDocumentTypeConstructionWithNullBaseUri() {
        DocumentType documentType = new DocumentType("html", "", "", null);

        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.getAttr("name"));
        assertNull(documentType.getAttr("publicId"));
        assertNull(documentType.getAttr("systemId"));
        assertNull(documentType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "", "", "baseUri");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeClone() throws CloneNotSupportedException {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        DocumentType clonedDocumentType = (DocumentType) documentType.cloneNode(true);

        assertNotSame(documentType, clonedDocumentType);
        assertEquals("#doctype", clonedDocumentType.nodeName());
        assertEquals("html", clonedDocumentType.getAttr("name"));
        assertEquals("publicId", clonedDocumentType.getAttr("publicId"));
        assertEquals("systemId", clonedDocumentType.getAttr("systemId"));
        assertEquals("baseUri", clonedDocumentType.baseUri().toString());
    }

    @Test
    public void testDocumentTypeToString() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");

        String expected = "<html xmlns:xml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">" +
                "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
        String actual = documentType.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testDocumentTypeIsEmpty() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        assertEquals(false, documentType.isTextOnly());
        assertEquals(false, documentType.isWhitespaceOnly());
        assertEquals(false, documentType.isHexadecimal());
        assertEquals(false, documentType.isWholeDocument());
        assertEquals(true, documentType.children().isEmpty());
        assertEquals(1, documentType.childNodes().size());
    }

    @Test
    public void testDocumentTypeIsTextOnly() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        Element element = new Element("html");
        TextNode textNode = new TextNode("Test text");

        document.appendChild(documentType);
        document.appendChild(element);
        element.appendChild(textNode);

        assertEquals(false, documentType.isTextOnly());
    }

    @Test
    public void testDocumentTypeIsWhitespaceOnly() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        Element element = new Element("html");
        TextNode textNode = new TextNode("");

        document.appendChild(documentType);
        document.appendChild(element);
        element.appendChild(textNode);

        assertEquals(false, documentType.isWhitespaceOnly());
    }

    @Test
    public void testDocumentTypeIsHexadecimal() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        Element element = new Element("html");
        TextNode textNode = new TextNode("&#x0;");

        document.appendChild(documentType);
        document.appendChild(element);
        element.appendChild(textNode);

        assertEquals(false, documentType.isHexadecimal());
    }

    @Test
    public void testDocumentTypeIsWholeDocument() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        Element element = new Element("html");
        TextNode textNode = new TextNode("Test text");

        document.appendChild(documentType);
        document.appendChild(element);
        element.appendChild(textNode);

        assertEquals(false, documentType.isWholeDocument());
    }

    @Test
    public void testDocumentTypeChildren() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");

        assertEquals(0, documentType.children().size());
        assertEquals(1, documentType.childNodes().size());
    }

    @Test
    public void testDocumentTypeChildNodes() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        Attribute attribute = new Attribute("name", "html");

        document.appendChild(documentType);
        documentType.attr(attribute);

        assertEquals(1, documentType.childNodes().size());
    }
}