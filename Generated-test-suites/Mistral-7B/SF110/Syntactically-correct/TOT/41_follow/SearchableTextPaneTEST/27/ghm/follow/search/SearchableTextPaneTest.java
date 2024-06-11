package ghm.follow.search;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private static final String SEARCH_TERM = "JUnit";
 private static final String ERROR_TERM = "";
 private static final String TAB_TEXT = "\t";

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyString() {
 List<LineResult> lineResults = textPane.highlight(ERROR_TERM, 0);
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightSingleWord() {
 textPane.setText("This is a test SearchableTextPane with JUnit.");
 List<LineResult> lineResults = textPane.highlight(SEARCH_TERM, 0);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 List<WordResult> wordResults = lineResult.getWordResults();
 assertEquals(1, wordResults.size());
 WordResult wordResult = wordResults.get(0);
 assertEquals(6, wordResult.getStartOffset());
 assertEquals(11, wordResult.getEndOffset());
 }

 @Test
 public void testHighlightMultipleWords() {
 textPane.setText("This is a test SearchableTextPane with JUnit testing framework.");
 List<LineResult> lineResults = textPane.highlight(SEARCH_TERM, 0);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 List<WordResult> wordResults = lineResult.getWordResults();
 assertEquals(2, wordResults.size());
 WordResult wordResult1 = wordResults.get(0);
 assertEquals(6, wordResult1.getStartOffset());
 assertEquals(11, wordResult1.getEndOffset());
 WordResult wordResult2 = wordResults.get(1);
 assertEquals(17, wordResult2.getStartOffset());
 assertEquals(22, wordResult2.getEndOffset());
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightWithBadLocationException() {
 textPane.setText("This is a test SearchableTextPane with JUnit.");
 textPane.highlight(SEARCH_TERM, -1);
 }

 @Test
 public void testSearch() {
 textPane.setText("This is a test SearchableTextPane with JUnit testing framework.");
 int position = textPane.search(SEARCH_TERM);
 assertEquals(6, position);
 }

 @Test
 public void testSearchEmptyString() {
 textPane.setText("This is a test SearchableTextPane with JUnit testing framework.");
 int position = textPane.search("");
 assertEquals(-1, position);
 }

 @Test
 public void testSearchMultipleTerms() {
 textPane.setText("This is a test SearchableTextPane with JUnit testing framework.");
 int position1 = textPane.search("testing framework");;
 assertEquals(22, position1);
 int position2 = textPane.search("JUnit");
 assertEquals(6, position2);
 }

 @Test
 public void testSearchDifferentStartPosition() {
 textPane.setText("This is a test SearchableTextPane with JUnit testing framework.");
 int position1 = textPane.search(SEARCH_TERM, 23);
 assertEquals(-1, position1);
 int position2 = textPane.search(SEARCH_TERM, 15);
 assertEquals(11, position2);
 }

 @Test
 public void testConvertWords2Lines() {
 textPane.setText("Line 1: This is a test.\nLine 2: SearchableTextPane with JUnit testing framework.");
 List<LineResult> lineResults = textPane.highlight(SEARCH_TERM, 0);
 assertNotNull(lineResults);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(1, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getStartOffset());
 assertEquals(12, lineResult1.getEndOffset());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(2, lineResult2.getLineNumber());
 assertEquals(55, lineResult2.getStartOffset());
 assertEquals(60, lineResult2.getEndOffset());
 List<WordResult> wordResults1 = lineResult1.getWordResults();
 assertEquals(1, wordResults1.size());
 WordResult wordResult1 = wordResults1.get(0);
 assertEquals(6, wordResult1.getStartOffset());
 assertEquals(11, wordResult1.getEndOffset());
 List<WordResult> wordResults2 = lineResult2.getWordResults();
 assertEquals(1, wordResults2.size());
 WordResult wordResult2 = wordResults2.get(0);
 assertEquals(57, wordResult2.getStartOffset());
 assertEquals(60, wordResult2.getEndOffset());
 }
}