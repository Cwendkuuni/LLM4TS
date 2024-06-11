package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.Line;
import javax.swing.text.Line.BoundedRange;
import javax.swing.text.LineResult;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit.StyledTextAction;

import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private String term;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8);
 term = "test";
 }

 // Test constructor
 @Test
 public void testSearchableTextPaneConstruction() {
 assertNotNull(searchableTextPane);
 }

 // Test getScrollableTracksViewportWidth method
 @Test
 public void testGetScrollableTracksViewportWidth() {
 final boolean result = searchableTextPane.getScrollableTracksViewportWidth();
 assertTrue(result);
 }

 // Test highlight method with valid input
 @Test
 public void testHighlightValidInput() throws BadLocationException {
 final List<LineResult> expectedResults = Arrays.asList(
 new LineResult(0, 0, 7),
 new LineResult(1, 8, 13)
 );
 final List<LineResult> results = searchableTextPane.highlight(term, 0);
 assertEquals(expectedResults, results);
 }

 // Test highlight method with empty term
 @Test
 public void testHighlightEmptyTerm() {
 final List<LineResult> results = searchableTextPane.highlight(term, 0);
 assertEquals(0, results.size());
 }

 // Test highlight method with null term
 @Test(expected = NullPointerException.class)
 public void testHighlightNullTerm() {
 searchableTextPane.highlight(null, 0);
 }

 // Test addHighlight method with valid input
 @Test
 public void testAddHighlightValidInput() throws BadLocationException {
 final int start = 12;
 final int length = 4;
 searchableTextPane.addHighlight(start, length);
 final Highlighter highlighter = searchableTextPane.getHighlighter();
 final List<Highlighter.Highlight> highlights = highlighter.getHighlights();
 assertEquals(1, highlights.size());
 final Highlighter.Highlight highlight = highlights.get(0);
 final BoundedRange range = highlight.getBounds();
 final int actualStart = range.getStartOffset();
 final int actualEnd = range.getEndOffset();
 assertEquals(start, actualStart);
 assertEquals(start + length, actualEnd);
 }

 // Test addHighlight method with invalid input (negative length)
 @Test(expected = IllegalArgumentException.class)
 public void testAddHighlightNegativeLength() throws BadLocationException {
 final int start = 12;
 final int length = -1;
 searchableTextPane.addHighlight(start, length);
 }

 // Test removeHighlights method
 @Test
 public void testRemoveHighlights() throws BadLocationException {
 searchableTextPane.highlight(term, 0);
 searchableTextPane.removeHighlights();
 final Highlighter highlighter = searchableTextPane.getHighlighter();
 final List<Highlighter.Highlight> highlights = highlighter.getHighlights();
 assertEquals(0, highlights.size());
 }

 // Test search method with valid input and found match
 @Test
 public void testSearchValidInputFound() {
 final int expectedPos = 7;
 final int actualPos = searchableTextPane.search(term);
 assertEquals(expectedPos, actualPos);
 }

 // Test search method with valid input and not found match
 @Test
 public void testSearchValidInputNotFound() {
 final String otherTerm = "other";
 final int actualPos = searchableTextPane.search(otherTerm);
 assertEquals(-1, actualPos);
 }

 // Test search method with null term
 @Test(expected = NullPointerException.class)
 public void testSearchNullTerm() {
 searchableTextPane.search(null);
 }

 // Test search method with empty term
 @Test
 public void testSearchEmptyTerm() {
 final int actualPos = searchableTextPane.search(term);
 assertEquals(-1, actualPos);
 }

 // Test search method with invalid input (negative position)
 @Test(expected = IllegalArgumentException.class)
 public void testSearchInvalidPosition() {
 final int position = -1;
 searchableTextPane.search(term, position);
 }

 // Test convertWords2Lines method with valid input
 @Test
 public void testConvertWords2LinesValidInput() throws BadLocationException {
 final String testText = "This is a test for SearchableTextPane search functionality.";
 final SearchableTextPane textPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8);
 textPane.setText(testText);
 final List<WordResult> words = Arrays.asList(
 new WordResult(13, 15),
 new WordResult(22, 25),
 new WordResult(34, 35),
 new WordResult(38, 42),
 new WordResult(47, 51),
 new WordResult(55, 58),
 new WordResult(61, 65),
 new WordResult(69, 73),
 new WordResult(75, 78),
 new WordResult(81, 84),
 new WordResult(88, 91),
 new WordResult(93, 96),
 new WordResult(100, 103),
 new WordResult(105, 108)
 );
 final List<LineResult> expectedResults = Arrays.asList(
 new LineResult(0, 0, 13),
 new LineResult(1, 13, 22),
 new LineResult(2, 22, 34),
 new LineResult(2, 34, 38),
 new LineResult(3, 38, 42),
 new LineResult(3, 42, 47),
 new LineResult(4, 47, 51),
 new LineResult(4, 51, 55),
 new LineResult(5, 55, 58),
 new LineResult(5, 58, 61),
 new LineResult(6, 61, 65),
 new LineResult(6, 65, 69),
 new LineResult(7, 69, 73),
 new LineResult(7, 73, 75),
 new LineResult(8, 75, 78),
 new LineResult(8, 78, 81),
 new LineResult(9, 81, 84),
 new LineResult(9, 84, 88),
 new LineResult(10, 88, 91),
 new LineResult(10, 91, 93),
 new LineResult(11, 93, 96),
 new LineResult(11, 96, 100),
 new LineResult(12, 100, 103),
 new LineResult(12, 103, 105),
 new LineResult(13, 105, 108)
 );
 final List<LineResult> results = textPane.convertWords2Lines(words);
 assertEquals(expectedResults, results);
 }

 // Test convertWords2Lines method with empty words list
 @Test
 public void testConvertWords2LinesEmptyWordsList() throws BadLocationException {
 final List<LineResult> results = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8).convertWords2Lines(new ArrayList<>());
 assertEquals(0, results.size());
 }

 // Test convertWords2Lines method with null words list
 @Test(expected = NullPointerException.class)
 public void testConvertWords2LinesNullWordsList() {
 new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8).convertWords2Lines(null);
 }

 // Test create and display SearchableTextPane in JFrame
 @Test
 public void testCreateAndDisplaySearchableTextPaneInJFrame() {
 final String testText = "This is a test for SearchableTextPane search functionality.";
 final JFrame frame = new JFrame();
 final JScrollPane scrollPane = new JScrollPane(new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8));
 frame.add(scrollPane);
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.pack();
 frame.setSize(800, 600);
 frame.setVisible(true);
 final Document doc = scrollPane.getViewport().getView().getDocument();
 doc.insertString(0, testText, null);
 final List<WordResult> words = Arrays.asList(
 new WordResult(13, 15),
 new WordResult(22, 25),
 new WordResult(34, 35),
 new WordResult(38, 42),
 new WordResult(47, 51),
 new WordResult(55, 58),
 new WordResult(61, 65),
 new WordResult(69, 73),
 new WordResult(75, 78),
 new WordResult(81, 84),
 new WordResult(88, 91),
 new WordResult(93, 96),
 new WordResult(100, 103),
 new WordResult(105, 108)
 );
 final List<LineResult> expectedResults = Arrays.asList(
 new LineResult(0, 0, 13),
 new LineResult(1, 13, 22),
 new LineResult(2, 22, 34),
 new LineResult(2, 34, 38),
 new LineResult(3, 38, 42),
 new LineResult(3, 42, 47),
 new LineResult(4, 47, 51),
 new LineResult(4, 51, 55),
 new LineResult(5, 55, 58),
 new LineResult(5, 58, 61),
 new LineResult(6, 61, 65),
 new LineResult(6, 65, 69),
 new LineResult(7, 69, 73),
 new LineResult(7, 73, 75),
 new LineResult(8, 75, 78),
 new LineResult(8, 78, 81),
 new LineResult(9, 81, 84),
 new LineResult(9, 84, 88),
 new LineResult(10, 88, 91),
 new LineResult(10, 91, 93),
 new LineResult(11, 93, 96),
 new LineResult(11, 96, 100),
 new LineResult(12, 100, 103),
 new LineResult(12, 103, 105),
 new LineResult(13, 105, 108)
 );
 final List<LineResult> results = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8).convertWords2Lines(words);
 final SimpleAttributeSet tag = new SimpleAttributeSet();
 tag.addAttribute(StyledTextAction.SelectionAction.class, new Object() {});
 for (final LineResult result : results) {
 for (final WordResult wordResult : result.getWordResults()) {
 doc.setCharacterAttributes(wordResult.getStart(), wordResult.getLength(), tag, false);
 }
 }
 final StyledEditorKit editorKit = new StyledEditorKit();
 editorKit.getViewFactory().createHighlighter(doc);
 }

}

