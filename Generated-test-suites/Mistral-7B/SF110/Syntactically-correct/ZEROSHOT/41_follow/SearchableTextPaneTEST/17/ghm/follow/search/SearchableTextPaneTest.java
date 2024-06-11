package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.LineElement;
import javax.swing.text.LineNumberManager;
import javax.swing.text.LineView;
import javax.swing.text.PlainDocument;
import javax.swing.text.SearchCookie;
import javax.swing.text.SearchContext;
import javax.swing.text.SearchEngine;
import javax.swing.text.Searchable;
import javax.swing.text.SearchResult;
import javax.swing.text.View;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private String searchTerm = "Test";
 private List<LineResult> lineResults;
 private int lastSearchPos = -1;

 @Test
 public void testSearchableTextPaneConstructor() {
 // Test constructor
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 assertNotNull(searchableTextPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 // Test getScrollableTracksViewportWidth
 JFrame frame = new JFrame();
 JScrollPane scrollPane = new JScrollPane();
 scrollPane.setViewportView(textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8));
 frame.getContentPane().add(scrollPane);
 frame.pack();
 frame.setVisible(true);
 assertTrue(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightWithNullTerm() {
 // Test highlight with null term
 lineResults = textPane.highlight(null, 0);
 assertNotNull(lineResults);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlightWithEmptyTerm() {
 // Test highlight with empty term
 lineResults = textPane.highlight("", 0);
 assertNotNull(lineResults);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlightWithText() {
 // Test highlight with text
 textPane.setText("This is a test SearchableTextPane. This SearchableTextPane can search for text.");
 lineResults = textPane.highlight(searchTerm, 0);
 assertNotNull(lineResults);
 assertFalse(lineResults.isEmpty());

 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(14, lineResult.getEndOffset());

 // Test highlight with text again (should return the same results)
 lineResults = textPane.highlight(searchTerm, 0);
 assertEquals(lineResults.size(), 1);
 assertEquals(lineResult, lineResults.get(0));
 }

 @Test
 public void testSearchWithNullTerm() {
 // Test search with null term
 lastSearchPos = textPane.search(null);
 assertEquals(-1, lastSearchPos);
 }

 @Test
 public void testSearchWithEmptyTerm() {
 // Test search with empty term
 lastSearchPos = textPane.search("");
 assertEquals(-1, lastSearchPos);
 }

 @Test
 public void testSearchWithText() {
 // Test search with text
 textPane.setText("This is a test SearchableTextPane. This SearchableTextPane can search for text.");
 lastSearchPos = textPane.search(searchTerm);
 assertEquals(5, lastSearchPos);
 }

 @Test
 public void testSearchWithTextStartingBeforeText() {
 // Test search with text starting before text
 textPane.setText("This is a test SearchableTextPane. This SearchableTextPane can search for text.");
 lastSearchPos = textPane.search(searchTerm, 0);
 assertEquals(-1, lastSearchPos);
 }

 @Test
 public void testSearchWithTextStartingInMiddleOfText() {
 // Test search with text starting in middle of text
 textPane.setText("This is a test SearchableTextPane. This SearchableTextPane can search for text.");
 lastSearchPos = textPane.search(searchTerm, 3);
 assertEquals(8, lastSearchPos);
 }

 @Test
 public void testSearchWithTextStartingAfterText() {
 // Test search with text starting after text
 textPane.setText("This is a test SearchableTextPane. This SearchableTextPane can search for text.");
 lastSearchPos = textPane.search(searchTerm, 21);
 assertEquals(-1, lastSearchPos);
 }

 @Test
 public void testConvertWords2LinesWithEmptyList() {
 // Test convertWords2Lines with empty list
 List<WordResult> words = Collections.emptyList();
 lineResults = textPane.convertWords2Lines(words);
 assertNotNull(lineResults);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testConvertWords2LinesWithSingleWord() {
 // Test convertWords2Lines with single word
 textPane.setText("This is a test SearchableTextPane. This SearchableTextPane can search for text.");
 List<WordResult> words = Arrays.asList(new WordResult(0, 1, "Test", 5));
 lineResults = textPane.convertWords2Lines(words);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(14, lineResult.getEndOffset());
 }

 @Test
 public void testConvertWords2LinesWithMultipleWords() {
 // Test convertWords2Lines with multiple words
 textPane.setText("This is a test SearchableTextPane. This SearchableTextPane can search for text.");
 List<WordResult> words = Arrays.asList(new WordResult(0, 1, "Test", 5), new WordResult(6, 7, "TextPane", 10));
 lineResults = textPane.convertWords2Lines(words);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(14, lineResult.getEndOffset());
 }

 @Test
 public void testAddHighlight() {
 // Test addHighlight
 textPane.setText("This is a test SearchableTextPane. This SearchableTextPane can search for text.");
 textPane.highlight(searchTerm, 0);
 int length = textPane.getDocument().getLength();
 DefaultHighlighter highlighter = textPane.getHighlighter();
 viewHighlight(highlighter, length - 5, 5);
 View view = highlighter.getView(0);
 LineView lineView = (LineView) view;
 assertEquals(Color.YELLOW, lineView.getBackground());
 }

 @Test
 public void testRemoveHighlights() {
 // Test removeHighlights
 textPane.setText("This is a test SearchableTextPane. This SearchableTextPane can search for text.");
 textPane.highlight(searchTerm, 0);
 textPane.removeHighlights();
 int length = textPane.getDocument().getLength();
 DefaultHighlighter highlighter = textPane.getHighlighter();
 assertFalse(highlighter.hasHighlightAtOffset(length - 5));
 }

 @Test
 public void testSearchEngineSearch() {
 // Test SearchEngine search
 SearchEngine searchEngine = new SearchEngine(SearchContext.LEXICAL);
 List<WordResult> wordResults = searchEngine.search(searchTerm, "This is a test SearchableTextPane. This SearchableTextPane can search for text.");
 assertEquals(2, wordResults.size());
 WordResult firstWordResult = wordResults.get(0);
 assertEquals("Test", firstWordResult.getWord());
 assertEquals(5, firstWordResult.getStart());
 assertEquals(10, firstWordResult.getEnd());
 WordResult secondWordResult = wordResults.get(1);
 assertEquals("TextPane", secondWordResult.getWord());
 assertEquals(11, secondWordResult.getStart());
 assertEquals(17, secondWordResult.getEnd());
 }

 @Test
 public void testUtilitiesGetParagraphElement() {
 // Test Utilities.getParagraphElement
 Element element = Utilities.getParagraphElement(textPane, 5);
 assertTrue(element instanceof LineElement);
 LineElement lineElement = (LineElement) element;
 assertEquals(5, lineElement.getStartOffset());
 assertEquals(14, lineElement.getEndOffset());
 }

 private void viewHighlight(DefaultHighlighter highlighter, int offset, int length) {
 SwingUtilities.invokeLater(() -> {
 try {
 highlighter.addHighlight(offset, offset + length, textPane.wordPainter);
 }
 catch (BadLocationException e) {
 e.printStackTrace();
 }
 });
 }

}