import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LexerTest {

    private ExtendedBufferedReader mockReader;
    private CSVFormat format;

    @Before
    public void setup() {
        format = new CSVFormatBuilder()
                .setDelimiter(';')
                .setIgnoreSurroundingSpaces(true)
                .setIgnoreEmptyLines(false)
                .build();
        mockReader = Mockito.mock(ExtendedBufferedReader.class);
    }

    @Test
    public void testNextToken_emptyLine() throws IOException {
        Mockito.when(mockReader.getLastChar()).thenReturn((char) 0);
        Mockito.when(mockReader.read()).thenReturn(-1);

        Lexer lexer = new Lexer(format, mockReader);
        Token token = new Token();

        lexer.nextToken(token);

        assertNotNull(token);
        assertEquals(TokenType.EOF, token.getType());
    }

    @Test
    public void testNextToken_singleCharacter() throws IOException {
        Mockito.when(mockReader.getLastChar()).thenReturn('A');
        Mockito.when(mockReader.read()).thenReturn('A');

        Lexer lexer = new Lexer(format, mockReader);
        Token token = new Token();

        lexer.nextToken(token);

        assertNotNull(token);
        assertEquals('A', token.getValue()[0]);
        assertEquals(TokenType.STRING, token.getType());
    }

    @Test
    public void testNextToken_stringLiteral() throws IOException {
        String input = "\"Hello, world!\"";
        Mockito.when(mockReader.getLastChar()).thenReturn('"');
        Mockito.when(mockReader.readBuffer(Mockito.anyInt())).thenReturn(input);

        Lexer lexer = new Lexer(format, mockitoMockitoMockitoMockitoMockito.spy(mockReader));
        Token token = new Token();

        lexer.nextToken(token);

        assertNotNull(token);
        assertEquals("Hello, world!", token.getValue());
        assertEquals(TokenType.STRING, token.getType());
    }

    @Test(expected = IOException.class)
    public void testNextToken_invalidEscapeSequence() throws IOException {
        Mockito.when(mockReader.getLastChar()).thenReturn('\\');
        Mockito.when(mockReader.read()).thenReturn('X');

        Lexer lexer = new Lexer(format, mockReader);
        Token token = new Token();

        lexer.nextToken(token);

        fail("Expected IOException but got: " + token);
    }

    @Test(expected = IOException.class)
    public void testNextToken_endOfStream() throws IOException {
        Mockito.when(mockReader.getLastChar()).thenReturn(0);
        Mockito.when(mockReader.read()).thenReturn(-1);

        Lexer lexer = new Lexer(format, mockReader);
        Token token = new Token();

        lexer.nextToken(token);

        fail("Expected IOException but got: " + token);
    }
}