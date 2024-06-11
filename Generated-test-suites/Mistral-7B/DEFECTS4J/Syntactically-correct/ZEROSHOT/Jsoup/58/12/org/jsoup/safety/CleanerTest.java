import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleTags("p", "a");
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCreateNewCleaner() {
 assertNotNull(cleaner);
 }

 @Test
 public void testCleanDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><div><p>Test</p><script>alert('XSS')</script></div></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><p>Test</p></body></html>", cleanDocument.body().html());
 assertNull(cleanDocument.body().child(1));
 }

 @Test
 public void testIsValidDocument() {
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
 assertFalse(cleaner.isValid(invalidDocument));

 Document validDocument = Jsoup.parse("<html><body><p>Test</p></body></html>");
 assertTrue(cleaner.isValid(validDocument));
 }

 @Test
 public void testCopySafeNodes() {
 Element sourceElement = new Element("div");
 sourceElement.addClass("test");
 sourceElement.appendText("Test");
 sourceElement.appendText(" Test with attributes");
 Element destinationElement = new Element("p");
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destinationElement);

 assertEquals("Test Test with attributes", destinationElement.text());
 assertEquals(0, numDiscarded);

 Element sourceElementWithScript = new Element("div");
 sourceElementWithScript.addClass("test");
 sourceElementWithScript.appendText("Test");
 sourceElementWithScript.appendText("<script>alert('XSS')</script>");
 Element destinationElementWithScript = new Element("p");
 numDiscarded = cleaner.copySafeNodes(sourceElementWithScript, destinationElementWithScript);

 assertEquals("Test", destinationElementWithScript.text());
 assertEquals(1, numDiscarded);
 }

 @Test
 public void testCreateSafeElement() {
 Element sourceElement = new Element("div");
 sourceElement.addClass("test");
 Attributes sourceAttrs = sourceElement.attributes();
 sourceAttrs.put("data-test", "testValue");
 sourceAttrs.put("style", "color: red;");
 ElementMeta destElementMeta = cleaner.createSafeElement(sourceElement);

 assertEquals("div", destElementMeta.el.tagName());
 assertEquals("test", destElementMeta.el.className());
 assertEquals("testValue", destElementMeta.el.attr("data-test"));
 assertFalse(destElementMeta.el.hasAttr("style"));
 }
}