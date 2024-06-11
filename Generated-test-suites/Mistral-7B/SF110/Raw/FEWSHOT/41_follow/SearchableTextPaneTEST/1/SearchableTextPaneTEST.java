package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

import org.junit.Test;

public class SearchableTextPaneTest {

    @Test
    public void testSearchableTextPaneConstruction() {
        Font font = new Font("Serif", Font.PLAIN, 12);
        int tabSize = 4;
        SearchableTextPane searchableTextPane = new SearchableTextPane(font, tabSize);
        assertEquals(font, searchableTextPane.getFont());
        assertEquals(tabSize, searchableTextPane.getTabSize());
        assertNull(searchableTextPane.getHighlighter());
        assertNull(searchableTextPane.lastSearchPos);
        assertNull(searchableTextPane.lastSearchTerm);
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 12), 4);
        JFrame frame = new JFrame();
        JScrollPane scrollPane = new JScrollPane(searchableTextPane);
        frame.add(scrollPane);
        frame.pack();
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
        frame.setSize(10, 10);
        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlight() throws BadLocationException {
        String text = "The quick brown fox jumps over the lazy dog.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 12), 4);
        Document document = new PlainDocument();
        searchableTextPane.setDocument(document);
        document.insertString(0, text, null);

        String term = "lazy";
        List<LineResult> lineResults = searchableTextPane.highlight(term, 0);
        assertEquals(1, lineResults.size());
        assertEquals(31, lineResults.get(0).getStartOffset());
        assertEquals(34, lineResults.get(0).getEndOffset());

        Highlighter highlighter = searchableTextPane.getHighlighter();
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        attributeSet.addAll(Highlighter.Property.FOREGROUND, Color.YELLOW);

        highlighter.addHighlight(31, 3, attributeSet);

        StyledDocument styledDocument = (StyledDocument) document;
        int line = searchableTextPane.getLineOfOffset(31);
        ElementElement element = (ElementElement) styledDocument.getElement(line);
        assertEquals(element.getAttributes().getBackground(), Color.WHITE);
        assertEquals(element.getAttributes().getForeground(), Color.YELLOW);
    }

    @Test
    public void testSearch() {
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 12), 4);
        searchableTextPane.search("test");
        assertNull(searchableTextPane.lastSearchTerm);
        assertFalse(searchableTextPane.lastSearchPos > -1);

        String text = "The quick brown fox jumps over the lazy dog.";
        SearchableTextPane searchableTextPaneWithText = new SearchableTextPane(new Font("Serif", Font.PLAIN, 12), 4);
        Document document = new PlainDocument();
        searchableTextPaneWithText.setDocument(document);
        document.insertString(0, text, null);

        searchableTextPaneWithText.search("lazy");
        assertEquals("lazy", searchableTextPaneWithText.lastSearchTerm);
        assertEquals(31, searchableTextPaneWithText.lastSearchPos);

        searchableTextPaneWithText.search("quick");
        assertEquals("quick", searchableTextPaneWithText.lastSearchTerm);
        assertEquals(-1, searchableTextPaneWithText.lastSearchPos);
    }

    @Test
    public void testConvertWords2Lines() throws BadLocationException {
        String text = "The quick brown fox jumps over the lazy dog.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 12), 4);
        Document document = new PlainDocument();
        searchableTextPane.setDocument(document);
        document.insertString(0, text, null);

        List<WordResult> wordResults = new ArrayList<>();
        wordResults.add(new WordResult(17, 22));
        wordResults.add(new WordResult(29, 34));

        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(wordResults);
        assertEquals(2, lineResults.size());

        LineResult line1 = lineResults.get(0);
        assertEquals(0, line1.getStartOffset());
        assertEquals(16, line1.getEndOffset());
        assertEquals(1, line1.getLineNumber());

        LineResult line2 = lineResults.get(1);
        assertEquals(17, line2.getStartOffset());
        assertEquals(35, line2.getEndOffset());
        assertEquals(2, line2.getLineNumber());

        List<WordResult> line1WordResults = line1.getWordResults();
        List<WordResult> line2WordResults = line2.getWordResults();

        WordResult word1 = line1WordResults.get(0);
        WordResult word2 = line2WordResults.get(0);

        assertEquals(17, word1.getStart());
        assertEquals(22, word1.getEnd());
        assertEquals(17, word2.getStart());
        assertEquals(22, word2.getEnd());
        assertEquals(1, word1.lineNumber);
        assertEquals(1, word2.lineNumber);
    }

    @Test
    public void testUpdateWordResult() throws BadLocationException {
        String text = "The quick brown fox jumps over the lazy dog.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.PLAIN, 12), 4);
        Document document = new PlainDocument();
        searchableTextPane.setDocument(document);
        document.insertString(0, text, null);

        List<WordResult> wordResults = new ArrayList<>();
        wordResults.add(new WordResult(17, 22));

        List<LineResult> lineResults = searchableTextPane.convertWords2Lines(wordResults);
        LineResult line1 = lineResults.get(0);

        WordResult word1 = new WordResult(17, 22);
        word1.parent = line1;

        searchableTextPane.updateWordResult(word1, line1);

        assertEquals(1, word1.lineNumber);
        assertEquals(16, line1.getStartOffset());
        assertEquals(35, line1.getEndOffset());
        assertEquals(17, word1.getStart());
        assertEquals(22, word1.getEnd());
    }
}