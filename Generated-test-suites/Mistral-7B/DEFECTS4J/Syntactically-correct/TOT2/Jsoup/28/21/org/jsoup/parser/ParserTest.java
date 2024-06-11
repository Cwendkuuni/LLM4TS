package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    // Test case for parseInput method with valid input
    @Test
    public void testParseInputValidInput() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput(html, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    // Test case for parseInput method with null input
    @Test(expected = NullPointerException.class)
    public void testParseInputNullInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.parseInput(null, null);
    }

    // Test case for parseInput method with empty input
    @Test
    public void testParseInputEmptyInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        Document doc = parser.parseInput("", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("", doc.body().text());
    }

    // Test case for parseInput method with error tracking enabled
    @Test
    public void testParseInputErrorTrackingEnabled() {
        String html = "<html><head><title>Error Title</title></head><body><script>throw new Error('Test Error')</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder()).setTrackErrors(1);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Test Error", errors.get(0).getMessage());
    }

    // Test case for parseInput method with error tracking disabled
    @Test
    public void testParseInputErrorTrackingDisabled() {
        String html = "<html><head><title>Error Title</title></head><body><script>throw new Error('Test Error')</script></body></html>";
        Parser parser = new Parser(new HtmlTreeBuilder()).setTrackErrors(0);
        Document doc = parser.parseInput(html, null);
        List<ParseError> errors = parser.getErrors();
        Assert.assertEquals(0, errors.size());
    }

    // Test case for parse method
    @Test
    public void testParseValidInput() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    // Test case for parse method with null input
    @Test(expected = NullPointerException.class)
    public void testParseNullInput() {
        Parser.parse(null, null);
    }

    // Test case for parse method with empty input
    @Test
    public void testParseEmptyInput() {
        Document doc = Parser.parse("", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("", doc.body().text());
    }

    // Test case for parseFragment method
    @Test
    public void testParseFragmentValidInput() {
        String fragmentHtml = "<div>Test Fragment</div>";
        Document doc = Parser.parseBodyFragment(fragmentHtml, null);
        Node node = doc.select("div").get(0);
        Assert.assertNotNull(node);
        Assert.assertEquals("Test Fragment", node.text());
    }

    // Test case for parseFragment method with null input
    @Test(expected = NullPointerException.class)
    public void testParseFragmentNullInput() {
        Parser.parseFragment(null, null, null);
    }

    // Test case for parseFragment method with empty input
    @Test
    public void testParseFragmentEmptyInput() {
        List<Node> nodes = Parser.parseFragment("", null, null);
        Assert.assertEquals(0, nodes.size());
    }

    // Test case for parseBodyFragment method
    @Test
    public void testParseBodyFragmentValidInput() {
        String bodyHtml = "<body><div>Test Body Fragment</div></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, null);
        Node node = doc.select("div").get(0);
        Assert.assertNotNull(node);
        Assert.assertEquals("Test Body Fragment", node.text());
    }

    // Test case for parseBodyFragment method with null input
    @Test(expected = NullPointerException.class)
    public void testParseBodyFragmentNullInput() {
        Parser.parseBodyFragment(null, null);
    }

    // Test case for parseBodyFragment method with empty input
    @Test
    public void testParseBodyFragmentEmptyInput() {
        Document doc = Parser.parseBodyFragment("", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals("", doc.body().text());
    }

    // Test case for unescapeHtmlEntities method
    @Test
    public void testUnescapeHtmlEntities() {
        String htmlEscaped = "&amp;lt;h1&amp;gt;Test&amp;nbsp;Unescaped&amp;lt;/h1&amp;gt;";
        String unescaped = Parser.unescapeHtmlEntities(htmlEscaped);
        Assert.assertEquals("<h1>Test Unescaped</h1>", unescaped);
    }

    // Test case for parseBodyFragmentRelaxed method (deprecated)
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<body><div>Test Body Fragment</div></body>";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, null);
        Node node = doc.select("div").get(0);
        Assert.assertNotNull(node);
        Assert.assertEquals("Test Body Fragment", node.text());
    }

    // Test case for htmlParser method
    @Test
    public void testHtmlParser() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Parser parser = Parser.htmlParser();
        Document doc = parser.parseInput(html, null);

        Assert.assertNotNull(doc);
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    // Test case for xmlParser method
    @Test
    public void testXmlParser() {
        String xml = "<xml><data>Test Data</data></xml>";
        Parser parser = Parser.xmlParser();
        Document doc = parser.parseInput(xml, null);

        Assert.assertNotNull(doc);
        Assert.assertEquals("Test Data", doc.select("data").text());
    }
}