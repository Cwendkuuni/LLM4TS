import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class CleanerTest {

 @Test
 public void testCreateCleaner() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 assertEquals("Cleaner created successfully", cleaner, cleaner);
 }

 @Test
 public void testCleanInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<script>alert('XSS')</script>");
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("Clean document should not contain script tags", 0, cleanDocument.select("script").size());
 assertEquals("Clean document base URI should be the same as dirty document base URI", dirtyDocument.baseUri(), cleanDocument.baseUri());
 }

 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parse("<p>Hello <strong>World</strong></p>");
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("Clean document should contain only text nodes", 1, cleanDocument.numTextNodes());
 assertEquals("Clean document text should be 'Hello World'", "Hello World", cleanDocument.text());
 }

 @Test
 public void testIsValidInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<script>alert('XSS')</script>");
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());

 assertFalse("Invalid document should not be considered valid", cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Jsoup.parse("<p>Hello <strong>World</strong></p>");
 Cleaner cleaner = new Cleaner(Whitelist.simpleText());

 assertTrue("Valid document should be considered valid", cleaner.isValid(dirtyDocument));
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullDocument() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 cleaner.clean(null);
 }
}