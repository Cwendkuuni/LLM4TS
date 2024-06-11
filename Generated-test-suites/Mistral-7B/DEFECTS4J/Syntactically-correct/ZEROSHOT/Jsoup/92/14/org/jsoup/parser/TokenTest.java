package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

import java.util.Arrays;

import static org.jsoup.internal.Normalizer.lowerCase;

/**
 * Test class for the Token abstract class.
 */
public class TokenTest {

    @org.junit.Test
    public void testTokenReset() {
        Token token = new Token.Doctype();
        token.reset();
        assertNull(token.getName());
        assertNull(token.getPubSysKey());
        assertNull(token.getPublicIdentifier());
        assertNull(token.getSystemIdentifier());
        assertFalse(token.isForceQuirks());
    }

    @org.junit.Test
    public void testDoctypeGetters() {
        Token token = new Token.Doctype();
        token.getName("html");
        token.getPubSysKey("publicId");
        token.getPublicIdentifier("publicId");
        token.setPublicIdentifier("publicId");
        token.getSystemIdentifier("systemId");
        token.setSystemIdentifier("systemId");
        token.isForceQuirks(true);

        assertEquals("html", token.getName());
        assertEquals("publicId", token.getPubSysKey());
        assertEquals("publicId", token.getPublicIdentifier());
        assertEquals("systemId", token.getSystemIdentifier());
        assertTrue(token.isForceQuirks());

        token.reset();
        assertNull(token.getName());
        assertNull(token.getPubSysKey());
        assertNull(token.getPublicIdentifier());
        assertNull(token.getSystemIdentifier());
        assertFalse(token.isForceQuirks());
    }

    @org.junit.Test
    public void testTagReset() {
        Token token = new Token.StartTag();
        token.reset();
        assertNull(token.tagName);
        assertNull(token.normalName);
        assertNull(token.pendingAttributeName);
        assertNull(token.pendingAttributeValue);
        assertNull(token.pendingAttributeValueS);
        assertFalse(token.hasEmptyAttributeValue);
        assertFalse(token.hasPendingAttributeValue);
        assertNull(token.attributes);
        assertFalse(token.isSelfClosing());
    }

    @org.junit.Test
    public void testTagNewAttribute() {
        Token token = new Token.StartTag();
        token.newAttribute();
        assertNull(token.attributes);

        token.newAttribute();
        token.appendAttributeName("class");
        token.appendAttributeValue("value");
        token.attributes = new Attributes();
        token.newAttribute();
        token.appendAttributeName("id");
        token.appendAttributeValue("idValue");

        assertEquals("class", token.pendingAttributeName);
        assertEquals("value", token.pendingAttributeValue.toString());
        assertEquals("value", token.pendingAttributeValueS);
        assertEquals(2, token.attributes.size());
        assertEquals("class", token.attributes.keySet().iterator().next());
        assertEquals("value", token.attributes.get("class"));
        assertEquals("value", token.attributes.get("class").toString());
        assertEquals("id", token.attributes.keySet().iterator().next());
        assertEquals("idValue", token.attributes.get("id"));
        assertEquals("idValue", token.attributes.get("id").toString());

        token.reset();
        assertNull(token.pendingAttributeName);
        assertNull(token.pendingAttributeValue);
        assertNull(token.pendingAttributeValueS);
        assertNull(token.attributes);
    }

    @org.junit.Test
    public void testTagFinaliseTag() {
        Token token = new Token.StartTag();
        token.newAttribute();
        token.appendAttributeName("class");
        token.appendAttributeValue("value");
        token.finaliseTag();
        assertSame(token.attributes, token.asStartTag().attributes);
        assertEquals("value", token.attributes.get("class"));
        assertEquals("value", token.attributes.get("class").toString());
        assertNull(token.pendingAttributeName);
        assertNull(token.pendingAttributeValue);
        assertNull(token.pendingAttributeValueS);
        assertFalse(token.hasPendingAttributeValue);
        assertNull(token.attributes);
    }

    @org.junit.Test
    public void testTagToString() {
        Token startTag = new Token.StartTag().name("div").newAttribute().appendAttributeName("class").appendAttributeValue("foo");
        Token endTag = new Token.EndTag().name("div");

        assertEquals("<div class=\"foo\">", startTag.toString());
        assertEquals("div", startTag.normalName());
        assertEquals("div", startTag.name());
        assertEquals(false, startTag.isSelfClosing());
        assertEquals(false, startTag.isEndTag());
        assertEquals("div", endTag.name());
        assertEquals(true, endTag.isEndTag());
        assertEquals(false, endTag.isSelfClosing());
    }

