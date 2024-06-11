package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeCreation() {
        // Test creating a DocumentType with valid arguments
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertNotNull(docType);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
        assertEquals("baseUri", docType.baseUri().toString());

        // Test creating a DocumentType with null baseUri
        DocumentType docTypeNullBaseUri = new DocumentType("html", "publicId", "systemId", null);
        assertNotNull(docTypeNullBaseUri);
        assertEquals("#doctype", docTypeNullBaseUri.nodeName());
        assertEquals("html", docTypeNullBaseUri.attr("name"));
        assertEquals("publicId", docTypeNullBaseUri.attr("publicId"));
        assertEquals("systemId", docTypeNullBaseUri.attr("systemId"));
        assertNull(docTypeNullBaseUri.baseUri());

        // Test creating a DocumentType with empty publicId and systemId
        DocumentType docTypeEmpty = new DocumentType("html", "", "", "");
        assertNotNull(docTypeEmpty);
        assertEquals("#doctype", docTypeEmpty.nodeName());
        assertEquals("html", docTypeEmpty.attr("name"));
        assertNull(docTypeEmpty.attr("publicId"));
        assertNull(docTypeEmpty.attr("systemId"));
        assertEquals("", docTypeEmpty.baseUri().toString());

        // Test creating a DocumentType with null name
        try {
            new DocumentType(null, "publicId", "systemId", "baseUri");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test creating a DocumentType with empty name
        try {
            new DocumentType("", "publicId", "systemId", "baseUri");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        DocumentType docType = doc.documentType();

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", accum.toString());

        Element element = new Element("test");
        doc.insertBefore(element, doc.body());
        Document newDoc = doc.body().detach();
        docType = newDoc.documentType();

        accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html>", accum.toString());

        DocumentType docTypeNull = new DocumentType("html", null, null, null);
        assertEquals("#doctype", docTypeNull.nodeName());
        assertEquals("html", docTypeNull.attr("name"));
        assertNull(docTypeNull.attr("publicId"));
        assertNull(docTypeNull.attr("systemId"));
        assertNull(docTypeNull.baseUri());
    }
}