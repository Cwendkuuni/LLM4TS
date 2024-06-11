package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private String term;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 term = "test";
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 // Test if getScrollableTracksViewportWidth method returns true if the component fits in the viewport
 JFrame frame = new JFrame();
 frame.setSize(300, 300);
 frame.add(searchableTextPane);
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightNullTerm() {
 List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlightEmptyTerm() {
 List<LineResult> lineResults = searchableTextPane.highlight("", 0);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlightTermInEmptyTextPane() {
 searchableTextPane.setText("");
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlightTermInTextPane() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog test.");
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(19, lineResult.getStartOffsetInLine());
 assertEquals(28, lineResult.getEndOffsetInLine());
 }

 @Test
 public void testHighlightTermInMultipleWords() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog the quick brown fox.");
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertEquals(2, lineResults.size());
 LineResult firstLineResult = lineResults.get(0);
 LineResult secondLineResult = lineResults.get(1);
 assertEquals(0, firstLineResult.getLineNumber());
 assertEquals(11, firstLineResult.getStartOffsetInLine());
 assertEquals(20, firstLineResult.getEndOffsetInLine());
 assertEquals(1, secondLineResult.getLineNumber());
 assertEquals(45, secondLineResult.getStartOffsetInLine());
 assertEquals(52, secondLineResult.getEndOffsetInLine());
 }

 @Test
 public void testHighlightTermInMultipleLines() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog\nThe quick brown fox jumps over the lazy dog test.");
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertEquals(2, lineResults.size());
 LineResult firstLineResult = lineResults.get(0);
 LineResult secondLineResult = lineResults.get(1);
 assertEquals(0, firstLineResult.getLineNumber());
 assertEquals(11, firstLineResult.getStartOffsetInLine());
 assertEquals(20, firstLineResult.getEndOffsetInLine());
 assertEquals(1, secondLineResult.getLineNumber());
 assertEquals(45, secondLineResult.getStartOffsetInLine());
 assertEquals(52, secondLineResult.getEndOffsetInLine());
 }

 @Test
 public void testSearchNullTerm() {
 int pos = searchableTextPane.search(null);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchEmptyTerm() {
 int pos = searchableTextPane.search("");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchTermInEmptyTextPane() {
 searchableTextPane.setText("");
 int pos = searchableTextPane.search(term);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchTermInTextPane() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog test.");
 int pos = searchableTextPane.search(term);
 assertEquals(19, pos);
 }

 @Test
 public void testSearchTermInTextPaneWithPreviousSearch() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog test.");
 int pos = searchableTextPane.search(term);
 assertEquals(19, pos);
 int prevPos = searchableTextPane.search(term, pos);
 assertEquals(-1, prevPos);
 searchableTextPane.setText("The quick brown fox jumps over the quick brown fox test.");
 pos = searchableTextPane.search(term);
 assertEquals(45, pos);
 }

 @Test
 public void testSearchTermInTextPaneWithMultipleMatches() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog test The quick brown fox jumps over the quick brown fox test.");
 int pos = searchableTextPane.search(term);
 assertEquals(19, pos);
 int prevPos = searchableTextPane.search(term, pos);
 assertEquals(45, prevPos);
 }

 @Test
 public void testRemoveHighlights() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog test.");
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 for (LineResult lineResult : lineResults) {
 for (WordResult wordResult : lineResult.getWordResults()) {
 try {
 searchableTextPane.addHighlight(wordResult.getStart(), wordResult.getLength());
 } catch (BadLocationException e) {
 fail("Failed to add highlight: " + e.getMessage());
 }
 }
 }
 List<Highlight> highlightsBefore = Arrays.asList(searchableTextPane.getHighlighters().getHighlights());
 searchableTextPane.removeHighlights();
 List<Highlight> highlightsAfter = Arrays.asList(searchableTextPane.getHighlighters().getHighlights());
 assertTrue(highlightsBefore.size() > highlightsAfter.size());
 assertTrue(Collections.isEmpty(highlightsAfter));
 }
}