import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CleanerTest {

 private final static String TEST_HTML = "" +
         "<html><head></head>" +
         "<body>" +
         "<script>alert('XSS');</script>" +
         "<div id=\"test\" class=\"safeClass\">Test</div>" +
         "<img src=\"http://example.com\" alt=\"Image\"/>" +
         "<p>Some text <b>with bold</b> and <i>italic</i></p>" +
         "</body></html>";

 @Test
 public void testCleanerWithValidHtml() {
 Document dirtyDocument = Jsoup.parse(TEST_HTML);
 Whitelist simpleWhitelist = Whitelist.relaxed();
 Cleaner cleaner = new Cleaner(simpleWhitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals(TEST_HTML.replaceAll("<[^>]*>", ""), cleanDocument.body().html());
 }

 @Test
 public void testCleanerWithInvalidHtml() {
 Document dirtyDocument = Jsoup.parse(TEST_HTML.replaceAll("<script>", "<scr" + "ipt>"));
 Whitelist simpleWhitelist = Whitelist.relaxed();
 Cleaner cleaner = new Cleaner(simpleWhitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 @Test
 public void testCleanerWithEmptyHtml() {
 Document dirtyDocument = Jsoup.parse("<html></html>");
 Whitelist emptyWhitelist = Whitelist.empty();
 Cleaner cleaner = new Cleaner(emptyWhitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertTrue(cleanDocument.body().textIsEmpty());
 }

 @Test
 public void testCleanerWithMalformedHtml() {
 Document dirtyDocument = Jsoup.parse("<html>");
 Whitelist simpleWhitelist = Whitelist.relaxed();
 Cleaner cleaner = new Cleaner(simpleWhitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 }

 @Test
 public void testCleanerWithNullWhitelist() {
 assertThrows(NullPointerException.class, () -> new Cleaner(null));
 }

 @Test
 public void testCleanerWithNullDocument() {
 assertThrows(NullPointerException.class, () -> cleaner.clean(null));
 }

}