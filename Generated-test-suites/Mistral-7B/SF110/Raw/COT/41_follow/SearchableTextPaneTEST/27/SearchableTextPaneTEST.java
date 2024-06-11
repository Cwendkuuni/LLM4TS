package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private String term;

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 12), 8);
 term = "test";
 }

 @Test
 public void testSearchableTextPaneConstructor() {
 SearchableTextPane searchedTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 12), 8);
 assertNotNull(searchedTextPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.getContentPane().add(textPane);
 frame.pack();
 frame.setVisible(true);
 assertFalse(textPane.getScrollableTracksViewportWidth());
 frame.dispose();
 }

 @Test
 public void testHighlightEmptyTerm() {
 textPane.highlight(null, 0);
 assertEquals(0, textPane.getDocument().getLength());
 }

 @Test
 public void testHighlightNullDocument() {
 textPane.setText("");
 textPane.highlight(term, 0);
 assertEquals(0, textPane.getDocument().getLength());
 }

 @Test
 public void testHighlightSuccess() {
 textPane.setText("The quick brown fox jumps over the lazy dog test test test.");
 textPane.highlight(term, 0);
 List<LineResult> lineResults = textPane.getHighlightResults();
 assertEquals(5, lineResults.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHighlightNegativeFlags() {
 textPane.highlight(term, -1);
 }

 @Test
 public void testRemoveHighlights() {
 textPane.setText("The quick brown fox jumps over the lazy dog test test test.");
 textPane.highlight(term, 0);
 List<LineResult> lineResults = textPane.getHighlightResults();
 textPane.removeHighlights();
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testSearchNullTerm() {
 textPane.setText("The quick brown fox jumps over the lazy dog test test test.");
 textPane.search(null);
 assertEquals(-1, textPane.getLastSearchPos());
 }

 @Test
 public void testSearchEmptyTerm() {
 textPane.setText("The quick brown fox jumps over the lazy dog test test test.");
 textPane.search("");
 assertEquals(-1, textPane.getLastSearchPos());
 }

 @Test
 public void testSearchSuccess() {
 textPane.setText("The quick brown fox jumps over the lazy dog test test test.");
 textPane.search("test");
 assertEquals(2, textPane.getLastSearchPos());
 }

 @Test
 public void testSearchSameTermTwice() {
 textPane.setText("The quick brown fox jumps over the lazy dog test test test.");
 textPane.search("test");
 textPane.search("test");
 assertEquals(7, textPane.getLastSearchPos());
 }

 @Test
 public void testSearchDifferentTerms() {
 textPane.setText("The quick brown fox jumps over the lazy dog test test test.");
 textPane.search("test");
 textPane.search("dog");
 assertEquals(-1, textPane.getLastSearchPos());
 }

 @Test
 public void testSearchOutOfBounds() {
 textPane.setText("The quick brown fox jumps over the lazy dog test test test.");
 textPane.search("test", textPane.getDocument().getLength());
 assertEquals(-1, textPane.getLastSearchPos());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchNegativeTermLength() {
 textPane.setText("The quick brown fox jumps over the lazy dog test test test.");
 textPane.search("test", -1);
 }

 @Test
 public void testSearchWithHighlights() {
 textPane.setText("The quick brown fox jumps over the lazy dog test test test.");
 textPane.highlight(term, 0);
 textPane.search(term);
 assertEquals(3, textPane.getLastSearchPos());
 }

 @Test
 public void testConvertWords2LinesEmptyList() {
 List<WordResult> emptyList = new ArrayList<>();
 List<LineResult> lineResults = SearchableTextPane.convertWords2Lines(emptyList);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testConvertWords2LinesSingleWord() {
 List<WordResult> singleWordList = Arrays.asList(new WordResult("test", 1, 4));
 List<LineResult> lineResults = SearchableTextPane.convertWords2Lines(singleWordList);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(5, lineResult.getEndOffset());
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals("test", wordResult.getTerm());
 assertEquals(1, wordResult.getStart());
 assertEquals(4, wordResult.getEnd());
 }

 @Test
 public void testConvertWords2LinesMultipleWords() {
 List<WordResult> multipleWordsList = Arrays.asList(
 new WordResult("test1", 1, 4),
 new WordResult("test2", 10, 14),
 new WordResult("test3", 20, 23)
 );
 List<LineResult> lineResults = SearchableTextPane.convertWords2Lines(multipleWordsList);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(27, lineResult.getEndOffset());
 assertEquals(3, lineResult.getWordResults().size());
 WordResult wordResult1 = lineResult.getWordResults().get(0);
 assertEquals("test1", wordResult1.getTerm());
 assertEquals(1, wordResult1.getStart());
 assertEquals(4, wordResult1.getEnd());
 WordResult wordResult2 = lineResult.getWordResults().get(1);
 assertEquals("test2", wordResult2.getTerm());
 assertEquals(10, wordResult2.getStart());
 assertEquals(14, wordResult2.getEnd());
 WordResult wordResult3 = lineResult.getWordResults().get(2);
 assertEquals("test3", wordResult3.getTerm());
 assertEquals(20, wordResult3.getStart());
 assertEquals(23, wordResult3.getEnd());
 }

 @Test
 public void testUpdateWordResultNullParent() {
 WordResult wordResult = new WordResult("test", 1, 4);
 LineResult lineResult = new LineResult(0, 0, 5);
 assertNull(SearchableTextPane.updateWordResult(wordResult, null));
 }

 @Test
 public void testUpdateWordResultNullWordResult() {
 LineResult lineResult = new LineResult(0, 0, 5);
 assertNull(SearchableTextPane.updateWordResult(null, lineResult));
 }

 @Test
 public void testUpdateWordResult() {
 WordResult wordResult = new WordResult("test", 1, 4);
 LineResult lineResult = new LineResult(0, 0, 5);
 SearchableTextPane.updateWordResult(wordResult, lineResult);
 assertEquals(0, wordResult.getParent().lineNumber);
 assertEquals(0, wordResult.getLineOffset());
 }

}