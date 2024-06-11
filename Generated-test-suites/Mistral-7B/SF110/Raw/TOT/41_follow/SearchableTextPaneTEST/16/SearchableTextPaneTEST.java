package ghm.follow.search;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private static final String TEST_TERM = "JUnit";
 private static final int TEST_TAB_SIZE = 4;
 private static final Font TEST_FONT = new Font("Serif", Font.PLAIN, 12);

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(TEST_FONT, TEST_TAB_SIZE);
 }

 @After
 public void tearDown() {
 textPane = null;
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 // Test when text pane is not a part of a parent component
 assertFalse(textPane.getScrollableTracksViewportWidth());
 // Test when text pane has a parent component with a smaller width than preferred size
 JFrame frame = new JFrame();
 frame.getContentPane().add(textPane);
 frame.setSize(100, 100);
 frame.setVisible(true);
 assertFalse(textPane.getScrollableTracksViewportWidth());
 // Test when text pane has a parent component with a larger width than preferred size
 frame.setSize(200, 100);
 assertTrue(textPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlight() throws BadLocationException {
 // Test empty search term
 List<LineResult> lineResults = textPane.highlight(null, 0);
 assertEquals(0, lineResults.size());
 // Test search term with no occurrences
 textPane.setText("This text pane does not contain the term JUnit.");
 lineResults = textPane.highlight(TEST_TERM, 0);
 assertEquals(0, lineResults.size());
 // Test search term with one occurrence
 textPane.setText("This text pane contains the JUnit framework.");
 lineResults = textPane.highlight(TEST_TERM, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(11, lineResult.getEndOffset());
 // Test search term with multiple occurrences
 textPane.setText("This text pane contains the JUnit framework multiple times.");
 lineResults = textPane.highlight(TEST_TERM, 0);
 assertEquals(2, lineResults.size());
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightWithError() throws BadLocationException {
 textPane.highlight(TEST_TERM, 100);
 }

 @Test
 public void testSearch() {
 // Test search term not found
 assertEquals(-1, textPane.search(TEST_TERM));
 // Test search term found
 textPane.setText("This text pane contains the JUnit framework.");
 assertEquals(4, textPane.search(TEST_TERM));
 // Test search term found at the beginning of the text
 textPane.setText("The JUnit framework is used for testing Java code.");
 assertEquals(0, textPane.search(TEST_TERM));
 // Test search term found at the end of the text
 textPane.setText("This text pane contains the JUnit framework at the end.");
 assertEquals(16, textPane.search(TEST_TERM));
 // Test search term found in the middle of the text
 textPane.setText("The JUnit framework is used for testing Java code in the middle.");
 assertEquals(10, textPane.search(TEST_TERM));
 }

 @Test(expected = BadLocationException.class)
 public void testSearchWithError() throws BadLocationException {
 textPane.search(TEST_TERM, 100);
 }

 @Test
 public void testConvertWords2Lines() throws BadLocationException {
 String text = "This text pane contains line 1 with the term JUnit,\n" +
 "line 2 with the term JUnit multiple times,\n" +
 "and line 3 without the term JUnit.";
 textPane.setText(text);
 List<WordResult> words = Arrays.asList(
 new WordResult(0, 11, "JUnit"),
 new WordResult(15, 20, "JUnit"),
 new WordResult(33, 37, "JUnit"),
 new WordResult(42, 46, "multiple"),
 new WordResult(47, 51, "times"),
 new WordResult(54, 56, "without")
 );
 List<LineResult> lineResults = textPane.convertWords2Lines(words);
 assertEquals(3, lineResults.size());
 LineResult line1 = lineResults.get(0);
 LineResult line2 = lineResults.get(1);
 LineResult line3 = lineResults.get(2);
 assertEquals(1, line1.getLineNumber());
 assertEquals(0, line1.getStartOffset());
 assertEquals(23, line1.getEndOffset());
 assertEquals(1, line2.getLineNumber());
 assertEquals(21, line2.getStartOffset());
 assertEquals(45, line2.getEndOffset());
 assertEquals(3, line3.getLineNumber());
 assertEquals(53, line3.getStartOffset());
 assertEquals(56, line3.getEndOffset());
 }

 @Test(expected = BadLocationException.class)
 public void testConvertWords2LinesWithError() throws BadLocationException {
 textPane.convertWords2Lines(null);
 }
}