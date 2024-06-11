import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private List<LineResult> lineResults;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 4);
 }

 @Test
 public void testSearchableTextPaneConstruction() {
 assertNotNull(searchableTextPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 final JFrame frame = new JFrame();
 frame.getContentPane().add(searchableTextPane);
 frame.pack();
 assertEquals(frame.getWidth() > searchableTextPane.getPreferredSize().width, searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyTerm() {
 searchableTextPane.highlight("", 0);
 assertNotNull(searchableTextPane.getHighlighter().getHighlights());
 assertEquals(0, searchableTextPane.getHighlighter().getHighlights().length);
 }

 @Test
 public void testHighlightValidTerm() {
 String text = "This is a SearchableTextPane. It can search for words.";
 searchableTextPane.setText(text);
 searchableTextPane.highlight("SearchableTextPane", 0);
 List<LineResult> expectedLineResults = new ArrayList<LineResult>();
 LineResult expectedLineResult = new LineResult(0, 0, text.length());
 expectedLineResult.addWord(new WordResult(24, 35, 12));
 expectedLineResult.addWord(new WordResult(37, 47, 10));
 expectedLineResults.add(expectedLineResult);
 lineResults = searchableTextPane.highlight("SearchableTextPane", 0);
 assertEquals(expectedLineResults, lineResults);
 }

 @Test
 public void testHighlightValidTermWithFlags() {
 String text = "This is a SearchableTextPane. It can search for words.";
 searchableTextPane.setText(text);
 searchableTextPane.highlight("SearchableTextPane", 0, SearchEngine.REGEX_FLAG);
 lineResults = searchableTextPane.highlight("SearchableTextPane", 0);
 assertEquals(expectedLineResults, lineResults);
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightNullTerm() {
 searchableTextPane.highlight(null, 0);
 }

 @Test
 public void testSearchEmptyTerm() {
 assertEquals(-1, searchableTextPane.search(""));
 }

 @Test
 public void testSearchValidTerm() {
 String text = "This is a SearchableTextPane. It can search for words.";
 searchableTextPane.setText(text);
 assertEquals(24, searchableTextPane.search("SearchableTextPane"));
 }

 @Test
 public void testSearchValidTermWithPreviousSearch() {
 String text = "This is a SearchableTextPane. It can search for words.";
 searchableTextPane.setText(text);
 searchableTextPane.search("SearchableTextPane");
 assertEquals(37, searchableTextPane.search("TextPane"));
 }

 @Test
 public void testSearchNullTerm() {
 assertEquals(-1, searchableTextPane.search(null));
 }

 @Test
 public void testConvertWords2Lines() {
 String text = "Line 1: This is a SearchableTextPane.\nLine 2: It can search for words.";
 searchableTextPane.setText(text);
 List<WordResult> wordResults = Arrays.asList(
 new WordResult(0, 13, 12),
 new WordResult(14, 19, 5),
 new WordResult(20, 26, 6),
 new WordResult(27, 30, 4),
 new WordResult(31, 38, 7),
 new WordResult(39, 43, 4),
 new WordResult(44, 47, 3),
 new WordResult(48, 49, 2),
 new WordResult(50, 51, 1)
 );
 lineResults = searchableTextPane.convertWords2Lines(wordResults);
 LineResult expectedLineResult1 = new LineResult(0, 0, 13);
 LineResult expectedLineResult2 = new LineResult(1, 14, 38);
 expectedLineResults.add(expectedLineResult1);
 expectedLineResults.add(expectedLineResult2);
 assertEquals(expectedLineResults, lineResults);
 }
}