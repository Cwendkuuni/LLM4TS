import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

 private TokenMgrError tokenMgrError;

 @Test
 public void testAddEscapes() {
 String input = "Hello\nWorld";
 String expectedOutput = "Hello\\nWorld";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testLexicalError_EOFSeen_true() {
 boolean eofSeen = true;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "";
 char curChar = '\0';
 int reason = TokenMgrError.LEXICAL_ERROR;
 String expectedOutput = "Lexical error at line 1, column 1. ";
 tokenMgrError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String actualOutput = tokenMgrError.getMessage();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testLexicalError_EOFSeen_false() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "abc";
 char curChar = 'x';
 int reason = TokenMgrError.LEXICAL_ERROR;
 String expectedOutput = "Lexical error at line 1, column 1. Encountered: \"x\" ";
 tokenMgrError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String actualOutput = tokenMgrError.getMessage();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testLexicalError_reason_other() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "abc";
 char curChar = 'x';
 int reason = 123;
 String expectedOutput = "Lexical error at line 1, column 1. ";
 tokenMgrError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String actualOutput = tokenMgrError.getMessage();
 assertEquals(expectedOutput, actualOutput);
 assertEquals("123", tokenMgrError.errorCode + "");
 }
}