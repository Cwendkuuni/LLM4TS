import org.apache.commons.csv.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

 private ExtendedBufferedReader mockInput;
 private Lexer lexer;

 @Before
 public void setUp() throws IOException {
 mockInput = new ExtendedBufferedReader(new StringReader("" +
 "\"field1,field2\",field3\r\n" +
 "field1,\"field2,field3\"\r\n" +
 "field1,field2,field3\r\n" +
 "field1,\r\n" +
 "field1,field2,\r" +
 "field1,\tfield2\r\n" +
 "field1,\tfield2,\r\n" +
 "field1,\tfield2,\r\n" +
 "field1,\tfield2,\r\n" +
 "\r\n" +
 "\r\n" +
 ""));
 lexer = new CSVLexer(new CSVFormat(), mockInput);
 }

 @Test
 public void testNextToken_ValidCharacters() throws IOException {
 Token token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_START, token.getType());
 assertEquals("field1", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_START, token.getType());
 assertEquals("field2", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_DATA, token.getType());
 assertEquals("field3", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_START, token.getType());
 assertEquals("field1", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_DATA, token.getType());
 assertEquals("field2", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_DATA, token.getType());
 assertEquals("field3", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_START, token.getType());
 assertEquals("field1", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_END, token.getType());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_END, token.getType());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_END, token.getType());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_END, token.getType());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_END, token.getType());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_END, token.getType());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_END, token.getType());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_END, token.getType());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_END, token.getType());

 token = lexer.nextToken(null);
 assertEquals(TokenType.END_OF_RECORD, token.getType());
 }

 @Test(expected = IllegalStateException.class)
 public void testNextToken_InvalidEscapeSequence() throws IOException {
 Token token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_START, token.getType());
 assertEquals("field1", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_DATA, token.getType());
 assertEquals("field2,field3", token.getValue()); // this contains an escape sequence

 token = lexer.nextToken(null); // should read the escape character (backslash)
 assertEquals(TokenType.ESCAPE, token.getType());

 token = lexer.nextToken(null); // should read the next character (d)
 assertEquals(TokenType.FIELD_DATA, token.getType());
 assertEquals("field3", token.getValue());

 // now try to read the next token and it will fail as there is no more data in the input stream
 token = lexer.nextToken(null);
 fail("Expected an IOException or an IllegalStateException");
 }

 @Test(expected = IOException.class)
 public void testNextToken_EndOfStream() throws IOException {
 Token token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_START, token.getType());
 assertEquals("field1", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_START, token.getType());
 assertEquals("field2", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_DATA, token.getType());
 assertEquals("field3", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_START, token.getType());
 assertEquals("field1", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_DATA, token.getType());
 assertEquals("field2", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_DATA, token.getType());
 assertEquals("field3", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_START, token.getType());
 assertEquals("field1", token.getValue());

 token = lexer.nextToken(null); // this should read the last \r\n
 assertEquals(TokenType.FIELD_END, token.getType());

 // now try to read the next token and it should fail as there is no more data in the input stream
 token = lexer.nextToken(null);
 fail("Expected an IOException");
 }

 @Test
 public void testTrimTrailingSpaces() throws IOException {
 StringBuilder buffer = new StringBuilder();
 token = lexer.nextToken(buffer);
 assertEquals(TokenType.FIELD_START, token.getType());
 assertEquals("field1", token.getValue());

 buffer.setLength(buffer.length() - "field1".length());
 lexer.trimTrailingSpaces(buffer);
 assertEquals("", buffer.toString());

 token = lexer.nextToken(buffer);
 assertEquals(TokenType.FIELD_START, token.getType());
 assertEquals("field2", token.getValue());

 buffer.setLength(buffer.length() - "field2".length());
 lexer.trimTrailingSpaces(buffer);
 assertEquals("", buffer.toString());

 buffer.setLength(0);
 token = lexer.nextToken(buffer);
 assertEquals(TokenType.FIELD_DATA, token.getType());
 assertEquals("field3", token.getValue());

 buffer.setLength(buffer.length() - "field3".length());
 lexer.trimTrailingSpaces(buffer);
 assertEquals("", buffer.toString());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_START, token.getType());
 assertEquals("field1", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_START, token.getType());
 assertEquals("field2", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_DATA, token.getType());
 assertEquals("field3", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_START, token.getType());
 assertEquals("field1", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_DATA, token.getType());
 assertEquals("field2", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_DATA, token.getType());
 assertEquals("field3", token.getValue());

 token = lexer.nextToken(null);
 assertEquals(TokenType.FIELD_START, token.getType());
 assertEquals("field1", token.getValue());

 token = lexer.nextToken(null); // this should read the last \r\n
 assertEquals(TokenType.FIELD_END, token.getType());

 buffer.setLength(0);
 token = lexer.nextToken(buffer);
 assertEquals(TokenType.FIELD_END, token.getType());

 buffer.setLength(buffer.length() - 1);
 lexer.trimTrailingSpaces(buffer);
 assertEquals("", buffer.toString());
 }

 @Test
 public void testIsWhitespace() throws IOException {
 assertTrue(lexer.isWhitespace('\t'));
 assertTrue(lexer.isWhitespace('\n'));
 assertTrue(lexer.isWhitespace('\r'));
 assertTrue(lexer.isWhitespace(' '));
 assertTrue(lexer.isWhitespace('\f'));

 assertFalse(lexer.isWhitespace('a'));
 assertFalse(lexer.isWhitespace('b'));
 assertFalse(lexer.isWhitespace('c'));
 }

 @Test
 public void testIsStartOfLine() throws IOException {
 assertTrue(lexer.isStartOfLine(' ')); // note that '\r' or '\n' are also considered as start-of-line
 assertTrue(lexer.isStartOfLine('\r'));
 assertTrue(lexer.isStartOfLine('\n'));
 assertTrue(lexer.isStartOfLine(UNDEFINED));

 assertFalse(lexer.isStartOfLine('\t'));
 assertFalse(lexer.isStartOfLine('a'));
 assertFalse(lexer.isStartOfLine('b'));
 assertFalse(lexer.isStartOfLine('c'));
 }

 @Test
 public void testIsEndOfFile() throws IOException {
 assertFalse(lexer.isEndOfFile('a'));
 assertFalse(lexer.isEndOfFile('b'));
 assertFalse(lexer.isEndOfFile('c'));

 assertTrue(lexer.isEndOfFile(END_OF_STREAM));
 }

 @Test
 public void testIsDelimiter() throws IOException {
 assertFalse(lexer.isDelimiter('\t'));
 assertFalse(lexer.isDelimiter('\r'));
 assertFalse(lexer.isDelimiter('\n'));
 assertFalse(lexer.isDelimiter('a'));
 assertFalse(lexer.isDelimiter('b'));

 assertTrue(lexer.isDelimiter(delimiter));
 }

 @Test
 public void testIsEscape() throws IOException {
 assertFalse(lexer.isEscape('\t'));
 assertFalse(lexer.isEscape('\r'));
 assertFalse(lexer.isEscape('\n'));
 assertFalse(lexer.isEscape('a'));
 assertFalse(lexer.isEscape('b'));

 assertTrue(lexer.isEscape(escape));
 }

 @Test
 public void testIsQuoteChar() throws IOException {
 assertFalse(lexer.isQuoteChar('\t'));
 assertFalse(lexer.isQuoteChar('\r'));
 assertFalse(lexer.isQuoteChar('\n'));
 assertFalse(lexer.isQuoteChar('a'));
 assertFalse(lexer.isQuoteChar('b'));

 assertTrue(lexer.isQuoteChar(quoteChar));
 }

 @Test
 public void testIsCommentStart() throws IOException {
 assertFalse(lexer.isCommentStart('\t'));
 assertFalse(lexer.isCommentStart('\r'));
 assertFalse(lexer.isCommentStart('\n'));
 assertFalse(lexer.isCommentStart('a'));
 assertFalse(lexer.isCommentStart('b'));

 assertTrue(lexer.isCommentStart(commmentStart));
 }
}