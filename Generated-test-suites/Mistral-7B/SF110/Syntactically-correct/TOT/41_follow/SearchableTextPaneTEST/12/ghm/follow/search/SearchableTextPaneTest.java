package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private List<LineResult> lineResults;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 14), 8);
 searchableTextPane.setPreferredSize(new Dimension(400, 300));
 }

 @Test
 public void testHighlightEmptyTerm() {
 searchableTextPane.setText("This is a test.");
 searchableTextPane.highlight(null, 0);
 assertTrue(searchableTextPane.getHighlighter().getHighlights().isEmpty());
 }

 @Test
 public void testHighlightSingleWord() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 final String term = "fox";
 final List<LineResult> expectedLineResults = new ArrayList<LineResult>();
 expectedLineResults.add(new LineResult(1, 40, 65));
 lineResults = searchableTextPane.highlight(term, 0);
 assertEquals(expectedLineResults, lineResults);
 }

 @Test
 public void testHighlightMultipleWordsInSameLine() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog jumps again.");
 final String term = "jumps";
 final List<LineResult> expectedLineResults = new ArrayList<LineResult>();
 expectedLineResults.add(new LineResult(1, 51, 65));
 expectedLineResults.add(new LineResult(1, 103, 113));
 lineResults = searchableTextPane.highlight(term, 0);
 assertEquals(expectedLineResults, lineResults);
 }

 @Test
 public void testHighlightMultipleWordsInMultipleLines() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog jumps again.\nThe quick red fox jumps over the slow cat.");
 final String term = "jumps";
 final List<LineResult> expectedLineResults = new ArrayList<LineResult>();
 expectedLineResults.add(new LineResult(1, 51, 65));
 expectedLineResults.add(new LineResult(2, 5, 20));
 lineResults = searchableTextPane.highlight(term, 0);
 assertEquals(expectedLineResults, lineResults);
 }

 @Test
 public void testHighlightEmptyTextArea() {
 searchableTextPane.setText("");
 final String term = "test";
 lineResults = searchableTextPane.highlight(term, 0);
 assertTrue(lineResults.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHighlightNullTextArea() {
 final String term = "test";
 searchableTextPane = null;
 lineResults = searchableTextPane.highlight(term, 0);
 }

 @Test
 public void testSearchEmptyTerm() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 final int expectedPos = -1;
 final int actualPos = searchableTextPane.search("");
 assertEquals(expectedPos, actualPos);
 }

 @Test
 public void testSearchSingleWord() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 final String term = "fox";
 final int expectedPos = 40;
 final int actualPos = searchableTextPane.search(term);
 assertEquals(expectedPos, actualPos);
 }

 @Test
 public void testSearchMultipleOccurrencesInSameLine() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog jumps again.");
 final String term = "jumps";
 final int expectedPos1 = 51;
 final int expectedPos2 = 103;
 final int[] expectedPositions = { expectedPos1, expectedPos2 };
 final int[] actualPositions = new int[expectedPositions.length];
 for (int i = 0; i < expectedPositions.length; i++) {
 actualPositions[i] = searchableTextPane.search(term);
 }
 assertArrayEquals(expectedPositions, actualPositions);
 }

 @Test
 public void testSearchMultipleOccurrencesInMultipleLines() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog jumps again.\nThe quick red fox jumps over the slow cat.");
 final String term = "jumps";
 final int[] expectedPositions = { 51, 5 };
 final int[] actualPositions = new int[expectedPositions.length];
 for (int i = 0; i < expectedPositions.length; i++) {
 actualPositions[i] = searchableTextPane.search(term);
 }
 assertArrayEquals(expectedPositions, actualPositions);
 }

 @Test
 public void testSearchEmptyTextArea() {
 searchableTextPane.setText("");
 final String term = "test";
 final int expectedPos = -1;
 final int actualPos = searchableTextPane.search(term);
 assertEquals(expectedPos, actualPos);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchNullTextArea() {
 final String term = "test";
 searchableTextPane = null;
 final int actualPos = searchableTextPane.search(term);
 }

 @Test
 public void testSearchCaseInsensitive() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 final String term = "jUmPs";
 final int expectedPos = 51;
 final int actualPos = searchableTextPane.search(term);
 assertEquals(expectedPos, actualPos);
 }

 @Test
 public void testHighlightWithError() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 try {
 searchableTextPane.highlight("test", 100);
 SwingUtilities.invokeAndWait(() -> {
fail("Highlighting should not be successful with invalid offset.");
 });
 } catch (Exception e) {
 // Expected
 }
 }

 @After
 public void tearDown() {
 if (searchableTextPane != null) {
 SwingUtilities.invokeLater(() -> searchableTextPane.setText(""));
 }
 }

}