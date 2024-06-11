package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Line.Border;
import javax.swing.text.LineElement;
import javax.swing.text.LineView;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private List<LineResult> lineResults;
 private String term;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 4);
 term = "JUnit";
 }

 @After
 public void tearDown() {
 searchableTextPane = null;
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptySearchTerm() {
 lineResults = searchableTextPane.highlight(null, 0);
 assertNull(lineResults);
 }

 @Test
 public void testHighlightNullSearchTerm() {
 lineResults = searchableTextPane.highlight((String) null, 0);
 assertNull(lineResults);
 }

 @Test
 public void testHighlightSearchTermInEmptyTextPane() {
 searchableTextPane.setText("");
 lineResults = searchableTextPane.highlight(term, 0);
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlightSearchTermInTextPane() throws BadLocationException {
 Document doc = new PlainDocument();
 doc.insertString(0, "This is a Java JUnit test for the SearchableTextPane class.", null);
 searchableTextPane.setDocument(doc);
 lineResults = searchableTextPane.highlight(term, 0);
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(6, lineResult.getStartLineOffset());
 assertEquals(18, lineResult.getEndLineOffset());
 }

 @Test
 public void testHighlightSearchTermInTextPaneWithMultipleOccurrences() throws BadLocationException {
 Document doc = new PlainDocument();
 doc.insertString(0, "This is a Java JUnit test for the SearchableTextPane class. JUnit is a popular testing framework. JUnit is a popular testing framework.", null);
 searchableTextPane.setDocument(doc);
 lineResults = searchableTextPane.highlight(term, 0);
 assertNotNull(lineResults);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(6, lineResult1.getStartLineOffset());
 assertEquals(11, lineResult1.getEndLineOffset());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(0, lineResult2.getLineNumber());
 assertEquals(26, lineResult2.getStartLineOffset());
 assertEquals(31, lineResult2.getEndLineOffset());
 }

 @Test
 public void testSearchEmptySearchTerm() {
 int pos = searchableTextPane.search("");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchNullSearchTerm() {
 int pos = searchableTextPane.search((String) null);
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchSearchTermInEmptyTextPane() {
 int pos = searchableTextPane.search("");
 assertEquals(-1, pos);
 }

 @Test
 public void testSearchSearchTermInTextPane() throws BadLocationException {
 Document doc = new PlainDocument();
 doc.insertString(0, "This is a Java JUnit test for the SearchableTextPane class.", null);
 searchableTextPane.setDocument(doc);
 pos = searchableTextPane.search(term);
 assertEquals(6, pos);
 }

 @Test
 public void testSearchSearchTermNotInTextPane() throws BadLocationException {
 Document doc = new PlainDocument();
 doc.insertString(0, "This is a Java JUnit test for the SearchableTextPane class.", null);
 pos = searchableTextPane.search("JUnitTest");
 assertEquals(-1, pos);
 }

 @Test
 public void testConvertWords2Lines() throws BadLocationException {
 Document doc = new PlainDocument();
 doc.insertString(0, "Line 1: This is line one.\nLine 2: This is line two.\nLine 3: This is line three.", null);
 searchableTextPane.setDocument(doc);
 List<WordResult> words = new ArrayList<>();
 words.add(new WordResult(3, 12, 15));
 words.add(new WordResult(3, 18, 21));
 lineResults = searchableTextPane.convertWords2Lines(words);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getLineNumber());
 assertEquals(6, lineResult.getStartLineOffset());
 assertEquals(19, lineResult.getEndLineOffset());
 }

 @Test
 public void testConvertWords2LinesWithMultipleLines() throws BadLocationException {
 Document doc = new PlainDocument();
 doc.insertString(0, "Line 1: This is line one.\nLine 2: This is line two.\nLine 3: This is line three.\nLine 4: This is line four.", null);
 searchableTextPane.setDocument(doc);
 List<WordResult> words = new ArrayList<>();
 words.add(new WordResult(2, 11, 13));
 words.add(new WordResult(3, 15, 18));
 lineResults = searchableTextPane.convertWords2Lines(words);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(1, lineResult1.getLineNumber());
 assertEquals(7, lineResult1.getStartLineOffset());
 assertEquals(13, lineResult1.getEndLineOffset());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(2, lineResult2.getLineNumber());
 assertEquals(15, lineResult2.getStartLineOffset());
 assertEquals(18, lineResult2.getEndLineOffset());
 }

 @Test
 public void testAddHighlight() throws BadLocationException {
 Document doc = new PlainDocument();
 searchableTextPane.setDocument(doc);
 int start = 0;
 int length = 4;
 searchableTextPane.addHighlight(start, length);
 LineView lineView = searchableTextPane.getLineView(0);
 LineElement lineElement = lineView.getLineElement();
 SimpleAttributeSet attributeSet = new SimpleAttributeSet();
 StyleConstants.setBackground(attributeSet, Color.YELLOW);
 assertEquals(attributeSet, lineElement.getAttributes());
 }

 @Test
 public void testRemoveHighlights() {
 searchableTextPane.removeHighlights();
 assertTrue(searchableTextPane.getHighlighter().getHighlights().isEmpty());
 }

 @Test
 public void testSearchAndHighlight() throws BadLocationException {
 Document doc = new PlainDocument();
 doc.insertString(0, "This is a Java JUnit test for the SearchableTextPane class. JUnit is a popular testing framework.", null);
 searchableTextPane.setDocument(doc);
 int startSearchPosition = 6;
 int endSearchPosition = 11;
 searchableTextPane.search(term);
 searchableTextPane.highlight(term, 0);
 LineView lineView = searchableTextPane.getLineView(0);
 LineElement lineElement = lineView.getLineElement();
 SimpleAttributeSet attributeSet = new SimpleAttributeSet();
 assertEquals(attributeSet, lineElement.getAttributes());
 lineElement = lineView.findElement(startSearchPosition);
 assertNotNull(lineElement);
 assertEquals(Color.YELLOW, lineElement.getBackground());
 }

 @Test
 public void testSearchAndHighlightWithMultipleOccurrences() throws BadLocationException {
 Document doc = new PlainDocument();
 doc.insertString(0, "This is a Java JUnit test for the SearchableTextPane class. JUnit is a popular testing framework. JUnit is a popular testing framework.", null);
 searchableTextPane.setDocument(doc);
 int startSearchPosition = 6;
 int endSearchPosition = 11;
 searchableTextPane.search(term);
 searchableTextPane.highlight(term, 0);
 LineView lineView1 = searchableTextPane.getLineView(0);
 LineElement lineElement1 = lineView1.getLineElement();
 assertEquals(Color.YELLOW, lineElement1.getBackground());
 LineView lineView2 = searchableTextPane.getLineView(0);
 LineElement lineElement2 = lineView2.findElement(26);
 assertNotNull(lineElement2);
 assertEquals(Color.YELLOW, lineElement2.getBackground());
 }

 @Test
 public void testSearchAndHighlightWithScrolling() throws BadLocationException {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 JScrollPane scrollPane = new JScrollPane(searchableTextPane);
 frame.add(scrollPane);
 frame.setSize(400, 400);
 frame.setVisible(true);
 Document doc = new PlainDocument();
 doc.insertString(0, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac risus quis mi bibendum iaculis. Vivamus vel leo eu sapien iaculis bibendum. Sed vulputate, magna a ultrices dapibus, tellus metus tristique sapien, vel pharetra magna mi non mauris. Donec iaculis, arcu et lobortis condimentum, lorem quam laoreet magna, ac consequat lorem nunc vel purus.", null);
 searchableTextPane.setDocument(doc);
 int startSearchPosition = 30;
 int endSearchPosition = 40;
 searchableTextPane.search(term);
 searchableTextPane.highlight(term, 0);
 LineView lineView = searchableTextPane.getLineView(1);
 LineElement lineElement = lineView.findElement(startSearchPosition - 1);
 assertEquals(Color.YELLOW, lineElement.getBackground());
 searchableTextPane.setCaretPosition(startSearchPosition);
 FrameUtilities.centerFrame(frame);
 }
}