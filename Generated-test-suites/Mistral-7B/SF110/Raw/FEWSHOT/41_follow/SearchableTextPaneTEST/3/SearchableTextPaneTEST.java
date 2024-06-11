package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.LineResult;
import javax.swing.text.SearchEngine;
import javax.swing.text.WordResult;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private static final String TEST_TERM = "TEST";

 @Before
 public void setup() {
 textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 JTextArea parent = new JTextArea();
 parent.add(textPane);
 assertTrue(textPane.getScrollableTracksViewportWidth());
 parent = null;
 assertTrue(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyString() {
 List<LineResult> lineResults = textPane.highlight("", 0);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlightNullTerm() {
 List<LineResult> lineResults = textPane.highlight(null, 0);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlightSingleWord() {
 textPane.setText("The quick brown fox jumps over the lazy dog.");
 List<LineResult> lineResults = textPane.highlight(TEST_TERM, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 List<WordResult> wordResults = lineResult.getWordResults();
 assertEquals(1, wordResults.size());
 WordResult wordResult = wordResults.get(0);
 assertEquals(TEST_TERM, wordResult.getWord().trim());
 assertEquals(11, wordResult.getStart());
 assertEquals(14, wordResult.getEnd());
 assertNotNull(lineResult.getHighlightPainter());
 }

 @Test
 public void testHighlightMultipleWords() {
 textPane.setText("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog.");
 List<LineResult> lineResults = textPane.highlight(TEST_TERM, 0);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(0, lineResult1.getLineNumber());
 List<WordResult> wordResults1 = lineResult1.getWordResults();
 assertEquals(1, wordResults1.size());
 WordResult wordResult1 = wordResults1.get(0);
 assertEquals(TEST_TERM, wordResult1.getWord().trim());
 assertEquals(11, wordResult1.getStart());
 assertEquals(14, wordResult1.getEnd());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(1, lineResult2.getLineNumber());
 List<WordResult> wordResults2 = lineResult2.getWordResults();
 assertEquals(1, wordResults2.size());
 WordResult wordResult2 = wordResults2.get(0);
 assertEquals(TEST_TERM, wordResult2.getWord().trim());
 assertEquals(27, wordResult2.getStart());
 assertEquals(30, wordResult2.getEnd());
 }

 @Test
 public void testHighlightMultipleLines() {
 textPane.setText("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog Another quick brown fox jumps over the lazy dog.");
 List<LineResult> lineResults = textPane.highlight(TEST_TERM, 0);
 assertEquals(3, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(0, lineResult1.getLineNumber());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(1, lineResult2.getLineNumber());
 LineResult lineResult3 = lineResults.get(2);
 assertEquals(2, lineResult3.getLineNumber());
 }

 @Test
 public void testSearchEmptyString() {
 assertEquals(-1, textPane.search(""));
 }

 @Test
 public void testSearchNullTerm() {
 assertEquals(-1, textPane.search(null));
 }

 @Test
 public void testSearchSingleWord() {
 textPane.setText("The quick brown fox jumps over the lazy dog.");
 assertEquals(11, textPane.search(TEST_TERM));
 }

 @Test
 public void testSearchMultipleWords() {
 textPane.setText("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog.");
 assertEquals(11, textPane.search(TEST_TERM));
 assertEquals(27, textPane.search(TEST_TERM));
 }

 @Test
 public void testSearchMultipleLines() {
 textPane.setText("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog Another quick brown fox jumps over the lazy dog.");
 assertEquals(11, textPane.search(TEST_TERM));
 assertEquals(27, textPane.search(TEST_TERM));
 assertEquals(43, textPane.search(TEST_TERM));
 }

 @Test
 public void testSearchPreviousTerm() {
 textPane.setText("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog Another quick brown fox jumps over the lazy dog.");
 textPane.search("TEST");
 textPane.search("TEST");
 assertEquals(27, textPane.search("TEST"));
 }

 @Test
 public void testSearchPreviousTermNull() {
 textPane.setText("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog Another quick brown fox jumps over the lazy dog.");
 textPane.search("TEST");
 textPane.search("TEST");
 textPane.search(null);
 assertEquals(-1, textPane.search(null));
 }

 @Test
 public void testRemoveHighlights() {
 textPane.setText("The quick brown fox jumps over the lazy dog.");
 List<LineResult> lineResults = textPane.highlight("TEST", 0);
 assertEquals(1, lineResults.size());
 textPane.removeHighlights();
 List<LineResult> lineResults2 = textPane.highlight("TEST", 0);
 assertTrue(lineResults2.isEmpty());
 }
}