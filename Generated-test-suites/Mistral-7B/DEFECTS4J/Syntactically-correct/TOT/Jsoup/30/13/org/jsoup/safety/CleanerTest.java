import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setup() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCreateCleaner() {
 validateWhitelist(whitelist);
 cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertFalse(cleanDocument.body().text().contains("XSS"));
 }

 @Test
 public void testIsValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));

 Document dirtyDocumentWithScript = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 assertFalse(cleaner.isValid(dirtyDocumentWithScript));
 }

 @Test
 public void testCopySafeNodesWithAllowedTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><p id='test' class='test'><b>Test</b></p></body></html>");
 Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 Element testElement = cleanDocument.body().getElementById("test");
 assertEquals(1, testElement.childrenSize());
 assertNotNull(testElement.child(0));
 assertInstanceOf(Element.class, testElement.child(0));
 assertFalse(numDiscarded > 0);
 }

 @Test
 public void testCopySafeNodesWithDisallowedTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 assertFalse(cleanDocument.body().text().isEmpty());
 assertFalse(numDiscarded <= 0);
 }

 @Test
 public void testCopySafeNodesWithAllowedAttribute() {
 Document dirtyDocument = Jsoup.parse("<html><body><p id='test' class='test' style='color: red;'></p></body></html>");
 Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 Element testElement = cleanDocument.body().getElementById("test");
 assertEquals(1, testElement.childrenSize());
 assertNotNull(testElement.child(0));
 assertInstanceOf(Element.class, testElement.child(0));
 assertFalse(numDiscarded > 0);
 }

 @Test
 public void testCopySafeNodesWithDisallowedAttribute() {
 Document dirtyDocument = Jsoup.parse("<html><body><p id='test' class='test' onclick='alert('XSS');'></p></body></html>");
 Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
 assertFalse(cleanDocument.body().text().isEmpty());
 assertFalse(numDiscarded <= 0);
 }

 @Test
 public void testCreateSafeElementWithAllowedTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><p id='test' class='test'><b>Test</b></p></body></html>");
 ElementMeta elementMeta = cleaner.createSafeElement(dirtyDocument.body().get(0));
 Element safeElement = elementMeta.el;
 assertEquals("p", safeElement.tagName());
 assertFalse(elementMeta.numAttribsDiscarded > 0);
 }

 @Test
 public void testCreateSafeElementWithDisallowedTag() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 ElementMeta elementMeta = cleaner.createSafeElement(dirtyDocument.body().get(0));
 assertNull(elementMeta.el);
 }

 @Test
 public void testCreateSafeElementWithAllowedAttribute() {
 Document dirtyDocument = Jsoup.parse("<html><body><p id='test' class='test' style='color: red;'></p></body></html>");
 ElementMeta elementMeta = cleaner.createSafeElement(dirtyDocument.body().get(0));
 Element safeElement = elementMeta.el;
 assertEquals("p", safeElement.tagName());
 assertFalse(elementMeta.numAttribsDiscarded > 0);
 }

 @Test
 public void testCreateSafeElementWithDisallowedAttribute() {
 Document dirtyDocument = Jsoup.parse("<html><body><p id='test' class='test' onclick='alert('XSS');'></p></body></html>");
 ElementMeta elementMeta = cleaner.createSafeElement(dirtyDocument.body().get(0));
 assertNull(elementMeta.el);
 }

 private void validateWhitelist(Whitelist whitelist) {
 List<Tag> tags = whitelist.tags();
 assertFalse(tags.isEmpty());
 for (Tag tag : tags) {
 assertNotNull(tag.name());
 }
 }
}