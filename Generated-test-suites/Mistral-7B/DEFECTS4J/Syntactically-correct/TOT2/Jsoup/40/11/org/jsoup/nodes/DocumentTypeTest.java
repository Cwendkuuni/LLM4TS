package org.jsoup.nodes;

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
        DocumentType documentType = new DocumentType("html", "", "", "");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testDocumentTypeEmptyConstructor() {
        DocumentType documentType = new DocumentType();
        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertNull(documentType.baseUri());
    }

    @Test
    public void testDocumentTypeNullArguments() {
        try {
            new DocumentType(null, null, null, null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testDocumentTypeEmptyBaseUri() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertEquals("", documentType.baseUri());
    }

    @Test
    public void testDocumentTypeEmptyName() {
        DocumentType documentType = new DocumentType("", "publicId1", "systemId1", "");
        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attr("name"));
        assertEquals("publicId1", documentType.attr("publicId"));
        assertEquals("systemId1", documentType.attr("systemId"));
        assertEquals("", documentType.baseUri());
    }

    @Test
    public void testDocumentTypeEmptyPublicId() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertEquals("", documentType.attr("systemId"));
        assertEquals("", documentType.baseUri());
    }

    @Test
    public void testDocumentTypeEmptySystemId() {
        DocumentType documentType = new DocumentType("html", "publicId1", "", "");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("publicId1", documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertEquals("", documentType.baseUri());
    }

    @Test
    public void testDocumentTypeInvalidNodeName() {
        String invalidNodeName = "";
        try {
            new DocumentType(invalidNodeName, "", "", "");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testDocumentTypeHtml5() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html", accum.toString());
    }

    @Test
    public void testDocumentTypeXml() {
        DocumentType documentType = new DocumentType("xml", "", "", "");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE xml", accum.toString());
    }

    @Test
    public void testDocumentTypeEmptyOuterHtml() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        documentType.outerHtmlTail(accum, 0, null);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testDocumentTypeNullOuterHtml() {
        DocumentType documentType = new DocumentType("html", "", "", "");
        StringBuilder accum = null;
        documentType.outerHtmlHead(accum, 0, null);
        documentType.outerHtmlTail(accum, 0, null);
        assertEquals("", documentType.outerHtml());
    }
}