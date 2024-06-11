import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.*;
import java.util.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private JFrame frame;
 private JTextArea textArea;
 private SearchableTextPane searchableTextPane;
 private List<LineResult> lineResults;
 private static final String SEARCH_TERM = "junit";

 @Before
 public void setUp() {
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 textArea = new JTextArea();
 frame.getContentPane().add(textArea, BorderLayout.CENTER);
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 16), 8);
 textArea.setDocument(searchableTextPane.getDocument());

 frame.pack();
 frame.setSize(400, 400);
 frame.setVisible(true);
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testSearchableTextPaneConstructor() {
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 16), 8);
 assertNotNull(searchableTextPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 16), 8);
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlightEmptyString() {
 searchableTextPane.highlight("", 0);
 assertFalse(searchableTextPane.hasFocus());
 }

 @Test
 public void testHighlightNullTerm() {
 searchableTextPane.setText("This is a test text pane.");
 searchableTextPane.highlight(null, 0);
 assertFalse(searchableTextPane.hasFocus());
 }

 @Test
 public void testHighlightValidTerm() throws BadLocationException {
 searchableTextPane.setText("This is a test text pane for JUnit 4 testing. JUnit is a popular Java testing framework.");
 lineResults = searchableTextPane.highlight(SEARCH_TERM, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(51, wordResult.getLineOffset());
 assertEquals(53, wordResult.getStart());
 assertEquals(56, wordResult.getEnd());
 }

 @Test
 public void testSearchEmptyString() {
 assertEquals(-1, searchableTextPane.search(""));
 }

 @Test
 public void testSearchNullTerm() {
 assertEquals(-1, searchableTextPane.search(null));
 }

 @Test
 public void testSearchValidTerm() throws BadLocationException {
 searchableTextPane.setText("This is a test text pane for JUnit 4 testing. JUnit is a popular Java testing framework.");
 assertEquals(53, searchableTextPane.search(SEARCH_TERM));
 }

 @Test
 public void testSearchValidTermFromDifferentPosition() throws BadLocationException {
 searchableTextPane.setText("This is a test text pane for JUnit 4 testing. JUnit is a popular Java testing framework.");
 assertEquals(56, searchableTextPane.search(SEARCH_TERM, 54));
 }

 @Test
 public void testSearchTermNotPresent() throws BadLocationException {
 searchableTextPane.setText("This is a test text pane for JUnit 4 testing. JUnit is a popular Java testing framework.");
 assertEquals(-1, searchableTextPane.search("spring"));
 }

 @Test
 public void testConvertWords2LinesEmptyList() throws BadLocationException {
 List<WordResult> wordResults = Collections.emptyList();
 lineResults = searchableTextPane.convertWords2Lines(wordResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testConvertWords2LinesSingleWord() throws BadLocationException {
 List<WordResult> wordResults = Arrays.asList(new WordResult(10, 12, 14));
 lineResults = searchableTextPane.convertWords2Lines(wordResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResult.getWordResults().size());
 WordResult wordResult = lineResult.getWordResults().get(0);
 assertEquals(10, wordResult.getLineOffset());
 assertEquals(12, wordResult.getStart());
 assertEquals(14, wordResult.getEnd());
 }

 @Test
 public void testConvertWords2LinesMultipleWords() throws BadLocationException {
 List<WordResult> wordResults = Arrays.asList(
 new WordResult(10, 12, 14),
 new WordResult(18, 20, 22),
 new WordResult(34, 36, 38)
 );
 lineResults = searchableTextPane.convertWords2Lines(wordResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(3, lineResult.getWordResults().size());
 }
}