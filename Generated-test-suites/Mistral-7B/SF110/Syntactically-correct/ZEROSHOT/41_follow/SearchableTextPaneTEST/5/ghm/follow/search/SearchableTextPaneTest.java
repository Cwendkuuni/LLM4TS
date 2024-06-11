package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Line;
import javax.swing.text.Line.PropertyChangeEvent;
import javax.swing.text.LineNumberModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private JFrame frame;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 searchableTextPane.setLineWrap(true);
 searchableTextPane.setWrapStyleWord(true);
 searchableTextPane.setText("This is a test for the SearchableTextPane class. This is a test for the SearchableTextPane class.");

 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.add(new JScrollPane(searchableTextPane));
 frame.pack();
 frame.setSize(600, 400);
 frame.setVisible(true);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyString() throws BadLocationException {
 List<LineResult> lineResults = searchableTextPane.highlight("", 0);
 assertNotNull(lineResults);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlightNullTerm() throws BadLocationException {
 List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
 assertNull(lineResults);
 }

 @Test
 public void testHighlightSimpleTerm() throws BadLocationException {
 List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getLineStartOffset());
 assertEquals(13, lineResult.getLineEndOffset());
 }

 @Test
 public void testHighlightTermInMiddleOfLine() throws BadLocationException {
 List<LineResult> lineResults = searchableTextPane.highlight("text", 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 SimpleAttributeSet attributeSet = lineResult.getWordResults().get(0).getAttributeSet();
 assertEquals(Color.YELLOW, attributeSet.getAttribute(StyleConstants.Background));
 }

 @Test
 public void testHighlightTermAtEndOfLine() throws BadLocationException {
 List<LineResult> lineResults = searchableTextPane.highlight("class", 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 SimpleAttributeSet attributeSet = lineResult.getWordResults().get(0).getAttributeSet();
 assertEquals(Color.YELLOW, attributeSet.getAttribute(StyleConstants.Background));
 }

 @Test
 public void testHighlightTermAtBeginningOfLine() throws BadLocationException {
 List<LineResult> lineResults = searchableTextPane.highlight("This", 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 SimpleAttributeSet attributeSet = lineResult.getWordResults().get(0).getAttributeSet();
 assertEquals(Color.YELLOW, attributeSet.getAttribute(StyleConstants.Background));
 }

 @Test
 public void testHighlightTermInMiddleOfMultipleOccurrences() throws BadLocationException {
 List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
 LineResult lineResult = lineResults.get(0);
 assertEquals(2, lineResult.getWordResults().size());
 }

 @Test
 public void testSearchEmptyString() {
 int position = searchableTextPane.search("");
 assertNull(position);
 }

 @Test
 public void testSearchNullTerm() {
 int position = searchableTextPane.search(null);
 assertNull(position);
 }

 @Test
 public void testSearchSimpleTerm() {
 int position = searchableTextPane.search("test");
 assertEquals(2, position);
 }

 @Test
 public void testSearchTermInMiddleOfLine() throws BadLocationException {
 int position = searchableTextPane.search("test");
 int charPosition = position + "test".length();
 int nextPosition = searchableTextPane.search("class", charPosition);
 assertEquals(5, nextPosition);
 }

 @Test
 public void testSearchTermAtBeginningOfLine() throws BadLocationException {
 int position = searchableTextPane.search("This");
 int charPosition = position + "This".length();
 int nextPosition = searchableTextPane.search("is", charPosition);
 assertEquals(6, nextPosition);
 }

 @Test
 public void testSearchTermAtEndOfLine() throws BadLocationException {
 int position = searchableTextPane.search("class");
 int charPosition = position + "class".length();
 int nextPosition = searchableTextPane.search(".", charPosition);
 assertEquals(14, nextPosition);
 }

 @Test
 public void testSearchTermInMiddleOfMultipleOccurrences() throws BadLocationException {
 int position = searchableTextPane.search("test");
 int charPosition = position + "test".length();
 int nextPosition = searchableTextPane.search("test", charPosition);
 assertEquals(10, nextPosition);
 }

 @Test
 public void testRemoveHighlights() throws BadLocationException {
 List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
 searchableTextPane.removeHighlights();
 assertNull(lineResults);
 }
}