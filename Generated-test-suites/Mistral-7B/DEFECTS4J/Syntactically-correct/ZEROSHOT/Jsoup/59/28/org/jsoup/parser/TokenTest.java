package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeToken() {
        Doctype doctype = new Doctype();

        // test reset
        doctype.reset();
        assertEquals(TokenType.Doctype, doctype.type);
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        // test set values and reset
        doctype.getName().append("html");
        assertTrue(doctype.getName().toString().equals("html"));
        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
        doctype.getSystemIdentifier().append("about:blank");
        assertTrue(doctype.getSystemIdentifier().toString().equals("about:blank"));
        doctype.reset();
        assertEquals(TokenType.Doctype, doctype.type);
        assertNull(doctype.getName());
        assertNull(doctyp.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        // test getters
        doctype.getName().append("html");
        doctype.setForceQuirks(true);
        doctype.getSystemIdentifier().append("about:blank");
        assertEquals("html", doctype.getName().toString());
        assertTrue(doctype.isForceQuirks());
        assertEquals("about:blank", doctype.getSystemIdentifier().toString());
    }

    @org.junit.Test
    public void testStartTagToken() {
        StartTag startTag = new StartTag();

        // test reset
        startTag.reset();
        assertEquals(TokenType.StartTag, startTag.type);
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);

        // test set values and reset
        startTag.name("html").tagName("HTML").normalName("html").selfClosing(true);
        assertEquals("HTML", startTag.tagName);
        assertEquals("html", startTag.normalName);
        assertTrue(startTag.selfClosing);
        assertNull(startTag.attributes);
        startTag.newAttribute().name("id").value("1").name("class").value("class1 class2");
        Attributes attributes = startTag.getAttributes();
        assertEquals(2, attributes.size());
        Attribute idAttribute = attributes.get("id");
        assertEquals("1", idAttribute.getValue());
        Attribute classNameAttribute = attributes.get("class");
        assertEquals("class1 class2", classNameAttribute.getValue());
        startTag.reset();
        assertEquals(TokenType.StartTag, startTag.type);
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);

        // test toString
        startTag.tagName("html").normalName("html").selfClosing(true);
        assertEquals("<html/>", startTag.toString());
        startTag.newAttribute().name("id").value("1").name("class").value("class1 class2");
        assertEquals("<html id=\"1\" class=\"class1 class2\"/>", startTag.toString());
    }

    @org.junit.Test
    public void testEndTagToken() {
        EndTag endTag = new EndTag();

        // test reset
        endTag.reset();
        assertEquals(TokenType.EndTag, endTag.type);
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.attributes);

        // test set values and reset
        endTag.name("html");
        assertEquals("html", endTag.tagName);
        assertEquals("html", endTag.normalName);
        assertNull(endTag.attributes);
        endTag.reset();
        assertEquals(TokenType.EndTag, endTag.type);
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.attributes);

        // test toString
        endTag.name("html");
        assertEquals("</html>", endTag.toString());
    }

    @org.junit.Test
    public void testCommentToken() {
        Comment comment = new Comment();

        // test reset
        comment.reset();
        assertEquals(TokenType.Comment, comment.type);
        assertNull(comment.data);
        assertFalse(comment.bogus);

        // test set values and reset
        comment.data("-- This is a comment");
        assertEquals("-- This is a comment", comment.getData());
        comment.bogus = true;
        assertTrue(comment.bogus);
        comment.reset();
        assertEquals(TokenType.Comment, comment.type);
        assertNull(comment.data);
        assertFalse(comment.bogus);

        // test toString
        comment.data("-- This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @org.junit.Test
    public void testCharacterToken() {
        Character character = new Character();

        // test reset
        character.reset();
        assertEquals(TokenType.Character, character.type);
        assertNull(character.data);

        // test set values and reset
        character.data("abc");
        assertEquals("abc", character.getData());
        character.reset();
        assertEquals(TokenType.Character, character.type);
        assertNull(character.data);

        // test toString
        character.data("abc");
        assertEquals("abc", character.getData());
        assertEquals("abc", character.toString());

        // test isEmptyAttributeValue
        character.setEmptyAttributeValue();
        assertTrue(character.hasEmptyAttributeValue);
        assertFalse(character.hasPendingAttributeValue);
        character.reset();
        assertFalse(character.hasEmptyAttributeValue);
        assertFalse(character.hasPendingAttributeValue);

        // test appendAttributeValue
        character.ensureAttributeValue();
        character.appendAttributeValue("ab");
        character.appendAttributeValue("cd");
        assertEquals("abcd", character.pendingAttributeValue.toString());
        character.reset();
        assertFalse(character.hasEmptyAttributeValue);
        assertFalse(character.hasPendingAttributeValue);
        assertNull(character.pendingAttributeValue);
        assertNull(character.pendingAttributeValueS);

        // test appendAttributeValueS
        character.ensureAttributeValue();
        character.appendAttributeValueS("abc");
        assertEquals("abc", character.pendingAttributeValueS);
        character.appendAttributeValueS("def");
        assertEquals("abcdef", character.pendingAttributeValueS);
        character.reset();
        assertFalse(character.hasEmptyAttributeValue);
        assertFalse(character.hasPendingAttributeValue);
        assertNull(character.pendingAttributeValue);
        assertNull(character.pendingAttributeValueS);
    }

    @org.junit.Test
    public void testEOFToken() {
        EOF eof = new EOF();

        // test reset
        eof.reset();
        assertEquals(TokenType.EOF, eof.type);
        assertEquals(Token.EOF.class, eof.getClass());

        // test toString
        assertEquals("", eof.toString());
    }

    @org.junit.Test
    public void testTokenTypeEnum() {
        TokenType[] tokenTypes = TokenType.values();
        assertEquals(6, tokenTypes.length);
        assertTrue(Arrays.asList(tokenTypes).contains(TokenType.Doctype));
        assertTrue(Arrays.asList(tokenTypes).contains(TokenType.StartTag));
        assertTrue(Arrays.asList(tokenTypes).contains(TokenType.EndTag));
        assertTrue(Arrays.asList(tokenTypes).contains(TokenType.Comment));
        assertTrue(Arrays.asList(tokenTypes).contains(TokenType.Character));
        assertTrue(Arrays.asList(tokenTypes).contains(TokenType.EOF));
    }
}