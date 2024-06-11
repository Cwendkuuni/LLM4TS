package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.helper.ElementFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        Document document = new Document();
        DocumentType doctype = new DocumentType("html", "publicId1", "systemId1", document.baseUri());

        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.attr("name"));
        assertEquals("publicId1", doctype.attr("publicId"));
        assertEquals("systemId1", doctype.attr("systemId"));
        assertEquals(document.baseUri(), doctype.baseUri());
    }
}