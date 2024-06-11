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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LexerTest {

    private CSVFormat format;
    private ExtendedBufferedReader in;
    private Lexer lexer;

    @Before
    public void setup() {
        format = new CSVFormatBuilder()
                .withDelimiter(';')
                .withHeader("Header1", "Header2")
                .build();
        in = new ExtendedBufferedReader(new StringReader(""));
        lexer = new Lexer(format, in);
    }

    @Test
    public void testNextToken_empty() throws IOException {
        Token token = lexer.nextToken(null);
        assertNull(token);
        assertEquals(UNDEFINED, lexer.getLineNumber());
    }

    @Test
    public void testNextToken_whitespace() throws IOException {
        in.readLine();
        in.append(" \t\n");
        Token token = lexer.nextToken(null);
        assertTrue(token.getType() == Token.Type.WHITESPACE);
        assertEquals(" ", token.getText());
    }

    @Test
    public void testNextToken_delimiter() throws IOException {
        in.readLine();
        in.append("header1;header2");
        Token token = lexer.nextToken(null);
        assertTrue(token.getType() == Token.Type.FIELD);
        assertEquals("header1", token.getText());

        token = lexer.nextToken(null);
        assertTrue(token.getType() == Token.Type.FIELD);
        assertEquals("header2", token.getText());

        token = lexer.nextToken(null);
        assertNull(token);
    }

    @Test
    public void testNextToken_emptyLine() throws IOException {
        in.readLine();
        in.append("\n");
        Token token = lexer.nextToken(null);
        assertNull(token);
        assertEquals(UNDEFINED, lexer.getLineNumber());
    }

    @Test
    public void testNextToken_comment() throws IOException {
        in.readLine();
        in.append("// this is a comment");
        Token token = lexer.nextToken(null);
        assertNull(token);
        assertEquals(UNDEFINED, lexer.getLineNumber());

        token = lexer.nextToken(null);
        assertNull(token);
    }

    @Test
    public void testNextToken_escape() throws IOException {
        in.readLine();
        in.append("\"field1\\rfield2\\\\field3\"");
        Token token = lexer.nextToken(null);
        assertTrue(token.getType() == Token.Type.FIELD);
        assertEquals("field1", token.getText());

        token = lexer.nextToken(null);
        assertTrue(token.getType() == Token.Type.FIELD);
        assertEquals("field2", token.getText());

        token = lexer.nextToken(null);
        assertTrue(token.getType() == Token.Type.FIELD);
        assertEquals("field3", token.getText());

        token = lexer.nextToken(null);
        assertNull(token);
    }

    @Test
    public void testNextToken_quotedString() throws IOException {
        in.readLine();
        in.append("\"field1,\"field2\",\"field3\"");
        Token token = lexer.nextToken(null);
        assertTrue(token.getType() == Token.Type.FIELD);
        assertEquals("field1,", token.getText());

        token = lexer.nextToken(null);
        assertTrue(token.getType() == Token.Type.FIELD);
        assertEquals("field2,", token.getText());

        token = lexer.nextToken(null);
        assertTrue(token.getType() == Token.Type.FIELD);
        assertEquals("field3", token.getText());

        token = lexer.nextToken(null);
        assertNull(token);
    }

    @Test
    public void testNextToken_endOfStream() throws IOException {
        in.readLine();
        in.append("header1;header2");
        in.close();

        Token token = lexer.nextToken(null);
        assertTrue(token.getType() == Token.Type.FIELD);
        assertEquals("header1", token.getText());

        token = lexer.nextToken(null);
        assertTrue(token.getType() == Token.Type.FIELD);
        assertEquals("header2", token.getText());

        token = lexer.nextToken(null);
        assertNull(token);
        assertEquals(-1, lexer.getLineNumber());
    }

    @Test(expected = IOException.class)
    public void testNextToken_invalidEscape() throws IOException {
        in.readLine();
        in.append("\"field1\\\\field2\"");
        Token token = lexer.nextToken(null);
        token = lexer.nextToken(null);

        // the next call should throw an exception
        lexer.nextToken(null);
    }
}