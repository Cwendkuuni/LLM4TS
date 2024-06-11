package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Line.Border;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

    private static final String TEST_TEXT = "The quick brown fox jumps over the lazy dog.";
    private SearchableTextPane textPane;
    private Document document;

    @Before
    public void setUp() {
        SwingUtilities.invokeLater(() -> {
            textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 4);
            textPane.setText(TEST_TEXT);
            document = textPane.getDocument();
        });
    }

    @After
    public void tearDown() {
        textPane.dispose();
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertTrue(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlightEmptyTerm() {
        List<LineResult> lineResults = textPane.highlight(null, 0);
        assertNotNull(lineResults);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testHighlightTermNotFound() {
        List<LineResult> lineResults = textPane.highlight("zebra", 0);
        assertNotNull(lineResults);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testHighlightTermFound() {
        List<LineResult> lineResults = textPane.highlight("fox", 0);
        assertNotNull(lineResults);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(1, lineResult.getLineNumber());
        assertEquals(35, lineResult.getStartOffset());
        assertEquals(39, lineResult.getEndOffset());

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            JScrollPane scrollPane = new JScrollPane(textPane);
            frame.getContentPane().add(scrollPane);
            frame.setSize(640, 480);
            frame.setVisible(true);
            SwingUtilities.invokeLater(() -> {
                try {
                    textPane.search("fox");
                    textPane.getHighlighter().setPaintMode(DefaultHighlighter.DEFAULT_HIGHlightPAINT_MODE);
                    textPane.getHighlighter().paint(document, 0, document.getLength());
                } catch (BadLocationException e) {
                    fail("Failed to search for term 'fox'");
                }
                LineResult lineResultFromGui = lineResults.get(0);
                assertEquals(lineResultFromGui.getLineNumber(), textPane.getCaretPosition().getLine());
                assertEquals(lineResultFromGui.getStartOffset(), textPane.getCaretPosition().getOffset());
                textPane.setEditable(true);
                textPane.getText().replaceRange("replacement", lineResultFromGui.getStartOffset(), lineResultFromGui.getEndOffset());
                textPane.setEditable(false);
                textPane.removeHighlights();
                textPane.highlight("fox", 0);
                textPane.getHighlighter().setPaintMode(DefaultHighlighter.DEFAULT_HIGHlightPAINT_MODE);
                textPane.getHighlighter().paint(document, 0, document.getLength());
                LineResult lineResultFromGuiAfterReplace = lineResults.get(0);
                assertEquals(lineResultFromGuiAfterReplace.getLineNumber(), textPane.getCaretPosition().getLine());
                assertEquals("replacement", textPane.getText().substring(lineResultFromGuiAfterReplace.getStartOffset(), lineResultFromGuiAfterReplace.getEndOffset()));
            });
        });
    }

    @Test
    public void testSearchEmptyTerm() {
        int pos = textPane.search("");
        assertEquals(-1, pos);
    }

    @Test
    public void testSearchTermNotFound() {
        int pos = textPane.search("zebra");
        assertEquals(-1, pos);
    }

    @Test
    public void testSearchTermFound() {
        int pos = textPane.search("fox");
        assertEquals(34, pos);

        SwingUtilities.invokeLater(() -> {
            textPane.search("fox");
            int posFromGui = textPane.getCaretPosition().getOffset();
            assertEquals(posFromGui, textPane.search("fox", pos - 1));
        });
    }

    @Test
    public void testConvertWords2Lines() {
        List<WordResult> words = Arrays.asList(
            new WordResult("The", 0, 3),
            new WordResult("quick", 7, 10),
            new WordResult("brown", 11, 13),
            new WordResult("fox", 15, 19),
            new WordResult("jumps", 21, 27),
            new WordResult("over", 28, 31),
            new WordResult("the", 32, 35),
            new WordResult("lazy", 36, 39),
            new WordResult("dog.", 40, 43)
        );

        List<LineResult> lineResults = null;
        try {
            lineResults = textPane.convertWords2Lines(words);
        } catch (BadLocationException e) {
            fail("Failed to convert words to lines");
        }

        assertEquals(5, lineResults.size());
        LineResult lineResult1 = lineResults.get(0);
        assertEquals(0, lineResult1.getLineNumber());
        assertEquals(0, lineResult1.getStartOffset());
        assertEquals(7, lineResult1.getEndOffset());
        LineResult lineResult2 = lineResults.get(1);
        assertEquals(1, lineResult2.getLineNumber());
        assertEquals(11, lineResult2.getStartOffset());
        assertEquals(19, lineResult2.getEndOffset());
        LineResult lineResult3 = lineResults.get(2);
        assertEquals(2, lineResult3.getLineNumber());
        assertEquals(21, lineResult3.getStartOffset());
        assertEquals(31, lineResult3.getEndOffset());
        LineResult lineResult4 = lineResults.get(3);
        assertEquals(3, lineResult4.getLineNumber());
        assertEquals(32, lineResult4.getStartOffset());
        assertEquals(39, lineResult4.getEndOffset());
        LineResult lineResult5 = lineResults.get(4);
        assertEquals(4, lineResult5.getLineNumber());
        assertEquals(40, lineResult5.getStartOffset());
        assertEquals(43, lineResult5.getEndOffset());
    }
}

class WordResult {
    final int lineNumber;
    final int start;
    final int end;
    final Element parent;

    public WordResult(String word, int start, int end) {
        this.start = start;
        this.end = end;
        this.parent = null;
        this.lineNumber = -1;
    }

    public void setLineOffset(int lineOffset) {
        this.lineNumber = lineOffset / getFontMetrics(getTextComponent().getFont()).getLineHeight();
    }

    public String getTextComponent() {
        return textPane;
    }
}

class LineResult {
    final int lineNumber;
    final int startOffset;
    final int endOffset;
    final List<WordResult> wordResults = new ArrayList<>();

    public LineResult(int lineNumber, int startOffset, int endOffset) {
        this.lineNumber = lineNumber;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    public void addWord(WordResult wordResult) {
        this.wordResults.add(wordResult);
    }

    public List<WordResult> getWordResults() {
        return this.wordResults;
    }
}