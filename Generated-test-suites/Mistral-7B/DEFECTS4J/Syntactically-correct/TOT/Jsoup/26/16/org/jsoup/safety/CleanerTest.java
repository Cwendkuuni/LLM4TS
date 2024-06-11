import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.*;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanMethod() {
 // Test with valid HTML
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());

 // Test with invalid HTML (script tag)
 Document invalidDocument = Jsoup.parse("<html><body><h1>Hello World</h1><script>alert('XSS')</script></body></html>");
 Document cleanInvalidDocument = cleaner.clean(invalidDocument);
 assertEquals("<html><body><h1></h1></body></html>", cleanInvalidDocument.html());

 // Test with empty HTML
 Document emptyDocument = Jsoup.parse("<html></html>");
 Document cleanEmptyDocument = cleaner.clean(emptyDocument);
 assertEquals("<html></html>", cleanEmptyDocument.html());
 }

 @Test
 public void testIsValidMethod() {
 // Test with valid HTML
 Document validDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 assertTrue(cleaner.isValid(validDocument));

 // Test with invalid HTML (script tag)
 Document invalidDocument = Jsoup.parse("<html><body><h1>Hello World</h1><script>alert('XSS')</script></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));
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