import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testCleaner_ValidInput() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p>Valid Input: Hello World!</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><p>Hello World!</p></body></html>", cleanDocument.html());
    }

    @Test
    public void testCleaner_InvalidInput() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS Attack!');</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.outputContent().isEmpty());
    }

    @Test
    public void testCleaner_IsValid_ValidInput() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><p>Valid Input: Hello World!</p></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCleaner_IsValid_InvalidInput() {
        Whitelist whitelist = Whitelist.simpleTextAndHtml();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS Attack!');</script></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }
}