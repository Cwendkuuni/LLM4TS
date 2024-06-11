
package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseHTML() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Parser parser = new Parser(html, "http://test.com");
        Document doc = parser.parse();

        // Check the parsed document structure
        Element root = doc.body();
        assertNotNull(root);
        assertEquals("body", root.tagName());
        assertEquals("http://test.com", doc.baseUri());

        // Check the title tag
        Element title = root.head();
        assertEquals("head", title.tagName());
        assertEquals("Test Title", title.text());

        // Check the heading tag
        Element heading = root.getFirstElementChild();
        assertEquals("h1", heading.tagName());
        assertEquals("Test Heading", heading.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Test Heading</h1></body>";
        Parser parser = new Parser(bodyHtml, "http://test.com");
        Document doc = parser.parse();

        // Check the parsed document structure
        assertEquals("html", doc.tagName());
        assertEquals("http://test.com", doc.baseUri());

        Element root = doc; // Doc is the implicit HTML tag
        Element body = root.getFirstElementChild();
        assertEquals("body", body.tagName());

        // Check the heading tag
        Element heading = body.getFirstElementChild();
        assertEquals("h1", heading.tagName());
        assertEquals("Test Heading", heading.text());
    }

    @Test
    public void testParseInvalidHTML() {
        String invalidHtml = "<!DOCTYPE html><html><body><script>alert('invalid html');</script><h1>Test Heading</h1>";
        Parser parser = new Parser(invalidHtml, "http://test.com");

        try {
            Document doc = parser.parse();
            fail("Expected ParserException");
        } catch (Parser.ParserException e) {
            // Parsing failed, test exception message
            String message = e.getMessage();
            assertTrue(message.contains("script tag"));
        }
    }
}