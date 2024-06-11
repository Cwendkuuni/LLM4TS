package com.werken.saxpath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class XPathLexerTest {

    @Test
    public void testNextToken() {
        XPathLexer lexer = new XPathLexer("testXPath");
        Token token = lexer.nextToken();
        assertNotNull(token);
        // Add more assertions based on the expected token type and value
    }
}