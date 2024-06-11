package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    private Parser parser;
    private Document doc;

    @Before
    public void setUp() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        parser = new Parser(html, "/");
        doc = parser.parse();
    }

    @Test
    public void testParseWithValidBaseUri() {
        assertNotNull(doc);
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World!", doc.body().children().get(0).text());
        assertEquals("/", doc.baseUri());
    }

    @Test
    public void testParseWithEmptyBodyFragment() {
        String bodyHtml = "<script type=\"text/javascript\">var test = \"hello\";</script>";
        Document bodyFragmentDoc = Parser.parseBodyFragment(bodyHtml, "/");
        assertTrue(bodyFragmentDoc.head() == null);
        assertEquals("/", bodyFragmentDoc.baseUri());
        Element body = bodyFragmentDoc.body();
        assertFalse(body.text().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullHtml() {
        Parser.parse(null, "/");
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Parser.parse(html, null);
    }

    @Test
    public void testParseWithComment() {
        String html = "<html><!-- Test comment --><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document doc = Parser.parse(html, "/");
        Element comment = doc.body().child(0);
        assertEquals("Test comment", comment.text());
    }

    @Test
    public void testParseWithCdata() {
        String html = "<html><![CDATA[<script type=\"text/javascript\">var test = \"hello\";</script>]]><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document doc = Parser.parse(html, "/");
        Element cdata = doc.body().child(0);
        assertEquals("<script type=\"text/javascript\">var test = \"hello\";</script>", cdata.text());
    }

    @Test
    public void testParseWithXmlDeclaration() {
        String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
        Document doc = Parser.parse(html, "/");
        Element xmlDeclaration = doc.child(0);
        assertEquals("http://www.w3.org/1999/xhtml", xmlDeclaration.attr("xmlns"));
    }

    @Test
    public void testParseWithEndTag() {
        String html = "<html><body><div><p>Test</p></div></body></html>";
        Document doc = Parser.parse(html, "/");
        Element div = doc.body().child(0);
        assertNull(div.child(1));
        assertEquals(1, div.children().size());
    }

    @Test
    public void testParseWithStartTag() {
        String html = "<html><body><p>Test</p></body></html>";
        Document doc = Parser.parse(html, "/");
        Element p = doc.body().child(0);
        assertEquals(1, p.children().size());
    }

    @Test
    public void testParseWithTextNode() {
        String html = "<html><body><p>Test</p></body></html>";
        Document doc = Parser.parse(html, "/");
        TextNode textNode = (TextNode) doc.body().child(0).child(0);
        assertEquals("Test", textNode.text());
    }

    @Test
    public void testParseWithInvalidTagName() {
        String html = "<html><body><invalid></body></html>";
        Document doc = Parser.parse(html, "/");
        assertNull(doc.body().child(0));
    }

    @Test
    public void testParseWithAttribute() {
        String html = "<html><body><p class=\"test\" id=\"testId\"></p></body></html>";
        Document doc = Parser.parse(html, "/");
        Element p = doc.body().child(0);
        assertEquals("test", p.classes().get(0));
        assertEquals("testId", p.id());
    }

    @Test
    public void testParseWithEmptyAttribute() {
        String html = "<html><body><p></p></body></html>";
        Document doc = Parser.parse(html, "/");
        Element p = doc.body().child(0);
        assertTrue(p.attributes().isEmpty());
    }

    @Test
    public void testParseWithQuotedAttributeValue() {
        String html = "<html><body><p title=\"Test Title\"></p></body></html>";
        Document doc = Parser.parse(html, "/");
        Element p = doc.body().child(0);
        assertEquals("Test Title", p.title().text());
    }

    @Test
    public void testParseWithUnquotedAttributeValue() {
        String html = "<html><body><p class=test></p></body></html>";
        Document doc = Parser.parse(html, "/");
        Element p = doc.body().child(0);
        assertEquals("test", p.classes().get(0));
    }

    @Test
    public void testParseWithMultilineAttributeValue() {
        String html = "<html><body><p id=\"testId\" style=\"color: red; font-size: 24px;\"></p></body></html>";
        Document doc = Parser.parse(html, "/");
        Element p = doc.body().child(0);
        assertEquals("red", p.style("color"));
        assertEquals("24px", p.style("font-size"));
    }

    @Test
    public void testParseWithMixedAttributeTypes() {
        String html = "<html><body><p id=\"testId\" class=\"test\" title=\"Test Title\"></p></body></html>";
        Document doc = Parser.parse(html, "/");
        Element p = doc.body().child(0);
        assertEquals("test", p.classes().get(0));
        assertEquals("Test Title", p.title().text());
        assertEquals("testId", p.id());
    }

    @Test
    public void testParseWithSelfClosingStartTag() {
        String html = "<html><body><img src=\"test.png\" alt=\"Test Image\"/></body></html>";
        Document doc = Parser.parse(html, "/");
        Element img = doc.body().child(0);
        assertTrue(img.isSelfClosingTag());
        assertEquals("test.png", img.absUrl("src"));
        assertEquals("Test Image", img.attr("alt"));
    }

    @Test
    public void testParseWithEmptyElement() {
        String html = "<html><body><br/></body></html>";
        Document doc = Parser.parse(html, "/");
        Element br = doc.body().child(0);
        assertTrue(br.isEmptyElement());
        assertNull(br.text());
    }

    @Test
    public void testParseWithEmptyElementWithText() {
        String html = "<html><body><br/>Test</body></html>";
        Document doc = Parser.parse(html, "/");
        Element br = doc.body().child(0);
        assertFalse(br.isTextOnly());
        TextNode textNode = (TextNode) br.child(0);
        assertEquals("Test", textNode.text());
    }

    @Test
    public void testParseWithImplicitParent() {
        String html = "<html><body><img src=\"test.png\"/></body></html>";
        Document doc = Parser.parse(html, "/");
        Element img = doc.body().child(0);
        Tag imgTag = img.tag();
        assertSame(imgTag.getImplicitParent(), img.parent());
    }

    @Test
    public void testParseWithInvalidParent() {
        String html = "<html><body><h1></body></html>";
        Document doc = Parser.parse(html, "/");
        Element h1 = doc.body().child(0);
        assertFalse(h1.tag().isValidParent(bodyTag));
        Element implicit = new Element(htmlTag, "/");
        implicit.appendChild(h1);
        Element parent = addChildToParent(implicit, false);
        assertSame(parent, doc);
    }

    @Test
    public void testParseWithValidParent() {
        String html = "<html><head><title>Test</title></head><body><h1></body></html>";
        Document doc = Parser.parse(html, "/");
        Element h1 = doc.body().child(0);
        Element head = doc.head();
        assertSame(head, h1.parent());
    }

    @Test
    public void testParseWithBaseTag() {
        String html = "<html><head><base href=\"test.html\"/><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document doc = Parser.parse(html, "/");
        assertEquals("test.html", doc.baseUri());
    }

    @Test
    public void testParseWithInvalidTagNameInBaseTag() {
        String html = "<html><head><base invalid=\"test.html\"/><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        Document doc = Parser.parse(html, "/");
        assertNull(doc.baseUri());
    }

    @Test
    public void testParseWithNestedImplicitParents() {
        String html = "<html><body><div><span><img src=\"test.png\"/></span></div></body></html>";
        Document doc = Parser.parse(html, "/");
        Element img = doc.body().child(0).child(0).child(0);
        Tag imgTag = img.tag();
        Element div = doc.body().child(0).child(0);
        Element span = doc.body().child(0).child(0).child(0);
        assertSame(imgTag.getImplicitParent(), img.parent());
        assertSame(img.parent(), span);
        assertSame(span.parent(), div);
    }

    private Element addChildToParent(Element parent, boolean isEmptyElement) {
        return parser.addChildToParent(parent, isEmptyElement);
    }

}