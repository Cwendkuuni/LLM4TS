import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setup() {
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World!</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertNotNull(cleanDocument);
 assertTrue(cleanDocument.body().text().equals("Hello World!"));
 }

 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS Attack!');</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertNotNull(cleanDocument);
 assertTrue(cleanDocument.body().text().isEmpty());
 }

 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World!</p></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS Attack!');</script></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidEmptyDocument() {
 Document dirtyDocument = Jsoup.parse(" ");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullDocument() {
 cleaner.clean(null);
 }

 @Test(expected = NullPointerException.class)
 public void testIsValidNullDocument() {
 cleaner.isValid(null);
 }
}