class WordResult {
 private int start;
 private int end;
 private LineResult parent;

 public WordResult(int start, int end) {
 this.start = start;
 this.end = end;
 }

 public void setLineOffset(int lineOffset) {
 parent = new LineResult(-1, -1, lineOffset, start);
 }

 public int getLineOffset() {
 return parent.lineNumber * this.getLine().getLength() + this.start;
 }

 public List<WordResult> getWordResults() {
 return Arrays.asList(this);
 }

 public int getStart() {
 return start;
 }

 public int getEnd() {
 return end;
 }

 public int length() {
 return end - start;
 }
}

class LineResult {
 private int lineNumber;
 private Line line;
 private int startOffset;

 public LineResult(int line, int startOffset, int endOffset) {
 line = new Line.BoundedRange(startOffset, endOffset);
 }

 public LineResult(int line, Line line, int startOffset, int endOffset) {
 this.line = line;
 this.lineNumber = line.getLineNumber();
 this.startOffset = startOffset;
 }

 public LineResult(int line, int startOffset, int endOffset, int offset) {
 this.lineNumber = line;
 this.startOffset = startOffset;
 this.line = new Line.BoundedRange(offset, offset + line.getLength());
 }

 public Line getLine() {
 return line;
 }

 public List<WordResult> getWordResults() {
 return new ArrayList<>();
 }

 public void addWord(WordResult wordResult) {
 ((ArrayList<WordResult>) getWordResults()).add(wordResult);
 }
}