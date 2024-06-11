package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.Line;
import javax.swing.text.LineElement;
import javax.swing.text.LineView;
import javax.swing.text.Position.Bias;
import javax.swing.text.TabSet;
import javax.swing.text.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private JFrame frame;

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8);
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.add(new JScrollPane(textPane), BorderLayout.CENTER);
 frame.pack();
 frame.setVisible(true);
 }

 @After
 public void tearDown() {
 textPane = null;
 frame.dispose();
 }

 @Test
 public void testSearchableTextPaneConstructor() {
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8);
 assertNotNull(searchableTextPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(textPane.getScrollableTracksViewportWidth());
 textPane.setParent(null);
 assertFalse(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyTerm() {
 textPane.highlight("", 0);
 assertEquals(0, textPane.getDocument().getLength());
 }

 @Test
 public void testHighlightNullDocument() {
 textPane.removeHighlights();
 textPane.setDocument(null);
 textPane.highlight("test", 0);
 assertFalse(textPane.hasFocus());
 }

 @Test
 public void testHighlightNullTerm() {
 textPane.highlight(null, 0);
 assertFalse(textPane.hasFocus());
 }

 @Test
 public void testHighlightTerm() throws BadLocationException {
 textPane.setText("This is a test text.");
 textPane.highlight("test", 0);
 verifyHighlights(textPane, 0, 4);
 }

 @Test
 public void testHighlightTermWithFlags() throws BadLocationException {
 textPane.setText("This is a test text.");
 textPane.highlight("test", 0, SearchEngine.REGULAR_EXPRESSION);
 verifyHighlights(textPane, 2, 5);
 }

 @Test
 public void testHighlightWrongPosition() throws BadLocationException {
 textPane.setText("This is a test text.");
 textPane.highlight("test", textPane.getDocument().getLength());
 verifyNoHighlights(textPane);
 }

 @Test
 public void testSearchEmptyTerm() {
 textPane.setText("This is a test text.");
 int position = textPane.search("");
 assertEquals(-1, position);
 }

 @Test
 public void testSearchNullTerm() {
 textPane.setText("This is a test text.");
 int position = textPane.search(null);
 assertEquals(-1, position);
 }

 @Test
 public void testSearchTermFoundInDocument() {
 textPane.setText("This is a test text.");
 int position = textPane.search("test");
 assertEquals(7, position);
 }

 @Test
 public void testSearchTermNotFoundInDocument() {
 textPane.setText("This is a test text.");
 int position = textPane.search("test2");
 assertEquals(-1, position);
 }

 @Test
 public void testSearchTermCaseSensitive() {
 textPane.setText("This is a test text.");
 int position = textPane.search("Test");
 assertEquals(-1, position);
 }

 @Test
 public void testSearchTermCaseInsensitive() {
 textPane.setText("This is a test text.");
 textPane.setSearchCaseSensitive(false);
 int position = textPane.search("Test");
 assertEquals(0, position);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchNegativePosition() {
 textPane.setText("This is a test text.");
 textPane.search(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchPositionBeyondDocumentEnd() {
 textPane.setText("This is a test text.");
 int length = textPane.getDocument().getLength();
 textPane.search(length + 1);
 }

 @Test
 public void testConvertWords2LinesEmptyList() throws BadLocationException {
 textPane.setText("This is a test text.");
 List<WordResult> emptyList = new ArrayList<>();
 List<LineResult> lineResults = textPane.convertWords2Lines(emptyList);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testConvertWords2LinesNullList() throws BadLocationException {
 textPane.setText("This is a test text.");
 List<LineResult> lineResults = textPane.convertWords2Lines(null);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testConvertWords2LinesSingleWord() throws BadLocationException {
 textPane.setText("This is a test text.");
 String word = "test";
 List<WordResult> wordResult = new ArrayList<>();
 wordResult.add(new WordResult(0, 4, word));
 List<LineResult> lineResults = textPane.convertWords2Lines(wordResult);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getLineStartOffset());
 assertEquals(4, lineResult.getLineEndOffset());
 }

 @Test
 public void testConvertWords2LinesMultipleWords() throws BadLocationException {
 textPane.setText("This is a test text.");
 String word1 = "test1";
 String word2 = "test2";
 List<WordResult> wordResults = new ArrayList<>();
 wordResults.add(new WordResult(0, 4, word1));
 wordResults.add(new WordResult(10, 5, word2));
 List<LineResult> lineResults = textPane.convertWords2Lines(wordResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getLineStartOffset());
 assertEquals(11, lineResult.getLineEndOffset());
 }

 @Test
 public void testConvertWords2LinesMultipleLines() throws BadLocationException {
 textPane.setText("This is a test text1.\nThis is a test text2.");
 String word1 = "test1";
 String word2 = "test2";
 List<WordResult> wordResults = new ArrayList<>();
 wordResults.add(new WordResult(0, 6, word1));
 wordResults.add(new WordResult(17, 4, word2));
 List<LineResult> lineResults = textPane.convertWords2Lines(wordResults);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getLineStartOffset());
 assertEquals(6, lineResult1.getLineEndOffset());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(1, lineResult2.getLineNumber());
 assertEquals(11, lineResult2.getLineStartOffset());
 assertEquals(16, lineResult2.getLineEndOffset());
 }

 private void verifyNoHighlights(SearchableTextPane textPane) {
 Highlighter highlighter = textPane.getHighlighter();
 List<Highlight> highlights = highlighter.getHighlights(0, textPane.getDocument().getLength());
 assertEquals(0, highlights.size());
 }

 private void verifyHighlights(SearchableTextPane textPane, int start, int end) throws BadLocationException {
 Highlighter highlighter = textPane.getHighlighter();
 List<Highlight> highlights = highlighter.getHighlights(start, end);
 assertEquals(1, highlights.size());
 Line line = textPane.getDocument().getDefaultDocumentElement().getLineElement(textPane.getLineOfOffset(start));
 View view = line.viewForPosition(line.getStartOffset(), Bias.Forward);
 assertEquals(view.getClass(), LineView.class);
 LineView lineView = (LineView) view;
 Highlight highlight = highlights.get(0);
 assertEquals(highlight.getClass(), Highlight.HighlightPainter.class);
 HighlightPainter painter = (HighlightPainter) highlight;
 assertEquals(painter.getPaint(), textPane.wordPainter);
 }

}