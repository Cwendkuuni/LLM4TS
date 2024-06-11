package org.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.CharacterInput;

public class TokeniserTest {

    @Test
    public void testRead() {
        // Test read method with valid input
        CharacterInput reader = new CharacterInput("Hello World");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());
        Token token = tokeniser.read();

        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("html", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("H", token.content.charAt(0));

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("e", token.content.charAt(0));

        // Test empty token
        token = tokeniser.read();
        assertNull(token);

        tokeniser.acknowledgeSelfClosingFlag();
        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("html", token.tagName);
    }

    @Test
    public void testEmptyInputStream() {
        // Test read method with empty input stream
        CharacterInput reader = new CharacterInput("");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());
        Token token = tokeniser.read();

        assertNull(token);
    }

    @Test
    public void testInvalidCharacter() {
        // Test read method with invalid character
        CharacterInput reader = new CharacterInput("Hello World<");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());

        try {
            tokeniser.read();
            fail("Expected an error");
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testSelfClosingFlagNotAcknowledged() {
        // Test read method with self-closing flag not acknowledged
        CharacterInput reader = new CharacterInput("<!-- Self closing -->");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());

        try {
            tokeniser.read();
            tokeniser.read();
            tokeniser.read();
            tokeniser.acknowledgeSelfClosingFlag();
            tokeniser.read(); // Should be an end tag, but it's not acknowledged yet
            fail("Expected an error");
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testInvalidCharacterReference() {
        // Test consumeCharacterReference method with invalid character reference
        CharacterInput reader = new CharacterInput("Hello World&#x12345;");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());

        try {
            tokeniser.consumeCharacterReference(null, false);
            fail("Expected an error");
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testEndTagWithoutCorrespondingStartTag() {
        // Test read method with end tag without corresponding start tag
        CharacterInput reader = new CharacterInput("<body></html>");
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());

        Token startTagToken = tokeniser.read(); // html
        Token endTagToken = tokeniser.read(); // html

        try {
            tokeniser.read();
            fail("Expected an error");
        } catch (AssertionError e) {
            // Expected
        }
    }
}