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

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setUp() {
 whitelist = Whitelist.simple();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><div id='test' onclick='alert(\"XSS\")'><script>alert(\"XSS\")</script></div></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 assertFalse(cleanDocument.body().child(0).toString().contains("onclick"));
 assertFalse(cleanDocument.body().child(0).toString().contains("alert"));
 assertFalse(cleanDocument.body().child(1).toString().contains("script"));
 }

 @Test
 public void testIsValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><div id='test'><p>Hello World</p></div></body></html>");
 assertTrue(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testInvalidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><div id='test' onclick='alert(\"XSS\")'><script>alert(\"XSS\")</script></div></body></html>");
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleaningVisitor() {
 Parser parser = new Parser();
 Document dirtyDocument = parser.parse("<html><body><div id='test'><p>Hello <b>World</b></p></div></body></html>");
 Element expectedElement = new Element("p", null, null, null, null);
 expectedElement.text("Hello World");
 expectedElement.getByTag("b").text("World");
 expectedElement.getByTag("b").tagName("i");

 Element destination = new Element("div", null, null, null, null);
 CleaningVisitor cleaningVisitor = new Cleaner.CleaningVisitor(dirtyDocument.body(), destination);
 NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
 traversor.traverse(dirtyDocument.body());

 Element actualElement = destination.child(0);
 assertNotNull(actualElement);
 assertTrue(actualElement.tagName().equals("p"));
 assertTrue(actualElement.text().equals("Hello World"));
 assertTrue(actualElement.getByTag("i").tagName().equals("i"));
 assertTrue(actualElement.getByTag("i").text().equals("World"));
 }
}