package ghm.follow.search;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.text.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SearchableTextPaneTest {

    private JTextArea textArea;
    private SearchableTextPane searchableTextPane;
    private Logger mockLogger;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        mockLogger = mock(Logger.class);
        textArea = mock(JTextArea.class);
        when(textArea.getDocument()).thenReturn(mock(Document.class));
        when(textArea.getUI()).thenReturn(mock(ComponentUI.class));
        searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 4) {
            {
                this.log = mockLogger;
            }
        };
        searchableTextPane.setBackground(Color.WHITE);
        searchableTextPane.setEditable(false);
        searchableTextPane.setText("This is a searchable text pane.");
        when(textArea.getDocument()).thenReturn(searchableTextPane.getDocument());
        when(textArea.getUI()).thenReturn(searchableTextPane.getUI());
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        final String term = "searchable";
        final int flags = 0;
        final List<WordResult> searchResults = Arrays.asList(
                new WordResult(10, 15, "searchable".length()),
                new WordResult(25, 30, "searchable".length())
        );
        final List<LineResult> lineResults = searchableTextPane.convertWords2Lines(searchResults);
        searchableTextPane.highlight(term, flags);

        final Highlighter highlighter = searchableTextPane.getHighlighter();
        int index = 0;
        for (int i = 0; i < searchResults.size(); i++) {
            final WordResult wordResult = searchResults.get(i);
            final int wordStart = wordResult.start;
            final int wordEnd = wordStart + wordResult.length();
            final int line = searchableTextPane.getLineOfOffset(wordStart);
            final Element element = Utilities.getParagraphElement(searchableTextPane, wordStart);
            final int lineStart = element.getStartOffset();
            final int lineEnd = element.getEndOffset();

            final AttributeSet attributeSet = highlighter.getHighlightPainter().getPaintAttributes();
            final BasicAttribute attribute = attributeSet.getAttribute(SimpleAttributeSet.SimpleAttributeKey.FOREGROUND);
            assertEquals(Color.YELLOW, attribute);

            final int highlightStart = searchableTextPane.getDocument().getStartOffset();
            final int highlightEnd = highlightStart + index;
            assertEquals(highlightStart + wordStart, highlightStart + searchResults.get(i).start);
            assertEquals(highlightStart + wordStart + wordResult.length(), highlightEnd);
            assertEquals(lineStart, searchableTextPane.getDocument().getCharacterElement(highlightEnd - 1).getStartOffset());
            assertEquals(lineEnd, searchableTextPane.getDocument().getCharacterElement(highlightEnd - 1).getEndOffset());
            index += wordResult.length();
        }
    }

    @Test
    public void testSearch() {
        final String term1 = "searchable";
        final String term2 = "text pane";
        final int pos1 = searchableTextPane.search(term1);
        assertEquals(10, pos1);

        final int pos2 = searchableTextPane.search(term2);
        assertEquals(25, pos2);

        final int pos3 = searchableTextPane.search(term1, 25);
        assertEquals(-1, pos3);

        final int pos4 = searchableTextPane.search(term2, 35);
        assertEquals(-1, pos4);

        searchableTextPane.search(term1);
        assertEquals(10, searchableTextPane.lastSearchPos);
        assertEquals("searchable", searchableTextPane.lastSearchTerm);

        searchableTextPane.search(term2);
        assertEquals(25, searchableTextPane.lastSearchPos);
        assertEquals("text pane", searchableTextPane.lastSearchTerm);

        searchableTextPane.search(null);
        assertNull(searchableTextPane.lastSearchTerm);
        assertNull(searchableTextPane.lastSearchPos);
    }

    @Test
    public void testSearchWithEmptyTerm() {
        searchableTextPane.search("");
        assertNull(searchableTextPane.lastSearchTerm);
        assertNull(searchableTextPane.lastSearchPos);
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        final String text = "This is a searchable text pane.";
        final List<WordResult> words = Arrays.asList(
                new WordResult(0, 1, 1, "This"),
                new WordResult(2, 6, 1, "is"),
                new WordResult(7, 11, 1, "a"),
                new WordResult(12, 18, 1, "searchable"),
                new WordResult(19, 25, 1, "text"),
                new WordResult(26, 30, 1, "pane.")
        );
        final List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);

        assertEquals(2, lineResults.size());
        final LineResult line1 = lineResults.get(0);
        assertEquals(0, line1.getLineNumber());
        assertEquals(0, line1.getLineStartOffset());
        assertEquals(18, line1.getLineEndOffset());
        final List<WordResult> wordsInLine1 = line1.getWordResults();
        assertEquals(2, wordsInLine1.size());
        assertEquals("is", wordsInLine1.get(0).getWord());
        assertEquals("a", wordsInLine1.get(1).getWord());

        final LineResult line2 = lineResults.get(1);
        assertEquals(1, line2.getLineNumber());
        assertEquals(19, line2.getLineStartOffset());
        assertEquals(30, line2.getLineEndOffset());
        final List<WordResult> wordsInLine2 = line2.getWordResults();
        assertEquals(4, wordsInLine2.size());
        assertEquals("searchable", wordsInLine2.get(0).getWord());
        assertEquals("text", wordsInLine2.get(1).getWord());
        assertEquals("pane.", wordsInLine2.get(2).getWord());
        assertEquals(".", wordsInLine2.get(3).getWord());

        final int length = text.length();
        assertTrue(line1.getLineEndOffset() + line2.getLineStartOffset() == length);
    }

    @Test
    public void testSearchWithNewTerm() throws BadLocationException {
        final String newTerm = "follow";
        final List<WordResult> searchResults = Arrays.asList(
                new WordResult(31, 33, 1, "follow")
        );
        final List<LineResult> lineResults = searchableTextPane.convertWords2Lines(searchResults);
        searchableTextPane.highlight(newTerm, 0);

        final Highlighter highlighter = searchableTextPane.getHighlighter();
        final AttributeSet attributeSet = highlighter.getHighlightPainter().getPaintAttributes();
        final BasicAttribute attribute = attributeSet.getAttribute(SimpleAttributeSet.SimpleAttributeKey.FOREGROUND);
        assertEquals(Color.YELLOW, attribute);

        final int highlightStart = searchableTextPane.getDocument().getStartOffset();
        final int highlightEnd = highlightStart + 3;
        assertEquals(31, searchableTextPane.lastSearchPos);
        assertEquals(newTerm, searchableTextPane.lastSearchTerm);

        final int line = searchableTextPane.getLineOfOffset(31);
        final Element element = Utilities.getParagraphElement(searchableTextPane, 31);
        final int lineStart = element.getStartOffset();
        final int lineEnd = element.getEndOffset();

        assertEquals(highlightStart + 31, highlightStart + searchResults.get(0).start);
        assertEquals(highlightStart + 31 + newTerm.length(), highlightEnd);
        assertEquals(lineStart, searchableTextPane.getDocument().getCharacterElement(highlightEnd - 1).getStartOffset());
        assertEquals(lineEnd, searchableTextPane.getDocument().getCharacterElement(highlightEnd - 1).getEndOffset());
    }

    @Test
    public void testRemoveHighlights() {
        final String term = "searchable";
        final int flags = 0;
        searchableTextPane.highlight(term, flags);
        searchableTextPane.removeHighlights();

        final Highlighter highlighter = searchableTextPane.getHighlighter();
        assertTrue(highlighter.getAllHighlights().isEmpty());
        assertNull(searchableTextPane.lastSearchTerm);
        assertNull(searchableTextPane.lastSearchPos);
    }
}