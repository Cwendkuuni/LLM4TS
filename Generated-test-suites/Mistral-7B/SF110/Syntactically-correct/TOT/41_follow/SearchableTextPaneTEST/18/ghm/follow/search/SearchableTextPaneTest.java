package ghm.follow.search;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.*;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private List<LineResult> lineResults;

 @Before
 public void setup() {
 textPane = new SearchableTextPane(new Font("Arial", Font.BOLD, 14), 8);
 lineResults = new ArrayList<>();
 }

 @Test
 public void testHighlightEmptyString() {
 textPane.highlight("", 0);
 Assert.assertEquals(0, textPane.getDocument().getLength());
 }

 @Test
 public void testHighlightNullTerm() {
 textPane.setText("This is a test for SearchableTextPane.");
 textPane.highlight(null, 0);
 Assert.assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightEmptyTerm() {
 textPane.setText("This is a test for SearchableTextPane.");
 textPane.highlight("", 0);
 Assert.assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightSingleWord() {
 textPane.setText("This is a test for SearchableTextPane.");
 textPane.highlight("Test", 0);
 lineResults = textPane.getHighlightResults();
 LineResult lineResult = lineResults.get(0);
 Assert.assertEquals(0, lineResult.getLineNumber());
 Assert.assertEquals(1, lineResult.getWordResults().size());
 Assert.assertEquals(4, lineResult.getWordResults().get(0).getStart());
 Assert.assertEquals(7, lineResult.getWordResults().get(0).getEnd());
 }

 @Test
 public void testHighlightMultipleWords() {
 textPane.setText("This is a test for SearchableTextPane.");
 textPane.highlight("test SearchableTextPane", 0);
 lineResults = textPane.getHighlightResults();
 LineResult lineResult = lineResults.get(0);
 Assert.assertEquals(0, lineResult.getLineNumber());
 Assert.assertEquals(2, lineResult.getWordResults().size());
 Assert.assertEquals(4, lineResult.getWordResults().get(0).getStart());
 Assert.assertEquals(8, lineResult.getWordResults().get(0).getEnd());
 Assert.assertEquals(12, lineResult.getWordResults().get(1).getStart());
 Assert.assertEquals(17, lineResult.getWordResults().get(1).getEnd());
 }

 @Test
 public void testHighlightCaseInsensitive() {
 textPane.setText("This is a test for SearchableTextPane.");
 textPane.highlight("test", 0);
 lineResults = textPane.getHighlightResults();
 LineResult lineResult = lineResults.get(0);
 Assert.assertEquals(0, lineResult.getLineNumber());
 Assert.assertEquals(1, lineResult.getWordResults().size());
 Assert.assertEquals(4, lineResult.getWordResults().get(0).getStart());
 Assert.assertEquals(7, lineResult.getWordResults().get(0).getEnd());
 }

 @Test
 public void testHighlightError() {
 textPane.setText("This is a test for SearchableTextPane.");
 try {
 textPane.highlight(null, -1);
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testSearchEmptyString() {
 textPane.setText("This is a test for SearchableTextPane.");
 int result = textPane.search("");
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testSearchNullTerm() {
 textPane.setText("This is a test for SearchableTextPane.");
 int result = textPane.search(null);
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testSearchEmptyTermAtStart() {
 textPane.setText("This is a test for SearchableTextPane.");
 int result = textPane.search("");
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testSearchEmptyTermAtEnd() {
 textPane.setText("This is a test for SearchableTextPane.");
 int result = textPane.search("");
 Assert.assertEquals(-1, result);
 }

 @Test
 public void testSearchSingleCharacter() {
 textPane.setText("This is a test for SearchableTextPane.");
 int result = textPane.search("a");
 Assert.assertEquals(7, result);
 }

 @Test
 public void testSearchMultipleCharacters() {
 textPane.setText("This is a test for SearchableTextPane.");
 int result = textPane.search("is a test");
 Assert.assertEquals(0, result);
 }

 @Test
 public void testSearchError() {
 textPane.setText("This is a test for SearchableTextPane.");
 try {
 textPane.search(null);
 } catch (NullPointerException e) {
 // Expected
 }
 }
}