package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;

import java.util.Arrays;

/**
 * Test class for the Tokeniser class.
 */
public class TokeniserTest {

    @org.junit.Before
    public void setup() {
        // Set up any initial mocks or variables here if necessary.
    }

    @org.junit.Test
    public void testCreateInstance() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        assertEquals(TokeniserState.Data, tokeniser.getState());
        assertNull(tokeniser.emitPending);
    }

    @org.junit.Test
    public void testReadEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        assertFalse(tokeniser.isEmitPending());
        Token token = tokeniser.read();
        assertNull(token);
    }

    @org.junit.Test
    public void testReadWhitespaceAndTags() {
        CharacterReader reader = new CharacterReader(" \t<p> Hello </p>\n ");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token;

        token = tokeniser.read();
        assertEquals(Token.TokenType.Whitespace, token.type);
        assertEquals(" ", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("p", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("H", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("e", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("l", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("l", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("o", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("p", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Whitespace, token.type);
        assertEquals(" ", token.data);
    }

    @org.junit.Test
    public void testReadSelfClosingTag() {
        CharacterReader reader = new CharacterReader("<img src='image.png' />");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("img", token.tagName);
        assertTrue(token.isSelfClosing());

        token = tokeniser.read();
        assertEquals(Token.TokenType.CharacterData, token.type);
        assertNull(token.tagName);
        assertEquals("src='image.png'", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("img", token.tagName);
    }

    @org.junit.Test
    public void testReadCharacterReference() {
        CharacterReader reader = new CharacterReader("&#x1F600;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("\uD83C\uDF60", token.data);
    }

    @org.junit.Test
    public void testReadEntityReference() {
        CharacterReader reader = new CharacterReader("<![CDATA[&amp;]]>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("![CDATA[", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.CharacterData, token.type);
        assertNull(token.tagName);
        assertEquals("&amp;", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("![CDATA[", token.tagName);
    }

    @org.junit.Test
    public void testErrorHandling() {
        CharacterReader reader = new CharacterReader("&");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertNull(token);

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(ParseError.Type.Character, error.type);
        assertEquals(ParseError.Severity.ERROR, error.severity);
        assertEquals("Invalid character reference: unexpected character '&'", error.message);
    }

    @org.junit.Test
    public void testCharacterReferenceError() {
        CharacterReader reader = new CharacterReader("&#x1F60Z;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertNull(token);

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(ParseError.Type.Character, error.type);
        assertEquals(ParseError.Severity.ERROR, error.severity);
        assertEquals("Invalid character reference: unexpected character 'Z'", error.message);
    }

    @org.junit.Test
    public void testReadEndTagMismatch() {
        CharacterReader reader = new CharacterReader("<p><div><div><div><div><p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("p", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("div", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("div", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("div", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("div", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("div", token.tagName); // Should be "p" for the expected end tag

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("div", token.tagName); // Should be "p" for the expected end tag

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(ParseError.Type.Misplaced, error.type);
        assertEquals(ParseError.Severity.ERROR, error.severity);
        assertEquals("Unexpected end tag div", error.message);
    }

    @org.junit.Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("&#x1F600;");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        int[] result = tokeniser.consumeCharacterReference(null, false);

        assertEquals(1, result.length);
        assertEquals(0xD83C, result[0]);
    }

    @org.junit.Test
    public void testConsumeCharacterReferenceWithAdditionalAllowedCharacter() {
        CharacterReader reader = new CharacterReader("&#x1F600&");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        int[] result = tokeniser.consumeCharacterReference('\u0026', false);

        assertNull(result);
    }

    @org.junit.Test
    public void testConsumeCharacterReferenceWithInAttribute() {
        CharacterReader reader = new CharacterReader("class='col-md-6 &quot;'");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        int[] result = tokeniser.consumeCharacterReference(null, true);

        assertNull(result);
    }

    @org.junit.Test
    public void testConsumeNamedCharacterReference() {
        CharacterReader reader = new CharacterReader("&eacute;");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        int[] result = tokeniser.consumeCharacterReference(null, false);

        assertEquals(2, result.length);
        assertEquals(0xEA, result[0]);
        assertEquals(0xBC, result[1]);
    }

    @org.junit.Test
    public void testConsumeNamedCharacterReferenceWithInAttribute() {
        CharacterReader reader = new CharacterReader("class='col-md-6 &eacute;'");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        int[] result = tokeniser.consumeCharacterReference(null, true);

        assertNull(result);
    }

    @org.junit.Test
    public void testConsumeNamedCharacterReferenceWithEntityNameOnly() {
        CharacterReader reader = new CharacterReader("&eacute;");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        String result = tokeniser.unescapeEntities(false);

        assertEquals("é", result);
    }

    @org.junit.Test
    public void testConsumeNamedCharacterReferenceWithEntityNameOnlyInAttribute() {
        CharacterReader reader = new CharacterReader("class='col-md-6 &eacute;'");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        String result = tokeniser.unescapeEntities(true);

        assertEquals("col-md-6 é", result);
    }

    @org.junit.Test
    public void testConsumeNamedCharacterReferenceWithEntityNameAndValue() {
        CharacterReader reader = new CharacterReader("&eacute;value");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        String result = tokeniser.unescapeEntities(false);

        assertEquals("évalue", result);
    }

    @org.junit.Test
    public void testConsumeNamedCharacterReferenceWithEntityNameAndValueInAttribute() {
        CharacterReader reader = new CharacterReader("class='col-md-6 &eacute;value'");
        Tokeniser tokeniser = new Tokeniser(reader, new ParseErrorList());
        String result = tokeniser.unescapeEntities(true);

        assertEquals("col-md-6 évalue", result);
    }
}