package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private JTextComponent textComponent;
 private Highlighter.Highlight highlighter;
 private final String term = "Java";

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 24), 4);
 textComponent = new JScrollPane(searchableTextPane);
 JFrame frame = new JFrame();
 frame.getContentPane().add(textComponent);
 frame.pack();
 frame.setVisible(true);
 SwingUtilities.invokeLater(() -> searchableTextPane.setText("This is a Java text pane."));
 textComponent.requestFocus();
 highlighter = searchableTextPane.getHighlighter();
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlight_emptyString() {
 List<LineResult> lineResults = searchableTextPane.highlight("", 0);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlight_nullTerm() {
 List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlight_emptyTextPane() {
 searchableTextPane.setText("");
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlight_singleTerm() {
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffsetInLine());
 assertEquals(term.length(), lineResult.getEndOffsetInLine() - lineResult.getStartOffsetInLine());
 assertFalse(highlighter.getHighlights().isEmpty());
 Highlight highlight = highlighter.getHighlights().get(0);
 assertEquals(Color.YELLOW, highlight.getPainter().getColor());
 }

 @Test
 public void testHighlight_multipleTerms() {
 List<LineResult> lineResults = searchableTextPane.highlight(term + " " + term, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 int lastOffset = 0;
 for (WordResult wordResult : lineResult.getWordResults()) {
 if (term.equals(wordResult.getWord())) {
 assertEquals(lastOffset + term.length(), wordResult.getStartOffset());
 lastOffset = wordResult.getEndOffset();
 }
 }
 assertEquals(term.length() * 2, lastOffset);
 assertFalse(highlighter.getHighlights().isEmpty());
 }

 @Test
 public void testSearch_nullTerm() {
 int pos = searchableTextPane.search(null);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearch_emptyTextPane() {
 int pos = searchableTextPane.search("");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearch_emptyTerm() {
 int pos = searchableTextPane.search("");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearch_foundTerm() {
 int pos = searchableTextPane.search(term);
 assertEquals(0, pos);
 }

 @Test
 public void testSearch_notFoundTerm() {
 int pos = searchableTextPane.search("C++");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearch_previousTerm() {
 searchableTextPane.setText("This is a Java text pane.\nThis is a Java text pane.");
 int pos = searchableTextPane.search(term);
 int previousPos = searchableTextPane.search(term, pos - term.length());
 assertEquals(pos - term.length(), previousPos);
 }

 @Test
 public void testSearch_nextTerm() {
 searchableTextPane.setText("This is a Java text pane.\nThis is a Java text pane.");
 int pos = searchableTextPane.search(term);
 int nextPos = searchableTextPane.search(term, pos + term.length());
 assertEquals(pos + term.length(), nextPos);
 }

 @Test
 public void testSearch_sameTerm() {
 searchableTextPane.setText("This is a Java text pane.\nThis is a Java text pane.\nThis is a Java text pane.");
 int pos = searchableTextPane.search(term);
 int nextPos = searchableTextPane.search(term, pos + term.length());
 assertEquals(pos, nextPos);
 }

 @Test
 public void testConvertWords2Lines_emptyList() {
 List<WordResult> wordResults = Collections.emptyList();
 List<LineResult> lineResults = searchableTextPane.convertWords2Lines(wordResults);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testConvertWords2Lines_singleWord() {
 List<WordResult> wordResults = Collections.singletonList(new WordResult("Java", 0, 5));
 List<LineResult> lineResults = searchableTextPane.convertWords2Lines(wordResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffsetInLine());
 assertEquals(5, lineResult.getEndOffsetInLine() - lineResult.getStartOffsetInLine());
 }

 @Test
 public void testConvertWords2Lines_multipleWords() {
 List<WordResult> wordResults = List.of(
 new WordResult("Java", 0, 5),
 new WordResult("text", 7, 11),
 new WordResult("pane", 14, 18)
 );
 List<LineResult> lineResults = searchableTextPane.convertWords2Lines(wordResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 List<WordResult> expectedWordResults = List.of(
 new WordResult("Java", 0, 5),
 new WordResult("text", 7, 11),
 new WordResult("pane", 14, 18)
 );
 List<WordResult> actualWordResults = lineResult.getWordResults();
 for (int i = 0; i < expectedWordResults.size(); i++) {
 WordResult expectedWordResult = expectedWordResults.get(i);
 WordResult actualWordResult = actualWordResults.get(i);
 assertEquals(expectedWordResult.getWord(), actualWordResult.getWord());
 assertEquals(expectedWordResult.getStartOffset(), actualWordResult.getStartOffset());
 assertEquals(expectedWordResult.getEndOffset(), actualWordResult.getEndOffset());
 }
 }

 @Test
 public void testRemoveHighlights() {
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertFalse(highlighter.getHighlights().isEmpty());
 searchableTextPane.removeHighlights();
 assertTrue(highlighter.getHighlights().isEmpty());
 }

 @Test(expected = BadLocationException.class)
 public void testSearch_invalidOffset() {
 searchableTextPane.search(term, Integer.MIN_VALUE);
 }

 @Test(expected = BadLocationException.class)
 public void testSearch_invalidOffset2() {
 searchableTextPane.search(term, Integer.MAX_VALUE);
 }

 @Test(expected = BadLocationException.class)
 public void testConvertWords2Lines_invalidOffset() throws BadLocationException {
 List<WordResult> wordResults = List.of(new WordResult("Java", 0, 5));
 List<LineResult> lineResults = searchableTextPane.convertWords2Lines(wordResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 int invalidOffset = -1;
 lineResult.getWord(invalidOffset);
 }
}