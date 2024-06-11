package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyleConstants;

import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

    private static final String TEST_TEXT = "The quick brown fox jumps over the lazy dog.\n"
            + "The quick brown fox jumps over the quick lazy dog.\n"
            + "The quick brown fox jumps over the quick brown lazy dog.";

    private SearchableTextPane textPane;
    private JFrame frame;
    private JScrollPane scrollPane;

    @Before
    public void setUp() {
        final Font font = new Font("Serif", Font.PLAIN, 12);
        final int tabSize = 4;
        textPane = new SearchableTextPane(font, tabSize);
        textPane.setText(TEST_TEXT);

        frame = new JFrame();
        scrollPane = new JScrollPane(textPane);
        frame.getContentPane().add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertTrue(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        List<String> terms = Arrays.asList("quick", "brown", "lazy", "dog", "fox");
        for (String term : terms) {
            List<LineResult> lineResults = textPane.highlight(term, 0);
            assertEquals(2, lineResults.size());
            for (LineResult lineResult : lineResults) {
                assertEquals(term, lineResult.getTerm());
                assertEquals(getLineNumber(lineResult.getLineNumber()), lineResult.getLineNumber());
                List<WordResult> wordResults = lineResult.getWordResults();
                assertEquals(2, wordResults.size());
                for (WordResult wordResult : wordResults) {
                    int start = wordResult.getStart();
                    int end = wordResult.getEnd();
                    assertEquals(term, textPane.getText(Math.min(start, TEXT_LENGTH - 1), term.length()));
                    assertEquals(term, textPane.getText(start, term.length()));
                    assertEquals(start, textPane.getDocument().getStartPosition().getOffset());
                    textPane.getDocument().setCharacterAttributes(start, term.length(), new SimpleAttributeSet(), false);
                    assertEquals(Color.YELLOW, textPane.getBackground(), textPane.getHighlighter().getHighlightPainter(0));
                    assertEquals(Color.YELLOW, textPane.getForeground(), textPane.getHighlighter().getHighlightPainter(0).getColor());
                }
            }
        }
    }

    @Test
    public void testSearch() {
        List<String> terms = Arrays.asList("quick", "brown", "lazy", "dog", "fox");
        for (String term : terms) {
            int pos = textPane.search(term);
            if (pos != -1) {
                assertEquals((term.length() + pos) % TEXT_LENGTH, textPane.search(term, pos + term.length()));
            }
            else {
                assertNull(textPane.search(term, 0));
            }
        }
        assertNull(textPane.search(null, 0));
        assertNull(textPane.search("", 0));
    }

    @Test
    public void testConvertWords2Lines() {
        List<WordResult> wordResults = Arrays.asList(
                new WordResult(1, 3, 6, "quick"),
                new WordResult(2, 15, 18, "brown"),
                new WordResult(2, 24, 27, "lazy"),
                new WordResult(2, 31, 34, "dog"),
                new WordResult(3, 46, 49, "quick"),
                new WordResult(3, 53, 56, "brown"),
                new WordResult(3, 60, 63, "lazy"),
                new WordResult(3, 66, 69, "dog")
        );
        List<LineResult> lineResults = textPane.convertWords2Lines(wordResults);
        assertEquals(3, lineResults.size());
        for (LineResult lineResult : lineResults) {
            assertEquals(1, lineResult.getLineNumber());
            List<WordResult> lineWordResults = lineResult.getWordResults();
            assertEquals(4, lineWordResults.size());
            for (WordResult wordResult : lineWordResults) {
                assertEquals(wordResult.getStart(), lineResult.getStart() + wordResult.getStartOffset());
                assertEquals(wordResult.getEnd(), lineResult.getStart() + wordResult.getStartOffset() + wordResult.getLength());
            }
        }
    }

    @Test
    public void testAddHighlight() throws BadLocationException {
        List<WordResult> wordResults = Arrays.asList(new WordResult(1, 3, 6, "quick"));
        textPane.addHighlight(wordResults.get(0).getStart(), wordResults.get(0).getLength());
        assertEquals(Color.YELLOW, textPane.getBackground(), textPane.getHighlighter().getHighlightPainter(0).getColor());
        assertEquals(Color.YELLOW, textPane.getForeground(), textPane.getHighlighter().getHighlightPainter(0).getColor());
    }

    @Test
    public void testRemoveHighlights() {
        textPane.addHighlight(0, 6);
        textPane.addHighlight(15, 3);
        List<HighlightPainter> painters = textPane.getHighlighter().getPaintifiers();
        assertEquals(2, painters.size());
        textPane.removeHighlights();
        painters = textPane.getHighlighter().getPaintifiers();
        assertEquals(0, painters.size());
    }

    @Test
    public void testSearchWithCaseSensitivity() {
        textPane.setText(TEST_TEXT.toUpperCase());
        List<String> terms = Arrays.asList("quick", "Brown", "LAZY", "DOG", "FOX");
        for (String term : terms) {
            int pos = textPane.search(term);
            if (pos != -1) {
                assertEquals((term.length() + pos) % TEXT_LENGTH, textPane.search(term, pos + term.length()));
            }
            else {
                assertNull(textPane.search(term, 0));
            }
        }
        assertNull(textPane.search(null, 0));
        assertNull(textPane.search("", 0));
    }

    @Test
    public void testSearchWithRegularExpression() {
        textPane.setText(TEST_TEXT);
        List<String> terms = Arrays.asList("qu[ck]", "brow[n]|broun", "laz[yz]", "dog|do[gG]");
        for (String term : terms) {
            List<LineResult> lineResults = textPane.highlight(term, 0);
            List<WordResult> wordResults = lineResults.get(0).getWordResults();
            assertEquals(4, wordResults.size());
            int sum = 0;
            for (WordResult wordResult : wordResults) {
                sum += wordResult.getLength();
            }
            assertEquals(term.length() + sum, textPane.search(term, 0));
        }
        assertNull(textPane.search(null, 0));
        assertNull(textPane.search("", 0));
    }

    private int getLineNumber(int lineNumber) {
        return lineNumber + 1;
    }

    private final int TEXT_LENGTH = TEST_TEXT.length();

}