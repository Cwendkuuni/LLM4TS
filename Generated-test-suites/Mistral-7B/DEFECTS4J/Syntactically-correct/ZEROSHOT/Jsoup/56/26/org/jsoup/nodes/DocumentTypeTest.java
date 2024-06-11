package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Xml;
import org.jsoup.parser.Parser;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for {@link DocumentType} class.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(3, documentType.childNodes().size());

        Node nameNode = documentType.childNodes().get(0);
        assertEquals("name", nameNode.attr("xmlns:html"));
        assertEquals("html", nameNode.nodeValue());

        Node publicIdNode = documentType.childNodes().get(1);
        assertEquals("PUBLIC", publicIdNode.nodeName());
        assertEquals("publicId", publicIdNode.attr("xmlns:DTD"));
        assertEquals(publicId, publicIdNode.nodeValue());

        Node systemIdNode = documentType.childNodes().get(2);
        assertEquals("SYSTEM", systemIdNode.nodeName());
        assertEquals("systemId", systemIdNode.attr("xmlns:DTD"));
        assertEquals(systemId, systemIdNode.nodeValue());

        assertEquals(baseUri, documentType.baseUri());
        assertEquals(name, documentType.attr(DocumentType.NAME));
        assertEquals(publicId, documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals(systemId, documentType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithoutAttributes() {
        String name = "html";
        DocumentType documentType = new DocumentType(name, null, null, null);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(1, documentType.childNodes().size());

        Node nameNode = documentType.childNodes().get(0);
        assertEquals("html", nameNode.nodeValue());
    }

    @Test
    public void testDocumentTypeOuterHtml() throws Exception {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "http://example.com";

        Document document = new Document();
        document.setBaseUri(baseUri);
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
        String outerHtml = documentType.outerHtml();
        assertEquals(expected, outerHtml);

        Document parsedDocument = Parser.parse(outerHtml, baseUri);
        Node node = parsedDocument.selectSingleNode("#doctype");
        assertEquals(documentType, node);
    }

    @Test
    public void testDocumentTypeOuterHtmlWithoutAttributes() {
        String name = "html";
        DocumentType documentType = new DocumentType(name, null, null, null);

        String expected = "<!DOCTYPE html>";
        String outerHtml = documentType.outerHtml();
        assertEquals(expected, outerHtml);

        Document parsedDocument = Parser.parse(outerHtml, null);
        Node node = parsedDocument.selectSingleNode("#doctype");
        assertEquals(documentType, node);
    }

    @Test
    public void testDocumentTypeHasAttribute() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertTrue(documentType.has(DocumentType.NAME));
        assertTrue(documentType.has(DocumentType.PUBLIC_ID));
        assertTrue(documentType.has(DocumentType.SYSTEM_ID));

        assertFalse(documentType.has("nonExistentAttribute"));
    }

    @Test
    public void testDocumentTypeAttributes() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Map<String, Attribute> expectedAttributes = new HashMap<>();
        expectedAttributes.put(DocumentType.NAME, new Attribute(DocumentType.NAME, name));
        expectedAttributes.put(DocumentType.PUBLIC_ID, new Attribute(DocumentType.PUBLIC_ID, publicId));
        expectedAttributes.put(DocumentType.SYSTEM_ID, new Attribute(DocumentType.SYSTEM_ID, systemId));

        Map<String, Attribute> actualAttributes = new HashMap<>();
        for (Node node : documentType.childNodes()) {
            actualAttributes.put(node.nodeName(), (Attribute) node);
        }

        assertEquals(expectedAttributes, actualAttributes);
    }

    @Test
    public void testDocumentTypeToString() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        String expected = "<html xmlns:DTD=\"\" xmlns:html=\"\">" +
                "<!DOCTYPE " + name + " PUBLIC \"publicId\" \"systemId\">";
        String output = documentType.toString();
        assertEquals(expected, output);

        // test toString() with an empty document type
        DocumentType emptyDocumentType = new DocumentType(name, null, null, null);
        expected = "<!" + name + ">";
        output = emptyDocumentType.toString();
        assertEquals(expected, output);
    }
}