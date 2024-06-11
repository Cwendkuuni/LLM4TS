package org.junit.test;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeName() {
        // Test case for nodeName() method
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        // Test case for outerHtmlHead() method
        Document document = Jsoup.parse("<!DOCTYPE html>");
        DocumentType docType = document.documentType();

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html";
        if (!StringUtil.isBlank(docType.attr("publicId")))
            expected += " PUBLIC \"" + docType.attr("publicId") + "\"";
        if (!StringUtil.isBlank(docType.attr("systemId")))
            expected += ' ' + docType.attr("systemId") + "\"";
        expected += '>';

        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeAttributes() {
        // Test case for attr() method and constructor
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("html", docType.name());
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
        assertEquals("baseUri", docType.baseUri().toString());

        Element element = Jsoup.parse("<!DOCTYPE html>").getFirstElement();
        DocumentType docTypeFromElement = (DocumentType) element;
        assertEquals("html", docTypeFromElement.name());
        assertEquals("", docTypeFromElement.attr("publicId"));
        assertEquals("", docTypeFromElement.attr("systemId"));
        assertEquals("", docTypeFromElement.baseUri().toString());
    }

    @Test
    public void testEmptyDocumentType() {
        // Test case for empty document type
        DocumentType docType = new DocumentType("html", "", "", "");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("", docType.attr("publicId"));
        assertEquals("", docType.attr("systemId"));
        assertEquals("", docType.baseUri().toString());

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testNullValues() {
        // Test case for null values
        DocumentType docType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals(null, docType.attr("publicId"));
        assertEquals(null, docType.attr("systemId"));
        assertEquals(null, docType.baseUri());

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }
}