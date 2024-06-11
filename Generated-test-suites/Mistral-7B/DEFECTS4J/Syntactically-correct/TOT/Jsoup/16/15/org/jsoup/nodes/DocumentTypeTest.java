package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void SaraswatiTest() {
        // Test Saraswati's test case
        Saraswati saraswati = new Saraswati();
        saraswati.testDocumentTypeCreation();
    }

    @Test
    public void SureshTest() {
        // Test Suresh's test case
        Suresh suresh = new Suresh();
        suresh.testDocumentTypeOuterHtmlHead();
    }

    @Test
    public void RohitTest() {
        // Test Rohit's test case
        Rohit rohit = new Rohit();
        rohit.testDocumentTypeWithEmptyAttributes();
        rohit.testDocumentTypeWithNullBaseUri();
    }

    static class Saraswati {
        public void testDocumentTypeCreation() {
            DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseURI");
            String expectedName = "#doctype";
            String expectedPublicId = "publicID";
            String expectedSystemId = "systemID";
            String expectedBaseUri = "baseURI";

            assertEquals(expectedName, documentType.nodeName());
            assertEquals(expectedPublicId, documentType.attr("publicId"));
            assertEquals(expectedSystemId, documentType.attr("systemId"));
            assertEquals(expectedBaseUri, documentType.baseUri());
        }
    }

    static class Suresh {
        @Test
        public void testDocumentTypeOuterHtmlHead() {
            Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"publicID\" \"systemID\" >");
            DocumentType documentType = document.docType();

            StringBuilder accum = new StringBuilder();
            documentType.outerHtmlHead(accum, 0, null);

            String expectedOutput = "<!DOCTYPE html PUBLIC \"publicID\" \"systemID\" >";
            assertEquals(expectedOutput, accum.toString());
        }
    }

    static class Rohit {
        @Test
        public void testDocumentTypeWithEmptyAttributes() {
            DocumentType documentType = new DocumentType("html");

            String expectedName = "#doctype";
            String expectedPublicId = "";
            String expectedSystemId = "";
            String expectedBaseUri = null;

            assertEquals(expectedName, documentType.nodeName());
            assertEquals(expectedPublicId, documentType.attr("publicId"));
            assertEquals(expectedSystemId, documentType.attr("systemId"));
            assertNull(documentType.baseUri());
        }

        @Test
        public void testDocumentTypeWithNullBaseUri() {
            DocumentType documentType = new DocumentType("html", null, null, null);

            String expectedName = "#doctype";
            String expectedPublicId = "";
            String expectedSystemId = "";
            String expectedBaseUri = null;

            assertEquals(expectedName, documentType.nodeName());
            assertEquals(expectedPublicId, documentType.attr("publicId"));
            assertEquals(expectedSystemId, documentType.attr("systemId"));
            assertNull(documentType.baseUri());
        }
    }
}