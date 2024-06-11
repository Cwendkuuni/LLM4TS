import org.junit.jupiters.java8.TestWitness;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CleanerTest {

 private Document dirtyDocument;
 private Document cleanDocument;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @BeforeAll
 public void setup() {
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);
 dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script><h1>Test</h1></body></html>");
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 @Test
 public void testCleanMethod() {
 // Test with valid input
 cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument.body());
 assertFalse(cleanDocument.body().text().contains("alert('XSS');"));

 // Test with invalid input
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 cleanDocument = Document.createShell(invalidDocument.baseUri());
 cleaner.clean(invalidDocument);
 assertNotNull(cleanDocument.body());
 assertFalse(cleanDocument.body().text().contains("alert('XSS');"));

 // Test with empty body
 dirtyDocument = Jsoup.parse("<html><body></body></html>");
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument.body());
 assertFalse(cleanDocument.body().text().isEmpty());

 // Test with empty document
 dirtyDocument = Jsoup.parse(" ");
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 cleaner.clean(dirtyDocument);
 assertNull(cleanDocument.body());
 }

 @Test
 public void testIsValidMethod() {
 // Test with valid input
 assertTrue(cleaner.isValid(dirtyDocument));

 // Test with invalid input
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));

 // Test with empty body
 Document emptyBodyDocument = Jsoup.parse("<html><body></body></html>");
 assertFalse(cleaner.isValid(emptyBodyDocument));

 // Test with empty document
 Document emptyDocument = Jsoup.parse(" ");
 assertFalse(cleaner.isValid(emptyDocument));
 }

 @AfterAll
 public void teardown(@TestWitness TestContext testContext) {
 testContext.complete();
 }
}