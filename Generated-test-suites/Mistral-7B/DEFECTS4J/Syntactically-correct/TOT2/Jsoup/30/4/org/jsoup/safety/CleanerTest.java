import org.junit.Test;
import org.jsoup.Jsoup;
import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {
    @Test
    public void testCleanerInitialization() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><body><h1></h1></body></html>", cleanDocument.body().html());
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));
    }
}