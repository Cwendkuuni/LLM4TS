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
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.Line;
import javax.swing.text.LineMapper;
import javax.swing.text.LineNumberFormatter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TextAction;
import javax.swing.text.View;

public class SearchableTextPaneTest {

    private SearchableTextPane textPane;
    private static final String TEST_TEXT = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.";

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 4);
        textPane.setText(TEST_TEXT);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertTrue(textPane.getScrollableTracksViewportWidth());

        JFrame frame = new JFrame();
        frame.setSize(100, 100);
        frame.add(textPane);
        assertFalse(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() {
        List<String> searchTerms = Arrays.asList("brown", "quick", "lazy", "fox", "jumps", "over", "the");

        for (String term : searchTerms) {
            List<LineResult> lineResults = textPane.highlight(term, 0);
            assertEquals(1, lineResults.size());

            LineResult lineResult = lineResults.get(0);
            int lineNumber = lineResult.getLineNumber();
            Line line = textPane.getLineAtOffset(lineResult.getLineStartOffset());

            HighlightPainter painter = textPane.getHighlighter().getHighlightPainter();
            int startOffset = lineResult.getWordResults().get(0).getStart();
            int endOffset = lineResult.getWordResults().get(0).getEnd();

            assertNotNull(lineResult.getWordResults());
            assertEquals(1, lineResult.getWordResults().size());

            TextAction highlightAction = textPane.getActionMap().get("SearchHighlight");
            highlightAction.actionPerformed(null);

            LineNumberFormatter formatter = (LineNumberFormatter) textPane.getPrintable().getDocument().getDocumentElement().getComponent(0).getEditorKit().getDocumentFormater();
            String expectedLineNumber = formatter.format(lineNumber + 1);

            int lineHeight = textPane.getLineHeight(lineNumber);
            int lineBaseline = line.getBounds().y + lineHeight - textPane.getFontMetric(textPane.getFont()).getHeight();

            // Verify that the line has been highlighted
            View view = textPane.viewAt(lineNumber, 0);
            LineMapper mapper = textPane.getLineMapper();
            Line lineView = mapper.getLineForOffset(view.getStartOffset());
            assertEquals(lineView, line);
            assertNotNull(lineView.getLogicalPosition(startOffset));
            assertNotNull(lineView.getLogicalPosition(endOffset));
            assertEquals(painter, lineView.getCharacterAttributes().getBackground());
            assertEquals(expectedLineNumber, textPane.getLineNumberFormatter().formatLineNumber(lineNumber + 1));
            assertEquals(lineBaseline, textPane.getViewport().getViewPosition().y);

            // Verify that the first word in the line has been highlighted
            View wordView = textPane.getLineView(lineNumber).findElement(startOffset, null);
            assertEquals(wordView, lineView.getFirstElement());
            assertEquals(painter, wordView.getCharacterAttributes().getBackground());
        }
    }

    @Test
    public void testSearch() {
        List<String> searchTerms = Arrays.asList("brown", "quick", "lazy", "fox", "jumps", "over", "the");

        for (String term : searchTerms) {
            int pos = textPane.search(term);
            if (pos == -1) {
                assertNull(textPane.lastSearchTerm);
            } else {
                assertEquals(term, textPane.lastSearchTerm);
            }
        }

        String newTerm = "cat";
        int pos = textPane.search(newTerm);
        assertNull(textPane.lastSearchTerm);
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        String text = "Line 1: The quick brown fox jumps over the lazy dog. Line 2: The quick brown fox jumps over the lazy dog.";
        SearchableTextPane textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 4);
        textPane.setText(text);

        List<WordResult> searchResults = new ArrayList<>();
        searchResults.add(new WordResult("quick", 14, 17));
        searchResults.add(new WordResult("brown", 18, 22));
        searchResults.add(new WordResult("jumps", 41, 46));
        searchResults.add(new WordResult("lazy", 52, 56));
        searchResults.add(new WordResult("dog", 59, 62));

        List<LineResult> lineResults = textPane.convertWords2Lines(searchResults);

        assertEquals(2, lineResults.size());
        assertEquals(1, lineResults.get(0).getWordResults().size());
        assertEquals(1, lineResults.get(1).getWordResults().size());

        LineResult lineResult1 = lineResults.get(0);
        LineResult lineResult2 = lineResults.get(1);

        assertEquals(1, lineResult1.getLineNumber());
        assertEquals(14, lineResult1.getLineStartOffset());
        assertEquals(63, lineResult1.getLineEndOffset());

        assertEquals(2, lineResult2.getLineNumber());
        assertEquals(52, lineResult2.getLineStartOffset());
        assertEquals(63, lineResult2.getLineEndOffset());
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        List<WordResult> searchResults = new ArrayList<>();
        WordResult wordResult1 = new WordResult("quick", 14, 17);
        WordResult wordResult2 = new WordResult("brown", 18, 22);
        searchResults.add(wordResult1);
        searchResults.add(wordResult2);

        LineResult lineResult = new LineResult(1, 0, 0);

        textPane.updateWordResult(wordResult1, lineResult);
        textPane.updateWordResult(wordResult2, lineResult);

        assertEquals(2, lineResult.getWordResults().size());

        WordResult updatedWordResult1 = lineResult.getWordResults().get(0);
        WordResult updatedWordResult2 = lineResult.getWordResults().get(1);

        assertEquals(14, updatedWordResult1.getStart());
        assertEquals(17, updatedWordResult1.getEnd());
        assertEquals(1, updatedWordResult1.parent.lineNumber);

        assertEquals(18, updatedWordResult2.getStart());
        assertEquals(22, updatedWordResult2.getEnd());
        assertEquals(1, updatedWordResult2.parent.lineNumber);
    }

    @Test
    public void testRemoveHighlights() {
        SwingUtilities.invokeLater(() -> {
            SearchableTextPane textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 4);
            textPane.setText("The quick brown fox jumps over the lazy dog.");

            List<WordResult> searchResults = new ArrayList<>();
            WordResult wordResult1 = new WordResult("quick", 14, 17);
            WordResult wordResult2 = new WordResult("brown", 18, 22);
            searchResults.add(wordResult1);
            searchResults.add(wordResult2);

            textPane.highlight("quick", 0);
            textPane.highlight("brown", 0);

            assertEquals(2, textPane.getHighlighter().getHighlightCount());

            textPane.removeHighlights();

            assertEquals(0, textPane.getHighlighter().getHighlightCount());
        });

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);
            frame.setVisible(true);
            frame.add(new JScrollPane(textPane));
        });
    }
}