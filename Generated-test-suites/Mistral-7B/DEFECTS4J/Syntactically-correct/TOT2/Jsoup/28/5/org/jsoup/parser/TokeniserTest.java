package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Token;
import org.jsoup.parser.ParseError;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testTokeniser_Read() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("<html></html>"));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // When
        Token token = tokeniser.read();

        // Then
        Validate.notNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("html", token.tagName);

        token = tokeniser.read();
        Validate.notNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("html", token.tagName);

        token = tokeniser.read();
        Validate.notNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("head", token.tagName);

        token = tokeniser.read();
        Validate.notNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("head", token.tagName);

        token = tokeniser.read();
        Validate.notNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("body", token.tagName);

        token = tokeniser.read();
        Validate.notNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("body", token.tagName);

        token = tokeniser.read();
        assertNull(token);

        // Edge cases and exception handling
        reader = new CharacterReader(new StringReader(""));
        tokeniser = new Tokeniser(reader, errors);
        token = tokeniser.read();
        assertNull(token);
        Validate.notNull(errors.getErrorList());
        assertEquals(1, errors.getErrorList().size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.getErrorList().get(0).getMessage());

        reader = new CharacterReader(new StringReader("&"));
        tokeniser = new Tokeniser(reader, errors);
        token = tokeniser.read();
        assertNull(token);
        Validate.notNull(errors.getErrorList());
        assertEquals(1, errors.getErrorList().size());
        assertEquals("Invalid character reference: missing semicolon", errors.getErrorList().get(0).getMessage());

        reader = new CharacterReader(new StringReader("&foo;"));
        tokeniser = new Tokeniser(reader, errors);
        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("foo", token.getString());

        reader = new CharacterReader(new StringReader("<html><head><title></title>"));
        tokeniser = new Tokeniser(reader, errors);
        token = tokeniser.read();
        assertNull(token);
        Validate.notNull(errors.getErrorList());
        assertEquals(1, errors.getErrorList().size());
        assertEquals("Unexpected character '<' in input state [Data]", errors.getErrorList().get(0).getMessage());
    }

    @Test
    public void testTokeniser_Emit() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("<html></html>"));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // When & Then
        Token token = new Token.Character("h");
        tokeniser.emit(token);
        token = tokeniser.read();
        Validate.notNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("t", token.getString());

        token = new Token.Character("l");
        tokeniser.emit(token);
        token = tokeniser.read();
        Validate.notNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(" ", token.getString());

        token = new Token.Character(">");
        tokeniser.emit(token);
        token = tokeniser.read();
        Validate.notNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("html", token.tagName);

        token = new Token.Character('/');
        tokeniser.emit(token);
        token = tokeniser.read();
        Validate.notNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("html", token.tagName);

        // Edge cases and exception handling
        tokeniser = new Tokeniser(reader, errors);
        token = new Token.Character(' ');
        tokeniser.emit(token);
        token = tokeniser.read();
        assertNull(token);
        Validate.notNull(errors.getErrorList());
        assertEquals(1, errors.getErrorList().size());
        assertEquals("There is an unread token pending!", errors.getErrorList().get(0).getMessage());

        tokeniser = new Tokeniser(reader, errors);
        token = new Token.Character('>');
        tokeniser.emit(token);
        token = tokeniser.read();
        assertNull(token);
        Validate.notNull(errors.getErrorList());
        assertEquals(1, errors.getErrorList().size());
        assertEquals("Self closing flag not acknowledged", errors.getErrorList().get(0).getMessage());
    }

    @Test
    public void testTokeniser_States() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("<html></html>"));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // When & Then
        assertEquals(TokeniserState.Data, tokeniser.getState());
        tokeniser.transition(TokeniserState.AfterComment);
        assertEquals(TokeniserState.AfterComment, tokeniser.getState());
        tokeniser.transition(TokeniserState.Data);
        assertEquals(TokeniserState.Data, tokeniser.getState());

        // Edge cases and exception handling
        tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(TokeniserState.SelfClosingTag);
        assertNotNull(errors.getErrorList());
        assertEquals(1, errors.getErrorList().size());
        assertEquals("Self closing flag not acknowledged", errors.getErrorList().get(0).getMessage());
    }

    @Test
    public void testTokeniser_CharacterReference() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("<html>&#160;</html>"));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // When
        Character character = tokeniser.consumeCharacterReference(null, false);

        // Then
        Validate.notNull(character);
        assertEquals('\u00A0', character);

        // Edge cases and exception handling
        reader = new CharacterReader(new StringReader("&foo;"));
        tokeniser = new Tokeniser(reader, errors);
        character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
        Validate.notNull(errors.getErrorList());
        assertEquals(1, errors.getErrorList().size());
        assertEquals("Invalid character reference: invalid named referenece 'foo'", errors.getErrorList().get(0).getMessage());

        reader = new CharacterReader(new StringReader("&foo; "));
        tokeniser = new Tokeniser(reader, errors);
        character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
        Validate.notNull(errors.getErrorList());
        assertEquals(1, errors.getErrorList().size());
        assertEquals("Invalid character reference: missing semicolon", errors.getErrorList().get(0).getMessage());
    }

    @Test
    public void testTokeniser_Transition() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("<html><head><title></title></head>"));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // When
        tokeniser.transition(TokeniserState.InScript);
        tokeniser.advanceTransition(TokeniserState.InScript);

        // Then
        assertEquals(TokeniserState.InScript, tokeniser.getState());

        // Edge cases and exception handling
        tokeniser.transition(TokeniserState.InScript);
        tokeniser.transition(TokeniserState.InScript);
        assertNotNull(errors.getErrorList());
        assertEquals(1, errors.getErrorList().size());
        assertEquals("Self closing flag not acknowledged", errors.getErrorList().get(0).getMessage());
    }

    @Test
    public void testTokeniser_ErrorHandling() {
        // Given
        CharacterReader reader = new CharacterReader(new StringReader("&foo;"));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // When
        tokeniser.error(TokeniserState.Data);

        // Then
        Validate.notNull(errors.getErrorList());
        assertEquals(1, errors.getErrorList().size());
        assertEquals("Invalid character reference: invalid named referenece 'foo'", errors.getErrorList().get(0).getMessage());

        // Edge cases and exception handling
        reader = new CharacterReader(new StringReader(""));
        tokeniser = new Tokeniser(reader, errors);
        tokeniser.error(TokeniserState.Data);
        tokeniser.error(TokeniserState.Data);
        tokeniser.error(TokeniserState.Data);
        tokeniser.error(TokeniserState.Data);
        tokeniser.error(TokeniserState.Data);
        tokeniser.error(TokeniserState.Data);
        tokeniser.error(TokeniserState.Data);
        tokeniser.error(TokeniserState.Data);
        Validate.notNull(errors.getErrorList());
        assertEquals(9, errors.getErrorList().size());
        List<ParseError> errorList = errors.getErrorList();
        for (int i = 0; i < errorList.size(); i++) {
            ParseError error = errorList.get(i);
            assertTrue(error.getMessage().startsWith("Unexpected character '"));
            assertTrue(error.getMessage().endsWith(" in input state [Data]"));
        }
    }
}