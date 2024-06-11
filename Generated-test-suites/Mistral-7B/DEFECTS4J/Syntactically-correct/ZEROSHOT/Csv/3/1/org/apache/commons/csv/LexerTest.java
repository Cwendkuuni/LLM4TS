import org.apache.commons.csv.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

 private CSVFormat format;
 private ExtendedBufferedReader in;
 private Lexer lexer;

 @Before
 public void setUp() throws Exception {
 String input = "col1, col2\n" +
 "value1, value2\n" +
 "value3,\n" +
 "value4, value5\n" +
 "value6\n";
 format = new CSVFormat(',', null);
 in = new ExtendedBufferedReader(new StringReader(input));
 lexer = new Lexer(format, in);
 }

 @Test
 public void testReadEscape() throws Exception {
 int c = lexer.read();
 assertEquals(Constants.COMMA, lexer.isDelimiter((int) c));

 int escapedChar = lexer.readEscape();
 assertEquals(Constants.CR, escapedChar);

 assertEquals(Constants.LF, lexer.read()); // consume CR
 assertEquals(Constants.LF, lexer.read()); // consume LF
 }

 @Test
 public void testIsStartOfLine() throws Exception {
 int c = lexer.read();
 assertTrue(lexer.isStartOfLine((int) c));
 }

 @Test
 public void testIsDelimiter() throws Exception {
 int c = lexer.read();
 assertTrue(lexer.isDelimiter((int) c));
 }

 @Test
 public void testIsEscape() throws Exception {
 int c = lexer.read();
 assertFalse(lexer.isEscape((int) c));

 int escape = lexer.read();
 assertTrue(lexer.isEscape((int) escape));
 }

 @Test
 public void testIsQuoteChar() throws Exception {
 int c = lexer.read();
 assertFalse(lexer.isQuoteChar((int) c));

 int quoteChar = lexer.read();
 assertTrue(lexer.isQuoteChar((int) quoteChar));
 }

 @Test
 public void testIsCommentStart() throws Exception {
 int c = lexer.read();
 assertFalse(lexer.isCommentStart((int) c));

 int commentStart = lexer.read();
 assertTrue(lexer.isCommentStart((int) commentStart));
 }

 @Test
 public void testIsEndOfFile() throws Exception {
 int c = lexer.read();
 assertFalse(lexer.isEndOfFile((int) c));

 int endOfFile = lexer.read();
 assertTrue(lexer.isEndOfFile((int) endOfFile));
 }

 @Test
 public void testNextToken() throws Exception {
 Token token = new Token(null, null, null, null, null, null);
 Token nextToken = lexer.nextToken(token);

 assertEquals(TokenType.START_LINE, nextToken.getType());
 assertEquals("col1", nextToken.getString(0));
 assertEquals(null, nextToken.getString(1));
 assertEquals(null, nextToken.getInteger(2));
 assertEquals(null, nextToken.getDouble(3));
 assertEquals(null, nextToken.getBoolean(4));
 assertEquals(null, nextToken.getBigDecimal(5));

 nextToken = lexer.nextToken(token);
 assertEquals(TokenType.DATA_CELL, nextToken.getType());
 assertEquals("value1", nextToken.getString(0));
 assertEquals(null, nextToken.getString(1));
 assertEquals(null, nextToken.getInteger(2));
 assertEquals(null, nextToken.getDouble(3));
 assertEquals(null, nextToken.getBoolean(4));
 assertEquals(null, nextToken.getBigDecimal(5));

 nextToken = lexer.nextToken(token);
 assertEquals(TokenType.START_LINE, nextToken.getType());
 assertEquals("value3", nextToken.getString(0));
 assertEquals(null, nextToken.getString(1));
 assertEquals(null, nextToken.getInteger(2));
 assertEquals(null, nextToken.getDouble(3));
 assertEquals(null, nextToken.getBoolean(4));
 assertEquals(null, nextToken.getBigDecimal(5));

 nextToken = lexer.nextToken(token);
 assertEquals(TokenType.DATA_CELL, nextToken.getType());
 assertEquals("value4", nextToken.getString(0));
 assertEquals("value5", nextToken.getString(1));
 assertEquals(null, nextToken.getInteger(2));
 assertEquals(null, nextToken.getDouble(3));
 assertEquals(null, nextToken.getBoolean(4));
 assertEquals(null, nextToken.getBigDecimal(5));

 nextToken = lexer.nextToken(token);
 assertEquals(TokenType.START_LINE, nextToken.getType());
 assertEquals("value6", nextToken.getString(0));
 assertEquals(null, nextToken.getString(1));
 assertEquals(null, nextToken.getInteger(2));
 assertEquals(null, nextToken.getDouble(3));
 assertEquals(null, nextToken.getBoolean(4));
 assertEquals(null, nextToken.getBigDecimal(5));

 nextToken = lexer.nextToken(token);
 assertTrue(lexer.isEndOfFile());
 }
}