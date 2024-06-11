import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class ParserTest {

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
 String html = "<html><head><title>Test</title></head><body>" +
 " <h1>Hello World</h1>" +
 " <p>This is a <b>test</b>.</p>" +
 " <img src=\"image.jpg\" alt=\"Image\">" +
 " <script src=\"script.js\"></script>" +
 "</body></html>";
 parser = new Parser(html, "http://localhost", false);
 doc = parser.parse();
 }

 @Test
 public void testParseNormalHtml() {
 // Test the root element
 assertEquals(htmlTag, doc.rootElement().tag());
 // Test the head element
 Element head = doc.head();
 assertEquals(headTag, head.tag());
 // Test the title element
 assertEquals("Test", head.child(0).text());
 // Test the body element
 Element body = doc.body();
 // Test the h1 element
 assertEquals("Hello World", body.child(0).text());
 // Test the p element
 assertEquals("This is a test.", body.child(1).child(0).text());
 // Test the bold text
 assertEquals("test", body.child(1).child(1).tagName());
 // Test the image element
 assertTrue(body.child(1).child(2) instanceof Element);
 Element image = body.child(1).child(2);
 assertEquals("image.jpg", image.absUrl("src"));
 // Test the script element
 assertTrue(body.child(1).child(3) instanceof Element);
 Element script = body.child(1).child(3);
 assertEquals("script.js", script.absUrl("src"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullHtml() {
 parser = new Parser(null, "http://localhost", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyHtml() {
 parser = new Parser("", "http://localhost", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidHtml() {
 String invalidHtml = "<html><head><title>Test</title></head><body>" +
 " <h1>Hello World</h1>" +
 " </p>" + // missing closing tag
 "</body></html>";
 parser = new Parser(invalidHtml, "http://localhost", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidHtml2() {
 String invalidHtml = "<html><head><title>Test</title></head><body>" +
 " <h1>Hello World</h1>" +
 " <p><!-- this is a comment -->" +
 " </body></html>";
 parser = new Parser(invalidHtml, "http://localhost", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidHtml3() {
 String invalidHtml = "<html><head><title>Test</title></head><body>" +
 " <h1>Hello World</h1>" +
 " <script><!-- this is a script --></script>" +
 "</body></html>";
 parser = new Parser(invalidHtml, "http://localhost", false);
 }
}