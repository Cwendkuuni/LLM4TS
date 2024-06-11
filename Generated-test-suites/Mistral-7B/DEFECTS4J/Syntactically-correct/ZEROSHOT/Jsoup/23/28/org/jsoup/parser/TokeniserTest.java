package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Test class for Tokeniser.
 */
public class TokeniserTest {

    private static final String TEST_HTML = "<html><head><title>Test</title>" +
            "<script type=\"text/javascript\">function test() { alert('Hello World'); }</script>" +
            "</head><body><div id=\"testDiv\"></div></body></html>";

    private ByteArrayInputStream inputStream;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        errors = new ParseErrorList();
        inputStream = new ByteArrayInputStream(TEST_HTML.getBytes());
        tokeniser = new Tokeniser(new CharacterReader(inputStream), errors);
    }

    @After
    public void tearDown() {
        tokeniser = null;
        inputStream.close();
    }

    @Test
    public void testInitialState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testReadBasicCharacters() throws IOException {
        for (int i = 0; i < TEST_HTML.length(); ++i) {
            Token token = tokeniser.read();
            assertEquals(Token.TokenType.Character, token.getType());
            assertEquals(TEST_HTML.charAt(i), token.getData().charAt(0));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testReadSelfClosingTag() throws IOException {
        tokeniser.read(); // <html
        tokeniser.read(); // <head
        tokeniser.read(); // <title
        tokeniser.read(); // Test
        tokeniser.read(); // >
        tokeniser.read(); // <script
        tokeniser.read(); // type
        tokeniser.read(); // =
        tokeniser.read(); // "text/javascript"
        tokeniser.read(); // >
        tokeniser.read(); // function
        tokeniser.read(); // test
        tokeniser.read(); // {
        tokeniser.read(); // <-- Should throw an exception here
    }

    @Test
    public void testReadScriptTag() throws IOException {
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.getType());
        assertEquals("script", token.getData().tagName);
        assertSame(Tag.SCRIPT, token.getData().tag.getTagName());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Attribute, token.getType());
        assertEquals("type", token.getData().attrName);
        assertEquals("text/javascript", token.getData().attrValue);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.getType());
        assertEquals('>', token.getData().charAt(0));

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.getType());
        assertNull(token.getData());
    }

    @Test
    public void testReadEndTag() throws IOException {
        for (int i = 0; i < TEST_HTML.length(); ++i) {
            Token token = tokeniser.read();
            // ...
        }
        tokeniser.acknowledgeSelfClosingFlag();

        tokeniser.emit(createTagPending(false));
        tokeniser.emitTagPending();
        tokeniser.read(); // <-- Should read <div here
        tokeniser.emit(createEndTagPending(Tag.DIV));
        tokeniser.emitCommentPending();
        tokeniser.emitCommentPending();
        tokeniser.error("Unexpected character 'e'"); // <-- Should throw an error here

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.getType());
        assertEquals("div", token.getData().tagName);
        assertSame(Tag.DIV, token.getData().tag.getTagName());
    }

    @Test
    public void testReadNonPrintableCharacters() throws IOException {
        tokeniser.advanceTransition(TokeniserState.CharRef);

        Character c = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\u00A9', c);

        tokeniser.consumeCharacterReference('\u0026', false); // '&'
        tokeniser.consumeCharacterReference('\u0022', true); // '"' in attribute
    }

    @Test
    public void testReadNamedCharacterReference() throws IOException {
        tokeniser.advanceTransition(TokeniserState.NamedCharRef);

        Character c = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\u00A9', c); // copyright sign

        c = tokeniser.consumeCharacterReference(null, true);
        assertEquals('\u0026', c); // '&' in attribute
    }

    @Test(expected = IllegalStateException.class)
    public void testReadAfterEOF() throws IOException {
        tokeniser.acknowledgeSelfClosingFlag();

        for (int i = 0; i < TEST_HTML.length(); ++i) {
            tokeniser.read();
        }

        tokeniser.read(); // <-- Should throw an exception here
    }

    @Test
    public void testErrorHandling() throws IOException {
        tokeniser.advanceTransition(TokeniserState.Data);
        tokeniser.transition(TokeniserState.Script);

        tokeniser.error(TokeniserState.Script);
        assertEquals(1, errors.size());
        assertEquals("Unexpected character 'e' in input state [script]", errors.get(0).getMessage());

        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.transition(TokeniserState.Data);

        tokeniser.error("Unexpected character 'e'");
        assertEquals(2, errors.size());
        assertEquals("Unexpected character 'e' in input state [Data]", errors.get(1).getMessage());
    }

    @Test
    public void testEofErrorHandling() throws IOException {
        tokeniser.advanceTransition(TokeniserState.Script);

        tokeniser.error(TokeniserState.Script);
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [script]", errors.get(0).getMessage());

        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.transition(TokeniserState.Data);

        tokeniser.eofError(TokeniserState.Data);
        assertEquals(2, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(1).getMessage());
    }

    @Test
    public void testReadComments() throws IOException {
        tokeniser.advanceTransition(TokeniserState.Comment);

        List<Token> comments = new ArrayList<>();
        while (tokeniser.getState() == TokeniserState.Comment) {
            Token token = tokeniser.read();
            comments.add(token);
        }

        assertEquals(2, comments.size());
        assertEquals("<!-- Test -->", comments.get(0).getData().getData());
        assertEquals("<!-- Test 2 -->", comments.get(1).getData().getData());
    }

    @Test
    public void testReadDoctype() throws IOException {
        tokeniser.advanceTransition(TokeniserState.Doctype);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.getType());
        assertEquals("<!doctype html>", token.getData().getData());
        assertEquals(Tag.HTML, token.getData().tag.getTagName());
    }

    private Token.Tag createTagPending(boolean start) {
        return tokeniser.createTagPending(start);
    }

    private Token.EndTag createEndTagPending(Tag tag) {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = tag.name();
        return endTag;
    }
}