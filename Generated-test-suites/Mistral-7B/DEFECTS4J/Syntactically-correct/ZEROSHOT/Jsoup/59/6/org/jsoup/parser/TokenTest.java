package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Token.*;
import org.junit.Test;

/**
 * Test class for Token abstract class and its subclasses.
 */
public class TokenTest {

    @Test
    public void testTokenReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("doctype");
        assertEquals(TokenType.Doctype, doctype.type);

        doctype.reset();
        assertEquals(null, doctype.getName());
        assertEquals(TokenType.Doctype, doctype.type);

        StartTag startTag = new StartTag();
        startTag.name("html").attributes(new Attributes().put("id", "root"));
        assertEquals("html", startTag.name());
        assertEquals("html", startTag.normalName());
        assertEquals(TokenType.StartTag, startTag.type);

        startTag.reset();
        assertEquals(null, startTag.name());
        assertEquals(null, startTag.normalName());
        assertEquals(TokenType.StartTag, startTag.type);

        Tag tag = new StartTag();
        tag.tagName("div").attributes(new Attributes().put("class", "box"));
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
        assertEquals(TokenType.StartTag, tag.type);

        tag.reset();
        assertEquals(null, tag.name());
        assertEquals(null, tag.normalName());
        assertEquals(TokenType.StartTag, tag.type);

        Comment comment = new Comment();
        comment.getData().equals("<!-- This is a comment -->");
        assertEquals(TokenType.Comment, comment.type);

        comment.reset();
        assertEquals(null, comment.getData());
        assertEquals(TokenType.Comment, comment.type);

        Character character = new Character().data("A");
        assertEquals("A", character.getData());
        assertEquals(TokenType.Character, character.type);

        character.reset();
        assertEquals(null, character.getData());
        assertEquals(TokenType.Character, character.type);

        EOF eof = new EOF();
        assertEquals(TokenType.EOF, eof.type);

        eof.reset();
        assertEquals(TokenType.EOF, eof.type);
    }

    @Test
    public void testTagNewAttribute() {
        Tag tag = new StartTag();
        tag.tagName("img").attributes(new Attributes().put("src", "test.jpg").put("alt", "test image"));
        tag.newAttribute();
        tag.newAttribute();
        tag.newAttribute().appendAttributeName("title");
        tag.newAttribute().appendAttributeValue("Test Title");

        assertEquals(TokenType.StartTag, tag.type);
        Attributes tagAttributes = tag.getAttributes();
        assertEquals(4, tagAttributes.size());
        Attribute titleAttribute = tagAttributes.get("title");
        assertEquals("Test Title", titleAttribute.getValue());
        assertTrue(titleAttribute.getValue().equals("Test Title") || titleAttribute.getValue().equals(""));
        BooleanAttribute srcAttribute = (BooleanAttribute) tagAttributes.get("src");
        assertTrue(srcAttribute.getValue());
        assertEquals("test.jpg", srcAttribute.getValue());
        assertTrue(srcAttribute.getValue().equals("test.jpg") || srcAttribute.getValue().equals(""));
    }
}