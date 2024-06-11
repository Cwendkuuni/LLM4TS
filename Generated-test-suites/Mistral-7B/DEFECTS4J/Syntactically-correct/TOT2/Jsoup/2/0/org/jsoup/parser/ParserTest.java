package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(html, baseUri, false);
        Document doc = parser.parse();

        Element htmlNode = doc.head();
        Assert.assertEquals(htmlTag, htmlNode.tag());
        Assert.assertEquals(baseUri, htmlNode.baseUri());

        Element titleNode = htmlNode.child(0);
        Assert.assertEquals(titleTag, titleNode.tag());
        Assert.assertEquals("Test", titleNode.text());

        Element bodyNode = doc.body();
        Assert.assertEquals(bodyTag, bodyNode.tag());

        Element h1Node = bodyNode.child(0);
        Assert.assertEquals(h1Tag, h1Node.tag());
        Assert.assertEquals("Hello World", h1Node.text());

        // Test parsing a body fragment
        String bodyHtml = "<h1>Fragment</h1>";
        Document fragmentDoc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Element fragmentBodyNode = fragmentDoc.body();
        Assert.assertEquals(bodyTag, fragmentBodyNode.tag());
        Assert.assertEquals(1, fragmentBodyNode.childSize());
        Assert.assertEquals(h1Tag, fragmentBodyNode.child(0).tag());
        Assert.assertEquals("Fragment", fragmentBodyNode.child(0).text());
    }

    @Test
    public void testParseInvalidHtml() {
        String invalidHtml = "<html><body<h1>Hello World";
        String baseUri = "http://example.com";

        try {
            Parser parser = new Parser(invalidHtml, baseUri, false);
            parser.parse();
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Add more test cases here for other methods and potential edge cases
}