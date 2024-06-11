package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ghm.follow.search.SearchableTextPane;
import ghm.follow.search.SearchEngine;
import ghm.follow.search.Utilities;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private JFrame frame;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 4);
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.add(new JScrollPane(searchableTextPane));
 frame.pack();
 frame.setVisible(true);
 }

 @After
 public void tearDown() {
 SwingUtilities.invokeLater(() -> {
 try {
 frame.dispose();
 } catch (Exception e) {
 e.printStackTrace();
 }
 });
 }

 @Test
 public void testSearchableTextPaneConstructor() {
 assertNotNull(searchableTextPane);
 assertEquals(new Font("Arial", Font.BOLD, 14), searchableTextPane.getFont());
 assertEquals(4, searchableTextPane.getTabSize());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyTerm() {
 searchableTextPane.setText("This is a test for SearchableTextPane.");
 List<LineResult> lineResults = searchableTextPane.highlight("", 0);
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightSingleWord() {
 searchableTextPane.setText("This is a test for SearchableTextPane.");
 List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(22, lineResult.getStartOffset());
 assertEquals(28, lineResult.getEndOffset());
 }

 @Test
 public void testHighlightMultipleWords() {
 searchableTextPane.setText("This is a test for SearchableTextPane.");
 List<LineResult> lineResults = searchableTextPane.highlight("test test test", 0);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(22, lineResult.getStartOffset());
 assertEquals(34, lineResult.getEndOffset());
 }

 @Test
 public void testHighlightMultipleLines() {
 searchableTextPane.setText("Line 1: This is a test.\nLine 2: For SearchableTextPane.");
 List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
 assertNotNull(lineResults);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(1, lineResult1.getLineNumber());
 assertEquals(5, lineResult1.getStartOffset());
 assertEquals(11, lineResult1.getEndOffset());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(2, lineResult2.getLineNumber());
 assertEquals(31, lineResult2.getStartOffset());
 assertEquals(35, lineResult2.getEndOffset());
 }

 @Test
 public void testHighlightMultipleWordsMultipleLines() {
 searchableTextPane.setText("Line 1: This is a test.\nLine 2: For SearchableTextPane.");
 List<LineResult> lineResults = searchableTextPane.highlight("test test test", 0);
 assertNotNull(lineResults);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(1, lineResult1.getLineNumber());
 assertEquals(5, lineResult1.getStartOffset());
 assertEquals(11, lineResult1.getEndOffset());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(2, lineResult2.getLineNumber());
 assertEquals(31, lineResult2.getStartOffset());
 assertEquals(35, lineResult2.getEndOffset());
 }

 @Test
 public void testSearchEmptyTerm() {
 searchableTextPane.setText("This is a test for SearchableTextPane.");
 assertEquals(-1, searchableTextPane.search(""));
 assertNull(searchableTextPane.lastSearchTerm);
 }

 @Test
 public void testSearchSingleWord() {
 searchableTextPane.setText("This is a test for SearchableTextPane.");
 assertEquals(22, searchableTextPane.search("test"));
 assertEquals("test", searchableTextPane.lastSearchTerm);
 }

 @Test
 public void testSearchMultipleWords() {
 searchableTextPane.setText("This is a test for SearchableTextPane.");
 assertEquals(34, searchableTextPane.search("test test"));
 assertEquals("test test", searchableTextPane.lastSearchTerm);
 }

 @Test
 public void testSearchMultipleWordsMultipleLines() {
 searchableTextPane.setText("Line 1: This is a test.\nLine 2: For SearchableTextPane.");
 assertEquals(31, searchableTextPane.search("test test"));
 assertEquals("test test", searchableTextPane.lastSearchTerm);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchNegativeStartPosition() {
 searchableTextPane.setText("This is a test for SearchableTextPane.");
 searchableTextPane.search("test", -1);
 }

 @Test
 public void testRemoveHighlights() {
 searchableTextPane.setText("This is a test for SearchableTextPane.");
 List<LineResult> lineResults = searchableTextPane.highlight("test", 0);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 searchableTextPane.removeHighlights();
 assertNull(lineResult.getHighlights());
 }

 @Test
 public void testConvertWords2LinesEmptyList() {
 List<WordResult> emptyList = Collections.emptyList();
 List<LineResult> lineResults = SearchableTextPane.convertWords2Lines(emptyList);
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testConvertWords2LinesSingleWord() {
 List<WordResult> wordResults = Arrays.asList(new WordResult(10, 15, "test"));
 List<LineResult> lineResults = SearchableTextPane.convertWords2Lines(wordResults);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(10, lineResult.getStartOffset());
 assertEquals(15, lineResult.getEndOffset());
 assertEquals(Arrays.asList(wordResults.get(0)), lineResult.getWordResults());
 }

 @Test
 public void testConvertWords2LinesMultipleWordsMultipleLines() {
 List<WordResult> wordResults = Arrays.asList(
 new WordResult(5, 10, "test"),
 new WordResult(25, 28, "test"),
 new WordResult(45, 48, "test")
 );
 List<LineResult> lineResults = SearchableTextPane.convertWords2Lines(wordResults);
 assertNotNull(lineResults);
 assertEquals(3, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(1, lineResult1.getLineNumber());
 assertEquals(5, lineResult1.getStartOffset());
 assertEquals(10, lineResult1.getEndOffset());
 assertEquals(Arrays.asList(wordResults.get(0)), lineResult1.getWordResults());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(2, lineResult2.getLineNumber());
 assertEquals(25, lineResult2.getStartOffset());
 assertEquals(28, lineResult2.getEndOffset());
 assertEquals(Arrays.asList(wordResults.get(1)), lineResult2.getWordResults());
 LineResult lineResult3 = lineResults.get(2);
 assertEquals(3, lineResult3.getLineNumber());
 assertEquals(45, lineResult3.getStartOffset());
 assertEquals(48, lineResult3.getEndOffset());
 assertEquals(Arrays.asList(wordResults.get(2)), lineResult3.getWordResults());
 }
}