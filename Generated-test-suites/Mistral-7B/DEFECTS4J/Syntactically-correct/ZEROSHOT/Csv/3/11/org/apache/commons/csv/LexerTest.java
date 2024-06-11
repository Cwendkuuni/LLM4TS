import org.apache.commons.csv.Constants;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

 private Lexer lexer;
 private ExtendedBufferedReader mockReader;

 @Before
 public void setUp() {
 mockReader = new ExtendedBufferedReader(new StringReader("line1,\"value1,value2\",line2\nline3\tline4\n"));
 lexer = new Lexer(new CSVFormat(new CSVParserDefaultConfig.Builder().build()), mockReader);
 }

 @Test
 public void testGetLineNumber() throws IOException {
 assertEquals(1, lexer.getLineNumber());
 }

 @Test
 public void testHandleEscapeCR() throws IOException {
 assertEquals(Constants.CR, lexer.readEscape());
 assertEquals(Constants.CR, mockReader.getLastChar());
 }

 @Test(expected = IOException.class)
 public void testHandleEscapeEndOfStream() throws IOException {
 lexer.readEscape();
 mockReader.read(); // consumes CR
 assertFalse(mockReader.ready());
 lexer.readEscape();
 }

 @Test
 public void testTrimTrailingSpaces() throws IOException {
 StringBuilder buffer = new StringBuilder(" value   ");
 lexer.trimTrailingSpaces(buffer);
 assertEquals("value", buffer.toString());
 }

 @Test
 public void testReadEndOfLine() throws IOException {
 assertTrue(lexer.readEndOfLine(Constants.CR));
 assertTrue(lexer.readEndOfLine(Constants.LF));
 assertTrue(lexer.readEndOfLine(Constants.CR));
 assertTrue(lexer.readEndOfLine(Constants.LF));
 }

 @Test
 public void testIsStartOfLine() throws IOException {
 assertTrue(lexer.isStartOfLine(Constants.LF));
 assertTrue(lexer.isStartOfLine(Constants.CR));
 assertFalse(lexer.isStartOfLine('a'));
 }

 @Test
 public void testIsEndOfFile() throws IOException {
 assertFalse(lexer.isEndOfFile(Constants.LF));
 assertFalse(lexer.isEndOfFile(Constants.CR));
 assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
 }

 @Test
 public void testIsDelimiter() throws IOException {
 assertTrue(lexer.isDelimiter(','));
 assertFalse(lexer.isDelimiter('a'));
 }

 @Test
 public void testIsEscape() throws IOException {
 assertTrue(lexer.isEscape('\\'));
 assertFalse(lexer.isEscape('a'));
 }

 @Test
 public void testIsQuoteChar() throws IOException {
 assertTrue(lexer.isQuoteChar('"'));
 assertFalse(lexer.isQuoteChar('a'));
 }

 @Test
 public void testIsCommentStart() throws IOException {
 assertFalse(lexer.isCommentStart('a'));
 }

 @Test
 public void testNextToken() throws IOException {
 Token token = new Token();
 Token nextToken = lexer.nextToken(token);
 assertEquals(TokenType.START_LINE, nextToken.getType());
 assertEquals("line1", nextToken.getValue());

 nextToken = lexer.nextToken(token);
 assertEquals(TokenType.FIELD_START, nextToken.getType());
 assertEquals(",", nextToken.getValue());

 nextToken = lexer.nextToken(token);
 assertEquals(TokenType.STRING, nextToken.getType());
 assertEquals("value1,value2", nextToken.getValue());

 nextToken = lexer.nextToken(token);
 assertEquals(TokenType.START_LINE, nextToken.getType());
 assertEquals("line2", nextToken.getValue());

 nextToken = lexer.nextToken(token);
 assertEquals(TokenType.END_OF_STREAM, nextToken.getType());
 }
}