package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testTokenReset() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.getName().length(); // initialize name

        Token resetDoctypeToken = doctypeToken.reset();
        assertFalse(doctypeToken.isForceQuirks());
        assertEquals(doctypeToken.getClass().getSimpleName(), resetDoctypeToken.tokenType());
        assertNull(resetDoctypeToken.getName());
        assertNull(doctypeToken.getPubSysKey());
        assertNull(doctypeToken.getPublicIdentifier());
        assertNull(doctypeToken.getSystemIdentifier());

        // Testing reset for other tokens
        Token resetCharacterToken = new Token.Character().reset();
        assertNull(resetCharacterToken.getData());

        Token resetStartTagToken = new Token.StartTag().reset();
        assertNull(resetStartTagToken.tagName);
        assertNull(resetStartTagToken.normalName);
        assertNull(resetStartTagToken.getAttributes());

        Token resetEndTagToken = new Token.EndTag().reset();
        assertNull(resetEndTagToken.tagName);
        assertNull(resetEndTagToken.normalName);
        assertNull(resetEndTagToken.getAttributes());

        Token resetCommentToken = new Token.Comment().reset();
        assertNull(resetCommentToken.getData());
        assertFalse(resetCommentToken.isBogus());
    }

    @org.junit.Test
    public void testTokenType() {
        Token doctypeToken = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctypeToken.isDoctype());
        assertEquals(TokenType.Doctype, doctypeToken.tokenType().equals(TokenType.Doctype) ? 1 : 0);

        Token startTagToken = new Token.StartTag();
        assertEquals(TokenType.StartTag, startTagToken.isStartTag());
        assertEquals(TokenType.StartTag, startTagToken.tokenType().equals(TokenType.StartTag) ? 1 : 0);

        Token endTagToken = new Token.EndTag();
        assertEquals(TokenType.EndTag, endTagToken.isEndTag());
        assertEquals(TokenType.EndTag, endTagToken.tokenType().equals(TokenType.EndTag) ? 1 : 0);

        Token commentToken = new Token.Comment();
        assertEquals(TokenType.Comment, commentToken.isComment());
        assertEquals(TokenType.Comment, commentToken.tokenType().equals(TokenType.Comment) ? 1 : 0);

        Token characterToken = new Token.Character();
        assertEquals(TokenType.Character, characterToken.isCharacter());
        assertEquals(TokenType.Character, characterToken.tokenType().equals(TokenType.Character) ? 1 : 0);

        Token eofToken = new Token.EOF();
        assertEquals(TokenType.EOF, eofToken.isEOF());
        assertEquals(TokenType.EOF, eofToken.tokenType().equals(TokenType.EOF) ? 1 : 0);

        Character cDataToken = new Token.CData("data");
        assertTrue(cDataToken.isCData());
        assertEquals(TokenType.Character, cDataToken.tokenType().equals(TokenType.Character) ? 1 : 0);
    }

    @org.junit.Test
    public void testTokenToString() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("<doctype>", doctypeToken.toString());

        Token startTagToken = new Token.StartTag().name("html");
        assertEquals("<html>", startTagToken.toString());

        Token endTagToken = new Token.EndTag().name("html");
        assertEquals("</html>", endTagToken.toString());

        Token commentToken = new Token.Comment("This is a comment");
        assertEquals("<!--This is a comment-->", commentToken.toString());

        Token characterToken = new Token.Character('A');
        assertEquals("A", characterToken.toString());

        Token cdataToken = new Token.CData("<![CDATA[cdatadata]]>");
        assertEquals("<![CDATA[cdatadata]]>", cdataToken.toString());
    }

    @org.junit.Test
    public void testTagAttributes() {
        Token startTagToken = new Token.StartTag().name("html").attributes(new Attributes().put("id", "html"));
        assertEquals("<html id=\"html\">", startTagToken.toString());
        assertEquals("html", startTagToken.name());
        assertEquals("HTML", startTagToken.normalName());
        assertFalse(startTagToken.isSelfClosing());
        assertEquals(Arrays.asList("id", "html"), startTagToken.getAttributes().keySet());
        assertEquals("html", startTagToken.getAttributes().get("id"));
    }
}