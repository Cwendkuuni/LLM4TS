package ghm.follow.search;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SearchableTextPaneTest {

    @Test
    public void testSearchableTextPaneConstruction() {
        Font font = new Font("Serif", Font.BOLD, 14);
        int tabSize = 4;
        SearchableTextPane searchableTextPane = new SearchableTextPane(font, tabSize);
        assertNotNull(searchableTextPane);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(searchableTextPane);
        frame.pack();
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
        searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.pack();
        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlightEmptyTerm() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
        String emptyTerm = "";
        List<LineResult> lineResults = searchableTextPane.highlight(emptyTerm, 0);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testHighlightSingleTerm() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
        String term = "test";
        List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
        assertEquals(1, lineResults.size());
        assertEquals(0, lineResults.get(0).getLineNumber());
        assertEquals(-1, lineResults.get(0).getStartOffset());
        assertEquals(-1, lineResults.get(0).getEndOffset());
    }

    @Test
    public void testHighlightMultipleTerms() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
        String term1 = "test1";
        String term2 = "test2";
        List<LineResult> lineResults = searchableTextPane.highlight(term1 + " " + term2, 0);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(0, lineResult.getLineNumber());
        List<WordResult> wordResults = lineResult.getWordResults();
        assertEquals(2, wordResults.size());
        WordResult firstWordResult = wordResults.get(0);
        assertEquals(term1.length(), firstWordResult.getLength());
        WordResult secondWordResult = wordResults.get(1);
        assertEquals(term2.length(), secondWordResult.getLength());
    }

    @Test
    public void testSearch() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
        String term = "test";
        int pos = searchableTextPane.search(term);
        assertEquals(-1, pos);
        String text = "This is a test";
        searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
        searchableTextPane.setText(text);
        pos = searchableTextPane.search(term);
        assertEquals(5, pos);
    }

    @Test
    public void testSearchPreviousTerm() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
        String term1 = "test1";
        String term2 = "test2";
        searchableTextPane.setText("This is a " + term1 + " " + term2);
        int pos1 = searchableTextPane.search(term1);
        int pos2 = searchableTextPane.search(term2);
        assertEquals(5, pos1);
        assertEquals(12, pos2);
        searchableTextPane.search(term1); // Search for term1 again
        int pos3 = searchableTextPane.search(term2);
        assertEquals(12, pos3);
    }

    @Test(expected = NullPointerException.class)
    public void testNullTermSearch() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
        searchableTextPane.search(null);
    }
}