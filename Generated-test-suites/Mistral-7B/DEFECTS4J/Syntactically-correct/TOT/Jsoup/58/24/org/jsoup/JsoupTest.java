import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class JsoupTest {

 @Test
 public void testParseString() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 Document doc = Jsoup.parse(html);
 assertNotNull(doc);
 assertNotNull(doc.title());
 assertNotNull(doc.head());
 assertNotNull(doc.body());
 assertNotNull(doc.getElementsByTag("h1"));
 }

 @Test
 public void testParseStringWithBaseUri() {
 String html = "<img src=\"image.jpg\" alt=\"Image\">";
 String baseUri = "http://test.com";
 Document doc = Jsoup.parse(html, baseUri);
 String imageUrl = doc.select("img[src]").first().attr("src");
 assertNotNull(imageUrl);
 assertNotNull(new URL(baseUri, imageUrl));
 }

 @Test
 public void testParseStringWithCustomParser() {
 String html = "<xml><root/></xml>";
 Parser parser = org.jsoup.parser.XmlParser.xmlParser();
 Document doc = Jsoup.parse(html, "", parser);
 assertNotNull(doc);
 assertNotNull(doc.rootElement());
 }

 @Test
 public void testParseFile() throws IOException {
 File inputFile = new File("input.html");
 Document doc = Jsoup.parse(inputFile, "UTF-8");
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testParseFileWithInvalidCharset() throws IOException {
 File inputFile = new File("input.html");
 Document doc = Jsoup.parse(inputFile, "invalid-charset");
 }

 @Test(expected = IOException.class)
 public void testConnectInvalidUrl() {
 assertThrows(IOException.class, () -> Jsoup.connect("jsoup://invalid"));
 }

 @Test
 public void testParseInputStream() throws IOException {
 InputStream inputStream = this.getClass().getResourceAsStream("/input.html");
 Document doc = Jsoup.parse(inputStream, "UTF-8");
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testParseInputStreamWithInvalidCharset() throws IOException {
 InputStream inputStream = this.getClass().getResourceAsStream("/input.html");
 Document doc = Jsoup.parse(inputStream, "invalid-charset");
 }

 @Test
 public void testClean() {
 String html = "<html><head><title>Test Page</title><script src=\"malicious.js\"></script></head><body><h1 id=\"attack\">XSS<script>alert('Hello World')</script></h1></body></html>";
 Whitelist whitelist = Whitelist.relaxed();
 String cleanHtml = Jsoup.clean(html, "", whitelist);
 assertNotNull(cleanHtml);
 assertFalse(cleanHtml.contains("<script>"));
 }

 @Test
 public void testCleanWithBaseUri() {
 String html = "<img src=\"image.jpg\" alt=\"Image\">";
 String baseUri = "http://test.com";
 String cleanHtml = Jsoup.clean(html, baseUri, Whitelist.relaxed());
 assertNotNull(cleanHtml);
 assertFalse(cleanHtml.contains("src"));
 }

 @Test
 public void testCleanWithOutputSettings() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello &amp; World</h1></body></html>";
 Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
 String cleanHtml = Jsoup.clean(html, "", Whitelist.simpleText());
 assertNotNull(cleanHtml);
 assertEquals("<html><head><title>Test Page</title></head><body><h1>Hello &amp; World</h1></body></html>", cleanHtml);
 }

}