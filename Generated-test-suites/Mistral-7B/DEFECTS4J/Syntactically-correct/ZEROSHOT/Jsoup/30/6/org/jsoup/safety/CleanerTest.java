package org.jsoup.safety;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleTextOnly();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCreateCleaner() {
 assertNotNull(cleaner);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateCleanerNullWhitelist() {
 cleaner = new Cleaner(null);
 }

 @Test
 public void testCleanDocumentWithAllowedTags() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><h1></h1></body></html>", cleanDocument.body().html());
 }

 @Test
 public void testCleanDocumentWithDisallowedTags() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertTrue(cleanDocument.body().text().isEmpty());
 }

 @Test
 public void testIsValidDocumentWithAllowedTags() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testIsValidDocumentWithDisallowedTags() {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodes() {
 Element sourceElement = new Element("div", null, new Attributes() {{
 put("id", "source");
 put("class", "source");
 put("style", "color: red;");
 put("title", "This is a title");
 }});
 Element destinationElement = new Element("div", null, new Attributes());
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destinationElement);
 assertEquals(0, numDiscarded);
 assertEquals("class", sourceElement.attributes().keySet().iterator().next());
 assertNull(destinationElement.attributes().get("style"));
 assertNull(destinationElement.attributes().get("title"));
 }

 @Test
 public void testCopySafeNodesWithDisallowedAttributes() {
 Element sourceElement = new Element("div", null, new Attributes() {{
 put("id", "source");
 put("class", "source");
 put("style", "color: red;");
 put("onclick", "alert('XSS');");
 }});
 Element destinationElement = new Element("div", null, new Attributes());
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destinationElement);
 assertEquals(1, numDiscarded);
 assertNull(destinationElement.attributes().get("onclick"));
 }

}