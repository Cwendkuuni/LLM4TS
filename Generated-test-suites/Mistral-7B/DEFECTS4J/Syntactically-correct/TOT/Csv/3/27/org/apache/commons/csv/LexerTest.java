import org.apache.commons.csv.Constants;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

 private Lexer lexer;
 private ExtendedBufferedReader mockReader;
 private StringBuilder buffer;

 @Before
 public void setUp() throws IOException {
 mockReader = new ExtendedBufferedReader(System.in) {
 @Override
 public char getLastChar() {
 throw new UnsupportedOperationException();
 }
 };
 buffer = new StringBuilder();
 lexer = new Lexer(new CSVFormat(','), mockReader);
 }

 @Test
 public void testIsStartOfLine() throws IOException {
 // Test when char is CR
 int cr = (int) Constants.CR;
 assertTrue(lexer.isStartOfLine(cr));
 // Test when char is LF
 int lf = (int) Constants.LF;
 assertTrue(lexer.isStartOfLine(lf));
 // Test when char is at the start of the file
 int undefined = (int) Constants.UNDEFINED;
 assertTrue(lexer.isStartOfLine(undefined));
 // Test when char is not a line terminator
 int charValue = 'A';
 assertFalse(lexer.isStartOfLine(charValue));
 }

 @Test
 public void testIsEndOfFile() throws IOException {
 // Test when char is not EOF
 int charValue = 'A';
 assertFalse(lexer.isEndOfFile(charValue));
 // Test when char is EOF
 int eof = Constants.END_OF_STREAM;
 assertTrue(lexer.isEndOfFile(eof));
 }

 @Test
 public void testIsWhitespace() throws IOException {
 // Test when char is whitespace
 int space = ' ';
 assertTrue(lexer.isWhitespace(space));
 // Test when char is delimiter
 int comma = ',';
 assertFalse(lexer.isWhitespace(comma));
 }

 @Test
 public void testIsDelimiter() throws IOException {
 // Test when char is delimiter
 int comma = ',';
 assertTrue(lexer.isDelimiter(comma));
 // Test when char is not delimiter
 int space = ' ';
 assertFalse(lexer.isDelimiter(space));
 }

 @Test
 public void testIsEscape() throws IOException {
 // Test when char is escape
 int backslash = '\\';
 assertTrue(lexer.isEscape(backslash));
 // Test when char is not escape
 int space = ' ';
 assertFalse(lexer.isEscape(space));
 }

 @Test
 public void testIsQuoteChar() throws IOException {
 // Test when char is quoteChar
 int quote = '\'';
 assertTrue(lexer.isQuoteChar(quote));
 // Test when char is not quoteChar
 int space = ' ';
 assertFalse(lexer.isQuoteChar(space));
 }

 @Test
 public void testIsCommentStart() throws IOException {
 // Test when char is commentStart
 int comment = '/';
 assertTrue(lexer.isCommentStart(comment));
 // Test when char is not commentStart
 int space = ' ';
 assertFalse(lexer.isCommentStart(space));
 }

 @Test
 public void testNextToken() throws IOException {
 // Create a mock token
 Token token = new Token();
 // Test when next character is a line terminator
 int lineTerminator = (int) Constants.LF;
 lexer.readEndOfLine(lineTerminator);
 Token nextToken = lexer.nextToken(token);
 assertEquals(TokenType.LINE_BREAK, nextToken.getType());
 // Test when next character is a whitespace character
 int space = ' ';
 assertEquals(TokenType.WHITESPACE, lexer.nextToken(token).getType());
 // Test when next character is a delimiter
 int comma = ',';
 assertEquals(TokenType.DELIMITER, lexer.nextToken(token).getType());
 // Test when next character is a quote char
 int quote = '\'';
 assertEquals(TokenType.QUOTE, lexer.nextToken(token).getType());
 // Test when next character is an escape char
 int backslash = '\\';
 assertEquals(TokenType.ESCAPE, lexer.nextToken(token).getType());
 // Test when next character is a comment start char
 int comment = '/';
 assertEquals(TokenType.COMMENT, lexer.nextToken(token).getType());
 // Test when next character is a letter
 int letter = 'A';
 assertEquals(TokenType.STRING, lexer.nextToken(token).getType());
 // Test when next character is a number
 int number = '1';
 assertEquals(TokenType.NUMBER, lexer.nextToken(token).getType());
 // Test when next character is an unknown character
 int unknown = 'X';
 assertEquals(TokenType.INVALID, lexer.nextToken(token).getType());
 // Test when next character is the end of stream
 int eof = Constants.END_OF_STREAM;
 assertEquals(TokenType.EOF, lexer.nextToken(token).getType());
 }
}