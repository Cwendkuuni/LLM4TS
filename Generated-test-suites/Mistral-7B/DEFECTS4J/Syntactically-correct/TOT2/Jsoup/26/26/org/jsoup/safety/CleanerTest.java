import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Assertions.assertNotNull(cleaner);
    }

    @Test
    public void testCleanMethod() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><h1 id='title'>Hello</h1></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Assertions.assertNotNull(cleanDocument);
        Assertions.assertEquals("<html><body><h1></h1></body></html>", cleanDocument.body().html());
    }

    // tests for other methods
}