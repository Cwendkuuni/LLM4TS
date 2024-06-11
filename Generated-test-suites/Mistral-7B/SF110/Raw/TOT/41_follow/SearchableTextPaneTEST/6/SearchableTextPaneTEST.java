import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.*;

import ghm.follow.search.SearchableTextPane;
import ghm.follow.search.SearchEngine;
import ghm.follow.search.LineResult;
import ghm.follow.search.WordResult;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private Logger log;

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 4);
 textPane.setBackground(Color.WHITE);
 log = Logger.getLogger(SearchableTextPane.class.getName());
 }

 @Test
 public void testSearchableTextPaneCreation() {
 assertEquals("Unexpected font", new Font("Serif", Font.BOLD, 12), textPane.getFont());
 assertEquals("Unexpected tab size", 4, textPane.getTabSize());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 final JFrame frame = new JFrame();
 frame.add(textPane);
 frame.setSize(300, 200);
 frame.setVisible(true);
 assertTrue("Should track viewport width", textPane.getScrollableTracksViewportWidth());
 frame.dispose();
 }

 @Test
 public void testHighlightEmptyTerm() {
 textPane.setText("This is a test text pane.");
 textPane.highlight("", SearchEngine.REGULAR);
 assertEquals("No highlights expected", 0, textPane.getDocument().getLength());
 }

 @Test
 public void testHighlightSingleWord() {
 textPane.setText("This is a test text pane.");
 List<LineResult> lineResults = textPane.highlight("text", SearchEngine.REGULAR);
 assertEquals("Unexpected number of line results", 1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals("Unexpected line number", 0, lineResult.getLineNumber());
 assertEquals("Unexpected start offset", 24, lineResult.getStartOffset());
 assertEquals("Unexpected end offset", 31, lineResult.getEndOffset());
 }

 @Test
 public void testHighlightMultipleWords() {
 textPane.setText("This is a test text pane with multiple words and different colors.");
 List<LineResult> lineResults = textPane.highlight("multiple words", SearchEngine.REGULAR);
 assertEquals("Unexpected number of line results", 1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals("Unexpected line number", 1, lineResult.getLineNumber());
 List<WordResult> wordResults = lineResult.getWordResults();
 assertEquals("Unexpected number of words", 3, wordResults.size());
 WordResult word1 = wordResults.get(0);
 assertEquals("Unexpected word start", 31, word1.getStart());
 assertEquals("Unexpected word end", 37, word1.getEnd());
 WordResult word2 = wordResults.get(1);
 assertEquals("Unexpected word start", 45, word2.getStart());
 assertEquals("Unexpected word end", 51, word2.getEnd());
 WordResult word3 = wordResults.get(2);
 assertEquals("Unexpected word start", 58, word3.getStart());
 assertEquals("Unexpected word end", 67, word3.getEnd());
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightWithBadLocationException() {
 textPane.setText("This is a test text pane.");
 textPane.highlight("exception", SearchEngine.REGULAR);
 }

 @Test
 public void testSearchEmptyTerm() {
 textPane.setText("This is a test text pane.");
 assertEquals("Unexpected search position", -1, textPane.search(""));
 }

 @Test
 public void testSearchSingleWord() {
 textPane.setText("This is a test text pane.");
 assertEquals("Unexpected search position", 24, textPane.search("text"));
 }

 @Test
 public void testSearchMultipleWords() {
 textPane.setText("This is a test text pane with multiple words and different colors.");
 assertEquals("Unexpected search position", 31, textPane.search("multiple words"));
 }

 @Test
 public void testSearchCaseSensitive() {
 textPane.setText("This is a test text pane with multiple words and different colors.");
 assertEquals("Unexpected search position", 31, textPane.search("Multiple words"));
 }

 @Test
 public void testSearchCaseInsensitive() {
 textPane.setText("This is a test text pane with multiple words and different colors.");
 assertEquals("Unexpected search position", 31, textPane.search("multiple words"), 0);
 assertEquals("Unexpected search position", -1, textPane.search("Multiple words"), 1);
 }

 @Test
 public void testSearchWithBadLocationException() {
 textPane.setText("This is a test text pane.");
 assertEquals("Unexpected search position", -1, textPane.search("exception"), 0);
 assertEquals("Unexpected search position", -1, textPane.search("exception"), 1);
 }

 @Test
 public void testSearchWithPreviousTerm() {
 textPane.setText("This is a test text pane with multiple words and different colors.");
 textPane.search("multiple words");
 assertEquals("Unexpected search position", 31, textPane.search("multiple words"));
 textPane.search("Multiple words");
 assertEquals("Unexpected search position", 31, textPane.search("multiple words"), 0);
 textPane.search("Multiple words");
 assertEquals("Unexpected search position", 58, textPane.search("multiple words"), 1);
 }
}