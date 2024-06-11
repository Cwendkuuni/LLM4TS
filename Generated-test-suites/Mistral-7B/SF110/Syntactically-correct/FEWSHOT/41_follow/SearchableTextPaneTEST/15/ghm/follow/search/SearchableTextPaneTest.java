package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 static final String TEST_SEARCH_TERM = "testTerm";
 static final int TEST_FLAGS = 0;
 static final int TEST_TAB_SIZE = 5;
 static final Font TEST_FONT = new Font("Serif", Font.PLAIN, 12);

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(TEST_FONT, TEST_TAB_SIZE);
 }

 @Test
 public void testSearchableTextPaneConstructor() {
 assertNotNull(searchableTextPane);
 assertEquals(TEST_FONT, searchableTextPane.getFont());
 assertEquals(TEST_TAB_SIZE, searchableTextPane.getTabSize());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightWithEmptyTerm() {
 List<LineResult> lineResults = searchableTextPane.highlight(null, TEST_FLAGS);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlightWithEmptyTextPane() {
 searchableTextPane.setText("");
 List<LineResult> lineResults = searchableTextPane.highlight(TEST_SEARCH_TERM, TEST_FLAGS);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlightWithSingleWord() {
 String testText = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(testText);
 List<LineResult> lineResults = searchableTextPane.highlight(TEST_SEARCH_TERM, TEST_FLAGS);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getLineStartOffset());
 assertEquals(testText.length(), lineResult.getLineEndOffset());
 List<WordResult> wordResults = lineResult.getWordResults();
 assertEquals(1, wordResults.size());
 assertEquals(TEST_SEARCH_TERM, wordResults.get(0).getWord());
 assertEquals(6, wordResults.get(0).getStart());
 assertEquals(11, wordResults.get(0).getEnd());
 }

 @Test
 public void testHighlightWithMultipleWordsPerLine() {
 String testText = "The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(testText);
 List<LineResult> lineResults = searchableTextPane.highlight(TEST_SEARCH_TERM, TEST_FLAGS);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getLineStartOffset());
 assertEquals(testText.length(), lineResult1.getLineEndOffset());
 assertEquals(1, lineResult2.getLineNumber());
 assertEquals(testText.length(), lineResult2.getLineStartOffset());
 assertEquals((int) (testText.length() * 1.5), lineResult2.getLineEndOffset());
 List<WordResult> wordResults1 = lineResult1.getWordResults();
 List<WordResult> wordResults2 = lineResult2.getWordResults();
 assertEquals(2, wordResults1.size());
 assertEquals(TEST_SEARCH_TERM, wordResults1.get(0).getWord());
 assertEquals(6, wordResults1.get(0).getStart());
 assertEquals(11, wordResults1.get(0).getEnd());
 assertEquals(TEST_SEARCH_TERM, wordResults2.get(0).getWord());
 assertEquals(testText.length() - 11, wordResults2.get(0).getStart());
 assertEquals(testText.length(), wordResults2.get(0).getEnd());
 }

 @Test
 public void testHighlightWithMultipleTermsPerLine() {
 String testText = "The quick brown fox jumps over the lazy dog The quick brown fox jumps over the quick lazy dog.";
 searchableTextPane.setText(testText);
 List<LineResult> lineResults = searchableTextPane.highlight(TEST_SEARCH_TERM + " " + TEST_SEARCH_TERM, TEST_FLAGS);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getLineStartOffset());
 assertEquals(testText.length(), lineResult1.getLineEndOffset());
 List<WordResult> wordResults1 = lineResult1.getWordResults();
 assertEquals(3, wordResults1.size());
 assertEquals(TEST_SEARCH_TERM, wordResults1.get(0).getWord());
 assertEquals(6, wordResults1.get(0).getStart());
 assertEquals(11, wordResults1.get(0).getEnd());
 assertEquals(TEST_SEARCH_TERM, wordResults1.get(1).getWord());
 assertEquals(27, wordResults1.get(1).getStart());
 assertEquals(32, wordResults1.get(1).getEnd());
 List<WordResult> wordResults2 = lineResult2.getWordResults();
 assertEquals(1, wordResults2.size());
 assertEquals(TEST_SEARCH_TERM, wordResults2.get(0).getWord());
 assertEquals(38, wordResults2.get(0).getStart());
 assertEquals(43, wordResults2.get(0).getEnd());
 }

 @Test
 public void testSearchWithEmptyTerm() {
 assertEquals(-1, searchableTextPane.search(null));
 }

 @Test
 public void testSearchWithEmptyTextPane() {
 assertEquals(-1, searchableTextPane.search(TEST_SEARCH_TERM));
 }

 @Test
 public void testSearchWithSingleWord() {
 String testText = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(testText);
 assertEquals(6, searchableTextPane.search(TEST_SEARCH_TERM));
 }

 @Test
 public void testSearchWithMultipleWordsPerLine() {
 String testText = "The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(testText);
 assertEquals(6, searchableTextPane.search(TEST_SEARCH_TERM));
 assertEquals(33, searchableTextPane.search(TEST_SEARCH_TERM + " " + TEST_SEARCH_TERM));
 }

 @Test
 public void testSearchWithMultipleTermsPerLine() {
 String testText = "The quick brown fox jumps over the lazy dog The quick brown fox jumps over the quick lazy dog.";
 searchableTextPane.setText(testText);
 assertEquals(6, searchableTextPane.search(TEST_SEARCH_TERM));
 assertEquals(36, searchableTextPane.search(TEST_SEARCH_TERM + " " + TEST_SEARCH_TERM));
 }

 @Test
 public void testSearchWithTermAtEndOfLine() {
 String testText = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(testText);
 assertEquals(18, searchableTextPane.search("dog"));
 }

 @Test
 public void testSearchWithTermAtStartOfLine() {
 String testText = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(testText);
 assertEquals(0, searchableTextPane.search("The"));
 }

 @Test
 public void testSearchWithTermInMiddleOfLine() {
 String testText = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(testText);
 assertEquals(7, searchableTextPane.search("brown"));
 }

 @Test
 public void testSearchWithTermInMiddleOfLineCaseInsensitive() {
 String testText = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(testText);
 assertEquals(7, searchableTextPane.search("brown".toUpperCase()));
 }

 @Test
 public void testSearchWithTermAtStartOrEndOfMultipleWords() {
 String testText = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(testText);
 assertEquals(0, searchableTextPane.search("The quick"));
 assertEquals(18, searchableTextPane.search("dog"));
 }

 @Test
 public void testSearchWithTermInMiddleOfMultipleWords() {
 String testText = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(testText);
 assertEquals(7, searchableTextPane.search("brown"));
 }

 @Test
 public void testSearchWithTermInMiddleOfMultipleWordsCaseInsensitive() {
 String testText = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(testText);
 assertEquals(7, searchableTextPane.search("brown".toUpperCase()));
 }

 @Test
 public void testSearchWithTermInMiddleOfMultipleLines() {
 String testText = "The quick brown fox jumps over the lazy dog\nThe quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(testText);
 assertEquals(6, searchableTextPane.search(TEST_SEARCH_TERM));
 assertEquals(33, searchableTextPane.search(TEST_SEARCH_TERM + " " + TEST_SEARCH_TERM));
 }

 @Test
 public void testSearchWithTermAtStartOfMultipleLines() {
 String testText = "The quick brown fox jumps over the lazy dog\nThe quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(testText);
 assertEquals(0, searchableTextPane.search("The"));
 assertEquals(6, searchableTextPane.search("The quick"));
 }

 @Test
 public void testSearchWithTermInMiddleOfMultipleLines() {
 String testText = "The quick brown fox jumps over the lazy dog\nThe quick brown fox jumps over the quick lazy dog.";
 searchableTextPane.setText(testText);
 assertEquals(7, searchableTextPane.search("brown"));
 assertEquals(33, searchableTextPane.search("dog"));
 }

 @Test
 public void testSearchWithTermInMiddleOfMultipleLinesCaseInsensitive() {
 String testText = "The quick brown fox jumps over the lazy dog\nThe quick brown fox jumps over the quick lazy dog.";
 searchableTextPane.setText(testText);
 assertEquals(7, searchableTextPane.search("brown".toUpperCase()));
 assertEquals(33, searchableTextPane.search("dog".toUpperCase()));
 }
}