package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Line.Border;
import javax.swing.text.LineElement;
import javax.swing.text.ParagraphElement;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

    private SearchableTextPane textPane;
    private List<LineResult> lineResults;

    @Before
    public void setup() {
        textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 8);
    }

    @Test
    public void testConstructor() {
        assertTrue(textPane instanceof JTextArea);
        assertEquals(new Font("Arial", Font.PLAIN, 12), textPane.getFont());
        assertEquals(8, textPane.getTabSize());
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertTrue(textPane.getScrollableTracksViewportWidth());
        textPane.setParent(new JFrame());
        assertTrue(textPane.getScrollableTracksViewportWidth());
        textPane.setParent(null);
        assertTrue(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        String text = "The quick brown fox jumps over the lazy dog.";
        textPane.setText(text);

        List<WordResult> searchResults = Arrays.asList(
                new WordResult("The", 0, 3),
                new WordResult("quick", 6, 5),
                new WordResult("brown", 11, 5),
                new WordResult("fox", 17, 3),
                new WordResult("jumps", 21, 5),
                new WordResult("over", 26, 4),
                new WordResult("the", 31, 3),
                new WordResult("lazy", 34, 4),
                new WordResult("dog.", 39, 3)
        );

        lineResults = textPane.highlight("fox", 0);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(1, lineResult.getLineNumber());
        assertEquals(3, lineResult.getStartLineOffset());
        assertEquals(7, lineResult.getEndLineOffset());
        assertEquals(Color.YELLOW, lineResult.getHighlightPainter().getColor());

        lineResults = textPane.highlight("The", 0);
        assertEquals(1, lineResults.size());
        lineResult = lineResults.get(0);
        assertEquals(1, lineResult.getLineNumber());
        assertEquals(0, lineResult.getStartLineOffset());
        assertEquals(3, lineResult.getEndLineOffset());

        lineResults = textPane.highlight("dog.", 0);
        assertEquals(1, lineResults.size());
        lineResult = lineResults.get(0);
        assertEquals(1, lineResult.getLineNumber());
        assertEquals(36, lineResult.getStartLineOffset());
        assertEquals(39, lineResult.getEndLineOffset());

        lineResults = textPane.highlight("cat", 0);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testSearch() {
        String text = "The quick brown fox jumps over the lazy dog.";
        textPane.setText(text);

        int index = textPane.search("fox");
        assertEquals(17, index);

        index = textPane.search("The");
        assertEquals(0, index);

        index = textPane.search("cat");
        assertEquals(-1, index);

        textPane.search("lazy");
        assertEquals("lazy", textPane.lastSearchTerm);
        index = textPane.search(null);
        assertEquals(-1, index);
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        String text = "The quick brown fox jumps over the lazy dog.";
        textPane.setText(text);

        List<WordResult> searchResults = Arrays.asList(
                new WordResult("The", 0, 3),
                new WordResult("quick", 6, 5),
                new WordResult("brown", 11, 5),
                new WordResult("fox", 17, 3),
                new WordResult("jumps", 21, 5),
                new WordResult("over", 26, 4),
                new WordResult("the", 31, 3),
                new WordResult("lazy", 34, 4),
                new WordResult("dog.", 39, 3)
        );

        lineResults = textPane.convertWords2Lines(searchResults);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(1, lineResult.getLineNumber());
        assertEquals(3, lineResult.getStartLineOffset());
        assertEquals(7, lineResult.getEndLineOffset());

        lineResults = Arrays.asList(textPane.convertWords2Lines(Collections.emptyList()));
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testAddHighlight() throws BadLocationException {
        Document doc = textPane.getDocument();
        doc.insertString(0, "The quick brown fox", null);

        int start = 0;
        int length = "The quick brown fox".length();
        textPane.addHighlight(start, length);

        DefaultHighlighter highlighter = textPane.getHighlighter();
        assertEquals(1, highlighter.getHighlights().length);

        LineElement lineElement = highlighter.getHighlightPainter().getLineElement(highlighter.getHighlights()[0]);
        LineResult lineResult = new LineResult(1, 0, 14);
        assertEquals(lineResult, lineElement.getElement());
    }

    @Test
    public void testRemoveHighlights() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        textPane.highlight("fox", 0);

        List<LineResult> lineResultsBefore = textPane.convertWords2Lines(Arrays.asList(new WordResult("fox", 17, 3)));
        assertEquals(1, lineResultsBefore.size());

        textPane.removeHighlights();

        List<LineResult> lineResultsAfter = textPane.convertWords2Lines(Arrays.asList(new WordResult("fox", 17, 3)));
        assertEquals(0, lineResultsAfter.size());
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        textPane.setText("The quick brown fox jumps over the lazy dog.");

        int startWord1 = 6;
        int lengthWord1 = 5;
        WordResult wordResult1 = new WordResult("quick", startWord1, startWord1 + lengthWord1);

        int startWord2 = 11;
        int lengthWord2 = 5;
        WordResult wordResult2 = new WordResult("brown", startWord2, startWord2 + lengthWord2);

        lineResults = textPane.convertWords2Lines(Arrays.asList(wordResult1, wordResult2));
        assertEquals(1, lineResults.size());

        LineResult lineResult = lineResults.get(0);
        lineResult.addWord(wordResult1);
        lineResult.addWord(wordResult2);

        textPane.updateWordResult(wordResult1, lineResult);
        assertEquals("quick", wordResult1.getText());
        assertEquals(3, wordResult1.getLineOffset());

        textPane.updateWordResult(wordResult2, lineResult);
        assertEquals("brown", wordResult2.getText());
        assertEquals(8, wordResult2.getLineOffset());
    }
}