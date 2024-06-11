package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ghm.follow.search.SearchableTextPane;
import ghm.follow.search.SearchEngine;
import ghm.follow.search.Utilities;
import ghm.follow.search.WordResult;
import ghm.follow.search.LineResult;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private SearchEngine searchEngine;

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 12), 4);
 searchEngine = new SearchEngine(0);
 }

 @Test
 public void testSearchableTextPaneConstructor() {
 SearchableTextPane textPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 12), 4);
 assertNotNull(textPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 JFrame frame = new JFrame();
 frame.setSize(400, 400);
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.add(textPane);
 frame.pack();
 frame.setVisible(true);
 assertTrue(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyString() {
 String term = "";
 List<LineResult> lineResults = textPane.highlight(term, 0);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlightNullTerm() {
 String term = null;
 List<LineResult> lineResults = textPane.highlight(term, 0);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlightValidTerm() {
 String term = "Java";
 String text = "This is a Java text pane.";
 textPane.setText(text);
 List<WordResult> searchResults = searchEngine.search(term, text);
 List<LineResult> lineResults = textPane.highlight(term, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(6, lineResult.getLineStartOffset());
 assertEquals(12, lineResult.getLineEndOffset());
 for (WordResult wordResult : lineResult.getWordResults()) {
 assertEquals(5, wordResult.getStart());
 assertEquals(11, wordResult.getEnd());
 assertEquals(6, wordResult.getLineNumber());
 assertEquals(5, wordResult.getLineOffset());
 }
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightWithException() {
 String term = "Java";
 List<WordResult> searchResults = searchEngine.search(term, "Invalid text");
 textPane.highlight(term, 0);
 fail("Should have thrown BadLocationException");
 }

 @Test
 public void testRemoveHighlights() {
 String term = "Java";
 textPane.setText("This is a Java text pane. This is a Java text pane.");
 List<WordResult> searchResults = searchEngine.search(term, textPane.getText());
 textPane.highlight(term, 0);
 List<LineResult> lineResults = textPane.highlight(term, 0);
 textPane.removeHighlights();
 List<LineResult> lineResultsAfterRemove = textPane.highlight(term, 0);
 assertTrue(lineResults.size() > lineResultsAfterRemove.size());
 }

 @Test
 public void testSearchEmptyString() {
 String term = "";
 int pos = textPane.search(term);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchNullTerm() {
 String term = null;
 int pos = textPane.search(term);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchValidTerm() {
 String term = "Java";
 String text = "This is a Java text pane.";
 textPane.setText(text);
 int pos = textPane.search(term);
 assertEquals(5, pos);
 }

 @Test
 public void testSearchPreviousTerm() {
 String term = "text";
 String text = "This is a Java text pane. This is a Java text pane.";
 textPane.setText(text);
 textPane.search("Java");
 int pos = textPane.search(term);
 assertEquals(15, pos);
 }

 @Test
 public void testSearchInvalidTerm() {
 String term = "#%&";
 int pos = textPane.search(term);
 assertEquals(-1, pos);
 }

 @Test(expected = BadLocationException.class)
 public void testSearchWithException() {
 String term = "Java";
 textPane.search(null);
 fail("Should have thrown BadLocationException");
 }
}