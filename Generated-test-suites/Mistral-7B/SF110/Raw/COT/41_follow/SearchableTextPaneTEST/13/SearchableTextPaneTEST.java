package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.LineElement;
import javax.swing.text.LineIterator;
import javax.swing.text.Position.Bias;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private Highlighter highlighter;
 private Document document;

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 12), 4);
 document = textPane.getDocument();
 highlighter = textPane.getHighlighter();
 }

 @Test
 public void testConstructor() {
 assertTrue(textPane instanceof JTextArea);
 assertEquals(12, textPane.getFont().getSize());
 assertEquals(4, textPane.getTabSize());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlight_EmptySearchTerm() {
 List<LineResult> lineResults = textPane.highlight(null, 0);
 assertNotNull(lineResults);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlight_NullDocument() {
 textPane.setDocument(null);
 List<LineResult> lineResults = textPane.highlight("test", 0);
 assertNotNull(lineResults);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlight_EmptyText() {
 textPane.setText("");
 List<LineResult> lineResults = textPane.highlight("test", 0);
 assertNotNull(lineResults);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlight_ValidSearchTerm() throws BadLocationException {
 textPane.setText("This is a test for the SearchableTextPane.");
 List<WordResult> searchResults = Arrays.asList(
 new WordResult(0, 5, "is"),
 new WordResult(5, 8, "a"),
 new WordResult(8, 11, "test"),
 new WordResult(12, 16, "for"),
 new WordResult(16, 19, "the"),
 new WordResult(19, 24, "SearchableTextPane.")
 );
 List<LineResult> lineResults = textPane.highlight("test", 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(16, lineResult.getEndOffset());
 LineIterator lineIterator = textPane.getLineIterator();
 lineIterator.setEnd(textPane.getDocument().getLength());
 int lineNumber = 0;
 while (lineIterator.nextLineStart()) {
 if (lineNumber == lineResult.getLineNumber()) {
 int start = lineIterator.getEndPosition().getOffset();
 int end = lineIterator.getLineEndPosition(Bias.Forward).getOffset();
 assertEquals(start, lineResult.getStartOffset());
 assertEquals(end, lineResult.getEndOffset());
 break;
 }
 lineNumber++;
 }
 for (WordResult wordResult : searchResults) {
 HighlightPainter painter = new DefaultHighlightPainter(Color.RED);
 highlighter.addHighlight(wordResult.getStart(), wordResult.getLength(), painter);
 }
 textPane.repaint();
 }

 @Test(expected = BadLocationException.class)
 public void testHighlight_InvalidDocumentPosition() throws BadLocationException {
 List<LineResult> lineResults = textPane.highlight("test", -1);
 fail("Should have thrown BadLocationException");
 }

 @Test
 public void testRemoveHighlights() {
 textPane.setText("This is a test for the SearchableTextPane.");
 List<WordResult> searchResults = Arrays.asList(
 new WordResult(0, 5, "is"),
 new WordResult(5, 8, "a"),
 new WordResult(8, 11, "test"),
 new WordResult(12, 16, "for"),
 new WordResult(16, 19, "the"),
 new WordResult(19, 24, "SearchableTextPane.")
 );
 List<LineResult> lineResults = textPane.highlight("test", 0);
 assertEquals(1, lineResults.size());
 for (WordResult wordResult : searchResults) {
 HighlightPainter painter = new DefaultHighlightPainter(Color.YELLOW);
 highlighter.addHighlight(wordResult.getStart(), wordResult.getLength(), painter);
 }
 textPane.removeHighlights();
 List<LineResult> lineResultsAfterRemoval = textPane.highlight("test", 0);
 assertTrue(lineResultsAfterRemoval.isEmpty());
 }

 @Test
 public void testSearch_EmptySearchTerm() {
 assertEquals(-1, textPane.search(null));
 }

 @Test
 public void testSearch_EmptyText() {
 assertEquals(-1, textPane.search(""));
 }

 @Test
 public void testSearch_ValidSearchTerm() {
 textPane.setText("This is a test for the SearchableTextPane.");
 assertEquals(5, textPane.search("is"));
 assertEquals(8, textPane.search("a"));
 assertEquals(12, textPane.search("test"));
 assertEquals(16, textPane.search("for"));
 assertEquals(19, textPane.search("the"));
 assertEquals(24, textPane.search("SearchableTextPane."));
 }

 @Test
 public void testSearch_InvalidSearchTerm() {
 assertEquals(-1, textPane.search("zebra"));
 }

 @Test
 public void testSearch_InvalidPosition() {
 assertEquals(-1, textPane.search("test", 100));
 }

 @After
 public void tearDown() {
 textPane = null;
 document = null;
 highlighter = null;
 }

}