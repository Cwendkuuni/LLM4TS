import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class TreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        String input = "Some input string";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        TreeBuilder treeBuilder = new TreeBuilder();

        treeBuilder.initialiseParse(input, baseUri, errors);

        // Basic test
        assertNotNull(treeBuilder.doc);
        assertEquals(baseUri, treeBuilder.baseUri);

        // Edge case 1: Empty input string
        treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse("", baseUri, errors);
        assertNull(treeBuilder.doc);

        // Edge case 2: Null baseUri
        treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse(input, null, errors);
        fail("InitialiseParse method should not accept null baseUri");
    }
}