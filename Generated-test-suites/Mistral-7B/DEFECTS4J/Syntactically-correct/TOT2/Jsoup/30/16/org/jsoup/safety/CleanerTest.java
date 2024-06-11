import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Whitelist;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CleanerTest {
    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = Whitelist.simple();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);

        assertNull(new Cleaner(null));
    }

    @Test
    public void testCleanMethod() {
        Whitelist whitelist = Whitelist.simple();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<p>Test<script>alert('XSS')</script></p>");
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanedDocument);
        assertNotNull(cleanedDocument.body());
        assertFalse(cleanedDocument.body().text().contains("alert('XSS')"));

        assertNull(cleaner.clean(null));
        Document emptyBodyDocument = Jsoup.parse("<html></html>");
        assertNotNull(cleaner.clean(emptyBodyDocument));
        assertNotNull(cleaner.clean(emptyBodyDocument).body());
        assertEmpty(cleaner.clean(emptyBodyDocument).body().text());
    }

    @Test
    public void testIsValidMethod() {
        Whitelist whitelist = Whitelist.simple();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<p><script>alert('XSS')</script></p>");
        assertFalse(cleaner.isValid(dirtyDocument));

        Document invalidTagDocument = Jsoup.parse("<p><marquee>Test</marquee></p>");
        assertFalse(cleaner.isValid(invalidTagDocument));

        Document allowedTagDocument = Jsoup.parse("<p><button>Test</button></p>");
        assertTrue(cleaner.isValid(allowedTagDocument));

        Document invalidAttributeDocument = Jsoup.parse("<p><img src='test.com' onload='alert('XSS')'></p>");
        assertFalse(cleaner.isValid(invalidAttributeDocument));
    }

    // Test cases for other methods can be added here
}