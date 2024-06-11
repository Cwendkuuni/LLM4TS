package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attributes;
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
        assertEquals("html", doctype.asDoctype().getName());

        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
        assertTrue(doctype.asDoctype().isForceQuirks());

        doctype.getPublicIdentifier().append("public id");
        assertEquals("public id", doctype.getPublicIdentifier().toString());
        assertEquals("public id", doctype.asDoctype().getPublicIdentifier().toString());

        doctype.getSystemIdentifier().append("system id");
        assertEquals("system id", doctype.getSystemIdentifier().toString());
        assertEquals("system id", doctype.asDoctype().getSystemIdentifier().toString());

        doctype.reset();
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token tag = new Token.StartTag();
        assertEquals("StartTag", tag.tokenType());

        tag.reset();
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.getAttributes());
        assertFalse(tag.isSelfClosing());

        tag.name("html");
        tag.name("html").normalName("html");
        assertEquals("html", tag.tagName);
        assertEquals("html", tag.name().getName());
        assertEquals("html", tag.asStartTag().getName());
        assertEquals("html", tag.asStartTag().normalName());

        tag.newAttribute();
        tag.appendAttributeName("class").appendAttributeValue("test");
        assertEquals("class", tag.pendingAttributeName);
        assertEquals("test", tag.getAttributes().get("class"));
        assertEquals("test", tag.asStartTag().getAttributes().getValue("class"));
        assertFalse(tag.hasPendingAttributeValue());

        tag.finaliseTag();
        assertEquals(1, tag.getAttributes().size());

        tag.reset();
        assertNull(tag.getAttributes());
        assertFalse(tag.isSelfClosing());

        tag.name("img");
        tag.selfClosing(true);
        assertTrue(tag.isSelfClosing());
        assertTrue(tag.asStartTag().isSelfClosing());

        tag.reset();
        assertNull(tag.getAttributes());
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testCommentToken() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());

        comment.reset();
        assertNull(comment.getData());
        assertFalse(comment.isBogus());

        comment.getData().append("This is a comment");
        assertEquals("This is a comment", comment.getData().toString());
        assertEquals("This is a comment", comment.asComment().getData().toString());
        assertTrue(comment.isComment());
        assertTrue(comment.asComment().isComment());

        comment.reset();
        assertNull(comment.getData());
        assertFalse(comment.isBogus());
    }

    @Test
    public void testCharacterToken() {
        Token character = new Token.Character();
        assertEquals("Character", character.tokenType());

        character.reset();
        assertNull(character.getData());

        character.data("Hello World!");
        assertEquals("Hello World!", character.getData());
        assertEquals("Hello World!", character.asCharacter().getData());

        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCDataToken() {
        Token cdata = new Token.CData("<<CDATA[Hello World!]>");
        assertEquals("Character", cdata.tokenType());
        assertEquals("<<CDATA[Hello World!]", cdata.toString());

        cdata = new Token.CData("<<CDATA[Hello World!");
        assertTrue(cdata.isCData());
        assertEquals("<<CDATA[Hello World!]", cdata.toString());
    }

    @Test
    public void testEOFToken() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());

        eof.reset();
        assertTrue(eof.isEOF());
        assertTrue(eof.asEOF().isEOF());
    }
}