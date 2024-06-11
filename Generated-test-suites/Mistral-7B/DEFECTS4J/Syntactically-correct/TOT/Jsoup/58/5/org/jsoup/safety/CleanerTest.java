import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private Element dirtyHtml;
 private Document dirtyDocument;
 private Document cleanDocument;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 dirtyHtml = Jsoup.parse("<html><body><script>alert('XSS');</script><h1 id='title'>Hello</h1></body></html>");
 dirtyDocument = Jsoup.parseBodyFragment(dirtyHtml.html());
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 cleanDocument = cleaner.clean(dirtyDocument);
 }

 @Test
 public void testCleaner_ValidHtml() {
 Element validHtml = Jsoup.parse("<html><body><h1>Hello</h1></body></html>");
 Document validDocument = Jsoup.parseBodyFragment(validHtml.html());
 Document cleanValidDocument = cleaner.clean(validDocument);

 assertNotNull(cleanValidDocument);
 assertFalse(cleanValidDocument.body().child(0).toString().contains("script"));
 }

 @Test
 public void testCleaner_InvalidHtml_WithScriptTag() {
 assertFalse(cleanDocument.body().child(0).toString().contains("script"));
 }

 @Test
 public void testCleaner_InvalidHtml_WithScriptAttribute() {
 Element invalidHtmlWithScriptAttribute = Jsoup.parse("<html><body><div onclick='alert(\"XSS\")'></div></body></html>");
 Document invalidDocumentWithScriptAttribute = Jsoup.parseBodyFragment(invalidHtmlWithScriptAttribute.html());
 Document cleanInvalidDocumentWithScriptAttribute = cleaner.clean(invalidDocumentWithScriptAttribute);

 assertNotNull(cleanInvalidDocumentWithScriptAttribute);
 assertFalse(cleanInvalidDocumentWithScriptAttribute.body().child(0).toString().contains("onclick"));
 }

 @Test(expected = NullPointerException.class)
 public void testCleaner_NullDocument() {
 cleaner.clean(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleaner_EmptyDocument() {
 Document emptyDocument = Document.create();
 cleaner.clean(emptyDocument);
 }

 @Test
 public void testCleaner_IsValid_ValidHtml() {
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleaner_IsValid_InvalidHtml() {
 assertFalse(cleaner.isValid(Jsoup.parseBodyFragment("<html><body><script>alert('XSS');</script></body></html>").html()));
 }

 @Test
 public void testCleaner_IsValid_EmptyDocument() {
 Document emptyDocument = Document.create();
 assertFalse(cleaner.isValid(emptyDocument));
 }

}