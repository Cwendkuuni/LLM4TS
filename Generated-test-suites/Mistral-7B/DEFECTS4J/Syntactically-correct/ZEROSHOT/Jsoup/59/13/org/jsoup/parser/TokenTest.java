package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();

        doctypeToken.asDoctype().getName("html");
        assertEquals("html", doctypeToken.asDoctype().getName());
        assertEquals("html", doctypeToken.tokenType());

        doctypeToken.asDoctype().setForceQuirks(true);
        assertTrue(doctypeToken.asDoctype().isForceQuirks());

        doctypeToken.reset();
        assertFalse(doctypeToken.asDoctype().isForceQuirks());
    }

    @org.junit.Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();

        startTagToken.name("html");
        assertEquals("html", startTagToken.name());
        assertEquals("html", startTagToken.tokenType());
        assertEquals("HTML", startTagToken.name(null));
        assertEquals("html", startTagToken.name(null).toLowerCase());

        startTagToken.selfClosing(true);
        assertTrue(startTagToken.isSelfClosing());

        startTagToken.newAttribute().name("id").value("test");
        Attributes attributes = startTagToken.getAttributes();
        assertEquals(1, attributes.size());
        assertEquals("test", attributes.get("id").getValue());

        startTagToken.finaliseTag();
        assertNull(startTagToken.pendingAttributeName);
        assertNull(startTagToken.pendingAttributeValue);
        assertNull(startTagToken.pendingAttributeValueS);
        assertFalse(startTagToken.hasPendingAttributeValue);
        assertFalse(startTagToken.hasEmptyAttributeValue);

        startTagToken.reset();
        assertNull(startTagToken.tagName);
        assertNull(startTagToken.normalName);
        assertNull(startTagToken.pendingAttributeName);
        assertNull(startTagToken.pendingAttributeValue);
        assertNull(startTagToken.pendingAttributeValueS);
        assertFalse(startTagToken.hasPendingAttributeValue);
        assertFalse(startTagToken.hasEmptyAttributeValue);
        assertFalse(startTagToken.isSelfClosing());
        assertNull(startTagToken.attributes);
    }

    @org.junit.Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();

        endTagToken.name("html");
        assertEquals("html", endTagToken.name());
        assertEquals("html", endTagToken.tokenType());
        assertEquals("HTML", endTagToken.name(null));
        assertEquals("html", endTagToken.name(null).toLowerCase());

        Document doc = new Document();
        Element element = doc.createElement("html");
        doc.appendChild(element);
        Element child = doc.createElement("body");
        element.appendChild(child);

        endTagToken.asEndTag().name("html");
        doc.remove(element);

        assertEquals(1, doc.childNodes().size());

        endTagToken.reset();
        assertNull(endTagToken.tagName);
        assertNull(endTagToken.normalName);
        assertNull(endTagToken.pendingAttributeName);
        assertNull(endTagToken.pendingAttributeValue);
        assertNull(endTagToken.pendingAttributeValueS);
        assertFalse(endTagToken.hasPendingAttributeValue);
        assertFalse(endTagToken.hasEmptyAttributeValue);
        assertFalse(endTagToken.isSelfClosing());
        assertNull(endTagToken.attributes);
    }

    @org.junit.Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();

        commentToken.getData("This is a comment");
        assertEquals("This is a comment", commentToken.getData());
        assertEquals("--This is a comment--", commentToken.toString());
        assertEquals("COMMENT", commentToken.tokenType());

        commentToken.reset();
        assertNull(commentToken.getData());
        assertNull(commentToken.toString());
    }

    @org.junit.Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();

        characterToken.data("!");
        assertEquals("!", characterToken.getData());
        assertEquals("!", characterToken.toString());
        assertEquals("CHARACTER", characterToken.tokenType());

        characterToken.reset();
        assertNull(characterToken.getData());
        assertNull(characterToken.toString());
    }

    @org.junit.Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();

        assertEquals("EOF", eofToken.tokenType());

        Document doc = new Document();
        doc.appendText("Test");

        eofToken = new Token.EOF();

        try {
            doc.appendText("Test");
            fail("Expected EOF token");
        } catch (IllegalStateException e) {
            assertEquals("org.jsoup.parser.ParseError: Unexpected end of input", e.getMessage());
            assertEquals(eofToken.tokenType(), eofToken.tokenType());
        }
    }

    @org.junit.Test
    public void testResetTokenValues() {
        Token startTagToken = new Token.StartTag();

        startTagToken.name("html");
        startTagToken.newAttribute().name("id").value("test");

        assertEquals("html", startTagToken.name());
        assertEquals(1, startTagToken.getAttributes().size());

        startTagToken.reset();

        assertNull(startTagToken.tagName);
        assertNull(startTagToken.normalName);
        assertNull(startTagToken.pendingAttributeName);
        assertNull(startTagToken.pendingAttributeValue);
        assertNull(startTagToken.pendingAttributeValueS);
        assertFalse(startTagToken.hasPendingAttributeValue);
        assertFalse(startTagToken.hasEmptyAttributeValue);
        assertFalse(startTagToken.isSelfClosing());
        assertNull(startTagToken.attributes);
    }

    @org.junit.Test
    public void testTokenTypeEnumValues() {
        TokenType[] tokenTypeValues = TokenType.values();

        Arrays.stream(tokenTypeValues).forEach(tokenType -> {
            assertEquals(tokenType.name(), tokenType.toString());
        });
    }
}