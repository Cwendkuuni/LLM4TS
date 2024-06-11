package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Line;
import javax.swing.text.LineElement;
import javax.swing.text.LineNumberHandler;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private JFrame frame;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.add(new JScrollPane(searchableTextPane), JScrollPane.CENTER);
 frame.setSize(600, 600);
 frame.setVisible(true);
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testConstructor() {
 assertEquals(new Font("Arial", Font.PLAIN, 12), searchableTextPane.getFont());
 assertEquals(4, searchableTextPane.getTabSize());
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
}

 @Test
 public void testHighlightEmptyText() {
 searchableTextPane.highlight(null, 0);
 List<LineResult> lineResults = searchableTextPane.getLineResults();
 assertEquals(0, lineResults.size());
}

 @Test
 public void testHighlightSingleWord() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 searchableTextPane.highlight("fox", 0);
 List<LineResult> lineResults = searchableTextPane.getLineResults();
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResults.size());
 assertEquals(1, lineResult.getWordResults().size());
 assertEquals(10, lineResult.getWordResults().get(0).getStart());
 assertEquals(12, lineResult.getWordResults().get(0).getEnd());
}

 @Test
 public void testHighlightMultipleWords() {
 searchableTextPane.setText("The quick brown fox jumps over the quick lazy dog.");
 searchableTextPane.highlight("quick lazy", 0);
 List<LineResult> lineResults = searchableTextPane.getLineResults();
 LineResult lineResult = lineResults.get(0);
 assertEquals(1, lineResults.size());
 assertEquals(2, lineResult.getWordResults().size());
 assertEquals(3, lineResult.getWordResults().get(0).getStart());
 assertEquals(8, lineResult.getWordResults().get(0).getEnd());
 assertEquals(14, lineResult.getWordResults().get(1).getStart());
 assertEquals(22, lineResult.getWordResults().get(1).getEnd());
}

 @Test
 public void testSearchEmptyText() {
 assertEquals(-1, searchableTextPane.search(null));
}

 @Test
 public void testSearchSingleWord() {
 searchableTextPane.setText("The quick brown fox jumps over the lazy dog.");
 assertEquals(10, searchableTextPane.search("fox"));
}

 @Test
 public void testSearchMultipleWords() {
 searchableTextPane.setText("The quick brown fox jumps over the quick lazy dog.");
 assertEquals(3, searchableTextPane.search("quick lazy"));
}

 @Test
 public void testSearchPreviousWord() {
 searchableTextPane.setText("The quick brown fox jumps over the quick lazy dog.");
 assertEquals(3, searchableTextPane.search("quick"));
 assertEquals(10, searchableTextPane.search("fox"));
}

 @Test
 public void testRemoveHighlights() {
 searchableTextPane.setText("The quick brown fox jumps over the quick lazy dog.");
 searchableTextPane.highlight("quick lazy", 0);
 searchableTextPane.removeHighlights();
 List<LineResult> lineResults = searchableTextPane.getLineResults();
 assertEquals(0, lineResults.size());
}

 @Test
 public void testAddHighlight() throws BadLocationException {
 Document doc = searchableTextPane.getDocument();
 SimpleAttributeSet highlightAttributeSet = new SimpleAttributeSet();
 StyleConstants.setBackground(highlightAttributeSet, Color.YELLOW);
 doc.setCharacterAttribute(0, 3, highlightAttributeSet, false);
 Element element = doc.getDefaultRootElement();
 LineNumberHandler lineNumberHandler = (LineNumberModel) searchableTextPane.getDocument().getDefaultDocumentEventListener();
 Line line = lineNumberHandler.getDocumentLine(element, 0);
 LineResult lineResult = new LineResult(0, line.getStartOffset(), line.getEndOffset());
 List<WordResult> wordResults = Arrays.asList(new WordResult(3, 3, 4, "quick")); ;
 lineResult.addWord(wordResults.get(0));
 List<LineResult> lineResults = Arrays.asList(lineResult);
 assertEquals(1, lineResults.size());
 assertEquals(1, lineResult.getWordResults().size());
 assertEquals("quick", lineResult.getWordResults().get(0).getTerm());
}
}