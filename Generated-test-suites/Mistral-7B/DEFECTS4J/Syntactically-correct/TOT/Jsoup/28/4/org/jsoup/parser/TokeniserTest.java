import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        reader = Mockito.mock(CharacterReader.class);
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testReadEmptyInput() {
        // empty input test
        Mockito.when(reader.peek()).thenReturn((char) 0);
        Mockito.when(reader.read()).thenReturn((char) 0);

        Token token = tokeniser.read();
        assertNull(token);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadSingleCharacter() {
        // single character test
        Mockito.when(reader.peek()).thenReturn((char) 'a');
        Mockito.when(reader.read()).thenReturn((char) 'a');

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.getData());
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadStartTag() {
        // start tag test
        String input = "<html>";
        Mockito.when(reader.peekMultiple(input.length())).thenReturn(input.toCharArray());

        tokeniser.read(); // consume <
        Token startTag = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertEquals("html", startTag.tagName);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.getLineNumber());
        assertEquals(1, error.getColumnNumber());
        assertEquals("Unexpected character 'h' in state [Data]", error.getMessage());
        // reset errors for next test
        errors.clear();
    }

    @Test
    public void testReadEndTag() {
        // end tag test
        String input = "</html>";
        Mockito.when(reader.peekMultiple(input.length())).thenReturn(input.toCharArray());

        tokeniser.read(); // consume <
        tokeniser.read(); // consume /
        Token endTag = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, endTag.type);
        assertEquals("html", endTag.tagName);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadSelfClosingTag() {
        // self closing tag test
        String input = "<img src='example.jpg'/>";
        Mockito.when(reader.peekMultiple(input.length())).thenReturn(input.toCharArray());

        tokeniser.read(); // consume <
        tokeniser.read(); // consume img
        tokeniser.transition(TokeniserState.SelfClosingStartTag);
        tokeniser.consumeCharacterReference(null, false); // consume src
        tokeniser.consumeCharacterReference(null, true); // consume />

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadString() {
        // string test
        String input = "Hello, World!";
        Mockito.when(reader.peekMultiple(input.length())).thenReturn(input.toCharArray());

        tokeniser.read(); // consume <
        tokeniser.emit("data");
        tokeniser.read(); // consume "
        tokeniser.emitString(input);
        tokeniser.read(); // consume >

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals(0, errors.size());
    }

    @Test
    public void testReadError() {
        // error test
        String input = "<script";
        Mockito.when(reader.peekMultiple(input.length())).thenReturn(input.toCharArray());

        tokeniser.read(); // consume <
        tokeniser.read(); // consume s
        tokeniser.transition(TokeniserState.Script);
        tokeniser.error(TokeniserState.Script);

        Token token = tokeniser.read();
        assertNull(token);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.getLineNumber());
        assertEquals(3, error.getColumnNumber());
        assertEquals("Unexpected character 'c' in input state [Script]", error.getMessage());
        // reset errors for next test
        errors.clear();
    }

    @Test
    public void testReadEof() {
        // eof test
        String input = "<html>";
        Mockito.when(reader.peekMultiple(input.length())).thenReturn(input.toCharArray());

        tokeniser.read(); // consume <
        tokeniser.read(); // consume h
        tokeniser.read(); // consume t
        tokeniser.read(); // consume m
        tokeniser.read(); // consume l

        Mockito.when(reader.peek()).thenReturn((char) 0);

        Token token = tokeniser.read();
        assertNull(token);
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.getLineNumber());
        assertEquals(6, error.getColumnNumber());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", error.getMessage());
        // reset errors for next test
        errors.clear();
    }
}