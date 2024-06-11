import org.apache.commons.csv.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class LexerTest {

 private Lexer lexer;
 private ExtendedBufferedReader mockReader;
 private CSVFormat format;

 @Before
 public void setUp() {
 format = new CSVFormat(new String[]{}, ',', null);
 mockReader = mock(ExtendedBufferedReader.class);
 lexer = new Lexer(format, mockReader);
 }

 @Test
 public void testGetLineNumber() throws IOException {
 int lineNumber = lexer.getLineNumber();
 assertEquals(1, lineNumber);

 when(mockReader.getLineNumber()).thenReturn(2);
 lineNumber = lexer.getLineNumber();
 assertEquals(2, lineNumber);
 }

 @Test
 public void testIsStartOfLine() throws IOException {
 int c = Constants.LF;
 assertTrue(lexer.isStartOfLine(c));
 c = Constants.CR;
 assertTrue(lexer.isStartOfLine(c));
 c = Constants.UNDEFINED;
 assertTrue(lexer.isStartOfLine(c));

 c = 'A';
 assertFalse(lexer.isStartOfLine(c));
 }

 @Test
 public void testIsEndOfFile() throws IOException {
 int c = Constants.LF;
 assertFalse(lexer.isEndOfFile(c));

 when(mockReader.read()).thenReturn(-1);
 c = lexer.isEndOfFile(c);
 assertTrue(lexer.isEndOfFile(c));
 }

 @Test
 public void testIsDelimiter() throws IOException {
 int c = lexer.delimiter;
 assertTrue(lexer.isDelimiter(c));

 c = 'a';
 assertFalse(lexer.isDelimiter(c));
 }

 @Test
 public void testIsEscape() throws IOException {
 int c = lexer.escape;
 assertTrue(lexer.isEscape(c));

 c = 'a';
 assertFalse(lexer.isEscape(c));
 }

 @Test
 public void testIsQuoteChar() throws IOException {
 int c = lexer.quoteChar;
 assertTrue(lexer.isQuoteChar(c));

 c = 'a';
 assertFalse(lexer.isQuoteChar(c));
 }

 @Test
 public void testIsCommentStart() throws IOException {
 int c = lexer.commmentStart;
 assertTrue(lexer.isCommentStart(c));

 c = 'a';
 assertFalse(lexer.isCommentStart(c));
 }

 @Test
 public void testIsWhitespace() throws IOException {
 int c = ' ';
 assertTrue(lexer.isWhitespace(c));

 c = '\t';
 assertTrue(lexer.isWhitespace(c));

 c = '\r';
 assertFalse(lexer.isWhitespace(c));

 c = '\n';
 assertFalse(lexer.isWhitespace(c));

 c = ',';
 assertFalse(lexer.isWhitespace(c));
 }

 @Test
 public void testTrimTrailingSpaces() throws IOException {
 StringBuilder buffer = new StringBuilder("abc def");
 lexer.trimTrailingSpaces(buffer);
 assertEquals("abc def", buffer.toString());

 buffer.setLength(buffer.length() + 5);
 buffer.setCharAt(buffer.length() - 1, ' ');
 buffer.setCharAt(buffer.length() - 2, ' ');
 buffer.setCharAt(buffer.length() - 3, ' ');
 lexer.trimTrailingSpaces(buffer);
 assertEquals("abc def", buffer.toString());

 buffer.setLength(buffer.length() + 5);
 buffer.setCharAt(buffer.length() - 1, '\t');
 buffer.setCharAt(buffer.length() - 2, '\r');
 lexer.trimTrailingSpaces(buffer);
 assertEquals("abc def", buffer.toString());
 }

 @Test
 public void testReadEndOfLine() throws IOException {
 int c = Constants.LF;
 assertFalse(lexer.readEndOfLine(c));

 when(mockReader.lookAhead()).thenReturn(Constants.CR);
 c = lexer.readEndOfLine(c);
 assertTrue(lexer.readEndOfLine(c));

 when(mockReader.lookAhead()).thenReturn(Constants.CR);
 when(mockReader.read()).thenReturn(Constants.LF);
 c = lexer.readEndOfLine(c);
 assertTrue(lexer.readEndOfLine(c));
 }

 @Test(expected = IOException.class)
 public void testReadEndOfLineWithEndOfStream() throws IOException {
 int c = Constants.LF;
 lexer.readEndOfLine(c);
 lexer.readEndOfLine(c);

 when(mockReader.read()).thenReturn(Constants.END_OF_STREAM);
 lexer.readEndOfLine(c);
 }

 @Test
 public void testNextToken() throws IOException {
 Token reusableToken = new Token();
 Token nextToken = lexer.nextToken(reusableToken);
 assertNull(nextToken);

 when(mockReader.read()).thenReturn('A');
 nextToken = lexer.nextToken(reusableToken);
 assertEquals("A", nextToken.getString());

 when(mockReader.read()).thenReturn(lexer.delimiter);
 nextToken = lexer.nextToken(reusableToken);
 assertNull(nextToken);

 when(mockReader.read()).thenReturn(lexer.delimiter);
 when(mockReader.read()).thenReturn('B');
 nextToken = lexer.nextToken(reusableToken);
 assertEquals("B", nextToken.getString());
 }

 @Test
 public void testNextTokenWithComment() throws IOException {
 Token reusableToken = new Token();
 when(mockReader.read()).thenReturn(lexer.commmentStart);
 when(mockReader.read()).thenReturn('*');
 when(mockReader.read()).thenReturn('/');
 when(mockReader.read()).thenReturn('C');

 Token nextToken = lexer.nextToken(reusableToken);
 assertNull(nextToken);
 }
}