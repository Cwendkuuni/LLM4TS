import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

public class LexerTest {

    private CSVFormat format;
    private ExtendedBufferedReader reader;
    private Lexer lexer;

    @Before
    public void setUp() {
        format = new CSVFormat(',', '\u0000', '\u0000', '\u0000');
        reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
        lexer = new Lexer(format, reader);
    }

    @Test
    public void testNextToken_EmptyInput() throws IOException {
        // Test nextToken() with an empty input stream
        Token token = lexer.nextToken(null);
        assertNull(token);
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testNextToken_SimpleText() throws IOException {
        // Test nextToken() with a simple text input: "Hello, World!"
        String input = "Hello, World!";
        reader.init(new ByteArrayInputStream(input.getBytes()), null);
        lexer = new Lexer(format, reader);

        Token token = null;
        StringBuilder expected = new StringBuilder();

        // Test nextToken() multiple times
        while ((token = lexer.nextToken(token)) != null) {
            expected.append(token.getValue());
            expected.append(lexer.isEndOfLine(token.getType()) ? '\n' : ' ');
            token = lexer.nextToken(token);
        }

        String actual = new StringBuilder().append(lexer.getLineNumber())
                .append(": '")
                .append(expected.toString())
                .append("'\n")
                .toString();

        // Test the resulting string against the expected output
        assertEquals(actual, reader.getText());
    }

    @Test
    public void testNextToken_SimpleTextWithEscapes() throws IOException {
        // Test nextToken() with a simple text input: "Hello,\"World!\nBackslash:\\Backslash"
        String input = "Hello,\"World!\nBackslash:\\Backslash";
        reader.init(new ByteArrayInputStream(input.getBytes()), null);
        lexer = new Lexer(format, reader);

        Token token = null;
        StringBuilder expected = new StringBuilder();

        // Test nextToken() multiple times
        while ((token = lexer.nextToken(token)) != null) {
            expected.append(token.getValue());
            expected.append(lexer.isEndOfLine(token.getType()) ? '\n' : ' ');
            token = lexer.nextToken(token);
        }

        String actual = new StringBuilder().append(lexer.getLineNumber())
                .append(": '")
                .append(expected.toString())
                .append("'\n")
                .toString();

        // Test the resulting string against the expected output
        assertEquals(actual, reader.getText());
    }

    @Test(expected = IOException.class)
    public void testNextToken_InvalidInput() throws IOException {
        // Test nextToken() with an invalid input: "Hello,\"World!" (missing terminating quote)
        String input = "Hello,\"World";
        reader.init(new ByteArrayInputStream(input.getBytes()), null);
        lexer = new Lexer(format, reader);

        Token token = null;
        // Test nextToken() multiple times
        while ((token = lexer.nextToken(token)) != null) {
            // should not reach here
        }

        // Test the method should throw an IOException
        lexer.nextToken(token);
    }

    @Test
    public void testNextToken_EmptyLine() throws IOException {
        // Test nextToken() with an empty line
        String input = "\n";
        reader.init(new ByteArrayInputStream(input.getBytes()), null);
        lexer = new Lexer(format, reader);

        Token token = lexer.nextToken(null);
        assertEquals(Token.TYPE_EOF, token.getType());
        assertEquals("", token.getValue());

        token = lexer.nextToken(token);
        assertNull(token);
    }

    @Test
    public void testNextToken_Comment() throws IOException {
        // Test nextToken() with a comment input: "// This is a comment"
        String input = "// This is a comment";
        reader.init(new ByteArrayInputStream(input.getBytes()), null);
        lexer = new Lexer(format, reader);

        Token token = null;
        StringBuilder expected = new StringBuilder();

        // Test nextToken() multiple times
        while ((token = lexer.nextToken(token)) != null) {
            if (token.isComment()) {
                // Test comment handling
                assertEquals(Token.TYPE_COMMENT, token.getType());
                assertEquals("This is a comment", token.getValue());
            } else {
                expected.append(token.getValue());
                expected.append(lexer.isEndOfLine(token.getType()) ? '\n' : ' ');
            }

            token = lexer.nextToken(token);
        }

        String actual = new StringBuilder().append(lexer.getLineNumber())
                .append(": '")
                .append(expected.toString())
                .append("'\n")
                .toString();

        // Test the resulting string against the expected output
        assertEquals(actual, reader.getText());
    }

    @Test
    public void testNextToken_Delimiter() throws IOException {
        // Test nextToken() with a delimiter input: "1, 2, 3"
        String input = "1, 2, 3";
        reader.init(new ByteArrayInputStream(input.getBytes()), null);
        lexer = new Lexer(format, reader);

        Token token = null;
        StringBuilder expected = new StringBuilder();

        // Test nextToken() multiple times
        while ((token = lexer.nextToken(token)) != null) {
            if (token.isDelimiter()) {
                // Test delimiter handling
                assertEquals(Token.TYPE_DELIMITER, token.getType());
                assertEquals(",", token.getValue());
            } else {
                expected.append(token.getValue());
                expected.append(lexer.isEndOfLine(token.getType()) ? '\n' : ' ');
            }

            token = lexer.nextToken(token);
        }

        String actual = new StringBuilder().append(lexer.getLineNumber())
                .append(": '")
                .append(expected.toString())
                .append("'\n")
                .toString();

        // Test the resulting string against the expected output
        assertEquals(actual, reader.getText());
    }

    @Test
    public void testNextToken_QuotedString() throws IOException {
        // Test nextToken() with a quoted string input: "\"Hello,\"World!\""
        String input = "\"Hello,\"World!\"";
        reader.init(new ByteArrayInputStream(input.getBytes()), null);
        lexer = new Lexer(format, reader);

        Token token = null;
        StringBuilder expected = new StringBuilder();

        // Test nextToken() multiple times
        while ((token = lexer.nextToken(token)) != null) {
            if (token.isQuotedString()) {
                // Test quoted string handling
                assertEquals(Token.TYPE_QUOTED_STRING, token.getType());
                assertEquals("Hello,\"World!", token.getValue());
            } else {
                expected.append(token.getValue());
                expected.append(lexer.isEndOfLine(token.getType()) ? '\n' : ' ');
            }

            token = lexer.nextToken(token);
        }

        String actual = new StringBuilder().append(lexer.getLineNumber())
                .append(": '")
                .append(expected.toString())
                .append("'\n")
                .toString();

        // Test the resulting string against the expected output
        assertEquals(actual, reader.getText());
    }

    @Test
    public void testNextToken_MultipleTypes() throws IOException {
        // Test nextToken() with a complex input: "1, \"Hello, World!\", 3, // This is a comment"
        String input = "1, \"Hello, World!\", 3, // This is a comment";
        reader.init(new ByteArrayInputStream(input.getBytes()), null);
        lexer = new Lexer(format, reader);

        Token token = null;
        StringBuilder expected = new StringBuilder();

        // Test nextToken() multiple times
        while ((token = lexer.nextToken(token)) != null) {
            if (token.isNumber()) {
                // Test number handling
                assertEquals(Token.TYPE_NUMBER, token.getType());
                assertEquals("1", token.getValue());
                expected.append("1");
            } else if (token.isQuotedString()) {
                // Test quoted string handling
                assertEquals(Token.TYPE_QUOTED_STRING, token.getType());
                assertEquals("Hello, World!", token.getValue());
                expected.append(", '");
                expected.append("Hello, World!");
                expected.append("', ");
            } else if (token.isDelimiter()) {
                // Test delimiter handling
                assertEquals(Token.TYPE_DELIMITER, token.getType());
                assertEquals(",", token.getValue());
                expected.append(", ");
            } else if (token.isComment()) {
                // Test comment handling
                assertEquals(Token.TYPE_COMMENT, token.getType());
                assertEquals("This is a comment", token.getValue());
                expected.append("// This is a comment");
            } else {
                assertEquals(Token.TYPE_EOF, token.getType());
                break;
            }

            token = lexer.nextToken(token);
        }

        String actual = new StringBuilder().append(lexer.getLineNumber())
                .append(": '")
                .append(expected.toString())
                .append("'\n")
                .toString();

        // Test the resulting string against the expected output
        assertEquals(actual, reader.getText());
    }

    @Test
    public void testNextToken_EndOfStream() throws IOException {
        // Test nextToken() with an end-of-stream input
        String input = "";
        reader.init(new ByteArrayInputStream(new byte[0]), null);
        lexer = new Lexer(format, reader);

        Token token = null;
        assertEquals(Token.TYPE_EOF, lexer.nextToken(token).getType());
        assertEquals(input, reader.getText());
    }
}