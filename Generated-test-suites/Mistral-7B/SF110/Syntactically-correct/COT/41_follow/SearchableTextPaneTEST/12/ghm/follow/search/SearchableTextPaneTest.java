package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private JTextField textField;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 8);
 textField = new JTextField(50);
 }

 @Test
 public void testSearchableTextPaneConstructor() {
 // check if logger is initialized
 SearchableTextPane.class.getLogger().info("Logger initialized.");
 // check if font is set correctly
 assertEquals("Arial", searchableTextPane.getFont().getName());
 // check if tabSize is set correctly
 assertEquals(8, searchableTextPane.getTabSize());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 // test when parent component is null
 assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
 // test when parent component width is less than preferred width
 JTextArea parentTextArea = new JTextArea();
 parentTextArea.setSize(100, 100);
 parentTextArea.add(searchableTextPane);
 assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
 // test when parent component width is greater than preferred width
 JTextArea parentTextArea2 = new JTextArea();
 parentTextArea2.setSize(200, 200);
 parentTextArea2.add(searchableTextPane);
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyString() {
 List<LineResult> lineResults = searchableTextPane.highlight("", 0);
 assertNull(lineResults);
 }

 @Test
 public void testHighlightNullTerm() {
 List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
 assertNull(lineResults);
 }

 @Test
 public void testHighlightTermWithFlags() {
 String text = "This is a test for SearchableTextPane's highlight method.";
 searchableTextPane.setText(text);
 List<WordResult> searchResults = Arrays.asList(
 new WordResult(0, 3, "SearchableTextPane"),
 new WordResult(9, 4, "highlight"),
 new WordResult(21, 3, "method.")
 );
 List<LineResult> lineResults = searchableTextPane.highlight("SearchableTextPane", SearchableTextPane.HIGHLIGHT_FOUND);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartLineOffset());
 assertEquals(31, lineResult.getEndLineOffset());
 List<WordResult> wordResults = lineResult.getWordResults();
 assertEquals(3, wordResults.size());
 WordResult firstWord = wordResults.get(0);
 assertEquals(0, firstWord.getStart());
 assertEquals(3, firstWord.getEnd());
 assertEquals("SearchableTextPane", firstWord.getText());
 WordResult secondWord = wordResults.get(1);
 assertEquals(15, secondWord.getStart());
 assertEquals(19, secondWord.getEnd());
 assertEquals("highlight", secondWord.getText());
 WordResult thirdWord = wordResults.get(2);
 assertEquals(22, thirdWord.getStart());
 assertEquals(25, thirdWord.getEnd());
 assertEquals("method.", thirdWord.getText());
 SimpleAttributeSet attributeSet = new SimpleAttributeSet();
 StyleConstants.setBackground(attributeSet, Color.YELLOW);
 for (int i = 0; i < wordResults.size(); i++) {
 Highlighter.Highlight highligh = searchableTextPane.getHighlighter().getHighlights()[0];
 assertEquals(highligh.getStartOffset(), wordResults.get(i).getStart());
 assertEquals(highligh.getEndOffset(), wordResults.get(i).getEnd());
 searchableTextPane.getHighlighter().removeAllHighlights();
 }
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightWithBadLocationException() {
 searchableTextPane.highlight("test", 5);
 }

 @Test
 public void testAddHighlight() throws BadLocationException {
 searchableTextPane.setText("This is a test for SearchableTextPane's addHighlight method.");
 int start = 17;
 int length = 6;
 searchableTextPane.addHighlight(start, length);
 Highlighter.Highlight highlight = searchableTextPane.getHighlighter().getHighlights()[0];
 assertEquals(start, highlight.getStartOffset());
 assertEquals(length, highlight.getEndOffset() - start);
 }

 @Test
 public void testRemoveHighlights() {
 searchableTextPane.setText("This is a test for SearchableTextPane's removeHighlights method.");
 searchableTextPane.highlight("test", 0);
 List<Highlighter.Highlight> highlightsBefore = Arrays.asList(searchableTextPane.getHighlighter().getHighlights());
 searchableTextPane.removeHighlights();
 List<Highlighter.Highlight> highlightsAfter = Arrays.asList(searchableTextPane.getHighlighter().getHighlights());
 assertTrue(highlightsBefore.size() > highlightsAfter.size());
 }

 @Test
 public void testSearchEmptyString() {
 assertEquals(-1, searchableTextPane.search(""));
 }

 @Test
 public void testSearchNullTerm() {
 assertNull(searchableTextPane.search(null));
 }

 @Test
 public void testSearchTermFound() {
 String text = "This is a test for SearchableTextPane's search method.";
 searchableTextPane.setText(text);
 assertEquals(11, searchableTextPane.search("test"));
 }

 @Test
 public void testSearchTermNotFound() {
 String text = "This is a test for SearchableTextPane's search method.";
 searchableTextPane.setText(text);
 assertEquals(-1, searchableTextPane.search("tester"));
 }

 @Test
 public void testSearchTermFoundAtBeginning() {
 String text = "This is a test for SearchableTextPane's search method.";
 searchableTextPane.setText(text);
 assertEquals(0, searchableTextPane.search("test"));
 }

 @Test
 public void testSearchTermFoundAtEnd() {
 String text = "This is a test for SearchableTextPane's search method.";
 searchableTextPane.setText(text);
 assertEquals(text.length(), searchableTextPane.search("method."));
 }

 @Test
 public void testSearchTermFoundInMiddle() {
 String text = "This is a test for SearchableTextPane's search method.";
 searchableTextPane.setText(text);
 assertEquals(16, searchableTextPane.search("SearchableTextPane"));
 }

 @Test
 public void testSearchTermCaseSensitive() {
 String text = "This is a test for SearchableTextPane's search method.";
 searchableTextPane.setText(text);
 assertEquals(-1, searchableTextPane.search("Test"));
 }

 @Test
 public void testSearchTermMultipleOccurrences() {
 String text = "This is a test for SearchableTextPane's search method test test test.";
 searchableTextPane.setText(text);
 assertEquals(28, searchableTextPane.search("test"));
 }

 @Test
 public void testSearchTextAreaContent() {
 String text = "This is a test for SearchableTextPane's search method.";
 searchableTextPane.setText(text);
 assertEquals(0, searchableTextPane.search("This"));
 assertEquals(21, searchableTextPane.search("method."));
 }

 @Test
 public void testSearchTextAreaContentWithHighlights() {
 String text = "This is a test for SearchableTextPane's search method.";
 searchableTextPane.setText(text);
 List<WordResult> searchResults = Arrays.asList(
 new WordResult(0, 3, "SearchableTextPane"),
 new WordResult(9, 4, "highlight"),
 new WordResult(21, 3, "method.")
 );
 List<LineResult> lineResults = searchableTextPane.highlight("SearchableTextPane", SearchableTextPane.HIGHLIGHT_FOUND);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 final int lineNumber = lineResult.getLineNumber();
 final int startLineOffset = lineResult.getStartLineOffset();
 final int endLineOffset = lineResult.getEndLineOffset();
 assertEquals(lineNumber, searchableTextPane.getLineOfOffset(startLineOffset) / searchableTextPane.getLineHeight());
 assertEquals(startLineOffset, searchableTextPane.getDocument().getCharacterElement(lineNumber).getStartOffset());
 assertEquals(endLineOffset, searchableTextPane.getDocument().getCharacterElement(lineNumber + 1).getStartOffset() - 1);
 int searchPosition = searchableTextPane.search("SearchableTextPane");
 assertEquals(searchPosition, startLineOffset + searchableTextPane.getLineStartOffset(lineNumber));
 }

 @Test
 public void testSearchTextAreaContentWithNullDocument() {
 searchableTextPane.setText(null);
 assertNull(searchableTextPane.search("test"));
 }

 @Test(expected = BadLocationException.class)
 public void testSearchTextAreaContentWithBadLocationException() {
 searchableTextPane.search("test", 5);
 }
}