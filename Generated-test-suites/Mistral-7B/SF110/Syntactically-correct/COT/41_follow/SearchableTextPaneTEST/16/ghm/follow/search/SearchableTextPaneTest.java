package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.LineElement;
import javax.swing.text.LineView;
import javax.swing.text.Position.Bias;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private Document mockDocument;
 private DefaultHighlighter mockHighlighter;
 private Logger mockLogger;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 mockDocument = mock(Document.class);
 mockHighlighter = mock(DefaultHighlighter.class);
 mockLogger = mock(Logger.class);
 textPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 14), 8) {
 @Override
 public Document getDocument() {
 return mockDocument;
 }

 @Override
 public DefaultHighlighter getHighlighter() {
 return mockHighlighter;
 }

 @Override
 public Logger getLogger() {
 return mockLogger;
 }
 };
 }

 @Test
 public void testSearchableTextPaneConstructor() {
 // Test the constructor with valid arguments
 SearchableTextPane textPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 14), 8);
 assertTrue(textPane instanceof JTextArea);
 assertEquals(14, textPane.getFont().getSize2D().getHeight());
 assertEquals(8, textPane.getTabSize());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 // Test the getScrollableTracksViewportWidth method
 assertFalse(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlight_ValidArguments() throws BadLocationException {
 // Test the highlight method with valid arguments
 String term = "example";
 List<WordResult> searchResults = Arrays.asList(new WordResult(1, 10, 15), new WordResult(2, 20, 25));
 List<LineResult> lineResults = textPane.highlight(term, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(10, lineResult.getLineStartOffset());
 assertEquals(25, lineResult.getLineEndOffset());
 assertSame(Color.YELLOW, lineResult.getHighlightPainter().getColor());
 }

 @Test
 public void testHighlight_EmptyTerm() throws BadLocationException {
 // Test the highlight method with an empty term
 List<WordResult> emptySearchResults = Collections.emptyList();
 List<LineResult> lineResults = textPane.highlight("", 0);
 assertEquals(0, lineResults.size());
 }

 @Test(expected = BadLocationException.class)
 public void testHighlight_NullDocument() throws BadLocationException {
 // Test the highlight method with a null document
 textPane.setDocument(null);
 String term = "example";
 List<WordResult> searchResults = Arrays.asList(new WordResult(1, 10, 15), new WordResult(2, 20, 25));
 textPane.highlight(term, 0);
 }

 @Test(expected = NullPointerException.class)
 public void testHighlight_NullSearchResults() throws BadLocationException {
 // Test the highlight method with null search results
 List<LineResult> lineResults = textPane.highlight(null, 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testAddHighlight() throws BadLocationException {
 // Test the addHighlight method
 int start = 10;
 int length = 5;
 textPane.addHighlight(start, length);
 LineView highlight = textPane.getHighlighter().getHighlight(0);
 assertSame(Color.YELLOW, highlight.getPainter().getBackground());
 assertTrue(highlight.getLogicalEndOffset() > start + length);
 }

 @Test
 public void testRemoveHighlights() {
 // Test the removeHighlights method
 textPane.removeHighlights();
 assertFalse(textPane.getHighlighter().hasHighlightAt(0, 0));
 }

 @Test
 public void testSearch_ValidArguments() {
 // Test the search method with valid arguments
 String term = "example";
 int position = 10;
 int newPosition = textPane.search(term, position);
 assertEquals(-1, newPosition);
 textPane.setText("The quick brown fox jumps over the lazy dog. The example text is here.");
 int expectedPosition = textPane.getDocument().getLength() - term.length();
 newPosition = textPane.search(term, position);
 assertEquals(expectedPosition, newPosition);
 textPane.setText("New text here.");
 assertNull(textPane.search(term, 0));
 }

 @Test
 public void testSearch_EmptyTerm() {
 // Test the search method with an empty term
 String term = "";
 int position = 10;
 int newPosition = textPane.search(term, position);
 assertEquals(-1, newPosition);
 }

 @Test(expected = BadLocationException.class)
 public void testSearch_NullDocument() throws BadLocationException {
 // Test the search method with a null document
 textPane.setDocument(null);
 String term = "example";
 int position = 10;
 int newPosition = textPane.search(term, position);
 }

 @Test(expected = NullPointerException.class)
 public void testSearch_NullTerm() throws BadLocationException {
 // Test the search method with a null term
 String term = null;
 int position = 10;
 int newPosition = textPane.search(term, position);
 }

 @Test
 public void testConvertWords2Lines_ValidArguments() throws BadLocationException {
 // Test the convertWords2Lines method with valid arguments
 String text = "The quick brown fox jumps over the lazy dog.";
 List<WordResult> searchResults = Arrays.asList(new WordResult(1, 10, 15), new WordResult(2, 20, 25), new WordResult(3, 30, 35));
 List<LineResult> lineResults = textPane.convertWords2Lines(searchResults);
 assertEquals(2, lineResults.size());
 LineResult firstLineResult = lineResults.get(0);
 assertEquals(0, firstLineResult.getLineNumber());
 assertEquals(10, firstLineResult.getLineStartOffset());
 assertEquals(35, firstLineResult.getLineEndOffset());
 LineResult secondLineResult = lineResults.get(1);
 assertEquals(1, secondLineResult.getLineNumber());
 assertEquals(30, secondLineResult.getLineStartOffset());
 assertEquals(35, secondLineResult.getLineEndOffset());
 }

 @Test(expected = BadLocationException.class)
 public void testConvertWords2Lines_InvalidDocument() throws BadLocationException {
 // Test the convertWords2Lines method with an invalid document
 textPane.setDocument(null);
 List<WordResult> searchResults = Arrays.asList(new WordResult(1, 10, 15), new WordResult(2, 20, 25), new WordResult(3, 30, 35));
 textPane.convertWords2Lines(searchResults);
 }

 @Test
 public void testUpdateWordResult() throws BadLocationException {
 // Test the updateWordResult method
 String text = "The quick brown fox jumps over the lazy dog.";
 List<WordResult> searchResults = Arrays.asList(new WordResult(1, 10, 15), new WordResult(2, 20, 25), new WordResult(3, 30, 35));
 LineResult lineResult = new LineResult(0, 10, 45);
 WordResult wordResult = new WordResult(1, 10, 15);
 textPane.convertWords2Lines(searchResults);
 textPane.updateWordResult(wordResult, lineResult);
 assertEquals(1, wordResult.getLineNumber());
 assertEquals(10, wordResult.getLineOffset());
 assertEquals(45, wordResult.getLineEndOffset());
 }

}