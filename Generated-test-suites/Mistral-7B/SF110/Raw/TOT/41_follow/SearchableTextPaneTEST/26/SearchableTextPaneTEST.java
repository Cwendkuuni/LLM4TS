package ghm.follow.search;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private List<WordResult> searchResults;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 searchResults = new ArrayList<>();
 }

 @Test
 public void testSearchableTextPaneConstruction() {
 assertNotNull(searchableTextPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidthReturnsTrue() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyTermReturnsEmptyList() throws Exception {
 searchableTextPane.highlight("", 0);
 assertEquals(Collections.emptyList(), searchableTextPane.getHighlights());
 }

 @Test
 public void testHighlightSingleWord() throws Exception {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 searchResults = Arrays.asList(new WordResult(0, 3, "The"), new WordResult(11, 5, "brown"), new WordResult(17, 5, "jumps"), new WordResult(22, 5, "over"), new WordResult(27, 4, "the"), new WordResult(31, 3, "lazy"), new WordResult(34, 4, "dog."));
 when(searchResults.get(0)).thenReturn(searchResults.get(0));
 when(searchResults.get(1)).thenReturn(searchResults.get(1));
 when(searchResults.get(2)).thenReturn(searchResults.get(2));
 when(searchResults.get(3)).thenReturn(searchResults.get(3));
 when(searchResults.get(4)).thenReturn(searchResults.get(4));
 when(searchResults.get(5)).thenReturn(searchResults.get(5));
 when(searchResults.get(6)).thenReturn(searchResults.get(6));

 searchableTextPane.setText(searchableTextPane.getText());
 searchableTextPane.highlight("brown", 0);
 List<LineResult> lineResults = searchableTextPane.getHighlights();
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(11, lineResult.getStartOffset());
 assertEquals(16, lineResult.getEndOffset());
 assertTrue(lineResult.isHighlighted());
 }

 @Test
 public void testHighlightMultiWord() throws Exception {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 searchResults = Arrays.asList(new WordResult(0, 3, "The"), new WordResult(11, 5, "brown"), new WordResult(17, 5, "jumps"), new WordResult(22, 5, "over"), new WordResult(27, 4, "the"), new WordResult(31, 3, "lazy"), new WordResult(34, 4, "dog."));
 when(searchResults.get(0)).thenReturn(searchResults.get(0));
 when(searchResults.get(1)).thenReturn(searchResults.get(1));
 when(searchResults.get(2)).thenReturn(searchResults.get(2));
 when(searchResults.get(3)).thenReturn(searchResults.get(3));
 when(searchResults.get(4)).thenReturn(searchResults.get(4));
 when(searchResults.get(5)).thenReturn(searchResults.get(5));
 when(searchResults.get(6)).thenReturn(searchResults.get(6));

 searchableTextPane.setText(searchableTextPane.getText());
 searchableTextPane.highlight("jumps over", 0);
 List<LineResult> lineResults = searchableTextPane.getHighlights();
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getLineNumber());
 assertEquals(17, lineResult.getStartOffset());
 assertEquals(22, lineResult.getEndOffset());
 assertTrue(lineResult.isHighlighted());
 }

 @Test
 public void testHighlightInvalidSearchTermReturnsEmptyList() throws Exception {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 searchResults = Arrays.asList(new WordResult(0, 3, "The"), new WordResult(11, 5, "brown"), new WordResult(17, 5, "jumps"), new WordResult(22, 5, "over"), new WordResult(27, 4, "the"), new WordResult(31, 3, "lazy"), new WordResult(34, 4, "dog."));
 when(searchResults.get(0)).thenReturn(searchResults.get(0));
 when(searchResults.get(1)).thenReturn(searchResults.get(1));
 when(searchResults.get(2)).thenReturn(searchResults.get(3));
 when(searchResults.get(3)).thenReturn(searchResults.get(4));
 when(searchResults.get(4)).thenReturn(searchResults.get(5));
 when(searchResults.get(5)).thenReturn(searchResults.get(6));
 when(searchResults.get(6)).thenReturn(null);

 searchableTextPane.setText(searchableTextPane.getText());
 searchableTextPane.highlight("invalid", 0);
 assertEquals(Collections.emptyList(), searchableTextPane.getHighlights());
 }

 @Test
 public void testSearchReturnsCorrectPositionForValidSearchTerm() throws Exception {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 int expectedPosition = 11;
 int actualPosition = searchableTextPane.search("brown");
 assertEquals(expectedPosition, actualPosition);
 }

 @Test
 public void testSearchReturnsCorrectPositionForValidSearchTermStartingFromANewLine() throws Exception {
 searchableTextPane.setText("The quick brown fox\njumps over the lazy dog.");
 int expectedPosition = 11;
 int actualPosition = searchableTextPane.search("brown");
 assertEquals(expectedPosition, actualPosition);
 }

 @Test
 public void testSearchReturnsCorrectPositionForValidSearchTermStartingFromTheMiddleOfALine() throws Exception {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 int expectedPosition = 17;
 int actualPosition = searchableTextPane.search("jumps over", 11);
 assertEquals(expectedPosition, actualPosition);
 }

 @Test
 public void testSearchReturnsCorrectPositionForValidSearchTermStartingFromTheEndOfALine() throws Exception {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 int expectedPosition = 34;
 int actualPosition = searchableTextPane.search("dog.", 30);
 assertEquals(expectedPosition, actualPosition);
 }

 @Test
 public void testSearchReturnsCorrectPositionForValidSearchTermStartingFromTheEndOfTheText() throws Exception {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 int expectedPosition = -1;
 int actualPosition = searchableTextPane.search("zebra", searchableTextPane.getText().length());
 assertEquals(expectedPosition, actualPosition);
 }

 @Test
 public void testSearchReturnsCorrectPositionForInvalidSearchTerm() throws Exception {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 int expectedPosition = -1;
 int actualPosition = searchableTextPane.search("invalid", 0);
 assertEquals(expectedPosition, actualPosition);
 }

 @Test
 public void testSearchReturnsCorrectPositionForSearchTermStartingFromTheMiddleOfAnInvalidWord() throws Exception {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 int expectedPosition = 11;
 int actualPosition = searchableTextPane.search("invalidbrown", 0);
 assertEquals(expectedPosition, actualPosition);
 }

 @Test
 public void testSearchReturnsCorrectPositionForSearchTermStartingFromTheMiddleOfAnInvalidWordAndMiddleOfALine() throws Exception {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 int expectedPosition = 18;
 int actualPosition = searchableTextPane.search("invalidover", 11);
 assertEquals(expectedPosition, actualPosition);
 }

 @Test
 public void testSearchReturnsCorrectPositionForSearchTermStartingFromTheEndOfAnInvalidWordAndEndOfALine() throws Exception {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 int expectedPosition = 35;
 int actualPosition = searchableTextPane.search("invaliddog.", 31);
 assertEquals(expectedPosition, actualPosition);
 }
}