package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private List<LineResult> lineResults;
 private WordResult wordResult;
 private static final String SEARCH_TERM = "test";

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 12), 8);
 lineResults = new ArrayList<LineResult>();
 }

 @Test
 public void testSearchableTextPaneConstructor() {
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 12), 8);
 assertNotNull(searchableTextPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 JFrame frame = new JFrame();
 JTextPane textPane = new JTextPane();
 frame.getContentPane().add(textPane);
 frame.pack();
 assertTrue(new SearchableTextPane(new Font("Serif", Font.PLAIN, 12), 8).getScrollableTracksViewportWidth());
 textPane.setViewportView(searchableTextPane);
 assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyString() {
 searchableTextPane.highlight("", 0);
 assertFalse(searchableTextPane.getHighlighter().hasHighlight());
 }

 @Test
 public void testHighlightNullTerm() {
 searchableTextPane.highlight(null, 0);
 assertFalse(searchableTextPane.getHighlighter().hasHighlight());
 }

 @Test
 public void testHighlightTermInEmptyTextPane() {
 String text = "This text pane is empty.";
 searchableTextPane.setText(text);
 searchableTextPane.highlight(SEARCH_TERM, 0);
 assertFalse(searchableTextPane.getHighlighter().hasHighlight());
 }

 @Test
 public void testHighlightTermNotFound() {
 String text = "This text pane contains the text 'searchable'.";
 searchableTextPane.setText(text);
 searchableTextPane.highlight(SEARCH_TERM, 0);
 assertFalse(searchableTextPane.getHighlighter().hasHighlight());
 }

 @Test
 public void testHighlightTermFound() {
 String text = "This text pane contains the text 'searchable' and 'SearchableTextPane'.";
 searchableTextPane.setText(text);
 List<WordResult> wordResults = new SearchEngine(0).search(SEARCH_TERM, text);
 lineResults = searchableTextPane.convertWords2Lines(wordResults);
 searchableTextPane.highlight(SEARCH_TERM, 0);
 assertTrue(searchableTextPane.getHighlighter().hasHighlight());
 }

 @Test(expected = BadLocationException.class)
 public void testAddHighlightWithBadLocationException() {
 searchableTextPane.addHighlight(-1, 1);
 }

 @Test
 public void testRemoveHighlights() {
 searchableTextPane.setText("This text pane has highlights.");
 searchableTextPane.highlight(SEARCH_TERM, 0);
 searchableTextPane.removeHighlights();
 assertFalse(searchableTextPane.getHighlighter().hasHighlight());
 }

 @Test
 public void testSearchNullTerm() {
 assertNull(searchableTextPane.search(null));
 }

 @Test
 public void testSearchEmptyString() {
 assertEquals(-1, searchableTextPane.search(""));
 }

 @Test
 public void testSearchTermNotFound() {
 String text = "This text pane does not contain the term 'searchable'.";
 assertEquals(-1, searchableTextPane.search(SEARCH_TERM), text.length());
 }

 @Test
 public void testSearchTermFound() {
 String text = "This text pane contains the term 'searchable' at position 10.";
 int position = 10;
 assertEquals(position, searchableTextPane.search(SEARCH_TERM), text.length());
 }

 @Test
 public void testConvertWords2LinesEmptyList() {
 lineResults = searchableTextPane.convertWords2Lines(new ArrayList<WordResult>());
 assertFalse(lineResults.isEmpty());
 }

 @Test(expected = BadLocationException.class)
 public void testConvertWords2LinesWithBadLocationException() {
 searchableTextPane.setText("This text pane has highlights.");
 LineResult lineResult = new LineResult(1, 0, 100);
 lineResults = new ArrayList<LineResult>();
 lineResults.add(lineResult);
 searchableTextPane.highlight(SEARCH_TERM, 0);
 searchableTextPane.convertWords2Lines(new ArrayList<WordResult>());
 }

 @Test
 public void testUpdateWordResult() throws BadLocationException {
 Document document = new PlainDocument();
 document.insertString(0, "This is a test.", null);
 Element element = document.getDefaultRootElement().getElement(0);
 searchableTextPane.setDocument(document);
 searchableTextPane.setSelectionStart(11);
 searchableTextPane.setSelectionEnd(12);
 wordResult = new WordResult(1, 11, 12, null, null, null, null, null);
 lineResult = new LineResult(1, 0, document.getLength());
 lineResults.add(lineResult);
 searchableTextPane.highlight(SEARCH_TERM, 0);
 searchableTextPane.convertWords2Lines(new ArrayList<WordResult>());
 searchableTextPane.updateWordResult(wordResult, lineResult);
 assertEquals(1, wordResult.lineNumber);
 }
}