package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import org.junit.Test;

/**
 * Tests for {@link DocumentType} class.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        assertNotNull(docType);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeCreationWithEmptyAttributes() {
        DocumentType docType = new DocumentType("html", "", "", null);
        assertNotNull(docType);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("", docType.attr("publicId"));
        assertEquals("", docType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeCreationWithNullName() {
        try {
            new DocumentType(null, "", "", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDocumentTypeToString() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        String docTypeString = docType.toString();
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
        assertEquals(expected, docTypeString);
    }

    @Test
    public void testDocumentTypeParsing() {
        String html = "<html><!DOCTYPE html PUBLIC \"W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<body>\n<p>Hello World</p>\n</body>\n</html>";
        Document doc = Parser.parseBodyFragment(html);
        Node doctypeNode = doc.selectFirst("!DOCTYPE");
        DocumentType docType = (DocumentType) doctypeNode;
        assertEquals("html", docType.attr("name"));
        assertEquals("W3C//DTD XHTML 1.0 Transitional//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", docType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithEmptyPublicId() {
        DocumentType docType = new DocumentType("html", "", "systemId", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html SYSTEM \"systemId\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithEmptySystemId() {
        DocumentType docType = new DocumentType("html", "publicId", "", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithEmptyPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", "", "", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testDocumentTypeEqual() {
        DocumentType docType1 = new DocumentType("html", "publicId1", "systemId1", null);
        DocumentType docType2 = new DocumentType("html", "publicId1", "systemId1", null);
        DocumentType docType3 = new DocumentType("html", "publicId2", "systemId2", null);
        assertTrue(docType1.equals(docType1));
        assertTrue(docType1.equals(docType2));
        assertFalse(docType1.equals(docType3));
    }

    @Test
    public void testDocumentTypeCompareTo() {
        DocumentType docType1 = new DocumentType("html", "publicId1", "systemId1", null);
        DocumentType docType2 = new DocumentType("html", "publicId1", "systemId1", null);
        DocumentType docType3 = new DocumentType("html", "publicId2", "systemId2", null);
        DocumentType docType4 = new DocumentType("html", "publicId3", "systemId3", null);
        assertEquals(0, docType1.compareTo(docType2));
        assertTrue(docType1.compareTo(docType3) < 0);
        assertTrue(docType3.compareTo(docType1) > 0);
        assertTrue(docType1.compareTo(docType4) < 0);
        assertTrue(docType4.compareTo(docType1) > 0);
    }

    @Test
    public void testDocumentTypeHashCode() {
        DocumentType docType1 = new DocumentType("html", "publicId1", "systemId1", null);
        DocumentType docType2 = new DocumentType("html", "publicId1", "systemId1", null);
        DocumentType docType3 = new DocumentType("html", "publicId2", "systemId2", null);
        DocumentType docType4 = new DocumentType("html", "publicId3", "systemId3", null);
        assertEquals(docType1.hashCode(), docType1.hashCode());
        assertEquals(docType1.hashCode(), docType2.hashCode());
        assertNotEquals(docType1.hashCode(), docType3.hashCode());
        assertNotEquals(docType1.hashCode(), docType4.hashCode());
    }
}