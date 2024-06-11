package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.parser.Token.*;
import org.jsoup.parser.TokenType;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeToken() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        doctype.pubSysKey("about:blank");
        doctype.publicIdentifier("public html");
        doctype.systemIdentifier("system.html");
        doctype.forceQuirks(true);

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertEquals("about:blank", doctype.getPubSysKey());
        assertEquals("public html", doctype.getPublicIdentifier());
        assertEquals("system.html", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());

        Doctype resetDoctype = doctype.reset();
        assertEquals("Doctype", resetDoctype.tokenType());
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.getPubSysKey());
        assertNull(resetDoctype.getPublicIdentifier());
        assertNull(resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @org.junit.Test
    public void testStartTagToken() {
        StartTag startTag = new StartTag();
        startTag.name("html");
        startTag.attributes(new Attributes().put("class", "test"));

        assertEquals("StartTag", startTag.tokenType());
        assertEquals("html", startTag.name());
        assertNotNull(startTag.getAttributes());
        assertEquals(1, startTag.getAttributes().size());
        assertTrue(startTag.getAttributes().hasKey("class"));
        assertEquals("test", startTag.getAttributes().get("class"));

        StartTag resetStartTag = startTag.reset();
        assertEquals("StartTag", resetStartTag.tokenType());
        assertNull(resetStartTag.name());
        assertNull(resetStartTag.getAttributes());
    }

    @org.junit.Test
    public void testEndTagToken() {
        EndTag endTag = new EndTag();
        endTag.name("html");

        assertEquals("EndTag", endTag.tokenType());
        assertEquals("html", endTag.name());

        EndTag resetEndTag = endTag.reset();
        assertEquals("EndTag", resetEndTag.tokenType());
        assertNull(resetEndTag.name());
    }

    @org.junit.Test
    public void testCommentToken() {
        Comment comment = new Comment();
        comment.data("This is a test comment.");

        assertEquals("Comment", comment.tokenType());
        assertEquals("This is a test comment.", comment.getData());

        Comment resetComment = comment.reset();
        assertEquals("Comment", resetComment.tokenType());
        assertNull(resetComment.getData());
    }

    @org.junit.Test
    public void testCharacterToken() {
        Character character = new Character();
        character.data("A");

        assertEquals("Character", character.tokenType());
        assertEquals("A", character.getData());

        Character resetCharacter = character.reset();
        assertEquals("Character", resetCharacter.tokenType());
        assertNull(resetCharacter.getData());
    }

    @org.junit.Test
    public void testEOFToken() {
        EOF eof = new EOF();

        assertEquals("EOF", eof.tokenType());

        EOF resetEOF = eof.reset();
        assertEquals("EOF", resetEOF.tokenType());
    }

    @org.junit.Test
    public void testTokenTypeComparison() {
        assertTrue(TokenType.Doctype.ordinal() < TokenType.StartTag.ordinal());
        assertTrue(TokenType.StartTag.ordinal() < TokenType.Comment.ordinal());
        assertTrue(TokenType.Comment.ordinal() < TokenType.Character.ordinal());
        assertTrue(TokenType.Character.ordinal() < TokenType.EndTag.ordinal());
        assertTrue(TokenType.EndTag.ordinal() < TokenType.EOF.ordinal());
    }

    @org.junit.Test
    public void testTokenTypeToString() {
        assertEquals("Doctype", TokenType.Doctype.toString());
        assertEquals("StartTag", TokenType.StartTag.toString());
        assertEquals("EndTag", TokenType.EndTag.toString());
        assertEquals("Comment", TokenType.Comment.toString());
        assertEquals("Character", TokenType.Character.toString());
        assertEquals("EOF", TokenType.EOF.toString());
    }
}