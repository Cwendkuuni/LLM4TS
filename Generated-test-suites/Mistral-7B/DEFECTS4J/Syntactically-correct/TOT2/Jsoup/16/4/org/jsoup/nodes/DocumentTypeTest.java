package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import org.junit.Assert.*;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicId1", "systemId1", "baseUri1");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("publicId1", docType.attr("publicId"));
        assertEquals("systemId1", docType.attr("systemId"));
        assertEquals("baseUri1", docType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "", "", "");
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "publicId1", "systemId1", "baseUri1");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE htmlPUBLIC \"publicId1\" systemId1>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "publicId1", "systemId1", "baseUri1");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeCreationWithNullName() {
        new DocumentType(null, "publicId1", "systemId1", "baseUri1");
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeCreationWithNullPublicId() {
        new DocumentType("html", null, "systemId1", "baseUri1");
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeCreationWithNullSystemId() {
        new DocumentType("html", "publicId1", null, "baseUri1");
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeCreationWithNullBaseUri() {
        new DocumentType("html", "publicId1", "systemId1", null);
    }

    @Test
    public void testDocumentTypeCreationWithEmptyPublicId() {
        DocumentType docType = new DocumentType("html", "", "systemId1", "baseUri1");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("", docType.attr("publicId"));
        assertEquals("systemId1", docType.attr("systemId"));
        assertEquals("baseUri1", docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", "", "", "baseUri1");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("", docType.attr("publicId"));
        assertEquals("", docType.attr("systemId"));
        assertEquals("baseUri1", docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyBaseUri() {
        DocumentType docType = new DocumentType("html", "publicId1", "systemId1", "");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("publicId1", docType.attr("publicId"));
        assertEquals("systemId1", docType.attr("systemId"));
        assertEquals("", docType.baseUri());
    }
}