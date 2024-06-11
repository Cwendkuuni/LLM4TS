package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private List<LineResult> lineResults;
 private String term;

 @Before
 public void setUp() {
 char[] text = new char[]{'T', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 'J', 'a', 'v', 'a', ' ', 'J', 'u', 'n', 'i', 't', ' ', 'U', 'n', 'i', 't', ' ', 'T', 'e', 's', 't', ' ', 'P', 'a', 'n', 'e', ' ', 'f', 'o', 'r', ' ', 'J', 'a', 'v', 'a', ' ', 'J', 'u', 'n', 'i', 't', ' ', 'I', 's', ' ', 'a', ' ', 't', 'e', 's', 't', ' ', 'p', 'a', 'n', 'e'};
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
 JTextComponent textComponent = new JTextComponent() {
 @Override public Document getDocument() {
 return searchableTextPane.getDocument();
 }
 };
 JEditorPane editorPane = new JEditorPane(textComponent);
 editorPane.setContentType("text/plain");
 searchableTextPane.setText(new String(text));
 }

 @Test
 public void testConstructor() {
 assertEquals(Font.class, searchableTextPane.getFont().getClass());
 assertEquals(8, searchableTextPane.getTabSize());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.add(searchableTextPane);
 frame.pack();
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyTerm() {
 lineResults = searchableTextPane.highlight(null, 0);
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightNullTerm() {
 lineResults = searchableTextPane.highlight(null, 0);
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightEmptyTextPane() {
 lineResults = searchableTextPane.highlight("test", 0);
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightInvalidFlags() {
 searchableTextPane.highlight("test", 0, 12345);
 }

 @Test
 public void testHighlight() {
 term = "test";
 lineResults = searchableTextPane.highlight(term, 0);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 SearchEngine searchEngine = new SearchEngine(0);
 List<WordResult> wordResults = searchEngine.search(term, searchableTextPane.getText().toString());
 LineResult lineResult = lineResults.get(0);
 assertEquals(searchableTextPane.getLineCount(), lineResult.getLineNumber());
 List<WordResult> lineWords = lineResult.getWordResults();
 assertEquals(1, lineWords.size());
 assertEquals(term, lineWords.get(0).getWord());
 assertEquals(term.length(), lineWords.get(0).getLength());
 assertEquals(Color.YELLOW, lineWords.get(0).getHighlightPainter().getColor());
 }

 @Test
 public void testHighlightMultipleOccurrences() {
 term = "test";
 lineResults = searchableTextPane.highlight(term, 0);
 assertNotNull(lineResults);
 assertEquals(2, lineResults.size());
 SearchEngine searchEngine = new SearchEngine(0);
 List<WordResult> wordResults1 = searchEngine.search(term, searchableTextPane.getText().toString());
 LineResult lineResult1 = lineResults.get(0);
 List<WordResult> lineWords1 = lineResult1.getWordResults();
 assertEquals("this is a test", lineWords1.get(0).getWord());
 assertEquals("test", lineWords1.get(0).getWord());
 assertEquals(5, lineWords1.get(0).getStart());
 assertEquals(10, lineWords1.get(0).getEnd());
 List<WordResult> wordResults2 = searchEngine.search(term, searchableTextPane.getText().toString(), 10);
 LineResult lineResult2 = lineResults.get(1);
 List<WordResult> lineWords2 = lineResult2.getWordResults();
 assertEquals("unit test panes test", lineWords2.get(0).getWord());
 assertEquals("test", lineWords2.get(0).getWord());
 assertEquals(24, lineWords2.get(0).getStart());
 assertEquals(28, lineWords2.get(0).getEnd());
 }

 @Test
 public void testHighlightCaseInsensitive() {
 term = "Test";
 lineResults = searchableTextPane.highlight(term, 0);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 SearchEngine searchEngine = new SearchEngine(SearchEngine.CaseInsensitiveFlag.DEFAULT);
 List<WordResult> wordResults = searchEngine.search(term, searchableTextPane.getText().toString());
 LineResult lineResult = lineResults.get(0);
 List<WordResult> lineWords = lineResult.getWordResults();
 assertEquals("This is a test", lineWords.get(0).getWord());
 assertEquals("Test", lineWords.get(0).getWord());
 assertEquals(0, lineWords.get(0).getStart());
 assertEquals(6, lineWords.get(0).getEnd());
 }

 @Test
 public void testHighlightRegex() {
 term = "[Tt]est";
 lineResults = searchableTextPane.highlight(term, 0);
 assertNotNull(lineResults);
 assertEquals(2, lineResults.size());
 SearchEngine searchEngine = new SearchEngine(SearchEngine.RegExFlag.DEFAULT);
 List<WordResult> wordResults1 = searchEngine.search(term, searchableTextPane.getText().toString());
 LineResult lineResult1 = lineResults.get(0);
 List<WordResult> lineWords1 = lineResult1.getWordResults();
 assertEquals("This is a test", lineWords1.get(0).getWord());
 assertEquals("test", lineWords1.get(0).getWord());
 assertEquals(0, lineWords1.get(0).getStart());
 assertEquals(6, lineWords1.get(0).getEnd());
 List<WordResult> wordResults2 = searchEngine.search(term, searchableTextPane.getText().toString(), 10);
 LineResult lineResult2 = lineResults.get(1);
 List<WordResult> lineWords2 = lineResult2.getWordResults();
 assertEquals("unit test panes test", lineWords2.get(0).getWord());
 assertEquals("test", lineWords2.get(0).getWord());
 assertEquals(24, lineWords2.get(0).getStart());
 assertEquals(28, lineWords2.get(0).getEnd());
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightException() {
 term = "test";
 searchableTextPane.highlight(term, -1);
 }

 @Test
 public void testRemoveHighlights() {
 lineResults = searchableTextPane.highlight("test", 0);
 assertEquals(1, lineResults.size());
 searchableTextPane.removeHighlights();
 lineResults = searchableTextPane.highlight("test", 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testSearchEmptyTerm() {
 int result = searchableTextPane.search("");
 assertEquals(-1, result);
 }

 @Test
 public void testSearchNullTerm() {
 int result = searchableTextPane.search(null);
 assertEquals(-1, result);
 }

 @Test
 public void testSearchEmptyTextPane() {
 int result = searchableTextPane.search("test");
 assertEquals(-1, result);
 }

 @Test
 public void testSearch() {
 term = "test";
 int result = searchableTextPane.search(term);
 assertEquals(-1, result);
 int result2 = searchableTextPane.search(term, 0);
 assertEquals(5, result2);
 }

 @Test
 public void testSearchMultipleOccurrences() {
 term = "test";
 int result1 = searchableTextPane.search(term);
 assertEquals(-1, result1);
 int result2 = searchableTextPane.search(term, 0);
 assertEquals(5, result2);
 int result3 = searchableTextPane.search(term, 6);
 assertEquals(24, result3);
 }

 @Test
 public void testSearchCaseInsensitive() {
 term = "Test";
 int result = searchableTextPane.search(term);
 assertEquals(-1, result);
 int result2 = searchableTextPane.search(term, 0);
 assertEquals(0, result2);
 }

 @Test
 public void testSearchRegex() {
 term = "[Tt]est";
 int result = searchableTextPane.search(term);
 assertEquals(-1, result);
 int result2 = searchableTextPane.search(term, 0);
 assertEquals(5, result2);
 int result3 = searchableTextPane.search(term, 10);
 assertEquals(24, result3);
 }

 @Test(expected = BadLocationException.class)
 public void testSearchException() {
 searchableTextPane.search("test", -1);
 }

 @Test
 public void testConvertWords2LinesEmptyList() {
 List<WordResult> emptyList = Arrays.asList();
 lineResults = searchableTextPane.convertWords2Lines(emptyList);
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testConvertWords2LinesSingleWord() {
 List<WordResult> wordList = Arrays.asList(new WordResult("test", 5, 10));
 lineResults = searchableTextPane.convertWords2Lines(wordList);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getLineNumber());
 List<WordResult> lineWords = lineResult.getWordResults();
 assertEquals("test", lineWords.get(0).getWord());
 assertEquals(5, lineWords.get(0).getStart());
 assertEquals(10, lineWords.get(0).getEnd());
 }

 @Test
 public void testConvertWords2LinesMultipleWords() {
 List<WordResult> wordList = Arrays.asList(new WordResult("this", 0, 3), new WordResult("is", 3, 4), new WordResult("a", 5, 6), new WordResult("test", 7, 12));
 lineResults = searchableTextPane.convertWords2Lines(wordList);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getLineNumber());
 List<WordResult> lineWords = lineResult.getWordResults();
 assertEquals("this", lineWords.get(0).getWord());
 assertEquals("is", lineWords.get(1).getWord());
 assertEquals("a", lineWords.get(2).getWord());
 assertEquals("test", lineWords.get(3).getWord());
 assertEquals(0, lineWords.get(0).getStart());
 assertEquals(3, lineWords.get(0).getEnd());
 assertEquals(3, lineWords.get(1).getStart());
 assertEquals(4, lineWords.get(1).getEnd());
 assertEquals(6, lineWords.get(2).getStart());
 assertEquals(6, lineWords.get(2).getEnd());
 assertEquals(12, lineWords.get(3).getStart());
 assertEquals(16, lineWords.get(3).getEnd());
 }

 @Test(expected = BadLocationException.class)
 public void testConvertWords2LinesException() {
 List<WordResult> wordList = Arrays.asList(new WordResult("test", -1, 10));
 lineResults = searchableTextPane.convertWords2Lines(wordList);
 }
}