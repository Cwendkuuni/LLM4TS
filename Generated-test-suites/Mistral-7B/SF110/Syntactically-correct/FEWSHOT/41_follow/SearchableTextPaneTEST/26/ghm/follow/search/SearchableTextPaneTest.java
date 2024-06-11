package ghm.follow.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class SearchableTextPaneTest {

    @Test
    public void testSearchableTextPaneConstructor() {
        Font font = new Font("Serif", Font.BOLD, 12);
        SearchableTextPane searchableTextPane = new SearchableTextPane(font, 8);
        assertEquals(12, searchableTextPane.getFont().getSize());
        assertEquals(8, searchableTextPane.getTabSize());
    }

    @Test
    public void testGetScrollableTracksViewportWidth() {
        JTextArea textArea = new JTextArea();
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
        searchableTextPane.setParent(textArea);
        assertTrue(searchableTextPane.getScrollableTracksViewportWidth());

        JFrame frame = new JFrame();
        searchableTextPane.setParent(frame);
        assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
    }

    @Test
    public void testHighlightEmptyTerm() {
        String text = "This is a test text.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
        searchableTextPane.setText(text);
        List<LineResult> lineResults = searchableTextPane.highlight(null, 0);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertTrue(lineResult.getWordResults().isEmpty());
    }

    @Test
    public void testHighlightSingleWord() {
        String text = "The quick brown fox jumps over the lazy dog.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
        searchableTextPane.setText(text);
        List<LineResult> lineResults = searchableTextPane.highlight("fox", 0);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(2, lineResult.getWordResults().size());
        WordResult wordResult1 = lineResult.getWordResults().get(0);
        assertEquals(12, wordResult1.start);
        assertEquals(14, wordResult1.end);
        WordResult wordResult2 = lineResult.getWordResults().get(1);
        assertEquals(15, wordResult2.start);
        assertEquals(18, wordResult2.end);
    }

    @Test
    public void testHighlightMultipleWords() {
        String text = "The quick brown fox jumps over the lazy dog. The quick red fox jumps over the slow cat.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
        searchableTextPane.setText(text);
        List<LineResult> lineResults = searchableTextPane.highlight(Arrays.asList("fox", "lazy", "cat"), 0);
        assertEquals(2, lineResults.size());

        LineResult lineResult1 = lineResults.get(0);
        assertEquals(1, lineResult1.getWordResults().size());
        WordResult wordResult1 = lineResult1.getWordResults().get(0);
        assertEquals(12, wordResult1.start);
        assertEquals(14, wordResult1.end);

        LineResult lineResult2 = lineResults.get(1);
        assertEquals(2, lineResult2.getWordResults().size());
        WordResult wordResult3 = lineResult2.getWordResults().get(0);
        assertEquals(41, wordResult3.start);
        assertEquals(43, wordResult3.end);
        WordResult wordResult4 = lineResult2.getWordResults().get(1);
        assertEquals(45, wordResult4.start);
        assertEquals(48, wordResult4.end);
    }

    @Test
    public void testHighlightMultipleWordsWithFlags() {
        String text = "The quick brown fox jumps over the lazy dog. The quick red fox jumps over the slow cat.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
        searchableTextPane.setText(text);
        List<LineResult> lineResults = searchableTextPane.highlight(Arrays.asList("fox", "lazy", "cat"), 0, 2);
        assertEquals(2, lineResults.size());

        LineResult lineResult1 = lineResults.get(0);
        assertEquals(1, lineResult1.getWordResults().size());
        WordResult wordResult1 = lineResult1.getWordResults().get(0);
        assertEquals(12, wordResult1.start);
        assertEquals(14, wordResult1.end);

        LineResult lineResult2 = lineResults.get(1);
        assertEquals(2, lineResult2.getWordResults().size());
        WordResult wordResult3 = lineResult2.getWordResults().get(0);
        assertEquals(41, wordResult3.start);
        assertEquals(43, wordResult3.end);
        WordResult wordResult4 = lineResult2.getWordResults().get(1);
        assertEquals(45, wordResult4.start);
        assertEquals(48, wordResult4.end);

        for (LineResult lineResult : lineResults) {
            for (WordResult wordResult : lineResult.getWordResults()) {
                searchableTextPane.addHighlight(wordResult.start, wordResult.length());
            }
        }

        List<WordResult> wordResults = searchableTextPane.getHighlighter().getHighlights(0, searchableTextPane.getDocument().getLength());
        assertEquals(5, wordResults.size());
        WordResult expectedWordResult1 = new WordResult(12, 14, 0);
        WordResult expectedWordResult2 = new WordResult(41, 43, 2);
        WordResult expectedWordResult3 = new WordResult(45, 48, 2);
        WordResult expectedWordResult4 = new WordResult(12, 13, 0);
        WordResult expectedWordResult5 = new WordResult(45, 46, 2);

        for (int i = 0; i < wordResults.size(); i++) {
            WordResult wordResult = wordResults.get(i);
            if (i == 0) {
                assertSame(expectedWordResult1, wordResult);
            } else if (i == 1) {
                assertSame(expectedWordResult2, wordResult);
            } else if (i == 2) {
                assertSame(expectedWordResult3, wordResult);
            } else if (i == 3) {
                assertSame(expectedWordResult4, wordResult);
            } else {
                assertSame(expectedWordResult5, wordResult);
            }
        }
    }

    @Test
    public void testSearchEmptyTerm() {
        String text = "This is a test text.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
        searchableTextPane.setText(text);
        int position = searchableTextPane.search(null);
        assertEquals(-1, position);
    }

    @Test
    public void testSearchSingleWord() {
        String text = "The quick brown fox jumps over the lazy dog.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
        searchableTextPane.setText(text);
        int position = searchableTextPane.search("fox");
        assertEquals(11, position);
    }

    @Test
    public void testSearchSingleWordAtStart() {
        String text = "The quick brown fox jumps over the lazy dog.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
        searchableTextPane.setText(text);
        int position = searchableTextPane.search("The");
        assertEquals(0, position);
    }

    @Test
    public void testSearchSingleWordAtEnd() {
        String text = "The quick brown fox jumps over the lazy dog.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
        searchableTextPane.setText(text);
        int position = searchableTextPane.search("dog.");
        assertEquals(29, position);
    }

    @Test
    public void testSearchMultipleWords() {
        String text = "The quick brown fox jumps over the lazy dog. The quick red fox jumps over the slow cat.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
        searchableTextPane.setText(text);
        int position = searchableTextPane.search("quick red fox jumps over");
        assertEquals(10, position);
    }

    @Test
    public void testSearchMultipleWordsWithFlags() {
        String text = "The quick brown fox jumps over the lazy dog. The quick red fox jumps over the slow cat.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
        searchableTextPane.setText(text);
        int position = searchableTextPane.search("quick red fox jumps over", 2);
        assertEquals(10, position);
    }

    @Test
    public void testSearchMultipleWordsWithFlagsAtEnd() {
        String text = "The quick brown fox jumps over the lazy dog. The quick red fox jumps over the slow cat.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
        searchableTextPane.setText(text);
        int position = searchableTextPane.search("slow cat.", 2);
        assertEquals(45, position);
    }

    @Test
    public void testSearchMultipleWordsWithFlagsAtStart() {
        String text = "The quick brown fox jumps over the lazy dog. The quick red fox jumps over the slow cat.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
        searchableTextPane.setText(text);
        int position = searchableTextPane.search("the quick red fox jumps over slow", 2);
        assertEquals(10, position);
    }

    @Test
    public void testRemoveHighlights() {
        String text = "The quick brown fox jumps over the lazy dog.";
        SearchableTextPane searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 12), 8);
        searchableTextPane.setText(text);
        List<LineResult> lineResults = searchableTextPane.highlight("fox", 0);
        assertEquals(1, lineResults.size());
        LineResult lineResult = lineResults.get(0);
        assertEquals(1, lineResult.getWordResults().size());
        WordResult wordResult = lineResult.getWordResults().get(0);
        searchableTextPane.addHighlight(wordResult.start, wordResult.length());

        List<WordResult> wordResults = searchableTextPane.getHighlighter().getHighlights(0, searchableTextPane.getDocument().getLength());
        assertEquals(1, wordResults.size());

        searchableTextPane.removeHighlights();

        wordResults = searchableTextPane.getHighlighter().getHighlights(0, searchableTextPane.getDocument().getLength());
        assertEquals(0, wordResults.size());
    }
}