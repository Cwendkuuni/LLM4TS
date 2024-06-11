package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TreeBuilder;

public class ParserTest {

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = new Parser(new HtmlTreeBuilder()).parseInput(html, baseUri);
        assertEquals(html, document.html());
    }

    @Test(expected = NullPointerException.class)
    public void testNullHtml() {
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, baseUri);
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(html, null);
    }

    @Test
    public void testEmptyHtml() {
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document document = parser.parseInput("", baseUri);
        assertTrue(document.body() == null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidHtml() {
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput("</html>", baseUri);
    }

    @Test
    public void testLargeHtml() {
        String html = "<html lang=\"en\">" +
                "  <head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <title>Test</title>" +
                "  </head>" +
                "  <body>" +
                "    <h1>Test</h1>" +
                "    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>" +
                "  </body>" +
                "</html>";
        String baseUri = "http://example.com";
        List<ParseError> errors = new Parser(new HtmlTreeBuilder()).parseInput(html, baseUri).errors();
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testMaxErrorsReached() {
        String html = "<html lang=\"en\">" +
                "  <head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <title>Test</title>" +
                "  </head>" +
                "  <body>" +
                "    <h1>Test</h1>" +
                "    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>" +
                "    <script>alert('Hello World')</script>" +
                "    <script>document.write('<script src=\"http://example.com/malicious.js\"></script>')</script>" +
                "  </body>" +
                "</html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(1);
        Document document = parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();
        assertTrue(errors.size() > 0);
    }
}