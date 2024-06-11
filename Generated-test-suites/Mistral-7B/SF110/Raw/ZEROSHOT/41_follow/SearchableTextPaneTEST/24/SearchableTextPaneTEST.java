package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
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
 searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.add(searchableTextPane, java.awt.BorderLayout.CENTER);
 frame.pack();
 frame.setVisible(true);
 SwingUtilities.invokeLater(() -> searchableTextPane.setText("This is a searchable text pane with yellow highlights."));
 }

 @After
 public void tearDown() {
 SwingUtilities.invokeLater(() -> frame.dispose());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightNullTerm() {
 List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightEmptyTerm() {
 List<LineResult> lineResults = searchableTextPane.highlight("", 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightSingleWord() {
 List<LineResult> lineResults = searchableTextPane.highlight("follow", 0);
 assertEquals(1, lineResults.size());
 SearchEngine engine = new SearchEngine(SearchEngine.FLAG_CASE_SENSITIVE);
 List<WordResult> wordResults = engine.search("follow", searchableTextPane.getText());
 assertEquals(wordResults.size(), lineResults.get(0).getWordResults().size());
 }

 @Test
 public void testHighlightMultipleWords() {
 List<LineResult> lineResults = searchableTextPane.highlight("this is a searchable text pane", 0);
 assertEquals(1, lineResults.size());
 SearchEngine engine = new SearchEngine(SearchEngine.FLAG_CASE_SENSITIVE);
 List<WordResult> wordResults = engine.search("this is a searchable text pane", searchableTextPane.getText());
 assertEquals(wordResults.size(), lineResults.get(0).getWordResults().size());
 }

 @Test
 public void testSearchNullTerm() {
 int index = searchableTextPane.search(null);
 assertEquals(-1, index);
 }

 @Test
 public void testSearchEmptyTerm() {
 int index = searchableTextPane.search("");
 assertEquals(-1, index);
 }

 @Test
 public void testSearchSingleWord() {
 int index = searchableTextPane.search("follow");
 int expectedIndex = searchableTextPane.getText().indexOf("follow", 0);
 assertEquals(expectedIndex, index);
 }

 @Test
 public void testSearchMultipleWords() {
 int index = searchableTextPane.search("this is a searchable text pane");
 int expectedIndex = searchableTextPane.getText().indexOf("this is a searchable text pane", 0);
 assertEquals(expectedIndex, index);
 }

 @Test
 public void testSearchPreviousTerm() {
 searchableTextPane.setText("This is a searchable text pane with yellow highlights.\nThis is a test to search for the word 'follow'.");
 searchableTextPane.highlight("follow", 0);
 int index = searchableTextPane.search("follow");
 int expectedIndex = searchableTextPane.getText().indexOf("follow", index);
 assertEquals(expectedIndex, index);
 }

 @Test
 public void testSearchPreviousTermNotFound() {
 searchableTextPane.setText("This is a searchable text pane with yellow highlights.\nThis is a test to search for the word 'follow'.");
 int index = searchableTextPane.search("follow");
 index = searchableTextPane.search("follow", index);
 int expectedIndex = searchableTextPane.getText().indexOf("follow");
 assertEquals(-1, index);
 }

 @Test
 public void testSearchCaseSensitive() {
 searchableTextPane.setText("This is a searchable text pane with yellow highlights.\nThis is a test to search for the word 'Follow'.");
 searchableTextPane.highlight("Follow", 0);
 int index = searchableTextPane.search("Follow");
 int expectedIndex = searchableTextPane.getText().indexOf("Follow", 0);
 assertEquals(expectedIndex, index);
 }

 @Test
 public void testSearchCaseInsensitive() {
 searchableTextPane.setText("This is a searchable text pane with yellow highlights.\nThis is a test to search for the word 'Follow'.");
 searchableTextPane.highlight("Follow", 0);
 int index = searchableTextPane.search("follow");
 int expectedIndex = searchableTextPane.getText().indexOf("Follow", 0);
 assertEquals(expectedIndex, index);
 }

 @Test
 public void testRemoveHighlights() {
 searchableTextPane.highlight("follow", 0);
 List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
 assertEquals(0, lineResults.size());
 }
}