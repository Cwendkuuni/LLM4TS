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
import ghm.follow.search.SearchableTextPane.LineResult;
import ghm.follow.search.SearchableTextPane.WordResult;
import ghm.follow.search.Utilities;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private SearchEngine searchEngine;

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
 searchEngine = new SearchEngine(0);
 }

 @Test
 public void testSearchableTextPaneConstructor() {
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
 assertNotNull(searchableTextPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertFalse(textPane.getScrollableTracksViewportWidth());
 textPane.setParent(new JFrame());
 assertTrue(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testNullTermHighlight() {
 List<LineResult> lineResults = textPane.highlight(null, 0);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testEmptyTermHighlight() {
 List<WordResult> searchResults = Arrays.asList(new WordResult(0, 0, 0, 0, ""));
 List<LineResult> lineResults = textPane.convertWords2Lines(searchResults);
 textPane.highlight("", 0);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testSearchWithMatches() throws Exception {
 String text = "The quick brown fox jumps over the lazy dog.";
 textPane.setText(text);
 List<WordResult> searchResults = Arrays.asList(
 new WordResult(0, 2, 6, 8, "brown"),
 new WordResult(6, 18, 20, 21, "lazy"),
 new WordResult(19, 21, 23, 23, "dog")
 );
 List<LineResult> lineResults = textPane.highlight("brown lazy dog", 0);
 LineResult expectedLineResult1 = new LineResult(0, 2, 31);
 expectedLineResult1.addWord(new WordResult(0, 2, 6, 8, "brown"));
 expectedLineResult1.addWord(new WordResult(6, 11, 18, 18, "quick"));
 expectedLineResult1.addWord(new WordResult(11, 15, 16, 16, "brown"));
 expectedLineResult1.addWord(new WordResult(16, 18, 18, 18, "fox"));
 expectedLineResult1.addWord(new WordResult(18, 20, 20, 21, "lazy"));
 expectedLineResult1.addWord(new WordResult(20, 21, 23, 23, "dog"));
 LineResult expectedLineResult2 = new LineResult(1, 0, 31);
 expectedLineResult2.addWord(new WordResult(30, 30, 31, 31, "."));
 List<LineResult> expectedLineResults = Arrays.asList(expectedLineResult1, expectedLineResult2);
 assertEquals(expectedLineResults, lineResults);
 }

 @Test
 public void testSearchWithNoMatches() {
 List<WordResult> searchResults = Arrays.asList(new WordResult(0, 0, 0, 0, "apple"));
 List<LineResult> lineResults = textPane.highlight("apple", 0);
 assertTrue(lineResults.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullFlagsHighlight() {
 textPane.highlight(null, 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeFlagsHighlight() {
 textPane.highlight("test", -1);
 }

 @Test
 public void testSearchWithEmptyText() {
 textPane.setText("");
 List<LineResult> lineResults = textPane.highlight("test", 0);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testSearchWithTermAtStart() throws Exception {
 String text = "The quick brown fox jumps over the lazy dog.";
 textPane.setText(text);
 List<WordResult> searchResults = Arrays.asList(new WordResult(0, 0, 6, 6, "The"));
 List<LineResult> lineResults = textPane.highlight("The", 0);
 LineResult expectedLineResult = new LineResult(0, 0, 31);
 expectedLineResult.addWord(new WordResult(0, 0, 6, 6, "The"));
 expectedLineResult.addWord(new WordResult(6, 11, 18, 18, "quick"));
 expectedLineResult.addWord(new WordResult(11, 15, 16, 16, "brown"));
 expectedLineResult.addWord(new WordResult(15, 19, 19, 19, "fox"));
 expectedLineResult.addWord(new WordResult(19, 21, 21, 21, "jumps"));
 expectedLineResult.addWord(new WordResult(21, 25, 25, 25, "over"));
 expectedLineResult.addWord(new WordResult(25, 29, 29, 29, "the"));
 expectedLineResult.addWord(new WordResult(29, 30, 30, 30, "."));
 assertEquals(Arrays.asList(expectedLineResult), lineResults);
 }

 @Test
 public void testSearchWithTermAtEnd() throws Exception {
 String text = "The quick brown fox jumps over the lazy dog.";
 textPane.setText(text);
 List<WordResult> searchResults = Arrays.asList(new WordResult(30, 29, 31, 31, "."));
 List<LineResult> lineResults = textPane.highlight(".", 0);
 assertEquals(Arrays.asList(new LineResult(0, 0, 31)), lineResults);
 }

 @Test
 public void testSearchWithTermInMiddle() throws Exception {
 String text = "The quick brown fox jumps over the lazy dog.";
 textPane.setText(text);
 List<WordResult> searchResults = Arrays.asList(new WordResult(11, 15, 16, 16, "brown"),
 new WordResult(19, 21, 23, 23, "dog"));
 List<LineResult> lineResults = textPane.highlight("brown dog", 0);
 LineResult expectedLineResult1 = new LineResult(0, 2, 31);
 expectedLineResult1.addWord(new WordResult(0, 2, 6, 8, "The"));
 expectedLineResult1.addWord(new WordResult(6, 11, 18, 18, "quick"));
 expectedLineResult1.addWord(new WordResult(11, 15, 16, 16, "brown"));
 expectedLineResult1.addWord(new WordResult(15, 19, 19, 19, "fox"));
 expectedLineResult1.addWord(new WordResult(19, 21, 23, 23, "dog"));
 expectedLineResult1.addWord(new WordResult(23, 25, 25, 25, "jumps"));
 expectedLineResult1.addWord(new WordResult(25, 29, 29, 29, "over"));
 expectedLineResult1.addWord(new WordResult(29, 30, 30, 30, "."));
 LineResult expectedLineResult2 = new LineResult(1, 0, 31);
 expectedLineResult2.addWord(new WordResult(30, 30, 31, 31, "."));
 List<LineResult> expectedLineResults = Arrays.asList(expectedLineResult1, expectedLineResult2);
 assertEquals(expectedLineResults, lineResults);
 }

 @Test
 public void testSearchWithPartialMatch() throws Exception {
 String text = "The quick brown fox jumps over the lazy dog.";
 textPane.setText(text);
 List<WordResult> searchResults = Arrays.asList(new WordResult(6, 11, 18, 18, "quick"),
 new WordResult(19, 21, 23, 23, "dog"));
 List<LineResult> lineResults = textPane.highlight("qui", 0);
 LineResult expectedLineResult1 = new LineResult(0, 2, 31);
 expectedLineResult1.addWord(new WordResult(0, 2, 6, 8, "The"));
 expectedLineResult1.addWord(new WordResult(6, 11, 18, 18, "quick"));
 expectedLineResult1.addWord(new WordResult(11, 15, 16, 16, "brown"));
 expectedLineResult1.addWord(new WordResult(15, 19, 19, 19, "fox"));
 expectedLineResult1.addWord(new WordResult(19, 21, 23, 23, "dog"));
 expectedLineResult1.addWord(new WordResult(23, 25, 25, 25, "jumps"));
 expectedLineResult1.addWord(new WordResult(25, 29, 29, 29, "over"));
 expectedLineResult1.addWord(new WordResult(29, 30, 30, 30, "."));
 LineResult expectedLineResult2 = new LineResult(1, 0, 31);
 expectedLineResult2.addWord(new WordResult(30, 30, 31, 31, "."));
 List<LineResult> expectedLineResults = Arrays.asList(expectedLineResult1, expectedLineResult2);
 assertEquals(expectedLineResults, lineResults);
 }

 @Test(expected = BadLocationException.class)
 public void testSearchWithOutOfBoundsStartPosition() throws Exception {
 textPane.setText("The quick brown fox jumps over the lazy dog.");
 List<WordResult> searchResults = Arrays.asList(new WordResult(0, 0, 6, 6, "The"));
 textPane.highlight("The", -1);
 }

 @Test(expected = BadLocationException.class)
 public void testSearchWithOutOfBoundsEndPosition() throws Exception {
 textPane.setText("The quick brown fox jumps over the lazy dog.");
 List<WordResult> searchResults = Arrays.asList(new WordResult(30, 29, 31, 31, "."));
 textPane.highlight(".", doc.getLength());
 }

 @Test
 public void testSearchWithRepeatedTerm() throws Exception {
 String text = "The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog.";
 textPane.setText(text);
 List<WordResult> searchResults = Arrays.asList(new WordResult(0, 0, 6, 6, "The"),
 new WordResult(12, 25, 31, 31, "dog"),
 new WordResult(32, 35, 38, 38, "The"),
 new WordResult(45, 48, 51, 51, "dog"));
 List<LineResult> lineResults = textPane.highlight("The dog", 0);
 LineResult expectedLineResult1 = new LineResult(0, 0, 72);
 expectedLineResult1.addWord(new WordResult(0, 0, 6, 6, "The"));
 expectedLineResult1.addWord(new WordResult(6, 11, 18, 18, "quick"));
 expectedLineResult1.addWord(new WordResult(11, 15, 16, 16, "brown"));
 expectedLineResult1.addWord(new WordResult(15, 19, 19, 19, "fox"));
 expectedLineResult1.addWord(new WordResult(19, 21, 23, 23, "jumps"));
 expectedLineResult1.addWord(new WordResult(21, 25, 25, 25, "over"));
 expectedLineResult1.addWord(new WordResult(25, 29, 29, 29, "the"));
 expectedLineResult1.addWord(new WordResult(29, 33, 33, 33, "The"));
 expectedLineResult1.addWord(new WordResult(33, 38, 38, 38, "dog"));
 expectedLineResult1.addWord(new WordResult(38, 41, 41, 41, " "));
 expectedLineResult1.addWord(new WordResult(41, 45, 45, 45, "The"));
 expectedLineResult1.addWord(new WordResult(45, 48, 48, 48, "dog"));
 List<LineResult> expectedLineResults = Arrays.asList(expectedLineResult1);
 assertEquals(expectedLineResults, lineResults);
 }

 @Test(expected = BadLocationException.class)
 public void testSearchWithNegativeFlags() throws Exception {
 textPane.setText("The quick brown fox jumps over the lazy dog.");
 textPane.highlight("The quick brown fox jumps over the lazy dog.", -1);
 }

 @Test(expected = BadLocationException.class)
 public void testSearchWithNullDocument() throws Exception {
 textPane.setText("The quick brown fox jumps over the lazy dog.");
 SearchEngine searchEngine = new SearchEngine(0);
 textPane.highlight("The quick brown fox jumps over the lazy dog.", searchEngine.getDocument());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTextPane() {
 SearchEngine searchEngine = new SearchEngine(0);
 searchEngine.highlight(null, 0, 0, 0, 0);
 }

}