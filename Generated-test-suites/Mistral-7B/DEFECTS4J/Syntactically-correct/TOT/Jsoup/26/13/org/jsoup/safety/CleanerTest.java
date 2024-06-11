import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class CleanerTest {

 private static final String HTML_TAG = "html";
 private static final String BODY_TAG = "body";
 private static final String DIV_TAG = "div";
 private static final String P_TAG = "p";
 private static final String SPAN_TAG = "span";

 @Test
 public void testCleaner_ValidHTML() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);

 String validHtml = "<html><body><div><p>Hello, world!</p></div></body></html>";
 Document dirtyDocument = Jsoup.parse(validHtml);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals(validHtml, cleanDocument.html());
 }

 @Test
 public void testCleaner_InvalidHTML() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);

 String invalidHtml = "<html><script>alert('XSS attack!');</script><body><div><p>Hello, world!</p></div></body></html>";
 Document dirtyDocument = Jsoup.parse(invalidHtml);

 assertFalse(cleaner.isValid(dirtyDocument));
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNull(cleanDocument.select("script").first());
 }

 @Test
 public void testCleaner_EmptyHTML() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);

 String emptyHtml = "<html></html>";
 Document dirtyDocument = Jsoup.parse(emptyHtml);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals(emptyHtml, cleanDocument.html());
 }

 @Test(expected = NullPointerException.class)
 public void testCleaner_NullDocument() {
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);

 Document dirtyDocument = null;
 cleaner.clean(dirtyDocument);
 }

 @Test(expected = NullPointerException.class)
 public void testCleaner_NullWhitelist() {
 Cleaner cleaner = new Cleaner(null);
 }
}