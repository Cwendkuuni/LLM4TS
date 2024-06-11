package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ghm.follow.search.SearchableTextPane;
import ghm.follow.search.SearchEngine;
import ghm.follow.search.Utilities;
import ghm.follow.search.WordResult;
import ghm.follow.search.LineResult;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private SearchEngine searchEngine;

 @Before
 public void setup() {
 textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
 searchEngine = new SearchEngine(SearchEngine.REGULAR_EXPRESSION);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 final Component parent = new JFrame();
 parent.add(textPane);
 parent.pack();
 parent.setSize(300, 300);
 assertTrue(textPane.getScrollableTracksViewportWidth());

 parent.dispose();
 }

 @Test
 public void testHighlightEmptyString() {
 textPane.setText("This is a test text pane.");
 textPane.highlight("", SearchEngine.REGULAR_EXPRESSION);
 assertFalse(textPane.getHighlighter().hasHighlightAt(0, 0));
 }

 @Test
 public void testHighlightSingleWord() {
 textPane.setText("This is a test text pane.");
 final int pos = textPane.getDocument().getLength();
 textPane.insertString(pos, "test", null);
 textPane.highlight("test", SearchEngine.REGULAR_EXPRESSION);
 List<LineResult> lineResults = textPane.getLineResults();
 List<WordResult> wordResults = lineResults.get(0).getWordResults();
 assertEquals(1, wordResults.size());
 assertEquals("test", wordResults.get(0).getWord());
 assertTrue(textPane.getHighlighter().hasHighlightAt(textPane.getDocument().getLength() - 5, 4));
 }

 @Test
 public void testHighlightMultipleWords() {
 textPane.setText("This is a test text pane.");
 textPane.insertString(textPane.getDocument().getLength(), " multiple words ", null);
 textPane.highlight("multiple words", SearchEngine.REGULAR_EXPRESSION);
 List<LineResult> lineResults = textPane.getLineResults();
 List<WordResult> wordResults = lineResults.get(1).getWordResults();
 assertEquals(2, wordResults.size());
 assertEquals("multiple", wordResults.get(0).getWord());
 assertEquals("words", wordResults.get(1).getWord());
 assertTrue(textPane.getHighlighter().hasHighlightAt(textPane.getDocument().getLength() - 4, 6));
 }

 @Test
 public void testHighlightCaseInsensitive() {
 textPane.setText("This is a test text pane.");
 textPane.insertString(textPane.getDocument().getLength(), " Multiple Words ", null);
 textPane.highlight("Multiple words", SearchEngine.CASE_INSENSITIVE);
 List<LineResult> lineResults = textPane.getLineResults();
 List<WordResult> wordResults = lineResults.get(1).getWordResults();
 assertEquals(2, wordResults.size());
 assertEquals("Multiple", wordResults.get(0).getWord());
 assertEquals("words", wordResults.get(1).getWord());
 assertTrue(textPane.getHighlighter().hasHighlightAt(textPane.getDocument().getLength() - 4, 6));
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightNullString() {
 textPane.setText("This is a test text pane.");
 textPane.highlight(null, SearchEngine.REGULAR_EXPRESSION);
 }

 @Test
 public void testRemoveHighlights() {
 textPane.setText("This is a test text pane.");
 textPane.highlight("test", SearchEngine.REGULAR_EXPRESSION);
 List<LineResult> lineResults = textPane.getLineResults();
 List<WordResult> wordResults = lineResults.get(0).getWordResults();
 assertTrue(textPane.getHighlighter().hasHighlightAt(textPane.getDocument().getLength() - 5, 4));
 textPane.removeHighlights();
 assertFalse(textPane.getHighlighter().hasHighlightAt(textPane.getDocument().getLength() - 5, 4));
 }

 @Test
 public void testSearchEmptyString() {
 textPane.setText("This is a test text pane.");
 int pos = textPane.search("");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchSingleWord() {
 textPane.setText("This is a test text pane.");
 textPane.insertString(textPane.getDocument().getLength(), " test ", null);
 int pos = textPane.search("test");
 assertEquals(textPane.getDocument().getLength() - 4, pos);
 }

 @Test
 public void testSearchMultipleWords() {
 textPane.setText("This is a test text pane.");
 textPane.insertString(textPane.getDocument().getLength(), " multiple words ", null);
 int pos = textPane.search("multiple words");
 assertEquals(textPane.getDocument().getLength() - 4, pos);
 }

 @Test
 public void testSearchCaseInsensitive() {
 textPane.setText("This is a test text pane.");
 textPane.insertString(textPane.getDocument().getLength(), " Multiple Words ", null);
 int pos = textPane.search("Multiple words");
 assertEquals(textPane.getDocument().getLength() - 4, pos);
 }

 @Test(expected = BadLocationException.class)
 public void testSearchNullString() {
 textPane.setText("This is a test text pane.");
 int pos = textPane.search(null);
 }

 @Test
 public void testSearchPreviousWord() {
 textPane.setText("This is a test text pane.");
 textPane.insertString(textPane.getDocument().getLength(), " test test test test multiple words test test test ", null);
 int pos = textPane.search("multiple words");
 assertEquals(textPane.getDocument().getLength() - 12, pos);
 int prevPos = textPane.searchPrevious("test");
 assertEquals(textPane.getDocument().getLength() - 14, prevPos);
 }

 @Test
 public void testHighlightAndSearch() {
 textPane.setText("This is a test text pane.");
 textPane.highlight("test", SearchEngine.REGULAR_EXPRESSION);
 List<LineResult> lineResults = textPane.getLineResults();
 List<WordResult> wordResults = lineResults.get(0).getWordResults();
 assertEquals(1, wordResults.size());
 assertEquals("test", wordResults.get(0).getWord());
 assertTrue(textPane.getHighlighter().hasHighlightAt(textPane.getDocument().getLength() - 4, 4));
 int pos = textPane.search("test");
 assertEquals(textPane.getDocument().getLength() - 4, pos);
 }

 @Test
 public void testRemoveHighlightsAfterSearch() {
 textPane.setText("This is a test text pane.");
 textPane.highlight("test", SearchEngine.REGULAR_EXPRESSION);
 List<LineResult> lineResults = textPane.getLineResults();
 List<WordResult> wordResults = lineResults.get(0).getWordResults();
 assertEquals(1, wordResults.size());
 assertEquals("test", wordResults.get(0).getWord());
 assertTrue(textPane.getHighlighter().hasHighlightAt(textPane.getDocument().getLength() - 4, 4));
 int pos = textPane.search("test");
 assertEquals(textPane.getDocument().getLength() - 4, pos);
 textPane.removeHighlights();
 assertFalse(textPane.getHighlighter().hasHighlightAt(textPane.getDocument().getLength() - 4, 4));
 }

 @Test
 public void testHighlightAndSearchCaseInsensitive() {
 textPane.setText("This is a test text pane.");
 textPane.highlight("Test", SearchEngine.CASE_INSENSITIVE);
 List<LineResult> lineResults = textPane.getLineResults();
 List<WordResult> wordResults = lineResults.get(0).getWordResults();
 assertEquals(1, wordResults.size());
 assertEquals("Test", wordResults.get(0).getWord());
 assertTrue(textPane.getHighlighter().hasHighlightAt(textPane.getDocument().getLength() - 3, 3));
 int pos = textPane.search("test");
 assertEquals(textPane.getDocument().getLength() - 4, pos);
 int posCaseInsensitive = textPane.search("Test");
 assertEquals(textPane.getDocument().getLength() - 3, posCaseInsensitive);
 }

 @Test
 public void testSearchAndHighlightSameTerm() {
 textPane.setText("This is a test text pane.");
 textPane.highlight("test", SearchEngine.REGULAR_EXPRESSION);
 List<LineResult> lineResults = textPane.getLineResults();
 List<WordResult> wordResults = lineResults.get(0).getWordResults();
 assertEquals(1, wordResults.size());
 assertEquals("test", wordResults.get(0).getWord());
 assertTrue(textPane.getHighlighter().hasHighlightAt(textPane.getDocument().getLength() - 4, 4));
 int pos = textPane.search("test");
 assertEquals(textPane.getDocument().getLength() - 4, pos);
 textPane.removeHighlights();
 textPane.highlight("test", SearchEngine.REGULAR_EXPRESSION);
 List<LineResult> lineResults2 = textPane.getLineResults();
 List<WordResult> wordResults2 = lineResults2.get(0).getWordResults();
 assertEquals(1, wordResults2.size());
 assertEquals("test", wordResults2.get(0).getWord());
 assertTrue(textPane.getHighlighter().hasHighlightAt(textPane.getDocument().getLength() - 4, 4));
 }

 @Test
 public void testHighlightAndSearchSameTermCaseInsensitive() {
 textPane.setText("This is a test text pane.");
 textPane.highlight("Test", SearchEngine.CASE_INSENSITIVE);
 List<LineResult> lineResults = textPane.getLineResults();
 List<WordResult> wordResults = lineResults.get(0).getWordResults();
 assertEquals(1, wordResults.size());
 assertEquals("Test", wordResults.get(0).getWord());
 assertTrue(textPane.getHighlighter().hasHighlightAt(textPane.getDocument().getLength() - 3, 3));
 int pos = textPane.search("test");
 assertEquals(textPane.getDocument().getLength() - 4, pos);
 int posCaseInsensitive = textPane.search("Test");
 assertEquals(textPane.getDocument().getLength() - 3, posCaseInsensitive);
 textPane.removeHighlights();
 textPane.highlight("Test", SearchEngine.CASE_INSENSITIVE);
 List<LineResult> lineResults2 = textPane.getLineResults();
 List<WordResult> wordResults2 = lineResults2.get(0).getWordResults();
 assertEquals(1, wordResults2.size());
 assertEquals("Test", wordResults2.get(0).getWord());
 assertTrue(textPane.getHighlighter().hasHighlightAt(textPane.getDocument().getLength() - 3, 3));
 }

 @Test
 public void testHighlightAndSearchDifferentTerms() {
 textPane.setText("This is a test text pane.");
 textPane.highlight("test", SearchEngine.REGULAR_EXPRESSION);
 List<LineResult> lineResults = textPane.getLineResults();
 List<WordResult> wordResults = lineResults.get(0).getWordResults();
 assertEquals(1, wordResults.size());
 assertEquals("test", wordResults.get(0).getWord());
 assertTrue(textPane.getHighlighter().hasHighlightAt(textPane.getDocument().getLength() - 4, 4));
 int posTest = textPane.search("test");
 assertEquals(textPane.getDocument().getLength() - 4, posTest);
 textPane.removeHighlights();
 textPane.highlight("multiple words", SearchEngine.REGULAR_EXPRESSION);
 List<LineResult> lineResults2 = textPane.getLineResults();
 List<WordResult> wordResults2 = lineResults2.get(1).getWordResults();
 assertEquals(2, wordResults2.size());
 assertEquals("multiple", wordResults2.get(0).getWord());
 assertEquals("words", wordResults2.get(1).getWord());
 assertFalse(textPane.getHighlighter().hasHighlightAt(textPane.getDocument().getLength() - 4, 4));
 int posMultipleWords = textPane.search("multiple words");
 assertEquals(textPane.getDocument().getLength() - 10, posMultipleWords);
 }

 @Test
 public void testSearchBackwardFromEnd() {
 textPane.setText("This is a test text pane.");
 textPane.insertString(textPane.getDocument().getLength(), " multiple words multiple words test test test test test ", null);
 int pos = textPane.search("test");
 assertEquals(textPane.getDocument().getLength() - 4, pos);
 int posFromEnd = textPane.searchPrevious("test");
 assertEquals(textPane.getDocument().getLength() - 14, posFromEnd);
 }

 @Test
 public void testSearchAndReplace() {
 textPane.setText("This is a test text pane.");
 textPane.insertString(textPane.getDocument().getLength(), " multiple words multiple words ", null);
 textPane.searchAndReplace("multiple words", "replaced", SearchEngine.REGULAR_EXPRESSION);
 assertEquals("This is a test text pane.\nreplaced replaced ", textPane.getText());
 }

 @Test
 public void testSearchAndReplaceAll() {
 textPane.setText("This is a test text pane.");
 textPane.insertString(textPane.getDocument().getLength(), " multiple words multiple words multiple words multiple words ", null);
 textPane.searchAndReplaceAll("multiple words", "replaced", SearchEngine.REGULAR_EXPRESSION);
 assertEquals("This is a test text pane.\nreplaced replaced replaced replaced replaced replaced ", textPane.getText());
 }

 @Test
 public void testSearchAndReplaceWithCaseInsensitivity() {
 textPane.setText("This is a test text pane.");
 textPane.insertString(textPane.getDocument().getLength(), " Multiple words Multiple words Multiple words Multiple words ", null);
 textPane.searchAndReplace("Multiple words", "replaced", SearchEngine.CASE_INSENSITIVE);
 assertEquals("This is a test text pane.\nreplaced replaced replaced replaced replaced replaced ", textPane.getText());
 }

 @Test
 public void testHighlightWithCustomPaint() {
 textPane.setText("This is a test text pane.");
 textPane.highlight("test", new CustomHighlighter.DefaultHighlightPainter(Color.RED));
 List<LineResult> lineResults = textPane.getLineResults();
 List<WordResult> wordResults = lineResults.get(0).getWordResults();
 assertEquals(1, wordResults.size());
 assertEquals("test", wordResults.get(0).getWord());
 assertTrue(textPane.getHighlighter().hasHighlightAt(textPane.getDocument().getLength() - 4, 4));
 }

 @Test
 public void testSearchWithCustomPaint() {
 textPane.setText("This is a test text pane.");
 textPane.highlight("test", new CustomHighlighter.DefaultHighlightPainter(Color.RED));
 List<LineResult> lineResults = textPane.getLineResults();
 List<WordResult> wordResults = lineResults.get(0).getWordResults();
 assertEquals(1, wordResults.size());
 assertEquals("test", wordResults.get(0).getWord());
 int pos = textPane.search("test");
 assertEquals(textPane.getDocument().getLength() - 4, pos);
 textPane.removeHighlights();
 textPane.highlight("test", new CustomHighlighter.DefaultHighlightPainter(Color.BLUE));
 List<LineResult> lineResults2 = textPane.getLineResults();
 List<WordResult> wordResults2 = lineResults2.get(0).getWordResults();
 assertEquals(1, wordResults2.size());
 assertEquals("test", wordResults2.get(0).getWord());
 int pos2 = textPane.search("test");
 assertEquals(textPane.getDocument().getLength() - 4, pos2);
 assertEquals(Color.BLUE, textPane.getHighlighter().getHighlightPainter().getColor());
 }
}