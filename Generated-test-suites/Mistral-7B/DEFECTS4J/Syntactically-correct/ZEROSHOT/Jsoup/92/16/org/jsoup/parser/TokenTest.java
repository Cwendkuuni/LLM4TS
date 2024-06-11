package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.Doctype.SystemIdentifier;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

import org.junit.Test;

/**
 * Test the Token class.
 */
public class TokenTest {

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        assertEquals(TokenType.Doctype, doctype.type);

        doctype.name("html");
        assertEquals("html", doctype.getName());
        assertEquals("HTML", doctype.normalName());

        doctype.pubSysKey("DTD HTML 4.01 Transitional");
        assertEquals("DTD HTML 4.01 Transitional", doctype.getPubSysKey());

        doctype.publicIdentifier("public \"-//W3C//DTD HTML 4.01//EN\"");
        assertEquals("public \"-//W3C//DTD HTML 4.01//EN\"", doctype.getPublicIdentifier());

        doctype.systemIdentifier("system \"http://www.w3.org/TR/html4/strict.dtd\"");
        assertEquals("system \"http://www.w3.org/TR/html4/strict.dtd\"", doctype.getSystemIdentifier());

        doctype.forceQuirks(true);
        assertTrue(doctype.isForceQuirks());

        Doctype resetDoctype = doctype.reset();
        assertSame(doctype, resetDoctype);
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        assertEquals(TokenType.StartTag, startTag.type);

        startTag.name("div");
        assertEquals("div", startTag.name());
        assertEquals("div", startTag.normalName());

        startTag.attributes(new Attribute("class", "test"));
        assertEquals("class=\"test\"", startTag.toString());

        StartTag resetStartTag = startTag.reset();
        assertSame(startTag, resetStartTag);
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.getAttributes());
    }

    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag();
        assertEquals(TokenType.EndTag, endTag.type);

        endTag.name("p");
        assertEquals("p", endTag.name());
        assertEquals("p", endTag.normalName());

        EndTag resetEndTag = endTag.reset();
        assertSame(endTag, resetEndTag);
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        assertEquals(TokenType.Comment, comment.type);

        comment.data("-- This is a comment");
        assertEquals("-- This is a comment", comment.getData());
        assertEquals("<!--This is a comment-->", comment.toString());

        Comment resetComment = comment.reset();
        assertSame(comment, resetComment);
        assertNull(comment.getData());
    }

    @Test
    public void testCharacter() {
        Character character = new Character();
        assertEquals(TokenType.Character, character.type);

        character.data("A");
        assertEquals('A', character.getData().charAt(0));
        assertEquals("A", character.toString());

        Character resetCharacter = character.reset();
        assertSame(character, resetCharacter);
        assertNull(character.getData());
    }

    @Test
    public void testCData() {
        CData cData = new CData("CDATA[This is CData]");
        assertEquals(TokenType.Character, cData.type);

        assertEquals("CDATA[This is CData]", cData.toString());
    }

    @Test
    public void testReset() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        doctype.pubSysKey("DTD HTML 4.01 Transitional");
        doctype.publicIdentifier("public \"-//W3C//DTD HTML 4.01//EN\"");
        doctype.systemIdentifier("system \"http://www.w3.org/TR/html4/strict.dtd\"");
        doctype.forceQuirks(true);

        StartTag startTag = new StartTag();
        startTag.name("div");
        startTag.attributes(new Attribute("class", "test"));

        Comment comment = new Comment();
        comment.data("-- This is a comment");

        Character character = new Character();
        character.data("A");

        CData cData = new CData("CDATA[This is CData]");

        EndTag endTag = new EndTag();
        endTag.name("p");

        EOF eof = new EOF();

        Token[] tokens = new Token[]{ doctype, startTag, comment, character, cData, endTag, eof };

        for (Token token : tokens) {
            Token resetToken = token.reset();
            assertSame(token, resetToken);
        }
    }
}