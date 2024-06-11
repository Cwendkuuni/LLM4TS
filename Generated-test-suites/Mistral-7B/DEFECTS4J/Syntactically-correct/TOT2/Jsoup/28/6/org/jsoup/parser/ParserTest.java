package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;

public class ParserTest {
    @Test
    public void testParseInput() {
        // Happy Path
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = parser.parseInput(html, baseUri);
        Assert.assertNotNull(doc);

        // Edge Case: Empty Input
        parser = new Parser(new HtmlTreeBuilder());
        html = "";
        doc = parser.parseInput(html, baseUri);
        Assert.assertNull(doc);

        // Edge Case: Invalid Input
        parser = new Parser(new HtmlTreeBuilder());
        html = "<script>alert('Hello!')</script>";
        doc = parser.parseInput(html, baseUri);
        Assert.assertNotNull(doc);
        Element script = doc.selectFirst("script");
        Assert.assertEquals("script", script.tagName());

        // Exception Handling: Parse Errors
        parser = new Parser(new HtmlTreeBuilder());
        html = "<html><body><script>throw new Error('Test Error')</script></body></html>";
        parser.setTrackErrors(1);
        doc = parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();
        Assert.assertFalse(errors.isEmpty());
        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals("Test Error", error.getMessage());
        Assert.assertEquals(ParseError.ERROR_CLASS_SCRIPT_PARSE, error.getType());
    }

    // Add other test cases here for other methods
}