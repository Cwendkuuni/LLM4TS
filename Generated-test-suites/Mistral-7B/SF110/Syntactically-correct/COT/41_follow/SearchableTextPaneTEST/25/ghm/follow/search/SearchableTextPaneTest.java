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

 private SearchableTextPane textPane;
 private SearchEngine searchEngine;

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 searchEngine = new SearchEngine(SearchEngine.REGULAR_EXPRESSIONS);
 textPane.setText("The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.");
 }

 @Test
 public void testConstructor() {
 assertEquals("Expected font and tab size not set correctly", new Font("Serif", Font.BOLD, 14), textPane.getFont());
 assertEquals("Expected tab size not set correctly", 8, textPane.getTabSize());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertFalse("Expected text pane not to track viewport width when parent is null", textPane.getScrollableTracksViewportWidth());
 assertTrue("Expected text pane to track viewport width when parent is not null", new JFrame().add(textPane).isShowing());
 }

 @Test
 public void testHighlightEmptyString() {
 List<LineResult> lineResults = textPane.highlight("", 0);
 assertTrue("Expected empty list when searching for empty string", lineResults.isEmpty());
 }

 @Test
 public void testHighlightNullString() {
 List<LineResult> lineResults = textPane.highlight(null, 0);
 assertTrue("Expected empty list when searching for null string", lineResults.isEmpty());
 }

 @Test
 public void testHighlightSingleWord() {
 List<LineResult> lineResults = textPane.highlight("fox", 0);
 assertEquals("Expected one line result", 1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals("Expected line number 1", 1, lineResult.getLineNumber());
 assertEquals("Expected start and end offsets of first occurrence of 'fox'", 15, lineResult.getStartOffset());
 assertEquals("Expected start and end offsets of last occurrence of 'fox'", 24, lineResult.getEndOffset());
 }

 @Test
 public void testHighlightMultipleWords() {
 List<LineResult> lineResults = textPane.highlight("the quick brown fox", 0);
 assertEquals("Expected two line results", 2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 LineResult lineResult2 = lineResults.get(1);
 assertEquals("Expected line number 1 for the first line result", 1, lineResult1.getLineNumber());
 assertEquals("Expected line number 2 for the second line result", 2, lineResult2.getLineNumber());
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightWithException() {
 textPane.highlight("quick", -1);
 }

 @Test
 public void testSearchEmptyString() {
 int pos = textPane.search("");
 assertEquals("Expected position to be -1 when searching for empty string", -1, pos);
 }

 @Test
 public void testSearchNullString() {
 int pos = textPane.search(null);
 assertEquals("Expected position to be -1 when searching for null string", -1, pos);
 }

 @Test
 public void testSearchSingleWord() {
 int pos = textPane.search("fox");
 assertEquals("Expected position of first occurrence of 'fox'", 15, pos);
 }

 @Test
 public void testSearchMultipleWords() {
 int pos = textPane.search("the quick brown fox");
 assertEquals("Expected position of first occurrence of 'the quick brown fox'", 0, pos);
 }

 @Test(expected = BadLocationException.class)
 public void testSearchWithException() {
 textPane.search("quick", -1);
 }

 @Test
 public void testConvertWords2LinesEmptyList() {
 List<WordResult> words = new ArrayList<>();
 List<LineResult> lineResults = textPane.convertWords2Lines(words);
 assertTrue("Expected empty list when converting empty list", lineResults.isEmpty());
 }

 @Test
 public void testConvertWords2LinesSingleWord() {
 WordResult wordResult = new WordResult("fox", 15, 24);
 List<LineResult> lineResults = textPane.convertWords2Lines(Arrays.asList(wordResult));
 assertEquals("Expected one line result", 1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals("Expected line number 1 for the line result", 1, lineResult.getLineNumber());
 assertEquals("Expected start and end offsets of the word result", 15, lineResult.getStartOffset());
 assertEquals("Expected start and end offsets of the word result", 24, lineResult.getEndOffset());
 }

 @Test
 public void testConvertWords2LinesMultipleWords() {
 WordResult wordResult1 = new WordResult("quick", 3, 7);
 WordResult wordResult2 = new WordResult("brown", 13, 17);
 WordResult wordResult3 = new WordResult("fox", 15, 24);
 List<LineResult> lineResults = textPane.convertWords2Lines(Arrays.asList(wordResult1, wordResult2, wordResult3));
 assertEquals("Expected three line results", 3, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 LineResult lineResult2 = lineResults.get(1);
 LineResult lineResult3 = lineResults.get(2);
 assertEquals("Expected line number 1 for the first line result", 1, lineResult1.getLineNumber());
 assertEquals("Expected line number 2 for the second line result", 1, lineResult2.getLineNumber());
 assertEquals("Expected line number 3 for the third line result", 2, lineResult3.getLineNumber());
 assertEquals("Expected start and end offsets of the first word result", 3, lineResult1.getStartOffset());
 assertEquals("Expected start and end offsets of the second word result", 13, lineResult2.getStartOffset());
 assertEquals("Expected start and end offsets of the third word result", 15, lineResult3.getStartOffset());
 }

 @Test
 public void testAddHighlightEmptyString() {
 try {
 textPane.addHighlight(0, 0);
 fail("Expected IllegalArgumentException when adding highlight to empty string");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testAddHighlightSingleCharacter() throws BadLocationException {
 textPane.addHighlight(15, 1);
 List<LineResult> lineResults = textPane.highlight("fox", 0);
 LineResult lineResult = lineResults.get(0);
 int highlightStart = lineResult.getWordResults().get(0).start;
 assertTrue("Expected highlight to be added at the start of the first word", highlightStart == 16);
 }

 @Test
 public void testAddHighlightMultipleCharacters() throws BadLocationException {
 textPane.addHighlight(15, 3);
 List<LineResult> lineResults = textPane.highlight("fox", 0);
 LineResult lineResult = lineResults.get(0);
 int highlightStart = lineResult.getWordResults().get(0).start;
 int highlightEnd = lineResult.getWordResults().get(0).end;
 assertEquals("Expected highlight to be added to the entire first word", 15, highlightStart);
 assertEquals("Expected highlight to be added to the entire first word", 18, highlightEnd);
 }

 @Test(expected = BadLocationException.class)
 public void testAddHighlightWithException() throws Exception {
 textPane.addHighlight(-1, 0);
 }

 @Test
 public void testRemoveHighlights() {
 textPane.setText("The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.");
 List<LineResult> lineResults = textPane.highlight("fox", 0);
 textPane.removeHighlights();
 assertTrue("Expected no highlights after removing them", textPane.getHighlighter().getHighlights().isEmpty());
 }
}