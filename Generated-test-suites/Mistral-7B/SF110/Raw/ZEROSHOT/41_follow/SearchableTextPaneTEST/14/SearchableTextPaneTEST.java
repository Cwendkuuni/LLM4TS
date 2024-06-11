package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
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
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
 term = "JUnit";
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 JFrame frame = new JFrame();
 JPanel panel = new JPanel();
 panel.add(searchableTextPane);
 frame.getContentPane().add(panel);
 frame.setSize(200, 200);
 frame.setVisible(true);
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlight() {
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertEquals(lineResults.size(), 1);
 assertNotNull(lineResults.get(0).getWordResults());
 }

 @Test
 public void testSearch() {
 int initialPosition = searchableTextPane.getDocument().getLength();
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog. JUnit is a Java testing framework.");
 searchableTextPane.highlight(term, 0);
 int position = searchableTextPane.search(term);
 assertEquals(position, initialPosition + searchableTextPane.getDocument().getIndexOf(term) + term.length());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSearchNullTerm() {
 searchableTextPane.search(null);
 }

 @Test
 public void testSearchEmptyTextPane() {
 searchableTextPane.setText("");
 int position = searchableTextPane.search(term);
 assertEquals(position, -1);
 }

 @Test
 public void testSearchEmptyTerm() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 int position = searchableTextPane.search("");
 assertEquals(position, -1);
 }

 @Test
 public void testSearchPreviousTerm() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog. JUnit is a Java testing framework.");
 int initialPosition = searchableTextPane.search(term);
 int previousPosition = searchableTextPane.search(term, initialPosition - term.length());
 assertEquals(previousPosition, initialPosition - term.length() - term.length());
 }

 @Test
 public void testSearchTermNotPresent() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 int position = searchableTextPane.search("Selenium");
 assertEquals(position, -1);
 }

 @Test
 public void testConvertWords2Lines() {
 String text = "Line 1: The quick brown fox jumps over the lazy dog.\nLine 2: JUnit is a Java testing framework.";
 Document doc = new PlainDocument();
 doc.insertString(0, text, null);
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
 searchableTextPane.setDocument(doc);
 searchableTextPane.setCaretPosition(0);
 List<WordResult> words = new ArrayList<>();
 words.add(new WordResult("JUnit", 33, 38));
 words.add(new WordResult("Java", 38, 41));
 List<LineResult> lineResults = searchableTextPane.convertWords2Lines(words);
 assertEquals(lineResults.size(), 2);
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(lineResult1.getLineNumber(), 1);
 assertEquals(lineResult1.getStartOffset(), 0);
 assertEquals(lineResult1.getEndOffset(), 22);
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(lineResult2.getLineNumber(), 2);
 assertEquals(lineResult2.getStartOffset(), 23);
 assertEquals(lineResult2.getEndOffset(), 38);
 }

 @Test
 public void testAddHighlight() throws BadLocationException {
 Document doc = new PlainDocument();
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
 searchableTextPane.setDocument(doc);
 int start = 0;
 int length = 10;
 searchableTextPane.addHighlight(start, length);
 assertEquals(searchableTextPane.getHighlighter().getHighlights().length, 1);
 HighlightIterator highlightIterator = searchableTextPane.getHighlighter().getHighlights()[0].getIterator();
 assertEquals(highlightIterator.getEndOffset(), start + length);
 }

 @Test
 public void testRemoveHighlights() throws BadLocationException {
 Document doc = new PlainDocument();
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
 searchableTextPane.setDocument(doc);
 int start = 0;
 int length = 10;
 searchableTextPane.addHighlight(start, length);
 searchableTextPane.removeHighlights();
 assertEquals(searchableTextPane.getHighlighter().getHighlights().length, 0);
 }

 @Test
 public void testSearchEngineNullFlags() {
 SearchEngine searchEngine = new SearchEngine(null);
 List<WordResult> searchResults = searchEngine.search(term, "The quick brown fox jumps over the lazy dog.");
 assertTrue(searchResults.isEmpty());
 }

 @Test
 public void testSearchEngineEmptyText() {
 SearchEngine searchEngine = new SearchEngine(SearchEngine.CaseSensitive | SearchEngine.Regex);
 List<WordResult> searchResults = searchEngine.search(term, "");
 assertTrue(searchResults.isEmpty());
 }

 @Test
 public void testSearchEngineEmptyTerm() {
 SearchEngine searchEngine = new SearchEngine(SearchEngine.CaseSensitive | SearchEngine.Regex);
 List<WordResult> searchResults = searchEngine.search("", "The quick brown fox jumps over the lazy dog.");
 assertTrue(searchResults.isEmpty());
 }

 @Test
 public void testSearchEngineSingleTerm() {
 SearchEngine searchEngine = new SearchEngine(SearchEngine.CaseSensitive | SearchEngine.Regex);
 List<WordResult> searchResults = searchEngine.search(term, "The quick brown fox jumps over the lazy dog.");
 assertEquals(searchResults.size(), 1);
 }

 @Test
 public void testSearchEngineMultipleTerms() {
 SearchEngine searchEngine = new SearchEngine(SearchEngine.CaseSensitive | SearchEngine.Regex);
 List<WordResult> searchResults = searchEngine.search("JUnit Java", "The quick brown fox jumps over the lazy dog.");
 assertEquals(searchResults.size(), 1);
 }

 @Test
 public void testSearchEngineMultipleTermsWithSpaces() {
 SearchEngine searchEngine = new SearchEngine(SearchEngine.CaseSensitive | SearchEngine.Regex);
 List<WordResult> searchResults = searchEngine.search("JUnit Java", "The quick brown fox jumps over the lazy dog.");
 assertEquals(searchResults.size(), 1);
 }

 @Test
 public void testSearchEngineMultipleTermsWithDashes() {
 SearchEngine searchEngine = new SearchEngine(SearchEngine.CaseSensitive | SearchEngine.Regex);
 List<WordResult> searchResults = searchEngine.search("JUnit-Java", "The quick brown fox jumps over the lazy dog.");
 assertEquals(searchResults.size(), 1);
 }

 @Test
 public void testSearchEngineMultipleTermsWithDashesAndSpaces() {
 SearchEngine searchEngine = new SearchEngine(SearchEngine.CaseSensitive | SearchEngine.Regex);
 List<WordResult> searchResults = searchEngine.search("JUnit- Java", "The quick brown fox jumps over the lazy dog.");
 assertEquals(searchResults.size(), 1);
 }

 @Test
 public void testSearchEngineMultipleTermsWithSpecialChars() {
 SearchEngine searchEngine = new SearchEngine(SearchEngine.CaseSensitive | SearchEngine.Regex);
 List<WordResult> searchResults = searchEngine.search("JUnit\\.Test", "The quick brown fox jumps over the lazy dog.");
 assertEquals(searchResults.size(), 1);
 }

 @Test
 public void testSearchEngineMultipleTermsWithSpecialCharsAndSpaces() {
 SearchEngine searchEngine = new SearchEngine(SearchEngine.CaseSensitive | SearchEngine.Regex);
 List<WordResult> searchResults = searchEngine.search("JUnit\\.Test ", "The quick brown fox jumps over the lazy dog.");
 assertEquals(searchResults.size(), 1);
 }

 @Test
 public void testSearchEngineMultipleTermsWithSpecialCharsAndDashes() {
 SearchEngine searchEngine = new SearchEngine(SearchEngine.CaseSensitive | SearchEngine.Regex);
 List<WordResult> searchResults = searchEngine.search("JUnit\\.Test-Case", "The quick brown fox jumps over the lazy dog.");
 assertEquals(searchResults.size(), 1);
 }

 @Test
 public void testSearchEngineMultipleTermsWithSpecialCharsAndDashesAndSpaces() {
 SearchEngine searchEngine = new SearchEngine(SearchEngine.CaseSensitive | SearchEngine.Regex);
 List<WordResult> searchResults = searchEngine.search("JUnit\\.Test- Case", "The quick brown fox jumps over the lazy dog.");
 assertEquals(searchResults.size(), 1);
 }
}