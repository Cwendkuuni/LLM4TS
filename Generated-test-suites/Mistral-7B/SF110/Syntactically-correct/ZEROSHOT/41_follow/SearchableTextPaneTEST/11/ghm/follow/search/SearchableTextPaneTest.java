import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private static final String SEARCH_TERM = "JUnit";
 private static final int TAB_SIZE = 4;
 private static final int FLAGS = 0;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Monospaced", Font.BOLD, 14), TAB_SIZE);
 searchableTextPane.setPreferredSize(new Dimension(200, 200));
 }

 @After
 public void tearDown() {
 searchableTextPane = null;
 }

 @Test
 public void testConstructor() {
 assertNotNull(searchableTextPane);
 assertEquals(new Font("Monospaced", Font.BOLD, 14), searchableTextPane.getFont());
 assertEquals(TAB_SIZE, searchableTextPane.getTabSize());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyString() {
 final List<LineResult> lineResults = searchableTextPane.highlight("", FLAGS);
 assertNotNull(lineResults);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlightNullTerm() {
 final List<LineResult> lineResults = searchableTextPane.highlight(null, FLAGS);
 assertNotNull(lineResults);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlightTerm() {
 final String text = "JUnit is a Java framework for writing repeatable tests.";
 searchableTextPane.setText(text);
 final List<LineResult> lineResults = searchableTextPane.highlight(SEARCH_TERM, FLAGS);
 assertNotNull(lineResults);
 assertFalse(lineResults.isEmpty());
 for (final LineResult lineResult : lineResults) {
 final int lineNumber = lineResult.getLineNumber();
 final int lineStart = lineResult.getLineStart();
 final int lineEnd = lineResult.getLineEnd();
 System.out.println("Line " + lineNumber + " (" + lineStart + ", " + lineEnd + "): " + text.substring(lineStart, lineEnd));
 final int length = lineEnd - lineStart;
 final int highlightStart = lineStart + searchableTextPane.getLineStartOffset(lineNumber - 1) + searchableTextPane.getLineEndOffset(lineNumber - 1) + 1;
 final int highlightEnd = highlightStart + length;
 searchableTextPane.setCaretPosition(highlightEnd);
 final HighlightManager highlightManager = searchableTextPane.getHighlighter();
 final Highlight[] highlights = highlightManager.getHighlights();
 assertEquals(1, highlights.length);
 final Highlight highlight = highlights[0];
 assertEquals(Color.YELLOW, highlight.getPainter().getColor());
 }
 }

 @Test
 public void testSearchEmptyString() {
 assertEquals(-1, searchableTextPane.search(""));
 }

 @Test
 public void testSearchNullTerm() {
 assertNull(searchableTextPane.search(null));
 }

 @Test
 public void testSearchTerm() {
 final String text = "JUnit is a Java framework for writing repeatable tests.";
 searchableTextPane.setText(text);
 assertEquals(searchableTextPane.getDocument().getLength() - SEARCH_TERM.length(), searchableTextPane.search(SEARCH_TERM));
 }

 @Test
 public void testSearchTermWithPreviousSearch() {
 final String text = "JUnit is a Java framework for writing repeatable tests. JUnit is a powerful testing tool.";
 searchableTextPane.setText(text);
 searchableTextPane.search("JUnit");
 assertEquals(searchableTextPane.getDocument().getLength() - SEARCH_TERM.length(), searchableTextPane.search("JUnit"));
 }

 @Test
 public void testSearchTermAtEndOfText() {
 final String text = "JUnit is a Java framework for writing repeatable tests.";
 searchableTextPane.setText(text);
 assertEquals(searchableTextPane.getDocument().getLength() - SEARCH_TERM.length(), searchableTextPane.search(SEARCH_TERM));
 }

 @Test
 public void testSearchTermInMiddleOfText() {
 final String text = "JUnit is a Java framework for writing repeatable tests. JUnit is a powerful testing tool.";
 searchableTextPane.setText(text);
 assertEquals(searchableTextPane.getDocument().getLength() / 2 + SEARCH_TERM.length() / 2, searchableTextPane.search(SEARCH_TERM));
 }

 @Test(expected = BadLocationException.class)
 public void testSearchTermWithException() {
 searchableTextPane.setText("JUnit is a Java framework for writing repeatable tests.");
 searchableTextPane.search("InvalidTerm");
 }
}