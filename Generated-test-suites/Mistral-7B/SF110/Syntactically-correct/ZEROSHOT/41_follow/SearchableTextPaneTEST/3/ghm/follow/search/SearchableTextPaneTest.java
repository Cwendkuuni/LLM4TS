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

 private JTextArea textArea;
 private SearchableTextPane searchableTextPane;
 private List<LineResult> lineResults;

 @Before
 public void setUp() {
 final Font font = new Font("Serif", Font.PLAIN, 12);
 textArea = new JTextArea();
 searchableTextPane = new SearchableTextPane(font, 4);
 textArea.setText(null);
 textArea.setBackground(Color.WHITE);
 textArea.setWrapStyleWord(true);
 textArea.setLineWrap(true);
 searchableTextPane.setDocument(textArea.getDocument());
 }

 @Test
 public void testInitialization() {
 assertEquals(Color.WHITE, searchableTextPane.getBackground());
 assertNull(searchableTextPane.getLastSearchTerm());
 assertNull(searchableTextPane.getLastSearchPos());
 }

 @Test
 public void testScrollableTracksViewportWidth() {
 assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyTerm() {
 final List<WordResult> wordResults = new ArrayList<>();
 lineResults = searchableTextPane.convertWords2Lines(wordResults);
 assertNotNull(lineResults);
 assertEmpty(lineResults);
 }

 @Test
 public void testHighlightSingleWord() {
 final String term = "apple";
 final List<WordResult> wordResults = Arrays.asList(
 new WordResult(0, 0, term.length(), 0, 0),
 new WordResult(5, 5, term.length(), 6, 6)
 );
 lineResults = searchableTextPane.convertWords2Lines(wordResults);
 assertNotNull(lineResults);
 assertFalse(lineResults.isEmpty());
 final LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getLineStartOffset());
 assertEquals(5, lineResult.getLineEndOffset());
 assertFalse(lineResult.getWordResults().isEmpty());
 final WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(0, wordResult.getStart());
 assertEquals(term.length(), wordResult.getLength());
 assertEquals(Color.YELLOW, wordResult.getBackground());
 }

 @Test
 public void testHighlightMultipleWordsInSameLine() {
 final String text = "The quick brown fox jumps over the lazy dog.";
 final String term = "quick brown fox";
 final List<WordResult> wordResults = Arrays.asList(
 new WordResult(0, 0, 5, 5, 5),
 new WordResult(6, 6, 3, 9, 9),
 new WordResult(12, 12, 3, 14, 14),
 new WordResult(17, 17, 4, 20, 20),
 new WordResult(22, 22, 3, 24, 24)
 );
 lineResults = searchableTextPane.convertWords2Lines(wordResults);
 assertNotNull(lineResults);
 assertFalse(lineResults.isEmpty());
 final LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getLineStartOffset());
 assertEquals(24, lineResult.getLineEndOffset());
 assertFalse(lineResult.getWordResults().isEmpty());
 final List<WordResult> words = lineResult.getWordResults();
 assertEquals(4, words.size());
 final WordResult firstWord = words.get(0);
 assertEquals(0, firstWord.getStart());
 assertEquals(5, firstWord.getLength());
 assertEquals(Color.YELLOW, firstWord.getBackground());
 final WordResult secondWord = words.get(1);
 assertEquals(6, secondWord.getStart());
 assertEquals(3, secondWord.getLength());
 assertEquals(Color.YELLOW, secondWord.getBackground());
 final WordResult thirdWord = words.get(2);
 assertEquals(12, thirdWord.getStart());
 assertEquals(3, thirdWord.getLength());
 assertEquals(Color.YELLOW, thirdWord.getBackground());
 final WordResult fourthWord = words.get(3);
 assertEquals(17, fourthWord.getStart());
 assertEquals(4, fourthWord.getLength());
 assertEquals(Color.YELLOW, fourthWord.getBackground());
 }

 @Test
 public void testHighlightMultipleWordsInDifferentLines() {
 final String text = "The quick brown fox jumps over the lazy dog.\nThe quick red fox jumps over the lazy cat.";
 final String term = "quick red";
 final List<WordResult> wordResults = Arrays.asList(
 new WordResult(0, 0, 5, 5, 5),
 new WordResult(6, 6, 3, 9, 9),
 new WordResult(12, 12, 3, 14, 14),
 new WordResult(16, 16, 4, 20, 20),
 new WordResult(21, 21, 3, 23, 23),
 new WordResult(30, 30, 4, 33, 33),
 new WordResult(34, 34, 3, 36, 36)
 );
 lineResults = searchableTextPane.convertWords2Lines(wordResults);
 assertNotNull(lineResults);
 assertFalse(lineResults.isEmpty());
 final LineResult lineResult1 = lineResults.get(0);
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getLineStartOffset());
 assertEquals(11, lineResult1.getLineEndOffset());
 assertFalse(lineResult1.getWordResults().isEmpty());
 final WordResult wordResult1 = lineResult1.getWordResults().get(0);
 assertEquals(0, wordResult1.getStart());
 assertEquals(5, wordResult1.getLength());
 assertEquals(Color.YELLOW, wordResult1.getBackground());
 final LineResult lineResult2 = lineResults.get(1);
 assertEquals(1, lineResult2.getLineNumber());
 assertEquals(21, lineResult2.getLineStartOffset());
 assertEquals(36, lineResult2.getLineEndOffset());
 assertFalse(lineResult2.getWordResults().isEmpty());
 final WordResult wordResult2 = lineResult2.getWordResults().get(0);
 assertEquals(30, wordResult2.getStart());
 assertEquals(3, wordResult2.getLength());
 assertEquals(Color.YELLOW, wordResult2.getBackground());
 final WordResult wordResult3 = lineResult2.getWordResults().get(1);
 assertEquals(34, wordResult3.getStart());
 assertEquals(3, wordResult3.getLength());
 assertEquals(Color.YELLOW, wordResult3.getBackground());
 }

 @Test
 public void testSearchEmptyTerm() {
 final int pos = searchableTextPane.search("");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchSingleWord() {
 final String term = "apple";
 final int pos = searchableTextPane.search(term);
 assertEquals(-1, pos);
 final List<WordResult> wordResults = Arrays.asList(
 new WordResult(0, 0, term.length(), 0, 0)
 );
 lineResults = searchableTextPane.convertWords2Lines(wordResults);
 searchableTextPane.highlight(term, 0);
 final int pos2 = searchableTextPane.search(term);
 assertEquals(0, pos2);
 }

 @Test
 public void testSearchSingleWordAlreadyHighlighted() {
 final String term = "apple";
 final int pos = searchableTextPane.search(term);
 assertEquals(-1, pos);
 final List<WordResult> wordResults = Arrays.asList(
 new WordResult(0, 0, term.length(), 0, 0)
 );
 lineResults = searchableTextPane.convertWords2Lines(wordResults);
 searchableTextPane.highlight(term, 0);
 final int pos2 = searchableTextPane.search(term);
 assertEquals(0, pos2);
 }

 @Test
 public void testSearchMultipleWordsInSameLine() {
 final String text = "The quick brown fox jumps over the lazy dog.";
 final String term = "quick brown fox";
 final int pos = searchableTextPane.search(term);
 assertEquals(-1, pos);
 final List<WordResult> wordResults = Arrays.asList(
 new WordResult(0, 0, 5, 5, 5),
 new WordResult(6, 6, 3, 9, 9),
 new WordResult(12, 12, 3, 14, 14),
 new WordResult(17, 17, 4, 20, 20),
 new WordResult(22, 22, 3, 24, 24)
 );
 lineResults = searchableTextPane.convertWords2Lines(wordResults);
 searchableTextPane.highlight(term, 0);
 final int pos2 = searchableTextPane.search(term);
 assertEquals(0, pos2);
 }

 @Test
 public void testSearchMultipleWordsInDifferentLines() {
 final String text = "The quick brown fox jumps over the lazy dog.\nThe quick red fox jumps over the lazy cat.";
 final String term = "quick red";
 final int pos = searchableTextPane.search(term);
 assertEquals(-1, pos);
 final List<WordResult> wordResults1 = Arrays.asList(
 new WordResult(0, 0, 5, 5, 5),
 new WordResult(6, 6, 3, 9, 9),
 new WordResult(12, 12, 3, 14, 14),
 new WordResult(16, 16, 4, 20, 20)
 );
 lineResults = searchableTextPane.convertWords2Lines(wordResults1);
 searchableTextPane.highlight(term, 0);
 final int pos2 = searchableTextPane.search(term);
 assertEquals(1, pos2);
 final List<WordResult> wordResults2 = Arrays.asList(
 new WordResult(21, 21, 3, 23, 23),
 new WordResult(30, 30, 4, 33, 33),
 new WordResult(34, 34, 3, 36, 36)
 );
 lineResults = searchableTextPane.convertWords2Lines(wordResults2);
 searchableTextPane.highlight(term, 0);
 final int pos3 = searchableTextPane.search(term);
 assertEquals(1, pos3);
 }
}