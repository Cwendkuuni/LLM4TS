import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {

 private static final String SAMPLE_HTML = "<html><head><title>Test Page</title></head><body><h1>Hello, World!</h1></body></html>";

 @Before
 public void setUp() {
 // Set up a default whitelist for testing
 Whitelist whitelist = Whitelist.simpleText();
 whitelist.addTags("h1");
 }

 @Test
 public void testParseHtmlString() {
 // Test parsing HTML from a string
 Document document = Jsoup.parse(SAMPLE_HTML);
 assertNotNull(document);
 assertEquals("Test Page", document.title());
 assertEquals("Hello, World!", document.select("h1").text());
 }

 @Test
 public void testParseHtmlStringWithBaseUri() {
 // Test parsing HTML from a string with a base URI
 String baseUri = "http://example.com";
 Document document = Jsoup.parse(SAMPLE_HTML, baseUri);
 assertEquals(baseUri, document.baseUri());
 }

 @Test
 public void testParseHtmlStringWithParser() {
 // Test parsing HTML from a string with a custom parser
 Parser parser = Parser.xmlParser();
 Document document = Jsoup.parse(SAMPLE_HTML, "", parser);
 assertNotNull(document);
 }

 @Test
 public void testConnectUrl() {
 // Test connecting to a URL and parsing the HTML
 String url = "http://example.com";
 Document document = Jsoup.connect(url).get();
 assertNotNull(document);
 }

 @Test
 public void testParseFile() throws IOException {
 // Test parsing HTML from a file
 String fileName = "test.html";
 File file = new File(fileName);
 Document document = Jsoup.parse(file, "UTF-8");
 assertNotNull(document);
 }

 @Test
 public void testParseInputStream() throws IOException {
 // Test parsing HTML from an input stream
 InputStream inputStream = getClass().getResourceAsStream("/test.html");
 Document document = Jsoup.parse(inputStream, "UTF-8");
 assertNotNull(document);
 }

 @Test
 public void testParseBodyFragment() {
 // Test parsing a body fragment
 String bodyHtml = "<h1>Hello, World!</h1>";
 Document document = Jsoup.parseBodyFragment(bodyHtml);
 assertEquals("Hello, World!", document.select("h1").text());
 }

 @Test
 public void testCleanHtml() {
 // Test cleaning HTML with a default whitelist
 String bodyHtml = "<html><head><title>Test Page</title><script>alert('XSS!')</script></head><body><h1 id='title'>Hello, World!</h1></body></html>";
 String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
 assertFalse(cleanHtml.contains("<script>"));
 assertEquals("Hello, World!", cleanHtml.substring(cleanHtml.indexOf("title"), cleanHtml.lastIndexOf("title") + 7));
 // Test cleaning HTML with a custom whitelist
 Whitelist customWhitelist = Whitelist.simpleText();
 customWhitelist.addTags("h1", "script");
 String cleanHtmlWithScript = Jsoup.clean(bodyHtml, customWhitelist);
 assertTrue(cleanHtmlWithScript.contains("<script>"));
 }

 @Test
 public void testIsValidHtml() {
 // Test validating HTML with a default whitelist
 String bodyHtml = "<html><head><title>Test Page</title></head><body><h1>Hello, World!</h1></body></html>";
 assertTrue(Jsoup.isValid(bodyHtml, whitelist));
 // Test validating HTML with a custom whitelist
 String invalidBodyHtml = "<html><head><title>Test Page</title><script>alert('XSS!')</script></head><body><h1>Hello, World!</h1></body></html>";
 assertFalse(Jsoup.isValid(invalidBodyHtml, whitelist));
 }
}