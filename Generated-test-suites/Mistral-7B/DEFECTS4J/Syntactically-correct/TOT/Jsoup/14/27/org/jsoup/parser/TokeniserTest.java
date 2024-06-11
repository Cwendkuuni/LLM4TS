import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private CharacterReader characterReader;

    @Before
    public void setup() {
        characterReader = new CharacterReader(new ByteArrayInputStream("<html><head></head><body></body>".getBytes()));
        tokeniser = new Tokeniser(characterReader);
    }

    @Test
    public void testReadEmptyInput() {
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadCharacter() {
        assertEquals("a", tokeniser.read().toString());
        assertEquals("b", tokeniser.read().toString());
    }

    @Test
    public void testReadStartTag() {
        Token tag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, tag.type);
        assertNotNull(tag.tagName);
        assertEquals("html", tag.tagName);
    }

    @Test
    public void testReadEndTag() {
        tokeniser.emit(new Token.StartTag("html"));
        tokeniser.emit(new Token.Character(' '));
        Token tag = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, tag.type);
        assertNotNull(tag.tagName);
        assertEquals("html", tag.tagName);
    }

    @Test
    public void testReadSelfClosingTag() {
        tokeniser.transition(TokeniserState.SelfClosingStartTag);
        Token tag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, tag.type);
        assertNotNull(tag.tagName);
        assertEquals("img", tag.tagName);
        assertTrue(tag.selfClosing);
    }

    @Test
    public void testReadComment() {
        tokeniser.transition(TokeniserState.Comment);
        tokeniser.emit(new Token.Character('!'));
        tokeniser.emit(new Token.Character('*'));
        tokeniser.emit(new Token.Character('!'));
        Token comment = tokeniser.read();
        assertEquals(Token.TokenType.Comment, comment.type);
        assertEquals("", comment.content);
    }

    @Test
    public void testReadDoctype() {
        tokeniser.transition(TokeniserState.Doctype);
        tokeniser.emit(new Token.Character('<'));
        tokeniser.emit(new Token.Character('d'));
        tokeniser.emit(new Token.Character('o'));
        tokeniser.emit(new Token.Character('c'));
        tokeniser.emit(new Token.Character('t'));
        tokeniser.emit(new Token.Character('y'));
        tokeniser.emit(new Token.Character('p'));
        tokeniser.emit(new Token.Character('e'));
        tokeniser.emit(new Token.Character('>'));
        Token doctype = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, doctype.type);
        assertEquals("", doctype.content);
    }

    @Test
    public void testReadEmptyLine() {
        tokeniser.transition(TokeniserState.Data);
        tokeniser.emit(new Token.Character('\n'));
        tokeniser.emit(new Token.Character('\n'));
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadCharacterReference() throws IOException {
        characterReader = new CharacterReader(new ByteArrayInputStream("<html>&#x1F345;".getBytes()));
        tokeniser = new Tokeniser(characterReader);
        tokeniser.transition(TokeniserState.Data);
        Character reference = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\uD834\uDC45', reference.intValue());
    }

    @Test
    public void testReadCharacterReferenceInvalid() throws IOException {
        characterReader = new CharacterReader(new ByteArrayInputStream("<html>&x1F345;".getBytes()));
        tokeniser = new Tokeniser(characterReader);
        tokeniser.transition(TokeniserState.Data);
        Character reference = tokeniser.consumeCharacterReference(null, false);
        assertNull(reference);
    }

    @Test
    public void testReadCharacterReferenceInvalidEntity() throws IOException {
        characterReader = new CharacterReader(new ByteArrayInputStream("<html>&invalid;".getBytes()));
        tokeniser = new Tokeniser(characterReader);
        tokeniser.transition(TokeniserState.Data);
        Character reference = tokeniser.consumeCharacterReference(null, false);
        assertNull(reference);
    }

    @Test
    public void testReadEntityReference() throws IOException {
        characterReader = new CharacterReader(new ByteArrayInputStream("<html>&ntilde;".getBytes()));
        tokeniser = new Tokeniser(characterReader);
        tokeniser.transition(TokeniserState.Data);
        Character reference = tokeniser.consumeCharacterReference(null, false);
        assertEquals('ñ', reference.intValue());
    }

    @Test
    public void testReadCharacterReferenceAttribute() throws IOException {
        characterReader = new CharacterReader(new ByteArrayInputStream("<html <a href=\"#&eacute;\">Test</a>".getBytes()));
        tokeniser = new Tokeniser(characterReader);
        tokeniser.transition(TokeniserState.Data);
        Character reference = tokeniser.consumeCharacterReference(null, true);
        assertEquals('é', reference.intValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testReadInvalidState() {
        tokeniser.transition(new TokeniserState("InvalidState"));
        tokeniser.read();
    }
}