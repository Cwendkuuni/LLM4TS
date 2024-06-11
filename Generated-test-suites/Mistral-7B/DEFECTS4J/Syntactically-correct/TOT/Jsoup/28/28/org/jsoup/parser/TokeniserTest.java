import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        reader = mock(CharacterReader.class);
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_SimpleCharacter() {
        when(reader.current()).thenReturn('a');
        when(reader.peek()).thenReturn('a');
        when(reader.isEndOfStream()).thenReturn(false);

        Token token = tokeniser.read();
        assertEquals(TokenType.Character, token.type);
        assertEquals('a', token.data);

        reader.reset();
        tokeniser.emit(token);
        assertEquals(TokenType.Character, tokeniser.getState().getExpectedTokenType());
    }

    @Test
    public void testRead_SelfClosingTag() {
        when(reader.current()).thenReturn('<');
        when(reader.peek()).thenReturn('/');
        when(reader.peek(1)).thenReturn('>');
        when(reader.isEndOfStream()).thenReturn(false);

        Token token = tokeniser.read();
        assertEquals(TokenType.EndTag, token.type);
        assertNull(token.data);
        assertEquals("html", token.tagName);

        reader.reset();
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.emit(token);
        assertEquals(TokenType.Character, tokeniser.getState().getExpectedTokenType());
    }

    @Test
    public void testRead_CharacterReference() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("&nbsp;".getBytes());
        when(reader.reader()).thenReturn(inputStream);

        Token token = tokeniser.read();
        assertEquals(TokenType.Character, token.type);
        assertEquals('\u00A0', token.data);

        reader.reset();
        tokeniser.emit(token);
        assertEquals(TokenType.Character, tokeniser.getState().getExpectedTokenType());
    }

    @Test
    public void testRead_NamedCharacterReference() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("&copy;".getBytes());
        when(reader.reader()).thenReturn(inputStream);

        Token token = tokeniser.read();
        assertEquals(TokenType.Character, token.type);
        assertEquals('\u00A9', token.data);

        reader.reset();
        tokeniser.emit(token);
        assertEquals(TokenType.Character, tokeniser.getState().getExpectedTokenType());
    }

    @Test(expected = AssertionError.class)
    public void testRead_InvalidCharacterReference() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("&\u00FF;".getBytes());
        when(reader.reader()).thenReturn(inputStream);

        tokeniser.read();
    }

    @Test
    public void testRead_SelfClosingTagWithAttribute() {
        when(reader.current()).thenReturn('<');
        when(reader.peek()).thenReturn('/');
        when(reader.peek(1)).thenReturn('>');
        when(reader.peek(2)).thenReturn(' ');
        when(reader.peek(3)).thenReturn('=');
        when(reader.peek(4)).thenReturn('a');
        when(reader.peek(5)).thenReturn('>');
        when(reader.isEndOfStream()).thenReturn(false);

        tokeniser.read();
        tokeniser.consumeCharacterReference(null, true);
        tokeniser.acknowledgeSelfClosingFlag();

        Token token = tokeniser.read();
        assertEquals(TokenType.EndTag, token.type);
        assertNull(token.data);
        assertEquals("html", token.tagName);
    }

    @Test
    public void testRead_IncompleteTag() {
        when(reader.current()).thenReturn('<');
        when(reader.peek()).thenReturn('/');
        when(reader.isEndOfStream()).thenReturn(false);

        tokeniser.read();
        tokeniser.error(TokeniserState.TagName);

        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testRead_MissMatchedEndTag() {
        when(reader.current()).thenReturn('<');
        when(reader.peek()).thenReturn('/');
        when(reader.peek(1)).thenReturn('b');
        when(reader.isEndOfStream()).thenReturn(false);

        tokeniser.read();
        tokeniser.read();
        tokeniser.error(TokeniserState.TagName);

        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testRead_EOF() {
        when(reader.isEndOfStream()).thenReturn(true);

        tokeniser.read();
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.eofError(TokeniserState.Data);
    }

    @Test
    public void testRead_InvalidStateTransition() {
        TokeniserState state = TokeniserState.EndTagName;
        tokeniser.transition(state);

        tokeniser.error(state);
    }

    @Test
    public void testRead_AppropriateEndTag() {
        Token token = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Token expectedToken = new Token.EndTag();
        expectedToken.tagName = token.tagName;

        assertEquals(expectedToken, tokeniser.read());
    }
}