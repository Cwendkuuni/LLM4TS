package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private List<LineResult> lineResults;

 @Before
 public void setUp() {
 char[] text = new char[100];
 Arrays.fill(text, ' ');
 String textString = new String(text);
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 searchableTextPane.setText(textString);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 JFrame frame = new JFrame();
 frame.add(searchableTextPane);
 frame.pack();
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlight_EmptyTerm() {
 lineResults = searchableTextPane.highlight(null, 0);
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlight_NullDocument() {
 searchableTextPane.setDocument(null);
 lineResults = searchableTextPane.highlight("test", 0);
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlight_EmptyText() {
 searchableTextPane.setText("");
 lineResults = searchableTextPane.highlight("test", 0);
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlight_ValidTerm() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 lineResults = searchableTextPane.highlight("fox", 0);
 assertNotNull(lineResults);
 assertTrue(lineResults.size() > 0);
 }

 @Test
 public void testAddHighlight() throws BadLocationException {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 int start = searchableTextPane.getDocument().getLength();
 int length = 5;
 searchableTextPane.addHighlight(start, length);
 assertEquals(1, searchableTextPane.getHighlighter().getHighlights().length);
 }

 @Test(expected = BadLocationException.class)
 public void testAddHighlight_BadLocation() throws BadLocationException {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 int start = -1;
 int length = 5;
 searchableTextPane.addHighlight(start, length);
 }

 @Test
 public void testRemoveHighlights() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 int start = 0;
 int length = searchableTextPane.getDocument().getLength();
 searchableTextPane.addHighlight(start, length);
 searchableTextPane.removeHighlights();
 assertEquals(0, searchableTextPane.getHighlighter().getHighlights().length);
 }

 @Test
 public void testSearch_EmptyTerm() {
 assertEquals(-1, searchableTextPane.search(null));
 }

 @Test
 public void testSearch_EmptyText() {
 assertEquals(-1, searchableTextPane.search(""));
 }

 @Test
 public void testSearch_ValidTerm() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 assertEquals(-1, searchableTextPane.search("zebra"));
 assertEquals(18, searchableTextPane.search("dog"));
 }

 @Test
 public void testSearch_ValidTerm_PreviousResult() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 searchableTextPane.search("dog");
 assertEquals(36, searchableTextPane.search("dog"));
 }

 @Test
 public void testConvertWords2Lines_EmptyList() throws BadLocationException {
 List<WordResult> words = Collections.emptyList();
 lineResults = searchableTextPane.convertWords2Lines(words);
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testConvertWords2Lines_ValidList() throws BadLocationException {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 List<WordResult> words = Arrays.asList(new WordResult("The", 0, 3),
 new WordResult("quick", 4, 6),
 new WordResult("brown", 7, 9),
 new WordResult("fox", 10, 13),
 new WordResult("jumps", 14, 19),
 new WordResult("over", 20, 26),
 new WordResult("the", 27, 30),
 new WordResult("lazy", 31, 36),
 new WordResult("dog.", 37, 41));
 lineResults = searchableTextPane.convertWords2Lines(words);
 assertNotNull(lineResults);
 assertEquals(2, lineResults.size());
 LineResult result1 = lineResults.get(0);
 LineResult result2 = lineResults.get(1);
 assertEquals(0, result1.getLineNumber());
 assertEquals(0, result1.getLineStartOffset());
 assertEquals(42, result1.getLineEndOffset());
 assertEquals(1, result2.getLineNumber());
 assertEquals(0, result2.getLineStartOffset());
 assertEquals(42, result2.getLineEndOffset());
 }

 @Test
 public void testUpdateWordResult_ValidParameters() throws BadLocationException {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 WordResult wordResult = new WordResult("jumps", 14, 19);
 LineResult lineResult = new LineResult(1, 0, 42);
 searchableTextPane.updateWordResult(wordResult, lineResult);
 assertEquals(1, wordResult.parent.lineNumber);
 assertEquals(14, wordResult.lineOffset);
 }

 @Test(expected = BadLocationException.class)
 public void testUpdateWordResult_BadParameters() throws BadLocationException {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 WordResult wordResult = new WordResult("jumps", 14, 19);
 LineResult lineResult = null;
 searchableTextPane.updateWordResult(wordResult, lineResult);
 }
}