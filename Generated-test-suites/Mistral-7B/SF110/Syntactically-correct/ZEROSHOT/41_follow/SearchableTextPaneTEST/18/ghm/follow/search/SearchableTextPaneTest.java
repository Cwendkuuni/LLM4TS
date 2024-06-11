import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.JTextComponent;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private JFrame frame;

 @Before
 public void setUp() {
 final String term = "test";
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 searchableTextPane.setText("This is a test SearchableTextPane. Please search for the term 'test'.\n" +
 "It should be highlighted when you search for it.");
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 final JScrollPane scrollPane = new JScrollPane(searchableTextPane);
 frame.add(scrollPane);
 frame.pack();
 frame.setVisible(true);
 SwingUtilities.invokeLater(() -> searchableTextPane.highlight(term, 0));
 }

 @After
 public void tearDown() {
 SwingUtilities.invokeLater(() -> searchableTextPane.removeHighlights());
 frame.dispose();
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyString() {
 List<LineResult> lineResults = searchableTextPane.highlight("", 0);
 assertNull(lineResults);
 }

 @Test
 public void testHighlightNullTerm() {
 List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
 assertNull(lineResults);
 }

 @Test
 public void testHighlightTermNotFound() throws BadLocationException {
 List<LineResult> lineResults = searchableTextPane.highlight("notfound", 0);
 assertNull(lineResults);
 }

 @Test
 public void testHighlightTermFound() throws InterruptedException, BadLocationException {
 List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(82, lineResult.getEndOffset());
 Highlighter highlighter = searchableTextPane.getHighlighter();
 Point point = new Point(10, 10);
 highlightAtCursor(highlighter, point);
 assertEquals(Color.YELLOW, highlighter.getPaint(highlighter.getHitPoint()));
 }

 @Test
 public void testSearchEmptyString() {
 int result = searchableTextPane.search("");
 assertNull(result);
 }

 @Test
 public void testSearchNullTerm() {
 int result = searchableTextPane.search(null);
 assertNull(result);
 }

 @Test
 public void testSearchTermNotFound() {
 int result = searchableTextPane.search("notfound");
 assertNull(result);
 }

 @Test
 public void testSearchTermFound() {
 int result = searchableTextPane.search("test");
 assertEquals(21, result);
 }

 @Test
 public void testSearchTermFoundTwice() {
 int result1 = searchableTextPane.search("test");
 int result2 = searchableTextPane.search("test");
 assertEquals(21, result1);
 assertEquals(42, result2);
 }

 @Test
 public void testConvertWords2Lines() throws BadLocationException {
 List<WordResult> wordResults = Arrays.asList(
 new WordResult(0, 2, 3, "test1", null),
 new WordResult(31, 34, 35, "test2", null),
 new WordResult(43, 48, 49, "test3", null),
 new WordResult(50, 59, 60, "test4", null),
 new WordResult(71, 78, 79, "test5", null),
 new WordResult(80, 87, 88, "test6", null)
 );
 List<LineResult> lineResults = searchableTextPane.convertWords2Lines(wordResults);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getStartOffset());
 assertEquals(88, lineResult1.getEndOffset());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(1, lineResult2.getLineNumber());
 assertEquals(50, lineResult2.getStartOffset());
 assertEquals(88, lineResult2.getEndOffset());
 }

 private void highlightAtCursor(Highlighter highlighter, Point point) throws BadLocationException {
 Document doc = searchableTextPane.getDocument();
 int offset = doc.viewToModel(point);
 highlighter.addHighlight(offset, 0, new DefaultHighlighter.DefaultHighlightPainter(Color.RED));
 }

}