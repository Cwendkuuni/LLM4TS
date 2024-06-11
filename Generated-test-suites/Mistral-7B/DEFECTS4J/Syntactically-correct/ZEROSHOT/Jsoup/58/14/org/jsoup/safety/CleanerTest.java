package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Tests for the Cleaner class
 */
public class CleanerTest {

    @Test
    public void testValidDocument() {
        Whitelist whitelist = mock(Whitelist.class);
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = createDocument(" <html><body><h1 id='title'>Hello</h1><p class='content'>This is a test</p></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));

        // Verify that no nodes were discarded
        assertTrue(cleaner.copySafeNodes(dirtyDocument.body(), mock(Element.class)).equals(0));
    }

    @Test
    public void testInvalidDocument() {
        Whitelist whitelist = mock(Whitelist.class);
        when(whitelist.isSafeTag("script")).thenReturn(false);

        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = createDocument("<html><body><h1 id='title'>Hello</h1><script>alert('XSS');</script></body></html>");

        // The document should be considered invalid
        assertFalse(cleaner.isValid(dirtyDocument));

        Document cleanDocument = cleaner.clean(dirtyDocument);
        // Verify that the script tag was discarded
        assertTrue(cleanDocument.body().child(1).nodeName().equals("script"));
        assertTrue(cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body()).equals(1));
    }

    @Test
    public void testCopySafeNodesWithAttributes() {
        Whitelist whitelist = mock(Whitelist.class);
        when(whitelist.isSafeTag("div")).thenReturn(true);
        when(whitelist.isSafeAttribute("div", null, "class")).thenReturn(true);
        when(whitelist.isSafeAttribute("div", null, "id")).thenReturn(true);

        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = createDocument("<html><body><div id='test' class='test'><p>Hello</p></div></body></html>");
        Element dest = mock(Element.class);

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);
        assertEquals(0, numDiscarded);

        Element copiedDiv = dest.child(0);
        assertEquals("div", copiedDiv.nodeName());
        assertEquals("test", copiedDiv.getAttribute("id"));
        assertEquals("test", copiedDiv.getClass().getSimpleName());
    }

    @Test
    public void testCopySafeNodesWithDataNodes() {
        Whitelist whitelist = mock(Whitelist.class);
        when(whitelist.isSafeTag("script")).thenReturn(false);
        when(whitelist.isSafeTag("div")).thenReturn(true);

        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = createDocument("<html><body><div data-script='alert(\"XSS\");'><p>Hello</p></div></body></html>");
        Element dest = mock(Element.class);

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);
        assertEquals(1, numDiscarded);

        Element copiedDiv = dest.child(0);
        assertEquals("div", copiedDiv.nodeName());
        assertTrue(copiedDiv.childNodes().isEmpty());
    }

    @Test
    public void testCopySafeNodesWithComments() {
        Whitelist whitelist = mock(Whitelist.class);
        when(whitelist.isSafeTag("script")).thenReturn(false);
        when(whitelist.isSafeTag("comment")).thenReturn(true);

        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = createDocument("<html><body><script><!-- This is a comment --></script></body></html>");
        Element dest = mock(Element.class);

        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), dest);
        assertEquals(0, numDiscarded);

        assertTrue(dest.childNodes().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDocument() {
        Cleaner cleaner = new Cleaner(mock(Whitelist.class));
        cleaner.clean(null);
    }

    /**
     * Helper method to create a Document from a String
     *
     * @param html String representation of HTML document
     * @return Document instance
     */
    private Document createDocument(String html) {
        Parser parser = new Parser.MalformedHtmlParser();
        Document doc = parser.parseBodyFragment(html, new ParseErrorList());
        return doc;
    }

}

class AnswerWithNumDiscarded implements Answer<Integer> {
    private int numDiscarded;

    public AnswerWithNumDiscarded(int numDiscarded) {
        this.numDiscarded = numDiscarded;
    }

    @Override
    public Integer answer(InvocationOnMock invocation) throws Throwable {
        int num = invocation.getArgument(0);
        numDiscarded += num;
        return numDiscarded;
    }
}