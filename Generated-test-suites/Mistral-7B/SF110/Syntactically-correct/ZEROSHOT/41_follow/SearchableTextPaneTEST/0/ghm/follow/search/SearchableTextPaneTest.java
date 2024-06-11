package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private List<LineResult> lineResults;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 8);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.setSize(400, 400);
 frame.add(searchableTextPane);
 frame.setVisible(true);

 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyTerm() {
 searchableTextPane.setText("This is an empty test text.");
 searchableTextPane.highlight(null, 0);
 List<Component> components = searchableTextPane.getComponents(0);
 assertTrue(components.isEmpty());
 }

 @Test
 public void testHighlight() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 String term = "the";
 List<WordResult> searchResults = Arrays.asList(
 new WordResult(3, 11),
 new WordResult(11, 15)
 );
 lineResults = searchableTextPane.highlight(term, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getLineStart());
 assertEquals(17, lineResult.getLineEnd());
 List<WordResult> wordResults = lineResult.getWordResults();
 assertEquals(2, wordResults.size());
 WordResult wordResult1 = wordResults.get(0);
 assertEquals(11, wordResult1.getStart());
 assertEquals(13, wordResult1.getEnd());
 assertEquals("the", wordResult1.getText());
 WordResult wordResult2 = wordResults.get(1);
 assertEquals(15, wordResult2.getStart());
 assertEquals(17, wordResult2.getEnd());
 assertEquals("the", wordResult2.getText());
 }

 @Test
 public void testSearch() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 int pos = searchableTextPane.search("the");
 assertEquals(3, pos);
 pos = searchableTextPane.search("fox");
 assertEquals(5, pos);
 pos = searchableTextPane.search("lazy");
 assertEquals(13, pos);
 pos = searchableTextPane.search("elephant");
 assertEquals(-1, pos);
 }

 @Test
 public void testConvertWords2Lines() {
 searchableTextPane.setText("Line 1: The quick brown fox jumps\nLine 2: over the lazy dog.");
 List<WordResult> words = Arrays.asList(
 new WordResult(0, 1, "Line", 1),
 new WordResult(4, 5, "quick", 1),
 new WordResult(6, 12, "brown", 1),
 new WordResult(12, 13, "fox", 1),
 new WordResult(13, 15, "jumps", 1),
 new WordResult(16, 17, "over", 1),
 new WordResult(18, 20, "the", 2),
 new WordResult(21, 24, "lazy", 2),
 new WordResult(25, 28, "dog", 2)
 );
 lineResults = searchableTextPane.convertWords2Lines(words);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(1, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getLineStart());
 assertEquals(28, lineResult1.getLineEnd());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(2, lineResult2.getLineNumber());
 assertEquals(0, lineResult2.getLineStart());
 assertEquals(28, lineResult2.getLineEnd());
 List<WordResult> wordResults1 = lineResult1.getWordResults();
 List<WordResult> wordResults2 = lineResult2.getWordResults();
 assertEquals(9, wordResults1.size());
 assertEquals("Line", wordResults1.get(0).getText());
 assertEquals("the", wordResults1.get(8).getText());
 assertEquals("dog", wordResults2.get(0).getText());
 }
}