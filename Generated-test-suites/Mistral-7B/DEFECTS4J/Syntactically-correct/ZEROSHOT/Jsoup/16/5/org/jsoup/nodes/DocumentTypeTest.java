package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

/**
 * Tests for the {@link DocumentType} class.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "HTML_VERSION_NUMBER";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyValues() {
        String name = "html";
        DocumentType documentType = new DocumentType(name, "", "", "");

        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertNull(documentType.baseUri());
    }

    @Test
    public void testDocumentTypeToString() {
        String name = "html";
        String publicId = "HTML_VERSION_NUMBER";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        Element element = new Element("html");
        element.appendText("Test content");

        Document document = new Document();
        document.appendChild(documentType);
        document.appendChild(element);

        StringBuilder output = new StringBuilder();
        document.outputSettings().prettyPrint(false);
        document.outputSettings().encoding("UTF-8").indentAmount(0);
        document.select("html").outerHtml(output);

        String expected = "<!DOCTYPE html PUBLIC \"HTML_VERSION_NUMBER\" \"http://www.w3.org/TR/html4/strict.dtd\">\n" +
                "<html>\n" +
                "<!--\n" +
                "Test content\n" +
                "-->\n" +
                "</html>";

        assertEquals(expected, output.toString());
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeToStringWithEmptyValues() {
        String name = "html";
        DocumentType documentType = new DocumentType(name, "", "", "");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testDocumentTypeToStringWithOnlyPublicId() {
        String name = "html";
        String publicId = "HTML_VERSION_NUMBER";
        String systemId = "";
        String baseUri = "";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML_VERSION_NUMBER\">", accum.toString());
    }

    @Test
    public void testDocumentTypeToStringWithOnlySystemId() {
        String name = "html";
        String publicId = "";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }
}