import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class LexerTest {

    @Test
    public void testLexerBasic() throws IOException {
        CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(',')
                .setEscapeCharacter('\\')
                .setQuoteChar('"')
                .setCommentStart(";")
                .build();

        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("a,b,c;d\ne1,2,3\n"));
        Lexer lexer = new Lexer(format, in);

        Token token;
        for (int i = 0; i < 6; i++) {
            token = lexer.nextToken(null);
            Assert.assertNotNull(token);
            switch (i) {
                case 0:
                    Assert.assertEquals(",", token.getType());
                    Assert.assertEquals("", token.getValue());
                    break;
                case 1:
                    Assert.assertEquals("FIELD", token.getType());
                    Assert.assertEquals("a", token.getValue());
                    break;
                case 2:
                    Assert.assertEquals(",", token.getType());
                    Assert.assertEquals("", token.getValue());
                    break;
                case 3:
                    Assert.assertEquals("FIELD", token.getType());
                    Assert.assertEquals("b", token.getValue());
                    break;
                case 4:
                    Assert.assertEquals(",", token.getType());
                    Assert.assertEquals("", token.getValue());
                    break;
                case 5:
                    Assert.assertEquals("FIELD", token.getType());
                    Assert.assertEquals("c", token.getValue());
                    break;
                default:
                    Assert.assertEquals(Token.Type.EOF, token.getType());
            }
        }
    }

    @Test
    public void testLexerEmptyLine() throws IOException {
        CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(',')
                .setEscapeCharacter('\\')
                .setQuoteChar('"')
                .setCommentStart(";")
                .build();

        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("a,b,c\n;d\n\n"));
        Lexer lexer = new Lexer(format, in);

        Token token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.COMMENT, token.getType());
        Assert.assertEquals("", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.EOF, token.getType());
    }

    @Test
    public void testLexerTrailingSpaces() throws IOException {
        CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(',')
                .setEscapeCharacter('\\')
                .setQuoteChar('"')
                .setCommentStart(";")
                .build();

        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("a, b, c\n ; d e\t f\n"));
        Lexer lexer = new Lexer(format, in);

        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            Token token = lexer.nextToken(null);
            if (token.getType() != Token.Type.FIELD) {
                continue;
            }
            buffer.append(token.getValue());
            buffer.append(", ");
        }

        lexer.trimTrailingSpaces(buffer);

        Assert.assertEquals("a, b, c, ,", buffer.toString());
    }

    @Test(expected = IOException.class)
    public void testLexerInvalidEscapeSequence() throws IOException {
        CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(',')
                .setEscapeCharacter('\\')
                .setQuoteChar('"')
                .setCommentStart(";")
                .build();

        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("a,b,c\\x")); // invalid escape sequence
        Lexer lexer = new Lexer(format, in);
        lexer.nextToken(null);
    }

    @Test
    public void testLexerComment() throws IOException {
        CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(',')
                .setEscapeCharacter('\\')
                .setQuoteChar('"')
                .setCommentStart(";")
                .build();

        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("a,b; This is a comment\nc,d"));
        Lexer lexer = new Lexer(format, in);

        Token token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("a", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.COMMENT, token.getType());
        Assert.assertEquals(" This is a comment", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("b", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.COMMA, token.getType());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("c", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.EOF, token.getType());
    }

    @Test
    public void testLexerQuotedField() throws IOException {
        CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(',')
                .setEscapeCharacter('\\')
                .setQuoteChar('"')
                .setCommentStart(";")
                .build();

        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("a,\"field with,\"quotes\";b,c"));
        Lexer lexer = new Lexer(format, in);

        Token token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("a", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.QUOTED_STRING, token.getType());
        Assert.assertEquals("field with,\"quotes\"", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("b", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.COMMA, token.getType());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("c", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.EOF, token.getType());
    }

    @Test
    public void testLexerIgnoringSurroundingSpaces() throws IOException {
        CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(',')
                .setEscapeCharacter('\\')
                .setQuoteChar('"')
                .setCommentStart(";")
                .setIgnoreSurroundingSpaces(true)
                .build();

        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader(" a,b \tc,d"));
        Lexer lexer = new Lexer(format, in);

        Token token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("a", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("b", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("c", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("d", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.EOF, token.getType());
    }

    @Test
    public void testLexerIgnoreEmptyLines() throws IOException {
        CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(',')
                .setEscapeCharacter('\\')
                .setQuoteChar('"')
                .setCommentStart(";")
                .setIgnoreEmptyLines(true)
                .build();

        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("a,b\n\n\tc,d\ne1,2"));
        Lexer lexer = new Lexer(format, in);

        Token token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("a", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("b", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("c", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("d", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("e1", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("2", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.EOF, token.getType());
    }

    @Test
    public void testLexerEndOfFile() throws IOException {
        CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(',')
                .setEscapeCharacter('\\')
                .setQuoteChar('"')
                .setCommentStart(";")
                .build();

        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("a,b,c"));
        Lexer lexer = new Lexer(format, in);

        Token token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("a", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("b", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.FIELD, token.getType());
        Assert.assertEquals("c", token.getValue());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.EOF, token.getType());

        token = lexer.nextToken(null);
        Assert.assertEquals(Token.Type.EOF, token.getType());
    }
}