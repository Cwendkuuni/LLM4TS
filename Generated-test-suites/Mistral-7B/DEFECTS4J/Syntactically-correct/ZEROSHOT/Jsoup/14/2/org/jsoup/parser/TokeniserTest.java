package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link Tokeniser}
 */
public class TokeniserTest {

    private Tokeniser tokeniser;
    private CharacterReader characterReader;
    private List<ParseError> errors;

    @Before
    public void setUp() {
        characterReader = new CharacterReader(new ByteArrayInputStream("<html><head><title>Test</title></head><body></body>".getBytes()));
        tokeniser = new Tokeniser(characterReader);
        errors = tokeniser.getErrors();
    }

    @Test
    public void testConstructor() {
        tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<html><head><title>Test</title></head><body></body>".getBytes())));
        assertNotNull(tokeniser.getState());
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testReadEmptyStream() {
        tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream(new byte[0][])));
        assertNull(tokeniser.read());
    }

    @Test(expected = IllegalStateException.class)
    public void testReadWithoutInitialisingReader() {
        tokeniser = new Tokeniser(null);
        tokeniser.read();
    }

    @Test
    public void testReadCharacter() {
        assertEquals(new Token.Character('<'), tokeniser.read());
        assertEquals(new Token.Character('h'), tokeniser.read());
        assertEquals(new Token.Character('t'), tokeniser.read());
        assertEquals(new Token.Character('m'), tokeniser.read());
        assertEquals(new Token.Character('l'), tokeniser.read());
        assertEquals(new Token.Character('/'), tokeniser.read());
        assertEquals(new Token.Character('>'), tokeniser.read());
    }

    @Test
    public void testReadTagStart() {
        Token tag = tokeniser.read();
        assertTrue(tag instanceof Token.StartTag);
        assertEquals(Token.Tag.HTML, ((Token.StartTag) tag).getTagName());
        tag = tokeniser.read();
        assertTrue(tag instanceof Token.StartTag);
        assertEquals(Token.Tag.HEAD, ((Token.StartTag) tag).getTagName());
    }

    @Test
    public void testReadTagEnd() {
        Token tag = tokeniser.read();
        assertTrue(tag instanceof Token.EndTag);
        assertEquals(Token.Tag.HEAD, ((Token.EndTag) tag).getTagName());
    }

    @Test
    public void testReadComment() {
        Token tag = tokeniser.read();
        assertTrue(tag instanceof Token.Comment);
        assertEquals("<!--", ((Token.Comment) tag).getData());
    }

    @Test
    public void testReadDoctype() {
        tokeniser.transition(TokeniserState.Doctype);
        Token tag = tokeniser.read();
        assertTrue(tag instanceof Token.Doctype);
        String doctypeDeclaration = "DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"";
        assertEquals(doctypeDeclaration, tag.getData());
    }

    @Test
    public void testConsumeCharacterReference() {
        characterReader.advance(); // consume '<'
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('h', character);

        character = tokeniser.consumeCharacterReference('#', false);
        assertNull(character);

        character = tokeniser.consumeCharacterReference("amp", false);
        assertEquals('&', character);

        character = tokeniser.consumeCharacterReference("gt", false);
        assertEquals('>', character);

        character = tokeniser.consumeCharacterReference("lt", false);
        assertEquals('<', character);

        character = tokeniser.consumeCharacterReference("quot", false);
        assertEquals('\"', character);

        character = tokeniser.consumeCharacterReference("apos", false);
        assertEquals("'", character);

        character = tokeniser.consumeCharacterReference("#x26", false);
        assertEquals('&', character);
    }

    @Test
    public void testConsumeCharacterReferenceInAttribute() {
        characterReader.advance(); // consume '<'
        characterReader.advance(); // consume 't'
        characterReader.advance(); // consume 'r'
        characterReader.advance(); // consume ' '
        characterReader.advance(); // consume 'a'
        characterReader.advance(); // consume 't'
        characterReader.advance(); // consume '='
        characterReader.advance(); // consume '"'
        characterReader.advance(); // consume 'a'
        characterReader.advance(); // consume 'm'
        characterReader.advance(); // consume 'p'
        characterReader.advance(); // consume ';'

        Character character = tokeniser.consumeCharacterReference(null, true);
        assertNull(character);
    }

    @Test
    public void testEmit() {
        tokeniser.emit(new Token.Character('h'));
        tokeniser.emit(new Token.Character('t'));
        tokeniser.emit(new Token.Character('m'));
        tokeniser.emit(new Token.Character('/'));
        tokeniser.emit(new Token.Character('>'));

        Token tag = tokeniser.read();
        assertTrue(tag instanceof Token.StartTag);
        assertEquals(Token.Tag.HTML, ((Token.StartTag) tag).getTagName());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Token tag = tokeniser.read();
        assertTrue(tag instanceof Token.StartTag);
        assertEquals(Token.Tag.HTML, ((Token.StartTag) tag).getTagName());
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();

        Token tag = tokeniser.read();
        assertTrue(tag instanceof Token.Comment);
        assertEquals("<!--", ((Token.Comment) tag).getData());
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();

        Token tag = tokeniser.read();
        assertTrue(tag instanceof Token.Doctype);
        String doctypeDeclaration = "DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"";
        assertEquals(doctypeDeclaration, tag.getData());
    }

    @Test
    public void testAdvanceTransition() {
        tokeniser.transition(TokeniserState.Script);
        tokeniser.advanceTransition(TokeniserState.Doctype);

        Token tag = tokeniser.read();
        assertTrue(tag instanceof Token.Doctype);
    }

    @Test
    public void testGetState() {
        assertEquals(TokeniserState.Data, tokeniser.getState());
        tokeniser.transition(TokeniserState.Script);
        assertEquals(TokeniserState.Script, tokeniser.getState());
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.Script);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        assertTrue(tokeniser.isAppropriateEndTagToken());

        tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        tokeniser.setTrackErrors(false);
        assertFalse(tokeniser.isTrackErrors());

        tokeniser.setTrackErrors(true);
        assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testError() {
        tokeniser.error(TokeniserState.Data);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testEofError() {
        tokeniser.eofError(TokeniserState.Script);
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        assertTrue(errors.size() > 0);
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}