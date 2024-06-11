import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

    @Test
    public void testConstructor() {
        Whitelist whitelist = null; // Test for null whitelist
        try {
            Cleaner cleaner = new Cleaner(whitelist);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Whitelist whitelist2 = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist2);
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanMethod() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body></body></html>"); // Test with empty input
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body></body></html>", cleanDocument.html());

        dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>"); // Test with XSS
        cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body></body></html>", cleanDocument.html());

        dirtyDocument = Jsoup.parse("<html><body><h1><script>alert('XSS')</script></h1></body></html>"); // Test with deep nesting
        cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());

        dirtyDocument = Jsoup.parse("<html><body></body></html>"); // Test with valid input
        cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body></body></html>", cleanDocument.html());

        dirtyDocument = Jsoup.parse("<html><body><h1 class='disallowed'></h1></body></html>"); // Test with disallowed tag
        cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValidMethod() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));

        dirtyDocument = Jsoup.parse("<html><body><h1><script>alert('XSS')</script></h1></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));

        dirtyDocument = Jsoup.parse("<html><body><h1 class='disallowed'></h1></body></html>");
        assertFalse(cleaner.isValid(dirtyDocument));

        dirtyDocument = Jsoup.parse("<html><body></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopySafeNodesWithNullRoot() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        Element destination = new Element("div");
        cleaner.copySafeNodes(null, destination);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateSafeElementWithNullSourceEl() {
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        Element sourceEl = null;
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
    }

    // Add test cases for other methods
}