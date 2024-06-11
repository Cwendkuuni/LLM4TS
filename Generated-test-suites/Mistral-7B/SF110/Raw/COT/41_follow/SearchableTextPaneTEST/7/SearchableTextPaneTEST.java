package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.SearchEngine;
import javax.swing.text.Searchable;
import javax.swing.text.AreaView;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Utilities;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private JFrame frame;
 private JTextArea textArea;
 private JScrollPane scrollPane;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 14), 8);
 frame = new JFrame();
 textArea = new JTextArea();
 scrollPane = new JScrollPane(textArea);
 frame.getContentPane().add(scrollPane);
 frame.setSize(400, 400);
 frame.setVisible(true);
 frame.addWindowListener(new java.awt.event.WindowAdapter() {
 public void windowClosing(java.awt.event.WindowEvent e) {
 frame.dispose();
 }
 });
 textArea = searchableTextPane;
 }

 @Test
 public void testSearchableTextPaneConstructor() {
 SearchableTextPane stp = new SearchableTextPane(new Font("Serif", Font.PLAIN, 14), 8);
 assertNotNull(stp);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightNullTerm() {
 List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightEmptyTerm() {
 String term = "";
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightNoResults() {
 String term = "notfound";
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightOneResult() {
 String term = "Java";
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 }

 @Test
 public void testHighlightMultipleResults() {
 String text = "The quick brown fox jumps over the lazy dog.";
 String term = "the";
 textArea.setText(text);
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertNotNull(lineResults);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getStartOffset());
 assertEquals(6, lineResult1.getEndOffset());
 assertEquals(1, lineResult2.getLineNumber());
 assertEquals(16, lineResult2.getStartOffset());
 assertEquals(18, lineResult2.getEndOffset());
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightException() {
 List<LineResult> lineResults = searchableTextPane.highlight("Java", -1);
 fail("Expected BadLocationException");
 }

 @Test
 public void testAddHighlight() throws BadLocationException {
 int start = 0;
 int length = 5;
 searchableTextPane.addHighlight(start, length);
 Highlighter highlighter = searchableTextPane.getHighlighter();
 AreaView areaView = highlighter.getView(0);
 assertNotNull(areaView);
 HighlightPainter painter = areaView.getPainter();
 assertEquals(Color.YELLOW, painter.getPaint());
 }

 @Test
 public void testRemoveHighlights() {
 searchableTextPane.removeHighlights();
 Highlighter highlighter = searchableTextPane.getHighlighter();
 assertFalse(highlighter.hasHighlight(0, 0));
 }

 @Test
 public void testSearchNullTerm() {
 int result = searchableTextPane.search(null);
 assertEquals(-1, result);
 }

 @Test
 public void testSearchEmptyTerm() {
 int result = searchableTextPane.search("");
 assertEquals(-1, result);
 }

 @Test
 public void testSearchNoResults() {
 String term = "notfound";
 int result = searchableTextPane.search(term);
 assertEquals(-1, result);
 }

 @Test
 public void testSearchOneResult() {
 String text = "The quick brown fox jumps over the lazy dog.";
 String term = "the";
 textArea.setText(text);
 int result = searchableTextPane.search(term);
 assertEquals(0, result);
 }

 @Test
 public void testSearchMultipleResults() {
 String text = "The quick brown fox jumps over the lazy dog.\nThe quick brown fox jumps over the quick red dog.";
 String term = "the";
 textArea.setText(text);
 int result1 = searchableTextPane.search(term);
 assertEquals(0, result1);
 int result2 = searchableTextPane.search(term, result1 + term.length());
 assertEquals(1, result2);
 }

 @Test
 public void testSearchPreviousResult() {
 String text = "The quick brown fox jumps over the lazy dog.\nThe quick brown fox jumps over the quick red dog.";
 String term = "the";
 textArea.setText(text);
 int result1 = searchableTextPane.search(term);
 int result2 = searchableTextPane.search(term, result1 + term.length());
 int result3 = searchableTextPane.search(term, result2 + term.length());
 assertEquals(2, result3);
 }

 @Test(expected = BadLocationException.class)
 public void testSearchException() throws BadLocationException {
 int result = searchableTextPane.search("Java", 1000);
 fail("Expected BadLocationException");
 }

 @Test
 public void testConvertWords2LinesNullWords() {
 List<WordResult> words = null;
 List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testConvertWords2LinesEmptyWords() {
 List<WordResult> words = new ArrayList<>();
 List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testConvertWords2LinesOneWord() {
 List<WordResult> words = Arrays.asList(new WordResult("Java", 0, 5));
 List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(6, lineResult.getEndOffset());
 }

 @Test
 public void testConvertWords2LinesMultipleWords() {
 List<WordResult> words = Arrays.asList(new WordResult("The", 0, 3), new WordResult("quick", 7, 6), new WordResult("brown", 13, 6), new WordResult("fox", 20, 4));
 List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
 assertNotNull(lineResults);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getStartOffset());
 assertEquals(12, lineResult1.getEndOffset());
 assertEquals(1, lineResult2.getLineNumber());
 assertEquals(13, lineResult2.getStartOffset());
 assertEquals(20, lineResult2.getEndOffset());
 }

 @Test(expected = BadLocationException.class)
 public void testConvertWords2LinesException() throws BadLocationException {
 List<WordResult> words = Arrays.asList(new WordResult("Java", 0, 5));
 List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
 lineResults.get(0).addWord(new WordResult("Java", 6, 7));
 fail("Expected BadLocationException");
 }
}