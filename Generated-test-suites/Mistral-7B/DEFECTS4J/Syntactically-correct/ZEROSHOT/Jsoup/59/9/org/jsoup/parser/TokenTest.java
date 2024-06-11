package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

import java.util.ArrayList;
import java.util.List;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();

        // Test reset method
        doctypeToken.reset();
        assertEquals(TokenType.Doctype, doctypeToken.tokenType());
        assertNull(doctypeToken.asDoctype().getName());
        assertNull(doctypeToken.asDoctype().getPubSysKey());
        assertNull(doctypeToken.asDoctype().getPublicIdentifier());
        assertNull(doctypeToken.asDoctype().getSystemIdentifier());
        assertFalse(doctypeToken.asDoctype().isForceQuirks());

        // Set values and test getters
        doctypeToken.asDoctype().setName("html");
        doctypeToken.asDoctype().setPubSysKey("[public] -//W3C//DTD XHTML 1.0 Strict//EN");
        doctypeToken.asDoctype().setPublicIdentifier("Public Identifier: XHTML 1.0 Strict DTD");
        doctypeToken.asDoctype().setSystemIdentifier("/html/xhtml1-strict.dtd");
        doctypeToken.asDoctype().setForceQuirks(true);

        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("html", doctypeToken.asDoctype().getName());
        assertEquals("[public] -//W3C//DTD XHTML 1.0 Strict//EN", doctypeToken.asDoctype().getPubSysKey());
        assertEquals("Public Identifier: XHTML 1.0 Strict DTD", doctypeToken.asDoctype().getPublicIdentifier());
        assertEquals("/html/xhtml1-strict.dtd", doctypeToken.asDoctype().getSystemIdentifier());
        assertTrue(doctypeToken.asDoctype().isForceQuirks());
    }

    @org.junit.Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();

        // Test reset method
        startTagToken.reset();
        assertEquals(TokenType.StartTag, startTagToken.tokenType());
        assertNull(startTagToken.asStartTag().getName());
        assertNull(startTagToken.asStartTag().getAttributes());
        assertFalse(startTagToken.asStartTag().isSelfClosing());

        // Set values and test getters
        startTagToken.asStartTag().name("div");
        startTagToken.asStartTag().attributes(new Attributes().put("class", "container"));
        startTagToken.asStartTag().selfClosing(true);

        assertEquals("StartTag", doctypeToken.tokenType());
        assertEquals("div", doctypeToken.asStartTag().getName());
        assertEquals(1, doctypeToken.asStartTag().getAttributes().size());
        assertTrue(doctypeToken.asStartTag().getAttributes().hasKey("class"));
        assertEquals("container", doctypeToken.asStartTag().getAttributes().get("class").getValue());
        assertTrue(doctypeToken.asStartTag().isSelfClosing());

        // Test newAttribute method
        startTagToken.asStartTag().newAttribute();
        startTagToken.asStartTag().newAttribute().name("id").value("main");
        assertEquals(2, doctypeToken.asStartTag().getAttributes().size());
        assertTrue(doctypeToken.asStartTag().getAttributes().hasKey("class"));
        assertEquals("container", doctypeToken.asStartTag().getAttributes().get("class").getValue());
        assertTrue(doctypeToken.asStartTag().getAttributes().hasKey("id"));
        assertEquals("main", doctypeToken.asStartTag().getAttributes().get("id").getValue());
    }

    @org.junit.Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();

        // Test reset method
        endTagToken.reset();
        assertEquals(TokenType.EndTag, endTagToken.tokenType());
        assertNull(endTagToken.asEndTag().getName());
        assertNull(endTagToken.asEndTag().getAttributes());

        // Set values and test getters
        endTagToken.asEndTag().name("div");
        startTagToken.asStartTag().name("div");
        startTagToken.asStartTag().getAttributes().put("id", "main");
        endTagToken.asEndTag().setParent(startTagToken);

        assertEquals("EndTag", doctypeToken.tokenType());
        assertEquals("div", doctypeToken.asEndTag().getName());
        assertEquals(1, doctypeToken.asEndTag().getAttributes().size());
        assertTrue(doctypeToken.asEndTag().getAttributes().hasKey("id"));
        assertEquals("main", doctypeToken.asEndTag().getAttributes().get("id").getValue());
        assertTrue(startTagToken.asStartTag().getAttributes().hasKey("id"));
        assertEquals("main", startTagToken.asStartTag().getAttributes().get("id").getValue());
    }

    @org.junit.Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();

        // Test reset method
        commentToken.reset();
        assertEquals(TokenType.Comment, commentToken.tokenType());
        assertNull(commentToken.asComment().getData());

        // Set values and test getters
        commentToken.asComment().data("This is a comment");

        assertEquals("Comment", doctypeToken.tokenType());
        assertEquals("This is a comment", doctypeToken.asComment().getData());
    }

    @org.junit.Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();

        // Test reset method
        characterToken.reset();
        assertEquals(TokenType.Character, characterToken.tokenType());
        assertNull(characterToken.asCharacter().getData());

        // Set values and test getters
        characterToken.asCharacter().data("&amp;");

        assertEquals("Character", doctypeToken.tokenType());
        assertEquals("&amp;", doctypeToken.asCharacter().getData());
    }

    @org.junit.Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();

        // Test reset method
        eofToken.reset();
        assertEquals(TokenType.EOF, eofToken.tokenType());

        // Test isDoctype method
        assertFalse(eofToken.isDoctype());
        // Test isStartTag method
        assertFalse(eofToken.isStartTag());
        // Test isEndTag method
        assertFalse(eofToken.isEndTag());
        // Test isComment method
        assertFalse(eofToken.isComment());
        // Test isCharacter method
        assertFalse(eofToken.isCharacter());
        // Test isEOF method
        assertTrue(eofToken.isEOF());
    }

    @org.junit.Test
    public void testParseTokens() {
        String input = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"xhtml1-strict.dtd\">" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head>" +
                "<meta charset=\"UTF-8\"/>" +
                "<title>Test Page</title>" +
                "</head>" +
                "<body>" +
                "<div id=\"main\">" +
                "<p class=\"content\">Hello World!</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        List<Token> tokens = new ArrayList<>();
        Document doc = org.jsoup.Jsoup.parse(input);
        Tokenizer tokenizer = new Tokenizer();
        while (tokenizer.nextToken(doc) != null) {
            tokens.add(tokenizer.token());
        }

        for (Token token : tokens) {
            System.out.println(token.tokenType() + ": " + token);
            if (token.isDoctype()) {
                TokenType doctypeType = ((Token.Doctype) token).getType();
                assertEquals(TokenType.Doctype, token.tokenType());
                assertEquals(doctypeType, TokenType.Doctype);
            } else if (token.isStartTag()) {
                String tagName = ((Token.StartTag) token).getName();
                assertEquals(tagName, "html");
                assertEquals(token.tokenType(), TokenType.StartTag);
            } else if (token.isEndTag()) {
                String tagName = ((Token.EndTag) token).getName();
                assertEquals(tagName, "html");
                assertEquals(token.tokenType(), TokenType.EndTag);
            } else if (token.isComment()) {
                String commentData = ((Token.Comment) token).getData();
                assertEquals(commentData, "");
                assertEquals(token.tokenType(), TokenType.Comment);
            } else if (token.isCharacter()) {
                String characterData = ((Token.Character) token).getData();
                assertEquals(characterData, "&");
                assertEquals(token.tokenType(), TokenType.Character);
            } else if (token.isEOF()) {
                assertEquals(token.tokenType(), TokenType.EOF);
            }
        }
    }
}