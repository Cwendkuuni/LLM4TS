import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JsoupTest {

 @Test
 public void testParseHtmlString() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
 Document document = Jsoup.parse(html);

 assertEquals("Test Page", document.title());
 Element h1 = document.select("h1").first();
 assertEquals("Hello World", h1.text());
 }

 @Test
 public void testParseHtmlStringWithBaseUri() {
 String html = "<img src=\"image.jpg\" alt=\"Image\">";
 String baseUri = "http://example.com";
 Document document = Jsoup.parse(html, baseUri);
 String imgSrc = document.select("img[src]").first().attr("src");
 assertEquals("http://example.com/image.jpg", imgSrc);
 }

 @Test
 public void testParseHtmlStringWithCustomParser() {
 String html = "<xml><data>Test</data></xml>";
 Parser parser = org.jsoup.parser.XmlParser.xmlParser();
 Document document = Jsoup.parse(html, "", parser);
 Element data = document.select("data").first();
 assertEquals("Test", data.text());
 }

 @Test
 public void testParseHtmlFile() throws IOException {
 File inputFile = new File("input.html");
 Document document = Jsoup.parse(inputFile, "UTF-8");
 assertNotNull(document);
 }

 @Test(expected = IOException.class)
 public void testParseHtmlFileWithInvalidCharset() throws IOException {
 File inputFile = new File("input.html");
 Document document = Jsoup.parse(inputFile, "invalidCharset");
 // Exception should be thrown
 }

 @Test
 public void testCleanHtml() {
 String html = "<html><head><title>Test Page</title><script src=\"script.js\"></script></head><body><h1>Hello World<script>alert('XSS')</script></h1></body></html>";
 Whitelist whitelist = Whitelist.simpleText();
 String cleanHtml = Jsoup.clean(html, "", whitelist);
 assertEquals("<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>", cleanHtml);
 assertNull(cleanHtml.contains("script"));
 }

 @Test
 public void testIsValidHtml() {
 String html = "<html><head><title>Test Page</title></head><body><h1 id=\"test\" class=\"test\">Hello World</h1></body></html>";
 Whitelist whitelist = Whitelist.relaxed();
 boolean isValid = Jsoup.isValid(html, whitelist);
 assertTrue(isValid);
 }

 @Test
 public void testInvalidHtml() {
 String html = "<html><script>alert('XSS')</script>";
 Whitelist whitelist = Whitelist.relaxed();
 boolean isValid = Jsoup.isValid(html, whitelist);
 assertFalse(isValid);
 }
}