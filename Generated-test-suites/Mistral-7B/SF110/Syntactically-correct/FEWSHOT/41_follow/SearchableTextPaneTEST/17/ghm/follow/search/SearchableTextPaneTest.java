package ghm.follow.search;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.text.*;
import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private JTextComponent mockTextComponent;
 private DefaultHighlighter mockHighlighter;
 private DefaultCaret mockCaret;
 private Logger mockLogger;

 @Before
 public void setUp() {
 mockTextComponent = mock(JTextComponent.class);
 mockHighlighter = mock(DefaultHighlighter.class);
 mockCaret = mock(DefaultCaret.class);
 mockLogger = mock(Logger.class);
 searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4) {
 @Override
 public JTextComponent getTextComponent() {
 return mockTextComponent;
 }

 @Override
 public DefaultHighlighter getHighlighter() {
 return mockHighlighter;
 }

 @Override
 public DefaultCaret getCaret() {
 return mockCaret;
 }

 @Override
 public Logger getLogger() {
 return mockLogger;
 }
 };
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightWithEmptyString() {
 List<LineResult> lineResults = searchableTextPane.highlight("", 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightWithValidTermAndFlags() {
 final String term = "Java";
 final int flags = SearchEngine.REGEX_MULTILINE | SearchEngine.REGEX_CASE_INSENSITIVE;
 List<WordResult> searchResults = new ArrayList<>();
 searchResults.add(new WordResult(6, 10, "Java".length(), "Java", 0));
 final List<LineResult> lineResults = searchableTextPane.highlight(term, flags);
 assertEquals(1, lineResults.size());
 verify(mockTextComponent).setCaretPosition(AnyInt.anyInt());
 verify(mockHighlighter).addHighlight(6, 10 + term.length(), searchableTextPane.wordPainter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHighlightWithNullTerm() {
 searchableTextPane.highlight(null, 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHighlightWithNegativeFlags() {
 searchableTextPane.highlight("Java", -1);
 }

 @Test
 public void testRemoveHighlights() {
 searchableTextPane.removeHighlights();
 verify(mockHighlighter, times(1)).removeAllHighlights();
 }

 @Test
 public void testSearchWithEmptyString() {
 assertEquals(-1, searchableTextPane.search(""));
 }

 @Test
 public void testSearchWithValidTermAndPreviousMatchFound() {
 final String term = "Java";
 searchableTextPane.search(term);
 searchableTextPane.search(term);
 verify(mockLogger, times(1)).log(Level.WARNING, "BadLocationException in SearchableTextPane", any(BadLocationException.class));
 assertEquals(4, searchableTextPane.search(term, 1));
 }

 @Test
 public void testSearchWithValidTermAndNoPreviousMatchFound() {
 final String term = "Java";
 searchableTextPane.search(term);
 assertEquals(-1, searchableTextPane.search(term));
 }

 @Test
 public void testSearchWithNullTerm() {
 assertEquals(-1, searchableTextPane.search(null));
 }

 @Test
 public void testConvertWords2LinesWithEmptyList() {
 final List<WordResult> emptyWordsList = new ArrayList<>();
 final List<LineResult> lineResults = searchableTextPane.convertWords2Lines(emptyWordsList);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testConvertWords2LinesWithOneWordResult() {
 final String term = "Java";
 final WordResult wordResult = new WordResult(6, 10, "Java".length(), term, 0);
 final List<LineResult> lineResults = searchableTextPane.convertWords2Lines(Arrays.asList(wordResult));
 assertEquals(1, lineResults.size());
 }

 @Test
 public void testConvertWords2LinesWithMultipleWordResults() {
 final String term = "Java";
 final WordResult wordResult1 = new WordResult(6, 10, "Java".length(), term, 0);
 final WordResult wordResult2 = new WordResult(12, 15, "Java".length(), term, 0);
 final WordResult wordResult3 = new WordResult(18, 21, "Java".length(), term, 0);
 final List<LineResult> lineResults = searchableTextPane.convertWords2Lines(Arrays.asList(wordResult1, wordResult2, wordResult3));
 assertEquals(1, lineResults.size());
 }

 @Test
 public void testUpdateWordResult() throws BadLocationException {
 final int line = 1;
 final LineResult lineResult = new LineResult(line, 0, 0);
 final WordResult wordResult = new WordResult(6, 10, "Java".length(), "Java", line);
 searchableTextPane.updateWordResult(wordResult, lineResult);
 assertEquals(line, lineResult.lineNumber);
 }
}