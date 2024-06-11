import org.junit.Assert;
import org.junit.Test;

public class SearchableTextPaneTest {
    // 

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 12), 8);
        Assert.assertTrue(textPane.getScrollableTracksViewportWidth());
    }

    // 
}