package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Element;
import javax.swing.text.Line.Border;
import javax.swing.text.LineElement;
import javax.swing.text.LineIterator;
import javax.swing.text.LineNumberModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private JTextArea textArea;
 private DefaultHighlighter highlighter;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 this.textArea = mock(JTextArea.class);
 this.highlighter = mock(DefaultHighlighter.class);
 this.searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8) {
 private Document document = mock(Document.class);
 private StyledDocument styledDocument = mock(StyledDocument.class);

 @Override
 public Document getDocument() {
 return this.document;
 }

 @Override
 public void setDocument(Document document) {
 this.document = document;
 this.styledDocument = (StyledDocument) document;
 }

 @Override
 public void setDtd(DocumentTypeDTD dtd) {}

 @Override
 public void readLock() {}

 @Override
 public void writeLock() {}

 @Override
 public void setText(String text) {
 this.styledDocument.remove(0, this.styledDocument.getLength());
 this.styledDocument.insertString(0, text, null);
 }

 @Override
 public void setTabSize(int tabSize) {
 super.setTabSize(tabSize);
 }

 @Override
 public boolean getScrollableTracksViewportWidth() {
 return this.textArea.getScrollableTracksViewportWidth();
 }
 };
 this.searchableTextPane.setDocument(this.styledDocument);
 this.searchableTextPane.setHighlighter(this.highlighter);
 }

 @After
 public void tearDown() {
 this.searchableTextPane = null;
 this.textArea = null;
 this.highlighter = null;
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertFalse(this.searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testSearchableTextPaneConstructionWithFontAndTabSize() {
 final Font font = new Font("Serif", Font.BOLD, 14);
 final int tabSize = 8;
 SearchableTextPane searchableTextPane = new SearchableTextPane(font, tabSize);
 assertNotNull(searchableTextPane);
 assertEquals(font, searchableTextPane.getFont());
 assertEquals(tabSize, searchableTextPane.getTabSize());
 }

 @Test
 public void testHighlightNullTerm() {
 this.searchableTextPane.highlight(null, 0);
 assertNull(this.searchableTextPane.getLastSearchTerm());
 }

 @Test
 public void testHighlightEmptyTerm() {
 this.searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 this.searchableTextPane.highlight("", 0);
 assertNull(this.searchableTextPane.getLastSearchTerm());
 }

 @Test
 public void testHighlightTermWithNoMatches() {
 this.searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 this.searchableTextPane.highlight("zebra", 0);
 assertNull(this.searchableTextPane.getLastSearchTerm());
 }

 @Test
 public void testHighlightTermWithMatches() throws BadLocationException {
 this.searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 this.searchableTextPane.highlight("lazy", SearchableTextPane.DEFAULT_SEARCH_FLAGS);
 final List<LineResult> lineResults = this.searchableTextPane.getHighlightedLines();
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 final LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 final int lineStart = this.searchableTextPane.getLineStartOffset(0);
 final int lineEnd = this.searchableTextPane.getLineEndOffset(0);
 assertEquals(lineStart + 11, lineResult.getStartOffset());
 assertEquals(lineEnd, lineResult.getEndOffset());
 final List<WordResult> wordResults = lineResult.getWordResults();
 assertNotNull(wordResults);
 assertEquals(1, wordResults.size());
 final WordResult wordResult = wordResults.get(0);
 assertEquals("lazy", wordResult.getTerm());
 assertEquals(12, wordResult.getStart());
 assertEquals(14, wordResult.getEnd());
 }

 @Test
 public void testSearchNullTerm() {
 assertNull(this.searchableTextPane.search(null));
 }

 @Test
 public void testSearchEmptyTerm() {
 this.searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 assertEquals(-1, this.searchableTextPane.search(""));
 }

 @Test
 public void testSearchTermWithNoMatches() {
 this.searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 assertEquals(-1, this.searchableTextPane.search("zebra"));
 }

 @Test
 public void testSearchTermWithMatches() throws BadLocationException {
 this.searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 assertEquals(12, this.searchableTextPane.search("lazy"));
 }

 @Test
 public void testSearchTermWithMatchesAndPreviousTerm() throws BadLocationException {
 this.searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 this.searchableTextPane.search("lazy");
 assertEquals(12, this.searchableTextPane.search("lazy"));
 }

 @Test
 public void testConvertWords2Lines() throws BadLocationException {
 this.searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 final List<WordResult> wordResults = Arrays.asList(
 new WordResult("The", 0, 3),
 new WordResult("quick", 3, 6),
 new WordResult("brown", 6, 8),
 new WordResult("fox", 8, 10),
 new WordResult("jumps", 10, 12),
 new WordResult("over", 12, 13),
 new WordResult("the", 13, 15),
 new WordResult("lazy", 15, 18),
 new WordResult("dog.", 18, 21)
 );
 final List<LineResult> lineResults = this.searchableTextPane.convertWords2Lines(wordResults);
 final LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 final int lineStart = this.searchableTextPane.getLineStartOffset(0);
 final int lineEnd = this.searchableTextPane.getLineEndOffset(0);
 assertEquals(lineStart, lineResult.getStartOffset());
 assertEquals(lineEnd, lineResult.getEndOffset());
 final List<WordResult> words = lineResult.getWordResults();
 assertEquals(9, words.size());
 for (int i = 0; i < words.size(); ++i) {
 final WordResult wordResult = words.get(i);
 final WordResult expectedWordResult = wordResults.get(i);
 assertEquals(expectedWordResult.getTerm(), wordResult.getTerm());
 assertEquals(expectedWordResult.getStart(), wordResult.getStart());
 assertEquals(expectedWordResult.getEnd(), wordResult.getEnd());
 }
 }

 @Test
 public void testAddHighlight() throws BadLocationException {
 this.searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 final int start = 15;
 final int length = 3;
 this.searchableTextPane.addHighlight(start, length);
 final Element element = this.searchableTextPane.getDocument().getDefaultRootElement();
 final LineIterator lineIterator = this.searchableTextPane.getLineIterator();
 lineIterator.next();
 final LineElement lineElement = lineIterator.getCurrentElement();
 final int lineStart = lineElement.getStartOffset();
 final int lineEnd = lineElement.getEndOffset();
 final int offset = lineStart + start;
 final AttributeSet attributeSet = lineElement.getAttributes();
 final SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
 simpleAttributeSet.addAttribute(SimpleAttributeSet.AttributeName.FOREGROUND, Color.YELLOW);
 verify(this.highlighter).addHighlight(offset, length, argThat(isA(DefaultHighlightPainter.class)));
 }

 @Test
 public void testRemoveHighlights() throws BadLocationException {
 this.searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 final int start = 15;
 final int length = 3;
 this.searchableTextPane.addHighlight(start, length);
 this.searchableTextPane.removeHighlights();
 final Element element = this.searchableTextPane.getDocument().getDefaultRootElement();
 final LineIterator lineIterator = this.searchableTextPane.getLineIterator();
 lineIterator.next();
 final LineElement lineElement = lineIterator.getCurrentElement();
 final int lineStart = lineElement.getStartOffset();
 final int lineEnd = lineElement.getEndOffset();
 final int offset = lineStart + start;
 final AttributeSet attributeSet = lineElement.getAttributes();
 final SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
 verifyZeroInteractions(this.highlighter);
 assertFalse(attributeSet.isDefined(SimpleAttributeSet.AttributeName.FOREGROUND));
 }

 @Test
 public void testSearchEngineSearchNullTerm() throws BadLocationException {
 final SearchEngine searchEngine = new SearchEngine(SearchableTextPane.DEFAULT_SEARCH_FLAGS);
 List<WordResult> wordResults = searchEngine.search(null, "The quick brown fox jumps over the lazy dog.");
 assertNull(wordResults);
 }

 @Test
 public void testSearchEngineSearchEmptyTerm() throws BadLocationException {
 final SearchEngine searchEngine = new SearchEngine(SearchableTextPane.DEFAULT_SEARCH_FLAGS);
 List<WordResult> wordResults = searchEngine.search("", "The quick brown fox jumps over the lazy dog.");
 assertNull(wordResults);
 }

 @Test
 public void testSearchEngineSearchTermWithNoMatches() throws BadLocationException {
 final SearchEngine searchEngine = new SearchEngine(SearchableTextPane.DEFAULT_SEARCH_FLAGS);
 List<WordResult> wordResults = searchEngine.search("zebra", "The quick brown fox jumps over the lazy dog.");
 assertNull(wordResults);
 }

 @Test
 public void testSearchEngineSearchTermWithMatches() throws BadLocationException {
 final SearchEngine searchEngine = new SearchEngine(SearchableTextPane.DEFAULT_SEARCH_FLAGS);
 List<WordResult> wordResults = searchEngine.search("lazy", "The quick brown fox jumps over the lazy dog.");
 assertEquals(1, wordResults.size());
 final WordResult wordResult = wordResults.get(0);
 assertEquals(15, wordResult.getStart());
 assertEquals(18, wordResult.getEnd());
 }

 @Test
 public void testUpdateWordResult() throws BadLocationException {
 final LineResult lineResult = new LineResult(0, -1, -1);
 final WordResult wordResult = new WordResult("lazy", 15, 18);
 this.searchableTextPane.updateWordResult(wordResult, lineResult);
 assertEquals(0, lineResult.getLineNumber());
 final int lineStart = this.searchableTextPane.getLineStartOffset(0);
 final int lineEnd = this.searchableTextPane.getLineEndOffset(0);
 assertEquals(lineStart + 11, lineResult.getStartOffset());
 assertEquals(lineEnd, lineResult.getEndOffset());
 final List<WordResult> words = lineResult.getWordResults();
 assertEquals(1, words.size());
 assertSame(wordResult, words.get(0));
 }

 @Test
 public void testSearchableTextPaneWithJFrame() throws Exception {
 final JFrame jFrame = new JFrame();
 jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 jFrame.setSize(640, 480);
 jFrame.setVisible(true);
 this.searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 8) {
 @Override
 public void setBounds(int x, int y, int width, int height) {
 super.setBounds(x, y, width, height);
 this.setParent(jFrame.getContentPane());
 }
 };
 this.searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 Thread.sleep(1000); // Give time for JFrame to be displayed
 this.searchableTextPane.highlight("lazy", SearchableTextPane.DEFAULT_SEARCH_FLAGS);
 final JTextArea textArea = this.searchableTextPane;
 final ArgumentCaptor<Runnable> runnableArgumentCaptor = argumentCaptor(Runnable.class);
 verify(jFrame).add(textArea, argThat(new EqualsMatcher(Border.LINE_BELOW)));
 verify(jFrame).pack();
 verify(jFrame).validate();
 verify(this.highlighter).addHighlight(15, 3, argThat(isA(DefaultHighlightPainter.class)));
 }

}