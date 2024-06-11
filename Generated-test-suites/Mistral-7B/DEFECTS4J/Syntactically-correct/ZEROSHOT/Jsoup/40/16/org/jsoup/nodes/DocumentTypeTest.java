package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.StringUtil;

/**
 * Test class for DocumentType.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "-//W3C//DTD HTML 4.01//EN";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testEmptyDocumentTypeCreation() {
        DocumentType docType = new DocumentType("", "", "", "");

        assertEquals("#doctype", docType.nodeName());
        assertEquals("", docType.attr("name"));
        assertEquals("", docType.attr("publicId"));
        assertEquals("", docType.attr("systemId"));
        assertEquals("", docType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://example.com");

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, new Document.OutputSettings());

        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "", "", "");

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, new Document.OutputSettings());

        String empty = "";
        assertEquals(empty, accum.toString());
    }
}