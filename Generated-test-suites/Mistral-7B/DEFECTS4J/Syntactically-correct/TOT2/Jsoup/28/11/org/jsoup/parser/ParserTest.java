package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.getElementsByTag("h1").text());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<invalid>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        List<ParseError> errors = parser.getErrors();

        parser.parseInput(html, baseUri);
        Assert.assertTrue(errors.size() > 0);
    }

    @Test
    public void testParseInputWithMaxErrors() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);

        Document doc = parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();

        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.getElementsByTag("h1").text());
        Assert.assertEquals(5, errors.size());
    }

    @Test
    public void testParseStaticFunctions() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.getElementsByTag("h1").text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithNullHtml() {
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testParseInputWithNullBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(html, null);
    }
}