package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Searchable;
import javax.swing.text.SearchPattern;

public class SearchableTextPaneTest {

    private SearchableTextPane textPane;
    private DefaultHighlighter highlighter;
    private List<LineResult> lineResults;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
        highlighter = (DefaultHighlighter) textPane.getHighlighter();
        lineResults = new ArrayList<>();
    }

    @Test
    public void testSearchableTextPaneConstructor() {
        assertNotNull(textPane);
        assertEquals(new Font("Serif", Font.BOLD, 14), textPane.getFont());
        assertEquals(4, textPane.getTabSize());
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertTrue(textPane.getScrollableTracksViewportWidth());
        JTextArea textArea = new JTextArea();
        textArea.setTabSize(10);
        assertFalse(textArea.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlightEmptyTerm() {
        String term = "";
        List<LineResult> searchResults = textPane.highlight(term, 0);
        assertEquals(0, searchResults.size());
    }

    @Test
    public void testHighlightTermInEmptyTextPane() {
        String term = "test";
        List<LineResult> searchResults = textPane.highlight(term, 0);
        assertEquals(0, searchResults.size());
    }

    @Test
    public void testHighlightTermInTextPane() throws BadLocationException {
        String text = "The quick brown fox jumps over the lazy dog.";
        textPane.setText(text);
        SwingUtilities.invokeLater(() -> textPane.highlight(term, 0));
        SwingUtilities.invokeLater(() -> assertEquals(1, highlighter.getHighlightCount()));
        SwingUtilities.invokeLater(() -> {
            LineResult lineResult = lineResults.get(0);
            assertEquals(0, lineResult.getLineNumber());
            assertEquals(text.indexOf(term), lineResult.getStartOffset());
            assertEquals(text.indexOf(term, text.indexOf(term) + term.length()) + term.length(), lineResult.getEndOffset());
        });
    }

    @Test
    public void testHighlightTermWithFlags() throws BadLocationException {
        String text = "The quick brown fox jumps over the lazy dog.";
        textPane.setText(text);
        SwingUtilities.invokeLater(() -> textPane.highlight(term, Searchable.REGULAR_EXPRESSION_MATCH));
        SwingUtilities.invokeLater(() -> assertEquals(1, highlighter.getHighlightCount()));
        SwingUtilities.invokeLater(() -> {
            LineResult lineResult = lineResults.get(0);
            assertEquals(0, lineResult.getLineNumber());
            SearchPattern pattern = TextUIResource.createSearchPattern(term);
            int start = text.indexOf(term, 0);
            int end = start + term.length();
            int flags = Searchable.REGULAR_EXPRESSION_MATCH;
            assertEquals(start, lineResult.getStartOffset());
            assertEquals(end, lineResult.getEndOffset());
            assertEquals(pattern, lineResult.getSearchPattern());
            assertEquals(flags, lineResult.getFlags());
        });
    }

    @Test
    public void testSearchEmptyTextPane() {
        int result = textPane.search(term);
        assertEquals(-1, result);
    }

    @Test
    public void testSearchTermInTextPane() throws BadLocationException {
        String text = "The quick brown fox jumps over the lazy dog.";
        textPane.setText(text);
        SwingUtilities.invokeLater(() -> textPane.search(term));
        SwingUtilities.invokeLater(() -> assertEquals(0, textPane.lastSearchPos));
        SwingUtilities.invokeLater(() -> assertEquals(text.indexOf(term), textPane.lastSearchTerm.length() + textPane.lastSearchPos));
    }

    @Test
    public void testSearchTermInTextPaneWithPreviousTerm() throws BadLocationException {
        String term1 = "fox";
        String term2 = "the";
        textPane.setText(text);
        SwingUtilities.invokeLater(() -> textPane.search(term1));
        SwingUtilities.invokeLater(() -> textPane.search(term2));
        SwingUtilities.invokeLater(() -> assertEquals(text.indexOf(term1), textPane.lastSearchPos));
        SwingUtilities.invokeLater(() -> assertEquals(text.indexOf(term1) + term1.length(), textPane.lastSearchTerm.length() + textPane.lastSearchPos));
        SwingUtilities.invokeLater(() -> assertEquals(text.indexOf(term2), textPane.lastSearchPos + term1.length()));
        SwingUtilities.invokeLater(() -> assertEquals(text.indexOf(term2) + term2.length(), textPane.lastSearchTerm.length() + textPane.lastSearchPos + term1.length()));
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        String text = "The quick brown fox jumps over the lazy dog.";
        textPane.setText(text);
        List<WordResult> searchResults = new SearchEngine(0).search(term, text);
        lineResults = textPane.convertWords2Lines(searchResults);
        SwingUtilities.invokeLater(() -> assertEquals(1, lineResults.size()));
        LineResult lineResult = lineResults.get(0);
        assertEquals(0, lineResult.getLineNumber());
        assertEquals(text.indexOf(term), lineResult.getStartOffset());
        assertEquals(text.indexOf(term, text.indexOf(term) + term.length()) + term.length(), lineResult.getEndOffset());
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        String text = "The quick brown fox jumps over the lazy dog.";
        textPane.setText(text);
        List<WordResult> wordResults = new ArrayList<>();
        wordResults.add(new WordResult(0, text.indexOf("quick"), text.indexOf("quick") + "quick".length(), null, null));
        textPane.convertWords2Lines(wordResults);
        LineResult lineResult = lineResults.get(0);
        textPane.updateWordResult(wordResults.get(0), lineResult);
        SwingUtilities.invokeLater(() -> {
            assertEquals(1, lineResult.getWordResults().size());
            WordResult wordResult = lineResult.getWordResults().get(0);
            assertEquals(0, wordResult.getLineNumber());
            assertEquals(text.indexOf("quick"), wordResult.getStartOffset());
            assertEquals(text.indexOf("quick") + "quick".length(), wordResult.getEndOffset());
            assertEquals(lineResult, wordResult.parent);
        });
    }

    @Test
    public void testRemoveHighlights() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        SwingUtilities.invokeLater(() -> textPane.highlight(term, 0));
        SwingUtilities.invokeLater(() -> textPane.removeHighlights());
        SwingUtilities.invokeLater(() -> assertEquals(0, highlighter.getHighlightCount()));
    }

    @Test
    public void testSearchFrame() {
        JFrame frame = new JFrame();
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Serif", Font.BOLD, 14));
        textArea.setTabSize(4);
        frame.getContentPane().add(new JScrollPane(textArea));
        frame.pack();
        JTextComponent component = (JTextComponent) textArea;
        SwingUtilities.invokeLater(() -> component.setDocument(new Document() {
            @Override
            public String getText(int offset, int length) throws BadLocationException {
                return "The quick brown fox jumps over the lazy dog.";
            }
        }));
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
        searchableTextPane.setDocument(component.getDocument());
        JScrollPane searchableTextPaneScrollPane = new JScrollPane(searchableTextPane);
        frame.getContentPane().add(searchableTextPaneScrollPane, 1, 1);
        SwingUtilities.invokeLater(() -> searchableTextPane.highlight(term, 0));
        SwingUtilities.invokeLater(() -> assertEquals(1, highlighter.getHighlightCount()));
        SwingUtilities.invokeLater(() -> searchableTextPane.removeHighlights());
        SwingUtilities.invokeLater(() -> assertEquals(0, highlighter.getHighlightCount()));
        SwingUtilities.invokeLater(() -> frame.dispose());
    }

    static class WordResult {
        int start;
        int end;
        LineResult parent;

        public WordResult(int start, int end, String lineNumber, SearchPattern searchPattern, int flags) {
            this.start = start;
            this.end = end;
            this.parent = null;
            this.lineNumber = lineNumber;
            this.searchPattern = searchPattern;
            this.flags = flags;
        }

        int lineNumber;
        SearchPattern searchPattern;
        int flags;

        void setLineOffset(int lineOffset) {
            // Not implemented
        }

        List<WordResult> getWordResults() {
            return new ArrayList<>();
        }
    }

    static class LineResult {
        int lineNumber;
        int startOffset;
        int endOffset;
        DefaultHighlightPainter painter;
        List<WordResult> wordResults;

        LineResult(int lineNumber, int startOffset, int endOffset) {
            this.lineNumber = lineNumber;
            this.startOffset = startOffset;
            this.endOffset = endOffset;
            this.painter = null;
            this.wordResults = new ArrayList<>();
        }

        void addHighlight(int wordStart, int wordLength) throws BadLocationException {
            // Not implemented
        }

        List<WordResult> getWordResults() {
            return wordResults;
        }
    }
}

class SearchEngine {
    int flags;

    SearchEngine(int flags) {
        this.flags = flags;
    }

    List<WordResult> search(String term, String text) throws BadLocationException {
        List<WordResult> searchResults = new ArrayList<>();
        int start = text.indexOf(term);
        while (start != -1) {
            searchResults.add(new WordResult(start, start + term.length(), null, null, flags));
            start = text.indexOf(term, start + term.length());
        }
        return searchResults;
    }
}