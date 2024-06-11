package ghm.follow.search;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.text.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private JTextComponent mockTextComponent;
 private Document mockDocument;
 private Logger mockLogger;

 @Before
 public void setup() {
 mockTextComponent = mock(JTextComponent.class);
 mockDocument = mock(Document.class);
 mockLogger = Mockito.spy(Logger.getLogger(SearchableTextPane.class.getName()));
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8) {
 @Override
 public JTextComponent getTextComponent() {
 return mockTextComponent;
 }

 @Override
 public Document getDocument() {
 return mockDocument;
 }

 @Override
 public Logger getLogger() {
 return mockLogger;
 }
 };
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 final Component parent = mock(Component.class);
 searchableTextPane.setParent(parent);
 final ComponentUI ui = searchableTextPane.getUI();
 final boolean result = searchableTextPane.getScrollableTracksViewportWidth();
 assertTrue(result);
 }

 @Test
 public void testHighlightEmptySearchTerm() {
 searchableTextPane.highlight("", 0);
 verify(mockDocument, never()).getText(anyInt(), anyInt());
 }

 @Test
 public void testHighlightNullSearchTerm() {
 searchableTextPane.highlight(null, 0);
 verify(mockDocument, never()).getText(anyInt(), anyInt());
 }

 @Test
 public void testHighlightSearchTerm() {
 final String searchTerm = "junit";
 searchableTextPane.highlight(searchTerm, 0);
 final List<WordResult> wordResults = Arrays.asList(
 new WordResult(0, searchTerm.length(), 5, 1),
 new WordResult(11, searchTerm.length(), 15, 2)
 );
 final List<LineResult> expectedLineResults = Arrays.asList(
 new LineResult(0, 0, 20),
 new LineResult(1, 11, 16)
 );
 when(mockDocument.getText(anyInt(), anyInt())).thenReturn("The quick brown fox jumps over the lazy dog. The unittest framework for Java is JUnit.");
 doReturn(wordResults).when(searchableTextPane).convertWords2Lines(anyList());
 searchableTextPane.addHighlight(0, searchTerm.length());
 searchableTextPane.addHighlight(11, searchTerm.length());
 final List<LineResult> actualLineResults = searchableTextPane.getHighlightResults();
 assertEquals(expectedLineResults, actualLineResults);
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
 public void testSearchTermFound() {
 final String searchTerm = "junit";
 searchableTextPane.search(searchTerm);
 final int expectedPos = 5;
 when(mockDocument.getText(anyInt(), anyInt())).thenReturn("The quick brown fox jumps over the lazy dog. The unittest framework for Java is JUnit.");
 when(mockDocument.getLength()).thenReturn(22);
 doReturn(1).when(searchableTextPane).search(searchTerm, 0);
 doReturn(expectedPos).when(searchableTextPane).search(searchTerm, expectedPos);
 assertEquals(expectedPos, searchableTextPane.search(searchTerm));
 }

 @Test
 public void testSearchTermNotFound() {
 int result = searchableTextPane.search("brown");
 assertEquals(-1, result);
 }

 @Test(expected = BadLocationException.class)
 public void testSearchWithBadLocationException() {
 final String searchTerm = "junit";searchableTextPane.search(searchTerm);
 final String text = "The quick brown fox jumps over the lazy dog. The unittest framework for Java is JUnit.";
 when(mockDocument.getText(anyInt(), anyInt())).thenThrow(new BadLocationException());
 searchableTextPane.search(searchTerm);
 }
}