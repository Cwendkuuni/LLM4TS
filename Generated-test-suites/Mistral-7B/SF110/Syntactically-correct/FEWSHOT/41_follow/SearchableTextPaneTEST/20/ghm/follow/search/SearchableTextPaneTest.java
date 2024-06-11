package ghm.follow.search;

import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

    private SearchableTextPane textPane;
    private Logger log;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
        log = Logger.getLogger(SearchableTextPane.class.getName());
    }

    @Test
    public void testSearchableTextPaneConstructor() {
        assertNotNull(textPane);
        assertEquals(14.0f, textPane.getFont().getSize2D().getWidth());
        assertEquals(8, textPane.getTabSize());
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        assertFalse(textPane.getScrollableTracksViewportWidth());

        JFrame frame = new JFrame();
        frame.add(textPane);
        frame.pack();

        assertTrue(textPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlightWithEmptyTerm() {
        String text = "This is a test text.";
        textPane.setText(text);

        List<LineResult> lineResults = textPane.highlight(null, 0);
        assertEquals(Collections.emptyList(), lineResults);
    }

    @Test
    public void testHighlightTermNotPresent() {
        String text = "This is a test text.";
        textPane.setText(text);

        List<LineResult> lineResults = textPane.highlight("zoo", 0);
        assertEquals(Collections.emptyList(), lineResults);
    }

    @Test
    public void testHighlightTermPresent() {
        String text = "This is a test text. This is a test for SearchableTextPane's highlight method.";
        textPane.setText(text);

        List<LineResult> lineResults = textPane.highlight("SearchableTextPane", 0);
        assertEquals(1, lineResults.size());

        LineResult lineResult = lineResults.get(0);
        assertEquals(2, lineResult.getLineNumber());
        assertEquals(65, lineResult.getStartOffset());
        assertEquals(96, lineResult.getEndOffset());
    }

    @Test
    public void testSearchWithEmptyTerm() {
        String text = "This is a test text.";
        textPane.setText(text);

        int pos = textPane.search("");
        assertEquals(-1, pos);
    }

    @Test
    public void testSearchTermNotPresent() {
        String text = "This is a test text.";
        textPane.setText(text);

        int pos = textPane.search("zoo");
        assertEquals(-1, pos);
    }

    @Test
    public void testSearchTermPresent() {
        String text = "This is a test text. This is a test for SearchableTextPane's search method.";
        textPane.setText(text);

        int pos = textPane.search("SearchableTextPane");
        assertEquals(2, pos);
    }

    @Test
    public void testSearchTermPresentAtStart() {
        String text = "SearchableTextPane is a test text.";
        textPane.setText(text);

        int pos = textPane.search("SearchableTextPane");
        assertEquals(0, pos);
    }

    @Test
    public void testSearchTermPresentAtEnd() {
        String text = "This is a test text. SearchableTextPane is a test.";
        textPane.setText(text);

        int pos = textPane.search("SearchableTextPane");
        assertEquals(textPane.getText().length(), pos);
    }

    @Test
    public void testSearchTermPresentInMiddle() {
        String text = "This is a test text. This is a test for SearchableTextPane's search method.";
        textPane.setText(text);

        int pos = textPane.search("SearchableTextPane");
        assertEquals(65, pos);
    }
}