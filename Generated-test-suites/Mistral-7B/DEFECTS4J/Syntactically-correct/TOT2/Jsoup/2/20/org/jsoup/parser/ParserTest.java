package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Welcome to the test page!</h1>\n" +
                "</body>\n" +
                "</html>";
        String baseUri = "http://test.com";
        Document doc = Parser.parse(html, baseUri);

        // Check document structure
        assertEquals(htmlTag, doc.tag());
        assertEquals(htmlTag, last(doc.childNodes()).tag());
        assertEquals(headTag, doc.head().tag());
        assertEquals(headTag, doc.head().childNodes().get(0).tag());
        assertEquals(titleTag, doc.head().childNodes().get(0).childNodes().get(0).tag());
        assertEquals("Test Page", doc.head().childNodes().get(0).childNodes().get(0).text());
        assertEquals(bodyTag, doc.body().tag());
        assertEquals(h1Tag, doc.body().childNodes().get(0).tag());
        assertEquals("Welcome to the test page!", doc.body().childNodes().get(0).text());

        // Check base URI
        assertEquals(baseUri, doc.baseUri());

        // Check parsing of empty elements
        String bodyHtml = "<div />";
        Document emptyDoc = Parser.parseBodyFragment(bodyHtml, baseUri);
        assertEquals(htmlTag, emptyDoc.tag());
        assertEquals(bodyTag, emptyDoc.body().tag());
        assertEquals(divTag, emptyDoc.body().childNodes().get(0).tag());
        assertEquals(0, emptyDoc.body().childNodes().size());

        // Test parsing of malformed HTML
        String malformedHtml = "<html><head><title<script>alert('hi')</script>";
        try {
            Parser.parse(malformedHtml, baseUri);
            fail("Expected ParserException not thrown");
        } catch (ParserException e) {
            // Expected
        }
    }

    private Element last(Node node) {
        return node instanceof Element ? (Element) node.lastSibling() : node;
    }
}