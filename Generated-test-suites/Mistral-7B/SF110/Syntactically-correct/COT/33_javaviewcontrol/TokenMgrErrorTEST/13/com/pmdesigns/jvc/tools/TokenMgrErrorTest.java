import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

 private static final String TEST_INPUT = "This is a test string with special characters: \b\t\n\f\r\"'\\/.";

 @Test
 public void testAddEscapes() {
 String expected = "This is a test string with special characters: \\b\\t\\n\\f\\r\\\"'\\\\/.";
 String result = TokenMgrError.addEscapes(TEST_INPUT);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddEscapesWithNullInput() {
 String result = TokenMgrError.addEscapes(null);
 }

 @Test
 public void testLexicalError() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "input";
 char curChar = '!';
 String expected = "Lexical error at line 1, column 1. Encountered: ! (63), after : input";
 String result = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLexicalErrorWithInvalidArguments() {
 boolean eofSeen = true;
 int lexState = -1;
 int errorLine = 0;
 int errorColumn = 'a';
 String errorAfter = null;
 char curChar = '\u0000';
 String expectedMessage = "Illegal argument: errorLine";
 String result = "";
 try {
 result = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
 } catch (IllegalArgumentException e) {
 assertEquals(expectedMessage, e.getMessage());
 }
 }

 @Test
 public void testConstructorWithMessage() {
 String message = "Custom error message";
 int reason = TokenMgrError.LEXICAL_ERROR;
 TokenMgrError error = new TokenMgrError(message, reason);
 assertEquals(message, error.getMessage());
 assertEquals(reason, error.errorCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeReason() {
 int reason = -1;
 String message = "Custom error message";
 TokenMgrError error = new TokenMgrError(message, reason);
 }

 @Test
 public void testConstructorWithAllArguments() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 2;
 int errorColumn = 3;
 String errorAfter = "input";
 char curChar = '!';
 int reason = TokenMgrError.INVALID_LEXICAL_STATE;
 String expected = "Lexical error at line 2, column 3. Encountered: ! (63), after : input";
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 assertEquals(expected, error.getMessage());
 }
}