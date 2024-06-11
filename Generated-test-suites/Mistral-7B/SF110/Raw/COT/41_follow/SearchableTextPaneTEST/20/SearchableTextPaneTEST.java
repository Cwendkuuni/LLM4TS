package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
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
 textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
 searchEngine = new SearchEngine(SearchEngine.SEARCH_CASE_SENSITIVE);
 }

 @After
 public void tearDown() {
 textPane = null;
 searchEngine = null;
 }

 @Test
 public void testSearchableTextPaneConstructor() {
 SearchableTextPane textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
 assertNotNull(textPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyTerm() {
 String term = "";
 List<LineResult> lineResults = textPane.highlight(term, SearchEngine.SEARCH_CASE_INSENSITIVE);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightNullTerm() {
 String term = null;
 List<LineResult> lineResults = textPane.highlight(term, SearchEngine.SEARCH_CASE_INSENSITIVE);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightSingleWord() {
 String term = "Java";
 List<LineResult> lineResults = textPane.highlight(term, SearchEngine.SEARCH_CASE_INSENSITIVE);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 }

 @Test
 public void testHighlightMultipleWordsPerLine() {
 String term = "Java Swing";
 List<LineResult> lineResults = textPane.highlight(term, SearchEngine.SEARCH_CASE_INSENSITIVE);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(2, lineResult.getWordResults().size());
 }

 @Test
 public void testHighlightMultipleLines() {
 String text = "This is a Java Swing component\nThis is another Java Swing component";
 textPane.setText(text);
 String term = "Java";
 List<LineResult> lineResults = textPane.highlight(term, SearchEngine.SEARCH_CASE_INSENSITIVE);
 assertEquals(2, lineResults.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHighlightNegativeFlags() {
 String term = "Java";
 textPane.highlight(term, -1);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testSearchEmptyTerm() {
 String term = "";
 int pos = textPane.search(term);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchNullTerm() {
 String term = null;
 int pos = textPane.search(term);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchSingleWord() {
 String term = "Java";
 int pos = textPane.search(term);
 assertEquals(0, pos);
 }

 @Test
 public void testSearchMultipleWords() {
 String term = "Java Swing";
 int pos = textPane.search(term);
 assertEquals(textPane.getText().indexOf("Java Swing"), pos);
 }

 @Test
 public void testSearchMultipleWordsDifferentLines() {
 String text = "This is a Java Swing component\nThis is another Java Swing component";
 textPane.setText(text);
 String term = "Java Swing";
 int pos = textPane.search(term);
 assertEquals(textPane.getLineOfOffset(textPane.getLineNumber()) + textPane.getLineStartOffset(textPane.getLineNumber()), pos);
 }

 @Test
 public void testSearchCaseSensitive() {
 String text = "This is a Java Swing component\nThis is another Java Swing component";
 textPane.setText(text);
 String term = "java";
 int pos = textPane.search(term, SearchEngine.SEARCH_CASE_SENSITIVE);
 assertEquals(-1, pos);
 term = "Java";
 int pos2 = textPane.search(term, SearchEngine.SEARCH_CASE_SENSITIVE);
 assertEquals(0, pos2);
 }

 @Test
 public void testSearchCaseInsensitive() {
 String text = "This is a Java Swing component\nThis is another Java Swing component";
 textPane.setText(text);
 String term = "java";
 int pos = textPane.search(term, SearchEngine.SEARCH_CASE_INSENSITIVE);
 assertEquals(0, pos);
 }

 @Test
 public void testSearchFail() {
 String term = "Xyz";
 int pos = textPane.search(term);
 assertEquals(-1, pos);
 }

 @Test
 public void testConvertWords2LinesEmptyList() {
 String text = "";
 List<WordResult> wordResults = new ArrayList<>();
 List<LineResult> lineResults = SearchableTextPane.convertWords2Lines(wordResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testConvertWords2LinesNullList() {
 String text = "Java Swing";
 List<LineResult> lineResults = SearchableTextPane.convertWords2Lines(null);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testConvertWords2LinesSingleWord() {
 String text = "Java";
 List<WordResult> wordResults = new ArrayList<>();
 wordResults.add(new WordResult(1, "Java".length(), 0, 1, "Java"));
 List<LineResult> lineResults = SearchableTextPane.convertWords2Lines(wordResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getWordResults().size());
 }

 @Test
 public void testConvertWords2LinesMultipleWordsPerLine() {
 String text = "Java Swing Java";
 List<WordResult> wordResults = new ArrayList<>();
 wordResults.add(new WordResult(1, "Java".length(), 0, 1, "Java"));
 wordResults.add(new WordResult(1, "Swing".length(), "Java Swing".length(), 2, "Swing"));
 wordResults.add(new WordResult(1, "Java".length(), "Java Swing Java".length(), 3, "Java"));
 List<LineResult> lineResults = SearchableTextPane.convertWords2Lines(wordResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(3, lineResult.getWordResults().size());
 }

 @Test
 public void testConvertWords2LinesMultipleLines() {
 String text = "Java Swing\nJavaFX";
 List<WordResult> wordResults = new ArrayList<>();
 wordResults.add(new WordResult(1, "Java".length(), 0, 1, "Java"));
 wordResults.add(new WordResult(1, "Swing".length(), 6, 2, "Swing"));
 wordResults.add(new WordResult(2, "JavaFX".length(), 0, 1, "JavaFX"));
 List<LineResult> lineResults = SearchableTextPane.convertWords2Lines(wordResults);
 assertEquals(2, lineResults.size());
 }

 @Test
 public void testRemoveHighlights() {
 List<LineResult> lineResults = new ArrayList<>();
 LineResult lineResult = new LineResult(0, 0, 100);
 lineResult.addWord(new WordResult(0, 5, 0, 5, "Java"));
 lineResults.add(lineResult);
 textPane.highlight("Java", SearchEngine.SEARCH_CASE_INSENSITIVE);
 textPane.removeHighlights();
 boolean highlightsPresent = false;
 for (LineResult lineResult1 : lineResults) {
 for (WordResult wordResult : lineResult1.getWordResults()) {
 if (textPane.isHighlighted(wordResult.getStart(), wordResult.getLength())) {
 highlightsPresent = true;
 break;
 }
 }
 }
 assertTrue(highlightsPresent == false);
 }

}