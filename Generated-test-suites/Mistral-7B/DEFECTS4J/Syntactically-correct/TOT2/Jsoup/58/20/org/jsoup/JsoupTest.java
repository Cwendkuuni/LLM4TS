import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsoupTest {

 // Test case for parse method with String input and default baseUri
 @Test
 public void testParseStringWithDefaultBaseUri() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 Document doc = Jsoup.parse(html);
 assertNotNull(doc);
 assertEquals("Hello World", doc.title());
 }

 // Test case for parse method with String input, custom baseUri, and default parser
 @Test
 public void testParseStringWithCustomBaseUri() {
 String html = "<html><body><h1>Hello World</h1><a href='/links/link1.html'>Link 1</a></body></html>";
 String baseUri = "http://example.com";
 Document doc = Jsoup.parse(html, baseUri);
 assertNotNull(doc);
 assertEquals("Hello World", doc.title());
 assertEquals("http://example.com/links/link1.html", doc.select("a[href]").first().attr("abs:href"));
 }

 // Test case for parse method with String input, custom baseUri, and custom parser
 @Test
 public void testParseStringWithCustomBaseUriAndParser() {
 String html = "<html><body><h1>Hello World</h1><a href='/links/link1.html'>Link 1</a></body></html>";
 String baseUri = "http://example.com";
 Parser parser = Parser.xmlParser();
 Document doc = Jsoup.parse(html, baseUri, parser);
 assertNotNull(doc);
 assertEquals("Hello World", doc.title());
 assertEquals("http://example.com/links/link1.html", doc.select("a[href]").first().attr("abs:href"));
 }

 // Test case for parse method with File input, default charsetName and baseUri
 @Test
 public void testParseFileWithDefaultCharsetNameAndBaseUri() throws Exception {
 File inputFile = new File("path/to/input/file.html");
 Document doc = Jsoup.parse(inputFile);
 assertNotNull(doc);
 }

 // Test case for parse method with File input, custom charsetName and baseUri
 @Test
 public void testParseFileWithCustomCharsetNameAndBaseUri() throws Exception {
 File inputFile = new File("path/to/input/file.html");
 Document doc = Jsoup.parse(inputFile, "UTF-16", "");
 assertNotNull(doc);
 }

 // Test case for parse method with InputStream input, default charsetName and baseUri
 @Test
 public void testParseInputStreamWithDefaultCharsetNameAndBaseUri() throws Exception {
 InputStream inputStream = getClass().getResourceAsStream("/path/to/input/file.html");
 Document doc = Jsoup.parse(inputStream);
 assertNotNull(doc);
 }

 // Test case for parse method with InputStream input, custom charsetName and baseUri
 @Test
 public void testParseInputStreamWithCustomCharsetNameAndBaseUri() throws Exception {
 InputStream inputStream = getClass().getResourceAsStream("/path/to/input/file.html");
 Document doc = Jsoup.parse(inputStream, "UTF-16", "");
 assertNotNull(doc);
 }

 // Test case for parse method with InputStream input, custom charsetName, baseUri and parser
 @Test
 public void testParseInputStreamWithCustomCharsetNameBaseUriAndParser() throws Exception {
 InputStream inputStream = getClass().getResourceAsStream("/path/to/input/file.html");
 Parser parser = Parser.xmlParser();
 Document doc = Jsoup.parse(inputStream, "UTF-16", "", parser);
 assertNotNull(doc);
 }

 // Test case for parseBodyFragment method with String input and default baseUri
 @Test
 public void testParseBodyFragmentWithDefaultBaseUri() {
 String bodyHtml = "<h1>Hello World</h1>";
 Document doc = Jsoup.parseBodyFragment(bodyHtml);
 assertNotNull(doc);
 assertEquals("Hello World", doc.title());
 }

 // Test case for parseBodyFragment method with String input and custom baseUri
 @Test
 public void testParseBodyFragmentWithCustomBaseUri() {
 String bodyHtml = "<h1>Hello World</h1><a href='/links/link1.html'>Link 1</a>";
 String baseUri = "http://example.com";
 Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
 assertNotNull(doc);
 assertEquals("Hello World", doc.title());
 assertEquals("http://example.com/links/link1.html", doc.select("a[href]").first().attr("abs:href"));
 }

 // Test case for connect method
 @Test
 public void testConnect() throws Exception {
 Connection con = Jsoup.connect("http://example.com");
 Document doc = con.get();
 assertNotNull(doc);
 }

 // Test case for parse method with URL input, default timeoutMillis
 @Test
 public void testParseUrlWithDefaultTimeoutMillis() throws IOException {
 URL url = new URL("http://example.com");
 Document doc = Jsoup.parse(url);
 assertNotNull(doc);
 }

 // Test case for parse method with URL input and custom timeoutMillis
 @Test
 public void testParseUrlWithCustomTimeoutMillis() throws IOException {
 URL url = new URL("http://example.com");
 Document doc = Jsoup.parse(url, 5000);
 assertNotNull(doc);
 }

 // Test case for clean method with String input, default baseUri, and default whitelist
 @Test
 public void testCleanWithDefaultBaseUriAndWhitelist() {
 String bodyHtml = "<h1>Hello World<script>alert('Script');</script></h1>";
 String cleanedHtml = Jsoup.clean(bodyHtml);
 assertNotNull(cleanedHtml);
 assertEquals("<h1>Hello World</h1>", cleanedHtml);
 }

 // Test case for clean method with String input, custom baseUri, and custom whitelist
 @Test
 public void testCleanWithCustomBaseUriAndWhitelist() {
 String bodyHtml = "<h1>Hello World<script>alert('Script');</script></h1>";
 String baseUri = "http://example.com";
 Whitelist whitelist = Whitelist.simpleText();
 String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
 assertNotNull(cleanedHtml);
 assertEquals("<h1>", cleanedHtml);
 }

 // Test case for clean method with String input, custom baseUri, custom whitelist, and outputSettings
 @Test
 public void testCleanWithCustomBaseUriWhitelistAndOutputSettings() {
 String bodyHtml = "<h1>Hello World<script>alert('Script');</script></h1>";
 String baseUri = "http://example.com";
 Whitelist whitelist = Whitelist.simpleText();
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.prettyPrint();
 String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
 assertNotNull(cleanedHtml);
 assertEquals("<h1>", cleanedHtml);
 }

 // Test case for isValid method with String input and custom whitelist
 @Test
 public void testIsValidWithStringInputAndCustomWhitelist() {
 String bodyHtml = "<h1>Hello World<script>alert('Script');</script></h1>";
 Whitelist whitelist = Whitelist.simpleText();
 boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
 assertFalse(isValid);
 }

 // Test case for isValid method with String input and default whitelist
 @Test
 public void testIsValidWithStringInputAndDefaultWhitelist() {
 String bodyHtml = "<h1>Hello World<script>alert('Script');</script></h1>";
 boolean isValid = Jsoup.isValid(bodyHtml);
 assertFalse(isValid);
 }
}