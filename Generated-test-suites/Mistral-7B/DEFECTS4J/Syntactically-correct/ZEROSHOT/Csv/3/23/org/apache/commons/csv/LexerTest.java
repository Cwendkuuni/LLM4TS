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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LexerTest {

 private ExtendedBufferedReader in;
 private CSVFormat format;
 private Lexer lexer;

 @Before
 public void setUp() {
 in = mock(ExtendedBufferedReader.class);
 format = new CSVFormat(',', null);
 lexer = new Lexer(format, in);
 }

 @Test
 public void testGetLineNumber() throws IOException {
 when(in.getLineNumber()).thenReturn(42);
 assertEquals(42, lexer.getLineNumber());
 }

 @Test(expected = IOException.class)
 public void testReadEscapeWithEndOfStream() throws IOException {
 when(in.read()).thenReturn(Constants.END_OF_STREAM);
 lexer.readEscape();
 }

 @Test
 public void testIsWhitespace() {
 for (char c = 0; c < 256; c++) {
 if (Character.isWhitespace(c)) {
 assertTrue(lexer.isWhitespace((int) c));
 } else {
 assertFalse(lexer.isWhitespace((int) c));
 }
 }
 }

 @Test
 public void testIsStartOfLine() {
 for (int c = 0; c < 256; c++) {
 // CR and LF are line terminators, so they are considered start of line
 if (c == Constants.CR || c == Constants.LF) {
 assertTrue(lexer.isStartOfLine(c));
 } else {
 assertFalse(lexer.isStartOfLine(c));
 }
 }
}

 @Test
 public void testIsEndOfFile() {
 for (int c = 0; c < 256; c++) {
 assertFalse(lexer.isEndOfFile(c));
 }
 when(in.read()).thenReturn(Constants.END_OF_STREAM);
 assertTrue(lexer.isEndOfFile(in.read()));
}

 @Test
 public void testIsDelimiter() {
 char delimiter = ',';
 format = new CSVFormat(delimiter, null);
 lexer = new Lexer(format, in);
 assertTrue(lexer.isDelimiter((int) delimiter));
 }

 @Test
 public void testIsEscape() {
 char escape = '\\';
 format = new CSVFormat(',', escape);
 lexer = new Lexer(format, in);
 assertTrue(lexer.isEscape((int) escape));
 }

 @Test
 public void testIsQuoteChar() {
 char quoteChar = '"';
 format = new CSVFormat(',', null, '', quoteChar, null);
 lexer = new Lexer(format, in);
 assertTrue(lexer.isQuoteChar((int) quoteChar));
 }

 @Test
 public void testIsCommentStart() {
 char commentStart = '//';
 format = new CSVFormat(',', null, commentStart, null, null);
 lexer = new Lexer(format, in);
 assertTrue(lexer.isCommentStart((int) commentStart));
 }

 @Test
 public void testTrimTrailingSpaces() throws IOException {
 StringBuilder buffer = new StringBuilder("  test  ");
 lexer.trimTrailingSpaces(buffer);
 assertEquals("test", buffer.toString());
}

 @Test
 public void testReadEndOfLine() throws IOException {
 when(in.read()).thenReturn(Constants.CR);
 when(in.lookAhead()).thenReturn(Constants.LF);
 assertTrue(lexer.readEndOfLine(in.read()));
}

 @Test
 public void testNextTokenNullToken() throws IOException {
 Token token = null;
 Token result = lexer.nextToken(token);
 assertNull(result);
}
}