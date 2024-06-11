package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
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
import javax.swing.text.StyleConstants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

    private static final String TEST_TEXT = "The quick brown fox jumps over the lazy dog.";

    private SearchableTextPane textPane;
    private JFrame frame;

    @Before
    public void setUp() {
        textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 4);
        textPane.setText(TEST_TEXT);
        textPane.setWrapStyleWord(true);
        textPane.setLineWrap(true);

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(new JScrollPane(textPane));
        frame.pack();
        frame.setVisible(true);
    }

    @After
    public void tearDown() {
        SwingUtilities.invokeLater(() -> frame.dispose());
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertTrue(textPane.getScrollableTracksViewportWidth());
        textPane.setSize(new Dimension(200, 200));
        assertTrue(textPane.getScrollableTracksViewportWidth());
        textPane.setSize(new Dimension(400, 400));
        assertFalse(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        List<String> terms = new ArrayList<>();
        terms.add("brown");
        terms.add("lazy");
        terms.add("dog");

        for (String term : terms) {
            List<LineResult> lineResults = textPane.highlight(term, 0);
            assertEquals(1, lineResults.size());
            LineResult lineResult = lineResults.get(0);
            assertEquals(0, lineResult.getLineNumber());
            assertEquals(0, lineResult.getStartOffset());
            assertEquals(TEST_TEXT.length(), lineResult.getEndOffset());
            assertEquals(term, lineResult.getTerm());
            assertTrue(lineResult.hasHighlights());
            Highlighter highlighter = textPane.getHighlighter();
            int count = 0;
            for (Element element = textPane.getDocument().getRootElement(); element != null; element = element.getNextElement()) {
                if (element instanceof SimpleAttributeSet) {
                    SimpleAttributeSet attrSet = (SimpleAttributeSet) element;
                    if (attrSet.isDefinedAttribute("highlight")) {
                        count++;
                    }
                }
                element = element.getNextElement();
            }
            assertEquals(terms.size(), count);
        }
    }

    @Test
    public void testSearch() {
        List<String> terms = new ArrayList<>();
        terms.add("brown");
        terms.add("lazy");
        terms.add("dog");

        for (String term : terms) {
            int pos = textPane.search(term);
            if (term.equals("dog")) {
                assertEquals(15, pos);
            } else {
                assertTrue(pos < 0);
            }
            textPane.search(term);
            textPane.search(term);
            assertEquals(term, textPane.getLastSearchTerm());
            textPane.setText(TEST_TEXT + term);
            textPane.search(term);
            assertEquals(-1, textPane.getLastSearchPos());
            textPane.setText(TEST_TEXT);
        }
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        List<WordResult> words = new ArrayList<>();
        for (int i = 0; i < TEST_TEXT.length(); i++) {
            words.add(new WordResult(i, i + 1));
        }
        List<LineResult> lineResults = textPane.convertWords2Lines(words);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(0, lineResult.getLineNumber());
        assertEquals(0, lineResult.getStartOffset());
        assertEquals(TEST_TEXT.length(), lineResult.getEndOffset());
    }
}

class WordResult {
    int start;
    int end;
    int lineNumber;
    String term;
    Element parent;

    public WordResult(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public String toString() {
        return String.format("WordResult[start=%d, end=%d, lineNumber=%d, term=%s]", start, end, lineNumber, term);
    }
}

class LineResult {
    int lineNumber;
    int startOffset;
    int endOffset;
    String term;
    List<WordResult> wordResults;
    boolean hasHighlights;

    public LineResult(int lineNumber, int startOffset, int endOffset) {
        this.lineNumber = lineNumber;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.wordResults = new ArrayList<>();
        this.hasHighlights = false;
    }

    public void addWord(WordResult wordResult) {
        this.wordResults.add(wordResult);
    }
}