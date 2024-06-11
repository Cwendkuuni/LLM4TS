import static org.junit.Assert.assertNotNull;
import static org.jsoup.Jsoup.*;

import org.junit.Test;

public class JsoupTest {

 @Test
 public void testParse() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parse(html);
 assertNotNull(doc);
 }

 @Test
 public void testParseWithBaseUri() {
 String html = "<html><head><base href=\"/test\"></base></html><body><a href=\"about.html\"></a></body></html>";
 String baseUri = "http://example.com/test";
 Document doc = parse(html, baseUri);
 String expected = "<html><head><base href=\"/test\"></base></html><body><a href=\"http://example.com/test/about.html\"></a></body></html>";
 String actual = doc.html();
 assertNotNull(doc);
 assertNotNull(actual);
 assertNotNull(expected);
 assertTrue(actual.equals(expected));
 }

 @Test
 public void testParseWithParser() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = parse(html, new Parser.MalformedHtmlParser());
 assertNotNull(doc);
 }

 @Test
 public void testParseFromFile() throws IOException {
 File file = new File("test.html");
 Document doc = parse(file, "UTF-8");
 assertNotNull(doc);
 }

 @Test
 public void testParseFromInputStream() throws IOException {
 InputStream stream = Jsoup.class.getResourceAsStream("/test.html");
 Document doc = parse(stream, "UTF-8");
 assertNotNull(doc);
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<p>Hello World</p>";
 Document doc = parseBodyFragment(bodyHtml);
 assertNotNull(doc);
 }

 @Test
 public void testConnect() {
 String url = "http://example.com";
 Connection connection = connect(url);
 assertNotNull(connection);
 }

 @Test
 public void testParseWithBaseUriFromFile() throws IOException {
 File file = new File("test.html");
 Document doc = parse(file, "UTF-8", "http://example.com");
 assertNotNull(doc);
 }

 @Test
 public void testParseWithBaseUriFromInputStream() throws IOException {
 InputStream stream = Jsoup.class.getResourceAsStream("/test.html");
 Document doc = parse(stream, "UTF-8", "http://example.com");
 assertNotNull(doc);
 }

 @Test
 public void testClean() {
 String bodyHtml = "<html><body><script>alert('XSS')</script><p>Hello World</p></body></html>";
 Whitelist whitelist = Whitelist.simpleTagNames("p");
 String expected = "<p>Hello World</p>";
 String actual = clean(bodyHtml, "", whitelist);
 assertNotNull(actual);
 assertNotNull(expected);
 assertTrue(actual.equals(expected));
 }

 @Test
 public void testIsValid() {
 String bodyHtml = "<html><body><script>alert('XSS')</script><p>Hello World</p></body></html>";
 Whitelist whitelist = Whitelist.simpleTagNames("p");
 boolean actual = isValid(bodyHtml, whitelist);
 assertFalse(actual);
 }

 @Test
 public void testCleanWithOutputSettings() {
 String bodyHtml = "<html><body><script>alert('XSS')</script><p>Hello World</p></body></html>";
 Whitelist whitelist = Whitelist.simpleTagNames("p");
 Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true).escapeMode(Document.EscapeMode.html);
 String expected = "<p>Hello World</p>";
 String actual = clean(bodyHtml, "", whitelist, outputSettings);
 assertNotNull(actual);
 assertNotNull(expected);
 assertTrue(actual.equals(expected));
 }
}