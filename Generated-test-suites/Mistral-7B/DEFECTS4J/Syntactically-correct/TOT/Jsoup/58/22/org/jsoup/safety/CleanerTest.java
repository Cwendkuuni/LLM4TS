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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CleanerTest {

 private Whitelist allowedTagsWhitelist;
 private Whitelist disallowedTagsWhitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 allowedTagsWhitelist = Whitelist.simpleText();
 allowedTagsWhitelist.addTags("p", "a");
 disallowedTagsWhitelist = Whitelist.simpleText();
 disallowedTagsWhitelist.addTags("script", "iframe");
 cleaner = new Cleaner(allowedTagsWhitelist);
 }

 @Test
 public void testCleanDocument() {
 String html = "<html><head></head><body><p id='test'>Hello World!</p><script>alert('XSS Attack!');</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element root = cleanDocument.body();
 assertNotNull(root);
 assertEquals("<p id='test'>Hello World!</p>", root.html());
 assertNull(root.child(0, "script"));
 }

 @Test
 public void testIsValidDocument() {
 String html = "<html><head></head><body><p id='test'>Hello World!</p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleanDocumentWithEmptyBody() {
 String html = "<html><head></head></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertNotNull(cleanDocument.body());
 assertEquals("", cleanDocument.body().html());
 }

 @Test
 public void testCleanDocumentWithAllowedTags() {
 String html = "<html><head></head><body><p id='test'>Hello World! <a href='http://example.com'>Link</a></p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Element root = cleanDocument.body();
 assertNotNull(root);
 assertEquals("<p id='test'>Hello World! <a href='http://example.com'>Link</a></p>", root.html());
 }

 @Test(expected = NullPointerException.class)
 public void testCleanDocumentWithNullDocument() {
 cleaner.clean(null);
 }

 @Test
 public void testCleanDocumentWithDisallowedTags() {
 String html = "<html><head></head><body><script>alert('XSS Attack!');</script></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 cleaner = new Cleaner(disallowedTagsWhitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("", cleanDocument.body().html());
 }
}