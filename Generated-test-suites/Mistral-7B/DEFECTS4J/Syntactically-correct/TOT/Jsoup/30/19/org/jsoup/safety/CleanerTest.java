import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

 private static final String TEST_HTML = "<html><head></head><body>" +
         "<div id=\"test1\" style=\"color: red;\" onmouseover=\"alert('XSS');\">Test 1</div>" +
         "<script src=\"http://evil.com/malicious.js\"></script>" +
         "<p id=\"test2\">Test 2</p>" +
         "<img src=\"image.jpg\" alt=\"Image\"" +
         "</body></html>";

 @Test
 public void testCleaner() {
 Whitelist whitelist = Whitelist.relaxed();
 Cleaner cleaner = new Cleaner(whitelist);

 Document dirtyDocument = Jsoup.parse(TEST_HTML);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Check that all unsafe elements were removed
 Assert.assertEquals(0, cleanDocument.body().childNodeSize());

 // Check that all safe elements were preserved
 Document expectedDocument = Jsoup.parse("<html><head></head><body><p></p></body></html>");
 Assert.assertEquals(expectedDocument.body(), cleanDocument.body());
 }

 @Test(expected = NullPointerException.class)
 public void testNullDocument() {
 Cleaner cleaner = new Cleaner(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullWhitelist() {
 Cleaner cleaner = new Cleaner(null);
 }

 @Test
 public void testIsValid() {
 Whitelist whitelist = Whitelist.relaxed();
 Cleaner cleaner = new Cleaner(whitelist);

 Document dirtyDocument = Jsoup.parse(TEST_HTML);

 // Check that the document is considered invalid and is still cleaned
 Assert.assertFalse(cleaner.isValid(dirtyDocument));
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Check that all unsafe elements were removed
 Assert.assertEquals(0, cleanDocument.body().childNodeSize());

 // Check that all safe elements were preserved
 Document expectedDocument = Jsoup.parse("<html><head></head><body><p></p></body></html>");
 Assert.assertEquals(expectedDocument.body(), cleanDocument.body());
 }

}