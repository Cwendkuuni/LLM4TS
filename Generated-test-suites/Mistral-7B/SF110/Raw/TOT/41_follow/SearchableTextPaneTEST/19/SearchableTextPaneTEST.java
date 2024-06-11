import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Element;
import javax.swing.text.LineElement;
import javax.swing.text.LineView;
import javax.swing.text.ParagraphElement;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import ghm.follow.search.SearchableTextPane;
import ghm.follow.search.SearchEngine;
import ghm.follow.search.Utilities;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private Document document;

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 12), 8);
 document = textPane.getDocument();
 }

 @Test
 public void testConstructor() {
 // Test that the constructor initializes the necessary fields
 assertNotNull(textPane.getDocument());
 assertNotNull(textPane.getUI());
 assertNotNull(textPane.getLog());
 assertNotNull(textPane.getLastSearchPos());
 assertNotNull(textPane.getLastSearchTerm());
 assertNotNull(textPane.getWordPainter());
 assertNotNull(textPane.getFont());
 assertNotNull(textPane.getTabSize());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 // Test that getScrollableTracksViewportWidth returns the correct value
 assertTrue(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testSearchEmptyString() {
 // Test searching an empty string
 List<LineResult> results = textPane.highlight("", 0);
 assertEquals(0, results.size());
 }

 @Test
 public void testSearchSingleWord() {
 // Test searching for a single word
 document.insertString(0, "The quick brown fox jumps over the lazy dog.", null);
 List<LineResult> results = textPane.highlight("fox", 0);
 assertEquals(1, results.size());
 assertNotNull(results.get(0).getWordResults());
 assertEquals(1, results.get(0).getWordResults().size());
 assertEquals("fox", results.get(0).getWordResults().get(0).getWord());
 }

 @Test
 public void testSearchMultipleWordsInLine() {
 // Test searching for multiple words in a single line
 document.insertString(0, "John doe john smith john doe.", null);
 List<LineResult> results = textPane.highlight("john doe", 0);
 assertEquals(1, results.size());
 assertNotNull(results.get(0).getWordResults());
 assertEquals(2, results.get(0).getWordResults().size());
 List<WordResult> wordResults = results.get(0).getWordResults();
 assertEquals("john doe", wordResults.get(0).getWord());
 assertEquals("john doe", wordResults.get(1).getWord());
 }

 @Test
 public void testSearchMultipleWordsAcrossLines() {
 // Test searching for multiple words that span multiple lines
 document.insertString(0, "The quick brown fox jumps over the lazy dog.\nThe quick brown fox jumps over the lazy dog.", null);
 List<LineResult> results = textPane.highlight("quick brown fox", 0);
 assertEquals(1, results.size());
 assertNotNull(results.get(0).getWordResults());
 assertEquals(3, results.get(0).getWordResults().size());
 List<WordResult> wordResults = results.get(0).getWordResults();
 assertEquals("quick", wordResults.get(0).getWord());
 assertEquals("brown", wordResults.get(1).getWord());
 assertEquals("fox", wordResults.get(2).getWord());
 }

 @Test
 public void testSearchCaseInsensitive() {
 // Test searching for a word with different capitalization
 document.insertString(0, "The quick brown fox jumps over the lazy dog.", null);
 List<LineResult> results = textPane.highlight("fox", 0);
 assertEquals(1, results.size());
 assertEquals(1, results.get(0).getWordResults().size());
 assertEquals("fox", results.get(0).getWordResults().get(0).getWord());
 List<LineResult> results2 = textPane.highlight("Fox", 0);
 assertEquals(1, results2.size());
 assertEquals(1, results2.get(0).getWordResults().size());
 assertEquals("fox", results2.get(0).getWordResults().get(0).getWord());
 }

 @Test
 public void testSearchWithFlags() {
 // Test searching with various flags
 List<WordResult> searchResults = new SearchEngine(SearchEngine.REGEX_FLAG).search("\\bfox\\b", "The quick brown fox jumps over the lazy dog.");
 List<LineResult> results = textPane.highlight(searchResults.get(0).getWord(), SearchEngine.REGEX_FLAG);
 assertEquals(1, results.size());
 assertNotNull(results.get(0).getWordResults());
 assertEquals(1, results.get(0).getWordResults().size());
 List<WordResult> wordResults = results.get(0).getWordResults();
 assertEquals(searchResults.get(0).getWord(), wordResults.get(0).getWord());
 }

 @Test
 public void testSearchWithError() {
 // Test searching with invalid flags
 List<LineResult> results = textPane.highlight("", 0, (short) 0xFFFF);
 assertEquals(0, results.size());
 List<LineResult> results2 = textPane.highlight("", 0, (short) -1);
 assertEquals(0, results2.size());
 }

 @Test
 public void testRemoveHighlights() {
 // Test removing highlights
 textPane.highlight("test", 0);
 textPane.removeHighlights();
 assertNull(textPane.getHighlighter().getHighlights()[0]);
 }

 @Test
 public void testSearchWithPreviousSearchTerm() {
 // Test searching with a previous search term
 textPane.search("test");
 textPane.search("fox");
 List<LineResult> results = textPane.highlight("fox", 0);
 assertEquals(1, results.size());
 assertNotNull(results.get(0).getWordResults());
 assertEquals(1, results.get(0).getWordResults().size());
 assertEquals("fox", results.get(0).getWordResults().get(0).getWord());
 }

 @Test
 public void testSearchWithPreviousSearchTermAtEnd() {
 // Test searching with a previous search term at the end of the text
 document.insertString(document.getLength(), "The quick brown fox jumps over the lazy dog.", null);
 textPane.search("dog");
 textPane.search("fox");
 List<LineResult> results = textPane.highlight("fox", 0);
 assertEquals(1, results.size());
 assertNotNull(results.get(0).getWordResults());
 assertEquals(1, results.get(0).getWordResults().size());
 assertEquals("fox", results.get(0).getWordResults().get(0).getWord());
 }

 @Test
 public void testSearchWithPreviousSearchTermInMiddle() {
 // Test searching with a previous search term in the middle of the text
 document.insertString(document.getLength(), "The quick brown fox jumps over the lazy dog.", null);
 textPane.search("jumps");
 textPane.search("fox");
 List<LineResult> results = textPane.highlight("fox", 0);
 assertEquals(1, results.size());
 assertNotNull(results.get(0).getWordResults());
 assertEquals(1, results.get(0).getWordResults().size());
 assertEquals("fox", results.get(0).getWordResults().get(0).getWord());
 }

 @Test
 public void testSearchWithPreviousSearchTermNotFound() {
 // Test searching with a previous search term that was not found
 textPane.search("test");
 textPane.search("zebra");
 List<LineResult> results = textPane.highlight("zebra", 0);
 assertEquals(0, results.size());
 }

 @Test
 public void testHighlightLine() {
 // Test highlighting a line
 List<LineResult> results = textPane.highlight("line", 0);
 assertEquals(1, results.size());
 assertNotNull(results.get(0).getWordResults());
 assertEquals(0, results.get(0).getWordResults().size());
 LineView lineView = textPane.getLineView(results.get(0).getLineNumber() - 1);
 assertNotNull(lineView);
 assertNotNull(lineView.getBackground());
 assertEquals(Color.YELLOW, lineView.getBackground());
 }

 @Test
 public void testHighlightWord() {
 // Test highlighting a word
 List<LineResult> results = textPane.highlight("fox", 0);
 assertEquals(1, results.size());
 assertNotNull(results.get(0).getWordResults());
 assertEquals(1, results.get(0).getWordResults().size());
 WordResult wordResult = results.get(0).getWordResults().get(0);
 assertEquals("fox", wordResult.getWord());
 assertNotNull(wordResult.getHighlightPainter());
 assertEquals(Color.YELLOW, wordResult.getHighlightPainter().getColor());
 }

 @Test
 public void testHighlightMultipleWords() {
 // Test highlighting multiple words
 document.insertString(0, "The quick brown fox jumps over the lazy dog.", null);
 List<LineResult> results = textPane.highlight("quick brown fox jumps", 0);
 assertEquals(1, results.size());
 assertNotNull(results.get(0).getWordResults());
 assertEquals(5, results.get(0).getWordResults().size());
 List<WordResult> wordResults = results.get(0).getWordResults();
 assertEquals("quick", wordResults.get(0).getWord());
 assertEquals("brown", wordResults.get(1).getWord());
 assertEquals("fox", wordResults.get(2).getWord());
 assertEquals("jumps", wordResults.get(3).getWord());
 assertEquals("over", wordResults.get(4).getWord());
 }

 @Test
 public void testHighlightMultipleWordsOnMultipleLines() {
 // Test highlighting multiple words that span multiple lines
 document.insertString(0, "The quick brown fox jumps over the lazy dog.\nThe quick brown fox jumps over the lazy dog.", null);
 List<LineResult> results = textPane.highlight("quick brown fox jumps over", 0);
 assertEquals(1, results.size());
 assertNotNull(results.get(0).getWordResults());
 assertEquals(5, results.get(0).getWordResults().size());
 List<WordResult> wordResults = results.get(0).getWordResults();
 assertEquals("quick", wordResults.get(0).getWord());
 assertEquals("brown", wordResults.get(1).getWord());
 assertEquals("fox", wordResults.get(2).getWord());
 assertEquals("jumps", wordResults.get(3).getWord());
 assertEquals("over", wordResults.get(4).getWord());
 }

 @Test
 public void testConvertWords2Lines() {
 // Test converting words to lines
 List<WordResult> words = Arrays.asList(
 new WordResult("quick", 0, 5),
 new WordResult("brown", 6, 11),
 new WordResult("fox", 13, 17),
 new WordResult("jumps", 19, 23),
 new WordResult("over", 25, 29),
 new WordResult("lazy", 32, 37),
 new WordResult("dog", 39, 42)
 );
 List<LineResult> lines = textPane.convertWords2Lines(words);
 assertEquals(2, lines.size());
 LineResult line1 = lines.get(0);
 LineResult line2 = lines.get(1);
 assertEquals(1, line1.getWordResults().size());
 assertEquals("quick brown fox", line1.getWordResults().get(0).getWord());
 assertEquals(1, line2.getWordResults().size());
 assertEquals("jumps over lazy dog", line2.getWordResults().get(0).getWord());
 }
}