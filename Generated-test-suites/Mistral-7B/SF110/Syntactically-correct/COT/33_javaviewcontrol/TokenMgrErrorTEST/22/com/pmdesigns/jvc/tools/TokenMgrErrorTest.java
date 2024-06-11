import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TokenMgrErrorTest {

 private TokenMgrError tokenMgrError;

 @Test
 public void testAddEscapes() {
 String input = "Hello\tWorld\nWith\nBackslash\\AndQuotes\"'";
 String expectedOutput = "Hello\\tWorld\\nWith\\nBackslash\\\\AndQuotes\\\"'";
 tokenMgrError = new TokenMgrError();
 String output = tokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testLexicalErrorWithEOFSeen() {
 boolean eofSeen = true;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 5;
 String errorAfter = "identifier";
 char curChar = ';' ;
 int reason = TokenMgrError.LEXICAL_ERROR;
 tokenMgrError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String message = tokenMgrError.getMessage();
 String expectedMessage = "Lexical error at line 1, column 5. Encountered: <EOF> ";
 assertEquals(expectedMessage, message.substring(0, expectedMessage.length() + message.lastIndexOf(":", message.length() - 1)));
 }

 @Test
 public void testLexicalErrorWithoutEOFSeen() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 2;
 int errorColumn = 7;
 String errorAfter = "/";
 char curChar = '/' ;
 int reason = TokenMgrError.LEXICAL_ERROR;
 tokenMgrError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String message = tokenMgrError.getMessage();
 String expectedMessage = "Lexical error at line 2, column 7. Encountered: \"/\" (2F) ";
 assertEquals(expectedMessage, message);
 }

 @Test(expected = TokenMgrError.class)
 public void testLexicalErrorWithInvalidCharacter() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 3;
 int errorColumn = 10;
 String errorAfter = "";
 char curChar = '\u0001'; // Control Character
 int reason = TokenMgrError.LEXICAL_ERROR;
 tokenMgrError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 }

 @Test
 public void testTokenMgrErrorWithReason() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 4;
 int errorColumn = 13;
 String errorAfter = "TokenName";
 char curChar = '(';
 int reason = TokenMgrError.INVALID_LEXICAL_STATE;
 tokenMgrError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 String message = tokenMgrError.getMessage();
 String expectedMessage = "Lexical error at line 4, column 13. ";
 assertEquals(expectedMessage, message.substring(0, expectedMessage.length()));
 assertEquals(reason, tokenMgrError.getErrorCode());
 }

 @Test
 public void testTokenMgrErrorWithCustomMessage() {
 boolean eofSeen = false;
 int lexState = 0;
 int errorLine = 5;
 int errorColumn = 15;
 String errorAfter = "";
 char curChar = ';' ;
 String customMessage = "Unexpected End of File";
 int reason = TokenMgrError.LEXICAL_ERROR;
 tokenMgrError = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 tokenMgrError.setMessage(customMessage);
 String expectedMessage = "Unexpected End of File";
 assertEquals(expectedMessage, tokenMgrError.getMessage());
 }

}