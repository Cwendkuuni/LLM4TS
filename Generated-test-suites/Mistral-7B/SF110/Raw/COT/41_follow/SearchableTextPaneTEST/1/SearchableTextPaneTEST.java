package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Monospaced", Font.BOLD, 12), 8);
 }

 @After
 public void tearDown() {
 textPane = null;
 }

 @Test
 public void testConstructor() {
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Monospaced", Font.BOLD, 12), 8);
 assertNotNull(searchableTextPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 JFrame frame = new JFrame();
 frame.setSize(500, 500);
 frame.add(textPane);
 assertTrue(textPane.getScrollableTracksViewportWidth());
 textPane.setSize(new Dimension(300, 300));
 assertFalse(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyTerm() {
 List<LineResult> lineResults = textPane.highlight(null, 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightSingleWord() {
 textPane.setText("The quick brown fox jumps over the lazy dog.");
 List<LineResult> lineResults = textPane.highlight("fox", 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(11, wordResult.getStart());
 assertEquals(13, wordResult.getEnd());
 }

 @Test
 public void testHighlightMultipleWords() {
 textPane.setText("The quick brown fox jumps over the lazy dog.");
 List<LineResult> lineResults = textPane.highlight("the quick brown fox jumps over the lazy dog.", 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(10, lineResult.getWordResults().size());
 List<WordResult> wordResults = lineResult.getWordResults();
 assertEquals(5, wordResults.get(0).getStart());
 assertEquals(10, wordResults.get(0).getEnd());
 assertEquals("the", wordResults.get(0).getWord());
 assertEquals(6, wordResults.get(1).getStart());
 assertEquals(11, wordResults.get(1).getEnd());
 assertEquals("quick", wordResults.get(1).getWord());
 // ... and so on
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightWithException() {
 textPane.setText("The quick brown fox jumps over the lazy dog.");
 List<LineResult> lineResults = textPane.highlight("fox", 100);
 fail("Expected BadLocationException but got " + lineResults);
 }

 @Test
 public void testSearchEmptyTerm() {
 textPane.setText("The quick brown fox jumps over the lazy dog.");
 int pos = textPane.search(null);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchSingleWord() {
 textPane.setText("The quick brown fox jumps over the lazy dog.");
 int pos = textPane.search("fox");
 assertEquals(11, pos);
 }

 @Test
 public void testSearchMultipleWords() {
 textPane.setText("The quick brown fox jumps over the lazy dog.");
 int pos = textPane.search("the quick brown fox jumps over the lazy dog.");
 assertEquals(0, pos);
 }

 @Test
 public void testSearchWithException() {
 textPane.setText("The quick brown fox jumps over the lazy dog.");
 int pos = textPane.search("zebra");
 assertEquals(-1, pos);
 }
}