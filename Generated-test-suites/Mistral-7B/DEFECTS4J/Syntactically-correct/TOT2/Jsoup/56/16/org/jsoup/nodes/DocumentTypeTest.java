package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeConstructor() {
        String name = "HTML";
        String publicId = "public.id";
        String systemId = "system.id";
        String baseUri = "base-uri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr(DocumentType.NAME));
        assertEquals(publicId, documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals(systemId, documentType.attr(DocumentType.SYSTEM_ID));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeNullName() {
        String publicId = "public.id";
        String systemId = "system.id";
        String baseUri = "base-uri";

        DocumentType documentType = new DocumentType(null, publicId, systemId, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeNullPublicId() {
        String name = "HTML";
        String systemId = "system.id";
        String baseUri = "base-uri";

        DocumentType documentType = new DocumentType(name, null, systemId, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeNullSystemId() {
        String name = "HTML";
        String publicId = "public.id";
        String baseUri = "base-uri";

        DocumentType documentType = new DocumentType(name, publicId, null, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeNullBaseUri() {
        String name = "HTML";
        String publicId = "public.id";
        String systemId = "system.id";

        DocumentType documentType = new DocumentType(name, publicId, systemId, null);
    }

    @Test
    public void testDocumentTypeHasAttribute() {
        String name = "HTML";
        String publicId = "public.id";
        String systemId = "system.id";
        String baseUri = "base-uri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        documentType.attr(DocumentType.NAME, name);
        documentType.attr(DocumentType.PUBLIC_ID, publicId);
        documentType.attr(DocumentType.SYSTEM_ID, systemId);

        assertTrue(documentType.has(DocumentType.NAME));
        assertTrue(documentType.has(DocumentType.PUBLIC_ID));
        assertTrue(documentType.has(DocumentType.SYSTEM_ID));

        assertFalse(documentType.has("non-existing-attribute"));
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() throws IOException {
        String name = "HTML";
        String publicId = "public.id";
        String systemId = "system.id";
        String baseUri = "base-uri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Appendable accumulator = new StringBuilder();
        documentType.outerHtmlHead(accumulator, 0, null);

        String expectedHtml = "<!DOCTYPE " + name + " PUBLIC \"%\" " + publicId + " \"" + systemId + "\" >";
        assertEquals(expectedHtml, accumulator.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() throws IOException {
        String name = "HTML";
        String publicId = "public.id";
        String systemId = "system.id";
        String baseUri = "base-uri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Appendable accumulator = new StringBuilder();
        documentType.outerHtmlTail(accumulator, 0, null);

        assertEquals("", accumulator.toString());
    }
}