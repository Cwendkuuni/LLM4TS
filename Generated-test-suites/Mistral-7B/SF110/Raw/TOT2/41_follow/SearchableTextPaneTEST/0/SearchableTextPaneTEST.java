import static org.junit.Assert.*;
import ghm.follow.search.*;

public class SearchableTextPaneTest {

    @Test
    public void testSearchableTextPaneConstructor() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane searchableTextPane = new SearchableTextPane(font, 8);
        assertNotNull(searchableTextPane);
    }

    @Test(expected = NullPointerException.class)
    public void testSearchableTextPaneConstructorWithNullFont() {
        Font font = null;
        SearchableTextPane searchableTextPane = new SearchableTextPane(font, 8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchableTextPaneConstructorWithNegativeTabSize() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        SearchableTextPane searchableTextPane = new SearchableTextPane(font, -1);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 8);
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 8);
        List<String> terms = Arrays.asList("apple", "banana", "cherry");
        for (String term : terms) {
            List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
            assertTrue(!lineResults.isEmpty());
        }
    }

    @Test
    public void testRemoveHighlights() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 8);
        List<String> terms = Arrays.asList("apple", "banana", "cherry");
        searchableTextPane.highlight("apple", 0);
        searchableTextPane.removeHighlights();
        assertFalse(searchableTextPane.hasFocus());
    }

    @Test
    public void testSearch() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 8);
        String text = "The quick brown fox jumps over the lazy dog.";
        searchableTextPane.setText(text);
        int index = searchableTextPane.search("fox");
        assertEquals(4, index);
        index = searchableTextPane.search("lazy");
        assertEquals(10, index);
    }

    @Test(expected = NullPointerException.class)
    public void testSearchWithNullTerm() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 8);
        searchableTextPane.search(null);
    }

    @Test
    public void testSearchWithEmptyTerm() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 8);
        String text = "The quick brown fox jumps over the lazy dog.";
        searchableTextPane.setText(text);
        int index = searchableTextPane.search("");
        assertEquals(-1, index);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchWithNegativeStartPosition() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 8);
        String text = "The quick brown fox jumps over the lazy dog.";
        searchableTextPane.setText(text);
        int index = searchableTextPane.search("fox", -1);
    }

    @Test
    public void testConvertWords2Lines() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 8);
        String text = "The quick brown fox jumps over the lazy dog.";
        List<WordResult> words = Arrays.asList(
            new WordResult(0, 4, "The"),
            new WordResult(5, 7, "quick"),
            new WordResult(8, 11, "brown"),
            new WordResult(12, 17, "fox"),
            new WordResult(18, 22, "jumps"),
            new WordResult(23, 28, "over"),
            new WordResult(29, 35, "the"),
            new WordResult(36, 42, "lazy"),
            new WordResult(43, 48, "dog.")
        );
        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
        assertEquals(4, lineResults.size());
        LineResult lineResult0 = lineResults.get(0);
        assertEquals(0, lineResult0.getLineNumber());
        assertEquals(0, lineResult0.getStartOffset());
        assertEquals(14, lineResult0.getEndOffset());

        LineResult lineResult1 = lineResults.get(1);
        assertEquals(1, lineResult1.getLineNumber());
        assertEquals(15, lineResult1.getStartOffset());
        assertEquals(19, lineResult1.getEndOffset());

        LineResult lineResult2 = lineResults.get(2);
        assertEquals(2, lineResult2.getLineNumber());
        assertEquals(20, lineResult2.getStartOffset());
        assertEquals(24, lineResult2.getEndOffset());

        LineResult lineResult3 = lineResults.get(3);
        assertEquals(3, lineResult3.getLineNumber());
        assertEquals(36, lineResult3.getStartOffset());
        assertEquals(48, lineResult3.getEndOffset());
    }
}