import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseHtml() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");

        Element htmlNode = doc.head();
        Assert.assertEquals("html", htmlNode.tagName());
        Element titleNode = doc.head().title();
        Assert.assertEquals("Test Page", titleNode.text());

        Element bodyNode = doc.body();
        Element h1Node = bodyNode.child(0);
        Assert.assertEquals("h1", h1Node.tagName());
        Assert.assertEquals("Hello World", h1Node.text());

        List<Element> scripts = doc.body().getElementsByTag("script");
        Assert.assertEquals(0, scripts.size());

        List<Element> images = doc.body().getElementsByTag("img");
        Assert.assertEquals(0, images.size());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Fragment</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");

        Element htmlNode = doc.head();
        Assert.assertNull(htmlNode);

        Element bodyNode = doc.body();
        Element h1Node = bodyNode.child(0);
        Assert.assertEquals("h1", h1Node.tagName());
        Assert.assertEquals("Fragment", h1Node.text());

        List<Element> scripts = doc.body().getElementsByTag("script");
        Assert.assertEquals(0, scripts.size());

        List<Element> images = doc.body().getElementsByTag("img");
        Assert.assertEquals(0, images.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        Parser.parse(null, "http://test.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyHtml() {
        Parser.parse("", "http://test.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidStartTag() {
        String html = "<invalidTag>Test</invalidTag>";
        try {
            Parser.parse(html, "http://test.com");
        } catch (Exception e) {
            Assert.fail("Expected IllegalArgumentException, got " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidEndTag() {
        String html = "<html><body></htmlbar>";
        try {
            Parser.parse(html, "http://test.com");
        } catch (Exception e) {
            Assert.fail("Expected IllegalArgumentException, got " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSelfClosingTagWithoutSlash() {
        String html = "<img Test />";
        try {
            Parser.parse(html, "http://test.com");
        } catch (Exception e) {
            Assert.fail("Expected IllegalArgumentException, got " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSelfClosingTagWithSlash() {
        String html = "<img Test />";
        try {
            Parser.parse(html, "http://test.com");
        } catch (Exception e) {
            Assert.fail("Expected IllegalArgumentException, got " + e.getClass().getSimpleName());
        }

        String html2 = "<img Test>";
        try {
            Parser.parse(html2, "http://test.com");
        } catch (Exception e) {
            Assert.fail("Expected IllegalArgumentException, got " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseUnknownTag() {
        String html = "<unknownTag />";
        try {
            Parser.parse(html, "http://test.com");
        } catch (Exception e) {
            Assert.fail("Expected IllegalArgumentException, got " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseCdataStartTagWithoutCdata() {
        String html = "<![CDATA[Test<html>";
        try {
            Parser.parse(html, "http://test.com");
        } catch (Exception e) {
            Assert.fail("Expected IllegalArgumentException, got " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseCdataStartTagWithCdata() {
        String html = "<![CDATA[Test<![CDATA[Test]]>]]>";
        try {
            Parser.parse(html, "http://test.com");
        } catch (Exception e) {
            Assert.fail("Expected IllegalArgumentException, got " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlDeclWithoutVersion() {
        String html = "<xml>";
        try {
            Parser.parse(html, "http://test.com");
        } catch (Exception e) {
            Assert.fail("Expected IllegalArgumentException, got " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlDeclWithVersion() {
        String html = "<xml version='1.0'>";
        try {
            Parser.parse(html, "http://test.com");
        } catch (Exception e) {
            Assert.fail("Expected IllegalArgumentException, got " + e.getClass().getSimpleName());
        }
    }
}