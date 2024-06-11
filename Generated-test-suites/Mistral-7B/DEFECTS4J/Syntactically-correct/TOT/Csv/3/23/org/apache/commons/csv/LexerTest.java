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

import static org.apache.commons.csv.Constants.*;
import static org.junit.Assert.*;

public class LexerTest {

    private CSVFormat format;
    private ExtendedBufferedReader mockReader;
    private Lexer lexer;

    @Before
    public void setup() {
        format = new CSVFormat();
        mockReader = Mockito.mock(ExtendedBufferedReader.class);
        lexer = new Lexer(format, mockReader);
    }

    @Test
    public void testNextToken_simpleCase() throws IOException {
        // Given: a simple case with no escape or quote chars
        String input = "col1,col2,col3";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Mockito.when(mockReader.readLine()).thenReturn(input);
        Mockito.when(mockReader.read()).thenReturn(input.codePointAt(0));

        // When: next token is read
        Token token = lexer.nextToken(Token.newInstance());

        // Then: the expected token is returned
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).readLine();

        Token expectedToken = new Token(TokenType.DATA, "col1");
        assertEquals(expectedToken, token.getValue());
        assertEquals(1, token.getColumnNumber());
        assertEquals(1, token.getLineNumber());

        // Cleanup
        Mockito.reset(mockReader);
    }

    @Test
    public void testNextToken_escape_case() throws IOException {
        // Given: a case with an escape character
        String input = "col1,\"col2,col3\",col4";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Mockito.when(mockReader.readLine()).thenReturn(input);
        Mockito.when(mockReader.read()).thenReturn('\\').thenReturn('r').thenReturn(CR);

        // When: next token is read
        Token token = lexer.nextToken(Token.newInstance());

        // Then: the expected token is returned
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).readLine();

        Token expectedToken = new Token(TokenType.DATA, "col1");
        assertEquals(expectedToken, token.getValue());
        assertEquals(1, token.getColumnNumber());
        assertEquals(1, token.getLineNumber());

        // Cleanup
        Mockito.reset(mockReader);
    }

    @Test(expected = IOException.class)
    public void testNextToken_end_of_stream_case() throws IOException {
        // Given: a case when the end of stream is reached
        Mockito.when(mockReader.read()).thenReturn(END_OF_STREAM);

        // When: next token is read
        lexer.nextToken(Token.newInstance());

        // Then: an IOException is thrown
    }

    @Test
    public void testNextToken_comment_case() throws IOException {
        // Given: a case with a comment
        String input = "col1,\"col2,col3\",# this is a comment,col4";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Mockito.when(mockReader.readLine()).thenReturn(input);
        Mockito.when(mockReader.read()).thenReturn(input.codePointAt(0));
        Mockito.when(mockReader.read()).thenReturn(format.getCommentStart());

        // When: next token is read
        Token token = lexer.nextToken(Token.newInstance());

        // Then: the comment is skipped and the next token is returned
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockreader).read();
        Mockito.verify(mockReader).readLine();

        Token expectedToken = new Token(TokenType.DATA, "col2");
        assertEquals(expectedToken, token.getValue());
        assertEquals(1, token.getColumnNumber());
        assertEquals(1, token.getLineNumber());

        // Cleanup
        Mockito.reset(mockReader);
    }

    @Test
    public void testNextToken_quoted_string_case() throws IOException {
        // Given: a case with a quoted string
        String input = "col1,\"col2,\"col3\",col4";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Mockito.when(mockReader.readLine()).thenReturn(input);
        Mockito.when(mockReader.read()).thenReturn('"').thenReturn('c').thenReturn('o').thenReturn('l').thenReturn('2')
                .thenReturn(',').thenReturn('"').thenReturn('c').thenReturn('o').thenReturn('l').thenReturn('3')
                .thenReturn('"').thenReturn(CR);

        // When: next token is read
        Token token = lexer.nextToken(Token.newInstance());

        // Then: the expected token is returned
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).readLine();

        Token expectedToken = new Token(TokenType.DATA, "col2,\"col3\"");
        assertEquals(expectedToken, token.getValue());
        assertEquals(1, token.getColumnNumber());
        assertEquals(1, token.getLineNumber());

        // Cleanup
        Mockito.reset(mockReader);
    }

    @Test
    public void testNextToken_empty_line_case() throws IOException {
        // Given: a case with an empty line
        String input = "";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Mockito.when(mockReader.readLine()).thenReturn(input);

        // When: next token is read
        Token token = lexer.nextToken(Token.newInstance());

        // Then: the current position is moved to the next line and no token is returned
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
        Mockito.verify(mockReader).readLine();
        Mockito.verifyNoMoreInteractions(mockReader);

        // Cleanup
        Mockito.reset(mockReader);
    }

    @Test
    public void testNextToken_ignored_spaces_case() throws IOException {
        // Given: a case with spaces
        String input = " col1,\t col2,\r col3,\n col4";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Mockito.when(mockReader.readLine()).thenReturn(input);
        Mockito.when(mockReader.read()).thenReturn(' ').thenReturn('\t').thenReturn('\r').thenReturn('\n');

        // When: next token is read
        Token token = lexer.nextToken(Token.newInstance());

        // Then: the expected token is returned with ignored spaces
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).read();
        Mockito.verify(mockReader).readLine();

        Token expectedToken = new Token(TokenType.DATA, "col1");
        assertEquals(expectedToken, token.getValue());
        assertEquals(1, token.getColumnNumber());
        assertEquals(1, token.getLineNumber());

        // Cleanup
        Mockito.reset(mockReader);
    }
}