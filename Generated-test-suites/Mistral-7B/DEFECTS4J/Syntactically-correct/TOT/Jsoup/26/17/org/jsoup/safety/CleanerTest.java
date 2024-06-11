import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
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

 dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS');</script><h1 id='title'>Hello World</h1></body></html>");
 }

 @Test
 public void testClean() {
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("Hello World", cleanDocument.select("#title").text());
 assertNull(cleanDocument.select("script").first());
 }

 @Test
 public void testIsValid() {
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleanWithEmptyWhitelist() {
 Whitelist emptyWhitelist = Whitelist.empty();
 Cleaner emptyCleaner = new Cleaner(emptyWhitelist);
 Document emptyDocument = Jsoup.parse("<html><body></body></html>");

 Document emptyCleanDocument = emptyCleaner.clean(emptyDocument);
 assertNotNull(emptyCleanDocument);
 assertEquals("", emptyCleanDocument.body().html());
 }

 @Test(expected = NullPointerException.class)
 public void testCleanWithNullWhitelist() {
 Cleaner nullCleaner = new Cleaner(null);
 Document nullDocument = Jsoup.parse("<html><body></body></html>");

 nullCleaner.clean(nullDocument);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanWithNullDocument() {
 Cleaner cleaner = new Cleaner(whitelist);
 cleaner.clean(null);
 }

 @Test
 public void testCopySafeNodes() {
 Element safeSource = Jsoup.parse("<html><body><h1 id='title'>Hello World</h1></body></html>").body().getElementsByTag("h1").first();
 Element safeDest = Jsoup.parse("<html><body></body></html>").body();
 int numDiscarded = cleaner.copySafeNodes(safeSource, safeDest);

 assertEquals("Hello World", safeDest.select("#title").text());
 assertEquals(0, numDiscarded);
 }
}