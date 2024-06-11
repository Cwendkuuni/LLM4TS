package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Token.*;
import org.jsoup.parser.TokenType;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeToken() {
        Doctype doctype = new Doctype();
        assertEquals("Doctype", doctype.tokenType());

        doctype.name("html");
        assertEquals("html", doctype.getName());

        doctype.pubSysKey("public -1");
        assertEquals("public -1", doctype.getPubSysKey());

        doctype.publicIdentifier("ISOLAT1 1.0");
        assertEquals("ISOLAT1 1.0", doctype.getPublicIdentifier());

        doctype.systemIdentifier("/foo/bar.html");
        assertEquals("/foo/bar.html", doctype.getSystemIdentifier());

        doctype.forceQuirks(true);
        assertTrue(doctype.isForceQuirks());

        Doctype resetDoctype = doctype.reset();
        assertNotSame(doctype, resetDoctype);
        assertEquals("Doctype", resetDoctype.tokenType());
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.getPubSysKey());
        assertNull(resetDoctype.getPublicIdentifier());
        assertNull(resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @org.junit.Test
    public void testTagToken() {
        Tag tag = new StartTag();
        assertEquals("StartTag", tag.tokenType().name());

        tag.name("div");
        tag.name("div").normalName();
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());

        tag.appendTagName("class");
        tag.appendTagName("class").normalName();
        assertEquals("class", tag.name());
        assertEquals("class", tag.normalName());

        tag.appendAttributeName("id");
        tag.appendAttributeName("id").normalName();
        tag.appendAttributeName("ID");
        tag.appendAttributeName("ID").normalName();
        assertEquals("id", tag.name("id"));
        assertEquals("id", tag.name("ID"));
        assertEquals("id", tag.normalName("id"));
        assertEquals("id", tag.normalName("ID"));

        tag.appendAttributeValue("test");
        tag.appendAttributeValue("test");
        tag.appendAttributeValue("Test");
        tag.appendAttributeValue("Test");
        tag.appendAttributeValue("test");
        tag.appendAttributeValue("Test");
        tag.setEmptyAttributeValue();
        tag.hasEmptyAttributeValue();
        assertEquals("test", tag.pendingAttributeValue().toString());
        assertEquals("Test", tag.pendingAttributeValue().toString().toUpperCase());
        assertEquals("", tag.pendingAttributeValue().toString());

        tag.newAttribute();
        tag.newAttribute();
        tag.finaliseTag();
        tag.finaliseTag();
        assertEquals(2, tag.attributes().size());

        tag = new EndTag();
        assertEquals("EndTag", tag.tokenType().name());
        tag.name("div");
        tag.name("div");
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
    }

    @org.junit.Test
    public void testCommentToken() {
        Comment comment = new Comment();
        assertEquals("Comment", comment.tokenType().name());

        comment.data("-- comment --");
        assertEquals("-- comment --", comment.getData());
        assertEquals("-- comment --", comment.toString());

        Comment resetComment = comment.reset();
        assertNotSame(comment, resetComment);
        assertEquals("Comment", resetComment.tokenType().name());
        assertNull(resetComment.getData());
    }

    @org.junit.Test
    public void testCharacterToken() {
        Character character = new Character();
        assertEquals("Character", character.tokenType().name());

        character.data("Hello World!");
        assertEquals("Hello World!", character.getData());
        assertEquals("Hello World!", character.toString());

        Character resetCharacter = character.reset();
        assertNotSame(character, resetCharacter);
        assertEquals("Character", resetCharacter.tokenType().name());
        assertNull(resetCharacter.getData());

        CData cdata = new CData("Hello World!");
        assertEquals("Character", cdata.tokenType().name());
        assertEquals("<![CDATA[Hello World!]]>", cdata.toString());

        EOF eof = new EOF();
        assertEquals("EOF", eof.tokenType().name());
    }

    @org.junit.Test
    public void testTokenType() {
        assertTrue(Arrays.stream(TokenType.values()).allMatch(TokenType::isInstance));
    }
}