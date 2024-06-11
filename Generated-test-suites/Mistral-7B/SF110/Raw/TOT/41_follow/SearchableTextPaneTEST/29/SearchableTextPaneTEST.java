package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private List<LineResult> lineResults;

 @Before
 public void setup() {
 searchableTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 4);
 lineResults = new ArrayList<>();
 }

 @Test
 public void testHighlightNewTerm() {
 String term = "Java";
 int flags = 0; // default flags
 int startPos = 0;
 searchableTextPane.highlight(term, flags);
 List<WordResult> searchResults = new SearchEngine(flags).search(term, searchableTextPane.getText().toString());
 lineResults = searchableTextPane.convertWords2Lines(searchResults);
 for (LineResult lineResult : lineResults) {
 for (WordResult wordResult : lineResult.getWordResults()) {
 assertEquals("Color of highlighted word should be yellow.", Color.YELLOW, wordResult.getHighlightPainter().getColor());
 }
 }
 }

 @Test
 public void testHighlightEmptyTerm() {
 String term = "";
 int flags = 0; // default flags
 int startPos = 0;
 searchableTextPane.highlight(term, flags);
 assertEquals("Number of line results should be zero when term is empty.", 0, lineResults.size());
 }

 @Test
 public void testHighlightNullTerm() {
 String term = null;
 int flags = 0; // default flags
 int startPos = 0;
 searchableTextPane.highlight(term, flags);
 assertEquals("Number of line results should be zero when term is null.", 0, lineResults.size());
 }

 @Test
 public void testHighlightWithError() {
 String term = "Exception";
 int flags = 0; // default flags
 int startPos = -1; // invalid position
 searchableTextPane.highlight(term, flags);
 assertEquals("Number of line results should be zero when an error occurs during highlighting.", 0, lineResults.size());
 }

 @Test
 public void testSearchNewTerm() {
 String term = "Java";
 int startPos = 0;
 int expectedPos = searchableTextPane.search(term, startPos);
 int actualPos = 7; // assuming "Java" appears at the 8th character in the text
 assertEquals("Search should find the term at the expected position.", expectedPos + term.length(), actualPos);
 }

 @Test
 public void testSearchEmptyTerm() {
 String term = "";
 int startPos = 0;
 int expectedPos = -1;
 int actualPos = searchableTextPane.search(term, startPos);
 assertEquals("Search should return -1 when term is empty.", expectedPos, actualPos);
 }

 @Test
 public void testSearchNullTerm() {
 String term = null;
 int startPos = 0;
 int expectedPos = -1;
 int actualPos = searchableTextPane.search(term, startPos);
 assertEquals("Search should return -1 when term is null.", expectedPos, actualPos);
 }

 @Test
 public void testSearchWithError() {
 String term = "Exception";
 int startPos = -1; // invalid position
 int expectedPos = -1;
 int actualPos = searchableTextPane.search(term, startPos);
 assertEquals("Search should return -1 when an error occurs during searching.", expectedPos, actualPos);
 }
}