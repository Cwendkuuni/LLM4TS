package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 parser = new Parser("", "", false);
 doc = parser.parse("<html><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>");
 }

 @Test
 public void testParseComment() {
 String html = "<html><!-- This is a comment --> <head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";
 Document parsed = Parser.parse(html, "");
 Element comment = parsed.head().child(0);
 assertEquals("This is a comment", comment.text());
 assertTrue(comment.tagName().equals("comment"));
 }

 @Test
 public void testParseCdata() {
 String html = "<html><![CDATA[ <script type=\"text/javascript\"> alert('Hello, world!'); </script> ]]><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";
 Document parsed = Parser.parse(html, "");
 Element cdata = parsed.head().child(0);
 assertTrue(cdata.tagName().equals("cdata"));
 assertEquals("<script type=\"text/javascript\"> alert('Hello, world!'); </script>", cdata.text());
 }

 @Test
 public void testParseXmlDecl() {
 String html = "<html xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xml:lang=\"en\"><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";
 Document parsed = Parser.parse(html, "");
 XmlDeclaration xmlDecl = parsed.head().child(0);
 assertEquals("xml version=\"1.0\" standalone=\"no\"?", xmlDecl.xmlDecl());
 assertTrue(xmlDecl.isStandalone());
 }

 @Test
 public void testParseEmptyTag() {
 String html = "<html><body><img src=\"image.jpg\"/></body></html>";
 Document parsed = Parser.parseBodyFragment(html, "");
 Element img = parsed.body().child(0);
 assertTrue(img.tagName().equals("img"));
 assertTrue(img.isEmpty());
 }

 @Test
 public void testParseStartTag() {
 String html = "<html><body><h1 id=\"title1\" class=\"title\">Hello, world!</h1></body></html>";
 Document parsed = Parser.parse(html, "");
 Element title = parsed.body().child(0);
 assertEquals("h1", title.tagName().toString());
 assertTrue(title.classes().size() > 0);
 assertEquals("title", title.id());
 }

 @Test
 public void testParseEndTag() {
 String html = "<html><body><h1>Hello, world!</h1></body></html>";
 Document parsed = Parser.parse(html, "");
 Element h1 = parsed.body().child(0);
 assertEquals(1, h1.childSize());
 assertEquals("h1", h1.tagName().toString());
 }

 @Test
 public void testParseAttribute() {
 String html = "<html><body><a href=\"http://example.com\" title=\"Link title\"><img src=\"image.jpg\" alt=\"Image description\"></a></body></html>";
 Document parsed = Parser.parse(html, "");
 Element a = parsed.body().child(0);
 List<Attribute> attributes = a.attributes();
 Map<String, String> expectedAttributes = new HashMap<>();
 expectedAttributes.put("href", "http://example.com");
 expectedAttributes.put("title", "Link title");
 assertEquals(expectedAttributes.size(), attributes.size());
 for (Attribute attribute : attributes) {
 assertEquals(expectedAttributes.get(attribute.key()), attribute.value());
 expectedAttributes.remove(attribute.key());
 }
 assertNull(expectedAttributes.size());
 }

 @Test
 public void testParseTextNode() {
 String html = "<html><body><p>Test <script>alert('Hello, world!');</script> world!</p></body></html>";
 Document parsed = Parser.parse(html, "");
 Element p = parsed.body().child(0);
 assertEquals(2, p.childSize());
 StringBuilder expectedText = new StringBuilder();
 expectedText.append("Test ");
 expectedText.append(StringUtils.repeat(" ", 20)); // add 20 whitespaces to mimic script tag
 expectedText.append("world!");
 assertEquals(expectedText.toString(), p.child(0).text());
 assertTrue(p.child(1).tagName().toString().equals("script"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidHtml() {
 String html = "<html><body><h1<title>Hello, world!</h1>";
 parser = new Parser(html, "", false);
 parser.parse();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidHtml2() {
 String html = "<html><body><h1>Hello, world!<title>";
 parser = new Parser(html, "", false);
 parser.parse();
 }

 @Test
 public void testParseBodyFragment() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";
 Document parsed = Parser.parseBodyFragment(html, "");
 Element h1 = parsed.body().child(0);
 assertEquals("h1", h1.tagName().toString());
 }

 @Test
 public void testParseWithBaseAttribute() {
 String html = "<html><head><base href=\"/test/\" /> <title>Test</title></head><body><a href=\"page.html\">Link</a><h1>Hello, world!</h1></body></html>";
 Document parsed = Parser.parse(html, "");
 Element a = parsed.body().child(0);
 assertEquals("/test/page.html", a.absUrl("href"));
 }

 @Test
 public void testParseEmptyDocument() {
 String html = "<html></html>";
 Document parsed = Parser.parse(html, "");
 assertTrue(parsed.isEmpty());
 }

 @Test
 public void testParseFragmentWithScriptTag() {
 String html = "<html><script type=\"text/javascript\">alert('Hello, world!');</script><body></body></html>";
 Document parsed = Parser.parseBodyFragment(html, "");
 assertNotNull(parsed.body());
 }

 @Test
 public void testParseFragmentWithEmptyBody() {
 String html = "<html><body></body></html>";
 Document parsed = Parser.parseBodyFragment(html, "");
 assertTrue(parsed.body().isEmpty());
 }

 @Test
 public void testParseFragmentWithInvalidHtml() {
 String html = "<html><body><h1Hello, world!</h1>";
 Document parsed = Parser.parseBodyFragment(html, "");
 assertNull(parsed);
 }

 @Test
 public void testParseFragmentWithBodyTag() {
 String html = "<html><body><body><h1>Hello, world!</h1></body></body></html>";
 Document parsed = Parser.parseBodyFragment(html, "");
 assertNull(parsed);
 }

 @Test
 public void testParseFragmentWithHeadTag() {
 String html = "<html><head><head><title>Test</title></head></head></html>";
 Document parsed = Parser.parseBodyFragment(html, "");
 assertNull(parsed);
 }

 @Test
 public void testParseFragmentWithScriptAndStyleTags() {
 String html = "<html><head><title>Test</title><script type=\"text/javascript\"><!--\n" +
 "// Disabled by IE6 and older\n" +
 "function disableAnimations() {\n" +
 "if (window.XMLHttpRequest) {\n" +
 "XMLHttpRequest.prototype.sendAsBinary = function() {\n" +
 "this._binary = true;\n" +
 "}\n" +
 "}\n" +
 "--></script><style type=\"text/css\">body { color: red; }</style></head><body></body></html>";
 Document parsed = Parser.parseBodyFragment(html, "");
 assertNotNull(parsed.body());
 }

 @Test
 public void testParseFragmentWithBaseTag() {
 String html = "<html><head><base href=\"/test/\" /> <title>Test</title></head><body></body></html>";
 Document parsed = Parser.parseBodyFragment(html, "");
 assertEquals("/test/", parsed.baseUri());
 }

 @Test
 public void testParseFragmentWithMetaTag() {
 String html = "<html><head><title>Test</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /></head><body></body></html>";
 Document parsed = Parser.parseBodyFragment(html, "");
 assertEquals("UTF-8", parsed.charset());
 }

 @Test
 public void testParseFragmentWithDocumentTypeDeclaration() {
 String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" +
 "<html><head><title>Test</title></head><body></body></html>";
 Document parsed = Parser.parseBodyFragment(html, "");
 assertNotNull(parsed.doctype());
 }

 @Test
 public void testParseFragmentWithScriptTagWithAttributes() {
 String html = "<html><head><title>Test</title></head><body><script src=\"script.js\" type=\"text/javascript\" charset=\"UTF-8\"></script></body></html>";
 Document parsed = Parser.parseBodyFragment(html, "");
 assertEquals("UTF-8", parsed.scriptEncoding());
 }

 @Test
 public void testParseFragmentWithNestedScriptTags() {
 String html = "<html><head><title>Test</title></head><body><script type=\"text/javascript\">console.log('Outer script');function innerScript() {\nconsole.log('Inner script');}\ninnerScript();</script></body></html>";
 Document parsed = Parser.parseBodyFragment(html, "");
 List<Node> scripts = parsed.body().children();
 assertEquals(2, scripts.size());
 assertEquals("Outer script", scripts.get(0).text());
 List<Node> innerScriptNodes = scripts.get(0).child(0).children();
 assertEquals(1, innerScriptNodes.size());
 assertEquals("Inner script", innerScriptNodes.get(0).text());
 }

 @Test
 public void testParseFragmentWithStyleTagWithAttributes() {
 String html = "<html><head><title>Test</title><style type=\"text/css\" media=\"screen\" charset=\"UTF-8\">body { color: red; background-color: blue; }</style></head><body></body></html>";
 Document parsed = Parser.parseBodyFragment(html, "");
 assertEquals("UTF-8", parsed.styleSheetCharset());
 List<Node> styles = parsed.head().children();
 assertEquals(1, styles.size());
 assertEquals("screen", styles.get(0).attribute("media").value());
 }
}