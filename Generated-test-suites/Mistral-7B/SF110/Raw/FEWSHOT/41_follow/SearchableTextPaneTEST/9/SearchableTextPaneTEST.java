package ghm.follow.search;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.text.*;

import org.junit.Test;

public class SearchableTextPaneTest {

 private JTextArea textArea;

 @Test
 public void testSearchableTextPaneConstruction() {
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8);
 assertNotNull(searchableTextPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 JFrame frame = new JFrame();
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8);
 frame.getContentPane().add(searchableTextPane);
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 JTextArea textArea = new JTextArea();
 frame.getContentPane().add(textArea);
 assertFalse(textArea.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlight() throws Exception {
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8);
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 String term = "fox";
 List<LineResult> lineResults = searchableTextPane.highlight(term, SearchableTextPane.SEARCH_CASE_SENSITIVE);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(2, lineResult.getWordResults().size());
 int lineNumber = lineResult.getLineNumber();
 assertEquals(1, lineNumber);
 int lineStart = lineResult.getStartOffset();
 int lineEnd = lineResult.getEndOffset();
 assertEquals(49, lineStart);
 assertEquals(53, lineEnd);
 }

 @Test
 public void testHighlightCaseInsensitive() throws Exception {
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8);
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 String term = "fox";
 List<LineResult> lineResults = searchableTextPane.highlight(term, SearchableTextPane.SEARCH_CASE_INSENSITIVE);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getWordResults().size());
 int lineNumber = lineResult.getLineNumber();
 assertEquals(1, lineNumber);
 int lineStart = lineResult.getStartOffset();
 int lineEnd = lineResult.getEndOffset();
 assertEquals(48, lineStart);
 assertEquals(53, lineEnd);
 }

 @Test
 public void testHighlightEmptySearchTerm() throws Exception {
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8);
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 List<LineResult> lineResults = searchableTextPane.highlight("", SearchableTextPane.SEARCH_CASE_SENSITIVE);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightNullSearchTerm() throws Exception {
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8);
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 List<LineResult> lineResults = searchableTextPane.highlight(null, SearchableTextPane.SEARCH_CASE_SENSITIVE);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testSearch() throws Exception {
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8);
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 int position = searchableTextPane.search("fox");
 assertEquals(48, position);
 position = searchableTextPane.search("Fox");
 assertEquals(48, position);
 position = searchableTextPane.search("lazy");
 assertEquals(65, position);
 position = searchableTextPane.search("DOG");
 assertEquals(-1, position);
 }

 @Test
 public void testSearchEmptySearchTerm() throws Exception {
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8);
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 int position = searchableTextPane.search("");
 assertEquals(-1, position);
 }

 @Test
 public void testSearchNullSearchTerm() throws Exception {
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8);
 String text = "The quick brown fox jumps over the lazy dog.";
 searchableTextPane.setText(text);
 int position = searchableTextPane.search(null);
 assertEquals(-1, position);
 }
}