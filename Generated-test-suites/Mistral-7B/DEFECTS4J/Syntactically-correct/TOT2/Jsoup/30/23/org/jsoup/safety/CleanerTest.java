import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

public class CleanerTest {
    private Document dirtyDocument;
    private Document cleanDocument;
    private Cleaner cleaner;

    @Before
    public void setup() {
        dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        cleanDocument = Jsoup.parse("<html><body></body></html>");
        cleaner = new Cleaner(new Whitelist());
    }

    @Test
    public void testCleanMethod() {
        cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
        assertNotSame(dirtyDocument, cleanDocument);
    }
}