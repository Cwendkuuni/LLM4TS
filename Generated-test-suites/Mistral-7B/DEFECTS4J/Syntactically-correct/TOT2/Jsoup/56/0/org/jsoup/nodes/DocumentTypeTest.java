package org.jsoup.nodes.test;

import static org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.StringUtil;
import java.io.IOException;
import java.io.StringWriter;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeConstructor() {
        String name = "html";
        String publicId = "PUBLIC_ID";
        String systemId = "SYSTEM_ID";
        String baseUri = "http://example.com/";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr(DocumentType.NAME));
        assertEquals(publicId, documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals(systemId, documentType.attr(DocumentType.SYSTEM_ID));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testHasMethod() {
        DocumentType documentType = new DocumentType("html", "PUBLIC_ID", "SYSTEM_ID", null);

        assertTrue(documentType.has(DocumentType.NAME));
        assertTrue(documentType.has(DocumentType.PUBLIC_ID));
        assertTrue(documentType.has(DocumentType.SYSTEM_ID));

        assertFalse(documentType.has("non-existent-attribute"));
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType documentType = new DocumentType("html", "PUBLIC_ID", "SYSTEM_ID", null);
        Document document = new Document();
        StringWriter stringWriter = new StringWriter();

        document.outerHtmlHead(stringWriter, 0, Document.OutputSettings.html5());

        assertEquals("<!doctype html PUBLIC \"PUBLIC_ID\" SYSTEM \"SYSTEM_ID\">", stringWriter.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeEmptyConstructor() {
        new DocumentType(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeNullName() {
        new DocumentType(null, "PUBLIC_ID", "SYSTEM_ID", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeNullPublicId() {
        new DocumentType("name", null, "SYSTEM_ID", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeNullSystemId() {
        new DocumentType("name", "PUBLIC_ID", null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeNullBaseUri() {
        new DocumentType("name", "PUBLIC_ID", "SYSTEM_ID", null);
    }

    @Test
    public void testDocumentTypeEmptyAttributes() {
        DocumentType documentType = new DocumentType("name", null, null, null);

        assertFalse(documentType.has(DocumentType.PUBLIC_ID));
        assertFalse(documentType.has(DocumentType.SYSTEM_ID));
    }
}