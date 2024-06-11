package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ghm.follow.search.SearchableTextPane;
import ghm.follow.search.SearchEngine;
import ghm.follow.search.Utilities;
import ghm.follow.search.WordResult;
import ghm.follow.search.LineResult;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private static final String SEARCH_TERM = "test";

 @Test
 public void testSearchableTextPaneConstructor() {
     Font font = new Font("Arial", Font.PLAIN, 14);
     int tabSize = 4;
     textPane = new SearchableTextPane(font, tabSize);
     assertNotNull(textPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
     textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 4);
     assertTrue(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightMethodEmptyTerm() {
     String term = "";
     textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 4);
     List<LineResult> lineResults = textPane.highlight(term, 0);
     assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightMethodNullTerm() {
     textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 4);
     List<LineResult> lineResults = textPane.highlight(null, 0);
     assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightMethodValidTerm() throws Exception {
     String term = "test";
     textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 4);
     List<WordResult> searchResults = new SearchEngine(SearchEngine. flags()).search(term, "This is a test test test.");
     List<LineResult> lineResults = textPane.highlight(term, 0);
     assertEquals(1, lineResults.size());
     LineResult lineResult = lineResults.get(0);
     assertEquals(1, lineResult.getWordResults().size());
     WordResult wordResult = lineResult.getWordResults().get(0);
     assertEquals(term.length(), wordResult.length());
     assertEquals(SearchEngine.flags(), wordResult.flags);
     assertEquals(Color.YELLOW, textPane.getHighlighter().getHighlightPainter().getColor());
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightMethodWithException() {
     textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 4);
     List<LineResult> lineResults = textPane.highlight("test", 0);
     fail("Expected BadLocationException");
 }

 @Test
 public void testRemoveHighlights() {
     textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 4);
     List<LineResult> lineResults = textPane.highlight("test", 0);
     textPane.removeHighlights();
     assertEquals(0, lineResults.size());
 }

 @Test
 public void testSearchMethodEmptyTerm() {
     textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 4);
     assertEquals(-1, textPane.search(""));
 }

 @Test
 public void testSearchMethodNullTerm() {
     textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 4);
     assertEquals(-1, textPane.search(null));
 }

 @Test
 public void testSearchMethodValidTerm() {
     textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 4);
     textPane.setText("This is a test test test.");
     assertEquals(11, textPane.search("test"));
 }

 @Test
 public void testSearchMethodSameTermWithSamePosition() {
     textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 4);
     textPane.setText("This is a test test test.");
     assertEquals(11, textPane.search("test", 11));
 }

 @Test
 public void testSearchMethodDifferentTerm() {
     textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 4);
     textPane.setText("This is a test test test.");
     assertEquals(-1, textPane.search("search", 0));
 }

 @Test
 public void testSearchMethodWithException() {
     textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 4);
     assertEquals(-1, textPane.search("test", -1));
     fail("Expected BadLocationException");
 }

 @Test
 public void testConvertWords2LinesEmptyList() throws Exception {
     textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 4);
     List<WordResult> wordResults = new ArrayList<WordResult>();
     List<LineResult> lineResults = textPane.convertWords2Lines(wordResults);
     assertEquals(0, lineResults.size());
 }

 @Test
 public void testConvertWords2LinesValidList() throws Exception {
     textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 4);
     List<WordResult> wordResults = new ArrayList<WordResult>();
     wordResults.add(new WordResult("test", 1, 5, 10, SearchEngine.flags()));
     wordResults.add(new WordResult("test", 10, 12, 14, SearchEngine.flags()));
     List<LineResult> lineResults = textPane.convertWords2Lines(wordResults);
     assertEquals(1, lineResults.size());
     LineResult lineResult = lineResults.get(0);
     assertEquals(1, lineResult.getWordResults().size());
     assertEquals("1:5-11", lineResult.toString());
 }

 @Test
 public void testUpdateWordResultValidData() throws Exception {
     textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 4);
     List<WordResult> wordResults = new ArrayList<WordResult>();
     wordResults.add(new WordResult("test", 1, 5, 10, SearchEngine.flags()));
     LineResult lineResult = new LineResult(1, 0, 20, new ArrayList<WordResult>());
     textPane.updateWordResult(wordResults.get(0), lineResult);
     assertEquals(1, lineResult.getWordResults().size());
     assertEquals("test", lineResult.getWordResults().get(0).getWord());
 }

 @Test
 public void testUpdateWordResultNullData() throws Exception {
     textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 4);
     List<WordResult> wordResults = new ArrayList<WordResult>();
     wordResults.add(new WordResult("test", 1, 5, 10, SearchEngine.flags()));
     LineResult lineResult = new LineResult(1, 0, 20, new ArrayList<WordResult>());
     textPane.updateWordResult(null, lineResult);
     assertEquals(0, lineResult.getWordResults().size());
 }
}