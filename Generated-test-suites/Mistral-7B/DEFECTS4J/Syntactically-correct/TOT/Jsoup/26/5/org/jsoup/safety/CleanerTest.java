import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Whitelist;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Cleaner.ElementMeta;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

 private static final String TEST_HTML = "<html><body>\n" +
         "<h1 id='title' class='title' onclick='alert(\"XSS\")'>Title<script>alert('XSS');</script></h1>\n" +
         "<p>Safe paragraph with <b>bold</b> text.</p>\n" +
         "</body></html>";

 @Test
 public void testCleaner_ValidDocument() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);

 Document dirtyDocument = Jsoup.parse(TEST_HTML);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Check if all tags and attributes are removed
 Assert.assertEquals("", cleanDocument.body().html());
 }

 @Test
 public void testCleaner_SafeElements() {
 Whitelist whitelist = Whitelist.simpleText().addTags("h1", "p", "b");
 Cleaner cleaner = new Cleaner(whitelist);

 Document dirtyDocument = Jsoup.parse(TEST_HTML);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Check if safe elements and attributes are preserved
 Element title = cleanDocument.selectFirst("h1");
 Assert.assertNotNull(title);
 Assert.assertEquals("title", title.tagName());
 Assert.assertEquals("title", title.id());
 Element paragraph = cleanDocument.selectFirst("p");
 Assert.assertNotNull(paragraph);
 Assert.assertEquals("p", paragraph.tagName());
 Element bold = paragraph.selectFirst("b");
 Assert.assertNotNull(bold);
 Assert.assertEquals("b", bold.tagName());
 }

 @Test
 public void testCleaner_InvalidDocument() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);

 Document dirtyDocument = Jsoup.parse(TEST_HTML);
 boolean isValid = cleaner.isValid(dirtyDocument);

 // Check if invalid document is marked as invalid
 Assert.assertFalse(isValid);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleaner_NullWhitelist() {
 // Test the constructor with a null whitelist
 Cleaner cleaner = new Cleaner(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleaner_NullDocument() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);

 // Test the clean method with a null document
 cleaner.clean(null);
 }
}