package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Line.BoundedLine;
import javax.swing.text.LineElement;
import javax.swing.text.LineIterator;
import javax.swing.text.LineView;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit.StyledTextAction;

import org.junit.Test;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private List<LineResult> lineResults;

 @Test
 public void testSearchableTextPaneConstruction() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 assertEquals(14, searchableTextPane.getFont().getSize2D().getHeight());
 assertEquals(8, searchableTextPane.getTabSize());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
}

 @Test
 public void testHighlight() throws BadLocationException {
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 Document document = searchableTextPane.getDocument();
 document.create(0, 0, text.length(), null);
 searchableTextPane.setText(text);
 String term = "brown";
 List<WordResult> searchResults = new ArrayList<WordResult>();
 searchResults.add(new WordResult(0, 6, 6, "brown", null, null));
 searchResults.add(new WordResult(17, 21, 5, "brown", null, null));
 List<LineResult> lineResultsExpected = new ArrayList<LineResult>();
 lineResultsExpected.add(new LineResult(0, 0, text.length(), 2, Arrays.asList(new WordResult(0, 6, 6, "brown", null, null), new WordResult(17, 21, 5, "brown", null, null))));
 lineResults = searchableTextPane.highlight(term, 0);
 assertEquals(lineResultsExpected, lineResults);
 }

 @Test
 public void testSearch() {
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 Document document = searchableTextPane.getDocument();
 document.create(0, 0, text.length(), null);
 searchableTextPane.setText(text);
 String term = "brown";
 int pos = searchableTextPane.search(term);
 assertEquals(6, pos);
 term = "lazy";
 pos = searchableTextPane.search(term, 17);
 assertEquals(21, pos);
 term = "";
 pos = searchableTextPane.search(term);
 assertEquals(-1, pos);
 term = null;
 pos = searchableTextPane.search(term);
 assertEquals(-1, pos);
 }

 @Test
 public void testHighlightNullTerm() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
 assertNull(lineResults);
 }

 @Test
 public void testHighlightEmptyTerm() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 String text = "The quick brown fox jumps over the lazy dog.";
 Document document = searchableTextPane.getDocument();
 document.create(0, 0, text.length(), null);
 searchableTextPane.setText(text);
 List<LineResult> lineResults = searchableTextPane.highlight("", 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightEmptyDocument() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testAddHighlight() throws BadLocationException {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 searchableTextPane.setDocument(new PlainDocument());
 DefaultHighlighter highlighter = searchableTextPane.getHighlighter();
 SimpleAttributeSet attributeSet = new SimpleAttributeSet();
 attributeSet.addAttribute(SimpleAttributeSet.FontAttributeName, new Font("Serif", Font.BOLD, 14));
 highlighter.addHighlight(1, 1, new DefaultHighlightPainter(Color.BLUE), attributeSet);
 List<BoundedLine> lines = searchableTextPane.getLines();
 BoundedLine line = lines.get(0);
 List<LineView> lineViews = line.getLineViews();
 LineView lineView = lineViews.get(0);
 assertEquals(Color.BLUE, lineView.getBackground());
 }

 @Test
 public void testRemoveHighlights() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 DefaultHighlighter highlighter = searchableTextPane.getHighlighter();
 highlighter.addHighlight(1, 1, new DefaultHighlightPainter(Color.BLUE), null);
 highlighter.removeAllHighlights();
 List<BoundedLine> lines = searchableTextPane.getLines();
 List<LineView> lineViews = lines.get(0).getLineViews();
 assertEquals(0, lineViews.size());
 }

 @Test
 public void testConvertWords2Lines() throws BadLocationException {
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 Document document = searchableTextPane.getDocument();
 document.create(0, 0, text.length(), null);
 searchableTextPane.setText(text);
 List<WordResult> searchResults = new ArrayList<WordResult>();
 searchResults.add(new WordResult(0, 6, 6, "brown", null, null));
 searchResults.add(new WordResult(17, 21, 5, "brown", null, null));
 lineResults = searchableTextPane.convertWords2Lines(searchResults);
 int line = 0;
 for (LineResult lineResult : lineResults) {
 assertEquals(line++, lineResult.getLineNumber());
 List<WordResult> words = lineResult.getWordResults();
 for (WordResult wordResult : words) {
 assertEquals(wordResult.start, lineResult.getStartOffset() + wordResult.getLineOffset());
 assertEquals(wordResult.end, lineResult.getEndOffset());
 }
 }
 }

 @Test
 public void testUpdateWordResult() throws BadLocationException {
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 Document document = searchableTextPane.getDocument();
 document.create(0, 0, text.length(), null);
 searchableTextPane.setText(text);
 List<WordResult> searchResults = new ArrayList<WordResult>();
 searchResults.add(new WordResult(0, 6, 6, "brown", null, null));
 searchResults.add(new WordResult(17, 21, 5, "brown", null, null));
 lineResults = searchableTextPane.convertWords2Lines(searchResults);
 WordResult wordResult = searchResults.get(0);
 LineResult lineResult = lineResults.get(0);
 assertEquals(lineResult, wordResult.parent);
 }
}