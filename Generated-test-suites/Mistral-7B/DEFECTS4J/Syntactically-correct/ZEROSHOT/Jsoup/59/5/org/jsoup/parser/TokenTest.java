package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());

        doctype.reset();
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.getName("html");
        assertEquals("html", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.setPubSysKey("pubsys");
        assertEquals("pubsys", doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.setPublicIdentifier("public");
        assertEquals("public", doctype.getPublicIdentifier());
        assertEquals("pubsys public", doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.setSystemIdentifier("system");
        assertEquals("system", doctype.getSystemIdentifier());
        assertEquals("pubsys public system", doctype.getPublicIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());

        doctype.reset();
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());

        startTag.reset();
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);

        startTag.tagName("html");
        assertEquals("html", startTag.tagName);
        assertEquals("html", startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);

        startTag.name("div");
        assertEquals("div", startTag.tagName);
        assertEquals("div", startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);

        startTag.newAttribute();
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertTrue(startTag.hasPendingAttributeValue);
        assertNull(startTag.attributes);

        startTag.appendAttributeName("class");
        assertEquals("class", startTag.pendingAttributeName);

        startTag.appendAttributeValue("test");
        assertEquals("test", startTag.pendingAttributeValue.toString());

        startTag.newAttribute();
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertTrue(startTag.hasPendingAttributeValue);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertNull(startTag.attributes);

        startTag.finaliseTag();
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertEquals("div class=\"test\"", startTag.toString());
        assertNotNull(startTag.getAttributes());

        startTag.reset();
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testEndTagToken() {
        Token endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType());

        endTag.reset();
        assertEquals("", endTag.tagName);
        assertEquals("", endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertNull(endTag.pendingAttributeValue);
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);

        endTag.tagName("div");
        assertEquals("div", endTag.tagName);
        assertEquals("div", endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertNull(endTag.pendingAttributeValue);
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);

        endTag.finaliseTag();
        assertEquals("div", endTag.tagName);
        assertEquals("div", endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertNull(endTag.pendingAttributeValue);
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);

        endTag.reset();
        assertEquals("", endTag.tagName);
        assertEquals("", endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertNull(endTag.pendingAttributeValue);
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
    }

    @Test
    public void testCommentToken() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());

        comment.reset();
        assertEquals("", comment.getData());
        assertFalse(comment.isBogus());

        comment.data("--comment");
        assertEquals("--comment", comment.getData());
        assertFalse(comment.isBogus());

        comment.bogus(true);
        assertTrue(comment.isBogus());

        comment.reset();
        assertEquals("", comment.getData());
        assertFalse(comment.isBogus());
    }

    @Test
    public void testCharacterToken() {
        Token character = new Token.Character();
        assertEquals("Character", character.tokenType());

        character.reset();
        assertNull(character.getData());

        character.data("a");
        assertEquals("a", character.getData());

        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testEOFToken() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());

        eof.reset();
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertTrue(eof.isEOF());

        eof.reset();
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertTrue(eof.isEOF());
    }
}