package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private List<LineResult> lineResults;

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
 lineResults = new ArrayList<>();
 }

 @Test
 public void testSearchableTextPaneConstructor() {
 assertEquals(12, textPane.getFont().getSize2D().getHeight());
 assertEquals(8, textPane.getTabSize());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertFalse(textPane.getScrollableTracksViewportWidth());
 //TODO: Add test case for true condition
 }

 @Test
 public void testHighlightNullTerm() {
 textPane.highlight(null, 0);
 assertFalse(textPane.getHighlighter().hasHighlight(0));
 }

 @Test
 public void testHighlightEmptyTerm() {
 textPane.setText("Hello World");
 textPane.highlight("", 0);
 assertFalse(textPane.getHighlighter().hasHighlight(0));
 }

 @Test
 public void testHighlightSingleWord() {
 textPane.setText("The quick brown fox jumps over the lazy dog");
 textPane.highlight("fox", 0);
 assertTrue(textPane.getHighlighter().hasHighlight(3));
 assertTrue(textPane.getHighlighter().hasHighlight(4));
 assertTrue(textPane.getHighlighter().hasHighlight(5));
 assertFalse(textPane.getHighlighter().hasHighlight(6));
 lineResults = textPane.highlight("fox", 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(3, lineResult.getStartOffset());
 assertEquals(12, lineResult.getEndOffset());
 }

 @Test
 public void testHighlightMultipleWords() {
 textPane.setText("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog");
 textPane.highlight("quick brown fox", 0);
 assertTrue(textPane.getHighlighter().hasHighlight(0));
 assertTrue(textPane.getHighlighter().hasHighlight(1));
 assertTrue(textPane.getHighlighter().hasHighlight(2));
 assertTrue(textPane.getHighlighter().hasHighlight(3));
 assertTrue(textPane.getHighlighter().hasHighlight(4));
 assertTrue(textPane.getHighlighter().hasHighlight(5));
 assertTrue(textPane.getHighlighter().hasHighlight(6));
 assertTrue(textPane.getHighlighter().hasHighlight(7));
 assertTrue(textPane.getHighlighter().hasHighlight(8));
 assertTrue(textPane.getHighlighter().hasHighlight(9));
 assertFalse(textPane.getHighlighter().hasHighlight(10));
 lineResults = textPane.highlight("quick brown fox", 0);
 assertEquals(1, lineResults.size());
 lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(10, lineResult.getEndOffset());
 }

 @Test
 public void testHighlightMultipleWordsMultipleLines() {
 textPane.setText("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog");
 textPane.highlight("quick brown", 0);
 assertTrue(textPane.getHighlighter().hasHighlight(0));
 assertTrue(textPane.getHighlighter().hasHighlight(1));
 assertTrue(textPane.getHighlighter().hasHighlight(2));
 assertTrue(textPane.getHighlighter().hasHighlight(3));
 assertTrue(textPane.getHighlighter().hasHighlight(4));
 assertTrue(textPane.getHighlighter().hasHighlight(5));
 assertTrue(textPane.getHighlighter().hasHighlight(6));
 assertTrue(textPane.getHighlighter().hasHighlight(7));
 assertTrue(textPane.getHighlighter().hasHighlight(8));
 assertTrue(textPane.getHighlighter().hasHighlight(9));
 assertTrue(textPane.getHighlighter().hasHighlight(10));
 assertTrue(textPane.getHighlighter().hasHighlight(11));
 assertTrue(textPane.getHighlighter().hasHighlight(12));
 assertTrue(textPane.getHighlighter().hasHighlight(13));
 assertTrue(textPane.getHighlighter().hasHighlight(14));
 assertTrue(textPane.getHighlighter().hasHighlight(15));
 assertFalse(textPane.getHighlighter().hasHighlight(16));
 lineResults = textPane.highlight("quick brown", 0);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getStartOffset());
 assertEquals(10, lineResult1.getEndOffset());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(1, lineResult2.getLineNumber());
 assertEquals(0, lineResult2.getStartOffset());
 assertEquals(1, lineResult2.getEndOffset());
 }

 @Test
 public void testSearchNullTerm() {
 int pos = textPane.search(null);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchEmptyTerm() {
 textPane.setText("The quick brown fox jumps over the lazy dog");
 int pos = textPane.search("");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchSingleWord() {
 textPane.setText("The quick brown fox jumps over the lazy dog");
 pos = textPane.search("fox");
 assertEquals(8, pos);
 lineResults = textPane.highlight("fox", pos);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(8, lineResult.getStartOffset());
 assertEquals(11, lineResult.getEndOffset());
 }

 @Test
 public void testSearchMultipleWords() {
 textPane.setText("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog");
 pos = textPane.search("quick brown fox");
 assertEquals(0, pos);
 lineResults = textPane.highlight("quick brown fox", pos);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getStartOffset());
 assertEquals(10, lineResult1.getEndOffset());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(1, lineResult2.getLineNumber());
 assertEquals(0, lineResult2.getStartOffset());
 assertEquals(1, lineResult2.getEndOffset());
 }

 @Test
 public void testSearchMultipleWordsMultipleLines() {
 textPane.setText("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog");
 pos = textPane.search("quick brown");
 assertEquals(0, pos);
 lineResults = textPane.highlight("quick brown", pos);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getStartOffset());
 assertEquals(10, lineResult1.getEndOffset());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(1, lineResult2.getLineNumber());
 assertEquals(0, lineResult2.getStartOffset());
 assertEquals(1, lineResult2.getEndOffset());
 }

 @Test
 public void testSearchMultipleOccurrences() {
 textPane.setText("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog");
 pos = textPane.search("quick brown fox");
 assertEquals(0, pos);
 lineResults = textPane.highlight("quick brown fox", pos);
 assertEquals(3, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getStartOffset());
 assertEquals(10, lineResult1.getEndOffset());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(1, lineResult2.getLineNumber());
 assertEquals(0, lineResult2.getStartOffset());
 assertEquals(1, lineResult2.getEndOffset());
 LineResult lineResult3 = lineResults.get(2);
 assertEquals(2, lineResult3.getLineNumber());
 assertEquals(0, lineResult3.getStartOffset());
 assertEquals(1, lineResult3.getEndOffset());
 }

 @Test
 public void testSearchBackwards() {
 textPane.setText("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog");
 pos = textPane.search("fox", textPane.getText().length());
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchBackwardsMultipleWords() {
 textPane.setText("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog");
 pos = textPane.search("lazy dog", textPane.getText().length() - "lazy dog".length());
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchWithFlag() {
 textPane.setText("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog");
 List<WordResult> wordResults = new SearchEngine(SearchEngine.REGEX_FLAG).search("lazy", textPane.getText());
 LineResult lineResult = new LineResult(0, 0, textPane.getDocument().getLength());
 lineResults = textPane.convertWords2Lines(wordResults);
 lineResult.addWord(wordResults.get(0));
 lineResult.addWord(wordResults.get(1));
 textPane.highlight(lineResult);
 assertTrue(textPane.getHighlighter().hasHighlight(11));
 assertTrue(textPane.getHighlighter().hasHighlight(12));
 assertTrue(textPane.getHighlighter().hasHighlight(13));
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightOutOfRange() {
 textPane.setText("The quick brown fox jumps over the lazy dog");
 textPane.highlight("notthere", textPane.getText().length());
 }

 @Test(expected = BadLocationException.class)
 public void testSearchOutOfRange() {
 textPane.setText("The quick brown fox jumps over the lazy dog");
 int pos = textPane.search("notthere", textPane.getText().length());
 }
}