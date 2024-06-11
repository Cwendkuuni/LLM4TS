package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.Searchable;
import javax.swing.text.SearchMatcher;
import javax.swing.text.SearchPattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private JFrame frame;

 @Before
 public void setUp() {
 final Font font = new Font("Serif", Font.PLAIN, 12);
 textPane = new SearchableTextPane(font, 8);
 frame = new JFrame();
 frame.getContentPane().add(new JScrollPane(textPane));
 frame.pack();
 frame.setVisible(true);
 SwingUtilities.invokeLater(() -> textPane.setText("The quick brown fox jumps over the lazy dog."));
 }

 @After
 public void tearDown() {
 SwingUtilities.invokeLater(() -> frame.dispose());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlight() {
 final String term = "the";
 final int flags = Searchable.REGULAR_EXPRESSION;
 final List<LineResult> lineResults = textPane.highlight(term, flags);
 assertEquals(2, lineResults.size());
 final LineResult firstLineResult = lineResults.get(0);
 final LineResult secondLineResult = lineResults.get(1);
 final int firstLineNumber = firstLineResult.getLineNumber();
 final int secondLineNumber = secondLineResult.getLineNumber();
 assertEquals(1, firstLineResult.getWordResults().size());
 assertEquals(1, secondLineResult.getWordResults().size());
 assertEquals(4, firstLineResult.getWordResults().get(0).getStart());
 assertEquals(7, firstLineResult.getWordResults().get(0).getEnd());
 assertEquals(8, firstLineNumber);
 assertEquals(11, secondLineResult.getWordResults().get(0).getStart());
 assertEquals(14, secondLineResult.getWordResults().get(0).getEnd());
 assertEquals(2, secondLineNumber);

 SwingUtilities.invokeLater(() -> textPane.getHighlighter().removeAllHighlights());
 }

 @Test
 public void testHighlightEmptySearchTerm() {
 final List<LineResult> lineResults = textPane.highlight("", Searchable.REGULAR_EXPRESSION);
 assertFalse(lineResults.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHighlightNullSearchTerm() {
 textPane.highlight(null, Searchable.REGULAR_EXPRESSION);
 }

 @Test
 public void testSearch() {
 final String term = "the";
 final int position = textPane.search(term);
 assertTrue(position != -1);
 assertEquals(7, position);

 final String term2 = "lazy";
 final int position2 = textPane.search(term2);
 assertTrue(position2 != -1);
 assertEquals(18, position2);

 final String term3 = "zebra";
 final int position3 = textPane.search(term3);
 assertNull(position3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchNullSearchTerm() {
 textPane.search(null);
 }

 @Test
 public void testRemoveHighlights() {
 textPane.setText("The quick brown fox jumps over the lazy dog.");
 textPane.highlight("the", Searchable.REGULAR_EXPRESSION);
 textPane.removeHighlights();
 SwingUtilities.invokeLater(() -> {
 try {
 final Document doc = textPane.getDocument();
 final Element element = doc.getDefaultRootElement();
 final int length = element.getLength();
 final int offset = doc.getLength();
 final Highlighter highlighter = textPane.getHighlighter();
 for (int i = 0; i < length; i++) {
 final Object attribute = element.getAttributes().getAttribute(Highlighter.HighlightPaintAttribute);
 if (attribute != null && attribute instanceof HighlightPainter) {
 highlighter.removeHighlight(i, length - i);
 }
 }
 }
 catch (BadLocationException e) {
 e.printStackTrace();
 }
 });
 }
}

class LineResult {
 private int lineNumber;
 private int lineStart;
 private int lineEnd;
 private List<WordResult> wordResults;

 public LineResult(int lineNumber, int lineStart, int lineEnd) {
 this.lineNumber = lineNumber;
 this.lineStart = lineStart;
 this.lineEnd = lineEnd;
 this.wordResults = new ArrayList<>();
 }

 public int getLineNumber() {
 return lineNumber;
 }

 public int getLineStart() {
 return lineStart;
 }

 public int getLineEnd() {
 return lineEnd;
 }

 public void addWord(WordResult wordResult) {
 this.wordResults.add(wordResult);
 }

 public List<WordResult> getWordResults() {
 return this.wordResults;
 }
}

class WordResult {
 private int start;
 private int end;
 private int lineNumber;
 private Element parent;

 public WordResult(int start, int end) {
 this.start = start;
 this.end = end;
 this.parent = null;
 }

 public int getStart() {
 return start;
 }

 public int getEnd() {
 return end;
 }

 public int getLineNumber() {
 return lineNumber;
 }

 public Element getParent() {
 return parent;
 }

 public void setLineOffset(int lineOffset) {
 this.lineNumber = -1;
 this.parent = Utilities.getParagraphElement(textPane, start);
 this.lineNumber = parent.getLineNumber();
 }
}