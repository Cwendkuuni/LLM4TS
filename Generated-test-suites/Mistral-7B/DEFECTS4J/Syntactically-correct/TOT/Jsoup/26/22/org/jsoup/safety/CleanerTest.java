import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Whitelist;
import org.jsoup.safety.Cleaner;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class CleanerTest {

 private Whitelist allowedTags;
 private Cleaner cleaner;

 @Before
 public void setup() {
 allowedTags = Whitelist.simpleTagNames(); // using a simple whitelist for testing
 cleaner = new Cleaner(allowedTags);
 }

 @Test
 public void testCleanMethod() throws IOException {
 Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script><h1>Safe tag: <b>Hello</b></h1></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);

 assertEquals("<html><body><h1>Safe tag: <b>Hello</b></h1></body></html>", cleanDocument.body().html()); // expected output
 assertNull(cleanDocument.body().select("script").first()); // script tag is removed
 }

 @Test
 public void testIsValidMethod() throws IOException {
 Document invalidDocument = Jsoup.parse("<html><body><script>alert('XSS')</script><h1>Safe tag: <b>Hello</b></h1></body></html>");
 boolean isValid = cleaner.isValid(invalidDocument);

 assertFalse(isValid); // script tag is not allowed
 }

 @Test
 public void testIsValidMethodWithAllowedScriptTag() throws IOException {
 Document invalidDocument = Jsoup.parse("<html><body><script src=\"script.js\"></script><h1>Safe tag: <b>Hello</b></h1></body></html>");
 Whitelist whitelist = Whitelist.relaxedModel().addTags("script"); // adding script tag to whitelist
 Cleaner cleaner = new Cleaner(whitelist);
 boolean isValid = cleaner.isValid(invalidDocument);

 assertTrue(isValid); // script tag is now allowed
 }

 @Test(expected = NullPointerException.class)
 public void testCleanMethodWithNullDocument() {
 cleaner.clean(null);
 }

 @Test(expected = NullPointerException.class)
 public void testIsValidMethodWithNullDocument() {
 cleaner.isValid(null);
 }
}