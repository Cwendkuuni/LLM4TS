package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

import java.util.ArrayList;
import java.util.List;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctypeToken.tokenType());

        doctypeToken.reset();
        assertNull(doctypeToken.getName());
        assertNull(doctypeToken.getPubSysKey());
        assertNull(doctypeToken.getPublicIdentifier());
        assertNull(doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());

        doctypeToken.getName("html");
        assertEquals("html", doctypeToken.getName());
        assertEquals("html", doctypeToken.asDoctype().getName());

        doctypeToken.setForceQuirks(true);
        assertTrue(doctypeToken.isForceQuirks());
        assertTrue(doctypeToken.asDoctype().isForceQuirks());
    }

    @org.junit.Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        assertEquals(TokenType.StartTag, startTagToken.tokenType());

        startTagToken.reset();
        assertNull(startTagToken.getName());
        assertNull(startTagToken.getAttributes());
        assertFalse(startTagToken.isSelfClosing());

        startTagToken.name("div");
        assertEquals("div", startTagToken.name());
        assertEquals("div", startTagToken.asStartTag().name());

        startTagToken.attributes(new Attributes());
        startTagToken.attributes().put("class", "test");
        assertEquals("class", startTagToken.pendingAttributeName);
        assertEquals("test", startTagToken.attributes().get("class"));

        startTagToken.appendAttributeName("id");
        assertEquals("id", startTagToken.pendingAttributeName);
        assertNull(startTagToken.attributes().get("id"));

        startTagToken.appendAttributeValue("testId");
        assertEquals("testId", startTagToken.pendingAttributeValue);
        assertEquals("testId", startTagToken.attributes().get("id"));

        startTagToken.appendAttributeValue(" test");
        assertEquals("test", startTagToken.pendingAttributeValue);
        assertEquals("testId test", startTagToken.attributes().get("id"));

        startTagToken.newAttribute();
        assertEquals(1, startTagToken.attributes().size());

        startTagToken.name("class").attributes().put("test2", "test2Value");
        assertEquals(2, startTagToken.attributes().size());

        startTagToken.finaliseTag();
        assertTrue(startTagToken.isSelfClosing());
        assertFalse(startTagToken.asStartTag().isSelfClosing());
    }

    @org.junit.Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        assertEquals(TokenType.EndTag, endTagToken.tokenType());

        endTagToken.reset();
        assertNull(endTagToken.getName());

        endTagToken.name("div");
        assertEquals("div", endTagToken.name());
        assertEquals("div", endTagToken.asEndTag().name());

        endTagToken.finaliseTag();
        assertFalse(endTagToken.isSelfClosing());
        assertFalse(endTagToken.asEndTag().isSelfClosing());
    }

    @org.junit.Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals(TokenType.Comment, commentToken.tokenType());

        commentToken.reset();
        assertNull(commentToken.getData());

        commentToken.data("This is a comment");
        assertEquals("This is a comment", commentToken.getData());
        assertEquals("This is a comment", commentToken.asComment().getData());
    }

    @org.junit.Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals(TokenType.Character, characterToken.tokenType());

        characterToken.reset();
        assertNull(characterToken.getData());

        characterToken.data("A character");
        assertEquals("A character", characterToken.getData());
        assertEquals("A character", characterToken.asCharacter().getData());
    }

    @org.junit.Test
    public void testCDataToken() {
        Token cdataToken = new Token.CData("This is some CData");
        assertEquals(TokenType.Character, cdataToken.tokenType());
        assertTrue(cdataToken.isCData());

        assertEquals("This is some CData", cdataToken.getData());
        assertEquals("This is some CData", cdataToken.asCharacter().getData());
    }

    @org.junit.Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals(TokenType.EOF, eofToken.tokenType());

        assertTrue(eofToken.isEOF());
        assertFalse(eofToken.reset().isEOF());
    }

    @org.junit.Test
    public void testTokenTypeEnum() {
        assertTrue(TokenType.Doctype.toString().equals("Doctype"));
        assertTrue(TokenType.StartTag.toString().equals("StartTag"));
        assertTrue(TokenType.EndTag.toString().equals("EndTag"));
        assertTrue(TokenType.Comment.toString().equals("Comment"));
        assertTrue(TokenType.Character.toString().equals("Character"));
        assertTrue(TokenType.EOF.toString().equals("EOF"));
    }

    @org.junit.Test
    public void testTokenFactoryMethod() {
        Token doctypeToken = new Token.Doctype();
        Token startTagToken = new Token.StartTag();
        Token endTagToken = new Token.EndTag();
        Token commentToken = new Token.Comment();
        Token characterToken = new Token.Character();
        Token cdataToken = new Token.CData("Some data");
        Token eofToken = new Token.EOF();

        List<Token> tokens = new ArrayList<>();
        tokens.add(doctypeToken);
        tokens.add(startTagToken);
        tokens.add(endTagToken);
        tokens.add(commentToken);
        tokens.add(characterToken);
        tokens.add(cdataToken);
        tokens.add(eofToken);

        for (Token token : tokens) {
            switch (token.tokenType()) {
                case Doctype:
                    assertTrue(token instanceof Token.Doctype);
                    break;
                case StartTag:
                    assertTrue(token instanceof Token.StartTag);
                    break;
                case EndTag:
                    assertTrue(token instanceof Token.EndTag);
                    break;
                case Comment:
                    assertTrue(token instanceof Token.Comment);
                    break;
                case Character:
                    assertTrue(token instanceof Token.Character || token instanceof Token.CData);
                    break;
                case EOF:
                    assertTrue(token instanceof Token.EOF);
                    break;
            }
        }
    }
}