package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

import java.util.Arrays;

/**
 * Test class for Token abstract class.
 */
public class TokenTest {

    @org.junit.Test
    public void testTokenType() {
        Token token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());

        token = new Token.EndTag();
        assertEquals("EndTag", token.tokenType());

        token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());

        token = new Token.Comment();
        assertEquals("Comment", token.tokenType());

        token = new Token.Character();
        assertEquals("Character", token.tokenType());

        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
    }

    @org.junit.Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        Token resetDoctypeToken = doctypeToken.reset();

        assertEquals(TokenType.Doctype, resetDoctypeToken.tokenType());

        doctypeToken.asDoctype().getName = "html";
        assertEquals("html", doctypeToken.asDoctype().getName());

        doctypeToken.asDoctype().pubSysKey = "public";
        assertEquals("public", doctypeToken.asDoctype().getPubSysKey());

        doctypeToken.asDoctype().publicIdentifier = "DTD HTML 4.01 Transitional";
        assertEquals("DTD HTML 4.01 Transitional", doctypeToken.asDoctype().getPublicIdentifier());

        doctypeToken.asDoctype().systemIdentifier = "/path/to/myhtml.dtd";
        assertEquals("/path/to/myhtml.dtd", doctypeToken.asDoctype().getSystemIdentifier());

        doctypeToken.asDoctype().forceQuirks = true;
        assertTrue(doctypeToken.asDoctype().isForceQuirks());

        resetDoctypeToken = doctypeToken.reset();
        assertFalse(doctypeToken.asDoctype().isForceQuirks());
    }

    @org.junit.Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag().name("html").selfClosing(false);
        Token resetStartTagToken = startTagToken.reset();

        assertEquals(TokenType.StartTag, resetStartTagToken.tokenType());

        assertEquals("html", startTagToken.name());
        assertFalse(startTagToken.isSelfClosing());

        Attributes attributes = new Attributes();
        attributes.put("class", "myclass");
        startTagToken.asStartTag().nameAttr("html", attributes);
        assertEquals("html", startTagToken.name());
        assertTrue(startTagToken.getAttributes().containsKey("class"));
        assertEquals("myclass", startTagToken.getAttributes().get("class"));

        resetStartTagToken = startTagToken.reset();
        assertEquals(TokenType.StartTag, resetStartTagToken.tokenType());
        assertEquals(null, resetStartTagToken.getAttributes());
    }

    @org.junit.Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag().name("html");
        Token resetEndTagToken = endTagToken.reset();

        assertEquals(TokenType.EndTag, resetEndTagToken.tokenType());

        assertEquals("html", endTagToken.name());

        resetEndTagToken = endTagToken.reset();
        assertEquals(TokenType.EndTag, resetEndTagToken.tokenType());
        assertEquals(null, resetEndTagToken.getName());
    }

    @org.junit.Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        commentToken.asComment().data("This is a comment.");

        assertEquals(TokenType.Comment, commentToken.tokenType());
        assertEquals("This is a comment.", commentToken.getData());

        resetCommentToken = commentToken.reset();
        assertEquals(TokenType.Comment, resetCommentToken.tokenType());
        assertEquals(null, resetCommentToken.getData());
    }

    @org.junit.Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        characterToken.asCharacter().data("A");

        assertEquals(TokenType.Character, characterToken.tokenType());
        assertEquals("A", characterToken.getData());

        resetCharacterToken = characterToken.reset();
        assertEquals(TokenType.Character, resetCharacterToken.tokenType());
        assertEquals(null, resetCharacterToken.getData());
    }

    @org.junit.Test
    public void testCDataToken() {
        Token cDataToken = new Token.CData("['<![CDATA[This is CData]]>']");

        assertEquals(TokenType.Character, cDataToken.tokenType());
        assertEquals("<![CDATA[This is CData]]>", cDataToken.toString());

        resetCDataToken = cDataToken.reset();
        assertEquals(TokenType.Character, resetCDataToken.tokenType());
        assertEquals(null, resetCDataToken.getData());
    }

    @org.junit.Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();

        assertEquals(TokenType.EOF, eofToken.tokenType());

        resetEofToken = eofToken.reset();
        assertEquals(TokenType.EOF, resetEofToken.tokenType());
    }

    @org.junit.Test
    public void testTokenReset() {
        Token doctypeToken = new Token.Doctype();
        Token startTagToken = new Token.StartTag().name("html");
        Token commentToken = new Token.Comment();
        Token characterToken = new Token.Character();
        Token eofToken = new Token.EOF();

        doctypeToken.reset();
        startTagToken.reset();
        commentToken.reset();
        characterToken.reset();
        eofToken.reset();

        assertNull(doctypeToken.asDoctype().getName());
        assertNull(doctypeToken.asDoctype().pubSysKey);
        assertNull(doctypeToken.asDoctype().publicIdentifier);
        assertNull(doctypeToken.asDoctype().systemIdentifier);
        assertFalse(doctypeToken.asDoctype().isForceQuirks());

        assertNull(startTagToken.name());
        assertNull(startTagToken.getAttributes());
        assertFalse(startTagToken.isSelfClosing());

        assertNull(commentToken.getData());
        assertFalse(commentToken.isForceQuirks());

        assertNull(characterToken.getData());
        assertNull(characterToken.getAttributes());
        assertFalse(characterToken.isSelfClosing());

        assertFalse(eofToken.isDoctype());
        assertFalse(eofToken.isStartTag());
        assertFalse(eofToken.isEndTag());
        assertFalse(eofToken.isComment());
        assertFalse(eofToken.isCharacter());
        assertTrue(eofToken.isEOF());
    }
}