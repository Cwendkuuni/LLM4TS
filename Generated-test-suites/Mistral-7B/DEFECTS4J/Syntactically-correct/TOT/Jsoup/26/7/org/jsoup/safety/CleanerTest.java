import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private Document dirtyDocument;
 private Document cleanDocument;
 private Cleaner cleaner;
 private static final String TEST_HTML = "<html><body><h1 id=\"title\">Test Title</h1>" +
         "<script src=\"malicious.js\"></script>" +
         "<img src=\"image.jpg\" alt=\"Test Image\">" +
         "<p id=\"content\">Test Content <b>in bold</b></p></body></html>";

 @Before
 public void setUp() {
 dirtyDocument = Jsoup.parse(TEST_HTML);
 cleanDocument = Document.createShell(dirtyDocument.baseUri());
 cleaner = new Cleaner(Whitelist.simpleTextOnly());
 }

 @Test
 public void testCleaner_isValidFalse() {
 // Test case for isValid method when input document is invalid
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleaner_isValidTrue() {
 // Test case for isValid method when input document is valid (only text and simple tags)
 cleaner = new Cleaner(Whitelist.simpleTextAndHtml());
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleaner_clean() {
 // Test case for clean method, checking if only allowed tags and attributes are present in the cleaned document
 Document expectedCleanDocument = Jsoup.parse("<html><body><h1 id=\"title\">Test Title</h1>" +
         "<p id=\"content\">Test Content <b>in bold</b></p></body></html>");
 cleaner = new Cleaner(Whitelist.simpleTextAndHtml());
 Document actualCleanDocument = cleaner.clean(dirtyDocument);
 assertEquals(expectedCleanDocument, actualCleanDocument);
 }

 @Test(expected = NullPointerException.class)
 public void testCleaner_constructorNullWhitelist() {
 // Test case for constructor when whitelist is null
 cleaner = new Cleaner(null);
 }

 @Test
 public void testCleaner_copySafeNodes() {
 // Test case for copySafeNodes method, checking if only allowed tags and attributes are present in the copied nodes
 Element sourceElement = dirtyDocument.selectFirst("#content");
 int numDiscarded = cleaner.copySafeNodes(sourceElement, cleanDocument.body());
 assertEquals(0, numDiscarded);
 }
}