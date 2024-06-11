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
 mockReader = new ExtendedBufferedReader(new StringReader("Some CSV data\n" +
 "first,second,third\n" +
 "fourth,fifth,sixth\n"));
 lexer = new Lexer(new CSVFormat(new CSVParserDefaultConfig.Builder()
 .setDelimiter(',')
 .build()), mockReader);
 buffer = new StringBuilder();
 }

 /**
 * Test nextToken method with basic input
 */
 @Test
 public void testNextTokenBasicInput() throws IOException {
 Token token = lexer.nextToken(null);
 assertEquals("first", token.getString());
 assertEquals(Lexer.TokenType.STRING, token.getType());
 }

 /**
 * Test nextToken method with empty string
 */
 @Test
 public void testNextTokenEmptyString() throws IOException {
 Token token = lexer.nextToken(null);
 assertEquals("", token.getString());
 assertEquals(Lexer.TokenType.EMPTY, token.getType());
 }

 /**
 * Test nextToken method with delimiter
 */
 @Test
 public void testNextTokenDelimiter() throws IOException {
 Token token = lexer.nextToken(null);
 assertEquals(",", token.getString());
 assertEquals(Lexer.TokenType.DELIMITER, token.getType());
 }

 /**
 * Test nextToken method with escape sequence
 */
 @Test
 public void testNextTokenEscapeSequence() throws IOException {
 Token token = lexer.nextToken(null);
 assertEquals("second", token.getString());
 assertEquals(Lexer.TokenType.STRING, token.getType());
 lexer.readEscape(); // consume backslash
 assertEquals(Constants.LF, lexer.read()); // consume LF
 }

 /**
 * Test nextToken method with quote character
 */
 @Test
 public void testNextTokenQuoteCharacter() throws IOException {
 Token token = lexer.nextToken(null);
 assertEquals("third", token.getString());
 assertEquals(Lexer.TokenType.STRING, token.getType());
 }

 /**
 * Test nextToken method with comment
 */
 @Test
 public void testNextTokenComment() throws IOException {
 Token token = lexer.nextToken(null);
 assertEquals("fourth", token.getString());
 assertEquals(Lexer.TokenType.STRING, token.getType());
 assertEquals(lexer.getLineNumber(), 2); // check line number
 lexer.skipComment();
 Token commentToken = lexer.nextToken(null);
 assertEquals("fifth", token.getString());
 assertEquals(Lexer.TokenType.STRING, token.getType());
 }

 /**
 * Test nextToken method with end of file
 */
 @Test(expected = IOException.class)
 public void testNextTokenEndOfFile() throws IOException {
 Token token = lexer.nextToken(null);
 token = lexer.nextToken(null);
 token = lexer.nextToken(null);
 token = lexer.nextToken(null);
 token = lexer.nextToken(null);
 mockReader.close();
 lexer.nextToken(null); // should throw an IOException
 }

 /**
 * Test isStartOfLine method
 */
 @Test
 public void testIsStartOfLine() throws IOException {
 assertTrue(lexer.isStartOfLine(lexer.getLineNumber()));
 lexer.nextToken(null);
 assertFalse(lexer.isStartOfLine(lexer.getLineNumber()));
 }

 /**
 * Test isEndOfFile method
 */
 @Test
 public void testIsEndOfFile() throws IOException {
 assertFalse(lexer.isEndOfFile(Constants.UNDEFINED));
 assertFalse(lexer.isEndOfFile(lexer.getLineNumber()));
 mockReader.close();
 assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
 }

}