import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.AssertFalse;
import static org.junit.Assert.assertTrue;

public class TokenMgrErrorTest {

 private static final String STRING_WITH_ESCAPE_SEQUENCES = "abcd\\ef\\g\\h\n\r\t\"'\\\\";

 @Test
 public void testAddEscapes_NormalCharacter() {
 String input = "abcdefgh\n\r\t\"'\\";
 String expectedOutput = "abcdefgh\\n\\r\\t\"'\\\\";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testAddEscapes_NullInput() {
 String output = TokenMgrError.addEscapes(null);
 assertTrue(output.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testAddEscapes_NullInputWithException() {
 String input = null;
 TokenMgrError.addEscapes(input);
 }

 @Test
 public void testAddEscapes_SpecialCharacter() {
 String input = "\0\b\t\n\f\r\"'\\\\";
 String expectedOutput = "\\0\\b\\t\\n\\f\\r\\\"\\'\\\\\\";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testLexicalError_EOFSeen() {
 boolean eofSeen = true;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 2;
 String errorAfter = "errorAfter";
 char curChar = '\0';
 int reason = TokenMgrError.LEXICAL_ERROR;
 String expectedErrorMessage = "Lexical error at line 1, column 2.  Encountered: <EOF> ";
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String actualErrorMessage = error.getMessage();
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }

 @Test
 public void testLexicalError_NotEOFSeen() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 2;
 String errorAfter = "errorAfter";
 char curChar = 'a';
 int reason = TokenMgrError.LEXICAL_ERROR;
 String expectedErrorMessage = "Lexical error at line 1, column 2.  Encountered: \"a\" ";
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String actualErrorMessage = error.getMessage();
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }

 @Test
 public void testLexicalError_InvalidLexicalState() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 2;
 String errorAfter = "errorAfter";
 char curChar = 'a';
 int reason = TokenMgrError.INVALID_LEXICAL_STATE;
 String expectedErrorMessage = "Lexical error at line 1, column 2.  Invalid lexical state: 1";
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String actualErrorMessage = error.getMessage();
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }

 @Test
 public void testLexicalError_LoopDetected() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 2;
 String errorAfter = "errorAfter";
 char curChar = 'a';
 int reason = TokenMgrError.LOOP_DETECTED;
 String expectedErrorMessage = "Lexical error at line 1, column 2.  Loop detected";
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String actualErrorMessage = error.getMessage();
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }

}