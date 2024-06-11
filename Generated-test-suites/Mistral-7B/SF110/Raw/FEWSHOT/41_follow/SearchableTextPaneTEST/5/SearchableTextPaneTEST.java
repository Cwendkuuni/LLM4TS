package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private JTextPane textPane;
 private List<LineResult> lineResults;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8);
 textPane = new JTextPane();
 textPane.setDocument(new PlainDocument());
 searchableTextPane.setEditorKit(textPane.getEditorKit());
 searchableTextPane.setBackground(Color.WHITE);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlight() {
 String term = "Java";
 List<WordResult> wordResults = new ArrayList<>();
 wordResults.add(new WordResult(5, 10, 15, term));
 lineResults = searchableTextPane.convertWords2Lines(wordResults);
 searchableTextPane.highlight(term, 0);
 assertEquals(1, lineResults.size());
 assertEquals(5, lineResults.get(0).getLineNumber());
 }

 @Test
 public void testSearch() {
 String term = "Java";
 int pos = searchableTextPane.search(term);
 assertEquals(-1, pos);

 String text = "This is a Java text pane.";
 Document document = new PlainDocument();
 try {
 document.insertString(0, text, null);
 searchableTextPane.setDocument(document);
 } catch (BadLocationException e) {
 e.printStackTrace();
 }

 pos = searchableTextPane.search(term);
 assertEquals(6, pos);

 term = "text";
 pos = searchableTextPane.search(term, 6);
 assertEquals(11, pos);
 }

 @Test
 public void testSearchPrevious() {
 String term = "Java";
 searchableTextPane.search(term);
 searchableTextPane.search(term);
 int pos = searchableTextPane.search(term, 0);
 assertEquals(-1, pos);
 }

 @Test
 public void testConvertWords2Lines() {
 String term = "Java";
 List<WordResult> wordResults = new ArrayList<>();
 wordResults.add(new WordResult(5, 10, 15, term));
 wordResults.add(new WordResult(25, 20, 30, term));
 lineResults = searchableTextPane.convertWords2Lines(wordResults);
 assertEquals(2, lineResults.size());
 assertEquals(1, lineResults.get(0).getLineNumber());
 assertEquals(2, lineResults.get(1).getLineNumber());
 }

 @Test
 public void testUpdateWordResult() throws BadLocationException {
 String term = "Java";
 int start = 5;
 int end = 15;
 WordResult wordResult = new WordResult(start, start, end, term);
 LineResult lineResult = new LineResult(1, 1, 100);
 searchableTextPane.updateWordResult(wordResult, lineResult);
 assertEquals(1, lineResult.getLineNumber());
 assertEquals(5, lineResult.getStartLineOffset());
 assertEquals(10, lineResult.getEndLineOffset());
 }

 @Test
 public void testSearchWithHighlighter() {
 String term = "Java";
 List<WordResult> wordResults = new ArrayList<>();
 wordResults.add(new WordResult(5, 10, 15, term));
 lineResults = searchableTextPane.convertWords2Lines(wordResults);
 searchableTextPane.highlight(term, 0);
 searchableTextPane.search(term);

 JFrame frame = new JFrame();
 JScrollPane scrollPane = new JScrollPane(searchableTextPane);
 frame.getContentPane().add(scrollPane);
 frame.pack();
 frame.setVisible(true);

 Highlighter.Highlight[] highlights = searchableTextPane.getHighlighter().getHighlights();
 assertEquals(1, highlights.length);
 assertEquals(5, highlights[0].getStartOffset());
 assertEquals(15, highlights[0].getEndOffset());
 }
}

class WordResult {
 int start;
 int end;
 String term;
 Element parent;
 int lineNumber;

 public WordResult(int start, int end, int length, String term) {
 this.start = start;
 this.end = end;
 this.term = term;
 }
}

class LineResult {
 int lineNumber;
 int startLineOffset;
 int endLineOffset;
 List<WordResult> wordResults;

 public LineResult(int lineNumber, int startLineOffset, int endLineOffset) {
 this.lineNumber = lineNumber;
 this.startLineOffset = startLineOffset;
 this.endLineOffset = endLineOffset;
 this.wordResults = new ArrayList<>();
 }

 public void addWord(WordResult wordResult) {
 this.wordResults.add(wordResult);
 }
}