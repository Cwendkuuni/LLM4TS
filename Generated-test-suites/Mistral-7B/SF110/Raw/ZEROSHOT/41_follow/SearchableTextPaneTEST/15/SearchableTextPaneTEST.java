import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private List<LineResult> lineResults;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8);
 }

 @After
 public void tearDown() {
 searchableTextPane = null;
 }

 @Test
 public void testSearchableTextPaneCreation() {
 assertNotNull(searchableTextPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightNullTerm() {
 searchableTextPane.highlight(null, 0);
 assertTrue(searchableTextPane.getHighlighter().getHighlights().isEmpty());
 }

 @Test
 public void testHighlightEmptyTerm() {
 searchableTextPane.setText("This is a test searchable text pane.");
 String term = "";
 List<WordResult> searchResults = new SearchEngine(0).search(term, searchableTextPane.getDocument().getText(0, searchableTextPane.getDocument().getLength()));
 lineResults = searchableTextPane.convertWords2Lines(searchResults);
 searchableTextPane.highlight(term, 0);
 assertTrue(searchableTextPane.getHighlighter().getHighlights().isEmpty());
 }

 @Test
 public void testHighlightValidTerm() throws Exception {
 searchableTextPane.setText("This is a test searchable text pane.");
 String term = "text";
 List<WordResult> searchResults = new SearchEngine(0).search(term, searchableTextPane.getDocument().getText(0, searchableTextPane.getDocument().getLength()));
 lineResults = searchableTextPane.convertWords2Lines(searchResults);
 searchableTextPane.highlight(term, 0);
 List<Highlight> highlights = searchableTextPane.getHighlighter().getHighlights();
 assertEquals(1, highlights.size());
 Highlight highlight = highlights.get(0);
 Point startPoint = highlight.getLogicalEndPosition();
 Point endPoint = highlight.getLogicalStartPosition();
 assertEquals(21, startPoint.x);
 assertEquals(31, endPoint.x);
 assertEquals(6, startPoint.y);
 assertEquals(6, endPoint.y);
 }

 @Test
 public void testSearchNullTerm() {
 int pos = searchableTextPane.search(null);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchEmptyTerm() {
 searchableTextPane.setText("This is a test searchable text pane.");
 int pos = searchableTextPane.search("");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchValidTerm() throws Exception {
 searchableTextPane.setText("This is a test searchable text pane.");
 String term = "test";
 int pos = searchableTextPane.search(term);
 assertEquals(14, pos);
 }

 @Test
 public void testSearchPreviousTerm() throws Exception {
 searchableTextPane.setText("This is a test searchable text pane.");
 String term = "is";
 int pos = searchableTextPane.search(term);
 assertEquals(-1, pos);
 String term2 = "test";
 pos = searchableTextPane.search(term2);
 assertEquals(14, pos);
 pos = searchableTextPane.search(term2);
 assertEquals(pos + term2.length(), pos);
 }

 @Test(expected = BadLocationException.class)
 public void testSearchInvalidOffset() throws Exception {
 searchableTextPane.setText("This is a test searchable text pane.");
 int termLength = "test".length();
 int pos = searchableTextPane.search(term, 1);
 assertEquals(8, pos);
 searchableTextPane.search(term, 1);
 searchableTextPane.search(term, 1);
 searchableTextPane.search(term, 1);
 searchableTextPane.search(term, 1 + termLength);
 }

 @Test(expected = BadLocationException.class)
 public void testConvertWords2LinesInvalidOffset() throws Exception {
 searchableTextPane.setText("This is a test searchable text pane.");
 List<WordResult> searchResults = new ArrayList<>();
 WordResult wordResult = new WordResult(0, "test".length(), 14);
 searchResults.add(wordResult);
 lineResults = searchableTextPane.convertWords2Lines(searchResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(8, lineResult.getStartOffset());
 assertEquals(31, lineResult.getEndOffset());
 searchableTextPane.search(wordResult.getTerm(), lineResult.getStartOffset());
 searchableTextPane.search(wordResult.getTerm(), lineResult.getStartOffset() + 1);
 }
}