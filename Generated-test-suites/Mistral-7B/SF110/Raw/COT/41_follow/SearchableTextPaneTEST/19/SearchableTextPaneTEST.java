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
 private JFrame frame;
 private Document document;

 @Before
 public void setUp() {
 try {
 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
 } catch (Exception e) {
 e.printStackTrace();
 }
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.getContentPane().add(searchableTextPane, BorderLayout.CENTER);
 frame.pack();
 frame.setVisible(true);
 document = searchableTextPane.getDocument();
 }

 @Test
 public void testSearchableTextPaneConstruction() {
 assertEquals("Wrong font set", new Font("Serif", Font.BOLD, 12), searchableTextPane.getFont());
 assertEquals("Wrong tab size set", 8, searchableTextPane.getTabSize());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue("ScrollableTracksViewportWidth not set correctly", searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyString() {
 List<LineResult> lineResults = searchableTextPane.highlight("", 0);
 assertTrue("Should not return any results for empty string", lineResults.isEmpty());
 }

 @Test
 public void testHighlightNullString() {
 List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
 assertTrue("Should not return any results for null string", lineResults.isEmpty());
 }

 @Test
 public void testHighlightSingleWord() {
 String term = "Java";
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertEquals("Wrong number of lines returned", 1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals("Wrong line number", 0, lineResult.getLineNumber());
 List<WordResult> wordResults = lineResult.getWordResults();
 assertEquals("Wrong number of words in line", 1, wordResults.size());
 WordResult wordResult = wordResults.get(0);
 assertEquals("Wrong word text", term, wordResult.getText());
 assertEquals("Wrong word start offset", document.getLength() - term.length(), wordResult.getStart());
 assertEquals("Wrong word end offset", document.getLength(), wordResult.getEnd());
 assertEquals("Wrong word line offset", 0, wordResult.getLineOffset());
 }

 @Test
 public void testHighlightMultipleWords() {
 String term = "Java Swing";
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertEquals("Wrong number of lines returned", 1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 List<WordResult> wordResults = lineResult.getWordResults();
 assertEquals("Wrong number of words in line", 2, wordResults.size());
 WordResult firstWordResult = wordResults.get(0);
 WordResult secondWordResult = wordResults.get(1);
 assertEquals("First word text", "Java", firstWordResult.getText());
 assertEquals("Second word text", "Swing", secondWordResult.getText());
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightWithException() {
 List<LineResult> lineResults = searchableTextPane.highlight("Java", -1);
 fail("Should throw BadLocationException");
 }

 @Test
 public void testRemoveHighlights() {
 List<LineResult> lineResults = searchableTextPane.highlight("Java", 0);
 searchableTextPane.removeHighlights();
 List<LineResult> lineResultsAfterRemoval = searchableTextPane.highlight("Java", 0);
 assertTrue("Highlights not removed", lineResultsAfterRemoval.isEmpty());
 }

 @Test
 public void testSearchNullTerm() {
 int position = searchableTextPane.search(null);
 assertEquals("Last search term should be null", -1, position);
 }

 @Test
 public void testSearchEmptyString() {
 int position = searchableTextPane.search("");
 assertEquals("Last search term should be empty", -1, position);
 }

 @Test
 public void testSearchSingleWord() {
 String term = "Java";
 int position = searchableTextPane.search(term);
 assertEquals("Wrong search position", document.getLength() - term.length(), position);
 }

 @Test
 public void testSearchMultipleWords() {
 String term = "Java Swing";
 int position = searchableTextPane.search(term);
 int expectedPosition = document.getLength() - term.length();
 assertEquals("Wrong search position", expectedPosition, position);
 }

 @Test
 public void testSearchPreviousTerm() {
 String term = "Java";
 int position = searchableTextPane.search(term);
 int previousPosition = searchableTextPane.search(term, position - term.length());
 int expectedPreviousPosition = position - term.length() - term.length();
 assertEquals("Wrong previous search position", expectedPreviousPosition, previousPosition);
 }

 @Test
 public void testSearchPreviousTermWithNewOne() {
 String term1 = "Java";
 int position1 = searchableTextPane.search(term1);
 String term2 = "Swing";
 int position2 = searchableTextPane.search(term2);
 int expectedPosition2 = position1 + term1.length();
 assertEquals("Wrong search position for second term", expectedPosition2, position2);
 }

 @Test
 public void testSearchPreviousTermWithSameOne() {
 String term = "Java";
 int position = searchableTextPane.search(term);
 int previousPosition = searchableTextPane.search(term, position - term.length());
 int expectedPreviousPosition = position - term.length() - term.length();
 assertEquals("Wrong previous search position", expectedPreviousPosition, previousPosition);
 }

 @Test(expected = BadLocationException.class)
 public void testSearchWithException() {
 int position = searchableTextPane.search("Java", -1);
 fail("Should throw BadLocationException");
 }
}