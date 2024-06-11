import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
    public void setUp() {
        format = new CSVFormatBuilder()
                .setDelimiter(';')
                .setHeaderSeparator('=')
                .setIgnoreEmptyLines(true)
                .build();
        mockReader = Mockito.mock(ExtendedBufferedReader.class);
        lexer = new Lexer(format, mockReader);
    }

    @Test
    public void testNextToken_emptyLine() throws IOException {
        Mockito.when(mockReader.readLine()).thenReturn(null);
        Token token = lexer.nextToken(Token.NULL);
        assertEquals(TokenType.EOF, token.getType());
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
    }

    @Test
    public void testNextToken_comment() throws IOException {
        String testString = "a;b# This is a comment;c\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Mockito.when(mockReader.readLine()).then(line -> inputStream.readAllBytes());
        Mockito.when(mockReader.getLastChar()).thenReturn((char) 0);

        Token token = lexer.nextToken(Token.NULL);
        assertEquals(TokenType.COMMENT, token.getType());
        assertEquals(" This is a comment", token.getValue());
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
        Mockito.verify(mockReader, Mockito.times(1)).getLastChar();

        token = lexer.nextToken(Token.NULL);
        assertEquals(TokenType.FIELD, token.getType());
        assertEquals("c", token.getValue());
    }

    @Test
    public void testNextToken_delimiter_ignored() throws IOException {
        String testString = "a;b;c\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Mockito.when(mockReader.readLine()).then(line -> inputStream.readAllBytes());
        Mockito.when(mockReader.getLastChar()).thenReturn((char) 0);

        Token token = lexer.nextToken(Token.NULL);
        assertEquals(TokenType.FIELD, token.getType());
        assertEquals("a", token.getValue());
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
        Mockito.verify(mockReader, Mockito.times(1)).getLastChar();

        token = lexer.nextToken(token);
        assertEquals(TokenType.FIELD, token.getType());
        assertEquals("b", token.getValue());
        Mockito.verify(mockReader, Mockito.times(2)).readLine();
        Mockito.verify(mockReader, Mockito.times(2)).getLastChar();

        token = lexer.nextToken(token);
        assertEquals(TokenType.FIELD, token.getType());
        assertEquals("c", token.getValue());
        Mockito.verify(mockReader, Mockito.times(3)).readLine();
        Mockito.verify(mockReader, Mockito.times(3)).getLastChar();

        token = lexer.nextToken(Token.NULL);
        assertEquals(TokenType.EOF, token.getType());
    }

    @Test
    public void testNextToken_emptyField() throws IOException {
        String testString = "a;;\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Mockito.when(mockReader.readLine()).then(line -> inputStream.readAllBytes());
        Mockito.when(mockReader.getLastChar()).thenReturn((char) 0);

        Token token = lexer.nextToken(Token.NULL);
        assertEquals(TokenType.FIELD, token.getType());
        assertNull(token.getValue());
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
        Mockito.verify(mockReader, Mockito.times(1)).getLastChar();

        token = lexer.nextToken(token);
        assertEquals(TokenType.EOF, token.getType());
    }

    @Test
    public void testNextToken_quotedField() throws IOException {
        String testString = "a;\"This is a quoted field\"\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Mockito.when(mockReader.readLine()).then(line -> inputStream.readAllBytes());
        Mockito.when(mockReader.getLastChar()).thenReturn((char) 0);

        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        Token token = lexer.nextToken(Token.NULL);
        assertEquals(TokenType.FIELD, token.getType());
        assertEquals("This is a quoted field", token.getValue());
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
        Mockito.verify(mockReader, Mockito.times(1)).getLastChar();
        Mockito.verify(mockReader, Mockito.times(1)).skip(3);

        Mockito.verify(mockReader, Mockito.atLeastOnce()).getLastChar();
        Mockito.verify(mockReader, Mockito.never()).read();

        token = lexer.nextToken(Token.NULL);
        assertEquals(TokenType.EOF, token.getType());
    }

    @Test
    public void testNextToken_escapedField() throws IOException {
        String testString = "a;\"This\\ is a\\ escaped field\\\"\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Mockito.when(mockReader.readLine()).then(line -> inputStream.readAllBytes());
        Mockito.when(mockReader.getLastChar()).thenReturn((char) 0);

        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        Token token = lexer.nextToken(Token.NULL);
        assertEquals(TokenType.FIELD, token.getType());
        assertEquals("This is a escaped field", token.getValue());
        Mockito.verify(mockReader, Mockito.times(1)).readLine();
        Mockito.verify(mockReader, Mockito.times(1)).getLastChar();
        Mockito.verify(mockReader, Mockito.times(1)).read();
        Mockito.verify(mockReader, Mockito.times(3)).getLastChar();
        Mockito.verify(mockReader, Mockito.times(1)).skip(4);

        Mockito.verify(mockReader, Mockito.atLeastOnce()).getLastChar();
        Mockito.verify(mockReader, Mockito.never()).read(Mockito.anyInt());

        token = lexer.nextToken(Token.NULL);
        assertEquals(TokenType.EOF, token.getType());
    }

    @Test
    public void testNextToken_ignoresSurroundingSpaces() throws IOException {
        String testString = "a;\t This is a field \t\t\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Mockito.when(mockReader.readLine()).then(line -> inputStream.readAllBytes());
        Mockito.when(mockReader.getLastChar()).thenReturn((char) 0);

        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        Token token = lexer.nextToken(Token.NULL);
        assertEquals(TokenType.FIELD, token.getType());
        assertEquals("This is a field", token.getValue());
        Mockito.verify(mockreader, Mockito.times(1)).readLine();
        Mockito.verify(mockreader, Mockito.times(1)).getLastChar();
        Mockito.verify(mockreader, Mockito.times(1)).read();
        Mockito.verify(mockreader, Mockito.times(2)).getLastChar();
        Mockito.verify(mockreader, Mockito.times(1)).skip(3);

        Mockito.verify(mockreader, Mockito.atLeastOnce()).getLastChar();
        Mockito.verify(mockreader, Mockito.never()).read(Mockito.anyInt());

        token = lexer.nextToken(Token.NULL);
        assertEquals(TokenType.EOF, token.getType());
    }

    @Test
    public void testNextToken__ignoresEmptyLines() throws IOException {
        String testString = "a;\n\nThis is a field\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Mockito.when(mockReader.readLine()).then(line -> inputStream.readAllBytes());
        Mockito.when(mockReader.getLastChar()).thenReturn((char) 0);

        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        Token token = lexer.nextToken(Token.NULL);
        assertEquals(TokenType.FIELD, token.getType());
        assertEquals("This is a field", token.getValue());
        Mockito.verify(mockreader, Mockito.times(1)).readLine();
        Mockito.verify(mockreader, Mockito.times(1)).getLastChar();
        Mockito.verify(mockreader, Mockito.times(1)).skip(2);

        Mockito.verifyNoMoreInteractions(mockreader);
    }

    @Test
    public void testReadEscape_readsEscapeSequence() throws IOException {
        Mockito.when(mockReader.read()).thenReturn((int) LEF, CR);

        int escape = lexer.readEscape();
        assertEquals(CR, escape);
        Mockito.verify(mockReader, Mockito.times(1)).read();
        Mockito.verify(mockReader, Mockito.times(1)).read();

        escape = lexer.readEscape();
        assertEquals(LF, escape);
        Mockito.verify(mockreader, Mockito.times(1)).read();
        Mockito.verify(mockreader, Mockito.times(1)).read();

        escape = lexer.readEscape();
        assertEquals(UNDEFINED, escape);
        Mockito.verify(mockreader, Mockito.times(1)).read();
        Mockito.verify(mockreader, Mockito.times(1)).read();
    }

    @Test(expected = IOException.class)
    public void testReadEscape_throwsIOException() throws IOException {
        Mockito.when(mockReader.read()).thenReturn(END_OF_STREAM);

        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        StringBuilder testStringBuilder = new StringBuilder(" a\t  b  \t  ");
        String testString = testStringBuilder.toString();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        Mockito.when(mockReader.readLine()).then(line -> inputStream.readAllBytes());
        Mockito.when(mockReader.getLastChar()).thenReturn((char) 0);

        StringBuilder result = new StringBuilder();
        lexer.nextToken(Token.NULL);
        lexer.nextToken(Token.NULL);
        lexer.nextToken(Token.NULL);

        lexer.trimTrailingSpaces(result);
        assertEquals("a ", result.toString());

        testStringBuilder.setLength(testString.length() + 10);
        testString = testStringBuilder.toString();
        inputStream = new ByteArrayInputStream(testString.getBytes());
        lexer.nextToken(Token.NULL);
        lexer.nextToken(Token.NULL);
        lexer.nextToken(Token.NULL);

        result = new StringBuilder();
        lexer.trimTrailingSpaces(result);
        assertTrue(result.toString().isEmpty());

        testStringBuilder.setLength(0);
        testString = testStringBuilder.toString();
        inputStream = new ByteArrayInputStream(testString.getBytes());
        lexer.nextToken(Token.NULL);
        lexer.nextToken(Token.NULL);
        lexer.nextToken(Token.NULL);

        result = new StringBuilder();
        lexer.trimTrailingSpaces(result);
        assertEquals(UNDEFINED, result.length());
    }
}