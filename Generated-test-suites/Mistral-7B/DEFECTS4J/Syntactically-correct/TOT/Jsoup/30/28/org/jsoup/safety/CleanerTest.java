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
 private Document dirtyDocument;
 private Document cleanDocument;

 @Before
 public void setUp() {
 Whitelist simpleWhitelist = Whitelist.relaxed(); // Use a simple whitelist for testing
 cleaner = new Cleaner(simpleWhitelist);

 dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script><h1 id='title'>Hello</h1></body></html>");
 }

 @Test
 public void testClean() {
 cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><h1 id='title'>Hello</h1></body></html>", cleanDocument.body().html());
 }

 @Test
 public void testIsValid_ValidDocument() {
 Document validDocument = Jsoup.parse("<html><body><h1 id='title'>Hello</h1></body></html>");
 assertTrue(cleaner.isValid(validDocument));
 }

 @Test
 public void testIsValid_InvalidDocument() {
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS')</script><h1 id='title'>Hello</h1></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testIsValid_EmptyDocument() {
 Document emptyDocument = Jsoup.parse("<html></html>");
 assertTrue(cleaner.isValid(emptyDocument));
 }

 @Test
 public void testIsValid_FrameDocument() {
 Document frameDocument = Jsoup.parse("<html><frameset rows='50%,50%'><frame name='frame1' src='http://example.com'></frame><frame name='frame2' src='http://example.com'></frameset></html>");
 assertTrue(cleaner.isValid(frameDocument));
 }

 @Test(expected = NullPointerException.class)
 public void testNullDocument() {
 cleaner.isValid(null);
 }
}