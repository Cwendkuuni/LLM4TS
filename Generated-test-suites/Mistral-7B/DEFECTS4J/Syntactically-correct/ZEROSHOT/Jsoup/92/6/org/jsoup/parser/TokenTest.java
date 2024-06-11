package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.jsoup.helper.Validate.*;

import org.jsoup.nodes.Attributes;

import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for the Token class.
 */
public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();

        // test reset method
        doctypeToken.reset();
        assertNull(doctypeToken.getName());
        assertNull(doctypeToken.getPubSysKey());
        assertNull(doctypeToken.getPublicIdentifier());
        assertNull(doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());

        // test setting values
        doctypeToken.getName("html");
        assertEquals("html", doctypeToken.getName());
        doctypeToken.setForceQuirks(true);
        assertTrue(doctypeToken.isForceQuirks());
        doctypeToken.getPublicIdentifier().append("publicID");
        assertEquals("publicID", doctypeToken.getPublicIdentifier().toString());
        doctypeToken.getSystemIdentifier().append("systemID");
        assertEquals("systemID", doctypeToken.getSystemIdentifier().toString());
        doctypeToken.getPubSysKey("//foo.com/foo");
        assertEquals("//foo.com/foo", doctypeToken.getPubSysKey());

        // test toString method
        assertEquals("<!DOCTYPE html [>\n<! [ETERNAL]>\n<!ENTITY % i \"\"><!ENTITY % j \"\">\n<!ENTITY % k \"\">\n<!ENTITY % g1 CDATA \"%\"><!ENTITY % g2 CDATA \"%\">\n<!ENTITY % g3 CDATA \"%\">\n<!ENTITY % g4 CDATA \"%\">\n<!ENTITY % g5 CDATA \"%\">\n<!ENTITY % g6 CDATA \"%\">\n<!ENTITY % g7 CDATA \"%\">\n<!ENTITY % g8 CDATA \"%\">\n<!ENTITY % g9 CDATA \"%\">\n<!-- saved from url=(0014)about:internet -->\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<!DOCTYPE html SYSTEM \"about:html\">", doctypeToken.toString());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();

        // test reset method
        startTagToken.reset();
        assertNull(startTagToken.getName());
        assertNull(startTagToken.getAttributes());
        assertFalse(startTagToken.isSelfClosing());

        // test setting values
        startTagToken.name("div");
        assertEquals("div", startTagToken.getName());
        startTagToken.attributes(new Attributes().put("class", "class1").put("id", "id1"));
        assertEquals("div class=class1 id=id1", startTagToken.toString());
        startTagToken.isSelfClosing(true);
        assertTrue(startTagToken.isSelfClosing());

        // test newAttribute method
        startTagToken.newAttribute();
        startTagToken.newAttribute().name("title").value("Test Title");
        assertEquals("div class=class1 id=id1 title=\"Test Title\"", startTagToken.toString());

        // test finaliseTag method
        startTagToken.finaliseTag();
        assertEquals("<div class=class1 id=id1 title=\"Test Title\"></div>", startTagToken.toString());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();

        // test reset method
        endTagToken.reset();
        assertNull(endTagToken.getName());

        // test setting values
        endTagToken.name("div");
        assertEquals("div", endTagToken.getName());

        // test toString method
        assertEquals("</div>", endTagToken.toString());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();

        // test reset method
        commentToken.reset();
        assertFalse(commentToken.isBogus());

        // test setting values
        commentToken.getData().append("This is a comment");
        assertEquals("This is a comment", commentToken.getData().toString());
        commentToken.isBogus(true);
        assertTrue(commentToken.isBogus());

        // test toString method
        assertEquals("<!--This is a comment-->", commentToken.toString());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();

        // test reset method
        characterToken.reset();
        assertNull(characterToken.getData());

        // test setting values
        characterToken.getData().append("A");
        assertEquals("A", characterToken.getData().toString());

        // test toString method
        assertEquals("A", characterToken.toString());
    }

    @Test
    public void testCDataToken() {
        Token cdataToken = new Token.CData("[CDATA[\nTest\n]]");

        // test reset method
        cdataToken.reset();
        assertNull(cdataToken.getData());

        // test toString method
        assertEquals("[CDATA[\nTest\n]]", cdataToken.toString());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();

        // test reset method
        eofToken.reset();

        // test toString method
        assertEquals("", eofToken.toString());
    }

    @Test
    public void testTokenTypeEnum() {
        TokenType[] tokenTypes = TokenType.values();
        assertTrue(Arrays.asList(tokenTypes).contains(TokenType.Doctype));
        assertTrue(Arrays.asList(tokenTypes).contains(TokenType.StartTag));
        assertTrue(Arrays.asList(tokenTypes).contains(TokenType.EndTag));
        assertTrue(Arrays.asList(tokenTypes).contains(TokenType.Comment));
        assertTrue(Arrays.asList(tokenTypes).contains(TokenType.Character));
        assertTrue(Arrays.asList(tokenTypes).contains(TokenType.EOF));
    }
}