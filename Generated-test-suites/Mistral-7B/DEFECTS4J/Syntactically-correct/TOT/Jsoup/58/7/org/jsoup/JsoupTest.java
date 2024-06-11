import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {

 private Parser parser;
 private Document document;

 @Before
 public void setup() {
 parser = Jsoup.parser();
 }

 @Test
 public void testParseEmptyHtml() {
 String html = "";
 document = Jsoup.parse(html);
 assertEquals(0, document.childNodes().size());
 }

 @Test
 public void testParseValidHtml() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 document = Jsoup.parse(html);
 Element h1 = document.selectFirst("h1");
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseWithBaseUri() {
 String html = "<html><base href=\"/test/\"><link rel=\"stylesheet\" href=\"styles.css\"></html>";
 document = Jsoup.parse(html, "/test/");
 String linkHref = document.selectFirst("link[href]").attr("href");
 assertEquals("/test/styles.css", linkHref);
 }

 @Test
 public void testParseWithCustomParser() {
 Parser customParser = Jsoup.parser().xmlParser();
 String html = "<xml><tag>Hello World</tag></xml>";
 document = Jsoup.parse(html, "", customParser);
 Element tag = document.selectFirst("tag");
 assertEquals("Hello World", tag.text());
 }

 @Test
 public void testConnectAndParse() throws IOException {
 String url = "http://example.com";
 document = Jsoup.connect(url).get();
 Element title = document.title();
 assertEquals("Example Domain", title.text());
 }

 @Test
 public void testParseFile() throws IOException {
 File file = new File("test.html");
 document = Jsoup.parse(file, "UTF-8");
 Element h1 = document.selectFirst("h1");
 assertEquals("Test", h1.text());
 }

 @Test
 public void testParseInputStream() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 document = Jsoup.parse(inputStream, "UTF-8");
 Element h1 = document.selectFirst("h1");
 assertEquals("Test", h1.text());
 }

 @Test
 public void testCleanWithWhitelist() {
 String html = "<html><body><script>alert('Hello World')</script><h1>Test</h1></body></html>";
 Whitelist whitelist = Whitelist.relaxed();
 String cleanHtml = Jsoup.clean(html, "", whitelist);
 Document cleanDocument = parser.parse(cleanHtml);
 Element h1 = cleanDocument.selectFirst("h1");
 assertEquals("Test", h1.text());
 assertTrue(cleanHtml.contains("<h1>"));
 assertFalse(cleanHtml.contains("<script>"));
 }

 @Test
 public void testIsValidWithWhitelist() {
 String html = "<html><body><script>alert('Hello World')</script><h1>Test</h1></body></html>";
 Whitelist whitelist = Whitelist.relaxed();
 assertTrue(Jsoup.isValid(html, whitelist));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 document = Jsoup.parse("<html><base href=\"/\"><link rel=\"stylesheet\" href=\"styles.css\"></html>", null);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullHtml() {
 document = Jsoup.parse(null, "");
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullParser() {
 document = Jsoup.parse("<html></html>", "", null);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleanWithNullBodyHtml() {
 String cleanHtml = Jsoup.clean(null, "", Whitelist.relaxed());
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleanWithNullWhitelist() {
 String cleanHtml = Jsoup.clean("<html><h1>Test</h1></html>", "", null);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConnectWithNullUrl() {
 document = Jsoup.connect(null);
 fail("Expected IllegalArgumentException");
 }
}