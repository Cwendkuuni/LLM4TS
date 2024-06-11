package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for {@link Token} class.
 */
public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
        
        token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());
        
        token = new Token.EndTag();
        assertEquals("EndTag", token.tokenType());
        
        token = new Token.Comment();
        assertEquals("Comment", token.tokenType());
        
        token = new Token.Character();
        assertEquals("Character", token.tokenType());
        
        token = new Token.CData();
        assertEquals("Character", token.tokenType()); // CData is an extension of Character
        
        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
    }

    @Test
    public void testDoctypeToken() {
        TokenType tokenType = TokenType.Doctype;
        Token token = new Token.Doctype();

        assertEquals(tokenType, token.type);

        String name = "html";
        token = new Token.Doctype().name(name);
        assertEquals(name, token.asDoctype().getName());
        
        String pubSysKey = "public";
        token = new Token.Doctype().pubSysKey(pubSysKey);
        assertEquals(pubSysKey, token.asDoctype().getPubSysKey());
        
        String publicIdentifier = "//W3C//DTD HTML 4.01 Transitional//EN";
        token = new Token.Doctype().publicIdentifier(publicIdentifier);
        assertEquals(publicIdentifier, token.asDoctype().getPublicIdentifier());
        
        String systemIdentifier = "/usr/local/share/js/html4_transitional.dtd";
        token = new Token.Doctype().systemIdentifier(systemIdentifier);
        assertEquals(systemIdentifier, token.asDoctype().getSystemIdentifier());
        
        boolean forceQuirks = true;
        token = new Token.Doctype().forceQuirks(forceQuirks);
        assertEquals(forceQuirks, token.asDoctype().isForceQuirks());
        
        token.reset();
        assertNull(token.asDoctype().getName());
        assertNull(token.asDoctype().getPubSysKey());
        assertNull(token.asDoctype().getPublicIdentifier());
        assertNull(token.asDoctype().getSystemIdentifier());
        assertFalse(token.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        TokenType tokenType = TokenType.StartTag;
        Token token = new Token.StartTag();

        assertEquals(tokenType, token.type);

        String tagName = "html";
        token = new Token.StartTag().name(tagName);
        assertEquals(tagName, token.asStartTag().name());
        
        tagName = "body";
        token = new Token.StartTag().name(tagName).attributes(new Attributes().put("class", "content"));
        assertEquals(tagName, token.asStartTag().name());
        assertEquals("class", token.asStartTag().getAttributes().getClass());
        assertEquals("content", token.asStartTag().getAttributes().get("class"));
        
        token.reset();
        assertNull(token.asStartTag().name());
        assertNull(token.asStartTag().getAttributes());
    }

    @Test
    public void testEndTagToken() {
        TokenType tokenType = TokenType.EndTag;
        Token token = new Token.EndTag();

        assertEquals(tokenType, token.type);

        String tagName = "html";
        token = new Token.EndTag().name(tagName);
        assertEquals(tagName, token.asEndTag().name());
        
        token.reset();
        assertNull(token.asEndTag().name());
    }

    @Test
    public void testCommentToken() {
        TokenType tokenType = TokenType.Comment;
        Token token = new Token.Comment();

        assertEquals(tokenType, token.type);

        String data = "This is a comment.";
        token = new Token.Comment().data(data);
        assertEquals(data, token.asComment().getData());
        
        token.reset();
        assertNull(token.asComment().getData());
    }

    @Test
    public void testCharacterToken() {
        TokenType tokenType = TokenType.Character;
        Token token = new Token.Character();

        assertEquals(tokenType, token.type);

        String data = "!";
        token = new Token.Character().data(data);
        assertEquals(data, token.asCharacter().getData());
        
        token.reset();
        assertNull(token.asCharacter().getData());
    }

    @Test
    public void testCDataToken() {
        TokenType tokenType = TokenType.Character; // CData is an extension of Character
        Token token = new Token.CData("[CDATA[ This is CData. ]]>");

        assertEquals(tokenType, token.type);

        String data = "This is CData.";
        token = new Token.CData().data(data);
        assertEquals(data, token.asCharacter().getData());
        
        token.reset();
        assertNull(token.asCharacter().getData());
    }

    @Test
    public void testEOFToken() {
        TokenType tokenType = Token.TokenType.EOF;
        Token token = new Token.EOF();

        assertEquals(tokenType, token.type);

        token.reset();
        assertSame(token, token.reset());
    }
}