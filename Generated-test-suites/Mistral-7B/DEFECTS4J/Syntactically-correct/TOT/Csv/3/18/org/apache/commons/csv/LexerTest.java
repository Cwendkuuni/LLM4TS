import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

 private Lexer lexer;
 private ExtendedBufferedReader mockInput;
 private CSVFormat format;

 @Before
 public void setup() {
 format = new CSVFormat(',', null, null, null, null, null, null, null, false, false);
 mockInput = new ExtendedBufferedReader(new MockInputStream());
 lexer = new Lexer(format, mockInput);
 }

 @Test
 public void testNextToken_emptyLine() throws IOException {
 mockInput.reset(); // reset the reader for each test
 String input = "\n";
 mockInput.setInput(input);
 Token token = lexer.nextToken(null);
 assertEquals(TokenType.NEWLINE, token.getType());
 assertEquals(0, token.getColumnNumber());
 assertEquals(1, token.getLength());
 }

 @Test
 public void testNextToken_whitespace() throws IOException {
 mockInput.reset();
 String input = "  \t\n ";
 mockInput.setInput(input);
 Token token = lexer.nextToken(null);
 assertEquals(TokenType.WHITESPACE, token.getType());
 assertEquals(0, token.getColumnNumber());
 assertEquals(input.length(), token.getLength());
 }

 @Test
 public void testNextToken_delimiter() throws IOException {
 mockInput.reset();
 String input = ", ";
 mockInput.setInput(input);
 Token token = lexer.nextToken(null);
 assertEquals(TokenType.DELIMITER, token.getType());
 assertEquals(0, token.getColumnNumber());
 assertEquals(1, token.getLength());
 }

 @Test
 public void testNextToken_simpleQuotedString() throws IOException {
 mockInput.reset();
 String input = "\"Hello, World!\"";
 mockInput.setInput(input);
 Token token = lexer.nextToken(null);
 assertEquals(TokenType.STRING, token.getType());
 assertEquals(0, token.getColumnNumber());
 assertEquals(input.length(), token.getLength());
 }

 @Test
 public void testNextToken_escapedQuotedString() throws IOException {
 mockInput.reset();
 String input = "\"Hello,\"World!\"";
 mockInput.setInput(input);
 Token token = lexer.nextToken(null);
 assertEquals(TokenType.STRING, token.getType());
 assertEquals(0, token.getColumnNumber());
 assertEquals(input.length(), token.getLength());
 }

 @Test
 public void testNextToken_comment() throws IOException {
 mockInput.reset();
 String input = "// This is a comment";
 mockInput.setInput(input);
 Token token = lexer.nextToken(null);
 assertEquals(TokenType.COMMENT, token.getType());
 assertEquals(0, token.getColumnNumber());
 assertEquals(input.length(), token.getLength());
 }

 @Test(expected = IOException.class)
 public void testNextToken_invalidEscapeSequence() throws IOException {
 mockInput.reset();
 String input = "\\z";
 mockInput.setInput(input);
 lexer.nextToken(null);
 fail("Should throw an IOException");
 }

 @Test(expected = IOException.class)
 public void testNextToken_endOfFile() throws IOException {
 mockInput.reset();
 String input = "1,2,3";
 mockInput.setInput(input);
 lexer.nextToken(null);
 lexer.nextToken(null);
 lexer.nextToken(null);
 mockInput.reset(); // reset the reader for the next test
 mockInput.setInput("");
 Token token = lexer.nextToken(null);
 fail("Should throw an IOException");
 }
}