package ghm.follow.search;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Line.BoundedLine;
import javax.swing.text.LineElement;
import javax.swing.text.LineView;
import javax.swing.text.Position.Bias;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private Document mockDocument;
 private JFrame mockFrame;
 private JScrollPane mockScrollPane;

 @Before
 public void setUp() throws Exception {
 MockitoAnnotations.initMocks(this);
 mockDocument = mock(Document.class);
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 4);
 searchableTextPane.setDocument(mockDocument);
 mockScrollPane = mock(JScrollPane.class);
 mockFrame = new JFrame();
 mockFrame.getContentPane().add(mockScrollPane);
 mockScrollPane.setViewportView(searchableTextPane);
 }

 @After
 public void tearDown() throws Exception {
 searchableTextPane = null;
 mockDocument = null;
 mockFrame = null;
 mockScrollPane = null;
 }

 @Test
 public void getScrollableTracksViewportWidthTest() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void highlightTest() {
 final String term = "test";
 final int flags = JOptionPane.BALLOON_MESSAGE;
 List<LineResult> lineResults = Arrays.asList(new LineResult(1, 1, 10), new LineResult(2, 2, 20), new LineResult(3, 3, 30));
 List<WordResult> searchResults = Arrays.asList(new WordResult(1, 5, 3, "test"), new WordResult(2, 8, 4, "test"), new WordResult(3, 15, 5, "test"));
 when(mockDocument.getText(0, mockDocument.getLength())).thenReturn("Some quick brown fox jumps over the lazy dog test test test test test.");
 doReturn(lineResults).when(searchableTextPane).convertWords2Lines(searchResults);
 searchableTextPane.highlight(term, flags);
 verify(mockDocument, times(1)).getText(0, mockDocument.getLength());
 LineView lineView = mock(LineView.class);
 BoundedLine boundedLine = mock(BoundedLine.class);
 when(searchableTextPane.getLineView(0)).thenReturn(lineView);
 when(lineView.getLineElement()).thenReturn(boundedLine);
 when(boundedLine.getModel()).thenReturn(mockDocument);
 LineElement lineElement = mock(LineElement.class);
 when(boundedLine.getComponent(0)).thenReturn(lineElement);
 when(lineElement.getStartOffset()).thenReturn(5);
 when(lineElement.getEndOffset()).thenReturn(15);
 when(lineElement.getParentElement()).thenReturn(mockDocument.getDefaultRootElement());
 Element paragraphElement = mock(Element.class);
 when(Utilities.getParagraphElement(searchableTextPane, 5)).thenReturn(paragraphElement);
 when(paragraphElement.getStartOffset()).thenReturn(0);
 when(paragraphElement.getEndOffset()).thenReturn(30);
 WordResult wordResult = new WordResult(5, 7, 3, "test");
 when(paragraphElement.getElement(Anything.class)).thenReturn(paragraphElement);
 when(searchableTextPane.getLineOfOffset(5)).thenReturn(1);
 when(searchableTextPane.getLineStartOffset(1)).thenReturn(0);
 searchableTextPane.updateWordResult(wordResult, lineResults.get(0));
 List<LineResult> actualLineResults = searchableTextPane.getHighlightedLines();
 assertEquals(lineResults, actualLineResults);
 }

 @Test
 public void removeHighlightsTest() {
 searchableTextPane.highlight("test", 0);
 searchableTextPane.removeHighlights();
 assertEquals(0, searchableTextPane.getHighlighter().getHighlightCount());
 }

 @Test
 public void searchTest() {
 final String term = "test";
 final int startPos = 12;
 int expectedPos = 17;
 int actualPos = searchableTextPane.search(term);
 assertEquals(expectedPos, actualPos);
 }

 @Test
 public void searchTest_whenEmptyTerm_returnsLastSearchPosition() {
 final String term = "";
 final int lastSearchPosition = 12;
 int expectedPos = lastSearchPosition;
 int actualPos = searchableTextPane.search(term);
 assertEquals(expectedPos, actualPos);
 }

 @Test
 public void searchTest_whenNullTerm_returnsLastSearchPosition() {
 final String term = null;
 final int lastSearchPosition = 12;
 int expectedPos = lastSearchPosition;
 int actualPos = searchableTextPane.search(term);
 assertEquals(expectedPos, actualPos);
 }

 @Test
 public void searchTest_whenTermNotPresent_returnsLastSearchPosition() {
 final String term = "notPresent";
 final int lastSearchPosition = 12;
 int expectedPos = lastSearchPosition;
 int actualPos = searchableTextPane.search(term);
 assertEquals(expectedPos, actualPos);
 }

 @Test
 public void searchTest_whenTermPresent_updatesLastSearchPosition() {
 final String term = "test";
 final int lastSearchPosition = 12;
 int expectedPos = 17;
 int startPos = 12;
 int actualPos = searchableTextPane.search(term, startPos);
 assertEquals(expectedPos, actualPos);
 verify(searchableTextPane, times(1)).lastSearchPos = expectedPos;
 }

}