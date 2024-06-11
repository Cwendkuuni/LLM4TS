import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

public class ParserTest {

 @Test
 public void testParser_parseValidHtml() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Welcome to JSoup</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertEquals("Test Page", doc.title());
 assertEquals("<html><head><title>Test Page</title></head><body><h1>Welcome to JSoup</h1></body></html>", doc.outerHtml());
 }

 @Test
 public void testParser_parseBodyFragment() {
 String bodyHtml = "<h1>Welcome to JSoup</h1>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 assertEquals("<html><head></head><body><h1>Welcome to JSoup</h1></body></html>", doc.outerHtml());
 }

 @Test
 public void testParser_parseBodyFragmentRelaxed() {
 String bodyHtml = "<div><h1>Welcome to JSoup</h1></div>"; // this tag is not explicitly closed, but it should be accepted and closed automatically
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 assertEquals("<html><head></head><body><div><h1>Welcome to JSoup</h1></div></body></html>", doc.outerHtml());
 }

 @Test(expected = NullPointerException.class)
 public void testParser_parseNullHtml() {
 Parser.parse(null, "http://example.com");
 }

 @Test(expected = NullPointerException.class)
 public void testParser_parseNullBaseUri() {
 Parser.parse("<html></html>", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParser_parseInvalidHtml() {
 String invalidHtml = "<html<title>Test Page"; // missing closing angle bracket in title tag
 Document doc = Parser.parse(invalidHtml, "http://example.com");
 fail("Expected IllegalArgumentException, but got: " + doc.outerHtml());
 }

 @Test
 public void testParser_parseComment() {
 String html = "<html><!-- This is a comment --></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element comment = doc.select("html > comment()").first();
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testParser_parseXmlDeclaration() {
 String html = "<html xmlns:foo=\"http://foo.org\" xml:lang=\"en\">";
 Document doc = Parser.parse(html, "http://example.com");
 XmlDeclaration xmlDeclaration = doc.select("html > xml-declaration()").first();
 assertEquals("http://foo.org", xmlDeclaration.xmlns("foo"));
 assertEquals("en", xmlDeclaration.language());
 }

 @Test
 public void testParser_parseEndTag() {
 String html = "<html><body><p><div></div></p></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element div = doc.select("div").first();
 Element p = div.parent();
 assertEquals("p", p.tagName());
 }

 @Test
 public void testParser_parseStartTag() {
 String html = "<html><body><p><a href=\"http://example.com\"><img src=\"image.jpg\"></a></p></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element img = doc.select("img").first();
 assertEquals("image.jpg", img.absUrl("src"));
 Element a = img.parent();
 assertEquals("a", a.tagName());
 }

 @Test
 public void testParser_parseStartTagWithAttributes() {
 String html = "<html><body><p><a href=\"http://example.com\" class=\"external\" title=\"Link to example\">Link</a></p></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 Element a = doc.select("a").first();
 assertEquals("http://example.com", a.absUrl("href"));
 assertEquals("external", a.classes().get(0));
 assertEquals("Link to example", a.attr("title"));
 }

 @Test
 public void testParser_parseTextNode() {
 String html = "<html><body><p>Welcome to JSoup</p></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 TextNode textNode = (TextNode) doc.select("p > text()").first();
 assertEquals("Welcome to JSoup", textNode.text());
 }

 @Test
 public void testParser_parseCdata() {
 String html = "<html><![CDATA[This is some CDATA]]><body></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 TextNode textNode = (TextNode) doc.select("html > text()").first();
 assertEquals("This is some CDATA", textNode.text());
 }

 @Test
 public void testParser_parseBaseTag() {
 String html = "<html><head><base href=\"/test\"></base></head><body><a href=\"/test/page\">Link</a></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 assertEquals("/test", doc.baseUri());
 }

}