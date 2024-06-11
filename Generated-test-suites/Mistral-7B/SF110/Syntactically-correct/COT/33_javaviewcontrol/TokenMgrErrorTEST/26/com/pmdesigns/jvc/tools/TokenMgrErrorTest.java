import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

 private TokenMgrError tokenMgrError;

 @Test
 public void testAddEscapes() {
 String input = "Hello World!\n\tBackslash \\ and Quotes \"'";
 String expectedOutput = "Hello World!\\n\\tBackslash \\ and Quotes \\\"'";
 tokenMgrError = new TokenMgrError();
 String output = tokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testLexicalErrorWithEOF() {
 tokenMgrError = new TokenMgrError(true, 0, 1, 1, "", 0, TokenMgrError.LEXICAL_ERROR);
 String expectedMessage = "Lexical error at line 1, column 1. ";
 String actualMessage = tokenMgrError.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testLexicalErrorWithNonEOF() {
 tokenMgrError = new TokenMgrError(false, 0, 1, 1, "abc", 'a', TokenMgrError.LEXICAL_ERROR);
 String expectedMessage = "Lexical error at line 1, column 1. Encountered: \"a\" (97), after : \"abc\"";
 String actualMessage = tokenMgrError.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testInvalidLexicalState() {
 tokenMgrError = new TokenMgrError(false, -1, 1, 1, "abc", 'a', TokenMgrError.INVALID_LEXICAL_STATE);
 String expectedMessage = "Invalid lexical state";
 String actualMessage = tokenMgrError.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }

 @Test(expected = TokenMgrError.class)
 public void testLoopDetected() {
 tokenMgrError = new TokenMgrError(false, 0, 1, 1, "abc", 'a', TokenMgrError.LOOP_DETECTED);
 }

 @Test
 public void testConstructorWithMessageAndReason() {
 String message = "Custom error message";
 int reason = TokenMgrError.LEXICAL_ERROR;
 tokenMgrError = new TokenMgrError("Custom error message", reason);
 String expectedMessage = "Custom error message";
 String actualMessage = tokenMgrError.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }

 @Test
 public void testConstructorWithAllArguments() {
 boolean eofSeen = true;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "abc";
 char curChar = 'a';
 int reason = TokenMgrError.LEXICAL_ERROR;
 tokenMgrError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expectedMessage = "Lexical error at line 1, column 1. Encountered: <EOF> ";
 String actualMessage = tokenMgrError.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }
}