    @org.junit.Test
    public void testTokenEnum() {
        assertEquals(TokenType.Doctype, TokenType.valueOf("Doctype"));
        assertEquals(TokenType.StartTag, TokenType.valueOf("StartTag"));
        assertEquals(TokenType.EndTag, TokenType.valueOf("EndTag"));
        assertEquals(TokenType.Comment, TokenType.valueOf("Comment"));
        assertEquals(TokenType.Character, TokenType.valueOf("Character"));
        assertEquals(TokenType.EOF, TokenType.valueOf("EOF"));

        try {
            TokenType.valueOf("InvalidType");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testTokenTypeIdentifiers() {
        TokenType[] types = TokenType.values();
        assertEquals(6, types.length);
        assertEquals(TokenType.Doctype, types[0]);
        assertEquals(TokenType.StartTag, types[1]);
        assertEquals(TokenType.EndTag, types[2]);
        assertEquals(TokenType.Comment, types[3]);
        assertEquals(TokenType.Character, types[4]);
        assertEquals(TokenType.EOF, types[5]);
    }

    @org.junit.Test
    public void testTokenTypeToString() {
        TokenType[] types = TokenType.values();
        for (TokenType type : types) {
            assertEquals(type.name(), type.toString());
        }
    }

    @org.junit.Test
    public void testTokenTypeIsDoctype() {
        TokenType[] types = TokenType.values();
        Token token = new Token();
        for (TokenType type : types) {
            if (type == TokenType.Doctype) {
                assertTrue(token.isDoctype());
                assertSame(TokenType.Doctype, type);
                break;
            }
            assertFalse(token.isDoctype());
        }
    }

    @org.junit.Test
    public void testTokenTypeIsStartTag() {
        TokenType[] types = TokenType.values();
        Token token = new Token.StartTag();
        for (TokenType type : types) {
            if (type == TokenType.StartTag) {
                assertTrue(token.isStartTag());
                assertSame(TokenType.StartTag, type);
                break;
            }
            assertFalse(token.isStartTag());
        }
    }

    @org.junit.Test
    public void testTokenTypeIsEndTag() {
        TokenType[] types = TokenType.values();
        Token token = new Token.EndTag();
        for (TokenType type : types) {
            if (type == TokenType.EndTag) {
                assertTrue(token.isEndTag());
                assertSame(TokenType.EndTag, type);
                break;
            }
            assertFalse(token.isEndTag());
        }
    }

    @org.junit.Test
    public void testTokenTypeIsComment() {
        TokenType[] types = TokenType.values();
        Token token = new Token.Comment();
        for (TokenType type : types) {
            if (type == TokenType.Comment) {
                assertTrue(token.isComment());
                assertSame(TokenType.Comment, type);
                break;
            }
            assertFalse(token.isComment());
        }
    }

    @org.junit.Test
    public void testTokenTypeIsCharacter() {
        TokenType[] types = TokenType.values();
        Token token = new Token.Character();
        for (TokenType type : types) {
            if (type == TokenType.Character) {
                assertTrue(token.isCharacter());
                assertSame(TokenType.Character, type);
                break;
            }
            assertFalse(token.isCharacter());
        }
    }

    @org.junit.Test
    public void testTokenTypeIsEOF() {
        TokenType[] types = TokenType.values();
        Token token = new Token.EOF();
        for (TokenType type : types) {
            if (type == TokenType.EOF) {
                assertTrue(token.isEOF());
                assertSame(TokenType.EOF, type);
                break;
            }
            assertFalse(token.isEOF());
        }
    }

    @org.junit.Test
    public void testTokenTypeFromString() {
        TokenType[] types = TokenType.values();
        for (TokenType type : types) {
            Token token = null;
            try {
                String className = type.name();
                token = Class.forName(className).newInstance();
            } catch (Exception e) {
                fail();
            }
            assertEquals(type, token.tokenType());
        }
    }

    @org.junit.Test
    public void testTokenStringRepresentation() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.toString());
        assertEquals("Doctype", token.tokenType());

        token = new Token.StartTag();
        assertEquals("StartTag", token.toString());
        assertEquals("StartTag", token.tokenType());

        token = new Token.EndTag();
        assertEquals("EndTag", token.toString());
        assertEquals("EndTag", token.tokenType());

        token = new Token.Comment();
        assertEquals("Comment", token.toString());
        assertEquals("Comment", token.tokenType());

        token = new Token.Character();
        assertEquals("Character", token.toString());
        assertEquals("Character", token.tokenType());

        token = new Token.EOF();
        assertEquals("EOF", token.toString());
        assertEquals("EOF", token.tokenType());
    }
}