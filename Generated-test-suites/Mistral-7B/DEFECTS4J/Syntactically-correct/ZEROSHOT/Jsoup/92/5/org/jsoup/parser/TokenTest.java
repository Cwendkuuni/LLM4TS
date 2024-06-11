package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Token.*;
import org.jsoup.parser.TokenType;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName("html");
        doctype.getPublicIdentifier().append("public");
        doctype.getSystemIdentifier().append("system");
        doctype.setForceQuirks(true);

        Doctype resetDoctype = doctype.reset();

        assertEquals(TokenType.Doctype, resetDoctype.type);
        assertEquals(null, resetDoctype.getName());
        assertEquals(null, resetDoctype.getPublicIdentifier());
        assertEquals(null, resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @org.junit.Test
    public void testTagReset() {
        StartTag startTag = new StartTag();
        startTag.name("html");
        startTag.getAttributes().put("id", "root");

        StartTag resetStartTag = startTag.reset();

        assertEquals(TokenType.StartTag, resetStartTag.type);
        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.attributes);
    }

    @org.junit.Test
    public void testTagNewAttribute() {
        StartTag startTag = new StartTag();

        startTag.name("html");
        startTag.appendAttributeName("id");
        startTag.appendAttributeValue("root");
        startTag.newAttribute();

        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("document");
        startTag.newAttribute();

        Attributes attributes = startTag.getAttributes();

        assertEquals(2, attributes.size());
        assertEquals("root", attributes.get("id"));
        assertEquals("document", attributes.get("class"));
    }

    @org.junit.Test
    public void testTagFinaliseTag() {
        StartTag startTag = new StartTag();

        startTag.name("html");
        startTag.appendAttributeName("id");
        startTag.appendAttributeValue("root");
        startTag.newAttribute();

        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("document");
        startTag.newAttribute();

        startTag.finaliseTag();

        assertEquals("html", startTag.name());
        assertEquals(1, startTag.getAttributes().size());
        assertEquals("document", startTag.getAttributes().get("class"));
    }

    @org.junit.Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData().append("This is a comment");

        Comment resetComment = comment.reset();

        assertEquals(TokenType.Comment, resetComment.type);
        assertEquals(null, resetComment.getData());
    }

    @org.junit.Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data("A");

        Character resetCharacter = character.reset();

        assertEquals(TokenType.Character, resetCharacter.type);
        assertNull(resetCharacter.getData());
    }

    @org.junit.Test
    public void testCharacterData() {
        Character character = new Character();

        character.data("ABC");

        assertEquals("ABC", character.getData());
    }

    @org.junit.Test
    public void testCDataReset() {
        CData cData = new CData("<![CDATA[ABC]]>");

        CData resetCData = cData.reset();

        assertEquals(TokenType.Character, resetCData.type);
        assertNull(resetCData.getData());
    }

    @org.junit.Test
    public void testCDataData() {
        CData cData = new CData("<![CDATA[ABC]]>");

        assertEquals("<![CDATA[ABC]]>", cData.getData());
    }

    @org.junit.Test
    public void testEOFReset() {
        EOF eof = new EOF();

        EOF resetEOF = eof.reset();

        assertEquals(TokenType.EOF, resetEOF.type);
    }

    @org.junit.Test
    public void testTokenTypes() {
        Token token = new Doctype();

        assertTrue(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());

        token = new StartTag();

        assertFalse(token.isDoctype());
        assertTrue(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());

        token = new EndTag();

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertTrue(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());

        token = new Comment();

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertTrue(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());

        token = new Character();

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertTrue(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());

        token = new CData("");

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertTrue(token.isCData());
        assertFalse(token.isEOF());

        token = new EOF();

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertTrue(token.isEOF());
    }
}