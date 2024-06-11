
package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
        assertEquals("baseUri", docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyAttributes() {
        DocumentType docType = new DocumentType("html");
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithNullName() {
        try {
            new DocumentType(null, "publicId", "systemId", "baseUri");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDocumentTypeCreationWithEmptyName() {
        try {
            new DocumentType("", "publicId", "systemId", "baseUri");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDocumentTypeCreationWithAllAttributes() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
        assertEquals("baseUri", docType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        DocumentType docType = doc.documentType();
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        docType.outerHtmlTail(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
        assertEquals(expected, accum.toString());
    }
}