import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParserTest {

 @Test
 public void testParseSimpleHtml() {
 String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Parser.parse(html, "http://test.com");
 Element htmlTag = doc.head();
 assertEquals(htmlTag, doc.getElementsByTag("html").first());
 Element titleTag = doc.title();
 assertEquals("Test", titleTag.text());
 Element bodyTag = doc.body();
 Element h1Tag = bodyTag.getElementsByTag("h1").first();
 assertEquals("Hello World", h1Tag.text());
 }

 @Test
 public void testParseEmptyHtml() {
 String html = "<html></html>";
 Document doc = Parser.parse(html, "http://test.com");
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertEquals(0, doc.numberOfChildren());
 }

 @Test
 public void testParseInvalidHtml() {
 String html = "<html><!-- This is a comment -->";
 Document doc = Parser.parse(html, "http://test.com");
 assertNotNull(doc);
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 }

 @Test(expected = RuntimeException.class)
 public void testParseMissingTag() {
 String html = "<html></html>";
 Document doc = Parser.parse(html, "http://test.com");
 doc.getElementsByTag("nonexistent").first();
 }

 @Test
 public void testParseBodyFragment() {
 String html = "<body><h1>Hello</h1></body>";
 Document doc = Parser.parseBodyFragment(html, "http://test.com");
 Element h1Tag = doc.getElementsByTag("h1").first();
 assertEquals("Hello", h1Tag.text());
 }

 @Test
 public void testParseBodyFragmentWithRelaxedParsing() {
 String html = "<body><div></div><div></div>";
 Document doc = Parser.parseBodyFragmentRelaxed(html, "http://test.com");
 Element div1Tag = doc.getElementsByTag("div").get(0);
 Element div2Tag = doc.getElementsByTag("div").get(1);
 assertEquals(2, doc.numberOfChildren());
 assertEquals(0, div1Tag.numberOfChildren());
 assertEquals(0, div2Tag.numberOfChildren());
 }

 @Test
 public void testParseXmlDeclaration() {
 String html = "<xml version='1.0' encoding='UTF-8' standalone='yes' >";
 Document doc = Parser.parse(html, "http://test.com");
 XmlDeclaration xmlDeclaration = doc.head().getElementsByTag("xml").first();
 assertEquals("UTF-8", xmlDeclaration.encoding());
 }

 @Test
 public void testParseComment() {
 String html = "<html><!-- This is a comment --></html>";
 Document doc = Parser.parse(html, "http://test.com");
 Element comment = doc.head().getElementsByTag("comment").first();
 assertNotNull(comment);
 assertNull(comment.text());
 }

 @Test
 public void testParseEndTag() {
 String html = "<html><body><div></div></body></html>";
 Document doc = Parser.parse(html, "http://test.com");
 Element divTag = doc.body().getElementsByTag("div").first();
 assertEquals(0, divTag.numberOfChildren());
 }

 @Test
 public void testParseStartTagWithEmptyAttributes() {
 String html = "<img src='image.png' alt='' />";
 Document doc = Parser.parse(html, "http://test.com");
 Element imgTag = doc.getElementsByTag("img").first();
 assertNotNull(imgTag);
 assertNotNull(imgTag.attributes());
 assertEquals(0, imgTag.attributes().size());
 }

 @Test
 public void testParseStartTagWithAttributes() {
 String html = "<a href='http://test.com' title='Test'>Link</a>";
 Document doc = Parser.parse(html, "http://test.com");
 Element aTag = doc.body().getElementsByTag("a").first();
 assertEquals("http://test.com", aTag.attr("href"));
 assertEquals("Test", aTag.attr("title"));
 }

 @Test
 public void testParseStartTagWithSelfClosingTag() {
 String html = "<img src='image.png' alt='' />";
 Document doc = Parser.parse(html, "http://test.com");
 Element imgTag = doc.getElementsByTag("img").first();
 assertEquals(true, imgTag.isSelfClosing());
 }

 @Test
 public void testParseTextNode() {
 String html = "<p>This is a text node.</p>";
 Document doc = Parser.parse(html, "http://test.com");
 Element pTag = doc.body().getElementsByTag("p").first();
 assertEquals("This is a text node.", pTag.text());
 }

 @Test
 public void testParseCdata() {
 String html = "<html><![CDATA[ <script type='text/javascript'>function test() { alert('Hello World!'); }]]></html>";
 Document doc = Parser.parse(html, "http://test.com");
 Element scriptTag = doc.getElementsByTag("script").first();
 assertEquals(" <script type='text/javascript'>function test() { alert('Hello World!'); }", scriptTag.html());
 }
}