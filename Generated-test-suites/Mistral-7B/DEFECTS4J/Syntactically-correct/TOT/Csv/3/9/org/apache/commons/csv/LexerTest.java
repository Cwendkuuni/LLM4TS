import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LexerTest {

    private CSVFormat format;
    private ExtendedBufferedReader mockReader;
    private Lexer lexer;

    @Before
    public void setup() throws IOException {
        format = new CSVFormatBuilder()
                .setDelimiter(';')
                .setEscape('\\')
                .setQuoteChar('"')
                .setCommentPrefix("#")
                .build();

        mockReader = Mockito.mock(ExtendedBufferedReader.class);
        lexer = new Lexer(format, mockReader);
    }

    @Test
    public void testNextToken_emptyLine() throws IOException {
        Mockito.when(mockReader.read()).thenReturn(UNDEFINED);
        Mockito.when(mockReader.readLine()).thenReturn(null);

        Token token = lexer.nextToken(null);

        assertNull(token);
        assertTrue(lexer.isEndOfFile(Mockito.mock(Integer.TYPE.class).getName()));
    }

    @Test
    public void testNextToken_whitespace() throws IOException {
        Mockito.when(mockReader.read()).thenReturn(' ');
        Mockito.when(mockReader.read()).thenReturn(' ');

        StringBuilder buffer = new StringBuilder();
        Token token = lexer.nextToken(null);

        assertNull(token.getValue());
        assertEquals(Token.TokenType.WHITESPACE, token.getType());

        lexer.trimTrailingSpaces(buffer);
        assertEquals("", buffer.toString());
    }

    @Test
    public void testNextToken_delimiter() throws IOException {
        Mockito.when(mockReader.read()).thenReturn('a');
        Mockito.when(mockReader.read()).thenReturn(';');

        Token token = lexer.nextToken(null);

        assertEquals("", token.getValue());
        assertEquals(Token.TokenType.DELIMITER, token.getType());
    }

    @Test
    public void testNextToken_escapedChar() throws IOException {
        Mockito.when(mockReader.read()).thenReturn('\\');
        Mockito.when(mockReader.read()).thenReturn('n');

        Token token = lexer.nextToken(null);

        assertEquals("\n", token.getValue());
        assertEquals(Token.TokenType.ESCAPED, token.getType());
    }

    @Test
    public void testNextToken_comment() throws IOException {
        Mockito.when(mockReader.read()).thenReturn('#');
        Mockito.when(mockReader.read()).thenReturn('#');

        Token token = lexer.nextToken(null);

        assertNull(token.getValue());
        assertEquals(Token.TokenType.COMMENT, token.getType());
    }

    @Test
    public void testNextToken_quotedString() throws IOException {
        Mockito.when(mockReader.read()).thenReturn('"');
        Mockito.when(mockReader.read()).thenReturn('a');
        Mockito.when(mockReader.read()).thenReturn('"');

        Token token = lexer.nextToken(null);

        assertEquals("a", token.getValue());
        assertEquals(Token.TokenType.STRING, token.getType());
    }

    @Test
    public void testNextToken_invalidChar() throws IOException {
        Mockito.when(mockReader.read()).thenReturn('x');

        Token token = lexer.nextToken(null);

        assertNull(token.getValue());
        assertFalse(lexer.isEndOfFile(Mockito.mock(Integer.TYPE.class).getName()));
    }
}