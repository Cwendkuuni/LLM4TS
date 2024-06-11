import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TokenMgrErrorTest {

 private TokenMgrError tokenMgrError;

 @Test
 public void testAddEscapesEmptyString() {
 String input = "";
 String expectedOutput = "";
 String output = tokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testAddEscapesSingleCharacter() {
 char input = 'a';
 String expectedOutput = "\\u0061";
 String output = tokenMgrError.addEscapes(Character.toString(input));
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testAddEscapesMultipleCharacters() {
 String input = "abc";
 String expectedOutput = "\\u0061\\u0062\\u0063";
 String output = tokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testAddEscapesSpecialCharacters() {
 String input = "\tab\n\fc\r\"'\\";
 String expectedOutput = "\\t\\b\\n\\f\\r\\\"\\'\\\\";
 String output = tokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEscapesNullInput() {
 String input = null;
 tokenMgrError = new TokenMgrError(null, 0);
 String output = tokenMgrError.addEscapes(input);
 }

 @Test
 public void testLexicalError() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "if";
 char curChar = '(';
 String expectedOutput = "Lexical error at line 5, column 10. Encountered: ( after : if";
 String output = tokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testLexicalErrorWithEOF() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "";
 char curChar = '\0';
 String expectedOutput = "Lexical error at line 5, column 10. Encountered: <EOF> ";
 String output = tokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testConstructorWithMessageAndReason() {
 String message = "Invalid token";
 int reason = 1;
 tokenMgrError = new TokenMgrError(message, reason);
 String expectedOutput = "Invalid token";
 assertEquals(expectedOutput, tokenMgrError.getMessage());
 assertEquals(reason, tokenMgrError.errorCode);
 }

 @Test
 public void testConstructorWithLexicalErrorDetails() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 5;
 int errorColumn = 10;
 String errorAfter = "if";
 char curChar = '(';
 int reason = 1;
 tokenMgrError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expectedOutput = "Lexical error at line 5, column 10. Encountered: ( after : if";
 String output = tokenMgrError.getMessage();
 assertEquals(expectedOutput, output);
 assertEquals(reason, tokenMgrError.errorCode);
 }
}