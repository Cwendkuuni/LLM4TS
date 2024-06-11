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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CleanerTest {

 private Document dirtyDocument;
 private Document cleanDocument;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script></body></html>");
 cleanDocument = Jsoup.parse("<html><body></body></html>");
 }

 @Test
 public void testCleanEmptyDocument() {
 assertEquals(cleanDocument, cleaner.clean(cleanDocument));
 }

 @Test
 public void testCleanInvalidDocument() {
 assertFalse(cleaner.isValid(dirtyDocument));
 assertEquals(cleanDocument, cleaner.clean(dirtyDocument));
 }

 @Test
 public void testCleanValidDocument() {
 Document validDocument = Jsoup.parse("<html><body><p>Hello World</p></body></html>");
 assertTrue(cleaner.isValid(validDocument));
 assertEquals(validDocument, cleaner.clean(validDocument));
 }

 @Test(expected = NullPointerException.class)
 public void testNullDocument() {
 cleaner.clean(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidWhitelist() {
 Whitelist invalidWhitelist = new Whitelist() {
 @Override
 public boolean isSafeTag(String tagName) {
 return false;
 }
 };
 cleaner = new Cleaner(invalidWhitelist);
 assertThrows(IllegalArgumentException.class, () -> cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCopySafeNodes() {
 Element sourceElement = dirtyDocument.body().selectFirst("script");
 Element destinationElement = cleanDocument.body().addElement("div");
 int discardedAttributes = cleaner.copySafeNodes(sourceElement, destinationElement);
 assertEquals(sourceElement.attributes().size(), discardedAttributes + destinationElement.attributes().size());
 }
}