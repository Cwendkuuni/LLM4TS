package ghm.follow.search;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;
import java.util.logging.Logger;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private static final String SEARCH_TERM = "Java";
 private static final int TAB_SIZE = 4;
 private static final Logger LOGGER = Logger.getLogger(SearchableTextPaneTest.class.getName());

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Monospaced", Font.BOLD, 14), TAB_SIZE);
 }

 @Test
 public void testSearchableTextPaneConstruction() {
 assertNotNull(textPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightNullTerm() {
 textPane.highlight(null, 0);
 assertEquals(0, textPane.getDocument().getLength());
 }

 @Test
 public void testHighlightEmptyTerm() {
 List<LineResult> lineResults = textPane.highlight("", 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightSingleWord() {
 String text = "This is a Java text pane.";
 textPane.setText(text);
 textPane.highlight(SEARCH_TERM, 0);
 List<LineResult> lineResults = textPane.highlight(SEARCH_TERM, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(text.length(), lineResult.getEndOffset());
 }

 @Test
 public void testHighlightMultipleOccurrences() {
 String text = "This is a Java text pane. Java is a programming language.";
 textPane.setText(text);
 textPane.highlight(SEARCH_TERM, 0);
 List<LineResult> lineResults = textPane.highlight(SEARCH_TERM, 0);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(1, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getStartOffset());
 assertEquals(11, lineResult1.getEndOffset());
 assertEquals(2, lineResult2.getLineNumber());
 assertEquals(56, lineResult2.getStartOffset());
 assertEquals(65, lineResult2.getEndOffset());
 }

 @Test
 public void testHighlightMultipleWords() {
 String text = "This is a Java text pane. Java is a programming language.";
 textPane.setText(text);
 List<String> terms = Arrays.asList("Java", "text", "pane");
 for (String term : terms) {
 textPane.highlight(term, 0);
 List<LineResult> lineResults = textPane.highlight(term, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 int lineNumber = lineResult.getLineNumber();
 for (int i = 0; i < terms.size(); i++) {
 String searchTerm = terms.get(i);
 int expectedStart = text.indexOf(searchTerm, lineResult.getStartOffset());
 int expectedEnd = expectedStart + searchTerm.length();
 assertEquals(expectedStart, lineResult.getWordResults().get(i).getStart());
 assertEquals(expectedEnd, lineResult.getWordResults().get(i).getEnd());
 }
 }
 }

 @Test
 public void testSearchNullTerm() {
 assertEquals(-1, textPane.search(null));
 }

 @Test
 public void testSearchEmptyTerm() {
 assertEquals(-1, textPane.search(""));
 }

 @Test
 public void testSearchSingleOccurrence() {
 textPane.setText("This is a Java text pane.");
 assertEquals(8, textPane.search(SEARCH_TERM));
 }

 @Test
 public void testSearchMultipleOccurrences() {
 textPane.setText("This is a Java text pane. Java is a programming language.");
 assertEquals(8, textPane.search(SEARCH_TERM));
 assertEquals(56, textPane.search(SEARCH_TERM));
 }

 @Test
 public void testSearchPreviousTerm() {
 textPane.setText("This is a Java text pane. Java is a programming language.");
 textPane.search(SEARCH_TERM);
 textPane.search(SEARCH_TERM);
 assertEquals(56, textPane.search(SEARCH_TERM));
 }

 @Test
 public void testSearchPreviousNonTerm() {
 textPane.setText("This is a Java text pane. Java is a programming language.");
 textPane.search(SEARCH_TERM);
 textPane.search(SEARCH_TERM);
 assertEquals(-1, textPane.search("is"));
 }

 @Test
 public void testRemoveHighlights() {
 textPane.setText("This is a Java text pane.");
 textPane.highlight(SEARCH_TERM, 0);
 List<LineResult> lineResults = textPane.highlight(SEARCH_TERM, 0);
 textPane.removeHighlights();
 assertEquals(0, lineResults.size());
 }

 @Test(expected = BadLocationException.class)
 public void testSearchBadLocationException() {
 textPane.search(-1);
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightBadLocationException() {
 textPane.highlight("-1", 0);
 }
}