package ghm.follow.search;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultHighlighter;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Line.Border;
import javax.swing.text.LineElement;
import javax.swing.text.LineView;
import javax.swing.text.Position.Bias;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private JFrame frame;
 private JTextArea textArea;
 private Document document;
 private DefaultHighlighter.DefaultHighlightPainter painter;

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8);
 document = textPane.getDocument();
 textArea = textPane;
 frame = new JFrame();
 frame.getContentPane().add(textArea, java.awt.FlowLayout.CENTER);
 frame.pack();
 frame.setVisible(true);
 painter = textPane.wordPainter;
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testConstructor() {
 assertNotNull(textPane);
 assertNotNull(textPane.getDocument());
 assertNotNull(textPane.getUI());
 assertEquals(new Font("Arial", Font.BOLD, 14), textPane.getFont());
 assertEquals(8, textPane.getTabSize());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertFalse(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyTerm() {
 textPane.highlight("", 0);
 assertTrue(textPane.getHighlighter().getHighlights().isEmpty());
 }

 @Test
 public void testHighlightSingleWord() {
 textPane.setText("The quick brown fox jumps over the lazy dog");
 textPane.highlight("fox", 0);
 List<LineResult> lineResults = textPane.getHighlighter().getHighlights();
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(34, lineResult.getStartOffset());
 assertEquals(39, lineResult.getEndOffset());
 assertNotNull(lineResult.getWordResults());
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals("fox", wordResult.getTerm());
 assertEquals(34, wordResult.getStart());
 assertEquals(39, wordResult.getEnd());
 assertEquals(35, wordResult.getLineOffset());
 }

 @Test
 public void testHighlightMultiWord() {
 textPane.setText("The quick brown fox jumps over the lazy dog");
 textPane.highlight("quick brown fox", 0);
 List<LineResult> lineResults = textPane.getHighlighter().getHighlights();
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(35, lineResult.getEndOffset());
 assertNotNull(lineResult.getWordResults());
 assertEquals(3, lineResult.getWordResults().size());
 List<WordResult> wordResults = lineResult.getWordResults();
 assertEquals("quick", wordResults.get(0).getTerm());
 assertEquals(0, wordResults.get(0).getStart());
 assertEquals(7, wordResults.get(0).getEnd());
 assertEquals(0, wordResults.get(0).getLineOffset());
 assertEquals("brown", wordResults.get(1).getTerm());
 assertEquals(7, wordResults.get(1).getStart());
 assertEquals(12, wordResults.get(1).getEnd());
 assertEquals(1, wordResults.get(1).getLineOffset());
 assertEquals("fox", wordResults.get(2).getTerm());
 assertEquals(12, wordResults.get(2).getStart());
 assertEquals(16, wordResults.get(2).getEnd());
 assertEquals(2, wordResults.get(2).getLineOffset());
 }

 @Test
 public void testHighlightMultipleOccurrences() {
 textPane.setText("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog");
 textPane.highlight("quick brown fox", 0);
 List<LineResult> lineResults = textPane.getHighlighter().getHighlights();
 assertEquals(2, lineResults.size());
 List<LineResult> lineResultsExpected = Arrays.asList(
 new LineResult(0, 0, 75),
 new LineResult(1, 75, 150)
 );
 verify(textPane.getHighlighter(), times(2)).addHighlight(anyInt(), anyInt(), eq(painter));
 assertEquals(lineResultsExpected, lineResults);
 }

 @Test
 public void testHighlightAtEndOfDocument() {
 textPane.setText("The quick brown fox jumps over the lazy dog");
 textPane.setCaretPosition(textPane.getText().length());
 textPane.highlight("fox", 0);
 List<LineResult> lineResults = textPane.getHighlighter().getHighlights();
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(textPane.getLineCount() - 1, lineResult.getLineNumber());
 assertEquals(textPane.getDocument().getLength() - 4, lineResult.getStartOffset());
 assertEquals(textPane.getDocument().getLength(), lineResult.getEndOffset());
 }

 @Test
 public void testHighlightErrorSearch() {
 textPane.setText("The quick brown fox jumps over the lazy dog");
 textPane.highlight(null, 0);
 List<LineResult> lineResults = textPane.getHighlighter().getHighlights();
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testSearchEmptyTerm() {
 assertEquals(-1, textPane.search(""));
 }

 @Test
 public void testSearchSingleWord() {
 textPane.setText("The quick brown fox jumps over the lazy dog");
 assertEquals(7, textPane.search("fox"));
 }

 @Test
 public void testSearchMultiWord() {
 textPane.setText("The quick brown fox jumps over the lazy dog");
 assertEquals(7, textPane.search("quick brown fox"));
 }

 @Test
 public void testSearchMultiWordWithError() {
 textPane.setText("The quick brown fox jumps over the lazy dog");
 textPane.setCaretPosition(100);
 assertEquals(-1, textPane.search("quick brown fox"));
 }

 @Test
 public void testSearchErrorTerm() {
 textPane.setText("The quick brown fox jumps over the lazy dog");
 assertEquals(-1, textPane.search(null));
 }

 @Test
 public void testSearchAtEndOfDocument() {
 textPane.setText("The quick brown fox jumps over the lazy dog");
 textPane.setCaretPosition(textPane.getText().length());
 assertEquals(-1, textPane.search("fox"));
 }

 @Test
 public void testSearchFromMiddleOfDocument() {
 textPane.setText("The quick brown fox jumps over the lazy dog");
 textPane.setCaretPosition(15);
 assertEquals(12, textPane.search("fox"));
 }

 @Test
 public void testSearchCaseInsensitive() {
 textPane.setText("The quick brown fox jumps over the lazy dog");
 assertEquals(7, textPane.search("fox"));
 assertEquals(7, textPane.search("FOX"));
 }

 @Test
 public void testSearchEmptyDocument() {
 textPane.setText("");
 assertEquals(-1, textPane.search("fox"));
 }

 @Test
 public void testSearchWithFlags() {
 textPane.setText("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog");
 textPane.search("quick brown fox", SearchableTextPane.REGULAR_EXPRESSION);
 List<LineResult> lineResults = textPane.getHighlighter().getHighlights();
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getStartOffset());
 assertEquals(75, lineResult1.getEndOffset());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(1, lineResult2.getLineNumber());
 assertEquals(75, lineResult2.getStartOffset());
 assertEquals(150, lineResult2.getEndOffset());
 }

 @Test
 public void testRemoveHighlights() {
 textPane.setText("The quick brown fox jumps over the lazy dog");
 textPane.highlight("fox", 0);
 List<LineResult> lineResults = textPane.getHighlighter().getHighlights();
 assertEquals(1, lineResults.size());
 textPane.removeHighlights();
 lineResults = textPane.getHighlighter().getHighlights();
 assertTrue(lineResults.isEmpty());
 }

}