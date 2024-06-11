package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.jsoup.parse.ParseErrorList;
import org.jsoup.parse.TreeBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for Parser class.
 */
public class ParserTest {

    private Parser parser;
    private TreeBuilder treeBuilder;
    private List<ParseError> errors;

    @Before
    public void setUp() {
        treeBuilder = new HtmlTreeBuilder();
        parser = new Parser(treeBuilder);
        errors = new ArrayList<>();
    }

    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Test</h1></body></html>";
        Document doc = parser.parseInput(html, "");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithNullHtml() {
        parser.parseInput(null, "");
    }

    @Test
    public void testParseInputWithEmptyHtml() {
        Document doc = parser.parseInput("", "");
        assertNotNull(doc);
        assertTrue(doc.bodyElements().isEmpty());
    }

    @Test
    public void testParseInputWithErrors() {
        String html = "<html><body><script>alert('Hello')</script></body></html>";
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, "");
        errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected tag 'script'", error.getMessage());
        assertNull(doc.title());
    }

    @Test
    public void testParseInputWithoutErrors() {
        String html = "<html><body><h1>Test</h1></body></html>";
        parser.setTrackErrors(0);
        Document doc = parser.parseInput(html, "");
        errors = parser.getErrors();
        assertFalse(errors.isEmpty());
        assertNull(errors);
    }

    @Test
    public void testGetTreeBuilder() {
        TreeBuilder expectedTreeBuilder = new HtmlTreeBuilder();
        TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
        assertEquals(expectedTreeBuilder, actualTreeBuilder);
    }

    @Test
    public void testSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        parser.setTreeBuilder(newTreeBuilder);
        TreeBuilder actualTreeBuilder = parser.getTreeBuilder();
        assertEquals(newTreeBuilder, actualTreeBuilder);
    }

    @Test
    public void testIsTrackErrors() {
        assertTrue(parser.isTrackErrors());
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithZero() {
        parser.setTrackErrors(0);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithNegativeNumber() {
        parser.setTrackErrors(-1);
        assertFalse(parser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrorsWithPositiveNumber() {
        parser.setTrackErrors(5);
        assertTrue(parser.isTrackErrors());
    }

    @Test
    public void testGetErrors() {
        String html = "<html><body><script>alert('Hello')</script></body></html>";
        parser.setTrackErrors(1);
        Document doc = parser.parseInput(html, "");
        errors = parser.getErrors();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected tag 'script'", error.getMessage());
    }

    @Test
    public void testGetErrorsWithNoErrors() {
        String html = "<html><body><h1>Test</h1></body></html>";
        parser.setTrackErrors(0);
        Document doc = parser.parseInput(html, "");
        errors = parser.getErrors();
        assertFalse(errors.isEmpty());
        assertNull(errors);
    }

    @After
    public void tearDown() {
        parser = null;
        treeBuilder = null;
        errors = null;
    }
}