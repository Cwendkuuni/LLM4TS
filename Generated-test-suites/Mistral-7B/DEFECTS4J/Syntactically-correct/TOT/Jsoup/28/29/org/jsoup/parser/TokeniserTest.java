package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.util.ParseError;
import org.jsoup.util.CharacterInput;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList errors;
    private CharacterInput characterInput;

    @Before
    public void setUp() {
        characterInput = new CharacterInput(new ByteArrayInputStream("test".getBytes()));
        tokeniser = new Tokeniser(characterInput, new ArrayList<>());
        errors = tokeniser.errors;
    }

    @Test
    public void testReadNormalCharacter() {
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("t", token.data);
    }

    @Test
    public void testReadSelfClosingTag() {
        characterInput = new CharacterInput(new ByteArrayInputStream("<img src=\"test.png\" />".getBytes()));
        tokeniser = new Tokeniser(characterInput, new ArrayList<>());

        Token startTag = tokeniser.read();
        Token endTag = tokeniser.read();

        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertEquals("img", startTag.tagName);
        assertEquals(Token.TokenType.EndTag, endTag.type);
        assertEquals("img", endTag.tagName);
    }

    @Test(expected = AssertionError.class)
    public void testReadUnmatchedEndTag() {
        characterInput = new CharacterInput(new ByteArrayInputStream("<div>Hello<p></div>"));
        tokeniser = new Tokeniser(characterInput, new ArrayList<>());

        Token startTagDiv = tokeniser.read();
        Token startTagP = tokeniser.read();

        tokeniser.emit(startTagDiv); // emit start tag div

        Token endTagDiv = tokeniser.read(); // read end tag div
        Token endTagP = tokeniser.read(); // read end tag p

        // This test should fail because the end tag 'p' does not match the start tag 'div'
        tokeniser.emit(endTagDiv);
        tokeniser.emit(endTagP);
    }

    @Test
    public void testReadEntities() {
        characterInput = new CharacterInput(new ByteArrayInputStream("&nbsp; &hellip; &copy; 2022 &eacute; &rsquo;".getBytes()));
        tokeniser = new Tokeniser(characterInput, new ArrayList<>());

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("\u00A0", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("\u2026", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("C", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("é", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("‘", token.data);
    }

    @Test(expected = AssertionError.class)
    public void testReadInvalidCharacterReference() {
        characterInput = new CharacterInput(new ByteArrayInputStream("&#XYZ;".getBytes()));
        tokeniser = new Tokeniser(characterInput, new ArrayList<>());

        tokeniser.consumeCharacterReference(null, false);
    }

    @Test
    public void testReadCharacterReferenceWithAdditionalAllowedCharacter() {
        characterInput = new CharacterInput(new ByteArrayInputStream("&#xA9;&#xA0;".getBytes()));
        tokeniser = new Tokeniser(characterInput, new ArrayList<>());

        Character c = tokeniser.consumeCharacterReference('A', false);
        assertEquals('\u0041', c.intValue());

        c = tokeniser.consumeCharacterReference(' ', false);
        assertEquals('\u0020', c.intValue());
    }

    @Test
    public void testReadCharacterReferenceNamed() {
        characterInput = new CharacterInput(new ByteArrayInputStream("&copy;".getBytes()));
        tokeniser = new Tokeniser(characterInput, new ArrayList<>());

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("\u00A9", token.data);
    }

    @Test
    public void testReadCharacterReferenceNamedWithAttributes() {
        characterInput = new CharacterInput(new ByteArrayInputStream("class=\"foo\" &copy;".getBytes()));
        tokeniser = new Tokeniser(characterInput, new ArrayList<>());

        Character c = tokeniser.consumeCharacterReference(null, true);
        assertNull(c);
    }

    @Test
    public void testReadComment() {
        characterInput = new CharacterInput(new ByteArrayInputStream("<!-- test comment -->".getBytes()));
        tokeniser = new Tokeniser(characterInput, new ArrayList<>());

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals("test comment", token.data);
    }

    @Test(expected = AssertionError.class)
    public void testReadSelfClosingTagWithAttributes() {
        characterInput = new CharacterInput(new ByteArrayInputStream("<img src=\"test.png\" alt=\"test\" />".getBytes()));
        tokeniser = new Tokeniser(characterInput, new ArrayList<>());

        Token startTag = tokeniser.read();
        Token endTag = tokeniser.read();

        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertEquals("img", startTag.tagName);
        assertNotNull(startTag.attributes);
        assertEquals(2, startTag.attributes.size());

        // This test should fail because self-closing tags do not have attributes
        tokeniser.emit(startTag);
        tokeniser.emit(endTag);
    }

    @Test
    public void testTransitionStates() {
        characterInput = new CharacterInput(new ByteArrayInputStream(" <script src=\"test.js\"></script> <div>Hello World!</div> ".getBytes()));
        tokeniser = new Tokeniser(characterInput, new ArrayList<>());

        TokeniserState preScript = tokeniser.getState();
        tokeniser.advanceTransition(TokeniserState.Script);
        TokeniserState script = tokeniser.getState();

        assertEquals(preScript, TokeniserState.Data);
        assertEquals(script, TokeniserState.Script);

        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.advanceTransition(TokeniserState.Data);

        assertEquals(script, TokeniserState.Script);
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testErrorHandling() {
        characterInput = new CharacterInput(new ByteArrayInputStream("<!--test-->\n<script>alert('test')</script>".getBytes()));
        tokeniser = new Tokeniser(characterInput, new ArrayList<>());

        // Test if character reference error is handled correctly
        characterInput.mark(1);
        tokeniser.consumeCharacterReference('#', false);
        tokeniser.error(TokeniserState.Data);

        // Test if end tag error is handled correctly
        characterInput.mark(6);
        tokeniser.read();
        tokeniser.emit(new Token.EndTag("script"));
        tokeniser.error(TokeniserState.Script);

        // Test if comment error is handled correctly
        characterInput.mark(11);
        tokeniser.readCommentPending();
        tokeniser.error(TokeniserState.Data);

        // Test if invalid character is handled correctly
        characterInput.mark(17);
        tokeniser.emit('X');
        tokeniser.error(TokeniserState.Data);

        // Test if eof error is handled correctly
        characterInput.mark(17);
        characterInput.consume();
        tokeniser.error(TokeniserState.Data);

        // Test if state error is handled correctly
        characterInput = new CharacterInput(new ByteArrayInputStream("<!--test-->\n<script>alert('test')</script>".getBytes()));
        tokeniser = new Tokeniser(characterInput, new ArrayList<>());
        tokeniser.transition(TokeniserState.Script);
        tokeniser.error(TokeniserState.Script);

        List<ParseError> parseErrors = tokeniser.errors.getErrors();
        assertEquals(5, parseErrors.size());
    }

    @Test
    public void testConsumeCharacterReferenceWithName() {
        characterInput = new CharacterInput(new ByteArrayInputStream("&nbsp; &hellip; &copy; 2022 &eacute; &rsquo;".getBytes()));
        tokeniser = new Tokeniser(characterInput, new ArrayList<>());

        Character c = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\u00A0', c.intValue());

        c = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\u2026', c.intValue());

        c = tokeniser.consumeCharacterReference(null, false);
        assertEquals('C', c.data.charAt(0));

        c = tokeniser.consumeCharacterReference(null, false);
        assertEquals('é', c.data.charAt(0));

        c = tokeniser.consumeCharacterReference(null, false);
        assertEquals("‘", c.data.charAt(0));
    }

    @Test
    public void testCreateAndEmitTagPending() {
        characterInput = new CharacterInput(new ByteArrayInputStream("<div>Hello <span class=\"foo\">World!</span></div>".getBytes()));
        tokeniser = new Tokeniser(characterInput, new ArrayList<>());

        Token tagDiv = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Token startTagSpan = tokeniser.createTagPending(false);
        tokeniser.emitTagPending();

        Token tagSpan = tokeniser.read();
        assertSame(startTagSpan, tagSpan);

        tokeniser.emitTagPending();
        Token tagDiv2 = tokeniser.read();
        assertSame(tagDiv, tagDiv2);
    }

    @Test
    public void testCreateAndEmitCommentPending() {
        characterInput = new CharacterInput(new ByteArrayInputStream("<!--test-->\n<!--test2-->".getBytes()));
        tokeniser = new Tokeniser(characterInput, new ArrayList<>());

        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        Token comment1 = tokeniser.read();
        Token comment2 = tokeniser.read();

        assertEquals(Token.TokenType.Comment, comment1.type);
        assertEquals(Token.TokenType.Comment, comment2.type);
    }

    @Test
    public void testCreateAndEmitDoctypePending() {
        characterInput = new CharacterInput(new ByteArrayInputStream("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">".getBytes()));
        tokeniser = new Tokeniser(characterInput, new ArrayList<>());

        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        Token doctype = tokeniser.read();
        assertNotNull(doctype);
        assertEquals("[DOCTYPE]", doctype.tagName);
    }
}