import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Cleaner class.
 */
public class CleanerTest {

    /**
     * Test constructor with valid whitelist.
     */
    @Test
    public void testConstructor() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
    }

    /**
     * Test clean method with valid input.
     */
    @Test
    public void testCleanValidInput() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().text().equals("Hello World"));
    }

    /**
     * Test clean method with invalid input.
     */
    @Test
    public void testCleanInvalidInput() {
        String html = "<html><body><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().children().size() == 0);
    }

    /**
     * Test isValid method with valid input.
     */
    @Test
    public void testIsValidValidInput() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    /**
     * Test isValid method with invalid input.
     */
    @Test
    public void testIsValidInvalidInput() {
        String html = "<html><body><script>alert('XSS');</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Cleaner cleaner = new Cleaner(Whitelist.relaxed());

        assertFalse(cleaner.isValid(dirtyDocument));
    }
}