package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Tokeniser class.
 */
public class TokeniserTest {

    private CharacterReader reader;
    private Tokeniser tokeniser;
    private List<ParseError> errors;

    @Before
    public void setup() {
        reader = new CharacterReader("test.html");
        tokeniser = new Tokeniser(reader);
        errors = tokeniser.getErrors();
    }

    @Test
    public void testReadEmptyStream() {
        Token token = tokeniser.read();
        assertNull(token);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadCharacter() {
        tokeniser.emit('A');
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.emit('B');
        Token token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("AB", token.getData());
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadStartTag() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.emit('<');
        tokeniser.emit('p');
        tokeniser.emit('>');
        Token token = tokeniser.read();
        assertEquals(Token.StartTag.class, token.getClass());
        assertEquals("p", token.tagName);
        assertFalse(token.selfClosing);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadSelfClosingStartTag() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.emit('<');
        tokeniser.emit('img');
        tokeniser.emit(' ');
        tokeniser.emit('src');
        tokeniser.emit('=');
        tokeniser.emit('"');
        tokeniser.emit('test.jpg');
        tokeniser.emit('"');
        tokeniser.emit('>');
        Token token = tokeniser.read();
        assertEquals(Token.StartTag.class, token.getClass());
        assertEquals("img", token.tagName);
        assertTrue(token.selfClosing);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadEndTag() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.emit('<');
        tokeniser.emit('p');
        tokeniser.emit('>');
        tokeniser.emit('<');
        tokeniser.emit('/');
        tokeniser.emit('p');
        tokeniser.emit('>');
        Token token = tokeniser.read();
        assertEquals(Token.EndTag.class, token.getClass());
        assertEquals("p", token.tagName);
        assertEquals(0, errors.size());
    }

    @Test(expected = RuntimeException.class)
    public void testReadInvalidEndTag() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.emit('<');
        tokeniser.emit('/');
        tokeniser.emit('b'); // incorrect tag name
        tokeniser.emit('>');
        tokeniser.read();
    }

