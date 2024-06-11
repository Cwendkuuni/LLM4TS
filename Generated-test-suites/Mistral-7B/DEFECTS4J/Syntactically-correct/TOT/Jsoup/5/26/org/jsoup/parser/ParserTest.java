import org.junit.Test;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParserTest {

 @Test
 public void testParse() {
 // Test parsing a simple HTML document
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello, World!</h1></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");

 // Check the root element is HTML and has a title and body
 assertEquals(htmlTag, doc.rootElement().tag());
 Element title = doc.head().title();
 assertEquals("Test Page", title.text());
 Element body = doc.body();
 assertEquals(1, body.childNodes().size());
 assertEquals("Hello, World!", body.child(0).text());
 }

 @Test
 public void testParseBodyFragment() {
 // Test parsing a body fragment with a script tag
 String bodyHtml = "<script src=\"script.js\"></script>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com/");

 // Check the root element is a Document with an empty head and the script tag in the body
 assertEquals(Document.class, doc.getClass());
 assertEquals(0, doc.head().childNodes().size());
 Element script = doc.body().child(0);
 assertEquals("script", script.tagName());
 assertEquals("script.js", script.absUrl("src"));
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 // Test parsing a body fragment with a self-closing img tag
 String bodyHtml = "<img src=\"image.png\" />";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com/");

 // Check the root element is a Document with an empty head and the img tag in the body
 assertEquals(Document.class, doc.getClass());
 assertEquals(0, doc.head().childNodes().size());
 Element img = doc.body().child(0);
 assertEquals("img", img.tagName());
 assertEquals("image.png", img.absUrl("src"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidHtml() {
 // Test parsing invalid HTML
 String invalidHtml = "<html><body></html>"; // missing tag name
 Document doc = Parser.parse(invalidHtml, "http://test.com/");
 assertNull(doc); // should throw an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidBaseUri() {
 // Test parsing HTML with an invalid base URI
 String html = "<html><head><title>Test Page</title></head><body><a href=\"../test\"></a></body></html>";
 Document doc = Parser.parse(html, null); // null base URI
 assertNull(doc); // should throw an IllegalArgumentException
 }

 @Test
 public void testParseXmlDecl() {
 // Test parsing an XML declaration
 String html = "<xml version=\"1.0\" encoding=\"UTF-8\">";
 Document doc = Parser.parse(html, "http://test.com/");
 Element xmlDecl = doc.head().child(0);
 assertEquals("xml", xmlDecl.tagName());
 assertEquals("1.0", xmlDecl.attr("version"));
 assertEquals("UTF-8", xmlDecl.attr("encoding"));
 }

 @Test
 public void testParseComment() {
 // Test parsing a comment
 String html = "<html><!-- This is a comment --></html>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element comment = doc.head().child(0);
 assertEquals("!--", comment.tagName().substring(0, 3));
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParseEndTag() {
 // Test parsing an end tag
 String html = "<html><body><p>Hello, World!</p></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element p = doc.body().child(0);
 assertEquals("p", p.tagName());
 assertEquals(1, p.childNodes().size());
 assertEquals("Hello, World!", p.child(0).text());
 Element pEndTag = doc.body().child(1);
 assertEquals("p", pEndTag.tagName());
 assertEquals("</p>", pEndTag.text());
 assertEquals(0, pEndTag.childNodes().size());
 }

 @Test
 public void testParseEmptyElement() {
 // Test parsing an empty element
 String html = "<html><body><img src=\"image.png\"/></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element img = doc.body().child(0);
 assertEquals("img", img.tagName());
 assertEquals("image.png", img.absUrl("src"));
 assertEquals(0, img.childNodes().size());
 }

 @Test
 public void testParseDataNode() {
 // Test parsing a data node (script tag)
 String html = "<html><body><script>console.log('Hello, World!');</script></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element script = doc.body().child(0);
 assertEquals("script", script.tagName());
 assertEquals("script", script.tagName().toLowerCase());
 String scriptText = script.text();
 assertEquals("console.log('Hello, World!');", scriptText);
 }

 @Test
 public void testParseCdata() {
 // Test parsing a CDATA section
 String html = "<html><body><![CDATA[console.log('Hello, World!');]]></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element cdata = doc.body().child(0);
 assertEquals("CDATA", cdata.tagName().substring(0, 4));
 assertEquals("console.log('Hello, World!');", cdata.text());
 }
}