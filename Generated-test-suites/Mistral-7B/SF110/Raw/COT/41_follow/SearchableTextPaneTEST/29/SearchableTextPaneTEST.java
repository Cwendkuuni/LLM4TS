package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ghm.follow.search.SearchableTextPane;
import ghm.follow.search.SearchEngine;
import ghm.follow.search.Utilities;
import ghm.follow.search.WordResult;
import ghm.follow.search.LineResult;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Monospaced", Font.BOLD, 12), 4);
 }

 @Test
 public void testSearchableTextPaneConstruction() {
 SearchableTextPane textPane = new SearchableTextPane(new Font("Monospaced", Font.BOLD, 12), 4);
 assertNotNull(textPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 Component parent = null;
 ComponentUI ui = searchableTextPane.getUI();
 boolean result = searchableTextPane.getScrollableTracksViewportWidth();
 assertTrue(result);

 parent = new JFrame();
 searchableTextPane = new SearchableTextPane(new Font("Monospaced", Font.BOLD, 12), 4);
 parent.add(searchableTextPane);
 Rectangle preferredSize = ui.getPreferredSize(searchableTextPane);
 result = searchableTextPane.getScrollableTracksViewportWidth();
 assertFalse(result);
 }

 @Test
 public void testHighlightEmptyText() {
 List<LineResult> lineResults = searchableTextPane.highlight("", 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightNullTerm() {
 List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightInvalidFlags() {
 try {
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Monospaced", Font.BOLD, 12), 4);
 List<LineResult> lineResults = searchableTextPane.highlight("test", 12345);
 fail("Expected IllegalArgumentException for invalid flag");
 }
 catch (IllegalArgumentException expected) {
 //Test passed
 }
 }

 @Test
 public void testHighlightSingleWord() {
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 List<LineResult> lineResults = searchableTextPane.highlight("fox", 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(5, wordResult.getStart());
 assertEquals(7, wordResult.getEnd());
 }

 @Test
 public void testHighlightMultipleWords() {
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 List<LineResult> lineResults = searchableTextPane.highlight("quick brown", 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(2, lineResult.getWordResults().size());
 List<WordResult> wordResults = lineResult.getWordResults();
 assertEquals(5, wordResults.get(0).getStart());
 assertEquals(10, wordResults.get(0).getEnd());
 assertEquals(13, wordResults.get(1).getStart());
 assertEquals(19, wordResults.get(1).getEnd());
 }

 @Test
 public void testHighlightMultipleLines() {
 String text = "The quick brown fox\njumps over the lazy dog.";
 searchableTextPane.setText(text);
 List<LineResult> lineResults = searchableTextPane.highlight("quick brown", 0);
 assertEquals(2, lineResults.size());
 List<WordResult> wordResults = lineResults.get(1).getWordResults();
 assertEquals(1, wordResults.size());
 assertEquals(14, wordResults.get(0).getStart());
 assertEquals(19, wordResults.get(0).getEnd());
 }

 @Test
 public void testHighlightSameTermMultipleTimes() {
 String text = "The quick brown fox jumps over the quick lazy dog.";
 searchableTextPane.setText(text);
 List<LineResult> lineResults = searchableTextPane.highlight("quick", 0);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 LineResult lineResult2 = lineResults.get(1);
 List<WordResult> wordResults1 = lineResult1.getWordResults();
 List<WordResult> wordResults2 = lineResult2.getWordResults();
 assertEquals(1, wordResults1.size());
 assertEquals(3, wordResults1.get(0).getStart());
 assertEquals(6, wordResults1.get(0).getEnd());
 assertEquals(1, wordResults2.size());
 assertEquals(11, wordResults2.get(0).getStart());
 assertEquals(14, wordResults2.get(0).getEnd());
 }

 @Test
 public void testHighlightEdgeCasePartialWord() {
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 List<LineResult> lineResults = searchableTextPane.highlight("quic", 0);
 assertEquals(1, lineResults.size());
 List<WordResult> wordResults = lineResults.get(0).getWordResults();
 assertEquals(1, wordResults.size());
 assertEquals(3, wordResults.get(0).getStart());
 assertEquals(6, wordResults.get(0).getEnd());
 }

 @Test
 public void testHighlightEdgeCaseLastWord() {
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 List<LineResult> lineResults = searchableTextPane.highlight("dog.", 0);
 assertEquals(1, lineResults.size());
 List<WordResult> wordResults = lineResults.get(0).getWordResults();
 assertEquals(1, wordResults.size());
 assertEquals(32, wordResults.get(0).getStart());
 assertEquals(33, wordResults.get(0).getEnd());
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightException() {
 List<LineResult> lineResults = searchableTextPane.highlight("test", -1);
 fail("Expected BadLocationException for invalid offset");
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightExceptionWhileSettingText() {
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText("Invalid text");
 List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
 fail("Expected BadLocationException for invalid document state");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchNullTerm() {
 int result = searchableTextPane.search(null);
 fail("Expected IllegalArgumentException for null term");
 }

 @Test
 public void testSearchEmptyText() {
 String text = "";
 searchableTextPane.setText(text);
 int result = searchableTextPane.search("");
 assertEquals(-1, result);
 }

 @Test
 public void testSearchSingleWord() {
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 int result = searchableTextPane.search("fox");
 assertEquals(5, result);
 }

 @Test(expected = BadLocationException.class)
 public void testSearchException() {
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 int result = searchableTextPane.search("test");
 fail("Expected BadLocationException for invalid offset");
 }

 @Test
 public void testSearchMultipleWords() {
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 int result = searchableTextPane.search("quick brown");
 assertEquals(5, result);
 }

 @Test
 public void testSearchSameTermMultipleTimes() {
 String text = "The quick brown fox jumps over the quick brown dog.";
 searchableTextPane.setText(text);
 int result = searchableTextPane.search("quick");
 assertEquals(5, result);
 int result2 = searchableTextPane.search(6);
 assertEquals(11, result2);
 }

 @Test
 public void testSearchEdgeCasePartialWord() {
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 int result = searchableTextPane.search("quic");
 assertEquals(3, result);
 }

 @Test
 public void testSearchEdgeCaseLastWord() {
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 int result = searchableTextPane.search("dog.");
 assertEquals(33, result);
 }

 @Test
 public void testAddHighlight() throws BadLocationException {
 int start = 3;
 int length = 4;
 searchableTextPane.addHighlight(start, length);
 List<Highlight> highlights = searchableTextPane.getHighlighter().getHighlights();
 assertEquals(1, highlights.size());
 Highlight highlight = highlights.get(0);
 assertEquals("SearchableTextPane", highlight.getComponent().getClass().getName());
 assertEquals(Color.YELLOW, highlight.getPainter().getColor());
 assertEquals(start, highlight.getStartOffset());
 assertEquals(start + length, highlight.getEndOffset());
 }

 @Test(expected = BadLocationException.class)
 public void testAddHighlightException() throws BadLocationException {
 int start = -1;
 int length = 1;
 searchableTextPane.addHighlight(start, length);
 fail("Expected BadLocationException for invalid offset");
 }

 @Test
 public void testRemoveHighlights() {
 List<Highlight> highlights = Arrays.asList(searchableTextPane.getHighlighter().getHighlights());
 int sizeBeforeRemoval = highlights.size();
 searchableTextPane.removeHighlights();
 int sizeAfterRemoval = highlights.size();
 assertEquals(0, sizeAfterRemoval);
 }

 @Test
 public void testConvertWords2Lines() throws BadLocationException {
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 List<WordResult> words = Arrays.asList(new WordResult("The", 0, 3), new WordResult("quick", 3, 6),
 new WordResult("brown", 6, 10), new WordResult("fox", 10, 13), new WordResult("jumps", 13, 17),
 new WordResult("over", 17, 21), new WordResult("the", 21, 25), new WordResult("lazy", 25, 29),
 new WordResult("dog", 29, 33));
 List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(1, lineResult1.getWordResults().size());
 assertEquals(1, lineResult2.getWordResults().size());
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(1, lineResult2.getLineNumber());
 assertEquals(3, lineResult1.getLineStartOffset());
 assertEquals(10, lineResult1.getLineEndOffset());
 assertEquals(3, lineResult2.getLineStartOffset());
 assertEquals(19, lineResult2.getLineEndOffset());
 List<WordResult> wordResults1 = lineResult1.getWordResults();
 List<WordResult> wordResults2 = lineResult2.getWordResults();
 assertEquals(3, wordResults1.get(0).getStart());
 assertEquals(6, wordResults1.get(0).getEnd());
 assertEquals(10, wordResults2.get(0).getStart());
 assertEquals(13, wordResults2.get(0).getEnd());
 }

}