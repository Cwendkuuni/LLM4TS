package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.Searchable;
import javax.swing.text.SearchPattern;
import javax.swing.text.Searchable.SearchHit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

    private SearchableTextPane textPane;
    private JFrame frame;

    @Before
    public void setUp() {
        FrameUtilities.setLookAndFeel();
        textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 4);
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new JScrollPane(textPane), java.awt.BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    @After
    public void tearDown() {
        SwingUtilities.invokeLater(() -> {
            frame.dispose();
            System.exit(0);
        });
    }

    @Test
    public void testSearchableTextPaneConstructor() {
        assertEquals("12", textPane.getFont().getName());
        assertEquals(4, textPane.getTabSize());
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertTrue(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlightEmptyString() {
        textPane.highlight("", 0);
        assertEquals(0, textPane.getDocument().getLength());
    }

    @Test
    public void testHighlightNullTerm() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        textPane.highlight(null, 0);
    }

    @Test
    public void testHighlightSingleWord() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        List<LineResult> lineResults = textPane.highlight("fox", 0);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(3, lineResult.getLineNumber());
        assertEquals(16, lineResult.getLineStartOffset());
        assertEquals(21, lineResult.getLineEndOffset());
        final int wordStart = textPane.getDocument().getStartOffset();
        final int wordEnd = textPane.getDocument().getEndOffset();
        assertEquals(16, lineResult.getWordResults().get(0).start);
        assertEquals(21, lineResult.getWordResults().get(0).end);
        assertEquals("fox", textPane.getDocument().getText(lineResult.getWordResults().get(0).start, lineResult.getWordResults().get(0).length()));
        assertEquals(1, textPane.getDocument().getLength());
    }

    @Test
    public void testHighlightMultiWord() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        List<LineResult> lineResults = textPane.highlight("quick brown fox", 0);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(0, lineResult.getLineNumber());
        assertEquals(0, lineResult.getLineStartOffset());
        assertEquals(22, lineResult.getLineEndOffset());
        final int wordStart = textPane.getDocument().getStartOffset();
        final int wordEnd = textPane.getDocument().getEndOffset();
        List<WordResult> wordResults = lineResult.getWordResults();
        assertEquals(3, wordResults.size());
        assertEquals("quick", wordResults.get(0).term);
        assertEquals(0, wordResults.get(0).start);
        assertEquals(7, wordResults.get(0).end);
        assertEquals("brown", wordResults.get(1).term);
        assertEquals(8, wordResults.get(1).start);
        assertEquals(13, wordResults.get(1).end);
        assertEquals("fox", wordResults.get(2).term);
        assertEquals(16, wordResults.get(2).start);
        assertEquals(21, wordResults.get(2).end);
        assertEquals("quick brown fox", textPane.getDocument().getText(wordStart, wordEnd - wordStart));
    }

    @Test
    public void testHighlightMultiLine() {
        textPane.setText("The quick brown fox jumps over the lazy dog.\nThe quick red fox jumps over the lazy cat.");
        List<LineResult> lineResults = textPane.highlight("quick", 0);
        assertEquals(2, lineResults.size());
        LineResult lineResult1 = lineResults.get(0);
        assertEquals(0, lineResult1.getLineNumber());
        assertEquals(0, lineResult1.getLineStartOffset());
        assertEquals(22, lineResult1.getLineEndOffset());
        List<WordResult> wordResults1 = lineResult1.getWordResults();
        assertEquals(1, wordResults1.size());
        assertEquals("quick", wordResults1.get(0).term);
        assertEquals(0, wordResults1.get(0).start);
        assertEquals(7, wordResults1.get(0).end);

        LineResult lineResult2 = lineResults.get(1);
        assertEquals(1, lineResult2.getLineNumber());
        assertEquals(0, lineResult2.getLineStartOffset());
        assertEquals(33, lineResult2.getLineEndOffset());
        List<WordResult> wordResults2 = lineResult2.getWordResults();
        assertEquals(1, wordResults2.size());
        assertEquals("quick", wordResults2.get(0).term);
        assertEquals(16, wordResults2.get(0).start);
        assertEquals(21, wordResults2.get(0).end);
    }

    @Test
    public void testSearch() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        int pos = textPane.search("fox");
        assertEquals(16, pos);
        pos = textPane.search("lazy");
        assertEquals(40, pos);
        pos = textPane.search("zebra");
        assertEquals(-1, pos);
        textPane.setText("The quick brown fox jumps over the lazy dog.\nThe quick red fox jumps over the lazy cat.");
        pos = textPane.search("fox");
        assertEquals(16, pos);
        pos = textPane.search("cat");
        assertEquals(32, pos);
        pos = textPane.search("zebra");
        assertEquals(-1, pos);
    }

    @Test
    public void testSearchWithOffset() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        int pos = textPane.search("fox", 41);
        assertEquals(-1, pos);
        textPane.setText("The quick brown fox jumps over the lazy dog.\nThe quick red fox jumps over the lazy cat.");
        pos = textPane.search("cat", 42);
        assertEquals(-1, pos);
    }

    @Test
    public void testSearchPattern() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        SearchPattern pattern = new RegularExpressionSearchPattern("[fF][oO][xX]");
        int pos = textPane.search(pattern, 0);
        assertEquals(16, pos);
        pattern = new RegularExpressionSearchPattern("[dD][oO][gG]");
        pos = textPane.search(pattern, 0);
        assertEquals(39, pos);
        pattern = new RegularExpressionSearchPattern("[zZ][eE][bB]");
        pos = textPane.search(pattern, 0);
        assertEquals(-1, pos);
        textPane.setText("The quick brown fox jumps over the lazy dog.\nThe quick red fox jumps over the lazy cat.");
        pattern = new RegularExpressionSearchPattern("[fF][oO][xX]");
        pos = textPane.search(pattern, 0);
        assertEquals(16, pos);
        pos = textPane.search(pattern, 42);
        assertEquals(-1, pos);
        pattern = new RegularExpressionSearchPattern("[cC][aA][tT]");
        pos = textPane.search(pattern, 42);
        assertEquals(32, pos);
    }

    @Test
    public void testHighlightAndSearch() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        List<LineResult> lineResults = textPane.highlight("fox", 0);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(3, lineResult.getLineNumber());
        assertEquals(16, lineResult.getLineStartOffset());
        assertEquals(21, lineResult.getLineEndOffset());
        int pos = textPane.search("lazy");;
        assertEquals(40, pos);
        textPane.setText("The quick brown fox jumps over the lazy dog.\nThe quick red fox jumps over the lazy cat.");
        lineResults = textPane.highlight("fox", 0);
        assertEquals(2, lineResults.size());
        lineResult = lineResults.get(0);
        assertEquals(0, lineResult.getLineNumber());
        assertEquals(0, lineResult.getLineStartOffset());
        assertEquals(22, lineResult.getLineEndOffset());
        lineResult = lineResults.get(1);
        assertEquals(1, lineResult.getLineNumber());
        assertEquals(0, lineResult.getLineStartOffset());
        assertEquals(33, lineResult.getLineEndOffset());
        pos = textPane.search("cat", 42);
        assertEquals(32, pos);
    }

    @Test
    public void testRemoveHighlights() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        List<LineResult> lineResults = textPane.highlight("fox", 0);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(3, lineResult.getLineNumber());
        assertEquals(16, lineResult.getLineStartOffset());
        assertEquals(21, lineResult.getLineEndOffset());
        textPane.removeHighlights();
        lineResults = textPane.highlight("fox", 0);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testSearchHit() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        List<LineResult> lineResults = textPane.highlight("fox", 0);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        SearchHit hit = textPane.getSelectionModel().getSelectionForOffset(lineResult.getWordResults().get(0).start);
        assertEquals(lineResult.getWordResults().get(0).start, hit.getOffset());
        assertEquals(lineResult.getWordResults().get(0).end, hit.getEndOffset());
        textPane.setText("The quick brown fox jumps over the lazy dog.\nThe quick red fox jumps over the lazy cat.");
        lineResults = textPane.highlight("fox", 0);
        assertEquals(2, lineResults.size());
        lineResult = lineResults.get(0);
        hit = textPane.getSelectionModel().getSelectionForOffset(lineResult.getWordResults().get(0).start);
        assertEquals(lineResult.getWordResults().get(0).start, hit.getOffset());
        assertEquals(lineResult.getWordResults().get(0).end, hit.getEndOffset());
        lineResult = lineResults.get(1);
        hit = textPane.getSelectionModel().getSelectionForOffset(lineResult.getWordResults().get(0).start);
        assertEquals(lineResult.getWordResults().get(0).start, hit.getOffset());
        assertEquals(lineResult.getWordResults().get(0).end, hit.getEndOffset());
    }

    @Test
    public void testHighlighterPainter() {
        textPane.setText("The quick brown fox jumps over the lazy dog.");
        List<LineResult> lineResults = textPane.highlight("fox", 0);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        Highlighter highlighter = textPane.getHighlighter();
        HighlightPainter painter = highlighter.getHighlightPainter();
        assertEquals(Color.YELLOW, painter.getColor());
        textPane.setText("The quick brown fox jumps over the lazy dog.\nThe quick red fox jumps over the lazy cat.");
        lineResults = textPane.highlight("fox", 0);
        assertEquals(2, lineResults.size());
        lineResult = lineResults.get(0);
        painter = lineResult.getHighlighter().getHighlightPainter();
        assertEquals(Color.YELLOW, painter.getColor());
        lineResult = lineResults.get(1);
        painter = lineResult.getHighlighter().getHighlightPainter();
        assertEquals(Color.YELLOW, painter.getColor());
    }
}