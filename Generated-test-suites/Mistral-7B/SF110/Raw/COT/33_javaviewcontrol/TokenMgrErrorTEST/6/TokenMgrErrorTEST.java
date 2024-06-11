import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

 private TokenMgrError error;

 @Test
 public void testAddEscapes_NormalCharacter() {
 char input = 'a';
 String expected = "a";
 String output = TokenMgrError.addEscapes(Character.toString(input));
 assertEquals(expected, output);
 }

 @Test
 public void testAddEscapes_ControlCharacter() {
 char input = '\b';
 String expected = "\\b";
 String output = TokenMgrError.addEscapes(Character.toString(input));
 assertEquals(expected, output);
 }

 @Test
 public void testAddEscapes_SpecialCharacter() {
 char input = '\"';
 String expected = "\\\"";
 String output = TokenMgrError.addEscapes(Character.toString(input));
 assertEquals(expected, output);
 }

 @Test
 public void testAddEscapes_TwoByteCharacter() {
 char input = 'ç';
 String expected = "\\u00e7";
 String output = TokenMgrError.addEscapes(Character.toString(input));
 assertEquals(expected, output);
 }

 @Test
 public void testAddEscapes_MultiByteCharacter() {
 char input = '\u00A9';
 String expected = "\\u00A9";
 String output = TokenMgrError.addEscapes(Character.toString(input));
 assertEquals(expected, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEscapes_NullString() {
 String input = null;
 TokenMgrError exception = assertThrows(IllegalArgumentException.class, () -> TokenMgrError.addEscapes(input));
 String message = exception.getMessage();
 assertEquals("String cannot be null", message);
 }

 @Test
 public void testLexicalError_NormalCase() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "input";
 char curChar = 'a';
 int reason = TokenMgrError.LEXICAL_ERROR;
 error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expected = "Lexical error at line 1, column 1. Encountered: a";
 String output = error.getMessage();
 assertEquals(expected, output);
 }

 @Test
 public void testLexicalError_EOFSeenCase() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "";
 char curChar = '\u0000';
 int reason = TokenMgrError.LEXICAL_ERROR;
 error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String expected = "Lexical error at line 1, column 1. Encountered: <EOF> ";
 String output = error.getMessage();
 assertEquals(expected, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLexicalError_NullErrorAfter() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = null;
 char curChar = 'a';
 int reason = TokenMgrError.LEXICAL_ERROR;
 error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLexicalError_NegativeErrorLine() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = -1;
 int errorColumn = 1;
 String errorAfter = "input";
 char curChar = 'a';
 int reason = TokenMgrError.LEXICAL_ERROR;
 error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLexicalError_NegativeErrorColumn() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = -1;
 String errorAfter = "input";
 char curChar = 'a';
 int reason = TokenMgrError.LEXICAL_ERROR;
 error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 }
}