import static org.junit.Assert.*;
import ghm.follow.search.SearchableTextPane;

public class SearchableTextPaneTest {

    @Test
    public void testSearchableTextPaneConstructor() {
        final Font font = new Font("Serif", Font.BOLD, 12);
        final SearchableTextPane textPane = new SearchableTextPane(font, 4);
        assertNotNull(textPane);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        final SearchableTextPane textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 4);
        assertTrue(textPane.getScrollableTracksViewportWidth());
    }

    // Add other tests for each method
}