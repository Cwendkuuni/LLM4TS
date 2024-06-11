package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ghm.follow.search.SearchableTextPane;
import ghm.follow.search.SearchEngine;
import ghm.follow.search.Utilities;
import ghm.follow.search.WordResult;
import ghm.follow.search.LineResult;

public class SearchableTextPaneTest {

    private SearchableTextPane textPane;
    private JFrame frame;

    @Before
    public void setUp() {
        textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        SwingUtilities.invokeLater(() -> {
            frame.getContentPane().add(new JScrollPane(textPane));
            frame.pack();
            frame.setVisible(true);
        });
    }

    @After
    public void tearDown() {
        SwingUtilities.invokeLater(() -> frame.dispose());
    }

    @Test
    public void testSearchableTextPaneCreation() {
        assertEquals(14, textPane.getFont().getSize2D().getHeight());
        assertEquals(4, textPane.getTabSize());
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertTrue(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlightEmptyTerm() {
        String term = "";
        List<LineResult> lineResults = textPane.highlight(term, 0);
        assertTrue(lineResults.isEmpty());
    }

    @Test
    public void testHighlightNullTerm() {
        String term = null;
        List<LineResult> lineResults = textPane.highlight(term, 0);
        assertTrue(lineResults.isEmpty());
    }

    @Test
    public void testHighlightSingleWord() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        String term = "fox";
        List<LineResult> lineResults = textPane.highlight(term, 0);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(1, lineResult.getLineNumber());
        assertEquals(14, lineResult.getStartLineOffset());
        assertEquals(24, lineResult.getEndLineOffset());
        List<WordResult> wordResults = lineResult.getWordResults();
        assertEquals(1, wordResults.size());
        WordResult wordResult = wordResults.get(0);
        assertEquals(17, wordResult.getStart());
        assertEquals(20, wordResult.getEnd());
        assertEquals("fox", wordResult.getText());
        assertEquals(Color.YELLOW, wordResult.getHighlightColor());
        assertEquals(1, textPane.getHighlighter().getHighlightCount());
    }

    @Test
    public void testHighlightMultipleWords() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        String term = "brown fox jumps over";
        List<LineResult> lineResults = textPane.highlight(term, 0);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(1, lineResult.getLineNumber());
        assertEquals(1, lineResult.getStartLineOffset());
        assertEquals(36, lineResult.getEndLineOffset());
        List<WordResult> wordResults = lineResult.getWordResults();
        assertEquals(3, wordResults.size());
        WordResult wordResult1 = wordResults.get(0);
        assertEquals(7, wordResult1.getStart());
        assertEquals(11, wordResult1.getEnd());
        assertEquals("brown", wordResult1.getText());
        WordResult wordResult2 = wordResults.get(1);
        assertEquals(14, wordResult2.getStart());
        assertEquals(20, wordResult2.getEnd());
        assertEquals("fox", wordResult2.getText());
        WordResult wordResult3 = wordResults.get(2);
        assertEquals(24, wordResult3.getStart());
        assertEquals(31, wordResult3.getEnd());
        assertEquals("jumps over", wordResult3.getText());
        assertEquals(Color.YELLOW, wordResult1.getHighlightColor());
        assertEquals(Color.YELLOW, wordResult2.getHighlightColor());
        assertEquals(Color.YELLOW, wordResult3.getHighlightColor());
        assertEquals(3, textPane.getHighlighter().getHighlightCount());
    }

    @Test
    public void testHighlightMultipleLines() {
        textPane.setText("Line 1: The quick brown fox jumps over the lazy dog.\n" +
                         "Line 2: A different sentence.\n" +
                         "Line 3: The quick brown fox jumps over the lazy dog.");
        String term = "jumps over";
        List<LineResult> lineResults = textPane.highlight(term, 0);
        assertEquals(2, lineResults.size());
        int lineNumber = 1;
        for (LineResult lineResult : lineResults) {
            assertEquals(lineNumber++, lineResult.getLineNumber());
            List<WordResult> wordResults = lineResult.getWordResults();
            assertEquals(1, wordResults.size());
            WordResult wordResult = wordResults.get(0);
            assertEquals(26, wordResult.getStart());
            assertEquals(30, wordResult.getEnd());
            assertEquals("jumps over", wordResult.getText());
        }
        assertEquals(2, textPane.getHighlighter().getHighlightCount());
    }

