package ghm.follow.search;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.*;

import org.junit.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private static final String TEST_TERM = "JUnit";

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 }

 @Test
 public void testSearchableTextPaneConstruction() {
 assertNotNull(searchableTextPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 JFrame frame = new JFrame();
 JTextPane textPane = new JTextPane();
 frame.getContentPane().add(textPane);
 frame.pack();
 frame.setVisible(true);
 textPane.setViewportView(searchableTextPane);
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 frame.dispose();
 }

 @Test
 public void testHighlightEmptyString() {
 searchableTextPane.highlight("", 0);
 assertEquals(0, searchableTextPane.getHighlighter().getHighlightCount());
 }

 @Test
 public void testHighlightNullTerm() {
 searchableTextPane.highlight(null, 0);
 assertEquals(0, searchableTextPane.getHighlighter().getHighlightCount());
 }

 @Test
 public void testHighlightSingleWord() {
 String text = "This is a JUnit test for SearchableTextPane";
 searchableTextPane.setText(text);
 List<LineResult> lineResults = searchableTextPane.highlight(TEST_TERM, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(TEST_TERM, wordResult.getWord());
 assertEquals(TEST_TERM.length(), wordResult.getLength());
 assertEquals(23, lineResult.getLineNumber());
 assertEquals(5, lineResult.getStartOffset());
 assertEquals(34, lineResult.getEndOffset());
 }

 @Test
 public void testHighlightMultipleWords() {
 String text = "This is a JUnit test for SearchableTextPane. The SearchableTextPane is a custom JTextArea that supports searching for text.";
 searchableTextPane.setText(text);
 List<LineResult> lineResults = searchableTextPane.highlight(TEST_TERM, 0);
 assertEquals(2, lineResults.size());
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightWithBadLocationException() {
 searchableTextPane.highlight(TEST_TERM, -1);
 }

 @Test
 public void testSearchEmptyString() {
 assertEquals(-1, searchableTextPane.search(""));
 }

 @Test
 public void testSearchNullTerm() {
 assertEquals(-1, searchableTextPane.search(null));
 }

 @Test
 public void testSearchSingleWord() {
 searchableTextPane.setText("This is a JUnit test for SearchableTextPane");
 int pos = searchableTextPane.search(TEST_TERM);
 assertEquals(10, pos);
 }

 @Test
 public void testSearchMultipleWords() {
 searchableTextPane.setText("This is a JUnit test for SearchableTextPane. The SearchableTextPane is a custom JTextArea that supports searching for text.");
 int pos = searchableTextPane.search(TEST_TERM);
 assertEquals(10, pos);
 int pos2 = searchableTextPane.search(TEST_TERM, pos + TEST_TERM.length());
 assertEquals(25, pos2);
 }

 @Test
 public void testSearchPreviousTerm() {
 searchableTextPane.setText("This is a JUnit test for SearchableTextPane. The SearchableTextPane is a custom JTextArea that supports searching for text.");
 searchableTextPane.search("SearchableTextPane");
 int pos = searchableTextPane.search("TextPane");
 assertEquals(25, pos);
 }

 @Test
 public void testSearchTermNotFound() {
 searchableTextPane.setText("This is a JUnit test for SearchableTextPane. The SearchableTextPane is a custom JTextArea that supports searching for text.");
 int pos = searchableTextPane.search("TextEditor");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchTermPartiallyFound() {
 searchableTextPane.setText("This is a JUnit test for SearchableTextPane. The SearchableTextPane is a custom JTextArea that supports searching for text.");
 int pos = searchableTextPane.search("Searchable");
 assertEquals(0, pos);
 }

 @Test(expected = BadLocationException.class)
 public void testSearchWithBadLocationException() {
 searchableTextPane.search("", 1);
 }

}