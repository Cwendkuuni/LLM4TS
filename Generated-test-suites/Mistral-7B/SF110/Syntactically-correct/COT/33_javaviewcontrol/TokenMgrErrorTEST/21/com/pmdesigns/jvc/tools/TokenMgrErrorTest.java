import static org.junit.Assert.*;
import com.pmdesigns.jvc.tools.TokenMgrError;

public class TokenMgrErrorTest {

 private TokenMgrError error;

 @Before
 public void setup() {
 error = new TokenMgrError();
 }

 // Test constructor with no arguments
 @Test
 public void testTokenMgrError() {
 TokenMgrError actual = new TokenMgrError();
 assertEquals("", actual.getMessage());
 assertNull(actual.getErrorCode());
 }

 // Test constructor with message and reason
 @Test
 public void testTokenMgrErrorWithMessageAndReason() {
 String message = "Test Error Message";
 int reason = 1;
 TokenMgrError actual = new TokenMgrError(message, reason);
 assertEquals(message, actual.getMessage());
 assertEquals(reason, actual.getErrorCode());
 }

 // Test constructor with boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar, int reason
 @Test
 public void testTokenMgrErrorWithAllArguments() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "testErrorAfter";
 char curChar = 'a';
 int reason = 2;
 String expectedMessage = "Lexical error at line 1, column 1. Encountered: <EOF>  (0), after : testErrorAfter";
 TokenMgrError actual = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 assertEquals(expectedMessage, actual.getMessage());
 assertEquals(reason, actual.getErrorCode());
 }

 // Test addEscapes method with normal characters
 @Test
 public void testAddEscapesWithNormalCharacters() {
 String input = "Hello World!";
 String expectedOutput = "Hello World!";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 // Test addEscapes method with special characters
 @Test
 public void testAddEscapesWithSpecialCharacters() {
 String input = "\0\b\t\n\f\r\"\'\\";
 String expectedOutput = "\\0\\b\\t\\n\\f\\r\\\"\\\'\\\\";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 // Test addEscapes method with non-printable characters
 @Test
 public void testAddEscapesWithNonPrintableCharacters() {
 String input = "\u0001\u007F";
 String expectedOutput = "\\u0001\\u007F";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 // Test LexicalError method with EOFSeen = false and valid error arguments
 @Test
 public void testLexicalErrorWithEOFSeenFalse() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "testErrorAfter";
 char curChar = 'a';
 int reason = 2;
 String expectedMessage = "Lexical error at line 1, column 1. Encountered: \"a\" (97), after : testErrorAfter";
 String actualMessage = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason).getMessage();
 assertEquals(expectedMessage, actualMessage);
 }

 // Test LexicalError method with EOFSeen = true and valid error arguments
 @Test
 public void testLexicalErrorWithEOFSeenTrue() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "testErrorAfter";
 char curChar = '\u0000';
 int reason = 2;
 String expectedMessage = "Lexical error at line 1, column 1. Encountered: <EOF> ";
 String actualMessage = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason).getMessage();
 assertEquals(expectedMessage, actualMessage);
 }

 // Test getMessage method
 @Test
 public void testGetMessage() {
 String message = "Test Error Message";
 int reason = 1;
 TokenMgrError error = new TokenMgrError(message, reason);
 String expectedMessage = message;
 String actualMessage = error.getMessage();
 assertEquals(expectedMessage, actualMessage);
 }

 // Test getErrorCode method
 @Test
 public void testGetErrorCode() {
 int reason = 1;
 TokenMgrError error = new TokenMgrError("", reason);
 int expectedErrorCode = reason;
 int actualErrorCode = error.getErrorCode();
 assertEquals(expectedErrorCode, actualErrorCode);
 }

}