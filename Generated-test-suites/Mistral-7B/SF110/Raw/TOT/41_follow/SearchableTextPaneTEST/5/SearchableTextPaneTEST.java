import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private List<LineResult> lineResults;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 12), 4);
 lineResults = new ArrayList<>();
 }

 @Test
 public void testSearchableTextPaneConstruction() {
 Assert.assertNotNull(searchableTextPane);
 Assert.assertEquals(12, searchableTextPane.getFont().getSize());
 Assert.assertEquals(4, searchableTextPane.getTabSize());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 //Scenario: TextPane is smaller than its parent
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 Container container = frame.getContentPane();
 container.setLayout(new BorderLayout());
 container.add(searchableTextPane, BorderLayout.CENTER);
 frame.pack();
 Assert.assertTrue(searchableTextPane.getScrollableTracksViewportWidth());

 //Scenario: TextPane is larger than its parent
 JFrame frame2 = new JFrame();
 frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 Container container2 = frame2.getContentPane();
 container2.add(searchableTextPane, BorderLayout.CENTER);
 Font font = new Font("Serif", Font.PLAIN, 20);
 searchableTextPane = new SearchableTextPane(font, 4);
 frame2.pack();
 Assert.assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyTerm() {
 searchableTextPane.highlight("", 0);
 Assert.assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightNullTerm() {
 searchableTextPane.highlight(null, 0);
 Assert.assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightTermInMiddleOfText() throws Exception {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog");
 String term = "lazy";
 List<WordResult> searchResults = new ArrayList<>();
 searchResults.add(new WordResult(13, 16));
 searchResults.add(new WordResult(29, 33));
 lineResults = searchableTextPane.highlight(term, 0);
 Assert.assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 Assert.assertEquals(0, lineResult.getLineNumber());
 Assert.assertEquals(13, lineResult.getStartLineOffset());
 Assert.assertEquals(40, lineResult.getEndLineOffset());
 Assert.assertEquals(2, lineResult.getWordResults().size());
 }

 @Test
 public void testHighlightTermAtEndOfText() throws Exception {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog");
 String term = "dog";
 List<WordResult> searchResults = new ArrayList<>();
 searchResults.add(new WordResult(41, 44));
 lineResults = searchableTextPane.highlight(term, 0);
 Assert.assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 Assert.assertEquals(5, lineResult.getLineNumber());
 Assert.assertEquals(39, lineResult.getStartLineOffset());
 Assert.assertEquals(45, lineResult.getEndLineOffset());
 Assert.assertEquals(1, lineResult.getWordResults().size());
 }

 @Test(expected = BadLocationException.class)
 public void testHighlightTermWithInvalidPosition() throws Exception {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog");
 String term = "cat";
 searchableTextPane.highlight(term, 100);
 }

 @Test
 public void testSearchEmptyTerm() {
 int position = searchableTextPane.search("");
 Assert.assertEquals(-1, position);
 }

 @Test
 public void testSearchNullTerm() {
 int position = searchableTextPane.search(null);
 Assert.assertEquals(-1, position);
 }

 @Test
 public void testSearchTermNotInText() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog");
 int position = searchableTextPane.search("cat");
 Assert.assertEquals(-1, position);
 }

 @Test
 public void testSearchTermInMiddleOfText() throws Exception {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog");
 int position = searchableTextPane.search("lazy");
 Assert.assertEquals(13, position);
 }

 @Test
 public void testSearchTermAtEndOfText() throws Exception {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog");
 int position = searchableTextPane.search("dog");
 Assert.assertEquals(41, position);
 }

 @Test(expected = BadLocationException.class)
 public void testSearchWithInvalidPosition() throws Exception {
 searchableTextPane.search("lazy", 100);
 }

 @Test
 public void testSearchTwiceSameTerm() throws Exception {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog");
 int position = searchableTextPane.search("lazy");
 Assert.assertEquals(13, position);
 int position2 = searchableTextPane.search("lazy");
 Assert.assertEquals(28, position2);
 }
}