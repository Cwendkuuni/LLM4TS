import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import static org.junit.Assert.*;

public class CleanerTest {

    // ... (Expert 1's basic test cases from above)

    // ... (Expert 1's test cases for edge cases and exceptions from above)

    @Test
    public void testNullWhitelist() {
        // ...
    }

    @Test
    public void testEmptyWhitelist() {
        // ...
    }

    @Test
    public void testMalformedHtmlInput() {
        // ...
    }

    @Test
    public void testMissingBodyTag() {
        // ...
    }

    @Test
    public void testInvalidTagWithAllowedAttributes() {
        // ...
    }

    @Test
    public void testInvalidTagWithDisallowedAttributes() {
        // ...
    }

    @Test
    public void testMalformedAttributeValue() {
        // ...
    }

    @Test
    public void testNestedElementsWithDisallowedTags() {
        // ...
    }

    @Test
    public void testElementsWithUnenforcedAttributes() {
        // ...
    }

    @Test
    public void testElementsWithUnknownTags() {
        Whitelist whitelist = Whitelist.simpleText();
        Document dirtyDocument = Document.parse("<html><body><x></x></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body></body></html>", cleanDocument.html());
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testEmptyInput() {
        Whitelist whitelist = Whitelist.simpleText();
        Document dirtyDocument = Document.parse("<html></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html></html>", cleanDocument.html());
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testWhitespaceOnlyInput() {
        Whitelist whitelist = Whitelist.simpleText();
        Document dirtyDocument = Document.parse("<html><body></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body></body></html>", cleanDocument.html());
        assertTrue(cleaner.isValid(dirtyDocument));
    }
}