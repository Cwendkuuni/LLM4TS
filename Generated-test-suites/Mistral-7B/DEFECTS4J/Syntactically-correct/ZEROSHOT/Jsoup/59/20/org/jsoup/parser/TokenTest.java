package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();

        // Test reset method
        doctypeToken.reset();
        assertEquals("Doctype", doctypeToken.tokenType());
        assertNull(doctypeToken.asDoctype().getName());
        assertNull(doctypeToken.asDoctype().getPubSysKey());
        assertNull(doctypeToken.asDoctype().getPublicIdentifier());
        assertNull(doctypeToken.asDoctype().getSystemIdentifier());
        assertFalse(doctypeToken.asDoctype().isForceQuirks());

        // Set values and test getters
        doctypeToken.asDoctype()
                .getName("html")
                .setPubSysKey("pubsyskey")
                .setPublicIdentifier("publicid")
                .setSystemIdentifier("systemid")
                .setForceQuirks(true);
        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("html", doctypeToken.asDoctype().getName());
        assertEquals("pubsyskey", doctypeToken.asDoctype().getPubSysKey());
        assertEquals("publicid", doctypeToken.asDoctype().getPublicIdentifier());
        assertEquals("systemid", doctypeToken.asDoctype().getSystemIdentifier());
        assertTrue(doctypeToken.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();

        // Test reset method
        startTagToken.reset();
        assertEquals("StartTag", startTagToken.tokenType());
        assertNull(startTagToken.asStartTag().getName());
        assertNull(startTagToken.asStartTag().getAttributes());
        assertFalse(startTagToken.asStartTag().isSelfClosing());

        // Set values and test getters
        startTagToken.asStartTag()
                .name("div")
                .setAttribute("class", "class1 class2")
                .setAttribute("id", "id1")
                .setAttribute("title", "title")
                .setAttribute("style", "color:red;background-color:blue;")
                .setAttribute("tabindex", "1")
                .setAttribute("disabled", "") // empty string as value
                .setAttribute("checked", true) // boolean attribute
                .setAttribute("src", "src.jpg")
                .setAttribute("href", "href.html")
                .setAttribute("for", "id2") // boolean attribute, but treated as normal attribute for parser convenience
                .setSelfClosing(true);
        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("div", startTagToken.asStartTag().getName());
        assertEquals(Arrays.asList("class1", "class2"), Arrays.asList(startTagToken.asStartTag().getAttributes().values()));
        assertNull(startTagToken.asStartTag().getAttributes().get("id")); // getAttribute returns Attribute object, not String
        assertEquals("title", startTagToken.asStartTag().getAttribute("title")); // getAttributeValue returns String
        assertEquals("color:red;background-color:blue;", startTagToken.asStartTag().getAttribute("style"));
        assertTrue(startTagToken.asStartTag().isSelfClosing());

        // Test finaliseTag method
        startTagToken.asStartTag().finaliseTag();
        assertNull(startTagToken.asStartTag().getAttributes());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();

        // Test reset method
        endTagToken.reset();
        assertEquals("EndTag", endTagToken.tokenType());
        assertNull(endTagToken.asEndTag().getName());
        assertNull(endTagToken.asEndTag().getAttributes());

        // Set values and test getters
        endTagToken.asEndTag().name("div");
        assertEquals("div", endTagToken.asEndTag().getName());
        assertEquals("div", endTagToken.asEndTag().getName()); // normalName is lowercase version of name
        assertNull(endTagToken.asEndTag().getAttributes());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();

        // Test reset method
        commentToken.reset();
        assertEquals("Comment", commentToken.tokenType());
        assertNull(commentToken.asComment().getData());

        // Set values and test getters
        commentToken.asComment()
                .getData("This is a comment.");
        assertEquals("Comment", commentToken.tokenType());
        assertEquals("This is a comment.", commentToken.asComment().getData());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();

        // Test reset method
        characterToken.reset();
        assertEquals("Character", characterToken.tokenType());
        assertNull(characterToken.asCharacter().getData());

        // Set values and test getters
        characterToken.asCharacter()
                .data("A");
        assertEquals("Character", characterToken.tokenType());
        assertEquals("A", characterToken.asCharacter().getData());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();

        // Test reset method
        eofToken.reset();
        assertEquals("EOF", eofToken.tokenType());

        // EOF token does not have any methods to test
    }
}