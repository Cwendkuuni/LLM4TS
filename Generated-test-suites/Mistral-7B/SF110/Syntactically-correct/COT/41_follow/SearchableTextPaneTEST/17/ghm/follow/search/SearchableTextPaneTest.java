package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
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
 private static final String SEARCH_TERM = "TEST";
 private static final int TAB_SIZE = 4;
 private static final int FLAGS = SearchEngine.REGEX;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), TAB_SIZE);
 }

 @Test
 public void testSearchableTextPaneConstructor() {
 assertNotNull(searchableTextPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightWithEmptyString() {
 List<LineResult> lineResults = searchableTextPane.highlight(null, FLAGS);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightWithNullString() {
 List<LineResult> lineResults = searchableTextPane.highlight(null, FLAGS);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightWithEmptyDocument() throws Exception {
 searchableTextPane.setText("");
 List<LineResult> lineResults = searchableTextPane.highlight(SEARCH_TERM, FLAGS);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightWithSingleWord() throws Exception {
 String expectedText = "This is a test Test is a test.";
 searchableTextPane.setText(expectedText);
 List<LineResult> lineResults = searchableTextPane.highlight(SEARCH_TERM, FLAGS);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(expectedText.length(), lineResult.getEndOffset());
 }

 @Test
 public void testHighlightWithMultipleWordsPerLine() throws Exception {
 String expectedText = "This is a test Test is a test Test is a test Test is a test";
 searchableTextPane.setText(expectedText);
 List<LineResult> lineResults = searchableTextPane.highlight(SEARCH_TERM, FLAGS);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(expectedText.length(), lineResult.getEndOffset());
 List<WordResult> wordResults = lineResult.getWordResults();
 assertEquals(5, wordResults.size());
 for (WordResult wordResult : wordResults) {
 assertEquals(SEARCH_TERM, wordResult.getWord());
 }
 }

 @Test
 public void testHighlightWithMultipleLines() throws Exception {
 String expectedText = "Line 1: This is a test Line 1: Test is a test Line 2: Test is a test Line 2: Test is a test Line 2: Test is a test";
 searchableTextPane.setText(expectedText);
 List<LineResult> lineResults = searchableTextPane.highlight(SEARCH_TERM, FLAGS);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(1, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getStartOffset());
 assertEquals(expectedText.length() / 2, lineResult1.getEndOffset());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(2, lineResult2.getLineNumber());
 assertEquals(expectedText.length() / 2, lineResult2.getStartOffset());
 assertEquals(expectedText.length(), lineResult2.getEndOffset());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHighlightWithNegativeFlags() {
 searchableTextPane.highlight(SEARCH_TERM, -1);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = NullPointerException.class)
 public void testHighlightWithNullTextPane() {
 SearchableTextPane nullTextPane = null;
 nullTextPane.highlight(SEARCH_TERM, FLAGS);
 fail("NullPointerException expected but not thrown");
 }

 @Test
 public void testRemoveHighlights() throws Exception {
 String expectedText = "This is a test Test is a test Test is a test Test is a test";
 searchableTextPane.setText(expectedText);
 List<LineResult> lineResults = searchableTextPane.highlight(SEARCH_TERM, FLAGS);
 assertEquals(1, lineResults.size());
 lineResults.get(0).getLineNumber(); // This will add highlights
 searchableTextPane.removeHighlights();
 List<LineResult> lineResultsAfterRemoval = searchableTextPane.highlight(SEARCH_TERM, FLAGS);
 assertEquals(0, lineResultsAfterRemoval.size());
 }

 @Test
 public void testSearchWithEmptyString() {
 int expectedPosition = -1;
 int actualPosition = searchableTextPane.search(null);
 assertEquals(expectedPosition, actualPosition);
 }

 @Test
 public void testSearchWithEmptyTextPane() {
 int expectedPosition = -1;
 int actualPosition = searchableTextPane.search(SEARCH_TERM);
 assertEquals(expectedPosition, actualPosition);
 }

 @Test
 public void testSearchWithSingleWord() throws Exception {
 String expectedText = "This is a test Test is a test.";
 searchableTextPane.setText(expectedText);
 int expectedPosition = 0;
 int actualPosition = searchableTextPane.search(SEARCH_TERM);
 assertEquals(expectedPosition, actualPosition);
 }

 @Test
 public void testSearchWithMultipleWordsPerLine() throws Exception {
 String expectedText = "This is a test Test is a test Test is a test Test is a test";
 searchableTextPane.setText(expectedText);
 int expectedPosition = expectedText.indexOf(SEARCH_TERM, 0);
 int actualPosition = searchableTextPane.search(SEARCH_TERM);
 assertEquals(expectedPosition, actualPosition);
 }

 @Test
 public void testSearchWithMultipleLines() throws Exception {
 String expectedText = "Line 1: This is a test Line 1: Test is a test Line 2: Test is a test Line 2: Test is a test Line 2: Test is a test";
 searchableTextPane.setText(expectedText);
 int expectedPosition = expectedText.indexOf(SEARCH_TERM, 11);
 int actualPosition = searchableTextPane.search(SEARCH_TERM);
 assertEquals(expectedPosition, actualPosition);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchWithNegativeFlags() {
 searchableTextPane.search(SEARCH_TERM, -1);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = NullPointerException.class)
 public void testSearchWithNullTextPane() {
 SearchableTextPane nullTextPane = null;
 nullTextPane.search(SEARCH_TERM);
 fail("NullPointerException expected but not thrown");
 }

}