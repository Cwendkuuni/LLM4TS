import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import ghm.follow.search.SearchableTextPane;
import ghm.follow.search.SearchEngine;
import ghm.follow.search.LineResult;
import ghm.follow.search.WordResult;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private SearchEngine searchEngine;

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 14), 8);
 searchEngine = new SearchEngine(0);
 }

 @After
 public void tearDown() {
 textPane = null;
 searchEngine = null;
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 // Test when parent is null
 assertTrue(textPane.getScrollableTracksViewportWidth());
 // Test when parent's size is smaller than text pane's width
 JFrame frame = new JFrame();
 frame.setSize(100, 100);
 frame.add(textPane);
 assertFalse(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightNullTerm() {
 List<LineResult> lineResults = textPane.highlight(null, 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightEmptyTerm() {
 String text = "This is a test for the SearchableTextPane.";
 textPane.setText(text);
 List<LineResult> lineResults = textPane.highlight("", 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightSingleWord() {
 String text = "This is a test for the SearchableTextPane.";
 textPane.setText(text);
 List<LineResult> lineResults = textPane.highlight("test", 0);
 List<WordResult> wordResults = lineResults.get(0).getWordResults();
 assertEquals(1, wordResults.size());
 assertEquals(19, wordResults.get(0).getStart());
 assertEquals(23, wordResults.get(0).getEnd());
 }

 @Test
 public void testHighlightMultipleWords() {
 String text = "This is a test for the SearchableTextPane.";
 textPane.setText(text);
 List<LineResult> lineResults = textPane.highlight("test for", 0);
 List<WordResult> wordResults = lineResults.get(0).getWordResults();
 assertEquals(2, wordResults.size());
 assertEquals(10, wordResults.get(0).getStart());
 assertEquals(15, wordResults.get(0).getEnd());
 assertEquals(18, wordResults.get(1).getStart());
 assertEquals(23, wordResults.get(1).getEnd());
 }

 @Test
 public void testHighlightCaseSensitivity() {
 String text = "This is a test for the SearchableTextPane.";
 textPane.setText(text);
 List<LineResult> lineResults = textPane.highlight("Test", 0);
 List<WordResult> wordResults = lineResults.get(0).getWordResults();
 assertEquals(0, wordResults.size());
 List<LineResult> lineResults2 = textPane.highlight("test", 0);
 List<WordResult> wordResults2 = lineResults2.get(0).getWordResults();
 assertEquals(1, wordResults2.size());
 }

 @Test
 public void testHighlightWithError() {
 textPane.setText("This is a test for the SearchableTextPane.");
 try {
 List<LineResult> lineResults = textPane.highlight(null, 0);
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testSearchNullTerm() {
 int pos = textPane.search(null);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchEmptyTerm() {
 textPane.setText("This is a test for the SearchableTextPane.");
 int pos = textPane.search("");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchSingleWord() {
 textPane.setText("This is a test for the SearchableTextPane.");
 int pos = textPane.search("test");
 assertEquals(19, pos);
 }

 @Test
 public void testSearchMultipleWords() {
 textPane.setText("This is a test for the SearchableTextPane.");
 int pos = textPane.search("test for");
 assertEquals(10, pos);
 }

 @Test
 public void testSearchWithError() {
 textPane.setText("This is a test for the SearchableTextPane.");
 try {
 int pos = textPane.search(null);
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testConvertWords2LinesEmptyList() {
 List<WordResult> words = new ArrayList<WordResult>();
 List<LineResult> lines = null;
 try {
 lines = textPane.convertWords2Lines(words);
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 assertEquals(0, lines.size());
 }

 @Test
 public void testConvertWords2LinesSingleWord() {
 String text = "This is a test for the SearchableTextPane.";
 textPane.setText(text);
 List<WordResult> words = Arrays.asList(new WordResult(19, 23, "test"));
 List<LineResult> lines = textPane.convertWords2Lines(words);
 List<WordResult> wordResults = lines.get(0).getWordResults();
 assertEquals(1, wordResults.size());
 assertEquals(19, wordResults.get(0).getStart());
 assertEquals(23, wordResults.get(0).getEnd());
 }

 @Test
 public void testConvertWords2LinesMultipleWords() {
 String text = "This is a test for the SearchableTextPane.";
 textPane.setText(text);
 List<WordResult> words = Arrays.asList(new WordResult(10, 15, "test"), new WordResult(18, 23, "for"));
 List<LineResult> lines = textPane.convertWords2Lines(words);
 List<WordResult> wordResults1 = lines.get(0).getWordResults();
 List<WordResult> wordResults2 = lines.get(1).getWordResults();
 assertEquals(2, wordResults1.size());
 assertEquals(10, wordResults1.get(0).getStart());
 assertEquals(15, wordResults1.get(0).getEnd());
 assertEquals(1, wordResults2.size());
 assertEquals(18, wordResults2.get(0).getStart());
 assertEquals(23, wordResults2.get(0).getEnd());
 }

 @Test
 public void testConvertWords2LinesWithError() {
 String text = "This is a test for the SearchableTextPane.";
 textPane.setText(text);
 List<WordResult> words = Arrays.asList(new WordResult(19, 23, "test"), new WordResult(-1, 23, "for"));
 try {
 List<LineResult> lines = textPane.convertWords2Lines(words);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

}