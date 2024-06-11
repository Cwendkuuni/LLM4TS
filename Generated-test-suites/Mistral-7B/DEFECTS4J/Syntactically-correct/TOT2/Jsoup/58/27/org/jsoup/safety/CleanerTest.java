import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {
    @Test
    public void testCleanMethod() {
        // Given
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document dirtyDocument = Parser.parseBodyFragment(html);

        // When
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Then
        assertNotNull(cleanDocument);
    }
}