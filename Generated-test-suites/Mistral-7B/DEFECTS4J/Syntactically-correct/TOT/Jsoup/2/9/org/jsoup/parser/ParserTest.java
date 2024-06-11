import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.BaseElement;
import org.jsoup.nodes.DataNode;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 parser = new Parser("");
 doc = parser.parse("<html><head></head><body></body></html>", "http://example.com");
 }

 @Test
 public void testParseEmptyHtml() {
 assertEquals("", doc.text());
 assertTrue(doc.getElementsByTag("html").size() == 1);
 assertTrue(doc.getElementsByTag("head").size() == 1);
 assertTrue(doc.getElementsByTag("body").size() == 1);
 }

 @Test
 public void testParseHtmlWithText() {
 parser = new Parser("<p>Hello World</p>", "http://example.com");
 doc = parser.parse();
 assertEquals("Hello World", doc.getElementsByTag("p").text());
 }

 @Test
 public void testParseHtmlWithComment() {
 parser = new Parser("<!-- This is a comment -->", "http://example.com");
 doc = parser.parse();
 Element comment = doc.getElementsByTag("!--").first();
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseHtmlWithXmlDeclaration() {
 parser = new Parser("<xml version=\"1.0\" encoding=\"UTF-8\"?>", "http://example.com");
 doc = parser.parse();
 XmlDeclaration xmlDecl = doc.getElementsByTag("xml").first();
 assertEquals("UTF-8", xmlDecl.encoding());
 }

 @Test
 public void testParseHtmlWithEmptyTag() {
 parser = new Parser("<img src=\"image.png\"/>", "http://example.com");
 doc = parser.parse();
 Element img = doc.getElementsByTag("img").first();
 assertTrue(img.tagName().equals("img"));
 assertTrue(img.classes().isEmpty());
 assertTrue(img.attributes().isEmpty());
 }

 @Test
 public void testParseHtmlWithAttributes() {
 parser = new Parser("<a href=\"http://example.com\" class=\"link\" title=\"Test\">Link</a>", "http://example.com");
 doc = parser.parse();
 Element a = doc.getElementsByTag("a").first();
 List<Attribute> attributes = a.attributes();
 assertEquals(3, attributes.size());
 assertEquals("http://example.com", attributes.get(0).getValue());
 assertEquals("link", attributes.get(1).getValue());
 assertEquals("Test", attributes.get(2).getValue());
 }

 @Test
 public void testParseHtmlWithNestedTags() {
 parser = new Parser("<div><p>Hello <b>World!</b></p></div>", "http://example.com");
 doc = parser.parse();
 Element div = doc.getElementsByTag("div").first();
 Element p = div.getElementsByTag("p").first();
 Element b = p.getElementsByTag("b").first();
 assertEquals("Hello <b>World!</b>", p.text());
 assertEquals("World!", b.text());
 }

 @Test
 public void testParseHtmlWithBaseTag() {
 parser = new Parser("<base href=\"/test/\"><html><head></head><body><a href=\"/test/page.html\">Page</a></body></html>", "http://example.com");
 doc = parser.parse();
 BaseElement base = doc.getElementsByTag("base").first();
 assertEquals("/test/", base.attr("href"));
 Element a = doc.getElementsByTag("a").first();
 assertEquals("/test/page.html", a.absUrl("href"));
 }

 @Test
 public void testParseHtmlWithCdata() {
 parser = new Parser("<script><![CDATA[function test() { alert(\"Hello World\"); }]]></script>", "http://example.com");
 doc = parser.parse();
 Element script = doc.getElementsByTag("script").first();
 DataNode cdata = (DataNode) script.getElementsByTag("script").first().child(0);
 assertEquals("function test() { alert(\"Hello World\"); }", cdata.text());
 }

 @Test
 public void testParseInvalidHtml() {
 parser = new Parser("<html><head><script>alert('Hello World')</script></head><body>"); // missing closing tag in head
 try {
 doc = parser.parse();
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 assertEquals("", doc.text());
 }
}