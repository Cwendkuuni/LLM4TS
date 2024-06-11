import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;

public class CleanerTest {

    @Test
    public void testCleanMethod() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><p>Hello World</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanedDocument);
    }
}