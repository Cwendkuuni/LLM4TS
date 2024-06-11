import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

 private TokenMgrError tokenMgrError;

 @Test
 public void testAddEscapes() {
 String str = "\0\b\t\n\f\r\"'\\\\abcdefghijklmnopqrstuvwxyz";
 String expected = "\\0\\b\\t\\n\\f\\r\\\"\\'\\\\\\u000ab\\u000c\\u000e\\f\\u000f\\r\\u0012\\u0013\\u0014\\u0015\\u0016\\u0017\\u0018\\u0019\\u001a\\u001b\\u001c\\u001d\\u001e\\u001f";
 String result = TokenMgrError.addEscapes(str);
 assertEquals(expected, result);
 }

 @Test
 public void testLexicalErrorDefaultConstructor() {
 tokenMgrError = new TokenMgrError();
 assertNull(tokenMgrError.getMessage());
 assertFalse(tokenMgrError.isInstanceOf(TokenMgrError.class));
 }

 @Test
 public void testLexicalErrorWithParameters() {
 tokenMgrError = new TokenMgrError(true, 0, 1, 1, "", ';', 0);
 String expected = "Lexical error at line 1, column 1.  Encountered: <EOF> ";
 String result = tokenMgrError.getMessage();
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLexicalErrorNegativeErrorLine() {
 tokenMgrError = new TokenMgrError(true, 0, -1, 1, "", ';', 0);
 }

 @Test
 public void testLexicalErrorNegativeErrorColumn() {
 tokenMgrError = new TokenMgrError(true, 0, 1, -1, "", ';', 0);
 String expected = "Lexical error at line 1, column 0.  Encountered: <EOF> ";
 String result = tokenMgrError.getMessage();
 assertEquals(expected, result);
 }

 @Test
 public void testLexicalErrorNullErrorAfter() {
 tokenMgrError = new TokenMgrError(true, 0, 1, 1, null, ';', 0);
 String expected = "Lexical error at line 1, column 1.  Encountered: <EOF> ";
 String result = tokenMgrError.getMessage();
 assertEquals(expected, result);
 }

 @Test
 public void testLexicalErrorInvalidCharacter() {
 tokenMgrError = new TokenMgrError(true, 0, 1, 1, "", '\u0000', 0);
 String expected = "Lexical error at line 1, column 1.  Encountered: \u0000 ";
 String result = tokenMgrError.getMessage();
 assertEquals(expected, result);
 }

 @Test
 public void testLexicalErrorWithAllParameters() {
 tokenMgrError = new TokenMgrError(true, 0, 1, 1, "\"invalid_token\"", ';', 3, "Invalid token: \"invalid_token\"");
 String expected = "Lexical error at line 1, column 1.  Encountered: \"invalid_token\"";
 String result = tokenMgrError.getMessage();
 assertEquals(expected, result);
 }
}