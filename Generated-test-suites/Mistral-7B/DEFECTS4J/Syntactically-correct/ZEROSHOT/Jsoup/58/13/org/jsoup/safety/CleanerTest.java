import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setup() {
 whitelist = Whitelist.basic();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testClean() {
 String html = "<html><body><h1>Test</h1><p id=\"p1\">Hello World</p><script>alert('XSS');</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><h1>Test</h1><p id=\"p1\">Hello World</p></body></html>", cleanDocument.body().html());
 assertNull(cleanDocument.body().select("script").first());
 }

 @Test
 public void testIsValid() {
 String html = "<html><body><h1>Test</h1><p id=\"p1\">Hello World</p><script>alert('XSS');</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertFalse(isValid);
 }

 @Test
 public void testIsValidWithValidHtml() {
 String html = "<html><body><h1>Test</h1><p id=\"p1\">Hello World</p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 boolean isValid = cleaner.isValid(dirtyDocument);

 assertTrue(isValid);
 }

 @Test
 public void testCleanWithEmptyHtml() {
 Document dirtyDocument = Jsoup.parse("<html></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html></html>", cleanDocument.body().html());
 }

 @Test(expected = NullPointerException.class)
 public void testCleanWithNullDocument() {
 cleaner.clean(null);
 }
}