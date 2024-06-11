import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

 private TokenMgrError error;

 @Test
 public void testAddEscapes() {
 String input = "abcd\u0001ef\u0123gh\nikl\b\tmno\r\fpq\"r'st\\xyz";
 String expected = "abcd\\u0001ef\\u0123gh\\nikl\\b\\tmno\\r\\fpq\\\"r'st\\\\xyz";
 error = new TokenMgrError();
 String result = error.addEscapes(input);
 assertEquals(expected, result);
 }

 @Test
 public void testLexicalError_EOFSeen_true() {
 boolean EOFSeen = true;
 int lexState = 1;
 int errorLine = 3;
 int errorColumn = 5;
 String errorAfter = "if";
 char curChar = ';' ;
 error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
 String message = error.getMessage();
 String expected = "Lexical error at line 3, column 5.  Encountered: <EOF> ";
 assertEquals(expected, message);
 }

 @Test
 public void testLexicalError_EOFSeen_false() {
 boolean EOFSeen = false;
 int lexState = 2;
 int errorLine = 10;
 int errorColumn = 15;
 String errorAfter = "else";
 char curChar = '(';
 error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
 String message = error.getMessage();
 String expected = "Lexical error at line 10, column 15.  Encountered: \"(\" ";
 assertEquals(expected, message);
 }

 @Test(expected = TokenMgrError.class)
 public void testLexicalError_InvalidCharacter() {
 boolean EOFSeen = false;
 int lexState = 3;
 int errorLine = 7;
 int errorColumn = 2;
 String errorAfter = "for";
 char curChar = '\u0001'; // Control Character
 error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, TokenMgrError.LEXICAL_ERROR);
 }

 @Test
 public void testTokenMgrErrorConstructor() {
 String message = "There was an error";
 int reason = 123;
 error = new TokenMgrError(message, reason);
 String actualMessage = error.getMessage();
 assertEquals(message, actualMessage);
 assertEquals(reason, error.getErrorCode());
 }

 @Test
 public void testTokenMgrErrorConstructor_WithLexicalErrorParameters() {
 boolean EOFSeen = true;
 int lexState = 1;
 int errorLine = 5;
 int errorColumn = 12;
 String errorAfter = "while";
 char curChar = '(';
 int reason = 567;
 error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String actualMessage = error.getMessage();
 String expected = "Lexical error at line 5, column 12.  Encountered: \"(\" ";
 assertEquals(expected, actualMessage);
 }

}