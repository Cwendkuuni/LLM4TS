import org.junit.jupitermarker.junit4.DisplayName;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@DisplayName("Cleaner Tests")
public class CleanerTest {

 private static final String HTML_TAGS_WHITELIST = "body,p,a,img";
 private static final String HTML_ATTRIBUTES_WHITELIST = "class,src";

 @DisplayName("Test creating a new Cleaner with a valid whitelist")
 public void testCreateCleaner() {
 Whitelist whitelist = Whitelist.simpleText(HTML_TAGS_WHITELIST, HTML_ATTRIBUTES_WHITELIST);
 Cleaner cleaner = new Cleaner(whitelist);
 assertNotNull(cleaner);
 }

 @DisplayName("Test cleaning a valid document")
 public void testCleanValidDocument() {
 String html = "<p class='test'>Hello <a href='http://example.com'>link</a></p>";
 Document dirtyDocument = Jsoup.parseBodyFragment(html);
 Cleaner cleaner = new Cleaner(Whitelist.simpleText(HTML_TAGS_WHITELIST, HTML_ATTRIBUTES_WHITELIST));
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals("<p class='test'><a href='http://example.com'></a></p>", cleanDocument.body().html());
 }

 @DisplayName("Test cleaning an invalid document")
 public void testCleanInvalidDocument() {
 String html = "<script src='http://attacker.com/malware.js'>";
 Document dirtyDocument = Jsoup.parseBodyFragment(html);
 Cleaner cleaner = new Cleaner(Whitelist.simpleText(HTML_TAGS_WHITELIST, HTML_ATTRIBUTES_WHITELIST));
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertNull(cleanDocument.body().html());
 }

 @DisplayName("Test checking if a valid document is valid")
 public void testIsValidValidDocument() {
 String html = "<p class='test'>Hello <a href='http://example.com'>link</a></p>";
 Document dirtyDocument = Jsoup.parseBodyFragment(html);
 Cleaner cleaner = new Cleaner(Whitelist.simpleText(HTML_TAGS_WHITELIST, HTML_ATTRIBUTES_WHITELIST));
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertEquals(true, isValid);
 }

 @DisplayName("Test checking if an invalid document is invalid")
 public void testIsInvalidInvalidDocument() {
 String html = "<script src='http://attacker.com/malware.js'>";
 Document dirtyDocument = Jsoup.parseBodyFragment(html);
 Cleaner cleaner = new Cleaner(Whitelist.simpleText(HTML_TAGS_WHITELIST, HTML_ATTRIBUTES_WHITELIST));
 boolean isValid = cleaner.isValid(dirtyDocument);
 assertEquals(false, isValid);
 }
}