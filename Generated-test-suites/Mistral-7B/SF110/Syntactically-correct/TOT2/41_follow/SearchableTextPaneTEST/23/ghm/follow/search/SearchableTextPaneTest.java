import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import ghm.follow.search.SearchEngine;
import ghm.follow.search.SearchableTextPane;
import ghm.follow.search.WordResult;
import ghm.follow.search.LineResult;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SearchableTextPaneTest {
    @org.junit.Test
    public void testHighlight() throws Exception {
        final String term = "test";
        final int flags = 0;
        final SearchableTextPane textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 4);
        final SearchEngine searchEngine = mock(SearchEngine.class);
        textPane.setSearchEngine(searchEngine);

        when(searchEngine.search(term, anyString())).thenReturn(Collections.singletonList(new WordResult(10, 15, term.length())));

        final List<LineResult> lineResults = textPane.highlight(term, flags);
        assertEquals(1, lineResults.size());
        final LineResult lineResult = lineResults.get(0);
        assertNotNull(lineResult);
        assertEquals(1, lineResult.getLineNumber());
        assertTrue(Arrays.asList(lineResult.getWordResults()).get(0).term.equals(term));

        verify(searchEngine).search(term, anyString());
        verifyZeroInteractions(textPane.getDocument());
        verify(textPane.getHighlighter()).addHighlight(10, 15 + term.length(), argThat(h -> h instanceof DefaultHighlightPainter && h.getPaint().equals(Color.YELLOW)));
    }
}