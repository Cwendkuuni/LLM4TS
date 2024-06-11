package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.*;

public class SearchableTextPaneTest
{
    private SearchableTextPane searchableTextPane;
    private List<LineResult> lineResults;

    @Before
    public void setUp()
    {
        String testText = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.";
        searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 4);
        searchableTextPane.setText(testText);
    }

    @Test
    public void testSearchableTextPaneConstructor()
    {
        assertNotNull(searchableTextPane);
    }

    @Test
    public void testGetScrollableTracksViewportWidth()
    {
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlightNullTerm()
    {
        lineResults = searchableTextPane.highlight(null, 0);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testHighlightEmptyTerm()
    {
        lineResults = searchableTextPane.highlight("", 0);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testHighlightTermWithMatches()
    {
        lineResults = searchableTextPane.highlight("fox", 0);
        List<LineResult> expectedLineResults = Arrays.asList(new LineResult(0, 0, 50), new LineResult(5, 36, 41));
        assertEquals(expectedLineResults, lineResults);
    }

    @Test
    public void testHighlightTermWithNoMatches()
    {
        lineResults = searchableTextPane.highlight("zebra", 0);
        assertEquals(0, lineResults.size());
    }

    @Test(expected = BadLocationException.class)
    public void testHighlightWithException()
    {
        searchableTextPane.highlight("error", -1);
    }

    @Test
    public void testRemoveHighlights()
    {
        searchableTextPane.highlight("fox", 0);
        searchableTextPane.removeHighlights();
        lineResults = searchableTextPane.highlight("fox", 0);
        assertEquals(0, lineResults.size());
    }

    @Test
    public void testSearchSameTermTwice()
    {
        searchableTextPane.highlight("fox", 0);
        searchableTextPane.search("fox");
        searchableTextPane.search("fox");
        assertEquals(searchableTextPane.lastSearchPos, searchableTextPane.lastSearchPos + "fox".length());
    }

    @Test
    public void testSearchDifferentTerms()
    {
        searchableTextPane.highlight("fox", 0);
        searchableTextPane.search("dog");
        assertEquals(searchableTextPane.lastSearchPos, -1);
    }

    @Test
    public void testSearchNullTerm()
    {
        searchableTextPane.search(null);
        assertEquals(searchableTextPane.lastSearchTerm, null);
    }

    @Test
    public void testSearchEmptyTerm()
    {
        searchableTextPane.search("");
        assertEquals(searchableTextPane.lastSearchTerm, null);
    }

    @Test(expected = BadLocationException.class)
    public void testSearchWithException()
    {
        searchableTextPane.search("error");
    }
}

class LineResult
{
    private final int lineNumber;
    private final int startOffset;
    private final int endOffset;
    private final List<WordResult> wordResults;

    public LineResult(int line, int startOffset, int endOffset)
    {
        this.lineNumber = line;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.wordResults = new ArrayList<>();
    }

    public void addWord(WordResult wordResult)
    {
        this.wordResults.add(wordResult);
    }

    public List<WordResult> getWordResults()
    {
        return this.wordResults;
    }
}

class WordResult
{
    private final String term;
    private final int start;
    private final int end;
    private int lineNumber;

    public WordResult(String term, int start, int end)
    {
        this.term = term;
        this.start = start;
        this.end = end;
    }

    public void setLineOffset(int lineOffset)
    {
        this.lineNumber = (this.start - lineOffset) / this.getLineWidth();
    }

    public int getLineWidth()
    {
        // Calculate line width based on the given font and tab size
        // This calculation is not provided in the SearchableTextPane class
        // so it needs to be implemented here
        return 0;
    }

    public String getTerm()
    {
        return this.term;
    }

    public int getStart()
    {
        return this.start;
    }

    public int getEnd()
    {
        return this.end;
    }

    public int getLineNumber()
    {
        return this.lineNumber;
    }
}