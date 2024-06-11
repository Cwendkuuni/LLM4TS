import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CleanerTest {

    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = new Whitelist(); // assume Whitelist is properly initialized
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanerWithNullWhitelist() {
        assertThrows(NullPointerException.class, () -> new Cleaner(null));
    }

    @Test
    public void testCleanerWithEmptyDocument() {
        Document emptyDocument = Document.createEmpty();
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document cleaned = cleaner.clean(emptyDocument);
        assertEquals(emptyDocument, cleaned);
    }

    @Test
    public void testCleanerWithMalformedHTML() {
        String malformedHTML = "<html><body><script>alert('XSS')</script></body>";
        Document malformedDocument = Jsoup.parse(malformedHTML);
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document cleaned = cleaner.clean(malformedDocument);
        // assert that all malicious code has been removed
        assertTrue(cleaned.body().children().isEmpty());
    }

    @Test
    public void testCleanerWithDisallowedTags() {
        String disallowedHTML = "<html><body><script>alert('XSS')</script><script tag='evil'/></body></html>";
        Document disallowedDocument = Jsoup.parse(disallowedHTML);
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document cleaned = cleaner.clean(disallowedDocument);
        // assert that disallowed tags have been removed
        assertTrue(cleaned.body().select("script[tag~='evil']").isEmpty());
    }

    @Test
    public void testCleanerWithDisallowedAttributes() {
        String disallowedHTML = "<html><body><div onclick='alert(\"XSS\")'></div></body></html>";
        Document disallowedDocument = Jsoup.parse(disallowedHTML);
        Cleaner cleaner = new Cleaner(new Whitelist());
        Document cleaned = cleaner.clean(disallowedDocument);
        // assert that disallowed attributes have been removed
        assertTrue(cleaned.body().select("div[onclick]").isEmpty());
    }

    @Test
    public void testCleanerisValidWithValidDocument() {
        String validHTML = "<html><body><p>Hello World</p></body></html>";
        Document validDocument = Jsoup.parse(validHTML);
        Cleaner cleaner = new Cleaner(new Whitelist());
        boolean isValid = cleaner.isValid(validDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCleanerisValidWithInvalidDocument() {
        String invalidHTML = "<html><body><script>alert('XSS')</script></body></html>";
        Document invalidDocument = Jsoup.parse(invalidHTML);
        Cleaner cleaner = new Cleaner(new Whitelist());
        boolean isValid = cleaner.isValid(invalidDocument);
        assertFalse(isValid);
    }
}