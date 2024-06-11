import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private List<LineResult> lineResults;
 private static final String SEARCH_TERM_1 = "Java";
 private static final String SEARCH_TERM_2 = "Swing";
 private static final String SEARCH_TERM_3 = "Java Swing";
 private static final String SEARCH_TERM_4 = "";

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
 }

 @After
 public void tearDown() {
 searchableTextPane = null;
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.setSize(400, 400);
 frame.add(searchableTextPane);
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightWithValidTerm() {
 String text = "This is a Java Swing component. Java Swing is used for creating GUIs.";
 searchableTextPane.setText(text);
 lineResults = searchableTextPane.highlight(SEARCH_TERM_1, 0);
 assertEquals(2, lineResults.size());
 lineResults = searchableTextPane.highlight(SEARCH_TERM_3, 0);
 assertEquals(1, lineResults.size());
 }

 @Test
 public void testHighlightWithEmptyTerm() {
 searchableTextPane.setText("This is a test.");
 lineResults = searchableTextPane.highlight(SEARCH_TERM_4, 0);
 assertNull(lineResults);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHighlightWithNullTerm() {
 searchableTextPane.setText("This is a test.");
 lineResults = searchableTextPane.highlight(null, 0);
 }

 @Test
 public void testSearchWithValidTerm() {
 String text = "This is line 1. This is line 2. This is line 3.";
 searchableTextPane.setText(text);
 int pos = searchableTextPane.search(SEARCH_TERM_1, 0);
 assertEquals(-1, pos);
 pos = searchableTextPane.search(SEARCH_TERM_2, 0);
 assertEquals(3, pos);
 pos = searchableTextPane.search(SEARCH_TERM_3, 0);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchWithEmptyTerm() {
 String text = "This is a test.";
 searchableTextPane.setText(text);
 int pos = searchableTextPane.search(SEARCH_TERM_4, 0);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchWithNullTerm() {
 String text = "This is a test.";
 searchableTextPane.setText(text);
 int pos = searchableTextPane.search(null, 0);
 assertEquals(-1, pos);
 }
}