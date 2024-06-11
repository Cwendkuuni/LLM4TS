import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 whitelist = Whitelist.simpleText();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanValidDocument() {
 Document dirtyDocument = Jsoup.parse(" <p>Valid: <b>Hello</b> </p>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<p><p/><b></b></p>", cleanDocument.body().html());
 }

 @Test
 public void testCleanInvalidDocumentWithScriptingAttack() {
 Document dirtyDocument = Jsoup.parse("<p onmouseover='alert(\"XSS\")'>Invalid: <script>alert('XSS')</script></p>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<p></p>", cleanDocument.body().html());
 }

 @Test
 public void testCleanInvalidDocumentWithInvalidTag() {
 Document dirtyDocument = Jsoup.parse("<meta invalidTag='content'>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("", cleanDocument.body().html());
 }

 @Test
 public void testCleanInvalidDocumentWithInvalidAttribute() {
 Document dirtyDocument = Jsoup.parse("<p data-xss='<script>alert(\"XSS\")'>Invalid: <b>Hello</b></p>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<p></p>", cleanDocument.body().html());
 }

 @Test
 public void testIsValidValidDocument() {
 Document dirtyDocument = Jsoup.parse("<p><b>Hello</b></p>");
 boolean valid = cleaner.isValid(dirtyDocument);

 assertTrue(valid);
 }

 @Test
 public void testIsValidInvalidDocumentWithScriptingAttack() {
 Document dirtyDocument = Jsoup.parse("<p onmouseover='alert(\"XSS\")'>Invalid: <script>alert('XSS')</script></p>");
 boolean valid = cleaner.isValid(dirtyDocument);

 assertFalse(valid);
 }

 @Test
 public void testIsValidInvalidDocumentWithInvalidTag() {
 Document dirtyDocument = Jsoup.parse("<meta invalidTag='content'>");
 boolean valid = cleaner.isValid(dirtyDocument);

 assertFalse(valid);
 }

 @Test
 public void testIsValidInvalidDocumentWithInvalidAttribute() {
 Document dirtyDocument = Jsoup.parse("<p data-xss='<script>alert(\"XSS\")'>Invalid: <b>Hello</b></p>");
 boolean valid = cleaner.isValid(dirtyDocument);

 assertFalse(valid);
 }
}