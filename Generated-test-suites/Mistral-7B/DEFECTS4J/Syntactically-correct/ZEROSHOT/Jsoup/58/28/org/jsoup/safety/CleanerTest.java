import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

 @Test
 public void testCleanerInstance() {
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 String html = "<html><head></head><body><script>alert('XSS');</script><div>Hello</div></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><head></head><body><div>Hello</div></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidDocumentFalse() {
 String html = "<html><head><script>alert('XSS');</script></head><body><div>Hello</div></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertFalse(isValid);
 }

 @Test
 public void testIsValidDocumentTrue() {
 String html = "<html><body><div>Hello</div></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Whitelist whitelist = Whitelist.simpleText();
 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertTrue(isValid);
 }
}