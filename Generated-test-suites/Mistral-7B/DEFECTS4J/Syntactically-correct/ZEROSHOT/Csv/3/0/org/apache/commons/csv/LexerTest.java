import org.apache.commons.csv.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LexerTest {

 private CSVFormat format;
 private ExtendedBufferedReader in;
 private Lexer lexer;

 @Before
 public void setUp() {
 format = new CSVFormat();
 format.setIgnoreSurroundingSpaces(true);
 format.setIgnoreEmptyLines(true);

 try {
 in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test".getBytes()), "UTF-8"));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }

 lexer = new Lexer(format, in);
 }

 @After
 public void tearDown() throws IOException {
 in.close();
 }

 @Test
 public void testGetLineNumber() throws IOException {
 assertEquals(1, lexer.getLineNumber());
 lexer.nextToken(null);
 assertEquals(2, lexer.getLineNumber());
 }

 @Test
 public void testReadEscape() throws IOException {
 assertEquals(CR, lexer.readEscape());
 assertEquals(LF, lexer.readEscape());
 assertEquals(TAB, lexer.readEscape());
 assertEquals(BACKSPACE, lexer.readEscape());
 assertEquals(FF, lexer.readEscape());
 assertEquals(Constants.UNDEFINED, lexer.readEscape());
 assertFalse(lexer.isEndOfFile(Constants.UNDEFINED));
 }

 @Test
 public void testIsStartOfLine() throws IOException {
 assertTrue(lexer.isStartOfLine(Constants.UNDEFINED));
 lexer.nextToken(null);
 assertFalse(lexer.isStartOfLine(lexer.in.read()));
 }

 @Test
 public void testIsEndOfFile() throws IOException {
 assertFalse(lexer.isEndOfFile(Constants.UNDEFINED));
 lexer.nextToken(null);
 lexer.nextToken(null);
 assertTrue(lexer.isEndOfFile(lexer.in.read()));
 }

 @Test
 public void testIsDelimiter() throws IOException {
 assertTrue(lexer.isDelimiter(lexer.delimiter));
 assertFalse(lexer.isDelimiter('x'));
 }

 @Test
 public void testIsEscape() throws IOException {
 assertTrue(lexer.isEscape(lexer.escape));
 assertFalse(lexer.isEscape('x'));
 }

 @Test
 public void testIsQuoteChar() throws IOException {
 assertTrue(lexer.isQuoteChar(lexer.quoteChar));
 assertFalse(lexer.isQuoteChar('x'));
 }

 @Test
 public void testIsCommentStart() throws IOException {
 assertTrue(lexer.isCommentStart(lexer.commmentStart));
 assertFalse(lexer.isCommentStart('x'));
 }

 @Test
 public void testIsWhitespace() throws IOException {
 char[] chars = new char[32];
 in.read(chars);
 for (char c : chars) {
 if (Character.isWhitespace(c)) {
 assertTrue(lexer.isWhitespace((int) c));
 } else {
 assertFalse(lexer.isWhitespace((int) c));
 }
 }
 }

 @Test
 public void testNextToken() throws IOException {
 String expected = "test";
 StringBuilder token = new StringBuilder(expected.length());
 Token actual = lexer.nextToken(token);
 assertEquals(expected, token.toString());
 assertNull(actual.getValue());
 assertFalse(actual.isQuoted());
 }

 @Test
 public void testNextTokenQuoted() throws IOException {
 String expected = "\"test\"";
 StringBuilder token = new StringBuilder(expected.length());
 Token actual = lexer.nextToken(token);
 assertEquals(expected, token.toString());
 assertTrue(actual.isQuoted());
 }

 @Test(expected = IOException.class)
 public void testNextTokenInvalidEscapeSequence() throws IOException {
 String expected = "test\\z";
 StringBuilder token = new StringBuilder(expected.length());
 lexer.nextToken(token);
 }
}