package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private static final String SEARCH_TERM = "JUnit";

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 4);
 }

 @Test
 public void testSearchableTextPaneConstruction() {
 assertNotNull(searchableTextPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyString() {
 List<LineResult> lineResults = searchableTextPane.highlight("", 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightNullString() {
 List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightValidTerm() throws Exception {
 String text = "This is a JUnit test for the SearchableTextPane class.";
 searchableTextPane.setText(text);
 searchableTextPane.setCaretPosition(0);
 List<LineResult> lineResults = searchableTextPane.highlight(SEARCH_TERM, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(text.indexOf(SEARCH_TERM), lineResult.getStartOffset());
 assertEquals(text.length(), lineResult.getEndOffset());
 assertNotNull(lineResult.getWordResults());
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(SEARCH_TERM, wordResult.getWord());
 assertEquals(0, wordResult.getStart());
 assertEquals(SEARCH_TERM.length(), wordResult.getEnd() - wordResult.getStart());
 assertEquals(SEARCH_TERM.length(), wordResult.getLength());
 }

 @Test
 public void testHighlightValidTermAtEnd() throws Exception {
 String text = "This is a test for the SearchableTextPane class.";
 searchableTextPane.setText(text);
 searchableTextPane.setCaretPosition(text.length());
 List<LineResult> lineResults = searchableTextPane.highlight(SEARCH_TERM, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(searchableTextPane.getLineCount() - 1, lineResult.getLineNumber());
 assertEquals(text.length(), lineResult.getStartOffset());
 assertEquals(text.length() + SEARCH_TERM.length(), lineResult.getEndOffset());
 assertNotNull(lineResult.getWordResults());
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(SEARCH_TERM, wordResult.getWord());
 assertEquals(text.length(), wordResult.getStart());
 assertEquals(text.length() + SEARCH_TERM.length(), wordResult.getEnd());
 }

 @Test
 public void testHighlightValidTermInMiddle() throws Exception {
 String text = "This is a JUnit test for the SearchableTextPane class.";
 searchableTextPane.setText(text);
 searchableTextPane.setCaretPosition(text.indexOf("JUnit") + SEARCH_TERM.length());
 List<LineResult> lineResults = searchableTextPane.highlight(SEARCH_TERM, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(searchableTextPane.getLineOfOffset(text.indexOf("JUnit")), lineResult.getLineNumber());
 assertEquals(text.indexOf("JUnit"), lineResult.getStartOffset());
 assertEquals(text.indexOf("JUnit") + SEARCH_TERM.length(), lineResult.getEndOffset());
 assertNotNull(lineResult.getWordResults());
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(SEARCH_TERM, wordResult.getWord());
 assertEquals(text.indexOf("JUnit"), wordResult.getStart());
 assertEquals(text.indexOf("JUnit") + SEARCH_TERM.length(), wordResult.getEnd());
 }

 @Test
 public void testHighlightMultipleTerms() throws Exception {
 String text = "This is a JUnit test for the SearchableTextPane class with JUnit and JTest.";
 searchableTextPane.setText(text);
 searchableTextPane.setCaretPosition(0);
 List<LineResult> lineResults = searchableTextPane.highlight(SEARCH_TERM, 0);
 assertEquals(2, lineResults.size());
 List<LineResult> expectedLineResults = new ArrayList<>();
 LineResult expectedLineResult1 = new LineResult(0, 0, text.length());
 LineResult expectedLineResult2 = new LineResult(1, text.indexOf("with") + 1, text.length());
 expectedLineResult1.addWord(new WordResult("JUnit", 0, SEARCH_TERM.length()));
 expectedLineResult2.addWord(new WordResult("JUnit", text.indexOf("JUnit"), SEARCH_TERM.length()));
 assertEquals(expectedLineResults, lineResults);
 }

 @Test
 public void testSearchEmptyString() {
 int position = searchableTextPane.search("");
 assertEquals(-1, position);
 }

 @Test
 public void testSearchNullString() {
 int position = searchableTextPane.search(null);
 assertEquals(-1, position);
 }

 @Test
 public void testSearchValidTerm() {
 String text = "This is a JUnit test for the SearchableTextPane class.";
 searchableTextPane.setText(text);
 int position = searchableTextPane.search(SEARCH_TERM);
 assertEquals(0, position);
 }

 @Test
 public void testSearchValidTermAtEnd() {
 String text = "This is a test for the SearchableTextPane class.";
 searchableTextPane.setText(text);
 int position = searchableTextPane.search(SEARCH_TERM);
 assertEquals(-1, position);
 }

 @Test
 public void testSearchValidTermInMiddle() throws Exception {
 String text = "This is a JUnit test for the SearchableTextPane class.";
 searchableTextPane.setText(text);
 searchableTextPane.setCaretPosition(text.indexOf("JUnit") + SEARCH_TERM.length());
 int position = searchableTextPane.search(SEARCH_TERM);
 assertEquals(searchableTextPane.getLineOfOffset(text.indexOf("JUnit")), position);
 }

 @Test
 public void testSearchMultipleTerms() throws Exception {
 String text = "This is a JUnit test for the SearchableTextPane class with JUnit and JTest.";
 searchableTextPane.setText(text);
 int position = searchableTextPane.search("JUnit JTest");
 assertEquals(1, position);
 int position2 = searchableTextPane.search("JUnit");
 assertEquals(0, position2);
 }

 @Test
 public void testRemoveHighlights() throws Exception {
 String text = "This is a JUnit test for the SearchableTextPane class.";
 searchableTextPane.setText(text);
 searchableTextPane.setCaretPosition(0);
 List<LineResult> lineResults = searchableTextPane.highlight(SEARCH_TERM, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 searchableTextPane.removeHighlights();
 assertEquals(0, lineResult.getWordResults().size());
 }
}