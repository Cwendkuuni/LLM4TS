package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.TokenType;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());
        assertEquals(TokenType.Doctype, doctype.type);

        Token doctype1 = new Token.Doctype();
        doctype1.asDoctype().getName().append("html");
        assertEquals("Doctype[html]", doctype1.tokenType());
        assertEquals(TokenType.Doctype, doctype1.type);
        assertEquals("html", doctype1.asDoctype().getName());

        Token doctype2 = new Token.Doctype();
        doctype2.asDoctype().setForceQuirks(true);
        assertTrue(doctype2.asDoctype().isForceQuirks());
    }

    @org.junit.Test
    public void testStartTag() {
        Token startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());
        assertEquals(TokenType.StartTag, startTag.type);

        Token startTag1 = new Token.StartTag();
        startTag1.asStartTag().name("html");
        assertEquals("StartTag[html]", startTag1.tokenType());
        assertEquals(TokenType.StartTag, startTag1.type);
        assertEquals("html", startTag1.asStartTag().name());

        Token startTag2 = new Token.StartTag();
        startTag2.asStartTag().name("html").selfClosing(true);
        assertEquals("StartTag[html][]", startTag2.tokenType());
        assertEquals(TokenType.StartTag, startTag2.type);
        assertTrue(startTag2.asStartTag().isSelfClosing());

        Token startTag3 = new Token.StartTag();
        startTag3.asStartTag().name("html").attributes(new Attributes().put("class", "test"));
        assertEquals("StartTag[html][class=test]", startTag3.tokenType());
        assertEquals(TokenType.StartTag, startTag3.type);
        assertEquals("html", startTag3.asStartTag().name());
        assertEquals("test", startTag3.asStartTag().getAttributes().get("class"));
    }

    @org.junit.Test
    public void testEndTag() {
        Token endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType());
        assertEquals(TokenType.EndTag, endTag.type);

        Token endTag1 = new Token.EndTag();
        endTag1.asEndTag().name("html");
        assertEquals("EndTag[html]", endTag1.tokenType());
        assertEquals(TokenType.EndTag, endTag1.type);
        assertEquals("html", endTag1.asEndTag().name());
    }

    @org.junit.Test
    public void testComment() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());
        assertEquals(TokenType.Comment, comment.type);

        Token comment1 = new Token.Comment();
        comment1.asComment().getData().append("This is a comment");
        assertEquals("Comment[This is a comment]", comment1.tokenType());
        assertEquals(TokenType.Comment, comment1.type);
        assertEquals("This is a comment", comment1.asComment().getData());
    }

    @org.junit.Test
    public void testCharacter() {
        Token character = new Token.Character("A");
        assertEquals("Character[A]", character.tokenType());
        assertEquals(TokenType.Character, character.type);
        assertEquals("A", character.getData());

        Token character1 = new Token.Character('\u00A9');
        assertEquals("Character[\u00A9]", character1.tokenType());
        assertEquals(TokenType.Character, character1.type);
        assertEquals("\u00A9", character1.getData());
    }

    @org.junit.Test
    public void testEOF() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
        assertEquals(TokenType.EOF, eof.type);
    }

    @org.junit.Test
    public void testTokenType() {
        Token doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
        assertFalse(doctype.isStartTag());
        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isEOF());

        Token startTag = new Token.StartTag();
        assertFalse(startTag.isDoctype());
        assertTrue(startTag.isStartTag());
        assertFalse(startTag.isEndTag());
        assertFalse(startTag.isComment());
        assertFalse(startTag.isCharacter());
        assertFalse(startTag.isEOF());

        Token endTag = new Token.EndTag();
        assertFalse(endTag.isDoctype());
        assertFalse(endTag.isStartTag());
        assertTrue(endTag.isEndTag());
        assertFalse(endTag.isComment());
        assertFalse(endTag.isCharacter());
        assertFalse(endTag.isEOF());

        Token comment = new Token.Comment();
        assertFalse(comment.isDoctype());
        assertFalse(comment.isStartTag());
        assertFalse(comment.isEndTag());
        assertTrue(comment.isComment());
        assertFalse(comment.isCharacter());
        assertFalse(comment.isEOF());

        Token character = new Token.Character('A');
        assertFalse(character.isDoctype());
        assertFalse(character.isStartTag());
        assertFalse(character.isEndTag());
        assertFalse(character.isComment());
        assertFalse(character.isEOF());
        assertTrue(character.isCharacter());

        Token eof = new Token.EOF();
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertTrue(eof.isEOF());
    }

    @org.junit.Test
    public void testTokenTypeEnum() {
        assertEquals(TokenType.Doctype, TokenType.Doctype);
        assertEquals(TokenType.Doctype, TokenType.DOCTYPE);
        assertEquals(TokenType.StartTag, TokenType.START_TAG);
        assertEquals(TokenType.EndTag, TokenType.END_TAG);
        assertEquals(TokenType.Comment, TokenType.COMMENT);
        assertEquals(TokenType.Character, TokenType.CHARACTER);
        assertEquals(TokenType.EOF, TokenType.EOF);
    }

    @org.junit.Test
    public void testToString() {
        Token startTag = new Token.StartTag();
        startTag.asStartTag().name("html").attributes(new Attributes().put("class", "test"));
        assertEquals("<html class=test>", startTag.toString());

        Token endTag = new Token.EndTag();
        endTag.asEndTag().name("html");
        assertEquals("</html>", endTag.toString());

        Token comment = new Token.Comment();
        comment.asComment().getData().append("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());

        Token character = new Token.Character('A');
        assertEquals("A", character.toString());
    }

    @org.junit.Test
    public void testTypeAnnotations() {
        Token startTag = new Token.StartTag();
        startTag.asStartTag().name("html").selfClosing(true);
        assertFalse(startTag.isStartTag());
        assertTrue(startTag.isSelfClosingStartTag());

        Token endTag = new Token.EndTag();
        endTag.asEndTag().name("html").selfClosing(true);
        assertFalse(endTag.isEndTag());
        assertTrue(endTag.isSelfClosingEndTag());
    }

    @org.junit.Test
    public void testGetters() {
        Token startTag = new Token.StartTag();
        startTag.asStartTag().name("html").attributes(new Attributes().put("class", "test"));
        assertEquals("html", startTag.asStartTag().name());
        assertEquals(Arrays.asList("class"), startTag.asStartTag().attributes().keys());
        assertEquals("test", startTag.asStartTag().attributes().get("class").getValue());
    }

    @org.junit.Test
    public void testSetters() {
        Token startTag = new Token.StartTag();
        startTag.asStartTag().name("html").attributes(new Attributes().put("class", "test"));
        startTag.asStartTag().name("head");
        assertEquals("head", startTag.asStartTag().name());

        Token endTag = new Token.EndTag();
        endTag.asEndTag().name("html");
        endTag.asEndTag().name("body");
        assertEquals("body", endTag.asEndTag().name());

        Token comment = new Token.Comment();
        comment.asComment().getData().append("This is a comment");
        comment.asComment().getData().append(" This is another comment.");
        assertEquals("This is a comment This is another comment.", comment.asComment().getData());

        Token character = new Token.Character('A');
        character.asCharacter().getData().append("B");
        assertEquals("AB", character.asCharacter().getData());
    }
}