package ghm.follow.search;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private static final String TEST_TERM = "JUnit";

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 12), 4);
 }

 @Test
 public void testHighlight_EmptyText() {
 searchableTextPane.highlight(null, 0);
 List<LineResult> lineResults = searchableTextPane.getHighlightedLines();
 Assert.assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlight_EmptySearchTerm() {
 searchableTextPane.setText("");
 searchableTextPane.highlight(TEST_TERM, 0);
 List<LineResult> lineResults = searchableTextPane.getHighlightedLines();
 Assert.assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlight_ValidSearchTerm() {
 searchableTextPane.setText("This is a SearchableTextPane where we test JUnit.");
 searchableTextPane.highlight(TEST_TERM, 0);
 List<LineResult> lineResults = searchableTextPane.getHighlightedLines();
 Assert.assertEquals(1, lineResults.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHighlight_NullSearchTerm() {
 searchableTextPane.highlight(null, 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHighlight_NegativePosition() {
 searchableTextPane.highlight(TEST_TERM, -1);
 }

 @Test
 public void testSearch_EmptyText() {
 searchableTextPane.search(TEST_TERM);
 int position = searchableTextPane.getSearchPosition();
 Assert.assertEquals(-1, position);
 }

 @Test
 public void testSearch_EmptySearchTerm() {
 searchableTextPane.setText("This is a SearchableTextPane where we test JUnit.");
 searchableTextPane.search("");
 int position = searchableTextPane.getSearchPosition();
 Assert.assertEquals(-1, position);
 }

 @Test
 public void testSearch_ValidSearchTerm() {
 searchableTextPane.setText("This is a SearchableTextPane where we test JUnit.");
 searchableTextPane.search(TEST_TERM);
 int position = searchableTextPane.getSearchPosition();
 Assert.assertEquals(searchableTextPane.getCaretPosition().getX(), position);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearch_NullSearchTerm() {
 searchableTextPane.search(null);
 }

 @Test
 public void testRemoveHighlights() {
 searchableTextPane.highlight(TEST_TERM, 0);
 List<LineResult> lineResults = searchableTextPane.getHighlightedLines();
 Assert.assertEquals(1, lineResults.size());
 searchableTextPane.removeHighlights();
 lineResults = searchableTextPane.getHighlightedLines();
 Assert.assertEquals(0, lineResults.size());
 }
}