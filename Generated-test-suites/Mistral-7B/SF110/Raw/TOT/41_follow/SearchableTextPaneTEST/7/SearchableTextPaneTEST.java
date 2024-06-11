package ghm.follow.search;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private static final String SEARCH_TERM = "Java";

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 12), 4);
 textPane.setText("This is a Java text pane where we can perform searches.\n" +
 "Java is a popular programming language.\n" +
 "It has many features that make it great for developing applications.\n" +
 "Searching in a text pane is an essential feature.");
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 // Test when the text pane is smaller than the parent component
 JFrame frame = new JFrame();
 frame.setPreferredSize(new Dimension(200, 400));
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.add(textPane, 0, 0);
 frame.pack();
 frame.setVisible(true);
 assertTrue(textPane.getScrollableTracksViewportWidth());

 // Test when the text pane is larger than or equal to the parent component
 JFrame frame2 = new JFrame();
 frame2.setPreferredSize(new Dimension(600, 400));
 frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame2.add(textPane, 0, 0);
 frame2.pack();
 frame2.setVisible(true);
 assertTrue(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightNullTerm() {
 List<LineResult> results = textPane.highlight(null, 0);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testHighlightEmptyTerm() {
 List<LineResult> results = textPane.highlight("", 0);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testHighlightValidTerm() {
 List<LineResult> results = textPane.highlight(SEARCH_TERM, 0);
 assertEquals(3, results.size());
 // Check if the highlights are in the correct positions
 for (LineResult lineResult : results) {
 List<WordResult> wordResults = lineResult.getWordResults();
 assertEquals(2, wordResults.size());
 int start = wordResults.get(0).start;
 int end = wordResults.get(0).end;
 int line = textPane.getLineOfOffset(start);
 int lineStart = textPane.getLineStartOffset(line);
 int lineEnd = textPane.getLineEndOffset(line);
 assertEquals(lineStart + 8, start); // "Java" starts at the 9th character
 assertEquals(lineStart + 13, end);
 int nextWordStart = wordResults.get(1).start;
 assertEquals(lineStart + 15, nextWordStart); // "programming" starts after "Java"
 }
 }

 @Test
 public void testHighlightTermAlreadyHighlighted() {
 List<LineResult> results = textPane.highlight(SEARCH_TERM, 0);
 results = textPane.highlight(SEARCH_TERM, 0);
 assertEquals(3, results.size());
 }

 @Test
 public void testHighlightTermNotPresent() {
 List<LineResult> results = textPane.highlight("C++", 0);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testRemoveHighlights() {
 List<LineResult> results = textPane.highlight(SEARCH_TERM, 0);
 textPane.removeHighlights();
 List<LineResult> results2 = textPane.highlight(SEARCH_TERM, 0);
 assertTrue(results2.isEmpty());
 }

 @Test
 public void testSearchNullTerm() {
 int pos = textPane.search(null);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchEmptyTerm() {
 int pos = textPane.search("");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchValidTerm() {
 int pos = textPane.search(SEARCH_TERM);
 assertEquals(2, pos);
 }

 @Test
 public void testSearchTermAlreadyFound() {
 int pos = textPane.search(SEARCH_TERM);
 int nextPos = textPane.search(SEARCH_TERM, pos + SEARCH_TERM.length());
 assertEquals(3, nextPos);
 }

 @Test
 public void testSearchTermNotPresent() {
 int pos = textPane.search("C++");
 assertEquals(-1, pos);
 }

 @Test
 public void testConvertWords2LinesEmptyList() throws BadLocationException {
 List<WordResult> words = new ArrayList<>();
 List<LineResult> lines = textPane.convertWords2Lines(words);
 assertTrue(lines.isEmpty());
 }

 @Test
 public void testConvertWords2LinesValidList() throws BadLocationException {
 List<WordResult> words = new ArrayList<>();
 words.add(new WordResult(10, 12, "Java", 0));
 words.add(new WordResult(25, 28, "language", 0));
 words.add(new WordResult(33, 35, "Text", 0));
 words.add(new WordResult(39, 44, "Pane", 0));
 List<LineResult> lines = textPane.convertWords2Lines(words);
 assertEquals(1, lines.size());
 LineResult line = lines.get(0);
 assertEquals(0, line.lineNumber);
 assertEquals(10, line.getLineStart());
 assertEquals(45, line.getLineEnd());
 List<WordResult> wordResults = line.getWordResults();
 assertEquals(4, wordResults.size());
 assertEquals("Java", wordResults.get(0).getWord());
 assertEquals(12, wordResults.get(0).start);
 assertEquals(13, wordResults.get(0).end);
 assertEquals(1, wordResults.get(0).lineNumber);
 assertEquals("language", wordResults.get(1).getWord());
 assertEquals(25, wordResults.get(1).start);
 assertEquals(28, wordResults.get(1).end);
 assertEquals(1, wordResults.get(1).lineNumber);
 assertEquals("Text", wordResults.get(2).getWord());
 assertEquals(39, wordResults.get(2).start);
 assertEquals(42, wordResults.get(2).end);
 assertEquals(1, wordResults.get(2).lineNumber);
 assertEquals("Pane", wordResults.get(3).getWord());
 assertEquals(43, wordResults.get(3).start);
 assertEquals(44, wordResults.get(3).end);
 assertEquals(1, wordResults.get(3).lineNumber);
 }
}