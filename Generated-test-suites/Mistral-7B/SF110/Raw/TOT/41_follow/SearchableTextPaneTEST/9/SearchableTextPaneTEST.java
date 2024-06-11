import org.junit.After;
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

 private SearchableTextPane searchableTextPane;
 private static final String DEFAULT_TERM = "test";
 private static final int DEFAULT_FLAGS = 0;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyTerm() {
 final List<LineResult> lineResults = searchableTextPane.highlight(null, DEFAULT_FLAGS);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightEmptyTextPane() {
 final String emptyText = "";
 final List<WordResult> words = new ArrayList<>();
 final List<LineResult> lineResults = searchableTextPane.highlight(emptyText, DEFAULT_FLAGS);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightTermInTextPane() {
 final String testText = "This is a test SearchableTextPane.";
 searchableTextPane.setText(testText);
 final List<WordResult> searchResults = new SearchEngine(DEFAULT_FLAGS).search(DEFAULT_TERM, testText);
 final List<LineResult> lineResults = searchableTextPane.highlight(DEFAULT_TERM, DEFAULT_FLAGS);
 assertEquals(1, lineResults.size());
 final LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getWordResults().size());
 final WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(DEFAULT_TERM, wordResult.getWord().toLowerCase());
 assertEquals(13, wordResult.start);
 assertEquals(16, wordResult.end);
 final int lineNumber = lineResult.lineNumber;
 assertEquals(1, lineNumber);
 }

 @Test
 public void testHighlightTermAtBeginningOfTextPane() {
 final String testText = "test This is a SearchableTextPane.";
 searchableTextPane.setText(testText);
 final List<WordResult> searchResults = new SearchEngine(DEFAULT_FLAGS).search(DEFAULT_TERM, testText);
 final List<LineResult> lineResults = searchableTextPane.highlight(DEFAULT_TERM, DEFAULT_FLAGS);
 assertEquals(1, lineResults.size());
 final LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getWordResults().size());
 final WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(0, wordResult.start);
 assertEquals(4, wordResult.end);
 final int lineNumber = lineResult.lineNumber;
 assertEquals(1, lineNumber);
 }

 @Test
 public void testHighlightTermInMiddleOfTextPane() {
 final String testText = "This is a test SearchableTextPane.";
 searchableTextPane.setText(testText);
 final List<WordResult> searchResults = new SearchEngine(DEFAULT_FLAGS).search(DEFAULT_TERM, testText);
 final List<LineResult> lineResults = searchableTextPane.highlight(DEFAULT_TERM, DEFAULT_FLAGS);
 assertEquals(1, lineResults.size());
 final LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getWordResults().size());
 final WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(10, wordResult.start);
 assertEquals(13, wordResult.end);
 final int lineNumber = lineResult.lineNumber;
 assertEquals(1, lineNumber);
 }

 @Test
 public void testHighlightTermAtEndOfTextPane() {
 final String testText = "This is a test SearchableTextPane.";
 searchableTextPane.setText(testText);
 final List<WordResult> searchResults = new SearchEngine(DEFAULT_FLAGS).search(DEFAULT_TERM, testText);
 final List<LineResult> lineResults = searchableTextPane.highlight(DEFAULT_TERM, DEFAULT_FLAGS);
 assertEquals(1, lineResults.size());
 final LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getWordResults().size());
 final WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(23, wordResult.start);
 assertEquals(26, wordResult.end);
 final int lineNumber = lineResult.lineNumber;
 assertEquals(1, lineNumber);
 }

 @Test
 public void testHighlightTermWithMultipleMatches() {
 final String testText = "This is a test SearchableTextPane. This is another test SearchableTextPane.";
 searchableTextPane.setText(testText);
 final List<WordResult> searchResults = new SearchEngine(DEFAULT_FLAGS).search(DEFAULT_TERM, testText);
 final List<LineResult> lineResults = searchableTextPane.highlight(DEFAULT_TERM, DEFAULT_FLAGS);
 assertEquals(2, lineResults.size());
 final LineResult lineResult1 = lineResults.get(0);
 assertEquals(1, lineResult1.getWordResults().size());
 final LineResult lineResult2 = lineResults.get(1);
 assertEquals(1, lineResult2.getWordResults().size());
 final WordResult wordResult1 = lineResult1.getWordResults().get(0);
 assertEquals(0, wordResult1.start);
 assertEquals(4, wordResult1.end);
 final WordResult wordResult2 = lineResult2.getWordResults().get(0);
 assertEquals(22, wordResult2.start);
 assertEquals(26, wordResult2.end);
 final int lineNumber1 = lineResult1.lineNumber;
 assertEquals(1, lineNumber1);
 final int lineNumber2 = lineResult2.lineNumber;
 assertEquals(2, lineNumber2);
 }

 @Test
 public void testHighlightTermCaseSensitive() {
 final String testText = "This is a TEST SearchableTextPane.";
 searchableTextPane.setText(testText);
 final List<WordResult> searchResults = new SearchEngine(DEFAULT_FLAGS).search(DEFAULT_TERM, testText);
 final List<LineResult> lineResults = searchableTextPane.highlight(DEFAULT_TERM, DEFAULT_FLAGS);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightTermWithFlags() {
 final String testText = "This is a test SearchableTextPane. This is another test SearchableTextPane.";
 searchableTextPane.setText(testText);
 final List<WordResult> searchResults = new SearchEngine(SearchEngine.REGEX_FLAG).search(DEFAULT_TERM, testText);
 final List<LineResult> lineResults = searchableTextPane.highlight(DEFAULT_TERM, SearchEngine.REGEX_FLAG);
 assertEquals(2, lineResults.size());
 final LineResult lineResult1 = lineResults.get(0);
 assertEquals(1, lineResult1.getWordResults().size());
 final LineResult lineResult2 = lineResults.get(1);
 assertEquals(1, lineResult2.getWordResults().size());
 final WordResult wordResult1 = lineResult1.getWordResults().get(0);
 assertEquals(0, wordResult1.start);
 assertEquals(4, wordResult1.end);
 final WordResult wordResult2 = lineResult2.getWordResults().get(0);
 assertEquals(22, wordResult2.start);
 assertEquals(26, wordResult2.end);
 final int lineNumber1 = lineResult1.lineNumber;
 assertEquals(1, lineNumber1);
 final int lineNumber2 = lineResult2.lineNumber;
 assertEquals(2, lineNumber2);
 }

 @Test
 public void testSearchTermInTextPane() {
 final String testText = "This is a test SearchableTextPane.";
 searchableTextPane.setText(testText);
 final int searchResult = searchableTextPane.search(DEFAULT_TERM);
 assertEquals(13, searchResult);
 }

 @Test
 public void testSearchTermAtBeginningOfTextPane() {
 final String testText = "test This is a SearchableTextPane.";
 searchableTextPane.setText(testText);
 final int searchResult = searchableTextPane.search(DEFAULT_TERM);
 assertEquals(0, searchResult);
 }

 @Test
 public void testSearchTermInMiddleOfTextPane() {
 final String testText = "This is a test SearchableTextPane.";
 searchableTextPane.setText(testText);
 final int searchResult = searchableTextPane.search(DEFAULT_TERM);
 assertEquals(13, searchResult);
 }

 @Test
 public void testSearchTermAtEndOfTextPane() {
 final String testText = "This is a test SearchableTextPane.";
 searchableTextPane.setText(testText);
 final int searchResult = searchableTextPane.search(DEFAULT_TERM);
 assertEquals(26, searchResult);
 }

 @Test
 public void testSearchTermMultipleMatches() {
 final String testText = "This is a test SearchableTextPane. This is another test SearchableTextPane.";
 searchableTextPane.setText(testText);
 final int searchResult1 = searchableTextPane.search(DEFAULT_TERM);
 assertEquals(13, searchResult1);
 final int searchResult2 = searchableTextPane.search(DEFAULT_TERM);
 assertEquals(23, searchResult2);
 }

 @Test
 public void testSearchTermCaseSensitive() {
 final String testText = "This is a TEST SearchableTextPane.";
 searchableTextPane.setText(testText);
 final int searchResult = searchableTextPane.search(DEFAULT_TERM);
 assertEquals(-1, searchResult);
 }

 @Test
 public void testSearchTermWithFlags() {
 final String testText = "This is a test SearchableTextPane. This is another test SearchableTextPane.";
 searchableTextPane.setText(testText);
 final int searchResult = searchableTextPane.search("Test");
 assertEquals(0, searchResult);
 }

 @Test
 public void testAddHighlight() throws BadLocationException {
 final int start = 5;
 final int length = 3;
 searchableTextPane.addHighlight(start, length);
 assertEquals(Color.YELLOW, searchableTextPane.getHighlighter().getHighlightPainter().getColor());
 }

 @Test(expected = BadLocationException.class)
 public void testAddHighlightInvalidStart() throws BadLocationException {
 final int start = -1;
 final int length = 3;
 searchableTextPane.addHighlight(start, length);
 fail("Expected BadLocationException");
 }

 @Test(expected = BadLocationException.class)
 public void testAddHighlightInvalidEnd() throws BadLocationException {
 final int start = 0;
 final int length = -3;
 searchableTextPane.addHighlight(start, length);
 fail("Expected BadLocationException");
 }

 @Test
 public void testRemoveHighlights() {
 final int start = 0;
 final int length = 10;
 searchableTextPane.addHighlight(start, length);
 searchableTextPane.removeHighlights();
 assertFalse(searchableTextPane.getHighlighter().hasHighlights());
 }

 @After
 public void tearDown() {
 searchableTextPane = null;
 }
}