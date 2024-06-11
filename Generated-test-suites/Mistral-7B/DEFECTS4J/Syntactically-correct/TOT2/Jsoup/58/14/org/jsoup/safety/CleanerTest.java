import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testCleanMethod() {
        Whitelist whitelist = new Whitelist(); // Assuming that you have a Whitelist object
        Cleaner cleaner = new Cleaner(whitelist);

        String dirtyHtml = "<html><head></head><body><h1>Hello World</h1><script>alert('Test')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHtml);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Test that the original document is not modified
        assertEquals(dirtyHtml, dirtyDocument.html());

        // Test that only allowed tags and attributes are present in the cleaned document
        Element root = cleanDocument.body();
        assertTrue(root.tagName().equals("body"));
        assertTrue(root.children().size() == 1);
        Element h1 = (Element) root.children().get(0);
        assertTrue(h1.tagName().equals("h1"));
        assertFalse(h1.hasAttr("script"));
    }
}