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
        doctype.pubSysKey("_pub_");
        doctype.publicIdentifier("ISO-8859-1");
        doctype.systemIdentifier("/some/file.html");
        doctype.forceQuirks(true);

        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertEquals("_pub_", doctype.getPubSysKey());
        assertEquals("ISO-8859-1", doctype.getPublicIdentifier());
        assertEquals("/some/file.html", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());

        Token resetDoctype = doctype.reset();
        assertSame(doctype, resetDoctype);
    }

    @org.junit.Test
    public void testStartTagToken() {
        StartTag startTag = new StartTag();
        startTag.name("div");
        startTag.nameAttr("class", "someClass");

        assertEquals("StartTag", startTag.tokenType());
        assertEquals("div", startTag.name());
        assertEquals("div", startTag.normalName());
        assertFalse(startTag.isSelfClosing());
        assertTrue(startTag.isStartTag());
        assertEquals("div someClass", startTag.toString());

        Token resetStartTag = startTag.reset();
        assertSame(startTag, resetStartTag);
    }

    @org.junit.Test
    public void testEndTagToken() {
        EndTag endTag = new EndTag();
        endTag.name("div");

        assertEquals("EndTag", endTag.tokenType());
        assertEquals("div", endTag.name());
        assertEquals("div", endTag.normalName());
        assertTrue(endTag.isEndTag());
        assertFalse(endTag.isSelfClosing());
        assertEquals("</div>", endTag.toString());

        Token resetEndTag = endTag.reset();
        assertSame(endTag, resetEndTag);
    }

    @org.junit.Test
    public void testCommentToken() {
        Comment comment = new Comment();
        comment.data("This is a comment");

        assertEquals("Comment", comment.tokenType());
        assertEquals("", comment.name());
        assertEquals("", comment.normalName());
        assertFalse(comment.isSelfClosing());
        assertTrue(comment.isComment());
        assertEquals("<!--This is a comment-->", comment.toString());

        Token resetComment = comment.reset();
        assertSame(comment, resetComment);
    }

    @org.junit.Test
    public void testCharacterToken() {
        Character character = new Character();
        character.data("A");

        assertEquals("Character", character.tokenType());
        assertEquals("A", character.getData());
        assertTrue(character.isCharacter());
        assertFalse(character.isCData());
        assertFalse(character.isSelfClosing());
        assertEquals("A", character.toString());

        Token resetCharacter = character.reset();
        assertSame(character, resetCharacter);
    }

    @org.junit.Test
    public void testCDataToken() {
        CData cData = new CData("<!--This is a CData-->");

        assertEquals("Character", cData.tokenType());
        assertEquals("", cData.name());
        assertEquals("", cData.normalName());
        assertFalse(cData.isSelfClosing());
        assertTrue(cData.isCData());
        assertEquals("<![CDATA[This is a CData]]>", cData.toString());

        Token resetCData = cData.reset();
        assertSame(cData, resetCData);
    }

    @org.junit.Test
    public void testEOFToken() {
        EOF eof = new EOF();

        assertEquals("EOF", eof.tokenType().name());
        assertTrue(eof.isEOF());
        assertFalse(eof.isSelfClosing());
        assertFalse(eof.isComment());
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isCharacter());
        assertFalse(eof.isCData());
        assertEquals("", eof.toString());

        Token resetEOF = eof.reset();
        assertSame(eof, resetEOF);
    }

    @org.junit.Test
    public void testTokenTypeEnumValues() {
        TokenType[] values = TokenType.values();
        assertArrayEquals(new TokenType[]{TokenType.Doctype, TokenType.StartTag, TokenType.EndTag, TokenType.Comment, TokenType.Character, TokenType.EOF}, values);
    }
}