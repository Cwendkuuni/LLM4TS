import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist(); // Create a default whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        String dirtyHtml = "<html><body><h1 id='title'>Hello World</h1><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertFalse(cleanDocument.body().hasMetaElements()); // Check if there are no meta elements in the clean document
        assertFalse(cleanDocument.body().hasScripts()); // Check if there are no scripts in the clean document
    }
}