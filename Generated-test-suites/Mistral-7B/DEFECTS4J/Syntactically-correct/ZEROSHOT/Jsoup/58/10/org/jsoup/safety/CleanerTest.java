import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

 private static final String HTML_SAMPLE = "<html><body>" +
         "<div id='safe' attr1='safeValue' attr2='anotherSafeValue'>Safe content</div>" +
         "<script>alert('XSS');</script>" +
         "<iframe src='malicious.com'></iframe>" +
         "<img src='image.jpg' alt='Image' title='Title'/>" +
         "<p>Unsafe paragraph</p>" +
         "</body></html>";

 @Test
 public void testCleaner() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);

 Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 Assert.assertFalse(cleanDocument.body().text().contains("alert('XSS')"));
 Assert.assertFalse(cleanDocument.body().text().contains("iframe src='malicious.com'"));
 Assert.assertFalse(cleanDocument.body().text().contains("Unsafe paragraph"));
 Assert.assertTrue(cleanDocument.body().text().contains("Safe content"));
 }

 @Test
 public void testIsValid() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);

 Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
 boolean isValid = cleaner.isValid(dirtyDocument);

 Assert.assertFalse(isValid);
 }

 @Test
 public void testIsValidWithSafeHtml() {
 Whitelist whitelist = Whitelist.relaxed();
 Cleaner cleaner = new Cleaner(whitelist);

 Document dirtyDocument = Jsoup.parse(HTML_SAMPLE);
 boolean isValid = cleaner.isValid(dirtyDocument);

 Assert.assertTrue(isValid);
 }
}