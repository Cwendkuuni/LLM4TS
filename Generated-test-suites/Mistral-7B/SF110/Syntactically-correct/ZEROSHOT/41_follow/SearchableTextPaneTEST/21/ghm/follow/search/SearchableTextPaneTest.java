package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.swing.*;
import javax.swing.text.*;

@RunWith(MockitoJUnitRunner.class)
public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private SearchEngine searchEngine;
 private DefaultHighlighter.DefaultHighlightPainter wordPainter;
 private List<LineResult> lineResults;

 @Before
 public void setup() {
 searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 8);
 searchEngine = Mockito.spy(new SearchEngine(SearchEngine.REGULAR));
 wordPainter = Mockito.mock(DefaultHighlighter.DefaultHighlightPainter.class);
 searchableTextPane.setHighlighter(Mockito.spy(new DefaultHighlighter(searchableTextPane.getDocument(), searchableTextPane)));
 lineResults = new ArrayList<>();
 }

 @Test
 public void testConstructor() {
 // check if constructor initializes required fields
 assertEquals("SearchableTextPane initialized with incorrect font", new Font("Arial", Font.PLAIN, 14), searchableTextPane.getFont());
 assertEquals("SearchableTextPane initialized with incorrect tabSize", 8, searchableTextPane.getTabSize());
 assertNotNull("SearchableTextPane initialized with null logger", searchableTextPane.getLog());
 assertNotNull("SearchableTextPane initialized with null lastSearchPos", searchableTextPane.lastSearchPos);
 assertNull("SearchableTextPane initialized with null lastSearchTerm", searchableTextPane.lastSearchTerm);
 assertNotNull("SearchableTextPane initialized with null wordPainter", searchableTextPane.wordPainter);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue("SearchableTextPane should not track viewport width if parent is null or smaller than preferred size", searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightWithEmptyTerm() {
 searchableTextPane.highlight(null, 0);
 assertTrue("Search should not have returned any LineResult when term is empty", lineResults.isEmpty());
 }

 @Test
 public void testHighlightWithNonExistentTerm() {
 String text = "This is a test text for SearchableTextPane. It doesn't contain the term 'nonExistentTerm'.";
 searchableTextPane.setText(text);
 searchableTextPane.highlight("nonExistentTerm", 0);
 assertTrue("Search should not have returned any LineResult when term does not exist in text", lineResults.isEmpty());
 }

 @Test
 public void testHighlightWithExistentTerm() {
 String text = "This is a test text for SearchableTextPane. It contains the term 'SearchableTextPane'.";
 searchableTextPane.setText(text);
 when(searchEngine.search("SearchableTextPane", text)).thenReturn(Arrays.asList(new WordResult(10, 20), new WordResult(30, 35)));
 searchableTextPane.highlight("SearchableTextPane", 0);
 assertEquals("Number of LineResult should match number of words in text", 1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals("Line number should match the expected line number", 0, lineResult.getLineNumber());
 assertEquals("Start offset of the first word in the line should match the expected start offset", 10, lineResult.getStartOffset());
 assertEquals("End offset of the last word in the line should match the expected end offset", 35, lineResult.getEndOffset());
 }

 @Test
 public void testHighlightWithMultipleTermsInOneLine() {
 String text = "This is a test text for SearchableTextPane. It contains the terms 'SearchableTextPane' and 'JUnit4'.";
 searchableTextPane.setText(text);
 when(searchEngine.search("SearchableTextPane JUnit4", text)).thenReturn(Arrays.asList(new WordResult(10, 21), new WordResult(45, 48), new WordResult(55, 59)));
 searchableTextPane.highlight("SearchableTextPane JUnit4", 0);
 assertEquals("Number of LineResult should match number of lines containing multiple words", 1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals("Line number should match the expected line number", 0, lineResult.getLineNumber());
 List<WordResult> wordResults = lineResult.getWordResults();
 assertEquals("Number of WordResult should match number of words in the line", 3, wordResults.size());
 assertEquals("First word's start offset should match the expected start offset", 10, wordResults.get(0).getStart());
 assertEquals("First word's end offset should match the expected end offset", 21, wordResults.get(0).getEnd());
 assertEquals("Second word's start offset should match the expected start offset", 45, wordResults.get(1).getStart());
 assertEquals("Second word's end offset should match the expected end offset", 48, wordResults.get(1).getEnd());
 assertEquals("Third word's start offset should match the expected start offset", 55, wordResults.get(2).getStart());
 assertEquals("Third word's end offset should match the expected end offset", 59, wordResults.get(2).getEnd());
 }

 @Test
 public void testHighlightWithFlags() {
 String text = "This is a test text for SearchableTextPane. It contains the term 'SearchableTextPane'.";
 searchableTextPane.setText(text);
 when(searchEngine.search("SearchableTextPane", text, SearchEngine.REGULAR)).thenReturn(Arrays.asList(new WordResult(10, 20), new WordResult(30, 35)));
 when(searchEngine.search("SearchableTextPane", text, SearchEngine.CASE_INSENSITIVE)).thenReturn(Arrays.asList(new WordResult(5, 10), new WordResult(30, 35), new WordResult(45, 48)));
 searchableTextPane.highlight("SearchableTextPane", 0, SearchEngine.REGULAR);
 assertEquals("Number of LineResult should match number of words in text when using REGULAR flags", 1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals("Line number should match the expected line number", 0, lineResult.getLineNumber());
 assertEquals("Start offset of the first word in the line should match the expected start offset", 10, lineResult.getStartOffset());
 assertEquals("End offset of the last word in the line should match the expected end offset", 35, lineResult.getEndOffset());
 searchableTextPane.highlight("SearchableTextPane", 0, SearchEngine.CASE_INSENSITIVE);
 assertEquals("Number of LineResult should match number of words in text when using CASE_INSENSITIVE flags", 3, lineResults.size());
 LineResult lineResultCaseInsensitive = lineResults.get(2);
 assertEquals("Line number should match the expected line number", 0, lineResultCaseInsensitive.getLineNumber());
 assertEquals("Start offset of the first word in the line should match the expected start offset", 45, lineResultCaseInsensitive.getStartOffset());
 assertEquals("End offset of the last word in the line should match the expected end offset", 48, lineResultCaseInsensitive.getEndOffset());
 }

 @Test
 public void testSearchWithEmptyTerm() {
 assertEquals(-1, searchableTextPane.search(null));
 }

 @Test
 public void testSearchWithNonExistentTerm() {
 searchableTextPane.setText("This is a test text for SearchableTextPane. It doesn't contain the term 'nonExistentTerm'.");
 assertEquals(-1, searchableTextPane.search("nonExistentTerm"));
 }

 @Test
 public void testSearchWithExistentTerm() {
 String text = "This is a test text for SearchableTextPane. It contains the term 'SearchableTextPane'.";
 searchableTextPane.setText(text);
 assertEquals(10, searchableTextPane.search("SearchableTextPane"));
 }

 @Test
 public void testSearchWithExistentTermStartingFromSpecificPosition() {
 String text = "This is a test text for SearchableTextPane. It contains the term 'SearchableTextPane'.";
 searchableTextPane.setText(text);
 assertEquals(21, searchableTextPane.search("SearchableTextPane", 10));
 }

 @Test
 public void testSearchWithMultipleTermsInOneLine() {
 String text = "This is a test text for SearchableTextPane. It contains the terms 'SearchableTextPane' and 'JUnit4'.";
 searchableTextPane.setText(text);
 assertEquals(21, searchableTextPane.search("SearchableTextPane JUnit4"));
 }

 @Test
 public void testSearchWithFlags() {
 String text = "This is a test text for SearchableTextPane. It contains the term 'SearchableTextPane'.";
 searchableTextPane.setText(text);
 assertEquals(10, searchableTextPane.search("SearchableTextPane", SearchEngine.REGULAR));
 assertEquals(5, searchableTextPane.search("SearchableTextPane", SearchEngine.CASE_INSENSITIVE));
 }

 @Test
 public void testConvertWords2LinesWithEmptyList() {
 List<WordResult> wordResults = new ArrayList<>();
 List<LineResult> lineResults = searchableTextPane.convertWords2Lines(wordResults);
 assertTrue("List of LineResult should be empty when List of WordResult is empty", lineResults.isEmpty());
 }

 @Test
 public void testConvertWords2LinesWithSingleWord() {
 List<WordResult> wordResults = Arrays.asList(new WordResult(10, 14));
 List<LineResult> lineResults = searchableTextPane.convertWords2Lines(wordResults);
 assertEquals("Number of LineResult should match number of words in text", 1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals("Line number should match the expected line number", 0, lineResult.getLineNumber());
 assertEquals("Start offset of the word in the line should match the expected start offset", 10, lineResult.getStartOffset());
 assertEquals("End offset of the word in the line should match the expected end offset", 14, lineResult.getEndOffset());
 }

 @Test
 public void testConvertWords2LinesWithMultipleWordsInOneLine() {
 List<WordResult> wordResults = Arrays.asList(new WordResult(10, 14), new WordResult(16, 19), new WordResult(22, 25));
 List<LineResult> lineResults = searchableTextPane.convertWords2Lines(wordResults);
 assertEquals("Number of LineResult should match number of lines containing multiple words", 1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals("Line number should match the expected line number", 0, lineResult.getLineNumber());
 List<WordResult> wordsInLine = lineResult.getWordResults();
 assertEquals("Number of WordResult should match number of words in the line", 3, wordsInLine.size());
 assertEquals("First word's start offset should match the expected start offset", 10, wordsInLine.get(0).getStart());
 assertEquals("First word's end offset should match the expected end offset", 14, wordsInLine.get(0).getEnd());
 assertEquals("Second word's start offset should match the expected start offset", 16, wordsInLine.get(1).getStart());
 assertEquals("Second word's end offset should match the expected end offset", 19, wordsInLine.get(1).getEnd());
 assertEquals("Third word's start offset should match the expected start offset", 22, wordsInLine.get(2).getStart());
 assertEquals("Third word's end offset should match the expected end offset", 25, wordsInLine.get(2).getEnd());
 }

 @Test
 public void testAddHighlight() throws BadLocationException {
 int start = 10;
 int length = 5;
 searchableTextPane.addHighlight(start, length);
 assertEquals("Number of highlights should match the expected number", 1, searchableTextPane.getHighlighter().getHighlights().size());
 Highlight highlight = searchableTextPane.getHighlighter().getHighlights().get(0);
 assertEquals("Highlight's start offset should match the expected start offset", start, highlight.getStartOffset());
 assertEquals("Highlight's length should match the expected length", length, highlight.getLength());
 }

 @Test
 public void testRemoveHighlights() {
 searchableTextPane.removeHighlights();
 assertEquals("Number of highlights should be 0 after removing all highlights", 0, searchableTextPane.getHighlighter().getHighlights().size());
 }
}