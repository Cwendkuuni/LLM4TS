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

 private SearchableTextPane searchableTextPane;
 private List<LineResult> lineResults;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 16), 4);
 }

 @Test
 public void testConstructor() {
 assertNotNull(searchableTextPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyTerm() {
 searchableTextPane.highlight("", 0);
 assertEquals(0, searchableTextPane.getDocument().getLength());
 }

 @Test
 public void testHighlightNullTerm() {
 String term = null;
 searchableTextPane.highlight(term, 0);
 assertNotNull(searchableTextPane.getDocument());
 assertEquals(0, searchableTextPane.getDocument().getLength());
 }

 @Test
 public void testHighlightValidTerm() {
 String term = "HelloWorld";
 searchableTextPane.setText("HelloWorld this is a test.");
 searchableTextPane.highlight(term, 0);
 lineResults = searchableTextPane.highlight(term, 0);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartLineOffset());
 assertEquals(14, lineResult.getEndLineOffset());
 assertNotNull(lineResult.getWordResults());
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(0, wordResult.getStart());
 assertEquals(13, wordResult.getEnd());
 assertEquals("World", wordResult.getWord());
 }

 @Test
 public void testHighlightTermAtEndOfText() {
 String term = "World";
 searchableTextPane.setText("HelloWorld this is a test.");
 searchableTextPane.highlight(term, searchableTextPane.getText().length());
 lineResults = searchableTextPane.highlight(term, searchableTextPane.getText().length());
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(6, lineResult.getLineNumber());
 assertEquals(35, lineResult.getStartLineOffset());
 assertEquals(38, lineResult.getEndLineOffset());
 assertNotNull(lineResult.getWordResults());
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(33, wordResult.getStart());
 assertEquals(37, wordResult.getEnd());
 assertEquals("World", wordResult.getWord());
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightOutOfBounds() {
 searchableTextPane.highlight("Test", -1);
 }

 @Test
 public void testSearchEmptyText() {
 int pos = searchableTextPane.search("");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchNullTerm() {
 String term = null;
 int pos = searchableTextPane.search(term);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchValidTerm() {
 String term = "HelloWorld";
 searchableTextPane.setText("HelloWorld this is a test.");
 int pos = searchableTextPane.search(term);
 assertEquals(0, pos);
 }

 @Test
 public void testSearchTermAtEndOfText() {
 String term = "World";
 searchableTextPane.setText("HelloWorld this is a test.");
 int pos = searchableTextPane.search(term);
 assertEquals(6, pos);
 }

 @Test
 public void testSearchTermNotPresentInText() {
 String term = "Test";
 searchableTextPane.setText("HelloWorld this is a test.");
 int pos = searchableTextPane.search(term);
 assertEquals(-1, pos);
 }

 @Test(expected = BadLocationException.class)
 public void testSearchOutOfBounds() {
 int pos = searchableTextPane.search("Test", -1);
 }

 @Test
 public void testRemoveHighlights() {
 searchableTextPane.setText("HelloWorld this is a test.");
 searchableTextPane.highlight("World", 0);
 lineResults = searchableTextPane.highlight("World", 0);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertNotNull(lineResult.getWordResults());
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(0, wordResult.getStart());
 assertEquals(13, wordResult.getEnd());
 searchableTextPane.removeHighlights();
 assertFalse(lineResult.hasHighlights());
 }
}