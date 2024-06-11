import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

 private TokenMgrError tokenMgrError;

 @Test
 public void testAddEscapes() {
 String input = "[ab\tcdef\\\"']";
 String expectedOutput = "\\[ab\\t\\tcdef\\\\\"'\\]";
 tokenMgrError = new TokenMgrError();
 String output = tokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testLexicalErrorWithEOFSeen() {
 tokenMgrError = new TokenMgrError(true, 0, 1, 1, "", 0, TokenMgrError.LEXICAL_ERROR);
 String expectedOutput = "Lexical error at line 1, column 1.  Encountered: <EOF> ";
 String output = tokenMgrError.getMessage();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testLexicalErrorWithoutEOFSeen() {
 char invalidChar = '\u0001';
 tokenMgrError = new TokenMgrError(false, 0, 1, 1, "", invalidChar, TokenMgrError.LEXICAL_ERROR);
 String expectedOutput = "Lexical error at line 1, column 1.  Encountered: \u0001 (1)";
 String output = tokenMgrError.getMessage();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testInvalidLexicalState() {
 tokenMgrError = new TokenMgrError(false, -1, 1, 1, "", 'a', TokenMgrError.INVALID_LEXICAL_STATE);
 String expectedOutput = "Invalid lexical state";
 String output = tokenMgrError.getMessage();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testLoopDetected() {
 tokenMgrError = new TokenMgrError(false, 0, 1, 1, "", 'a', TokenMgrError.LOOP_DETECTED);
 String expectedOutput = "Loop detected";
 String output = tokenMgrError.getMessage();
 assertEquals(expectedOutput, output);
 }
}