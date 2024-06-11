import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Document;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CleanerTest {
    @Test
    public void testCleanMethod() {
        Whitelist whitelist = new Whitelist(); // Create a valid whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.createFromString("<html><body><script>alert('XSS')</script></body></html>"); // Dirty document with XSS attack
        Document cleanedDocument = cleaner.clean(dirtyDocument); // Call the method under test

        assertNotNull(cleanedDocument); // Assert that the method returns a non-null value
    }
}