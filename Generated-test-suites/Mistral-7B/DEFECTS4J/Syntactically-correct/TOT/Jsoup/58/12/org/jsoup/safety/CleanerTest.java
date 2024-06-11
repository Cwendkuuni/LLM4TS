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

 private Document dirtyDocument;
 private Document cleanDocument;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 dirtyDocument = Jsoup.parse("<html><head><script>alert('XSS')</script></head><body><h1 id='title'>Hello</h1></body></html>");
 cleanDocument = Jsoup.parse("<html><body><h1 id='title'>Hello</h1></body></html>");
 whitelist = Whitelist.DEFAULT;
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidDocument() {
 Document cleaned = cleaner.clean(dirtyDocument);
 assertEquals(cleanDocument.body(), cleaned.body());
 }

 @Test
 public void testCleanInvalidDocument() {
 assertFalse(cleaner.isValid(dirtyDocument));
 Document cleaned = cleaner.clean(dirtyDocument);
 assertEquals(cleanDocument.body(), cleaned.body());
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullDocument() {
 cleaner.clean(null);
 }

 @Test
 public void testIsValidDocumentWithAllowedTags() {
 Document allowedTagsDocument = Jsoup.parse("<html><body><script src='safe.js'></script><h1 id='title'>Hello</h1></body></html>");
 assertTrue(cleaner.isValid(allowedTagsDocument));
 }

 @Test
 public void testIsValidDocumentWithDisallowedTags() {
 Document disallowedTagsDocument = Jsoup.parse("<html><body><script src='malicious.js'></script><h1 id='title'>Hello</h1></body></html>");
 assertFalse(cleaner.isValid(disallowedTagsDocument));
 }

 @Test
 public void testCopySafeNodes() {
 Element root = Jsoup.parse("<html><head><script>alert('XSS')</script></head><body><h1 id='title'>Hello</h1></body></html>").body();
 Element dest = Jsoup.parse("<html><body></body></html>").body();
 cleaner.copySafeNodes(root, dest);
 assertEquals(root, dest);
 }
}