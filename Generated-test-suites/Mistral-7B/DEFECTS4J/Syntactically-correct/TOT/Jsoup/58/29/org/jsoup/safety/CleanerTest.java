import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CleanerTest {

 private Document dirtyDocument;
 private Document cleanDocument;
 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setup() {
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);
 dirtyDocument = Jsoup.parse("<html><body><script>alert('Hello')</script><h1 id='test'>Test</h1></body></html>");
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 }

 @Test
 public void testCleanMethod() {
 copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 assertEquals("<html><body><h1 id='test'>Test</h1></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidMethodWithValidDocument() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidMethodWithInvalidDocument() {
 assertFalse(cleaner.isValid(Jsoup.parse("<html><body><script>alert('Hello')</script></body></html>")));
 }

 @Test
 public void testIsValidMethodWithEmptyDocument() {
 assertTrue(cleaner.isValid(Jsoup.parse("<html></html>")));
 }

 @Test
 public void testIsValidMethodWithNullDocument() {
 assertNull(cleaner.isValid(null));
 }

 @Test
 public void testCleanMethodWithNullDocument() {
 assertThrows(NullPointerException.class, () -> cleaner.clean(null));
 }

 @Test
 public void testCleanMethodWithEmptyDocument() {
 Document emptyDocument = Document.createShell(dirtyDocument.baseUri());
 cleaner.clean(emptyDocument);
 assertNull(emptyDocument.body());
 }

 @Test
 public void testCleanMethodWithInvalidDocument() {
 ParseErrorList errors = new ParseErrorList();
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('Hello')</script></body></html>", "", errors);
 assertFalse(cleaner.isValid(invalidDocument));
 cleanDocument = Document.createShell(invalidDocument.baseUri());
 cleaner.clean(invalidDocument);
 assertEquals("<html><body></body></html>", cleanDocument.html());
 }

}