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

 @Before
 public void setUp() {
 char[] text = "The quick brown fox jumps over the lazy dog. The lazy dog sleeps all day.".toCharArray();
 Font font = new Font("Serif", Font.PLAIN, 14);
 int tabSize = 4;
 searchableTextPane = new SearchableTextPane(font, tabSize);
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.getContentPane().add(searchableTextPane);
 frame.pack();
 frame.setVisible(true);
 searchableTextPane.setText(new String(text));
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightNullTerm() {
 lineResults = searchableTextPane.highlight(null, 0);
 assertNotNull(lineResults);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlightEmptyTerm() {
 lineResults = searchableTextPane.highlight("", 0);
 assertNotNull(lineResults);
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testHighlightSingleWord() {
 String term = "fox";
 lineResults = searchableTextPane.highlight(term, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(13, lineResult.getStartOffset());
 assertEquals(16, lineResult.getEndOffset());
 assertNotNull(lineResult.getWordResults());
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(13, wordResult.getLineNumber());
 assertEquals(13, wordResult.getStart());
 assertEquals(15, wordResult.getEnd());
 assertEquals(1, wordResult.getLength());
 assertEquals(term, wordResult.getTerm());
 assertEquals(Color.YELLOW, wordResult.getPaintColor());
 }

 @Test
 public void testHighlightMultipleWordsInSameLine() {
 String term = "lazy dog";
 lineResults = searchableTextPane.highlight(term, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getWordResults().size());
 List<WordResult> wordResults = lineResult.getWordResults();
 assertEquals(2, wordResults.size());
 WordResult wordResult1 = wordResults.get(0);
 assertEquals(1, wordResult1.getLineNumber());
 assertEquals(36, wordResult1.getStart());
 assertEquals(38, wordResult1.getEnd());
 assertEquals(3, wordResult1.getLength());
 assertEquals(term.substring(0, 3), wordResult1.getTerm());
 WordResult wordResult2 = wordResults.get(1);
 assertEquals(1, wordResult2.getLineNumber());
 assertEquals(41, wordResult2.getStart());
 assertEquals(44, wordResult2.getEnd());
 assertEquals(4, wordResult2.getLength());
 assertEquals(term.substring(4), wordResult2.getTerm());
 }

 @Test
 public void testHighlightMultipleWordsInDifferentLines() {
 String term = "lazy";
 lineResults = searchableTextPane.highlight(term, 0);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(7, lineResult1.getStartOffset());
 assertEquals(10, lineResult1.getEndOffset());
 assertEquals(1, lineResult1.getWordResults().size());
 WordResult wordResult1 = lineResult1.getWordResults().get(0);
 assertEquals(0, wordResult1.getLineNumber());
 assertEquals(7, wordResult1.getStart());
 assertEquals(9, wordResult1.getEnd());
 assertEquals(3, wordResult1.getLength());
 assertEquals(term, wordResult1.getTerm());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(1, lineResult2.getLineNumber());
 assertEquals(41, lineResult2.getStartOffset());
 assertEquals(44, lineResult2.getEndOffset());
 assertEquals(1, lineResult2.getWordResults().size());
 WordResult wordResult2 = lineResult2.getWordResults().get(0);
 assertEquals(1, wordResult2.getLineNumber());
 assertEquals(41, wordResult2.getStart());
 assertEquals(43, wordResult2.getEnd());
 assertEquals(2, wordResult2.getLength());
 assertEquals(term, wordResult2.getTerm());
 }

 @Test
 public void testSearchNullTerm() {
 int result = searchableTextPane.search(null);
 assertEquals(-1, result);
 }

 @Test
 publicvoid testSearchEmptyTerm() {
 int result = searchableTextPane.search("");
 assertEquals(-1, result);
 }

 @Test
 public void testSearchSingleWord() {
 String term = "fox";
 int result = searchableTextPane.search(term);
 assertEquals(13, result);
 }

 @Test
 public void testSearchSingleWordAtBeginningOfText() {
 String term = "The";
 int result = searchableTextPane.search(term);
 assertEquals(-1, result);
 }

 @Test
 public void testSearchSingleWordAtEndOfText() {
 String term = "dog.";
 int result = searchableTextPane.search(term);
 assertEquals(-1, result);
 }

 @Test
 public void testSearchMultipleWordsInSameLine() {
 String term = "lazy dog";
 int result = searchableTextPane.search(term);
 assertEquals(36, result);
 }

 @Test
 public void testSearchMultipleWordsInDifferentLines() {
 String term = "lazy";
 int result = searchableTextPane.search(term);
 assertEquals(7, result);
 }

 @Test
 public void testSearchMultipleTermsInSameLine() {
 String term = "lazy fox";
 int result = searchableTextPane.search(term);
 assertEquals(-1, result);
 }

 @Test
 public void testSearchMultipleTermsInDifferentLines() {
 String term = "lazy dog fox";
 int result = searchableTextPane.search(term);
 assertEquals(-1, result);
 }

 @Test
 public void testSearchTermThatDoesNotExist() {
 String term = "zebra";
 int result = searchableTextPane.search(term);
 assertEquals(-1, result);
 }

 @Test
 public void testSearchTermThatIsSubstringOfAnotherTerm() {
 String term = "dog";
 int result = searchableTextPane.search(term);
 assertEquals(-1, result);
 }

 @Test
 public void testSearchTermThatIsSubstringOfAnotherTermButIsTheFirstCharacterOfAnotherTerm() {
 String term = "The";
 int result = searchableTextPane.search(term);
 assertEquals(-1, result);
 }

 @Test
 public void testSearchTermThatIsSubstringOfAnotherTermButIsTheLastCharacterOfAnotherTerm() {
 String term = ".";
 int result = searchableTextPane.search(term);
 assertEquals(-1, result);
 }

 @Test
 public void testSearchTermThatIsSubstringOfAnotherTermButIsInTheMiddleOfAnotherTerm() {
 String term = "fox";
 int result = searchableTextPane.search(term);
 assertEquals(-1, result);
 }

 @Test
 public void testSearchTermThatIsSubstringOfAnotherTermButIsSurroundedByWhitespace() {
 String term = " the ";
 int result = searchableTextPane.search(term);
 assertEquals(-1, result);
 }

 @Test
 public void testRemoveHighlights() {
 searchableTextPane.highlight("lazy", 0);
 lineResults = searchableTextPane.highlight("lazy", 0);
 assertEquals(2, lineResults.size());
 searchableTextPane.removeHighlights();
 assertEquals(0, lineResults.size());
 }
}