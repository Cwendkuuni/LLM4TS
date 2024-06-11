package ghm.follow.search;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.PlainDocument;
import javax.swing.text.Searchably;
import javax.swing.text.SearchEngine;
import javax.swing.text.Searchable;
import javax.swing.text.SearchResult;
import javax.swing.text.TextAction;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private SearchEngine searchEngine;
 private Highlighter.Factory highlightFactory;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 14), 8);
 searchEngine = mock(SearchEngine.class);
 highlightFactory = JTextPane.getDefaultHighlighterFactory();
 searchableTextPane.setHighlighter(highlightFactory);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 final JTextArea parent = mock(JTextArea.class);
 when(parent.getSize()).thenReturn(new Dimension(10, 10));
 searchableTextPane.setParent(parent);
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());

 final JTextPane parentTextPane = mock(JTextPane.class);
 when(parentTextPane.getUI()).thenReturn(new PlainTextUI());
 searchableTextPane.setParent(parentTextPane);
 assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlight() throws BadLocationException {
 final String term = "test";
 final List<WordResult> searchResults = Arrays.asList(
 new WordResult(2, 4, 8),
 new WordResult(6, 10, 13),
 new WordResult(10, 15, 18));
 final List<LineResult> lineResults = Arrays.asList(
 new LineResult(1, 1, 20),
 new LineResult(2, 21, 40));
 final List<SearchResult> searchResultsFromEngine = Arrays.asList(
 new SearchResult(0, 2, 11),
 new SearchResult(0, 6, 7),
 new SearchResult(0, 10, 9));
 when(searchEngine.search(term, "testText")).thenReturn(searchResultsFromEngine);

 searchableTextPane.setDocument(new PlainDocument());
 searchableTextPane.setText("testText");
 SwingUtilities.invokeLater(new Runnable() {
 public void run() {
 searchableTextPane.highlight(term, SearchableTextPane.SEARCH_CASE_SENSITIVE | SearchableTextPane.SEARCH_REGULAR_EXPRESSION);
 }
 });

 assertEquals(2, searchableTextPane.getHighlightCount());
 HighlightPainter painter = highlightFactory.getPaint("Search");
 List<Highlight> highlights = searchableTextPane.getHighlighter().getHighlights();
 for (int i = 0; i < highlights.size(); i++) {
 assertEquals(painter, highlights.get(i).getPainter());
 }
 }

 @Test
 public void testSearch() throws BadLocationException {
 final String term = "test";
 final int startPos = 0;
 final int lastSearchPos = 10;
 when(searchEngine.search(term, "testText")).thenReturn(Arrays.asList(
 new SearchResult(lastSearchPos, lastSearchPos + term.length(), term.length()),
 new SearchResult(20, 21, term.length())));

 assertEquals(lastSearchPos, searchableTextPane.search(term));
 assertEquals(lastSearchPos + term.length(), searchableTextPane.search(term, startPos));
 assertNull(searchableTextPane.search(null));
 assertNull(searchableTextPane.search(""));
 }

 @Test
 public void testConvertWords2Lines() throws BadLocationException {
 final String text = "Line 1: This is the first line.\nLine 2: This is the second line.\nLine 3: This is the third line.";
 final Document document = new PlainDocument();
 document.setText(text);
 searchableTextPane.setDocument(document);
 searchableTextPane.setText(text);
 final List<WordResult> words = Arrays.asList(
 new WordResult(0, 18, 18, "Line"),
 new WordResult(19, 23, 5, "1:"),
 new WordResult(24, 27, 4, "This"),
 new WordResult(28, 32, 4, "is"),
 new WordResult(33, 37, 4, "the"),
 new WordResult(38, 41, 4, "first"),
 new WordResult(42, 45, 3, "line"),
 new WordResult(46, 48, 2, "."),
 new WordResult(49, 53, 4, "Line"),
 new WordResult(54, 58, 4, "2:"),
 new WordResult(59, 63, 4, "This"),
 new WordResult(64, 68, 4, "is"),
 new WordResult(73, 77, 4, "the"),
 new WordResult(82, 85, 3, "second"),
 new WordResult(86, 89, 3, "line"),
 new WordResult(90, 93, 3, "."),
 new WordResult(94, 98, 4, "Line"),
 new WordResult(99, 103, 4, "3:"),
 new WordResult(104, 108, 4, "This"),
 new WordResult(109, 113, 4, "is"),
 new WordResult(114, 118, 4, "the"),
 new WordResult(123, 126, 3, "third"),
 new WordResult(127, 130, 3, "line"),
 new WordResult(131, 134, 3, "."));

 final List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
 final List<LineResult> expectedLineResults = Arrays.asList(
 new LineResult(1, 1, 72),
 new LineResult(2, 73, 78),
 new LineResult(3, 79, 85),
 new LineResult(3, 86, 89),
 new LineResult(3, 90, 93),
 new LineResult(3, 94, 98),
 new LineResult(3, 99, 103),
 new LineResult(3, 104, 108),
 new LineResult(3, 109, 113),
 new LineResult(3, 114, 118),
 new LineResult(3, 119, 123),
 new LineResult(3, 124, 127),
 new LineResult(3, 128, 131),
 new LineResult(3, 132, 135));
 assertEquals(expectedLineResults, lineResults);
 }
}

class WordResult {
 static class LineResult {
 int lineNumber;
 int lineStart;
 int lineEnd;
 List<WordResult> wordResults;

 LineResult(int lineNumber, int lineStart, int lineEnd) {
 this.lineNumber = lineNumber;
 this.lineStart = lineStart;
 this.lineEnd = lineEnd;
 this.wordResults = new ArrayList<WordResult>();
 }

 void addWord(WordResult wordResult) {
 wordResult.parent = this;
 this.wordResults.add(wordResult);
 }
 }

 int start;
 int end;
 int length;
 int line;
 int lineOffset;
 Object parent;

 WordResult(int start, int end, int length, Object parent) {
 this.start = start;
 this.end = end;
 this.length = length;
 this.parent = parent;
 }

 int getLineOfOffset(int offset) {
 int line = 0;
 int lineStart = 0;
 int docEnd = this.searchableTextPane.getDocument().getLength();
 while (offset > lineStart) {
 Element element = this.searchableTextPane.getDocument().getElement(offset);
 if (element instanceof SimpleAttributeSet) {
 SimpleAttributeSet attributeSet = (SimpleAttributeSet) element;
 if (attributeSet.getAttribute(Searchable.TextAttribute) != null) {
 line++;
 lineStart = offset;
 }
 }
 offset--;
 }
 return line;
 }

 int getLineStartOffset(int line) {
 if (line < 0 || line >= this.searchableTextPane.getLineCount()) {
 throw new IndexOutOfBoundsException();
 }
 Element element = this.searchableTextPane.getDocument().getElement(this.searchableTextPane.getLineStartOffset(line));
 return element.getStartOffset();
 }
}

class SearchEngine implements SearchEngine {
 @Override
 public List<SearchResult> search(String pattern, String text) {
 return new ArrayList<SearchResult>();
 }
}