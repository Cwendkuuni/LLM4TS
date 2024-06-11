package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private String term;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 12), 4);
 term = "test";
 }

 @Test
 public void testHighlight() {
 String text = "This is a test for the SearchableTextPane's highlight method.";
 searchableTextPane.setText(text);
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(text.length(), lineResult.getEndOffset());
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(term.length(), wordResult.getLength());
 assertEquals(term, wordResult.getWord());
 assertTrue(searchableTextPane.getDocument().getCharacterElement(wordResult.getStart()).getAttributes().isAttributeEnabled(SearchableTextPane.class, "highlighted"));
 }

 @Test
 public void testHighlightEmptyString() {
 List<LineResult> lineResults = searchableTextPane.highlight("", 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightNull() {
 List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testRemoveHighlights() {
 searchableTextPane.highlight(term, 0);
 searchableTextPane.removeHighlights();
 assertFalse(searchableTextPane.getDocument().getCharacterElement(0).getAttributes().isAttributeEnabled(SearchableTextPane.class, "highlighted"));
 }

 @Test
 public void testSearch() {
 String text = "This is a test for the SearchableTextPane's search method.";
 searchableTextPane.setText(text);
 int position = searchableTextPane.search(term);
 assertEquals(0, position);
 position = searchableTextPane.search(term, 1);
 assertEquals(term.length(), searchableTextPane.getText().length() - position);
 }

 @Test
 public void testSearchEmptyString() {
 int position = searchableTextPane.search("");
 assertEquals(-1, position);
 }

 @Test
 public void testSearchNull() {
 int position = searchableTextPane.search(null);
 assertEquals(-1, position);
 }

 @Test
 public void testSearchPreviousTerm() {
 searchableTextPane.setText("This is a test for the SearchableTextPane's search method. This is a test for the SearchableTextPane's search method.");
 int position = searchableTextPane.search(term);
 int previousPosition = searchableTextPane.search(term, position - term.length());
 assertEquals(-1, previousPosition);
 }
}