import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Whitelist.AllowAllElementFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CleanerTest {

 @Test
 public void testCleanerCreation() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnlyWhitelist());
 Assertions.assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Whitelist whitelist = new Whitelist.SimpleTextWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 String expectedHtml = "<html><head></head><body><h1></h1></body></html>";
 String actualHtml = cleanDocument.html();
 Assertions.assertEquals(expectedHtml, actualHtml);
 }

 @Test
 public void testIsValidDocument() {
 String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
 Whitelist whitelist = new Whitelist.SimpleTextWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Jsoup.parse(html);

 boolean isValid = cleaner.isValid(dirtyDocument);
 Assertions.assertTrue(isValid);
 }

 @Test
 public void testInvalidDocument() {
 String html = "<html><head><script>alert('XSS')</script></head><body><h1>Hello World</h1></body></html>";
 Whitelist whitelist = new Whitelist.SimpleTextWhitelist();
 Cleaner cleaner = new Cleaner(whitelist);
 Document dirtyDocument = Jsoup.parse(html);

 boolean isValid = cleaner.isValid(dirtyDocument);
 Assertions.assertFalse(isValid);
 }

 @Test
 public void testCopySafeNodes() {
 String html = "<html><head></head><body><h1 id='title' class='title'><script>alert('XSS')</script>Hello World</h1></body></html>";
 Whitelist whitelist = new Whitelist.SimpleTextWhitelist();
 AllowAllElementFilter allowAllElementFilter = AllowAllElementFilter.INSTANCE;
 Whitelist customWhitelist = Whitelist.relaxed().userAgent("CleanerTest").addTags("h1").addAttributes("h1", "*");
 Cleaner cleaner = new Cleaner(customWhitelist);
 Document dirtyDocument = Jsoup.parse(html);
 Element dest = new Element(Tag.BODY);
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);

 String expectedHtml = "<html><head></head><body><h1 class='title'></h1></body></html>";
 String actualHtml = dest.html();
 Assertions.assertEquals(expectedHtml, actualHtml);
 Assertions.assertEquals(2, numDiscarded);
 }
}