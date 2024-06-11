import org.junit.Test; import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {

 private static final String ADD_ESCAPES_EXPECTED_RESULT = "\\0\\b\\t\\n\\f\\r\\\"\\'\\\\";

 @Test
 public void testAddEscapes_NormalCase() {
 String str = "abcdefghijklmnopqrstuvwxyz";
 String expectedResult = ADD_ESCAPES_EXPECTED_RESULT + str;
 String actualResult = TokenMgrError.addEscapes(str);
 assertEquals(expectedResult, actualResult);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAddEscapes_NullInput() {
 String str = null;
 String actualResult = TokenMgrError.addEscapes(str);
 }

 @Test
 public void testAddEscapes_SpecialChars() {
 String str = "\0\b\t\n\f\r\"\'\\";
 String expectedResult = ADD_ESCAPES_EXPECTED_RESULT + str;
 String actualResult = TokenMgrError.addEscapes(str);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testAddEscapes_NonPrintableChars() {
 char ch = 0x00;
 String str = Character.toString(ch);
 String expectedResult = ADD_ESCAPES_EXPECTED_RESULT + "\\u0000";
 String actualResult = TokenMgrError.addEscapes(str);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testLexicalError_NormalCase() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "identifier";
 char curChar = '=';
 String expectedResult = "Lexical error at line 1, column 1. Encountered: \"=\" (61), after : \"identifier\"";
 String actualResult = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, 0);
 assertEquals(expectedResult, actualResult);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testLexicalError_NullInput() {
 String errorAfter = null;
 char curChar = '=';
 int reason = 0;
 TokenMgrError error = new TokenMgrError(true, 1, 1, 1, errorAfter, curChar, reason);
 String actualResult = error.getMessage();
 }

 @Test (expected = IllegalArgumentException.class)
 public void testLexicalError_EOFSeen_Negative() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "identifier";
 char curChar = '=';
 int reason = -1;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testLexicalError_ErrorLine_Negative() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = -1;
 int errorColumn = 1;
 String errorAfter = "identifier";
 char curChar = '=';
 int reason = 0;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testLexicalError_ErrorColumn_Negative() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = -1;
 String errorAfter = "identifier";
 char curChar = '=';
 int reason = 0;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testLexicalError_ErrorAfter_Null() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 char curChar = '=';
 int reason = 0;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, null, curChar, reason);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testLexicalError_CurChar_Invalid() {
 boolean eofSeen = false;
 int lexState = 1;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "identifier";
 char curChar = '\u0000'; // Invalid Unicode character
 int reason = 0;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 }
}