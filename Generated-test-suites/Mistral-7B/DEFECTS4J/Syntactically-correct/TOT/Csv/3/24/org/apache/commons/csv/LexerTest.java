import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LexerTest {

 private Lexer lexer;
 private CSVFormat format;
 private ExtendedBufferedReader in;

 @TempDir
 private File testDataDir;

 @BeforeEach
 public void setup() throws IOException {
 format = CSVFormat.DEFAULT;
 in = new ExtendedBufferedReader(new FileReader(new File(testDataDir, "test.csv")));
 lexer = new Lexer(format, in);
 }

 @Test
 public void testNextToken_Whitespace() throws IOException {
 String expected = " ";
 Token token = new Token();
 Token actual = lexer.nextToken(token);
 assertEquals(expected, token.getValue());
 assertEquals(Token.Type.WHITESPACE, actual.getType());
 }

 @Test
 public void testNextToken_Delimiter() throws IOException {
 String expected = ",";
 Token token = new Token();
 Token actual = lexer.nextToken(token);
 assertEquals(expected, token.getValue());
 assertEquals(Token.Type.DELIMITER, actual.getType());
 }

 @Test
 public void testNextToken_QuotedString() throws IOException {
 String expected = "\"hello, world!\"";
 Token token = new Token();
 in.readLine(); // consume initial whitespace
 in.read(); // consume comma
 in.read(); // consume comma
 in.read(); // consume quote
 token = lexer.nextToken(token);
 assertEquals(expected, token.getValue());
 assertEquals(Token.Type.STRING, actual.getType());
 }

 @Test
 public void testNextToken_EscapedQuotedString() throws IOException {
 String expected = "\"\\,\" \\n\\r\"";
 Token token = new Token();
 in.readLine(); // consume initial whitespace
 in.read(); // consume comma
 in.read(); // consume comma
 in.read(); // consume comma
 in.read(); // consume quote
 token.setValue(token.getValue() + readEscape()); // escape sequence
 assertEquals(expected, token.getValue());
 assertEquals(Token.Type.STRING, actual.getType());
 }

 @Test
 public void testNextToken_Comment() throws IOException {
 String expected = "# This is a comment";
 Token token = new Token();
 Token actual = lexer.nextToken(token);
 assertEquals(expected, token.getValue());
 assertEquals(Token.Type.COMMENT, actual.getType());
 }

 @Test
 public void testNextToken_EmptyLine() throws IOException {
 String expected = "";
 Token token = new Token();
 in.readLine();
 assertEquals(expected, token.getValue());
 assertEquals(Token.Type.EMPTY, actual.getType());
 }

 @Test
 public void testNextToken_EndOfStream() throws IOException {
 String expected = "";
 Token token = new Token();
 token.setEndOfStream(true);
 Token actual = lexer.nextToken(token);
 assertEquals(expected, token.getValue());
 assertEquals(Token.Type.EOF, actual.getType());
 }

 @Test
 public void testNextToken_InvalidEscapeSequence() throws IOException {
 String expected = "";
 Token token = new Token();
 in.readLine(); // consume initial whitespace
 in.read(); // consume comma
 in.read(); // consume comma
 in.read(); // consume comma
 in.read(); // consume quote
 in.read(); // consume backslash
 in.read(); // consume 'x'
 token = lexer.nextToken(token);
 assertEquals(expected, token.getValue());
 assertEquals(Token.Type.STRING, actual.getType());
 }

 @Test
 public void testNextToken_UnrecognizedCharacter() throws IOException {
 String expected = "?";
 Token token = new Token();
 in.readLine(); // consume initial whitespace
 in.read(); // consume comma
 in.read(); // consume comma
 in.read(); // consume comma
 in.read(); // consume quote
 in.read(); // consume '?'
 token.setValue(token.getValue() + (char) in.read()); // consume next character
 assertEquals(expected, token.getValue());
 assertEquals(Token.Type.STRING, actual.getType());
 }

 @Test
 public void testNextToken_MalformedLine() throws IOException {
 String expected = "\"";
 Token token = new Token();
 in.readLine(); // consume initial whitespace
 in.read(); // consume comma
 token.setValue(token.getValue() + (char) in.read()); // consume quote
 assertEquals(expected, token.getValue());
 assertEquals(Token.Type.STRING, actual.getType());
 assertTrue(in.isEOF());
 }

 @Test
 public void testNextToken_MalformedInputFile() throws IOException {
 String expected = "";
 Token token = new Token();
 // Set up a malformed input file
 File testFile = new File(testDataDir, "test_malformed.csv");
 try (FileWriter writer = new FileWriter(testFile)) {
 writer.write("header1,header2\n");
 writer.write("value1,value2,\"value3");
 writer.close();
 }
 lexer = new Lexer(format, new ExtendedBufferedReader(new FileReader(testFile)));
 token = lexer.nextToken(token);
 assertEquals(expected, token.getValue());
 assertEquals(Token.Type.EOF, actual.getType());
 }
}