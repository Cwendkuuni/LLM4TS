import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

    @org.junit.Test
    public void testCleanerConstruction() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @org.junit.Test
    public void testCleanMethod() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("", cleanDocument.head().html(), "Head should be empty.");
        Element h1 = cleanDocument.selectFirst("h1");
        assertNotNull(h1);
        assertEquals("Hello World", h1.text());
    }

    @org.junit.Test
    public void testIsValidMethod() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);

        html = "<html><body><h1>Hello World</h1><script>alert('XSS')</script></body></html>";
        dirtyDocument = Jsoup.parse(html);
        isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }
}