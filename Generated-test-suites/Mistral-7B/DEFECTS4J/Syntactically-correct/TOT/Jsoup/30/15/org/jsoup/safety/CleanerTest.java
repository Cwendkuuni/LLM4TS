import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CleanerTest {

 private Document dirtyDocument;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 dirtyDocument = Jsoup.parseBodyElements("<html><body><script>alert('XSS')</script><p>Hello World</p></body></html>");
 }

 @Test
 public void testCleanMethod_ValidDocument_ShouldReturnCleanDocument() {
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertEquals("<html><body><p>Hello World</p></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanMethod_InvalidDocument_ShouldReturnCleanDocumentButKeepAttributes() {
 Document cleanDocument = cleaner.clean(Jsoup.parseBodyElements("<html><body><p id='test' class='test' title='Test'><script>alert('XSS')</script></p></body></html>"));
 assertNotNull(cleanDocument);
 assertEquals("<html><body><p id='test' class='test' title='Test'></p></body></html>", cleanDocument.html());
 }

 @Test
 public void testIsValidMethod_ValidDocument_ShouldReturnTrue() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidMethod_InvalidDocument_ShouldReturnFalse() {
 Document invalidDocument = Jsoup.parseBodyElements("<html><body><p onclick='alert(\"XSS\")'><script>alert('XSS')</script></p></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testCopySafeNodesMethod_ValidElements_ShouldCopyAllElementsAndAttributes() {
 Element root = dirtyDocument.body();
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(root, destination);
 assertNotNull(destination.children());
 assertEquals(root.childNodes().size(), destination.childNodes().size() + numDiscarded);
 }

 @Test
 public void testCopySafeNodesMethod_InvalidElements_ShouldDiscardAllNodesAndAttributes() {
 Element root = Jsoup.parseBodyElements("<html><body><script>alert('XSS')</script></body></html>").body();
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(root, destination);
 assertNull(destination.children());
 assertEquals(root.childNodes().size(), numDiscarded);
 }

}