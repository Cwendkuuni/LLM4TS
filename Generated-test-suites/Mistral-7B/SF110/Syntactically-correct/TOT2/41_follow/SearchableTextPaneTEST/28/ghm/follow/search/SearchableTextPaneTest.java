import static org.junit.Assert.*; // for assert statements
import ghm.follow.search.SearchableTextPane;
import ghm.follow.search.SearchEngine;
import ghm.follow.search.LineResult;
import ghm.follow.search.WordResult;

public class SearchableTextPaneTest {

    @Test
    public void testHighlight() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 8);
        String term = "test";
        int flags = 0; // default flags

        String text = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.";
        SearchEngine searchEngine = new SearchEngine(flags);
        List<WordResult> searchResults = searchEngine.search(term, text);

        List<LineResult> lineResults = searchableTextPane.highlight(term, flags);
        assertEquals(1, lineResults.size());

        LineResult lineResult = lineResults.get(0);
        assertEquals(1, lineResult.getWordResults().size());
        WordResult wordResult = lineResult.getWordResults().get(0);
        assertEquals(term, wordResult.getWord());
    }
}