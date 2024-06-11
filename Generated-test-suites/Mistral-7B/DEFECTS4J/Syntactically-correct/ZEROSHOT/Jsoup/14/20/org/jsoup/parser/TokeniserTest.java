package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the Tokeniser class.
 */
@RunWith(JUnit4.class)
public class TokeniserTest {

    @Test
    public void testTokeniser() throws IOException {
        // Test constructor
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<html><body></body></html>".getBytes()));
        Tokeniser tokeniser = new Tokeniser(reader);

        // Test read method
        Token token = tokeniser.read();
        assertEquals("Unexpected token type", Token.TokenType.StartTag, token.type);
        Token.StartTag startTag = (Token.StartTag) token;
        assertEquals("Unexpected tag name", "html", startTag.tagName);

        token = tokeniser.read();
        assertEquals("Unexpected token type", Token.TokenType.StartTag, token.type);
        Token.StartTag htmlTag = (Token.StartTag) token;
        assertEquals("Unexpected tag name", "html", htmlTag.tagName);
        assertEquals("Unexpected tag mode", TokeniserState.StartTag, tokeniser.getState().ordinal());
        assertEquals("Unexpected last start tag", null, tokeniser.getLastStartTag());

        token = tokeniser.read();
        assertEquals("Unexpected token type", Token.TokenType.Character, token.type);
        Token.Character characterToken = (Token.Character) token;
        assertEquals("Unexpected character", '<', characterToken.charAt(0));

        token = tokeniser.read();
        assertEquals("Unexpected token type", Token.TokenType.Character, token.type);
        Token.Character bodyTagOpeningBracketToken = (Token.Character) token;
        assertEquals("Unexpected character", 'b', bodyTagOpeningBracketToken.charAt(0));

        token = tokeniser.read();
        assertEquals("Unexpected token type", Token.TokenType.StartTag, token.type);
        Token.StartTag bodyTag = (Token.StartTag) token;
        assertEquals("Unexpected tag name", "body", bodyTag.tagName);
        assertEquals("Unexpected tag mode", TokeniserState.Body, tokeniser.getState().ordinal());
        assertEquals("Unexpected last start tag", bodyTag, tokeniser.getLastStartTag());

        token = tokeniser.read();
        assertEquals("Unexpected token type", Token.TokenType.EndTag, token.type);
        Token.EndTag bodyTagClosingTag = (Token.EndTag) token;
        assertEquals("Unexpected tag name", "body", bodyTagClosingTag.tagName);

        token = tokeniser.read();
        assertEquals("Unexpected token type", Token.TokenType.EndTag, token.type);
        Token.EndTag htmlTagClosingTag = (Token.EndTag) token;
        assertEquals("Unexpected tag name", "html", htmlTagClosingTag.tagName);

        tokeniser.read(); // Read the final EOF token

        // Test emit method
        tokeniser.emit(new Token.Character('A'));
        token = tokeniser.read();
        assertEquals("Unexpected token type", Token.TokenType.Character, token.type);
        Token.Character characterTokenA = (Token.Character) token;
        assertEquals("Unexpected character", 'A', characterTokenA.charAt(0));

        tokeniser.emit(new Token.StartTag("div"));
        token = tokeniser.read();
        assertEquals("Unexpected token type", Token.TokenType.StartTag, token.type);
        Token.StartTag divTag = (Token.StartTag) token;
        assertEquals("Unexpected tag name", "div", divTag.tagName);

        tokeniser.emit("Hello World!");
        token = tokeniser.read();
        assertEquals("Unexpected token type", Token.TokenType.Character, token.type);
        Token.Character characterTokenHelloWorld = (Token.Character) token;
        assertEquals("Unexpected character", 'H', characterTokenHelloWorld.charAt(0));

        tokeniser.emit(new Token.Character('!'));
        token = tokeniser.read();
        assertEquals("Unexpected token type", Token.TokenType.Character, token.type);
        Token.Character characterTokenExclamation = (Token.Character) token;
        assertEquals("Unexpected character", '!', characterTokenExclamation.charAt(0));

        // Test error methods
        tokeniser.transition(TokeniserState.Error);
        tokeniser.error("Unexpected character in input");
        List<ParseError> errors = tokeniser.getErrors();
        assertEquals("Unexpected number of errors", 1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected error message", "Unexpected character in input", error.getMessage());
        assertEquals("Unexpected line number", 1, error.getLineNumber());
        assertEquals("Unexpected column number", 1, error.getColumnNumber());
        assertEquals("Unexpected error state", TokeniserState.Error, error.getState());

        tokeniser.setTrackErrors(false);
        tokeniser.transition(TokeniserState.Error);
        tokeniser.error("Unexpected character in input");
        errors = tokeniser.getErrors();
        assertTrue("Unexpected number of errors (should be empty)", errors.isEmpty());

        // Test util methods
        tokeniser.advanceTransition(TokeniserState.Data);
        assertEquals("Unexpected state", TokeniserState.Data, tokeniser.getState());

        tokeniser.acknowledgeSelfClosingFlag();
        assertTrue("Unexpected selfClosingFlagAcknowledged value", tokeniser.isSelfClosingFlagAcknowledged());

        tokeniser.acknowledgeSelfClosingFlag();
        assertFalse("Unexpected selfClosingFlagAcknowledged value", tokeniser.isSelfClosingFlagAcknowledged());

        tokeniser.createCommentPending();
       tokeniser.emitCommentPending();
        token = tokeniser.read();
        assertEquals("Unexpected token type", Token.TokenType.Comment, token.type);
        Token.Comment comment = (Token.Comment) token;
        assertEquals("Unexpected comment text", "-- Comment --", comment.getData());

        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        token = tokeniser.read();
        assertEquals("Unexpected token type", Token.TokenType.Doctype, token.type);
        Token.Doctype doctype = (Token.Doctype) token;
        assertEquals("Unexpected public identifier", "html", doctype.getPublicId());
        assertEquals("Unexpected system identifier", "-//W3C//DTD HTML 4.01//EN", doctype.getSystemId());

        tokeniser.createTempBuffer();
        tokeniser.emit("<!-- This is a comment -->");
        tokeniser.emitDoctypePending();
        tokenizer.emit(" <html>");
        tokeniser.transition(TokeniserState.Body);
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.emit(new Token.StartTag("div"));
        tokeniser.emit(new Token.Character(' '));
        tokeniser.emit(new Token.Character('A'));
        tokeniser.emit(new Token.Character(' '));
        tokeniser.emit(new Token.Character('B'));
        tokeniser.emit(new Token.EndTag("div"));
        tokeniser.emit(new Token.Character('!'));
        tokeniser.emit(new Token.Character('>'));
        token = tokeniser.read();
        assertEquals("Unexpected token type", Token.TokenType.Character, token.type);
        Token.Character characterTokenGreaterThan = (Token.Character) token;
        assertEquals("Unexpected character", '>', characterTokenGreaterThan.charAt(0));

        tokeniser.setTrackErrors(true);
        tokeniser.transition(TokeniserState.Error);
        tokeniser.error("Unexpected character in input");
        errors = tokeniser.getErrors();
        assertEquals("Unexpected number of errors", 1, errors.size());
        error = errors.get(0);
        assertEquals("Unexpected error message", "Unexpected character in input", error.getMessage());
        assertEquals("Unexpected line number", 1, error.getLineNumber());
        assertEquals("Unexpected column number", 1, error.getColumnNumber());
        assertEquals("Unexpected error state", TokeniserState.Error, error.getState());

        tokeniser.setTrackErrors(false);
        tokeniser.transition(TokeniserState.Error);
        tokeniser.error("Unexpected character in input");
        errors = tokeniser.getErrors();
        assertTrue("Unexpected number of errors (should be empty)", errors.isEmpty());

        tokeniser.close();
    }
}