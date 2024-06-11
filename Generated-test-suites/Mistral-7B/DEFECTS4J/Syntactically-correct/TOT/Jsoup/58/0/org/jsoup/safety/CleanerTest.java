import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

 private static final String SAMPLE_HTML = "<html><head><title>Test</title></head><body><h1 id=\"title\">Hello World!</h1>" +
         "<p>This is a <script>malicious code</script> test.</p></body></html>";

 @Test
 public void testCleanMethod() {
  Whitelist whitelist = Whitelist.simpleText();
  Cleaner cleaner = new Cleaner(whitelist);

  Document dirtyDocument = Jsoup.parse(SAMPLE_HTML);
  Document cleanDocument = cleaner.clean(dirtyDocument);

  // Test empty document
  Assert.assertNotNull(cleanDocument);
  Assert.assertEquals(0, cleanDocument.body().children().size());

  // Test valid document
  String expectedCleanHtml = "<html><head></head><body><h1 id=\"title\">Hello World!</h1></body></html>";
  Assert.assertEquals(expectedCleanHtml, cleanDocument.body().html());

  // Test invalid document with script tag
  Parser parser = new Parser();
  Document invalidDocument = null;
  try {
   invalidDocument = parser.parseBodyFragment(SAMPLE_HTML, SAMPLE_HTML.length());
  } catch (ParseError e) {
   // Expected, as the input is invalid
  }

  cleaner = new Cleaner(Whitelist.simpleText());
  Document cleanedInvalidDocument = cleaner.clean(invalidDocument);

  // Test cleaned invalid document
  Assert.assertEquals(expectedCleanHtml, cleanedInvalidDocument.body().html());
 }

 @Test
 public void testIsValidMethod() {
  Whitelist whitelist = Whitelist.simpleText();
  Cleaner cleaner = new Cleaner(whitelist);

  Document validDocument = Jsoup.parse("<html><body><h1>Valid</h1></body></html>");
  Assert.assertTrue(cleaner.isValid(validDocument));

  Document invalidDocument = Jsoup.parse("<html><body><h1>Valid<script>malicious code</script></h1></body></html>");
  Assert.assertFalse(cleaner.isValid(invalidDocument));
 }

 @Test(expected = NullPointerException.class)
 public void testCleanerNullConstructor() {
  new Cleaner(null);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullDocument() {
  Cleaner cleaner = new Cleaner(Whitelist.simpleText());
  cleaner.clean(null);
 }

}