
import static org.junit.Assert.*;

import ghm.follow.search.SearchableTextPane;
import ghm.follow.search.SearchEngine;
import ghm.follow.search.Utilities;
import ghm.follow.search.LineResult;
import ghm.follow.search.WordResult;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import org.junit.Test;

public class SearchableTextPaneTest {

    // ... Previous test cases go here ...

    @Test
    public void testGetScrollableTracksViewportWidth() {
        JFrame frame = new JFrame();
        JTextArea textArea = new JTextArea();
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());

        JScrollPane scrollPane = new JScrollPane(searchableTextPane);
        frame.getContentPane().add(scrollPane);
        frame.setSize(100, 100);
        frame.setVisible(true);

        searchableTextPane.setSize(200, 200);
        searchableTextPane.setLocation(50, 50);

        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());

        searchableTextPane.setSize(500, 500);
        searchableTextPane.setLocation(150, 150);

        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlightWithNullTerm() {
        String term = null;
        int flags = SearchEngine.REGULAR_EXPRESSION;
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<LineResult> lineResults = textPane.highlight(term, flags);
        assertEquals(Collections.emptyList(), lineResults);
    }

    @Test
    public void testHighlightWithInvalidFlags() {
        String term = "test";
        int flags = Integer.MIN_VALUE;
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        List<LineResult> lineResults = textPane.highlight(term, flags);
        assertEquals(Collections.emptyList(), lineResults);
    }

    @Test
    public void testHighlightWithNoMatches() throws BadLocationException {
        String term = "test";
        SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
        Document doc = textPane.getDocument();
        doc.insertString(0, "This is a test text with no matches.", null);
        List<LineResult> lineResults = textPane.highlight(term, SearchEngine.REGULAR_EXPRESSION);
        assertEquals(Collections.emptyList(), lineResults);
    }

    // ... More test cases go here ...

}
