import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.relaxed();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testValidDocument() {
 String html = "<html><head></head><body><p>Hello world!</p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 assertTrue("Valid document should be considered valid", cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testInvalidDocument() {
 String html = "<html><head><script src=\"malicious.js\"></script></head><body><p>Hello world!</p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 assertFalse("Invalid document should not be considered valid", cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleanDocument() {
 String html = "<html><head><script src=\"malicious.js\"></script></head><body><p>Hello world! <script>alert('XSS')</script></p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 // Check that the cleaned document only contains allowed tags and attributes
 assertNotNull("Cleaned document should not be null", cleanDocument);
 assertEquals("<html><body><p>Hello world!</p></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanDocumentWithCustomWhitelist() {
 Whitelist customWhitelist = Whitelist.simpleTextOnly();
 Cleaner cleanerWithCustomWhitelist = new Cleaner(customWhitelist);
 String html = "<html><head></head><body><p>Hello world! <script>alert('XSS')</script></p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleanerWithCustomWhitelist.clean(dirtyDocument);
 // Check that the cleaned document only contains allowed tags and attributes
 assertNotNull("Cleaned document should not be null", cleanDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

}