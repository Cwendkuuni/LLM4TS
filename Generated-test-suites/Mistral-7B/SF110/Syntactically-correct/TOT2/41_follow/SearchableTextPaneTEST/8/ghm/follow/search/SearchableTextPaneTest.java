import org.junit.Test;
import static org.junit.Assert.*;

public class SearchableTextPaneTest {
    @Test
    public void testSearchableTextPaneConstructor() {
        Font font = new Font("Serif", Font.BOLD, 12);
        SearchableTextPane textPane = new SearchableTextPane(font, 8);
        assertTrue(textPane instanceof SearchableTextPane);
        assertEquals(font, textPane.getFont());
        assertEquals(8, textPane.getTabSize());
    }
}