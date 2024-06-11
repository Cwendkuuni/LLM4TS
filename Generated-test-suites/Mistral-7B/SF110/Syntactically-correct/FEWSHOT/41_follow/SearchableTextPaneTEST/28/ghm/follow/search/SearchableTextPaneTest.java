package ghm.follow.search;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.*;
import java.util.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private List<LineResult> lineResults;
 private String term;

 @Before
 public void setup() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 12), 8);
 lineResults = new ArrayList<>();
 term = "example";
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 final Component parent = mock(JFrame.class);
 final ComponentUI ui = mock(SearchableTextPaneUI.class);
 when(searchableTextPane.getParent()).thenReturn(parent);
 when(searchableTextPane.getUI()).thenReturn(ui);
 when(ui.getPreferredSize(searchableTextPane)).thenReturn(new Dimension(200, 200));
 when(parent.getSize()).thenReturn(new Dimension(300, 300));
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyTerm() {
 searchableTextPane.highlight(null, 0);
 assertFalse(searchableTextPane.hasFocusableComponentAt(0, 0));
 }

 @Test
 public void testHighlightTermInText() {
 searchableTextPane.setText("This is an example text.");
 searchableTextPane.highlight(term, 0);
 List<WordResult> searchResults = new ArrayList<>();
 searchResults.add(new WordResult(0, 10, "example", searchableTextPane.getDocument()));
 lineResults = searchableTextPane.convertWords2Lines(searchResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getLineStartOffset());
 assertEquals(25, lineResult.getLineEndOffset());
 assertTrue(searchableTextPane.hasFocusableComponentAt(10, 0));
 }

 @Test
 public void testHighlightTermInMultipleWords() {
 searchableTextPane.setText("This is an example text with multiple example words.");
 searchableTextPane.highlight(term, 0);
 List<WordResult> searchResults = new ArrayList<>();
 searchResults.add(new WordResult(12, 15, "example", searchableTextPane.getDocument()));
 searchResults.add(new WordResult(30, 34, "example", searchableTextPane.getDocument()));
 lineResults = searchableTextPane.convertWords2Lines(searchResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(12, lineResult.getLineStartOffset());
 assertEquals(35, lineResult.getLineEndOffset());
 assertTrue(searchableTextPane.hasFocusableComponentAt(15, 0));
 assertTrue(searchableTextPane.hasFocusableComponentAt(34, 0));
 }

 @Test
 public void testHighlightTermInMultipleLines() {
 searchableTextPane.setText("This is line 1.\nThis is line 2.\nThis is line 3.");
 searchableTextPane.highlight(term, 0);
 List<WordResult> searchResults = new ArrayList<>();
 searchResults.add(new WordResult(15, 19, "example", searchableTextPane.getDocument()));
 searchResults.add(new WordResult(46, 50, "example", searchableTextPane.getDocument()));
 lineResults = searchableTextPane.convertWords2Lines(searchResults);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(15, lineResult1.getLineStartOffset());
 assertEquals(20, lineResult1.getLineEndOffset());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(1, lineResult2.getLineNumber());
 assertEquals(45, lineResult2.getLineStartOffset());
 assertEquals(51, lineResult2.getLineEndOffset());
 assertTrue(searchableTextPane.hasFocusableComponentAt(18, 0));
 assertTrue(searchableTextPane.hasFocusableComponentAt(50, 0));
 }

 @Test
 public void testSearchEmptyTerm() {
 int pos = searchableTextPane.search(null);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchTermInText() {
 searchableTextPane.setText("This is an example text.");
 int pos = searchableTextPane.search(term);
 assertEquals(10, pos);
 }

 @Test
 public void testSearchTermInMultipleOccurrences() {
 searchableTextPane.setText("This is an example text with multiple example words.");
 int pos = searchableTextPane.search(term);
 assertEquals(12, pos);
 int nextPos = searchableTextPane.search(term, pos + term.length());
 assertEquals(30, nextPos);
 }

 @Test
 public void testSearchTermInMultipleLines() {
 searchableTextPane.setText("This is line 1.\nThis is line 2.\nThis is line 3.");
 int pos = searchableTextPane.search(term);
 assertEquals(15, pos);
 int nextPos = searchableTextPane.search(term, pos + term.length());
 assertEquals(46, nextPos);
 }

 @Test
 public void testSearchTermInTextAfterLastSearchTerm() {
 searchableTextPane.setText("This is an example text with multiple example words.");
 searchableTextPane.search("example");
 int pos = searchableTextPane.search("word");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchTermInTextBeforeLastSearchTerm() {
 searchableTextPane.setText("This is an example text with multiple example words.");
 searchableTextPane.search("example");
 int pos = searchableTextPane.search("text");;
 assertEquals(0, pos);
 }

 @Test
 public void testSearchTermInTextAtLastPosition() {
 searchableTextPane.setText("This is an example text with multiple example words.");
 int pos = searchableTextPane.search("words");;
 assertEquals(-1, pos);
 searchableTextPane.search("example");
 int lastPos = searchableTextPane.search("words");
 assertEquals(searchableTextPane.getDocument().getLength(), lastPos);
 }

 @Test
 public void testRemoveHighlights() {
 searchableTextPane.setText("This is an example text.");
 searchableTextPane.highlight(term, 0);
 searchableTextPane.removeHighlights();
 assertFalse(searchableTextPane.hasFocusableComponentAt(10, 0));
 }
}