package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import javax.swing.JTextArea;
import javax.swing.TextAttribute;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
     textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
 }

 @Test
 public void testSearchableTextPaneConstructor() {
     assertNotNull(textPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
     assertTrue(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlight_NullTerm() {
     List<LineResult> result = textPane.highlight(null, 0);
     assertTrue(result.isEmpty());
 }

 @Test
 public void testHighlight_EmptyTerm() {
     String text = "This is a test text.";
     textPane.setText(text);
     List<LineResult> result = textPane.highlight("", 0);
     assertTrue(result.isEmpty());
 }

 @Test
 public void testHighlight_Success() {
     String text = "This is a test text. This is a test for SearchableTextPane.class.";
     textPane.setText(text);
     List<LineResult> result = textPane.highlight("SearchableTextPane", 0);
     assertEquals(1, result.size());
     LineResult lineResult = result.get(0);
     assertEquals(1, lineResult.getLineNumber());
     assertEquals(25, lineResult.getStartOffset());
     assertEquals(35, lineResult.getEndOffset());
     assertNotNull(lineResult.getWordResults());
     assertEquals(1, lineResult.getWordResults().size());
     WordResult wordResult = lineResult.getWordResults().get(0);
     assertEquals(25, wordResult.getStart());
     assertEquals(30, wordResult.getEnd());
     assertEquals(1, wordResult.getLineNumber());
 }

 @Test
 public void testHighlight_MultipleMatches() {
     String text = "This is a test text. This is a test for SearchableTextPane.class SearchableTextPane is a class in the ghm.follow.search package.";
     textPane.setText(text);
     List<LineResult> result = textPane.highlight("SearchableTextPane", 0);
     assertEquals(2, result.size());
     LineResult firstLineResult = result.get(0);
     LineResult secondLineResult = result.get(1);
     assertEquals(1, firstLineResult.getLineNumber());
     assertEquals(25, firstLineResult.getStartOffset());
     assertEquals(35, firstLineResult.getEndOffset());
     assertEquals(2, secondLineResult.getLineNumber());
     assertEquals(78, secondLineResult.getStartOffset());
     assertEquals(88, secondLineResult.getEndOffset());
 }

 @Test(expected = BadLocationException.class)
 public void testHighlight_BadLocationException() {
     textPane.highlight("SearchableTextPane", -1);
 }

 @Test
 public void testRemoveHighlights() {
     String text = "This is a test text. This is a test for SearchableTextPane.class SearchableTextPane is a class in the ghm.follow.search package.";
     textPane.setText(text);
     List<LineResult> result = textPane.highlight("SearchableTextPane", 0);
     textPane.removeHighlights();
     List<LineResult> emptyResult = textPane.highlight("SearchableTextPane", 0);
     assertTrue(emptyResult.isEmpty());
 }

 @Test
 public void testSearch_NullTerm() {
     String text = "This is a test text.";
     textPane.setText(text);
     int result = textPane.search(null);
     assertEquals(-1, result);
 }

 @Test
 public void testSearch_EmptyTerm() {
     String text = "This is a test text.";
     textPane.setText(text);
     int result = textPane.search("");
     assertEquals(-1, result);
 }

 @Test
 public void testSearch_Success() {
     String text = "This is a test text. This is a test for SearchableTextPane.class SearchableTextPane is a class in the ghm.follow.search package.";
     textPane.setText(text);
     int result = textPane.search("SearchableTextPane");
     assertEquals(25, result);
 }

 @Test
 public void testSearch_MultipleMatches() {
     String text = "This is a test text. This is a test for SearchableTextPane.class SearchableTextPane is a class in the ghm.follow.search package.";
     textPane.setText(text);
     int result = textPane.search("SearchableTextPane");
     assertEquals(25, result);
     result = textPane.search(TextAttribute.FOREGROUND.getName());
     assertEquals(-1, result);
 }

 @Test
 public void testSearch_NotFound() {
     String text = "This is a test text. This is a test for SearchableTextPane.class SearchableTextPane is a class in the ghm.follow.search package.";
     textPane.setText(text);
     int result = textPane.search("TextArea");
     assertEquals(-1, result);
 }

 @Test
 public void testConvertWords2Lines_NullWords() {
     List<WordResult> words = null;
     List<LineResult> result = textPane.convertWords2Lines(words);
     assertTrue(result.isEmpty());
 }

 @Test
 public void testConvertWords2Lines_EmptyWords() {
     List<WordResult> words = new ArrayList<>();
     List<LineResult> result = textPane.convertWords2Lines(words);
     assertTrue(result.isEmpty());
 }

 @Test
 public void testConvertWords2Lines_Success() {
     String text = "This is a test text. This is a test for SearchableTextPane.class SearchableTextPane is a class in the ghm.follow.search package.";
     textPane.setText(text);
     List<WordResult> words = new ArrayList<>();
     words.add(new WordResult(0, 25, "SearchableTextPane", 1));
     List<LineResult> result = textPane.convertWords2Lines(words);
     assertEquals(1, result.size());
     LineResult lineResult = result.get(0);
     assertEquals(1, lineResult.getLineNumber());
     assertEquals(25, lineResult.getStartOffset());
     assertEquals(35, lineResult.getEndOffset());
     assertNotNull(lineResult.getWordResults());
     assertEquals(1, lineResult.getWordResults().size());
     WordResult wordResult = lineResult.getWordResults().get(0);
     assertEquals(25, wordResult.getStart());
     assertEquals(30, wordResult.getEnd());
 }

 @Test
 public void testConvertWords2Lines_MultipleMatches() {
     String text = "This is a test text. This is a test for SearchableTextPane.class SearchableTextPane is a class in the ghm.follow.search package.";
     textPane.setText(text);
     List<WordResult> words = new ArrayList<>();
     words.add(new WordResult(0, 25, "SearchableTextPane", 1));
     words.add(new WordResult(51, 58, "SearchableTextPane", 2));
     List<LineResult> result = textPane.convertWords2Lines(words);
     assertEquals(2, result.size());
     LineResult firstLineResult = result.get(0);
     LineResult secondLineResult = result.get(1);
     assertEquals(1, firstLineResult.getLineNumber());
     assertEquals(25, firstLineResult.getStartOffset());
     assertEquals(35, firstLineResult.getEndOffset());
     assertEquals(2, secondLineResult.getLineNumber());
     assertEquals(51, secondLineResult.getStartOffset());
     assertEquals(58, secondLineResult.getEndOffset());
 }

 @Test(expected = BadLocationException.class)
 public void testConvertWords2Lines_BadLocationException() {
     List<WordResult> words = new ArrayList<>();
     words.add(new WordResult(-1, 25, "SearchableTextPane", 1));
     textPane.convertWords2Lines(words);
 }

 @Test
 public void testAddHighlight_Success() throws BadLocationException {
     textPane.setText("This is a test text.");
     textPane.addHighlight(25, 5);
     Highlighter highlight = textPane.getHighlighter();
     HighlightPainter painter = highlight.getHighlightPainter();
     SimpleAttributeSet attr = new SimpleAttributeSet();
     StyleConstants.setBackground(attr, Color.YELLOW);
     assertEquals(Color.YELLOW, painter.getPaint());
     List<LineResult> lineResults = textPane.highlight("", 0);
     assertTrue(lineResults.isEmpty());
     List<WordResult> words = new ArrayList<>();
     words.add(new WordResult(20, 25, "SearchableTextPane", 1));
     List<LineResult> lines = textPane.convertWords2Lines(words);
     LineResult lineResult = lines.get(0);
     assertNotNull(lineResult.getWordResults());
     assertEquals(1, lineResult.getWordResults().size());
     WordResult wordResult = lineResult.getWordResults().get(0);
     assertEquals(25, wordResult.getStart());
     assertEquals(30, wordResult.getEnd());
     Highlight highlight1 = lineResult.getHighlight();
     assertEquals(Color.YELLOW, highlight1.getPainter().getPaint());
 }

 @Test(expected = BadLocationException.class)
 public void testAddHighlight_BadLocationException() throws BadLocationException {
     textPane.setText("This is a test text.");
     textPane.addHighlight(-1, 5);
 }

}