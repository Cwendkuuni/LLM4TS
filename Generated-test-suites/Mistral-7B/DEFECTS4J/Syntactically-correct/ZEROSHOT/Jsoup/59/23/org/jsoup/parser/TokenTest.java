package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

/**
 * Test class for the Token class.
 */
public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().getName("html");
        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("html", doctypeToken.asDoctype().getName());

        Token resetDoctypeToken = doctypeToken.reset();
        assertNotSame(doctypeToken, resetDoctypeToken);
        assertEquals("", doctypeToken.asDoctype().getName());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        startTagToken.asStartTag().name("p");
        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("p", startTagToken.asStartTag().name());

        Token resetStartTagToken = startTagToken.reset();
        assertNotSame(startTagToken, resetStartTagToken);
        assertNull(startTagToken.asStartTag().name());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        endTagToken.asEndTag().name("p");
        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("p", endTagToken.asEndTag().name());

        Token resetEndTagToken = endTagToken.reset();
        assertNotSame(endTagToken, resetEndTagToken);
        assertNull(endTagToken.asEndTag().name());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        commentToken.asComment().getData("This is a comment.");
        assertEquals("Comment", commentToken.tokenType());
        assertEquals("This is a comment.", commentToken.asComment().getData());

        Token resetCommentToken = commentToken.reset();
        assertNotSame(commentToken, resetCommentToken);
        assertNull(commentToken.asComment().getData());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        characterToken.asCharacter().getData("A");
        assertEquals("Character", characterToken.tokenType());
        assertEquals("A", characterToken.asCharacter().getData());

        Token resetCharacterToken = characterToken.reset();
        assertNotSame(characterToken, resetCharacterToken);
        assertNull(characterToken.asCharacter().getData());
    }

    @Test(expected = IllegalStateException.class)
    public void testResetDoctypeTokenWithData() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.asDoctype().getName("html");
        doctypeToken.asDoctype().setPubSysKey("pubSysKey");
        doctypeToken.asDoctype().getPublicIdentifier().append("publicIdentifier");
        doctypeToken.asDoctype().getSystemIdentifier().append("systemIdentifier");
        doctypeToken.asDoctype().isForceQuirks(true);

        Token resetDoctypeToken = doctypeToken.reset();
        // This should throw an IllegalStateException as data has been set.
        assertEquals("", doctypeToken.asDoctype().getName());
    }

    @Test
    public void testStartTagWithAttributes() {
        Token startTagToken = new Token.StartTag();
        startTagToken.asStartTag().name("p").classAttr("class1 class2");

        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("p", startTagToken.asStartTag().name());
        assertEquals("class1 class2", startTagToken.asStartTag().getAttributes().getClassAttribute("class").getValue());

        Token resetStartTagToken = startTagToken.reset();
        assertNotSame(startTagToken, resetStartTagToken);
        assertEquals("", startTagToken.asStartTag().name());
        assertNull(startTagToken.asStartTag().getAttributes());
    }

    @Test
    public void testResetStartTagTokenWithData() {
        Token startTagToken = new Token.StartTag();
        startTagToken.asStartTag().name("p").classAttr("class1 class2");

        Token resetStartTagToken = startTagToken.reset();
        // This should not throw an exception as no data has been set.
        assertEquals("", resetStartTagToken.asStartTag().name());
        assertNull(resetStartTagToken.asStartTag().getAttributes());
    }
}