    @Test
    public void testReadComment() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.emit('<');
        tokeniser.emit('!');
        tokeniser.emit('--');
        tokeniser.emit('>');
        tokeniser.emit('This is a comment');
        tokeniser.emit('<');
        tokeniser.emit('/');
        tokeniser.emit('!');
        tokeniser.emit('--');
        tokeniser.emit('>');
        Token token = tokeniser.read();
        assertEquals(Token.Comment.class, token.getClass());
        assertEquals("This is a comment", token.getData());
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadDoctype() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.emit('<');
        tokeniser.emit('!');
        tokeniser.emit('D');
        tokeniser.emit('O');
        tokeniser.emit('C');
        tokeniser.emit('T');
        tokeniser.emit('Y');
        tokeniser.emit('P');
        tokeniser.emit('E');
        tokeniser.emit('>');
        Token token = tokeniser.read();
        assertEquals(Token.Doctype.class, token.getClass());
        assertEquals("DOCTYPE", token.getData());
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadUnexpectedCharacter() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.emit('<');
        tokeniser.emit('/');
        tokeniser.emit('!');
        tokeniser.emit('>'); // unexpected character
        tokeniser.read();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected character in input", error.getMessage());
        assertEquals(reader.getCurrentPosition(), error.getPosition());
        assertEquals(Tokeniser.TokeniserState.Data, error.getState());
    }

    @Test
    public void testReadUnexpectedEndOfStream() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.emit('>'); // it is assumed that there is an end tag here
        tokeniser.read();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF)", error.getMessage());
    }

    @Test
    public void testReadCharacterReference() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.emit('&');
        tokeniser.emit('#');
        tokeniser.emit('x');
        tokeniser.emit('a');
        tokeniser.emit(';');
        Token token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("\u003C", token.getData());
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadCharacterReferenceWithAdditionalAllowedCharacter() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.consumeCharacterReference('x', true);
        Token token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("\u03B4", token.getData());
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadCharacterReferenceWithInvalidCharacterSequence() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.emit('>');
        tokeniser.read();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Invalid character reference", error.getMessage());
    }

    @Test
    public void testReadCharacterReferenceWithInvalidNamedEntity() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.consumeCharacterReference("foobar", false);
        tokeniser.read();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Invalid character reference", error.getMessage());
    }

    @Test
    public void testReadCharacterReferenceWithExtraCharacterAfterSemicolon() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.consumeCharacterReference("nbsp", false);
        tokeniser.emit('>');
        tokeniser.read();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Missing semi-colon in character reference", error.getMessage());
    }

    @Test
    public void testReadCharacterReferenceWithExtraCharacterBeforeSemicolon() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.consumeCharacterReference("nbsp", false);
        tokeniser.emit('x');
        tokeniser.read();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Missing semi-colon in character reference", error.getMessage());
    }

    @Test
    public void testReadCharacterReferenceWithInvalidCharacterInCharacterReference() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.consumeCharacterReference("0000", false);
        tokeniser.read();
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Invalid Unicode point in character reference", error.getMessage());
    }

    @Test
    public void testReadCharacterReferenceWithOutOfRangeCharacter() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.consumeCharacterReference("\uD801", false);
        Token token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals(Tokeniser.replacementChar, token.getData().charAt(0));
        assertEquals(0, errors.size());
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        assertEquals(Token.StartTag.class, tokeniser.tagPending.getClass());
        assertEquals(0, tokeniser.tagPending.attributes.size());
        tokeniser.createTagPending(false);
        assertEquals(Token.EndTag.class, tokeniser.tagPending.getClass());
        assertEquals(0, tokeniser.tagPending.attributes.size());
    }

    @Test
    public void testCreateTagPendingWithAttributes() {
        tokeniser.emit(Tokeniser.TokeniserState.StartTagBegin.name());
        tokeniser.emit('<');
        tokeniser.emit('a');
        tokeniser.emit(' ');
        tokeniser.emit('href');
        tokeniser.emit('=');
        tokeniser.emit('"');
        tokeniser.emit("http://example.com");
        tokeniser.emit('"');
        tokeniser.emit('>');
        tokeniser.createTagPending(true);
        assertEquals("a", tokeniser.tagPending.tagName);
        assertEquals(1, tokeniser.tagPending.attributes.size());
        assertEquals("href", tokeniser.tagPending.attributes.keySet().iterator().next());
        assertEquals("http://example.com", tokeniser.tagPending.attributes.get("href"));
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.emit('<');
        tokeniser.emit('p');
        tokeniser.emit('>');
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertEquals(Token.StartTag.class, token.getClass());
        assertEquals("p", token.tagName);
        assertFalse(token.selfClosing);
        assertEquals(0, errors.size());
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.emit('<');
        tokeniser.emit('!');
        tokeniser.emit('--');
        tokeniser.emit('>');
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        assertEquals(Token.Comment.class, token.getClass());
        assertEquals("", token.getData());
        assertEquals(0, errors.size());
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.emit('<');
        tokeniser.emit('!');
        tokeniser.emit('D');
        tokeniser.emit('O');
        tokeniser.emit('C');
        tokeniser.emit('T');
        tokeniser.emit('Y');
        tokeniser.emit('P');
        tokeniser.emit('E');
        tokeniser.emit('>');
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        assertEquals(Token.Doctype.class, token.getClass());
        assertEquals("DOCTYPE", token.getData());
        assertEquals(0, errors.size());
    }

    @Test
    public void testTransition() {
        tokeniser.transition(Tokeniser.TokeniserState.StartTagBegin);
        tokeniser.emit('<');
        tokeniser.emit('a');
        tokeniser.emit('>');
        assertEquals(Tokeniser.TokeniserState.InsideHTML.name(), tokeniser.getState().name());
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.emit('<');
        tokeniser.emit('/');
        tokeniser.emit('p');
        tokeniser.emit('>');
        tokeniser.advanceTransition(Tokeniser.TokeniserState.InsideHTML);
        tokeniser.emit('<');
        tokeniser.emit('!');
        tokeniser.emit('--');
        tokeniser.emit('>');
        assertEquals(Tokeniser.TokeniserState.Comment.name(), tokeniser.getState().name());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.emit(Tokeniser.TokeniserState.Data.name());
        tokeniser.emit('<');
        tokeniser.emit('/');
        tokeniser.emit('img');
        tokeniser.emit('>');
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.emit('<');
        tokeniser.emit('/');
        tokeniser.emit('img');
        tokeniser.emit('>');
        assertEquals(Tokeniser.TokeniserState.Data.name(), tokeniser.getState().name());
    }
}