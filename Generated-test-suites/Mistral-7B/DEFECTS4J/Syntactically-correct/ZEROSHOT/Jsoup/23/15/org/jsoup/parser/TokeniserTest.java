package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.junit.Test;
import org.junit.Before;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the Tokeniser class.
 */
public class TokeniserTest {

    private static final String TEST_HTML = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
    private Tokeniser tokeniser;
    private ParseErrorList errors;
    private InputStream inputStream;

    @Before
    public void init() {
        errors = new ParseErrorList();
        inputStream = new ByteArrayInputStream(TEST_HTML.getBytes());
        tokeniser = new Tokeniser(new CharacterReader(inputStream), errors);
    }

    @Test
    public void testDefaultConstructor() {
        assertNull(tokeniser.getState());
        assertNull(tokeniser.emitPending);
        assertNull(tokeniser.charBuffer);
        assertNull(tokeniser.dataBuffer);
        assertNull(tokeniser.tagPending);
        assertNull(tokeniser.doctypePending);
        assertNull(tokeniser.commentPending);
        assertNull(tokeniser.lastStartTag);
        assertFalse(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConstructorWithArguments() {
        InputStream input = new ByteArrayInputStream(TEST_HTML.getBytes());
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(input), errors);
        assertEquals(input, tokeniser.reader);
        assertEquals(errors, tokeniser.errors);
        assertNull(tokeniser.getState());
        assertNull(tokeniser.emitPending);
        assertNull(tokeniser.charBuffer);
        assertNull(tokeniser.dataBuffer);
        assertNull(tokeniser.tagPending);
        assertNull(tokeniser.doctypePending);
        assertNull(tokeniser.commentPending);
        assertNull(tokeniser.lastStartTag);
        assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testReadReturnsFirstToken() {
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("html", token.tagName);
    }

    @Test
    public void testReadReturnsSubsequentTokens() {
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("head", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("html", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("title", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("Test", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("title", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("body", token.tagName);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("Hello", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(" World!", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("body", token.tagName);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmitReturnsToken() {
        Token startTag = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, emittedToken.type);
        assertEquals("html", emittedToken.tagName);
        assertEquals(startTag, tokeniser.lastStartTag);

        tokeniser.emit(startTag);

        Token endTag = tokeniser.createTagPending(false);
        tokeniser.emitCommentPending();
        tokeniser.emitDoctypePending();
        tokeniser.emitTagPending();
        tokeniser.emitEndTag(endTag);
        tokeniser.emitCommentPending();
        tokeniser.emitTagPending();
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.emitTagPending();
        tokeniser.emitEndTag(endTag);
        tokeniser.emitTagPending();

        List<Token> tokens = new ArrayList<>();
        while (tokeniser.read() != null) {
            tokens.add(tokeniser.emitPending);
            tokeniser.emitPending = null;
        }

        assertEquals(15, tokens.size());
        assertEquals(Token.TokenType.EndTag, tokens.get(0).type);
        assertEquals("html", tokens.get(0).tagName);
        assertEquals(Token.TokenType.EndTag, tokens.get(1).type);
        assertEquals("body", tokens.get(1).tagName);
        assertEquals(Token.TokenType.EndTag, tokens.get(2).type);
        assertEquals("title", tokens.get(2).tagName);
        assertEquals(Token.TokenType.Character, tokens.get(3).type);
        assertEquals("Test", tokens.get(3).data);
        assertEquals(Token.TokenType.Comment, tokens.get(4).type);
        assertEquals("", tokens.get(4).data);
        assertEquals(Token.TokenType.StartTag, tokens.get(5).type);
        assertEquals("script", tokens.get(5).tagName);
        assertEquals(Token.TokenType.Character, tokens.get(6).type);
        assertEquals("type=\"text/javascript\"", tokens.get(6).data);
        assertEquals(Token.TokenType.EndTag, tokens.get(7).type);
        assertEquals("script", tokens.get(7).tagName);
        assertEquals(Token.TokenType.Character, tokens.get(8).type);
        assertEquals(">", tokens.get(8).data);
        assertEquals(Token.TokenType.Character, tokens.get(9).type);
        assertEquals(" ", tokens.get(9).data);
        assertEquals(Token.TokenType.Character, tokens.get(10).type);
        assertEquals("p", tokens.get(10).data);
        assertEquals(Token.TokenType.Character, tokens.get(11).type);
        assertEquals(">", tokens.get(11).data);
        assertEquals(Token.TokenType.Character, tokens.get(12).type);
        assertEquals(" ", tokens.get(12).data);
        assertEquals(Token.TokenType.Character, tokens.get(13).type);
        assertEquals("H", tokens.get(13).data);
        assertEquals(Token.TokenType.Character, tokens.get(14).type);
        assertEquals("e", tokens.get(14).data);
        assertEquals(Token.TokenType.Character, tokens.get(14).type);
        assertEquals("l", tokens.get(14).data);
        assertEquals(Token.TokenType.Character, tokens.get(15).type);
        assertEquals("lo", tokens.get(15).data);
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.Script);
        assertNotNull(tokeniser.getState());
        assertEquals(TokeniserState.Script, tokeniser.getState());
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.transition(TokeniserState.Script);
        tokeniser.advanceTransition(TokeniserState.Data);
        assertNotNull(tokeniser.getState());
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testConsumeCharacterReference() {
        assertEquals((char) 32, tokeniser.consumeCharacterReference(null, false));
        assertEquals((char) 32, tokeniser.consumeCharacterReference(null, true));
        assertEquals((char) 160, tokeniser.consumeCharacterReference("&szlig;", false));
        assertEquals((char) 154, tokeniser.consumeCharacterReference("&OElig;", false));
        assertEquals(Entities.getCharacterByName("ampersand"), tokeniser.consumeCharacterReference("&amp;", false));
        assertEquals(Entities.getCharacterByName("apos;"), tokeniser.consumeCharacterReference("&apos;", false));
        assertEquals(Entities.getCharacterByName("quot;"), tokeniser.consumeCharacterReference("&quot;", false));

        assertEquals(null, tokeniser.consumeCharacterReference("&amp;amp;", false));
        assertEquals(null, tokeniser.consumeCharacterReference("&quot;quot;", false));
        assertEquals(null, tokeniser.consumeCharacterReference("&apos;apos;", false));
        assertEquals(null, tokeniser.consumeCharacterReference("&szlig;szlig;", false));
        assertEquals(null, tokeniser.consumeCharacterReference("&OElig;OElig;", false));
    }

    @Test(expected = AssertionError.class)
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("numeric reference with no numerals");
    }

    @Test(expected = AssertionError.class)
    public void testError() {
        tokeniser.error(TokeniserState.Data);
    }

    @Test
    public void testEOFError() {
        tokeniser.reader.consume(TEST_HTML.length());
        tokeniser.eofError(TokeniserState.Data);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(false);
        assertTrue(tokeniser.isAppropriateEndTagToken());

        tokeniser.createTagPending(false);
        tokeniser.createTagPending(true);
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(false);
        assertEquals("html", tokeniser.appropriateEndTagName());

        tokeniser.createTagPending(false);
        tokeniser.createTagPending(true);
        assertEquals(null, tokeniser.appropriateEndTagName());
    }
}