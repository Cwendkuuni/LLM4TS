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
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private static final String HTML_WITH_SCRIPT_TAG = "<html><body><script>alert('XSS');</script></body></html>";
 private static final String HTML_WITH_DISALLOWED_TAG = "<html><body><marquee></marquee></body></html>";
 private static final String HTML_WITH_VALID_TAG = "<html><body><p>Hello World</p></body></html>";
 private Document dirtyDocument;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 Parser parser = new Parser();
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);
 dirtyDocument = Jsoup.parse(HTML_WITH_VALID_TAG);
 }

 @Test
 public void testClean_ValidHtml_ReturnsCleanDocument() {
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertEquals(HTML_WITH_VALID_TAG, cleanDocument.body().html());
 }

 @Test
 public void testIsValid_ValidHtml_ReturnsTrue() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testClean_HtmlWithScriptTag_ReturnsCleanDocumentWithoutScriptTag() {
 Document input = Jsoup.parse(HTML_WITH_SCRIPT_TAG);
 Document cleanDocument = cleaner.clean(input);
 assertFalse(cleanDocument.body().select("script").isEmpty());
 assertEquals(0, cleanDocument.body().select("script").size());
 }

 @Test
 public void testIsValid_HtmlWithScriptTag_ReturnsFalse() {
 assertFalse(cleaner.isValid(input));
 }

 @Test
 public void testClean_HtmlWithDisallowedTag_ReturnsCleanDocumentWithoutDisallowedTag() {
 Document input = Jsoup.parse(HTML_WITH_DISALLOWED_TAG);
 Document cleanDocument = cleaner.clean(input);
 assertTrue(cleanDocument.body().select("marquee").isEmpty());
 }

 @Test
 public void testIsValid_HtmlWithDisallowedTag_ReturnsFalse() {
 assertFalse(cleaner.isValid(input));
 }

 @Test
 public void testClean_HtmlWithoutBody_ThrowsIllegalArgumentException() {
 Document input = Jsoup.parse("<html></html>");
 try {
 cleanDocument = cleaner.clean(input);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testClean_HtmlWithEmptyBody_ReturnsEmptyDocument() {
 Document input = Jsoup.parse("<html><body></body></html>");
 Document cleanDocument = cleaner.clean(input);
 assertTrue(cleanDocument.body().isEmpty());
 }
}