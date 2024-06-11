import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JsoupTest {

 @Test
 public void testParseWithBaseUri() {
 String html = "<html><head><title>Test</title></head><body><h1 id=\"title\">Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 Document doc = Jsoup.parse(html, baseUri);
 assertNotNull(doc);
 assertEquals("Test", doc.title());
 assertEquals("Hello World", doc.select("#title").text());
 }

 @Test
 public void testParseWithParser() {
 Parser parser = new Parser.MalformedHtmlParser();
 String html = "<html><head><title>Test</title></head><body><h1 id=\"title\">Hello World</h1></body></html>";
 Document doc = Jsoup.parse(html, "", parser);
 assertNotNull(doc);
 assertEquals("Test", doc.title());
 assertEquals("Hello World", doc.select("#title").text());
 }

 @Test
 public void testParseWithoutBaseUri() {
 String html = "<html><head><title>Test</title></head><body><h1 id=\"title\">Hello World</h1></body></html>";
 Document doc = Jsoup.parse(html);
 assertNotNull(doc);
 assertEquals("Untitled Document", doc.title());
 assertEquals("Hello World", doc.select("#title").text());
 }

 @Test
 public void testConnect() {
 String url = "https://example.com";
 Document doc = Jsoup.connect(url).get();
 assertNotNull(doc);
 assertEquals(url, doc.baseUri());
 }

 @Test
 public void testParseFile() throws IOException {
 String filePath = "test.html";
 File file = new File(filePath);
 Document doc = Jsoup.parse(file, "UTF-8");
 assertNotNull(doc);
 assertEquals("Test", doc.title());
 }

 @Test
 public void testParseInputStream() throws IOException {
 String filePath = "test.html";
 File file = new File(filePath);
 InputStream inputStream = new FileInputStream(file);
 Document doc = Jsoup.parse(inputStream, "UTF-8");
 assertNotNull(doc);
 assertEquals("Test", doc.title());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<h1>Hello World</h1>";
 Document doc = Jsoup.parseBodyFragment(bodyHtml);
 assertNotNull(doc);
 assertEquals("Hello World", doc.body().html());
 }

 @Test
 public void testParseAndClean() {
 String bodyHtml = "<script>alert('XSS');</script><h1>Hello World</h1>";
 String baseUri = "http://example.com";
 Whitelist whitelist = Whitelist.simpleTagNames("h1");
 Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
 doc = Jsoup.clean(doc.body().html(), whitelist);
 assertEquals("Hello World", doc.body().html());
 assertNull(doc.body().select("script").first());
 }

 @Test
 public void testCleanInvalidHtml() {
 String bodyHtml = "<script>alert('XSS');</script>";
 Whitelist whitelist = Whitelist.simpleTagNames("h1");
 Document doc = Jsoup.parseBodyFragment(bodyHtml, "");
 doc = Jsoup.clean(doc.body().html(), whitelist);
 assertNull(doc.body());
 }

 @Test
 public void testIsValid() {
 String bodyHtml = "<script>alert('XSS');</script><h1>Hello World</h1>";
 Whitelist whitelist = Whitelist.simpleTagNames("h1");
 boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
 assertFalse(isValid);
 }

 @Test
 public void testIsValidWithCleanedHtml() {
 String bodyHtml = "<script>alert('XSS');</script><h1>Hello World</h1>";
 Whitelist whitelist = Whitelist.simpleTagNames("h1");
 Document doc = Jsoup.parseBodyFragment(bodyHtml, "");
 doc = Jsoup.clean(doc.body().html(), whitelist);
 String cleanedHtml = doc.body().html();
 boolean isValid = Jsoup.isValid(cleanedHtml, whitelist);
 assertTrue(isValid);
 }
}