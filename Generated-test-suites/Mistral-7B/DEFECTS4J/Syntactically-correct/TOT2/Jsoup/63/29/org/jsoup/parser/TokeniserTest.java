import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TokeniserTest {
    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        reader = Mockito.mock(CharacterReader.class);
        errors = Mockito.mock(ParseErrorList.class);
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testReadNormalCharacter() {
        when(reader.current()).thenReturn('a');
        when(reader.read()).thenReturn('a');
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('a', token.charSequence().charAt(0));
        verify(reader).read();
    }

    @Test
    public void testReadStartTag() {
        when(reader.current()).thenReturn('<');
        when(reader.consume()).thenReturn('p');
        when(reader.consume()).thenReturn('>');
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals(Token.StartTag.class, token.getClass());
        assertEquals("p", token.tagName());
        verify(reader).consume(2);
    }

    @Test
    public void testEmitCharacter() {
        tokeniser.emit(new Token.Character("a"));
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals('a', token.charSequence().charAt(0));
    }

    @Test
    public void testEmitStartTag() {
        tokeniser.emit(new Token.StartTag("p"));
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("p", token.tagName());
    }

    @Test
    public void testReadEmptyString() {
        when(reader.isEmpty()).thenReturn(true);
        Token token = tokeniser.read();
        assertNull(token);
        verify(reader).isEmpty();
    }

    @Test
    public void testReadSelfClosingTag() {
        when(reader.current()).thenReturn('<');
        when(reader.consume()).thenReturn('i');
        when(reader.consume()).thenReturn('>');
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("i", token.tagName());
        assertTrue(token.selfClosing());
        verify(reader).consume(2);
    }

    @Test
    public void testReadEndTag() {
        when(reader.current()).thenReturn('/');
        when(reader.consume()).thenReturn('/');
        when(reader.consume()).thenReturn('p');
        when(reader.consume()).thenReturn('>');
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("p", token.tagName());
        verify(reader).consume(3);
    }

    @Test
    public void testReadInvalidCharacterReference() {
        when(reader.current()).thenReturn('#');
        when(reader.consume()).thenReturn('X');
        when(reader.consume()).thenReturn('A');
        when(reader.consume()).thenReturn(';');
        tokeniser.error(TokeniserState.Data);
        verify(errors).add(any(ParseError.class));
        verify(reader).consume(4);
    }

    @Test
    public void testReadInvalidNamedCharacterReference() {
        when(reader.current()).thenReturn('#');
        when(reader.consume()).thenReturn('a');
        when(reader.consume()).thenReturn('m');
        when(reader.consume()).thenReturn('p');
        when(reader.consume()).thenReturn(';');
        tokeniser.error(TokeniserState.Data);
        verify(errors).add(any(ParseError.class));
        verify(reader).consume(5);
    }

    @Test
    public void testReadInvalidCharacterReferenceWithAllowedCharacter() {
        when(reader.current()).thenReturn('#');
        when(reader.consume()).thenReturn('X');
        when(reader.consume()).thenReturn('A');
        when(reader.consume()).thenReturn('&');
        tokeniser.error(TokeniserState.Data);
        verify(errors).add(any(ParseError.class));
        verify(reader).consume(4);
    }

    @Test
    public void testReadInvalidHexCharacterReference() {
        when(reader.current()).thenReturn('#');
        when(reader.consume()).thenReturn('X');
        when(reader.consume()).thenReturn('F');
        when(reader.consume()).thenReturn('F');
        when(reader.consume()).thenReturn(';');
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        assertArrayEquals(new int[]{Tokeniser.replacementChar}, codepoints);
        verify(errors).add(any(ParseError.class));
        verify(reader).consume(5);
    }

    @Test
    public void testReadCharacterReferenceWithAllowedCharacter() {
        when(reader.current()).thenReturn('#');
        when(reader.consume()).thenReturn('X');
        when(reader.consume()).thenReturn('A');
        when(reader.consume()).thenReturn('&');
        when(reader.consume()).thenReturn('a');
        when(reader.consume()).thenReturn(';');
        int[] codepoints = tokeniser.consumeCharacterReference('&', false);
        assertArrayEquals(new int[]{0x41}, codepoints);
        verify(reader).consume(6);
    }

    @Test
    public void testReadCharacterReferenceInAttribute() {
        when(reader.current()).thenReturn('#');
        when(reader.consume()).thenReturn('X');
        when(reader.consume()).thenReturn('F');
        when(reader.consume()).thenReturn('F');
        when(reader.consume()).thenReturn(';');
        tokeniser.error(TokeniserState.Data);
        verify(errors).add(any(ParseError.class));
        verify(reader).consume(5);
    }

    @Test
    public void testReadNamedCharacterReferenceWithAllowedCharacter() {
        when(reader.current()).thenReturn('#');
        when(reader.consume()).thenReturn('a');
        when(reader.consume()).thenReturn('m');
        when(reader.consume()).thenReturn('p');
        when(reader.consume()).thenReturn(';');
        int[] codepoints = tokeniser.consumeCharacterReference(null, true);
        assertArrayEquals(new int[]{0x61, 0x6D, 0x65}, codepoints);
        verify(reader).consume(5);
    }

    @Test
    public void testReadNamedCharacterReferenceWithMultipleCharacters() {
        when(reader.current()).thenReturn('#');
        when(reader.consume()).thenReturn('A');
        when(reader.consume()).thenReturn('M');
        when(reader.consume()).thenReturn('p');
        when(reader.consume()).thenReturn(';');
        int[] codepoints = tokeniser.consumeCharacterReference(null, true);
        assertArrayEquals(new int[]{0x41, 0x6D, 0x65}, codepoints);
        verify(reader).consume(5);
    }

    @Test
    public void testEmitValidTag() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("div", token.tagName());
        verify(reader).mark();
        verify(reader).consumeLetterThenDigitSequence();
        verify(reader).matchConsume('>');
    }

    @Test
    public void testEmitValidComment() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals("<!--", token.charSequence());
        verify(reader).mark();
        verify(reader).consumeLetterThenDigitSequence();
        verify(reader).consume('>');
    }

    @Test
    public void testEmitDoctype() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.type);
        assertEquals("<!doctype html", token.data());
        verify(reader).mark();
        verify(reader).consumeLetterThenDigitSequence();
        verify(reader).consume('>');
    }

    @Test
    public void testTransitionToErrorState() {
        tokeniser.transition(TokeniserState.Error);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Error, token.type);
    }

    @Test
    public void testAdvanceTransitionToErrorState() {
        tokeniser.advanceTransition(TokeniserState.Error);
        tokeniser.reader.advance();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Error, token.type);
    }
}