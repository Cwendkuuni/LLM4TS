import static org.junit.Assert.*;
import ghm.follow.search.SearchableTextPane;
import org.junit.Test;

public class SearchableTextPaneTest {

    @Test
    public void testSearchableTextPaneConstructor() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 12), 8);
        assertNotNull(searchableTextPane);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 12), 8);
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    // Add tests for other methods here...
}