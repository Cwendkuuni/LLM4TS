import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

 private static final String TEST_HTML = "<html><body><h1 id='title' class='title' onclick='alert(\"XSS\")'>Title</h1>" +
         "<p id='text'><script>alert('Script injection');</script></p></body></html>";

 @Test
 public void testClean_ValidHtml() {
 Whitelist simpleWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(simpleWhitelist);

 Document dirtyDocument = Jsoup.parse(TEST_HTML);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assert.assertNotNull(cleanDocument);
 Assert.assertEquals(cleanDocument.body().text(), "Title\n");
 }

 @Test
 public void testClean_InvalidHtml() {
 Whitelist simpleWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(simpleWhitelist);

 Document dirtyDocument = Jsoup.parse(TEST_HTML);
 boolean isValid = cleaner.isValid(dirtyDocument);

 Assert.assertFalse(isValid);
 }

 @Test
 public void testClean_EmptyHtml() {
 Whitelist simpleWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(simpleWhitelist);

 Document dirtyDocument = Jsoup.parse("<html></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assert.assertNotNull(cleanDocument);
 Assert.assertEquals(cleanDocument.body().text(), "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClean_NullHtml() {
 Whitelist simpleWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(simpleWhitelist);

 Document dirtyDocument = null;
 cleaner.clean(dirtyDocument); // should throw an IllegalArgumentException
 }

 @Test
 public void testClean_FramesetHtml() {
 Whitelist simpleWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(simpleWhitelist);

 Document dirtyDocument = Jsoup.parse("<html><frameset></frameset></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assert.assertNotNull(cleanDocument);
 Assert.assertEquals(cleanDocument.body().text(), "");
 }

 @Test
 public void testClean_IncompleteHtml() {
 Whitelist simpleWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(simpleWhitelist);

 Document dirtyDocument = Jsoup.parse("<html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assert.assertNotNull(cleanDocument);
 Assert.assertEquals(cleanDocument.body().text(), "");
 }

 @Test
 public void testClean_MalformedHtml() {
 Whitelist simpleWhitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(simpleWhitelist);

 Document dirtyDocument = Jsoup.parse("<html><body> -->");
 ParseErrorList parseErrors = dirtyDocument.parseErrors();

 Assert.assertNotNull(parseErrors);
 Assert.assertEquals(parseErrors.size(), 1);
 Assert.assertEquals(parseErrors.toString(), "Expecting '>', but '>' not found: line 2, column 6");

 Document cleanDocument = cleaner.clean(dirtyDocument);
 Assert.assertNull(cleanDocument);
 }
}