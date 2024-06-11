package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class SearchableTextPaneTest {

    private static final String TEST_TEXT = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.";

    @Test
    public void testSearchableTextPaneConstruction() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("SansSerif", Font.PLAIN, 12), 4);
        assertTrue(textPane instanceof JTextArea);
        assertEquals(12, textPane.getFont().getSize2D().getHeight());
        assertEquals(4, textPane.getTabSize());
    }

    @Test
    public void testScrollable() {
        JFrame frame = new JFrame();
        SearchableTextPane textPane = new SearchableTextPane(new Font("SansSerif", Font.PLAIN, 12), 4);
        JScrollPane scrollPane = new JScrollPane(textPane);
        frame.getContentPane().add(scrollPane);
        frame.setSize(300, 300);
        frame.setVisible(true);

        assertTrue(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("SansSerif", Font.PLAIN, 12), 4);
        Document document = textPane.getDocument();
        document.setLength(0);
        document.insertString(0, TEST_TEXT, null);

        List<LineResult> lineResults = textPane.highlight("lazy", 0);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertSame(textPane, lineResult.getParent());
        assertEquals(1, lineResult.getLineNumber());
        assertEquals(1, lineResult.getWordResults().size());
        WordResult wordResult = lineResult.getWordResults().get(0);
        assertEquals(13, wordResult.getLineOffset());
        assertEquals(25, wordResult.getWordOffset());
        assertEquals(17, wordResult.getEndOffset());
        assertEquals(15, wordResult.getStartOffset());
        assertEquals("lazy", wordResult.getText());

        Highlighter highlighter = textPane.getHighlighter();
        HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
        highlighter.addHighlight(wordResult.getStart(), wordResult.getEnd() - wordResult.getStart(), painter);

        JTextPane textPaneWithHighlight = new JTextPane(document);
        textPaneWithHighlight.setHighlighter(highlighter);

        SwingUtilities.invokeLater(() -> {
            JScrollPane scrollPane = new JTextPane.ScrollableViewportViewHolder(textPaneWithHighlight).getScrollPane();
            JFrame frame = new JFrame();
            frame.getContentPane().add(scrollPane);
            frame.pack();
            frame.setVisible(true);
        });

        JTextArea textArea = (JTextArea) textPaneWithHighlight;
        assertTrue(textArea.getCaretPosition().x >= 15 && textArea.getCaretPosition().x <= 17);
        assertTrue(textArea.getCaretPosition().y >= 1 && textArea.getCaretPosition().y <= 2);

        textPane.highlight("fox", 0);
        assertEquals(0, textPane.getHighlighter().getHighlightCount());
    }

    @Test
    public void testSearch() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("SansSerif", Font.PLAIN, 12), 4);
        Document document = textPane.getDocument();
        document.setLength(0);
        document.insertString(0, TEST_TEXT, null);

        int pos = textPane.search("lazy");
        assertEquals(-1, pos);

        List<LineResult> lineResults = textPane.highlight("lazy", 0);
        assertEquals(1, lineResults.size());
        pos = textPane.search("lazy");
        assertEquals(13, pos);

        textPane.search("fox");
        assertEquals(-1, textPane.getLastSearchPos());
        assertNull(textPane.getLastSearchTerm());

        textPane.search("fox");
        assertEquals(7, textPane.getLastSearchPos());
        assertEquals("fox", textPane.getLastSearchTerm());

        textPane.search("fox");
        assertEquals(7, textPane.getLastSearchPos());
        assertEquals("fox", textPane.getLastSearchTerm());

        textPane.search("dog");
        assertEquals(26, textPane.getLastSearchPos());
        assertEquals("dog", textPane.getLastSearchTerm());
    }

    @Test
    public void testSearchWithFlags() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("SansSerif", Font.PLAIN, 12), 4);
        Document document = textPane.getDocument();
        document.setLength(0);
        document.insertString(0, TEST_TEXT, null);

        List<WordResult> searchResults = new SearchEngine(SearchEngine.REGEXP_FLAG).search("lazy", TEST_TEXT);
        List<LineResult> lineResults = textPane.convertWords2Lines(searchResults);

        textPane.highlight("lazy", 0);
        assertEquals(1, textPane.getHighlighter().getHighlightCount());

        List<WordResult> searchResultsWithFlags = new SearchEngine(SearchEngine.MULTILINE_FLAG | SearchEngine.CASE_INSENSITIVE_FLAG).search("LAZY", TEST_TEXT);
        lineResults = textPane.convertWords2Lines(searchResultsWithFlags);

        textPane.highlight("lazy", 0);
        assertEquals(2, textPane.getHighlighter().getHighlightCount());

        lineResults = textPane.highlight("FOX", 0);
        assertEquals(1, lineResults.size());
        assertEquals(7, lineResults.get(0).getLineNumber());
    }

    @Test
    public void testConvertWords2Lines() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("SansSerif", Font.PLAIN, 12), 4);
        Document document = textPane.getDocument();
        document.setLength(0);
        document.insertString(0, TEST_TEXT, null);

        List<WordResult> wordResults = new ArrayList<>(Arrays.asList(
                new WordResult(13, 25, 17, "lazy"),
                new WordResult(33, 38, 38, "dog"),
                new WordResult(45, 49, 49, "fox"),
                new WordResult(52, 55, 55, "jumps"),
                new WordResult(58, 61, 61, "over"),
                new WordResult(64, 68, 68, "the"),
                new WordResult(70, 73, 73, "quick"),
                new WordResult(75, 78, 78, "brown"),
                new WordResult(79, 82, 82, "fox"),
                new WordResult(84, 88, 88, "jumps"),
                new WordResult(91, 94, 94, "over"),
                new WordResult(96, 100, 100, "the"),
                new WordResult(98, 101, 101, "lazy"),
                new WordResult(103, 106, 106, "dog.")
        ));

        List<LineResult> lineResults = textPane.convertWords2Lines(wordResults);
        assertEquals(4, lineResults.size());
        assertEquals(1, lineResults.get(0).getLineNumber());
        assertEquals(2, lineResults.get(1).getLineNumber());
        assertEquals(3, lineResults.get(2).getLineNumber());
        assertEquals(4, lineResults.get(3).getLineNumber());

        for (LineResult lineResult : lineResults) {
            assertEquals(lineResult.getParent(), textPane);
        }

        for (LineResult lineResult : lineResults) {
            for (WordResult wordResult : lineResult.getWordResults()) {
                assertEquals(wordResult.getParent(), lineResult);
            }
        }
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        SearchableTextPane textPane = new SearchableTextPane(new Font("SansSerif", Font.PLAIN, 12), 4);
        Document document = textPane.getDocument();
        document.setLength(0);
        document.insertString(0, "The quick brown fox jumps over the lazy dog.\nThe quick brown fox jumps over the lazy dog.", null);

        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setBackground(set, Color.YELLOW);

        List<WordResult> wordResults = new ArrayList<>(Arrays.asList(
                new WordResult(13, 25, 17, "lazy", set),
                new WordResult(33, 38, 38, "dog")
        ));

        List<LineResult> lineResults = textPane.convertWords2Lines(wordResults);

        textPane.updateWordResult(wordResults.get(0), lineResults.get(0));

        LineResult lineResult = lineResults.get(0);
        assertEquals(lineResult.getWordResults().get(0).getStartOffset(), wordResults.get(0).getStart());
        assertEquals(lineResult.getWordResults().get(0).getEndOffset(), wordResults.get(0).getEnd());
        assertEquals(lineResult.getWordResults().get(0).getText(), wordResults.get(0).getText());
        assertEquals(lineResult.getWordResults().get(0).getAttributes(), wordResults.get(0).getAttributes());

        textPane.updateWordResult(wordResults.get(1), lineResults.get(1));

        lineResult = lineResults.get(1);
        assertEquals(lineResult.getWordResults().get(0).getStartOffset(), wordResults.get(1).getStart());
        assertEquals(lineResult.getWordResults().get(0).getEndOffset(), wordResults.get(1).getEnd());
        assertEquals(lineResult.getWordResults().get(0).getText(), wordResults.get(1).getText());
        assertEquals(lineResult.getWordResults().get(0).getAttributes(), wordResults.get(1).getAttributes());

        JTextArea textArea = (JTextArea) textPane;
        textArea.setCaretPosition(wordResults.get(0).getStart());
        assertEquals("lazy", textArea.getText(textArea.getCaretPosition(), wordResults.get(0).getText().length()));

        textArea.setCaretPosition(wordResults.get(1).getStart());
        assertEquals("dog", textArea.getText(textArea.getCaretPosition(), wordResults.get(1).getText().length()));
    }

    @Test
    public void testRemoveHighlights() {
        SearchableTextPane textPane = new SearchableTextPane(new Font("SansSerif", Font.PLAIN, 12), 4);
        Document document = textPane.getDocument();
        document.setLength(0);
        document.insertString(0, TEST_TEXT, null);

        List<WordResult> wordResults = new ArrayList<>(Arrays.asList(
                new WordResult(13, 25, 17, "lazy"),
                new WordResult(33, 38, 38, "dog")
        ));

        List<LineResult> lineResults = textPane.convertWords2Lines(wordResults);

        textPane.highlight("lazy", 0);
        assertEquals(1, textPane.getHighlighter().getHighlightCount());

        textPane.highlight("dog", 0);
        assertEquals(2, textPane.getHighlighter().getHighlightCount());

        textPane.removeHighlights();
        assertEquals(0, textPane.getHighlighter().getHighlightCount());

        textPane.highlight("lazy", 0);
        assertEquals(1, textPane.getHighlighter().getHighlightCount());

        textPane.highlight("dog", 0);
        assertEquals(2, textPane.getHighlighter().getHighlightCount());

        textPane.removeHighlights();
        assertEquals(0, textPane.getHighlighter().getHighlightCount());
    }

    private static class WordResult {
        int start;
        int end;
        String text;
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        SearchableTextPane.LineNumberHolder lineNumber;

        WordResult(int start, int end, int length, String text) {
            this.start = start;
            this.end = start + length;
            this.text = text;
        }

        WordResult(int start, int end, int length, String text, SimpleAttributeSet attributes) {
            this.start = start;
            this.end = start + length;
            this.text = text;
            this.attributes = attributes;
        }
    }

    private static class LineResult {
        int lineNumber;
        List<WordResult> wordResults = new ArrayList<>();

        LineResult(int lineNumber, int lineStart, int lineEnd) {
            this.lineNumber = lineNumber;
        }

        void addWord(WordResult wordResult) {
            wordResult.parent = this;
            wordResults.add(wordResult);
        }

        List<WordResult> getWordResults() {
            return wordResults;
        }

        int getLineNumber() {
            return lineNumber;
        }
    }

    private static class SearchEngine {
        private int flags;

        SearchEngine(int flags) {
            this.flags = flags;
        }

        List<WordResult> search(String searchTerm, String text) {
            // Implement search logic here
            return new ArrayList<>();
        }

        static final int REGEXP_FLAG = 1;
        static final int MULTILINE_FLAG = 2;
        static final int CASE_INSENSITIVE_FLAG = 4;
    }

    private static class Utilities {
        static Element getParagraphElement(JTextComponent textComponent, int offset) throws BadLocationException {
            Document document = textComponent.getDocument();
            ElementIterator it = new ElementIterator(document.getRootElement());
            Element element = null;
            while (it.next()) {
                if (it.getOffset() >= offset && it.getOffset() < offset + 1) {
                    element = it.getElement();
                    break;
                }
            }
            return element;
        }
    }
}