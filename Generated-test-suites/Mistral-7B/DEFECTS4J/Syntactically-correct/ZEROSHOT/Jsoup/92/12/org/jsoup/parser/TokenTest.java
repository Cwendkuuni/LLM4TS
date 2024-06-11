package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;

import java.util.Arrays;

import static org.jsoup.internal.Normalizer.lowerCase;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());

        doctypeToken.getName = new StringBuilder("html");
        assertEquals("html", doctypeToken.getName());

        doctypeToken.pubSysKey = "pubSysKey";
        assertEquals("pubSysKey", doctypeToken.getPubSysKey());

        doctypeToken.publicIdentifier = new StringBuilder("publicId");
        assertEquals("publicId", doctypeToken.getPublicIdentifier());

        doctypeToken.systemIdentifier = new StringBuilder("systemId");
        assertEquals("systemId", doctypeToken.getSystemIdentifier());

        doctypeToken.forceQuirks = true;
        assertTrue(doctypeToken.isForceQuirks());

        Token resetDoctypeToken = doctypeToken.reset();
        assertNull(resetDoctypeToken.getName());
        assertNull(doctypeToken.pubSysKey);
        assertNull(doctypeToken.publicIdentifier);
        assertNull(doctypeToken.systemIdentifier);
        assertFalse(doctypeToken.isForceQuirks());
    }

    @org.junit.Test
    public void testTagToken() {
        Token tagToken = new Token.Tag();
        assertEquals("Tag", tagToken.tokenType());

        tagToken.tagName = "div";
        assertEquals("div", tagToken.name());
        tagToken.tagName = tagToken.tagName.toUpperCase();
        tagToken.normalName = lowerCase(tagToken.tagName);
        assertEquals("div", tagToken.normalName());

        tagToken.selfClosing = true;
        assertTrue(tagToken.isSelfClosing());

        tagToken.reset();
        assertNull(tagToken.tagName);
        assertNull(tagToken.normalName);
        assertFalse(tagToken.isSelfClosing());

        Token attributesTagToken = new Token.StartTag();
        assertEquals("StartTag", attributesTagToken.tokenType());
        tagToken.tagName = "input";
        tagToken.attributes = new Attributes();
        tagToken.attributes.put("type", "text");
        tagToken.attributes.put("name", "username");
        tagToken.attributes.put("value", "test");

        assertEquals("input", tagToken.name());
        assertEquals("input", tagToken.normalName());
        assertTrue(tagToken.isSelfClosing());
        assertEquals("type=\"text\" name=\"username\" value=\"test\"", tagToken.toString());

        tagToken.newAttribute();
        tagToken.appendAttributeName("id");
        tagToken.appendAttributeValue("username-input");
        tagToken.newAttribute();
        tagToken.appendAttributeName("class");
        tagToken.appendAttributeValue("form-control");

        assertEquals("type=\"text\" name=\"username\" value=\"test\" id=\"username-input\" class=\"form-control\"", tagToken.toString());

        tagToken.finaliseTag();
        assertEquals("input", tagToken.name());
        assertEquals("input", tagToken.normalName());
        assertFalse(tagToken.isSelfClosing());
    }

    @org.junit.Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());

        commentToken.data = new StringBuilder("<!-- This is a comment -->");
        assertEquals("This is a comment", commentToken.getData());
        assertEquals("<!--This is a comment-->", commentToken.toString());

        Token resetCommentToken = commentToken.reset();
        assertNull(resetCommentToken.getData());
    }

    @org.junit.Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals("Character", characterToken.tokenType());

        characterToken.data = "This is a character token.";
        assertEquals("This is a character token.", characterToken.getData());
        assertEquals("This is a character token.", characterToken.toString());

        Token resetCharacterToken = characterToken.reset();
        assertNull(resetCharacterToken.getData());
    }

    @org.junit.Test
    public void testCDataToken() {
        Token cDataToken = new Token.CData(new StringBuilder("<![CDATA[This is a CData token]]>"));
        assertEquals("CData", cDataToken.tokenType());

        assertEquals("<![CDATA[This is a CData token]]>", cDataToken.toString());
    }

    @org.junit.Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());

        Token resetEOFToken = eofToken.reset();
        assertEquals("EOF", resetEOFToken.tokenType());
    }
}