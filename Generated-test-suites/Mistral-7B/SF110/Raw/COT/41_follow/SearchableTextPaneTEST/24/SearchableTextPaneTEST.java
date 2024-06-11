package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

private SearchableTextPane searchableTextPane;

@Before
public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 16), 8);
}

// Test constructor
@Test
public void testSearchableTextPaneConstructor() {
 SearchableTextPane textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 16), 8);
 assertNotNull(textPane);
}

// Test getScrollableTracksViewportWidth method
@Test
public void testGetScrollableTracksViewportWidth() {
 Component parent = new JFrame();
 Component parent2 = null;
 boolean expectedResult1 = true;
 boolean expectedResult2 = false;
 JTextArea textArea = new JTextArea();
 SearchableTextPane searchableTextPane1 = new SearchableTextPane(new Font("Serif", Font.BOLD, 16), 8);
 SearchableTextPane searchableTextPane2 = new SearchableTextPane(new Font("Serif", Font.BOLD, 16), 8);

 assertEquals(expectedResult1, searchableTextPane1.getScrollableTracksViewportWidth());
 assertEquals(expectedResult2, searchableTextPane2.getScrollableTracksViewportWidth(parent));
 assertEquals(expectedResult2, searchableTextPane2.getScrollableTracksViewportWidth(parent2));
}

// Test highlight method with valid input
@Test
public void testHighlightValidInput() {
 List<LineResult> expectedResult = Arrays.asList(
 new LineResult(0, 0, 10),
 new LineResult(1, 20, 30)
 );
 searchableTextPane.setText("This is line one. This is line two.");
 searchableTextPane.highlight("line", 0);
 List<LineResult> actualResult = searchableTextPane.getHighlightedLines();
 assertEquals(expectedResult, actualResult);
}

// Test highlight method with empty input
@Test
public void testHighlightEmptyInput() {
 searchableTextPane.setText("This is a test.");
 searchableTextPane.highlight("", 0);
 List<LineResult> actualResult = searchableTextPane.getHighlightedLines();
 assertNull(actualResult);
}

// Test highlight method with null input
@Test(expected = IllegalArgumentException.class)
public void testHighlightNullInput() {
 searchableTextPane.setText("This is a test.");
 searchableTextPane.highlight(null, 0);
}

// Test search method with valid input
@Test
public void testSearchValidInput() {
 String text = "The quick brown fox jumps over the lazy dog.";
 int expectedResult = 17;
 searchableTextPane.setText(text);
 int actualResult = searchableTextPane.search("lazy");
 assertEquals(expectedResult, actualResult);
}

// Test search method with empty input
@Test
public void testSearchEmptyInput() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 int expectedResult = -1;
 int actualResult = searchableTextPane.search("");
 assertEquals(expectedResult, actualResult);
}

// Test search method with null input
@Test(expected = IllegalArgumentException.class)
public void testSearchNullInput() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 int expectedResult = -1;
 int actualResult = searchableTextPane.search(null);
 assertEquals(expectedResult, actualResult);
}

// Test search method with previous search term
@Test
public void testSearchPreviousSearchTerm() {
 String text = "The quick brown fox jumps over the lazy dog.";
 int pos = 0;
 searchableTextPane.setText(text);
 searchableTextPane.search("lazy");
 int expectedResult = 17;
 int actualResult = searchableTextPane.search("lazy");
 assertEquals(expectedResult, actualResult);
}

// Test search method with previous search term and new search term
@Test
public void testSearchPreviousSearchTermNewSearchTerm() {
 String text = "The quick brown fox jumps over the lazy dog.";
 int pos1 = 17;
 searchableTextPane.setText(text);
 searchableTextPane.search("lazy");
 int pos2 = 33;
 searchableTextPane.search("dog");
 int expectedResult = pos2;
 int actualResult = searchableTextPane.search("dog");
 assertEquals(expectedResult, actualResult);
}

// Test convertWords2Lines method with valid input
@Test
public void testConvertWords2LinesValidInput() {
 String text = "This is line one.\nThis is line two.";
 List<WordResult> wordResults = new ArrayList<>();
 wordResults.add(new WordResult(0, 7, "line"));
 wordResults.add(new WordResult(7, 11, "one"));
 wordResults.add(new WordResult(11, 15, "."));
 wordResults.add(new WordResult(15, 17, "\n"));
 wordResults.add(new WordResult(17, 21, "This"));
 wordResults.add(new WordResult(21, 25, "is"));
 wordResults.add(new WordResult(25, 29, "line"));
 wordResults.add(new WordResult(29, 32, "two"));
 wordResults.add(new WordResult(32, 35, "."));

 List<LineResult> expectedResult = Arrays.asList(
 new LineResult(0, 0, 15),
 new LineResult(1, 17, 35)
 );
 searchableTextPane.setText(text);
 List<LineResult> actualResult = searchableTextPane.convertWords2Lines(wordResults);
 assertEquals(expectedResult, actualResult);
}

// Test convertWords2Lines method with empty input
@Test
public void testConvertWords2LinesEmptyInput() {
 List<WordResult> wordResults = new ArrayList<>();
 List<LineResult> expectedResult = new ArrayList<>();
 List<LineResult> actualResult = searchableTextPane.convertWords2Lines(wordResults);
 assertNull(actualResult);
}

// Test convertWords2Lines method with null input
@Test(expected = IllegalArgumentException.class)
public void testConvertWords2LinesNullInput() {
 List<WordResult> expectedResult = new ArrayList<>();
 List<LineResult> actualResult = searchableTextPane.convertWords2Lines(null);
 assertNull(actualResult);
}

// Test addHighlight method with valid input
@Test
public void testAddHighlightValidInput() {
 searchableTextPane.setText("This is a test.");
 int start = 7;
 int length = 3;
 try {
 searchableTextPane.addHighlight(start, length);
 } catch (BadLocationException e) {
 // Should not reach here
 }
}

// Test addHighlight method with invalid input (start outside document)
@Test(expected = IndexOutOfBoundsException.class)
public void testAddHighlightInvalidInputStartOutsideDocument() {
 searchableTextPane.setText("This is a test.");
 int start = -1;
 int length = 3;
 try {
 searchableTextPane.addHighlight(start, length);
 } catch (Exception e) {
 // Assert that the correct exception was thrown
 assertEquals("Index out of bounds: Start: -1", e.getMessage());
 }
}

// Test addHighlight method with invalid input (length outside document)
@Test(expected = IndexOutOfBoundsException.class)
public void testAddHighlightInvalidInputLengthOutsideDocument() {
 searchableTextPane.setText("This is a test.");
 int start = 0;
 int length = 16;
 try {
 searchableTextPane.addHighlight(start, length);
 } catch (Exception e) {
 // Assert that the correct exception was thrown
 assertEquals("Index out of bounds: End or length: 16", e.getMessage());
 }
}

// Test removeHighlights method
@Test
public void testRemoveHighlights() {
 searchableTextPane.setText("This is a test.");
 searchableTextPane.highlight("test", 0);
 List<LineResult> highlightedLines = searchableTextPane.getHighlightedLines();
 assertFalse(highlightedLines.isEmpty());
 searchableTextPane.removeHighlights();
 highlightedLines = searchableTextPane.getHighlightedLines();
 assertTrue(highlightedLines.isEmpty());
}

}