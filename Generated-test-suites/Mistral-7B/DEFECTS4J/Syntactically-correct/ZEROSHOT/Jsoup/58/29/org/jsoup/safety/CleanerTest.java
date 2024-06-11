import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

 @Test
 public void testCreateCleaner() {
 // Given a valid whitelist
 Whitelist whitelist = Whitelist.RELAXED;

 // When a new Cleaner is created with the whitelist
 Cleaner cleaner = new Cleaner(whitelist);

 // Then the Cleaner should not be null
 assertNotNull(cleaner);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateCleanerWithNullWhitelist() {
 // Given a null whitelist
 Whitelist whitelist = null;

 // When a new Cleaner is created with the null whitelist
 Cleaner cleaner = new Cleaner(whitelist);

 // Then an exception should be thrown
 }

 @Test
 public void testCleanDocument() {
 // Given a dirty document
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");

 // And a Cleaner with a valid whitelist
 Cleaner cleaner = new Cleaner(Whitelist.RELAXED);

 // When the document is cleaned with the Cleaner
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Then the cleaned document should not contain the script tag
 assertFalse(cleanDocument.body().hasMetaElements());
 // And the original document should remain unchanged
 assertEquals(dirtyDocument.body(), dirtyDocument.body());
 }

 @Test
 public void testCleanDocumentWithEmptyBody() {
 // Given a document with an empty body
 Document dirtyDocument = Jsoup.parse("<html><head></head><body></body></html>");

 // And a Cleaner with a valid whitelist
 Cleaner cleaner = new Cleaner(Whitelist.RELaxed);

 // When the document is cleaned with the Cleaner
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Then the cleaned document should have an empty body
 assertEquals("", cleanDocument.body().text());
 // And the original document should remain unchanged
 assertEquals(dirtyDocument.body(), dirtyDocument.body());
 }

 @Test
 public void testCleanDocumentWithAllowedElements() {
 // Given a document with allowed elements
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");

 // And a Cleaner with a valid whitelist that allows p tags
 Cleaner cleaner = new Cleaner(Whitelist.relaxed().tagStart("p"));

 // When the document is cleaned with the Cleaner
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Then the cleaned document should contain the p tag
 assertEquals("Hello World", cleanDocument.body().select("p").text());
 // And the original document should remain unchanged
 assertEquals(dirtyDocument.body(), dirtyDocument.body());
 }

 @Test
 public void testCleanDocumentWithDisallowedElements() {
 // Given a document with a disallowed element
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");

 // And a Cleaner with a valid whitelist that does not allow script tags
 Cleaner cleaner = new Cleaner(Whitelist.RELaxed.filterOut("script"));

 // When the document is cleaned with the Cleaner
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Then the cleaned document should not contain the script tag
 assertFalse(cleanDocument.body().hasMetaElements());
 // And the original document should remain unchanged
 assertEquals(dirtyDocument.body(), dirtyDocument.body());
 }

 @Test
 public void testIsValidDocument() {
 // Given a valid document
 Document dirtyDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");

 // And a Cleaner with a valid whitelist that allows p tags
 Cleaner cleaner = new Cleaner(Whitelist.RELaxed);

 // When the document is tested for validity with the Cleaner
 boolean isValid = cleaner.isValid(dirtyDocument);

 // Then the document is valid
 assertTrue(isValid);
 }

 @Test
 public void testIsValidDocumentWithDisallowedElements() {
 // Given a document with a disallowed element
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");

 // And a Cleaner with a valid whitelist that does not allow script tags
 Cleaner cleaner = new Cleaner(Whitelist.RELaxed.filterOut("script"));

 // When the document is tested for validity with the Cleaner
 boolean isValid = cleaner.isValid(dirtyDocument);

 // Then the document is not valid
 assertFalse(isValid);
 }

 @Test
 public void testIsValidDocumentWithEmptyBody() {
 // Given a document with an empty body
 Document dirtyDocument = Jsoup.parse("<html><head></head><body></body></html>");

 // And a Cleaner with a valid whitelist
 Cleaner cleaner = new Cleaner(Whitelist.RELaxed);

 // When the document is tested for validity with the Cleaner
 boolean isValid = cleaner.isValid(dirtyDocument);

 // Then the document is valid
 assertTrue(isValid);
 }

 @Test
 public void testIsValidDocumentWithAllowAll() {
 // Given a document with all elements allowed
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");

 // And a Cleaner with a valid whitelist that allows all elements
 Cleaner cleaner = new Cleaner(Whitelist.ALLOW_ALL);

 // When the document is tested for validity with the Cleaner
 boolean isValid = cleaner.isValid(dirtyDocument);

 // Then the document is valid
 assertTrue(isValid);
 }

 @Test(expected = NullPointerException.class)
 public void testIsValidDocumentWithNullDocument() {
 // Given a null document
 Document dirtyDocument = null;

 // And a Cleaner with a valid whitelist
 Cleaner cleaner = new Cleaner(Whitelist.RELaxed);

 // When the document is tested for validity with the Cleaner
 boolean isValid = cleaner.isValid(dirtyDocument);

 // Then an exception should be thrown
 }

 private static final class ElementMeta {
 Element el;
 int numAttribsDiscarded;
 }

 private static ElementMeta createSafeElement(Element sourceEl, Whitelist whitelist) {
 String sourceTag = sourceEl.tagName();
 Attributes destAttrs = new Attributes();
 Element dest = new Element(Tag.valueOf(sourceTag), sourceEl.baseUri(), destAttrs);
 int numDiscarded = 0;

 Attributes sourceAttrs = sourceEl.attributes();
 for (Attribute sourceAttr : sourceAttrs) {
 if (whitelist.isSafeAttribute(sourceTag, sourceEl, sourceAttr))
 destAttrs.put(sourceAttr);
 else
 numDiscarded++;
 }

 return new ElementMeta(dest, numDiscarded);
 }
}