package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.text.*;

import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private List<WordResult> searchResults;
 private List<LineResult> lineResults;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 }

 // Test constructor
 @Test
 public void testSearchableTextPaneConstructor() {
 assertNotNull(searchableTextPane);
 }

 // Test getScrollableTracksViewportWidth method
 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 // Test highlight method - empty search term
 @Test
 public void testHighlightEmptySearchTerm() {
 searchableTextPane.highlight("", 0);
 assertEquals(0, searchableTextPane.getDocument().getLength());
 }

 // Test highlight method - null search term
 @Test(expected = IllegalArgumentException.class)
 public void testHighlightNullSearchTerm() {
 searchableTextPane.highlight(null, 0);
 }

 // Test highlight method - basic functionality
 @Test
 public void testHighlightBasicFunctionality() {
 final String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 searchResults = new ArrayList<>();
 searchResults.add(new WordResult("the", 0, 3, null));
 searchResults.add(new WordResult("quick", 6, 6, null));
 searchResults.add(new WordResult("brown", 10, 6, null));
 searchResults.add(new WordResult("fox", 16, 3, null));
 lineResults = searchableTextPane.highlight("fox", 0);
 assertEquals(1, lineResults.size());
 assertEquals(15, lineResults.get(0).getStartOffset());
 assertEquals(18, lineResults.get(0).getEndOffset());
 }

 // Test highlight method - multiple search terms
 @Test
 public void testHighlightMultipleSearchTerms() {
 final String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 searchResults = new ArrayList<>();
 searchResults.add(new WordResult("the", 0, 3, null));
 searchResults.add(new WordResult("quick", 6, 6, null));
 searchResults.add(new WordResult("brown", 10, 6, null));
 searchResults.add(new WordResult("fox", 16, 3, null));
 searchResults.add(new WordResult("lazy", 34, 5, null));
 searchResults.add(new WordResult("dog", 39, 3, null));
 lineResults = searchableTextPane.highlight("fox lazy dog", 0);
 assertEquals(1, lineResults.size());
 assertEquals(13, lineResults.get(0).getStartOffset());
 assertEquals(43, lineResults.get(0).getEndOffset());
 }

 // Test highlight method - edge cases
 @Test
 public void testHighlightEdgeCases() {
 final String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 searchResults = new ArrayList<>();
 searchResults.add(new WordResult("the", 0, 3, null));
 searchResults.add(new WordResult("quick", 6, 6, null));
 searchResults.add(new WordResult("brown", 10, 6, null));
 searchResults.add(new WordResult("fox", 16, 3, null));
 searchResults.add(new WordResult("lazy", 34, 5, null));
 searchResults.add(new WordResult("dog", 39, 3, null));
 // Empty text after highlighting
 searchableTextPane.highlight("", 0);
 assertEquals(0, searchableTextPane.getDocument().getLength());
 // Search term at beginning of text
 lineResults = searchableTextPane.highlight("the", 0);
 assertEquals(1, lineResults.size());
 assertEquals(0, lineResults.get(0).getStartOffset());
 assertEquals(3, lineResults.get(0).getEndOffset());
 // Search term at end of text
 lineResults = searchableTextPane.highlight("dog", 0);
 assertEquals(1, lineResults.size());
 assertEquals(39, lineResults.get(0).getStartOffset());
 assertEquals(43, lineResults.get(0).getEndOffset());
 // Search term in middle of text
 lineResults = searchableTextPane.highlight("lazy", 0);
 assertEquals(1, lineResults.size());
 assertEquals(34, lineResults.get(0).getStartOffset());
 assertEquals(39, lineResults.get(0).getEndOffset());
 }

 // Test search method - empty search term
 @Test
 public void testSearchEmptySearchTerm() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 assertEquals(-1, searchableTextPane.search(""));
 }

 // Test search method - null search term
 @Test(expected = IllegalArgumentException.class)
 public void testSearchNullSearchTerm() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 searchableTextPane.search(null);
 }

 // Test search method - basic functionality
 @Test
 public void testSearchBasicFunctionality() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 assertEquals(3, searchableTextPane.search("the"));
 assertEquals(6, searchableTextPane.search("quick"));
 assertEquals(10, searchableTextPane.search("brown"));
 assertEquals(16, searchableTextPane.search("fox"));
 assertEquals(34, searchableTextPane.search("lazy"));
 assertEquals(39, searchableTextPane.search("dog"));
 }

 // Test search method - multiple search terms
 @Test
 public void testSearchMultipleSearchTerms() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 assertEquals(3, searchableTextPane.search("the"));
 assertEquals(6, searchableTextPane.search("quick"));
 assertEquals(10, searchableTextPane.search("brown"));
 assertEquals(16, searchableTextPane.search("fox"));
 assertEquals(34, searchableTextPane.search("lazy"));
 assertEquals(39, searchableTextPane.search("dog"));
 assertEquals(-1, searchableTextPane.search("cat"));
 }

 // Test search method - edge cases
 @Test
 public void testSearchEdgeCases() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 assertEquals(-1, searchableTextPane.search(""));
 assertEquals(-1, searchableTextPane.search(null));
 assertEquals(0, searchableTextPane.search("the"));
 assertEquals(6, searchableTextPane.search("quick"));
 assertEquals(10, searchableTextPane.search("brown"));
 assertEquals(16, searchableTextPane.search("fox"));
 assertEquals(34, searchableTextPane.search("lazy"));
 assertEquals(39, searchableTextPane.search("dog"));
 assertEquals(-1, searchableTextPane.search("fox quick")); // Multiple search terms without separator
 assertEquals(-1, searchableTextPane.search("quick fox")); // Multiple search terms in different order
 }

 // Test convertWords2Lines method - empty list
 @Test
 public void testConvertWords2LinesEmptyList() {
 List<WordResult> emptyWordResults = new ArrayList<>();
 lineResults = searchableTextPane.convertWords2Lines(emptyWordResults);
 assertEquals(0, lineResults.size());
 }

 // Test convertWords2Lines method - null list
 @Test(expected = IllegalArgumentException.class)
 public void testConvertWords2LinesNullList() {
 lineResults = searchableTextPane.convertWords2Lines(null);
 }

 // Test convertWords2Lines method - basic functionality
 @Test
 public void testConvertWords2LinesBasicFunctionality() {
 final String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 searchResults = new ArrayList<>();
 searchResults.add(new WordResult("the", 0, 3, null));
 searchResults.add(new WordResult("quick", 6, 6, null));
 searchResults.add(new WordResult("brown", 10, 6, null));
 searchResults.add(new WordResult("fox", 16, 3, null));
 searchResults.add(new WordResult("lazy", 34, 5, null));
 searchResults.add(new WordResult("dog", 39, 3, null));
 lineResults = searchableTextPane.convertWords2Lines(searchResults);
 assertEquals(1, lineResults.size());
 assertEquals(15, lineResults.get(0).getStartOffset());
 assertEquals(43, lineResults.get(0).getEndOffset());
 }

 // Test convertWords2Lines method - multiple lines
 @Test
 public void testConvertWords2LinesMultipleLines() {
 final String text = "Line 1: The quick brown fox jumps over the lazy dog.\n" +
                    "Line 2: A swift fox can run up to 45 miles per hour.";
 searchableTextPane.setText(text);
 searchResults = new ArrayList<>();
 searchResults.add(new WordResult("the", 1, 3, null));
 searchResults.add(new WordResult("quick", 6, 6, null));
 searchResults.add(new WordResult("brown", 11, 6, null));
 searchResults.add(new WordResult("fox", 17, 3, null));
 searchResults.add(new WordResult("lazy", 41, 5, null));
 searchResults.add(new WordResult("dog", 46, 3, null));
 searchResults.add(new WordResult("swift", 49, 5, null));
 searchResults.add(new WordResult("can", 54, 3, null));
 searchResults.add(new WordResult("run", 57, 3, null));
 searchResults.add(new WordResult("up", 60, 2, null));
 searchResults.add(new WordResult("to", 62, 2, null));
 searchResults.add(new WordResult("45", 64, 2, null));
 searchResults.add(new WordResult("miles", 66, 5, null));
 lineResults = searchableTextPane.convertWords2Lines(searchResults);
 assertEquals(2, lineResults.size());
 assertEquals(17, lineResults.get(0).getStartOffset());
 assertEquals(69, lineResults.get(0).getEndOffset());
 assertEquals(13, lineResults.get(1).getStartOffset());
 assertEquals(68, lineResults.get(1).getEndOffset());
 }

 // Test convertWords2Lines method - edge cases
 @Test
 public void testConvertWords2LinesEdgeCases() {
 final String text = "Line 1: The quick brown fox jumps over the lazy dog.\n" +
                    "Line 2: A swift fox can run up to 45 miles per hour.";
 searchableTextPane.setText(text);
 searchResults = new ArrayList<>();
 searchResults.add(new WordResult("the", 1, 3, null));
 searchResults.add(new WordResult("quick", 6, 6, null));
 searchResults.add(new WordResult("brown", 11, 6, null));
 searchResults.add(new WordResult("fox", 17, 3, null));
 searchResults.add(new WordResult("lazy", 41, 5, null));
 searchResults.add(new WordResult("dog", 46, 3, null));
 searchResults.add(new WordResult("swift", 49, 5, null));
 searchResults.add(new WordResult("can", 54, 3, null));
 searchResults.add(new WordResult("run", 57, 3, null));
 searchResults.add(new WordResult("up", 60, 2, null));
 searchResults.add(new WordResult("to", 62, 2, null));
 searchResults.add(new WordResult("45", 64, 2, null));
 searchResults.add(new WordResult("miles", 66, 5, null));
 // Empty text after convertWords2Lines
 lineResults = searchableTextPane.convertWords2Lines(Collections.emptyList());
 assertEquals(0, lineResults.size());
 // No search results
 lineResults = searchableTextPane.convertWords2Lines(new ArrayList<>());
 assertEquals(0, lineResults.size());
 // Search results at beginning of text
 lineResults = searchableTextPane.convertWords2Lines(searchResults.subList(0, 1));
 assertEquals(1, lineResults.size());
 assertEquals(1, lineResults.get(0).getStartOffset());
 assertEquals(4, lineResults.get(0).getEndOffset());
 // Search results at end of text
 lineResults = searchableTextPane.convertWords2Lines(searchResults.subList(searchResults.size() - 1, searchResults.size()));
 assertEquals(1, lineResults.size());
 assertEquals(72, lineResults.get(0).getStartOffset());
 assertEquals(77, lineResults.get(0).getEndOffset());
 }

 // Test addHighlight method - empty text
 @Test
 public void testAddHighlightEmptyText() {
 searchableTextPane.addHighlight(0, 0);
 assertEquals(0, searchableTextPane.getDocument().getLength());
 }

 // Test addHighlight method - invalid offset
 @Test(expected = IllegalArgumentException.class)
 public void testAddHighlightInvalidOffset() {
 searchableTextPane.addHighlight(10, 20);
 }

 // Test addHighlight method - basic functionality
 @Test
 public void testAddHighlightBasicFunctionality() {
 final String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 lineResults = searchableTextPane.highlight("fox", 0);
 assertEquals(1, lineResults.size());
 assertEquals(15, lineResults.get(0).getStartOffset());
 assertEquals(18, lineResults.get(0).getEndOffset());
 searchableTextPane.addHighlight(15, 3);
 assertEquals(1, searchableTextPane.getHighlighter().getHighlightCount());
 }

 // Test removeHighlights method - empty text
 @Test
 public void testRemoveHighlightsEmptyText() {
 searchableTextPane.setText("");
 assertEquals(0, searchableTextPane.getHighlighter().getHighlightCount());
 }

 // Test removeHighlights method - basic functionality
 @Test
 public void testRemoveHighlightsBasicFunctionality() {
 final String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 lineResults = searchableTextPane.highlight("fox", 0);
 assertEquals(1, lineResults.size());
 assertEquals(15, lineResults.get(0).getStartOffset());
 assertEquals(18, lineResults.get(0).getEndOffset());
 searchableTextPane.removeHighlights();
 assertEquals(0, searchableTextPane.getHighlighter().getHighlightCount());
 }

 // Test removeHighlights method - multiple highlights
 @Test
 public void testRemoveHighlightsMultipleHighlights() {
 final String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 lineResults = searchableTextPane.highlight("fox", 0);
 assertEquals(1, lineResults.size());
 assertEquals(15, lineResults.get(0).getStartOffset());
 assertEquals(18, lineResults.get(0).getEndOffset());
 lineResults = searchableTextPane.highlight("lazy", 0);
 assertEquals(1, lineResults.size());
 assertEquals(34, lineResults.get(0).getStartOffset());
 assertEquals(39, lineResults.get(0).getEndOffset());
 searchableTextPane.removeHighlights();
 assertEquals(0, searchableTextPane.getHighlighter().getHighlightCount());
 }

}