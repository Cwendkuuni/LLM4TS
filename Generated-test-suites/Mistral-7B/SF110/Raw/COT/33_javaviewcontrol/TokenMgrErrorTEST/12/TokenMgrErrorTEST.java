import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

 private TokenMgrError tokenMgrError;

 @Test
 public void testAddEscapes() {
 String str = "Hello\nWorld\t\f\b\r\f\t\"'\\ \b\t\n\r\f\f\n";
 String expected = "Hello\\nWorld\\t\\f\\b\\r\\f\\t\\\"'\\\\ \\b\\t\\n\\r\\f\\f\\n";
 tokenMgrError = new TokenMgrError();
 String result = tokenMgrError.addEscapes(str);
 assertEquals(expected, result);
 }

 @Test
 public void testLexicalErrorEmpty() {
 tokenMgrError = new TokenMgrError(false, 0, 0, 0, "", ' ', 0);
 String expected = "Lexical error at line 0, column 0. ";
 String result = tokenMgrError.getMessage();
 assertEquals(expected, result);
 }

 @Test
 public void testLexicalErrorWithValues() {
 tokenMgrError = new TokenMgrError(true, 0, 1, 10, "abc", 'd', 1);
 String expected = "Lexical error at line 1, column 10. Encountered: <EOF> \"d\" (100, 1), after : \"abc\"";
 String result = tokenMgrError.getMessage();
 assertEquals(expected, result);
 }

 @Test(expected = TokenMgrError.class)
 public void testLexicalErrorWithNullMessage() {
 tokenMgrError = new TokenMgrError(true, 0, 1, 10, null, 'd', 1);
 }

 @Test
 public void testLexicalErrorWithNegativeErrorLine() {
 tokenMgrError = new TokenMgrError(true, 0, -1, 10, "abc", 'd', 1);
 String expected = "Lexical error at line -1, column 10. Encountered: <EOF> \"d\" (100, 1), after : \"abc\"";
 String result = tokenMgrError.getMessage();
 assertEquals(expected, result);
 }

 @Test
 public void testLexicalErrorWithNegativeErrorColumn() {
 tokenMgrError = new TokenMgrError(true, 0, 1, -1, "abc", 'd', 1);
 String expected = "Lexical error at line 1, column -1. Encountered: <EOF> \"d\" (100, -1), after : \"abc\"";
 String result = tokenMgrError.getMessage();
 assertEquals(expected, result);
 }

 @Test
 public void testLexicalErrorWithNegativeReason() {
 tokenMgrError = new TokenMgrError(true, 0, 1, 10, "abc", 'd', -1);
 String expected = "Lexical error at line 1, column 10. Encountered: <EOF> \"d\" (100, 1), after : \"abc\"";
 String result = tokenMgrError.getMessage();
 assertEquals(expected, result);
 }

 @Test
 public void testLexicalErrorWithNullCurChar() {
 tokenMgrError = new TokenMgrError(true, 0, 1, 10, "abc", (char)0, 1);
 String expected = "Lexical error at line 1, column 10. Encountered: <EOF>  after : \"abc\"";
 String result = tokenMgrError.getMessage();
 assertEquals(expected, result);
 }
}