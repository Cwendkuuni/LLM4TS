import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CleanerTest {

 private Document dirtyDocument;
 private Document cleanDocument;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setup() {
 whitelist = Whitelist.simple();
 dirtyDocument = Jsoup.parse("<html><head><script>alert('XSS')</script></head><body><h1>Hello</h1></body></html>");
 cleanDocument = Jsoup.parse("<html><body><h1>Hello</h1></body></html>");
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidDocument() {
 Document result = cleaner.clean(dirtyDocument);
 assertNotNull(result);
 assertEquals(cleanDocument.html(), result.html());
 }

 @Test
 public void testCleanInvalidDocument() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleanInvalidDocumentWithEmptyHead() {
 Document invalidDocument = Jsoup.parse("<html><body><h1>Hello</h1></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));
 Document result = cleaner.clean(invalidDocument);
 assertEquals(cleanDocument.html(), result.html());
 }

 @Test
 public void testCleanEmptyDocument() {
 Document emptyDocument = Jsoup.parse("<html></html>");
 Document result = cleaner.clean(emptyDocument);
 assertEquals(cleanDocument.html(), result.html());
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullDocument() {
 cleaner.clean(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateCleanerWithNullWhitelist() {
 assertThrows(IllegalArgumentException.class, () -> new Cleaner(null));
 }

 @Test
 public void testIsValidValidDocument() {
 assertTrue(cleaner.isValid(cleanDocument));
 }

 @Test
 public void testIsValidInvalidDocument() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidEmptyDocument() {
 assertTrue(cleaner.isValid(cleanDocument));
 }
}