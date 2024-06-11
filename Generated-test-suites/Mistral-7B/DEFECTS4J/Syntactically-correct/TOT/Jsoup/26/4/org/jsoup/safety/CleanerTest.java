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
 whitelist = Whitelist.relaxed();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidHtml() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Valid HTML</p></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><p>Valid HTML</p></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanInvalidHtmlWithScriptTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Valid HTML</p><script>alert('XSS')</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertFalse(cleanDocument.body().hasText());
 // The script tag was removed, and body is now empty
 }

 @Test
 public void testCleanInvalidHtmlWithStyleTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Valid HTML</p><style>body { background-color: red; }</style></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><p>Valid HTML</p></body></html>", cleanDocument.html());
 // The style tag was kept, but its content was empty
 }

 @Test
 public void testIsValidValidHtml() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Valid HTML</p></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidHtmlWithScriptTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Valid HTML</p><script>alert('XSS')</script></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidHtmlWithStyleTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Valid HTML</p><style>body { background-color: red; }</style></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidHtmlWithScriptAndStyleTags() {
 Document dirtyDocument = Jsoup.parse("<html><body><p>Valid HTML</p><script>alert('XSS')</script><style>body { background-color: red; }</style></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
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