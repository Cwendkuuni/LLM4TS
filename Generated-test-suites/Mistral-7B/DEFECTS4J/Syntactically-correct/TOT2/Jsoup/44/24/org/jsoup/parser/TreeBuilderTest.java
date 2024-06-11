package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.StartTag;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeBuilderTest {
    @Test
    public void testParseEmptyInput() {
        TreeBuilder builder = new TreeBuilder();
        Document doc = builder.parse("", "baseUri", ParseErrorList.noTracking());
        Element root = doc.body();
        assertNull(root);
    }

    @Test
    public void testParseSimpleStartTag() {
        TreeBuilder builder = new TreeBuilder();
        StartTag startTag = new StartTag("div", new Attributes());
        builder.start = startTag;
        Document doc = builder.parse("<div></div>", "baseUri", ParseErrorList.noTracking());
        Element root = doc.body();
        assertEquals("div", root.tagName());
        assertNull(root.children());
    }

    @Test
    public void testParseSimpleStartTagWithAttributes() {
        TreeBuilder builder = new TreeBuilder();
        StartTag startTag = new StartTag("p", new Attributes().put("id", "test"));
        builder.start = startTag;
        Document doc = builder.parse("<p id='test'></p>", "baseUri", ParseErrorList.noTracking());
        Element root = doc.body();
        assertEquals("p", root.tagName());
        Element attribute = root.child(0);
        assertEquals("id", attribute.tagName());
        assertEquals("test", attribute.attr("id"));
    }

    @Test
    public void testParseSimpleEndTag() {
        TreeBuilder builder = new TreeBuilder();
        Element root = new Element("div");
        builder.stack.add(root);
        StartTag startTag = new StartTag("p", new Attributes());
        builder.start = startTag;
        EndTag endTag = new EndTag("p");
        builder.end = endTag;
        Document doc = builder.parse("<p></p></div>", "baseUri", ParseErrorList.noTracking());
        Element p = root.child(0);
        assertNull(p.nextSibling());
        assertNull(p.nextElementSibling());
    }

    @Test(expected = AssertionError.class)
    public void testParseInvalidInput() {
        TreeBuilder builder = new TreeBuilder();
        Document doc = builder.parse("InvalidInput", "baseUri", ParseErrorList.noTracking());
        fail("Expected an AssertionError but got a Document instead.");
    }
}