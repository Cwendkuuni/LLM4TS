import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

 private static final String TEST_HTML = "<html><head><title>Test</title></head><body><div id='test' name='test' class='test' onfocus='alert(\"Focus\")'><script>alert('Script');</script><h1>Hello World!</h1></div></body></html>";

 @Test
 public void testCleaning() {
 Whitelist whitelist = Whitelist.relaxed();
 Cleaner cleaner = new Cleaner(whitelist);

 Document dirtyDocument = Jsoup.parse(TEST_HTML);
 Document cleanDocument = cleaner.clean(dirtyDocument);

 // Test that the original document is not modified
 Assert.assertNotSame(dirtyDocument, cleanDocument);

 // Test that only safe elements and attributes are present in the cleaned document
 Assert.assertEquals("<html><body><div id='test' class='test'><h1>Hello World!</h1></div></body></html>", cleanDocument.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullWhitelist() {
 // Test that a Cleaner cannot be created with a null Whitelist
 Cleaner cleaner = new Cleaner(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullDocument() {
 // Test that a Cleaner throws a NullPointerException when passed a null document
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 cleaner.clean(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullWhitelistTag() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 // Test that a Cleaner throws a NullPointerException when passed a null tag name
 cleaner.whitelist.isSafeTag(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullWhitelistAttribute() {
 Cleaner cleaner = new Cleaner(Whitelist.relaxed());
 // Test that a Cleaner throws a NullPointerException when passed a null tag, element, and/or attribute
 cleaner.whitelist.isSafeAttribute(null, null, null);
 }

 @Test
 public void testInvalidDocument() {
 Whitelist whitelist = Whitelist.relaxed();
 Cleaner cleaner = new Cleaner(whitelist);

 Document invalidDocument = Jsoup.parse("<html><script>alert('Script');</script>");
 boolean isValid = cleaner.isValid(invalidDocument);

 // Test that an invalid document is still cleaned successfully, but is considered invalid
 Assert.assertFalse(isValid);
 }
}