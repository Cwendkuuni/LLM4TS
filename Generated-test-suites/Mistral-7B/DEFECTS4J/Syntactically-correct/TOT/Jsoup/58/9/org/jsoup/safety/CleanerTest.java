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
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleTextOnly();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidDocument() {
 String html = "<html><body><p>This is a valid document.</p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<html><body><p></p></body></html>", cleanDocument.html());
 }

 @Test
 public void testCleanInvalidDocument() {
 String html = "<html><body><p>This is an invalid document with <script>alert('XSS!')</script></p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanDocument);
 assertFalse(cleanDocument.body().text().contains("XSS!"));
 }

 @Test
 public void testIsValidValidDocument() {
 String html = "<html><body><p>This is a valid document.</p></body></html>";
 Document invalidDocument = Jsoup.parse(html);
 assertTrue(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testIsValidInvalidDocument() {
 String html = "<html><body><p>This is an invalid document with <script>alert('XSS!')</script></p></body></html>";
 Document invalidDocument = Jsoup.parse(html);
 assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test
 public void testCopySafeNodesValidDocument() {
 String html = "<html><body><p id='test' class='test'>This is a valid document with <script>alert('XSS!')</script></p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destination);
 Element testElement = destination.getElementsByTag("p").first();
 assertNotNull(testElement);
 assertNull(testElement.child(0)); // <script> tag should have been discarded
 assertEquals(1, numDiscarded);
 }

 @Test
 public void testCopySafeNodesInvalidDocument() {
 String html = "<html><body><p>This is an invalid document with <script>alert('XSS!')</script></p></body></html>";
 Document dirtyDocument = Jsoup.parse(html);
 Element destination = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), destination);
 assertNotNull(destination);
 assertFalse(destination.text().contains("XSS!")); // <script> tag should have been discarded
 assertEquals(1, numDiscarded);
 }

}