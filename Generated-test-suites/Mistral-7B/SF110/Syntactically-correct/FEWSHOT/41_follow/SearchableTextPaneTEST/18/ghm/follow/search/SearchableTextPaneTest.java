package ghm.follow.search;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.*;
import java.util.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

    @Test
    public void testSearchableTextPaneConstruction() {
        Font font = new Font("Serif", Font.BOLD, 14);
        SearchableTextPane searchableTextPane = new SearchableTextPane(font, 8);
        assertEquals(font, searchableTextPane.getFont());
        assertEquals(8, searchableTextPane.getTabSize());
        assertNull(searchableTextPane.getLastSearchTerm());
        assertFalse(searchableTextPane.getHighlighter().hasHighlights());
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        JTextArea textArea = mock(JTextArea.class);
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
        searchableTextPane.setParent(textArea);
        ComponentUI textComponentUI = mock(ComponentUI.class);
        when(textArea.getUI()).thenReturn(textComponentUI);
        when(textComponentUI.getPreferredSize(searchableTextPane)).thenReturn(new Dimension(200, 200));
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
        searchableTextPane.setParent(null);
        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        String text = "This text contains the term to search for.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
        searchableTextPane.setDocument(new DefaultMutableDocument());
        searchableTextPane.setText(text);
        List<WordResult> searchResults = Arrays.asList(
                new WordResult("term", 10, 15),
                new WordResult("term", 45, 48),
                new WordResult("term", 60, 63)
        );
        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(searchResults);

        searchableTextPane.highlight("term", 0);

        SearchEngine searchEngine = mock(SearchEngine.class);
        when(searchEngine.search("term", text)).thenReturn(searchResults);
        assertEquals(lineResults.size(), 3);
        for (LineResult lineResult : lineResults) {
            assertEquals(1, lineResult.getWordResults().size());
            WordResult wordResult = lineResult.getWordResults().get(0);
            assertEquals(10, wordResult.start);
            assertEquals(15, wordResult.end);
            assertEquals(1, lineResult.getLineNumber());
            assertEquals(5, lineResult.getLineStartOffset());
            assertEquals(55, lineResult.getLineEndOffset());
        }
    }

    @Test
    public void testSearch() {
        String text = "This text contains the term to search for.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
        searchableTextPane.setDocument(new DefaultMutableDocument());
        searchableTextPane.setText(text);
        when(searchableTextPane.getDocument()).thenReturn(new DefaultMutableDocument());
        when(searchableTextPane.getDocument().getText(0, searchableTextPane.getDocument().getLength()))
                .thenReturn(text);

        int position = searchableTextPane.search("term");
        assertEquals(-1, position);

        searchableTextPane.search("term");
        searchableTextPane.search("term");
        assertEquals(10, searchableTextPane.getLastSearchPos());
        assertEquals("term", searchableTextPane.getLastSearchTerm());

        searchableTextPane.search("another term");
        assertNull(searchableTextPane.getLastSearchTerm());
    }

    @Test
    public void testSearchWithPreviousSearchTerm() {
        String text = "This text contains the term to search for.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
        searchableTextPane.setDocument(new DefaultMutableDocument());
        searchableTextPane.setText(text);
        when(searchableTextPane.getDocument()).thenReturn(new DefaultMutableDocument());
        when(searchableTextPane.getDocument().getText(0, searchableTextPane.getDocument().getLength()))
                .thenReturn(text);

        searchableTextPane.search("term");
        assertEquals(10, searchableTextPane.getLastSearchPos());
        assertEquals("term", searchableTextPane.getLastSearchTerm());

        searchableTextPane.search("another term");
        assertEquals(-1, searchableTextPane.getLastSearchPos());
        assertNull(searchableTextPane.getLastSearchTerm());

        searchableTextPane.search("term");
        assertEquals(10, searchableTextPane.getLastSearchPos());
        assertEquals("term", searchableTextPane.getLastSearchTerm());
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        String text = "Line 1: This text contains the term to search for.\nLine 2: This is another line.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
        searchableTextPane.setDocument(new DefaultMutableDocument());
        searchableTextPane.setText(text);
        ElementParagraphElement[] paragraphs = (ElementParagraphElement[]) searchableTextPane.getDocument().getElementIter().getElements();

        List<WordResult> searchResults = Arrays.asList(
                new WordResult("term", 26, 30),
                new WordResult("term", 42, 45)
        );

        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(searchResults);

        assertEquals(2, lineResults.size());

        LineResult lineResult1 = lineResults.get(0);
        assertEquals(1, lineResult1.getLineNumber());
        assertEquals(0, lineResult1.getLineStartOffset());
        assertEquals(52, lineResult1.getLineEndOffset());
        assertEquals(1, lineResult1.getWordResults().size());
        WordResult wordResult1 = lineResult1.getWordResults().get(0);
        assertEquals(26, wordResult1.start);
        assertEquals(30, wordResult1.end);

        LineResult lineResult2 = lineResults.get(1);
        assertEquals(2, lineResult2.getLineNumber());
        assertEquals(48, lineResult2.getLineStartOffset());
        assertEquals(51, lineResult2.getLineEndOffset());
        assertEquals(1, lineResult2.getWordResults().size());
        WordResult wordResult2 = lineResult2.getWordResults().get(0);
        assertEquals(42, wordResult2.start);
        assertEquals(45, wordResult2.end);
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
        String text = "Line 1: This text contains the term to search for.\nLine 2: This is another line.";
        searchableTextPane.setDocument(new DefaultMutableDocument());
        searchableTextPane.setText(text);
        ElementParagraphElement[] paragraphs = (ElementParagraphElement[]) searchableTextPane.getDocument().getElementIter().getElements();
        List<WordResult> searchResults = Arrays.asList(
                new WordResult("term", 26, 30),
                new WordResult("term", 42, 45)
        );
        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(searchResults);

        LineResult lineResult1 = lineResults.get(0);
        WordResult wordResult1 = lineResult1.getWordResults().get(0);

        int start = lineResult1.getStartOffset();
        int end = lineResult1.getEndOffset();
        int lineNumber = lineResult1.getLineNumber();

        searchableTextPane.updateWordResult(wordResult1, lineResult1);

        assertEquals(start, wordResult1.getLineStartOffset());
        assertEquals(end, wordResult1.getLineEndOffset());
        assertEquals(lineNumber, wordResult1.parent.lineNumber);
    }
}