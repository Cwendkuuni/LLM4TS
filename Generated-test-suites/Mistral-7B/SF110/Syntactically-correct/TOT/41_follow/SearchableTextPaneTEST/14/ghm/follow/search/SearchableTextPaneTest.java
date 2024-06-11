import org.junit.Assert;
import org.junit.Test;
import ghm.follow.search.SearchableTextPane;
import ghm.follow.search.LineResult;
import ghm.follow.search.WordResult;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 14), 8);
 textPane.setText("The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.");
 }

 @Test
 public void testHighlightTerm() {
 String term = "fox";
 List<LineResult> lineResults = textPane.highlight(term, SearchableTextPane.REGULAR_EXPRESSION);
 Assert.assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 Assert.assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 Assert.assertEquals(6, wordResult.getStart());
 Assert.assertEquals(12, wordResult.getEnd());
 Assert.assertEquals(Color.YELLOW, textPane.getBackgroundAt(wordResult.getStart(), wordResult.getLength()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHighlightNullTerm() {
 textPane.highlight(null, SearchableTextPane.REGULAR_EXPRESSION);
 }

 @Test
 public void testNoHighlightTerm() {
 String term = "lazy";
 List<LineResult> lineResults = textPane.highlight(term, SearchableTextPane.REGULAR_EXPRESSION);
 Assert.assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 Assert.assertEquals(0, lineResult.getWordResults().size());
 }

 @Test
 public void testSearchTermAndGoTo() {
 String term = "quick";
 int pos = textPane.search(term);
 textPane.setCaretPosition(pos);
 Assert.assertEquals(0, textPane.getCaretPosition().x);
 Assert.assertEquals(2, textPane.getCaretPosition().y);
 }

 @Test
 public void testSearchTermNotFound() {
 String term = "cat";
 int pos = textPane.search(term);
 Assert.assertEquals(-1, pos);
 }

 @Test
 public void testSearchTermPreviouslyFound() {
 String term = "quick";
 textPane.search(term);
 textPane.search(term);
 int pos = textPane.search(term);
 Assert.assertEquals(textPane.lastSearchPos + term.length(), pos);
 }

 @Test
 public void testSearchTermAndClearHighlights() {
 String term = "quick";
 List<LineResult> lineResults = textPane.highlight(term, SearchableTextPane.REGULAR_EXPRESSION);
 textPane.search(term);
 textPane.removeHighlights();
 List<LineResult> noHighlightLineResults = textPane.highlight(term, SearchableTextPane.REGULAR_EXPRESSION);
 Assert.assertEquals(0, noHighlightLineResults.size());
 }

 @Test
 public void testSearchTermAndMiddleOfWord() {
 String term = "quic";
 int pos = textPane.search(term, 0);
 textPane.setCaretPosition(pos);
 char[] chars = textPane.getText().toCharArray();
 Assert.assertEquals('q', chars[pos]);
 Assert.assertEquals('u', chars[pos + 1]);
 Assert.assertEquals('i', chars[pos + 2]);
 }

 @Test
 public void testSearchTermAtStartOfDocument() {
 String term = "The";
 int pos = textPane.search(term, 0);
 textPane.setCaretPosition(pos);
 Assert.assertEquals(0, textPane.getCaretPosition().x);
 Assert.assertEquals(0, textPane.getCaretPosition().y);
 }

 @Test
 public void testSearchTermAtEndOfDocument() {
 String term = "dog.";
 int pos = textPane.search(term, textPane.getText().length());
 textPane.setCaretPosition(pos);
 Assert.assertEquals(textPane.getText().length() - term.length(), textPane.getCaretPosition().x);
 Assert.assertEquals(textPane.getLineCount() - 1, textPane.getCaretPosition().y);
 }

 @Test
 public void testSearchTermInMiddleOfLine() {
 String term = "brown";
 int pos = textPane.search(term, 0);
 textPane.setCaretPosition(pos);
 Assert.assertEquals(12, textPane.getCaretPosition().x);
 Assert.assertEquals(1, textPane.getCaretPosition().y);
 }

 @Test
 public void testSearchTermInMultipleLines() {
 String term = "quick";
 List<LineResult> lineResults = textPane.highlight(term, SearchableTextPane.REGULAR_EXPRESSION);
 int pos = textPane.search(term);
 textPane.setCaretPosition(pos);
 Assert.assertEquals(0, textPane.getCaretPosition().x);
 Assert.assertEquals(2, textPane.getCaretPosition().y);
 textPane.setCaretPosition(pos + term.length());
 Assert.assertEquals(4, textPane.getCaretPosition().x);
 Assert.assertEquals(2, textPane.getCaretPosition().y);
 }

 @Test
 public void testSearchTermMultipleTerms() {
 String term1 = "quick";
 String term2 = "lazy";
 List<LineResult> lineResults = textPane.highlight(term1, SearchableTextPane.REGULAR_EXPRESSION);
 textPane.search(term1);
 textPane.search(term2);
 List<LineResult> lineResults2 = textPane.highlight(term1, SearchableTextPane.REGULAR_EXPRESSION);
 List<LineResult> lineResults3 = textPane.highlight(term2, SearchableTextPane.REGULAR_EXPRESSION);
 Assert.assertEquals(2, lineResults.size());
 Assert.assertEquals(2, lineResults2.size());
 Assert.assertEquals(0, lineResults3.size());
 }

 @Test
 public void testSearchTermCaseInsensitive() {
 String term = "quick";
 textPane.setSearchFlags(SearchableTextPane.CASE_INSENSITIVE);
 int pos = textPane.search(term);
 textPane.setCaretPosition(pos);
 Assert.assertEquals(0, textPane.getCaretPosition().x);
 Assert.assertEquals(2, textPane.getCaretPosition().y);
 textPane.setSearchFlags(SearchableTextPane.REGULAR_EXPRESSION);
 }

 @Test
 public void testSearchTermWithSpecialCharacters() {
 String term = "The\\. quick brown fox jumps over the lazy dog\\.";
 textPane.setSearchFlags(SearchableTextPane.REGULAR_EXPRESSION);
 int pos = textPane.search(term);
 textPane.setCaretPosition(pos);
 Assert.assertEquals(0, textPane.getCaretPosition().x);
 Assert.assertEquals(0, textPane.getCaretPosition().y);
 textPane.setSearchFlags(SearchableTextPane.REGULAR_EXPRESSION);
 }

 @After
 public void tearDown() {
 textPane = null;
 }
}