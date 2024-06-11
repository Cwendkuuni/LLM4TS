package org.jsoup.parser;

import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeBuilderTest {
    @Test
    public void testProcessStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        Token startTag = new Token.StartTag();
        startTag.name = "html";
        boolean result = treeBuilder.processStartTag(startTag.name);
        assertTrue(result);
    }

    @Test
    public void testProcessStartTagWithNullInput() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse(null, "baseUri", ParseErrorList.noTracking());
        Token startTag = new Token.StartTag();
        startTag.name = "html";
        boolean result = treeBuilder.processStartTag(startTag.name);
        assertFalse(result);
    }

    @Test
    public void testProcessStartTagWithEmptyInput() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse("", "baseUri", ParseErrorList.noTracking());
        Token startTag = new Token.StartTag();
        startTag.name = "html";
        boolean result = treeBuilder.processStartTag(startTag.name);
        assertFalse(result);
    }

    @Test
    public void testProcessStartTagWithInvalidBaseUri() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse("input", null, ParseErrorList.noTracking());
        Token startTag = new Token.StartTag();
        startTag.name = "html";
        boolean result = treeBuilder.processStartTag(startTag.name);
        assertFalse(result);
    }

    @Test
    public void testProcessStartTagWithNullParseErrorList() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse("input", "baseUri", null);
        Token startTag = new Token.StartTag();
        startTag.name = "html";
        boolean result = treeBuilder.processStartTag(startTag.name);
        assertFalse(result);
    }

    @Test
    public void testProcessStartTagWithEOFToken() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse("input", "baseUri", ParseErrorList.noTracking());
        Token eofToken = new Token();
        eofToken.type = Token.TokenType.EOF;
        boolean result = treeBuilder.processStartTag(eofToken.name);
        assertFalse(result);
    }

    @Test
    public void testProcessStartTagWithUnknownStartTag() {
        TreeBuilder treeBuilder = new TreeBuilder();
        treeBuilder.initialiseParse("input", "baseUri", ParseErrorList.noTracking());
        Token startTag = new Token.StartTag();
        startTag.name = "unknownTag";
        boolean result = treeBuilder.processStartTag(startTag.name);
        assertFalse(result);
    }
}