    @Test
    public void testSearchNullTerm() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        int pos = textPane.search(null);
        assertEquals(-1, pos);
    }

    @Test
    public void testSearchEmptyTerm() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        int pos = textPane.search("");
        assertEquals(-1, pos);
    }

    @Test
    public void testSearchSingleWord() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        String term = "fox";
        int pos = textPane.search(term);
        assertEquals(17, pos);
    }

    @Test
    public void testSearchMultipleWords() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        String term = "brown fox jumps over";
        int pos = textPane.search(term);
        assertEquals(7, pos);
    }

    @Test
    public void testSearchMultipleOccurrences() {
        textPane.setText("The quick brown fox jumps over the lazy dog.\n" +
                         "The quick brown fox jumps over the lazy dog.");
        String term = "jumps over";
        int pos1 = textPane.search(term);
        int pos2 = textPane.search(term, pos1 + term.length());
        List<LineResult> lineResults = textPane.highlight(term, 0);
        assertEquals(2, lineResults.size());
        assertEquals(1, lineResults.get(0).getLineNumber());
        assertEquals(1, lineResults.get(1).getLineNumber());
        assertEquals(Color.YELLOW, lineResults.get(0).getHighlightColor());
        assertEquals(Color.YELLOW, lineResults.get(1).getHighlightColor());
        assertEquals(2, textPane.getHighlighter().getHighlightCount());
    }

    @Test
    public void testSearchBackward() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        String term = "fox";
        int pos = textPane.search(term, textPane.getText().length() - term.length());
        assertEquals(16, pos);
    }

    @Test
    public void testSearchCaseInsensitive() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        String term = "Fox";
        int pos = textPane.search(term);
        assertEquals(17, pos);
    }

    @Test
    public void testSearchWrapsAround() {
        textPane.setText("The quick brown fox jumps over the lazy dog.\n" +
                         "The quick brown fox jumps over the lazy dog.");
        String term = "jumps over";
        int pos1 = textPane.search(term);
        int pos2 = textPane.search(term, pos1 + term.length());
        assertEquals(1, textPane.getHighlighter().getHighlightCount());
        int pos3 = textPane.search(term, pos2 - term.length());
        assertEquals(3, textPane.getHighlighter().getHighlightCount());
    }
}

class SearchEngine {
    public List<WordResult> search(final String term, final String text) {
        List<WordResult> results = new ArrayList<>();
        int start = 0;
        int end = 0;
        while ((start = text.indexOf(term, start)) != -1) {
            results.add(new WordResult(start, start + term.length()));
            end = start + term.length();
            start = end;
        }
        return results;
    }
}

class WordResult {
    int start;
    int end;
    String text;
    LineResult parent;
    int lineNumber;
    int lineOffset;

    public WordResult(int start, int end) {
        this.start = start;
        this.end = end;
        this.text = textBetween(text, start, end);
    }

    public void setLineOffset(int lineOffset) {
        this.lineOffset = lineOffset;
    }

    private static String textBetween(String text, int start, int end) {
        return text.substring(start, end);
    }
}

class LineResult {
    int lineNumber;
    int startLineOffset;
    int endLineOffset;
    List<WordResult> wordResults = new ArrayList<>();

    public LineResult(int lineNumber, int startLineOffset, int endLineOffset) {
        this.lineNumber = lineNumber;
        this.startLineOffset = startLineOffset;
        this.endLineOffset = endLineOffset;
    }

    public void addWord(WordResult wordResult) {
        this.wordResults.add(wordResult);
    }
}

class Utilities {
    public static Element getParagraphElement(JTextComponent component, int offset) {
        Document doc = component.getDocument();
        ElementIterator iterator = new ElementIterator(doc);
        Element element = iterator.getNextElement();
        int startOffset = 0;
        while (element != null && startOffset + element.getLength() < offset) {
            element = iterator.getNextElement();
            startOffset += element.getLength();
        }
        return element;
    }
}