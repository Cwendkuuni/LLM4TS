import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class SearchableTextPaneTest {

    @Test
    public void testSearchableTextPaneConstruction() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
        assertNotNull(searchableTextPane);
    }

    @Test
    public void testGetScrollableTracksViewportWidthTrue() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertTrue(result);
    }

    @Test
    public void testGetScrollableTracksViewportWidthFalse() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
        JFrame frame = new JFrame();
        searchableTextPane.setBounds(10, 10, 100, 100);
        frame.getContentPane().add(searchableTextPane);
        frame.pack();
        boolean result = searchableTextPane.getScrollableTracksViewportWidth();
        assertFalse(result);
    }

    // Add tests for other methods here...
}