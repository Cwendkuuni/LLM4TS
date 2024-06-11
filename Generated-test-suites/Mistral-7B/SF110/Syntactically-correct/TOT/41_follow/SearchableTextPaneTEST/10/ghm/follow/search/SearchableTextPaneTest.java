import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.search.SearchableTextPane;
import ghm.follow.search.SearchEngine;
import ghm.follow.search.LineResult;
import ghm.follow.search.WordResult;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private String term;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
 term = "test";
 }

 @Test
 public void testHighlight() {
 String text = "This is a test for the SearchableTextPane. The SearchableTextPane should be able to highlight the term 'test' in the text.";
 searchableTextPane.setText(text);
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getStartOffset());
 assertEquals(text.length(), lineResult.getEndOffset());
 }

 @Test
 public void testHighlight_EmptyText() {
 searchableTextPane.setText("");
 List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlight_EmptyTerm() {
 searchableTextPane.setText("This is a test for the SearchableTextPane.");
 List<LineResult> lineResults = searchableTextPane.highlight("", 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlight_Error() {
 searchableTextPane.setText("This is a test for the SearchableTextPane.");
 try {
 searchableTextPane.highlight(null, 0);
 fail();
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test
 public void testSearch() {
 searchableTextPane.setText("This is a test for the SearchableTextPane. The SearchableTextPane should return the position of the term 'test' in the text.");
 int pos = searchableTextPane.search(term);
 assertEquals(12, pos);
 }

 @Test
 public void testSearch_EmptyText() {
 searchableTextPane.setText("");
 int pos = searchableTextPane.search(term);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearch_EmptyTerm() {
 searchableTextPane.setText("This is a test for the SearchableTextPane.");
 int pos = searchableTextPane.search("");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearch_Error() {
 searchableTextPane.setText("This is a test for the SearchableTextPane.");
 try {
 searchableTextPane.search(null);
 fail();
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test
 public void testConvertWords2Lines() {
 String text = "Line 1: This is the first line. Line 2: This is the second line.";
 searchableTextPane.setText(text);
 List<WordResult> wordResults = new ArrayList<>();
 wordResults.add(new WordResult("Line 1:", 0, 13, wordResults));
 wordResults.add(new WordResult("first", 14, 17, wordResults));
 wordResults.add(new WordResult("line.", 18, 22, wordResults));
 wordResults.add(new WordResult("Line 2:", 23, 29, wordResults));
 wordResults.add(new WordResult("This", 30, 35, wordResults));
 wordResults.add(new WordResult("is", 36, 37, wordResults));
 wordResults.add(new WordResult("the", 38, 39, wordResults));
 wordResults.add(new WordResult("second", 40, 46, wordResults));
 wordResults.add(new WordResult("line.", 47, 51, wordResults));
 List<LineResult> lineResults = searchableTextPane.convertWords2Lines(wordResults);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(1, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getStartOffset());
 assertEquals(48, lineResult1.getEndOffset());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(2, lineResult2.getLineNumber());
 assertEquals(30, lineResult2.getStartOffset());
 assertEquals(52, lineResult2.getEndOffset());
 }

 @After
 public void tearDown() {
 searchableTextPane = null;
 }

}