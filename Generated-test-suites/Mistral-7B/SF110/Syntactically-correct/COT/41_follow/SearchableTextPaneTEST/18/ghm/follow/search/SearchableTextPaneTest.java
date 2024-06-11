package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
import javax.swing.text.Line.PropertyChangeEvent;
import javax.swing.text.LineResult;
import javax.swing.text.Position.Bias;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private JFrame frame;
 private JScrollPane scrollPane;

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 12), 8);
 frame = new JFrame();
 scrollPane = new JScrollPane(textPane);
 frame.getContentPane().add(scrollPane);
 frame.pack();
 frame.setVisible(true);
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testSearchableTextPaneConstructor() {
 // Test constructor with valid arguments
 assertNotNull(textPane);
 assertEquals(new Font("Arial", Font.BOLD, 12), textPane.getFont());
 assertEquals(8, textPane.getTabSize());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 // Test getScrollableTracksViewportWidth with parent component
 assertTrue(textPane.getScrollableTracksViewportWidth());
 // Test getScrollableTracksViewportWidth without parent component
 JTextArea textArea = new JTextArea();
 assertFalse(textArea.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightNullTerm() {
 List<LineResult> lineResults = textPane.highlight(null, 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightEmptyTerm() {
 List<WordResult> searchResults = new ArrayList<>();
 List<LineResult> lineResults = textPane.highlight("", searchResults.toArray(new WordResult[0]), 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightException() {
 List<WordResult> searchResults = new ArrayList<>();
 String text = "This is a test for SearchableTextPane. Highlight term: Exception";
 textPane.setText(text);
 Document doc = textPane.getDocument();
 try {
 doc.remove(0, doc.getLength());
 }
 catch (BadLocationException e) {
 // Expected exception
 }
 List<LineResult> lineResults = textPane.highlight("Exception", searchResults.toArray(new WordResult[0]), 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightNormalCase() {
 List<WordResult> searchResults = new ArrayList<>();
 String text = "This is a test for SearchableTextPane. Highlight term: SearchableTextPane";
 textPane.setText(text);
 Document doc = textPane.getDocument();
 LineResult lineResult = new LineResult(1, 0, text.length());
 textPane.highlight("SearchableTextPane", searchResults.toArray(new WordResult[0]), 0);
 assertEquals(1, lineResults.size());
 assertEquals(lineResult, lineResults.get(0));
 }

 @Test
 public void testAddAndRemoveHighlights() throws BadLocationException {
 List<WordResult> searchResults = new ArrayList<>();
 String text = "This is a test for SearchableTextPane. Highlight term: SearchableTextPane";
 textPane.setText(text);
 List<WordResult> wordResults = new ArrayList<>();
 wordResults.add(new WordResult("SearchableTextPane", 10, 24));
 LineResult lineResult = new LineResult(1, 0, text.length());
 lineResult.addWord(wordResults.get(0));
 textPane.highlight("SearchableTextPane", searchResults.toArray(new WordResult[0]), 0);
 assertEquals(1, textPane.getHighlighter().getHighlightCount());
 textPane.removeHighlights();
 assertEquals(0, textPane.getHighlighter().getHighlightCount());
 }

 @Test
 public void testSearchNullTerm() {
 assertEquals(-1, textPane.search(null));
 }

 @Test
 public void testSearchEmptyTerm() {
 assertEquals(-1, textPane.search(""));
 }

 @Test
 public void testSearchExistingTerm() {
 String text = "This is a test for SearchableTextPane. Search term: Test";
 textPane.setText(text);
 assertEquals(3, textPane.search("Test"));
 }

 @Test
 public void testSearchExistingTermAtStart() {
 String text = "This is a test for SearchableTextPane. Search term: Test";
 textPane.setText(text);
 assertEquals(0, textPane.search("Test"));
 }

 @Test
 public void testSearchExistingTermAtEnd() {
 String text = "This is a test for SearchableTextPane. Search term: Test";
 textPane.setText(text);
 assertEquals(text.length() - 4, textPane.search("Test"));
 }

 @Test
 public void testSearchExistingTermInMiddle() {
 String text = "This is a test for SearchableTextPane. Search term: is";
 textPane.setText(text);
 assertEquals(text.indexOf("is"), textPane.search("is"));
 }

 @Test
 public void testSearchTermNotFound() {
 String text = "This is a test for SearchableTextPane. Search term: XYZ";
 textPane.setText(text);
 assertEquals(-1, textPane.search("XYZ"));
 }

 @Test
 public void testSearchTermFoundThenChanged() {
 String text1 = "This is a test for SearchableTextPane. Search term: Test";
 textPane.setText(text1);
 int pos1 = textPane.search("Test");
 textPane.setText("This is a new test for SearchableTextPane. Search term: NewTest");
 int pos2 = textPane.search("Test");
 assertEquals(-1, pos2);
 assertEquals(pos1, textPane.search("Test"));
 }

 @Test
 public void testConvertWords2LinesNullWords() throws BadLocationException {
 List<LineResult> lineResults = textPane.convertWords2Lines(null);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testConvertWords2LinesEmptyWords() throws BadLocationException {
 List<WordResult> wordResults = new ArrayList<>();
 List<LineResult> lineResults = textPane.convertWords2Lines(wordResults.toArray(new WordResult[0]));
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testConvertWords2LinesSingleWord() throws BadLocationException {
 List<WordResult> wordResults = new ArrayList<>();
 wordResults.add(new WordResult("Test", 5, 6));
 List<LineResult> lineResults = textPane.convertWords2Lines(wordResults.toArray(new WordResult[0]));
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getLineNumber());
 assertEquals(0, lineResult.getLineStartOffset());
 assertEquals(textPane.getText().length(), lineResult.getLineEndOffset());
 }

 @Test
 public void testConvertWords2LinesMultipleWords() throws BadLocationException {
 List<WordResult> wordResults = new ArrayList<>();
 wordResults.add(new WordResult("Test", 5, 6));
 wordResults.add(new WordResult("TextPane", 12, 16));
 List<LineResult> lineResults = textPane.convertWords2Lines(wordResults.toArray(new WordResult[0]));
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getLineNumber());
 assertEquals(0, lineResult.getLineStartOffset());
 assertEquals(12, lineResult.getLineEndOffset());
 }

 @Test
 public void testUpdateWordResultNullWordResult() throws BadLocationException {
 WordResult wordResult = null;
 LineResult lineResult = new LineResult(1, 0, textPane.getText().length());
 textPane.updateWordResult(wordResult, lineResult);
 }

 @Test
 public void testUpdateWordResultNullLineResult() throws BadLocationException {
 WordResult wordResult = new WordResult("Test", 5, 6);
 LineResult lineResult = null;
 textPane.updateWordResult(wordResult, lineResult);
 }

 @Test
 public void testUpdateWordResultValidParameters() throws BadLocationException {
 WordResult wordResult = new WordResult("Test", 5, 6);
 LineResult lineResult = new LineResult(1, 0, textPane.getText().length());
 textPane.updateWordResult(wordResult, lineResult);
 assertEquals(lineResult, wordResult.parent);
 assertEquals(1, wordResult.lineNumber);
 }

 @Test
 public void testGetLineOfOffsetNullDocument() {
 assertNull(textPane.getLineOfOffset(0));
 }

 @Test
 public void testGetLineOfOffsetValidOffset() throws BadLocationException {
 Document doc = textPane.getDocument();
 int line = textPane.getLineOfOffset(100);
 assertSame(doc.getDefaultDocument().getDefaultRootElement().getElementAt(1), doc.getCharacterElement(line * textPane.getLineHeight() + textPane.getLineStartOffset()));
 }

 @Test
 public void testGetLineStartOffsetNullDocument() {
 assertEquals(-1, textPane.getLineStartOffset(0));
 }

 @Test
 public void testGetLineStartOffsetValidLine() throws BadLocationException {
 Document doc = textPane.getDocument();
 int lineStartOffset = textPane.getLineStartOffset(1);
 assertEquals(0, lineStartOffset);
 }

 @Test
 public void testGetLineStartOffsetInvalidLine() throws BadLocationException {
 Document doc = textPane.getDocument();
 int lineStartOffset = textPane.getLineStartOffset(10);
 assertEquals(-1, lineStartOffset);
 }

 @Test
 public void testSearchHighlightLineChangeListener() {
 LineResult lineResult = new LineResult(1, 0, textPane.getText().length());
 textPane.addLineChangeListener(e -> {
 if (e.getLine() == lineResult) {
 textPane.search("Test");
 }
 });
 textPane.setText("This is a test for SearchableTextPane. Highlight term: Test");
 }

}