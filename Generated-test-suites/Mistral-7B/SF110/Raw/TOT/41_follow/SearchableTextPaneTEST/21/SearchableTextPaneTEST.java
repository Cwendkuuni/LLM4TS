package ghm.follow.search;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private List<LineResult> lineResults;
 private static final String SEARCH_TERM = "Java";

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 14), 4);
 lineResults = Collections.emptyList();
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlight_EmptySearchTerm() {
 textPane.highlight("", 0);
 assertEquals(lineResults, textPane.getLineResults());
 }

 @Test
 public void testHighlight_NullSearchTerm() {
 textPane.highlight(null, 0);
 assertEquals(lineResults, textPane.getLineResults());
 }

 @Test
 public void testHighlight_EmptyTextPane() {
 textPane.setText("");
 textPane.highlight(SEARCH_TERM, 0);
 assertEquals(lineResults, textPane.getLineResults());
 }

 @Test
 public void testHighlight_SingleResult() {
 textPane.setText("The quick brown fox jumps over the lazy Java developer.");
 textPane.highlight(SEARCH_TERM, 0);
 lineResults = Arrays.asList(new LineResult(0, 0, textPane.getDocument().getLength()));
 lineResults.get(0).addWord(new WordResult(textPane.getDocument().getLength() - 4, textPane.getDocument().getLength() - 1, "Java", 0));
 assertEquals(lineResults, textPane.getLineResults());
 }

 @Test
 public void testHighlight_MultipleResults() {
 textPane.setText("The quick brown fox jumps over the lazy Java developer.\n" +
         "Java is a popular programming language.");
 textPane.highlight(SEARCH_TERM, 0);
 lineResults = Arrays.asList(
         new LineResult(0, 0, textPane.getDocument().getLength() - 1),
         new LineResult(1, 0, textPane.getDocument().getLength())
 );
 lineResults.get(0).addWord(new WordResult(0, 13, "Java", 0));
 lineResults.get(0).addWord(new WordResult(textPane.getDocument().getLength() - 4, textPane.getDocument().getLength() - 1, "Java", 1));
 lineResults.get(1).addWord(new WordResult(0, 11, "Java", 0));
 assertEquals(lineResults, textPane.getLineResults());
 }

 @Test(expected = BadLocationException.class)
 public void testHighlight_Exception() {
 textPane.highlight(SEARCH_TERM, -1);
 }

 @Test
 public void testSearch_EmptySearchTerm() {
 assertEquals(-1, textPane.search(""));
 }

 @Test
 public void testSearch_NullSearchTerm() {
 assertEquals(-1, textPane.search(null));
 }

 @Test
 public void testSearch_EmptyTextPane() {
 assertEquals(-1, textPane.search(SEARCH_TERM));
 }

 @Test
 public void testSearch_SingleResult() {
 textPane.setText("The quick brown fox jumps over the lazy Java developer.");
 textPane.highlight(SEARCH_TERM, 0);
 assertEquals(0, textPane.search(SEARCH_TERM));
 }

 @Test
 public void testSearch_MultipleResults() {
 textPane.setText("The quick brown fox jumps over the lazy Java developer.\n" +
         "Java is a popular programming language.");
 textPane.highlight(SEARCH_TERM, 0);
 assertEquals(0, textPane.search(SEARCH_TERM));
 textPane.search(SEARCH_TERM);
 assertEquals(1, textPane.search(SEARCH_TERM));
 }

 @Test
 public void testSearch_LastSearchTermIsSame() {
 textPane.setText("The quick brown fox jumps over the lazy Java developer.");
 textPane.highlight(SEARCH_TERM, 0);
 assertEquals(0, textPane.search(SEARCH_TERM));
 textPane.search(SEARCH_TERM);
 assertEquals(1, textPane.search(SEARCH_TERM));
 textPane.search(SEARCH_TERM);
 assertEquals(-1, textPane.search(SEARCH_TERM));
 }

 @Test
 public void testSearch_LastSearchTermIsDifferent() {
 textPane.setText("The quick brown fox jumps over the lazy Java developer.");
 textPane.highlight("Java", 0);
 assertEquals(0, textPane.search("Java"));
 textPane.search("Java");
 assertEquals(1, textPane.search("Java"));
 textPane.search("Java");
 assertEquals(-1, textPane.search("Java"));
 textPane.search("Language");
 assertEquals(-1, textPane.search("Java"));
 }

 @Test
 public void testRemoveHighlights() {
 textPane.setText("The quick brown fox jumps over the lazy Java developer.");
 textPane.highlight(SEARCH_TERM, 0);
 textPane.removeHighlights();
 assertEquals(lineResults, textPane.getLineResults());
 }
}