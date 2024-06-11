package com.werken.saxpath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class XPathLexerTest {

    @Test
    public void testNextTokenWithEmptyXPath() {
        XPathLexer lexer = new XPathLexer("");
        Token token = lexer.nextToken();
        assertEquals(new Token(-1, "", 0, 0), token);
    }
}