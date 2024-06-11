import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import ghm.follow.search.SearchableTextPane;
import ghm.follow.search.SearchEngine;
import ghm.follow.search.LineResult;
import ghm.follow.search.WordResult;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private SearchEngine searchEngine;

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 4);
 searchEngine = new SearchEngine(SearchEngine.REGULAR_EXPRESSION);
 }

 @Test
 public void testHighlightEmptyTerm() {
 String text = "This is a test for an empty search term.";
 textPane.setText(text);
 List<LineResult> lineResults = textPane.highlight(null, 0);
 assertNotNull(lineResults);
 assertTrue(lineResults.isEmpty());
 textPane.repaint();
 }

 @Test
 public void testHighlightSingleWordTerm() {
 String text = "The quick brown fox jumps over the lazy dog.";
 textPane.setText(text);
 List<LineResult> lineResults = textPane.highlight("fox", 0);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(5, wordResult.start);
 assertEquals(8, wordResult.end);
 assertEquals(Color.YELLOW, textPane.getBackgroundAt(wordResult.start, wordResult.length()));
 }

 @Test
 public void testHighlightMultipleWordsTerm() {
 String text = "The quick brown fox jumps over the lazy dog.";
 textPane.setText(text);
 List<LineResult> lineResults = textPane.highlight("quick brown", 0);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(0, wordResult.start);
 assertEquals(11, wordResult.end);
 assertEquals(Color.YELLOW, textPane.getBackgroundAt(wordResult.start, wordResult.length()));
 wordResult = lineResult.getWordResults().get(1);
 assertEquals(14, wordResult.start);
 assertEquals(19, wordResult.end);
 assertEquals(Color.YELLOW, textPane.getBackgroundAt(wordResult.start, wordResult.length()));
 }

 @Test
 public void testHighlightWholeTextTerm() {
 String text = "The quick brown fox jumps over the lazy dog.";
 textPane.setText(text);
 List<LineResult> lineResults = textPane.highlight("The quick brown fox jumps over the lazy dog.", 0);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(0, wordResult.start);
 assertEquals(32, wordResult.end);
 assertEquals(Color.YELLOW, textPane.getBackgroundAt(wordResult.start, wordResult.length()));
 }

 @Test
 public void testHighlightTermInMiddleOfText() {
 String text = "The quick brown fox jumps over the lazy dog.";
 textPane.setText(text);
 List<LineResult> lineResults = textPane.highlight("jumps over", 23);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(23, wordResult.start);
 assertEquals(29, wordResult.end);
 assertEquals(Color.YELLOW, textPane.getBackgroundAt(wordResult.start, wordResult.length()));
 }

 @Test
 public void testSearchEmptyTextPane() {
 textPane.setText("");
 int pos = textPane.search("");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchSingleWordFound() {
 String text = "The quick brown fox jumps over the lazy dog.";
 textPane.setText(text);
 int pos = textPane.search("fox");
 assertEquals(5, pos);
 }

 @Test
 public void testSearchMultipleWordsFound() {
 String text = "The quick brown fox jumps over the lazy dog.";
 textPane.setText(text);
 int pos = textPane.search("quick brown");
 assertEquals(0, pos);
 }

 @Test
 public void testSearchWholeTextFound() {
 String text = "The quick brown fox jumps over the lazy dog.";
 textPane.setText(text);
 int pos = textPane.search("The quick brown fox jumps over the lazy dog.");
 assertEquals(0, pos);
 }

 @Test
 public void testSearchTermNotFound() {
 String text = "The quick brown fox jumps over the lazy dog.";
 textPane.setText(text);
 int pos = textPane.search("elephant");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchTermInMiddleOfText() {
 String text = "The quick brown fox jumps over the lazy dog.";
 textPane.setText(text);
 int pos = textPane.search("jumps over");
 assertEquals(23, pos);
 }

 @Test(expected = NullPointerException.class)
 public void testSearchNullTextPane() {
 SearchableTextPane nullTextPane = null;
 int pos = nullTextPane.search("fox");
 }

 @Test
 public void testSearchCaseSensitive() {
 String text = "The quick brown fox jumps over the lazy dog.";
 textPane.setText(text);
 int pos = textPane.search("Fox");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchCaseInsensitive() {
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 4) {
 @Override
 public List<LineResult> highlight(String term, int flags) {
 return super.highlight(term.toLowerCase(), flags);
 }
 };
 String text = "The quick brown fox jumps over the lazy dog.";
 textPane.setText(text);
 int pos = textPane.search("Fox");
 assertEquals(5, pos);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchNegativeFlags() {
 textPane.search("term", -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchZeroFlags() {
 textPane.search("term", 0);
 }
}