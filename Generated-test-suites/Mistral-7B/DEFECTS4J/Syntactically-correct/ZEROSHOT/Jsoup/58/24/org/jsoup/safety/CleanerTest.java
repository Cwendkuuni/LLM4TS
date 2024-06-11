import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleTagNames("p", "a");
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p id='p1'>Hello World!</p><a href='http://www.example.com'>Link</a></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertTrue(cleanDocument.body().text().equals("Hello World!"));
 assertEquals(2, cleanDocument.body().childNodeSize());
 assertEquals("p", cleanDocument.body().getElementsByTag("p").first().tagName());
 assertEquals("a", cleanDocument.body().getElementsByTag("a").first().tagName());
 }

 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p id='p1'>Hello World!</p><script>alert('XSS');</script></body></html>");
 int numDiscarded = cleaner.clean(dirtyDocument).body().childNodeSize();
 assertTrue(numDiscarded > 1);
 }

 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p id='p1'>Hello World!</p><a href='http://www.example.com'>Link</a></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><p id='p1'>Hello World!</p><script>alert('XSS');</script></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }
}