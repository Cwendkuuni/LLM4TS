import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

 private static final String HTML_WITH_ALLOWED_TAGS = "" +
         "<html><body>" +
         "<h1>Allowed tag 1</h1>" +
         "<p>Allowed tag 2 with attribute 'data-custom-attribute': value='custom value'</p>" +
         "</body></html>";

 private static final String HTML_WITH_DISALLOWED_TAG = "" +
         "<html><body>" +
         "<script>alert('XSS attack');</script>" +
         "</body></html>";

 private static final String HTML_WITH_DISALLOWED_ATTRIBUTE = "" +
         "<html><body>" +
         "<p id='vulnerable'>Attack me: <input type='text' name='password' value='secret'></p>" +
         "</body></html>";

 private static final String HTML_WITH_MIXED_ALLOWED_AND_DISALLOWED = "" +
         "<html><body>" +
         "<h1>Allowed tag 1</h1>" +
         "<p>Allowed tag 2 with attribute 'data-custom-attribute': value='custom value'</p>" +
         "<script>alert('XSS attack');</script>" +
         "</body></html>";

 @Test
 public void testCleanDocumentWithAllowedTags() {
 Document dirtyDocument = Jsoup.parse(HTML_WITH_ALLOWED_TAGS);
 Whitelist whitelist = Whitelist.relaxed();
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Assertions:
 Assertions.assertEquals(HTML_WITH_ALLOWED_TAGS, cleanDocument.html(), "Cleaned document should be equal to the original document.");
 }

 @Test
 public void testInvalidDocumentWithDisallowedTag() {
 Document dirtyDocument = Jsoup.parse(HTML_WITH_DISALLOWED_TAG);
 Whitelist whitelist = Whitelist.relaxed();
 Cleaner cleaner = new Cleaner(whitelist);

 // Assertions:
 Assertions.assertFalse(cleaner.isValid(dirtyDocument), "Invalid document should not be valid.");
 }

 @Test
 public void testInvalidDocumentWithDisallowedAttribute() {
 Document dirtyDocument = Jsoup.parse(HTML_WITH_DISALLOWED_ATTRIBUTE);
 Whitelist whitelist = Whitelist.relaxed();
 Cleaner cleaner = new Cleaner(whitelist);

 // Assertions:
 Assertions.assertFalse(cleaner.isValid(dirtyDocument), "Invalid document should not be valid.");
 }

 @Test
 public void testCleanDocumentWithMixedAllowedAndDisallowed() {
 Document dirtyDocument = Jsoup.parse(HTML_WITH_MIXED_ALLOWED_AND_DISALLOWED);
 Whitelist whitelist = Whitelist.relaxed();
 Cleaner cleaner = new Cleaner(whitelist);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Assertions:
 Assertions.assertEquals(
         "<html><body><h1>Allowed tag 1</h1><p>Allowed tag 2 with attribute 'data-custom-attribute': value='custom value'</p></body></html>",
         cleanDocument.html(), "Cleaned document should only contain allowed tags and attributes.");

 // Check that the script tag was removed:
 Parser parser = new Parser();
 Document scriptOnlyDocument = parser.parseFromString("<script>alert('XSS attack');</script>");
 Assertions.assertFalse(cleaner.isValid(scriptOnlyDocument), "Invalid script-only document should not be valid.");
 }

}