package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private String term;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 4);
 term = "test";
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.add(searchableTextPane);
 frame.pack();
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlight() throws Exception {
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(4, lineResult.getEndOffset());
 assertEquals(Color.YELLOW, lineResult.getHighlighterPainter().getColor());
 }

 @Test
 public void testEmptySearch() throws Exception {
 List<LineResult> lineResults = searchableTextPane.highlight("", 0);
 assertTrue(lineResults.isEmpty());
 }

 @Test(expected = Exception.class)
 public void testBadSearch() throws Exception {
 searchableTextPane.highlight(null, 0);
 fail("Expected Exception");
 }

 @Test
 public void testSearch() throws Exception {
 int pos = searchableTextPane.search(term);
 assertEquals(-1, pos);

 String text = "This is a test text for SearchableTextPane.";
 searchableTextPane.setText(text);
 pos = searchableTextPane.search(term);
 assertEquals(0, pos);
 }

 @Test
 public void testSearchPrevious() throws Exception {
 String text = "This is a test text for SearchableTextPane.";
 searchableTextPane.setText(text);
 searchableTextPane.search(term);
 int pos = searchableTextPane.search(term, 1);
 assertEquals(1, pos);
 }

 @Test(expected = Exception.class)
 public void testBadSearchPosition() throws Exception {
 searchableTextPane.search(term, -1);
 fail("Expected Exception");
 }

 @Test
 public void testEmptySearchText() {
 int pos = searchableTextPane.search("");
 assertEquals(-1, pos);
 }

 @Test
 public void testRemoveHighlights() throws Exception {
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 searchableTextPane.removeHighlights();
 assertTrue(lineResults.isEmpty());
 }
}