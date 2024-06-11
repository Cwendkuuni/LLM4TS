package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

/**
 * Test for the Token class.
 */
public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.type);
        assertTrue(doctype.reset() instanceof Token.Doctype);
        assertNull(doctype.asDoctype().getName());
        doctype.asDoctype().getName("html");
        assertEquals("html", doctype.asDoctype().getName());
        assertNull(doctype.asDoctype().getPubSysKey());
        assertNull(doctype.asDoctype().getPublicIdentifier());
        assertNull(doctype.asDoctype().getSystemIdentifier());
        assertFalse(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token tag = new Token.StartTag();
        assertEquals(TokenType.StartTag, tag.type);
        assertTrue(tag.reset() instanceof Token.StartTag);
        assertNull(tag.asStartTag().getName());
        tag.asStartTag().name("html");
        assertEquals("html", tag.asStartTag().getName());
        assertNull(tag.asStartTag().getAttributes());
        tag.asStartTag().name("html").attributes(new Attributes());
        Attributes attributes = tag.asStartTag().getAttributes();
        assertEquals(attributes.size(), 0);
        tag.asStartTag().name("html").attributes(new Attributes().put("id", "test"));
        Attribute idAttribute = attributes.get("id");
        assertEquals(idAttribute.getValue(), "test");
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testEndTagToken() {
        Token endTag = new Token.EndTag();
        assertEquals(TokenType.EndTag, endTag.type);
        assertTrue(endTag.reset() instanceof Token.EndTag);
        assertEquals("", endTag.asEndTag().name());
    }

    @Test
    public void testCommentToken() {
        Token comment = new Token.Comment();
        assertEquals(TokenType.Comment, comment.type);
        assertTrue(comment.reset() instanceof Token.Comment);
        assertNull(comment.asComment().getData());
        comment.asComment().data("This is a comment.");
        assertEquals("This is a comment.", comment.asComment().getData());
        assertFalse(comment.isDoctype());
    }

    @Test
    public void testCharacterToken() {
        Token character = new Token.Character();
        assertEquals(TokenType.Character, character.type);
        assertTrue(character.reset() instanceof Token.Character);
        assertNull(character.asCharacter().getData());
        character.asCharacter().data("A");
        assertEquals("A", character.asCharacter().getData());
        assertFalse(character.isDoctype());
    }

    @Test
    public void testEOFToken() {
        Token eof = new Token.EOF();
        assertEquals(TokenType.EOF, eof.type);
        assertFalse(eof.reset() instanceof Token);
        assertTrue(eof.reset() instanceof Token.EOF);
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
    }

    @Test
    public void testTokenTypeEnum() {
        TokenType[] tokenTypes = TokenType.values();
        for (TokenType tokenType : tokenTypes) {
            assertEquals(tokenType.name(), tokenType.toString());
        }
    }
}