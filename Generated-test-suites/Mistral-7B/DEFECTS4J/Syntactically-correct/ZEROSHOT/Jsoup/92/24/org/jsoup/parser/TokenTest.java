package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Token.*;
import org.jsoup.parser.TokenType;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeToken() {
        Doctype doctype = new Doctype();
        doctype.getName("html");
        doctype.setForceQuirks(true);

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertTrue(doctype.isForceQuirks());

        Doctype resetDoctype = doctype.reset();
        assertNotSame(doctype, resetDoctype);
        assertFalse(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testStartTagToken() {
        StartTag startTag = new StartTag();
        startTag.name("html");

        assertEquals("StartTag", startTag.tokenType().name());
        assertEquals("html", startTag.name());
        assertEquals("<html>", startTag.toString());

        StartTag resetStartTag = startTag.reset();
        assertNotSame(startTag, resetStartTag);
    }

    @org.junit.Test
    public void testEndTagToken() {
        EndTag endTag = new EndTag();
        endTag.name("html");

        assertEquals("EndTag", endTag.tokenType().name());
        assertEquals("html", endTag.name());
        assertEquals("</html>", endTag.toString());

        EndTag resetEndTag = endTag.reset();
        assertNotSame(endTag, resetEndTag);
    }

    @org.junit.Test
    public void testCommentToken() {
        Comment comment = new Comment();
        comment.getData("This is a comment");

        assertEquals("Comment", comment.tokenType().name());
        assertEquals("This is a comment", comment.getData());
        assertEquals("<!--This is a comment-->", comment.toString());

        Comment resetComment = comment.reset();
        assertNotSame(comment, resetComment);
    }

    @org.junit.Test
    public void testCharacterToken() {
        Character character = new Character();
        character.data("A");

        assertEquals("Character", character.tokenType().name());
        assertEquals("A", character.getData());
        assertEquals("A", character.toString());

        Character resetCharacter = character.reset();
        assertNotSame(character, resetCharacter);
    }

    @org.junit.Test
    public void testCDATAToken() {
        CData cdata = new CData("This is CDATA");

        assertEquals("Character", cdata.tokenType().name()); // note: CDATA is treated as an extension of Character in the code
        assertEquals("<![CDATA[This is CDATA]]>", cdata.toString());

        CData resetCData = cdata.reset();
        assertNotSame(cdata, resetCData);
    }

    @org.junit.Test
    public void testEOToken() {
        EOF eof = new EOF();

        assertEquals("EOF", eof.tokenType().name());
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertFalse(eof.isCData());
        assertEquals("", eof.toString());

        EOF resetEof = eof.reset();
        assertSame(eof, resetEof);
    }

    @org.junit.Test
    public void testTokenTypeEnum() {
        TokenType[] tokenTypes = TokenType.values();
        Arrays.stream(tokenTypes).forEach(tokenType -> {
            assertEquals(tokenType.name(), tokenType.toString());
        });
    }

    @org.junit.Test
    public void testTokenReset() {
        Doctype doctype = new Doctype();
        doctype.getName("html");
        doctype.setForceQuirks(true);

        StartTag startTag = new StartTag();
        startTag.name("html");

        EndTag endTag = new EndTag();
        endTag.name("html");

        Comment comment = new Comment();
        comment.getData("This is a comment");

        Character character = new Character();
        character.data("A");

        CData cdata = new CData("This is CDATA");

        EOF eof = new EOF();

        // test resetting each token type
        Doctype resetDoctype = doctype.reset();
        assertEquals(Doctype.class, resetDoctype.getClass());
        assertFalse(doctype.isForceQuirks());

        StartTag resetStartTag = startTag.reset();
        assertEquals(StartTag.class, resetStartTag.getClass());

        EndTag resetEndTag = endTag.reset();
        assertEquals(EndTag.class, resetEndTag.getClass());

        Comment resetComment = comment.reset();
        assertEquals(Comment.class, resetComment.getClass());

        Character resetCharacter = character.reset();
        assertEquals(Character.class, resetCharacter.getClass());

        CData resetCData = cdata.reset();
        assertEquals(CData.class, resetCData.getClass());

        EOF resetEof = eof.reset();
        assertEquals(EOF.class, resetEof.getClass());
    }
}