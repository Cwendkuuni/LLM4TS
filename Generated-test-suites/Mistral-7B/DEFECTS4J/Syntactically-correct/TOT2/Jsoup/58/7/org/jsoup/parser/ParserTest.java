package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.jsoup.parser.Parser.*;

public class ParserTest {

    @Test
    public void testParserWithDefaults() {
        String html = "<html><body></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder treeBuilder = parser.getTreeBuilder();
        Assert.assertNotNull(treeBuilder);
    }

    @Test
    public void testParserWithEmptyString() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("", "http://example.com");
        Assert.assertNull(doc);
    }

    @Test
    public void testParserWithMalformedHtml() {
        String malformedHtml = "<html><body><script>alert('Hello World')</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(malformedHtml, "http://example.com");
        Assert.assertNotNull(doc); // Malformed HTML should still be parsed into a Document, but may have unexpected structure
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParserWithNullBaseUri() {
        String html = "<html><body></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(html, null);
    }

    @Test(expected = RuntimeException.class)
    public void testTreeBuilderThrowsException() {
        TreeBuilder treeBuilder = new TreeBuilder() {
            @Override
            public Document parse(String html, String baseUri, ParseErrorList errorList, ParseSettings settings) {
                throw new RuntimeException("TreeBuilder Exception");
            }
        };
        Parser parser = new Parser(treeBuilder);
        Assert.assertThrows(RuntimeException.class, () -> parser.parseInput("<html><body></body></html>", "http://example.com"));
    }

    @Test
    public void testMaxErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());

        String html = "<html><body><script>alert('Hello World')</script></body></html>";
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, "http://example.com");
        List<ParseError> errors = parser.getErrors();

        Assert.assertEquals(1, errors.size()); // Test that only one error is reported
        Assert.assertEquals("Unexpected char 60 found", errors.get(0).getMessage()); // Test that expected error is reported
    }

    // ... Add test cases for other methods here ...
}