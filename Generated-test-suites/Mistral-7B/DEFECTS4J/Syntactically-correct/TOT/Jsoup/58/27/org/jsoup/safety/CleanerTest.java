import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.jsoup.parser.ParseError;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CleanerTest {

 private static final Whitelist ALLOWED_TAGS = Whitelist.simpleTags("p", "a");

 @Test
 public void testCleanDocument() {
 Document dirtyDocument = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello <b>World</b></p></body></html>");
 Cleaner cleaner = new Cleaner(ALLOWED_TAGS);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><head></head><body><p>Hello World</p></body></html>", cleanDocument.html());
 assertFalse(cleanDocument.getElementsByTag("head").size() > 0);
 }

 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><head><script>alert('XSS');</script></head><body><p>Hello <b>World</b></p></body></html>");
 Cleaner cleaner = new Cleaner(ALLOWED_TAGS);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><head></head><body><p>Hello World</p></body></html>", cleanDocument.html());
 assertFalse(cleanDocument.getElementsByTag("head").size() > 0);
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullDocument() {
 Cleaner cleaner = new Cleaner(ALLOWED_TAGS);
 cleaner.clean(null);
 }

 @Test
 public void testIsValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><head></head><body><p>Hello <b>World</b></p></body></html>");
 Cleaner cleaner = new Cleaner(ALLOWED_TAGS);

 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><head><script>alert('XSS');</script></head><body><p>Hello <b>World</b></p></body></html>");
 Cleaner cleaner = new Cleaner(ALLOWED_TAGS);

 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleanEmptyDocument() {
 Document dirtyDocument = Jsoup.parse("<html></html>");
 Cleaner cleaner = new Cleaner(ALLOWED_TAGS);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html></html>", cleanDocument.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateCleanerWithNullWhitelist() {
 try {
 new Cleaner(null);
 } catch (IllegalArgumentException e) {
 assertEquals("Whitelist cannot be null.", e.getMessage());
 throw e;
 }
 }
}