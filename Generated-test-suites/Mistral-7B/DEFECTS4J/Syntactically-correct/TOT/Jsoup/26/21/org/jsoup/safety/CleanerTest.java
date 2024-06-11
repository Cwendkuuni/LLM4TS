import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setup() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World!</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><p></p></body></html>", cleanDocument.body().html());
 }

 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS attack!');</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body></body></html>", cleanDocument.body().html());
 }

 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World!</p></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS attack!');</script></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodesValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p><i>Hello World!</i></p></body></html>");
 Element dest = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);
 assertEquals(0, numDiscarded);
 assertEquals("<div><i></i></div>", dest.html());
 }

 @Test
 public void testCopySafeNodesInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS attack!');</script></body></html>");
 Element dest = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);
 assertEquals(1, numDiscarded);
 assertNull(dest.html());
 }
}