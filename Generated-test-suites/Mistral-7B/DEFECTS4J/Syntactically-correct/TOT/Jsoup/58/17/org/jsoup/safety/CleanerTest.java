import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CleanerTest {

 private static final String VALID_HTML = "<html><head></head><body><h1>Hello World</h1></body></html>";
 private static final String INVALID_HTML = "<html><head><script>alert('XSS')</script></head><body><h1>Hello World</h1></body></html>";

 @Test
 public void testCleanValidHtml() {
 Document dirtyDocument = Jsoup.parse(VALID_HTML);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals(VALID_HTML, cleanDocument.html());
 }

 @Test
 public void testCleanInvalidHtml() {
 Document dirtyDocument = Jsoup.parse(INVALID_HTML);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNull(cleanDocument);
 }

 @Test
 public void testIsValidValidHtml() {
 Document dirtyDocument = Jsoup.parse(VALID_HTML);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidInvalidHtml() {
 Document dirtyDocument = Jsoup.parse(INVALID_HTML);
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleanWithCustomWhitelist() {
 Whitelist customWhitelist = Whitelist.relaxed();
 customWhitelist.addTags("script", "style");
 customWhitelist.addAttribute("*", "on*");

 Cleaner cleaner = new Cleaner(customWhitelist);
 Document dirtyDocument = Jsoup.parse("<html><head><script src='test.js'></script><script>alert('XSS')</script></head><body><h1 onfocus='alert(\"Focus\")'>Hello World</h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 String expected = "<html><head></head><body><h1>Hello World</h1></body></html>";
 assertEquals(expected, cleanDocument.html());
 }

 @Test(expected = NullPointerException.class)
 public void testCleanWithNullDocument() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleTextOnly());
 Document dirtyDocument = null;
 cleaner.clean(dirtyDocument);
 }
}