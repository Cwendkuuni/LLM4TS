import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenMgrErrorTest {

 private static final String ADD_ESCAPES_INPUT = "abc\nabcd\t123\nefg\rfhij\rklm\nopqr\bstu\ftuv\bwxyz\n";

 @Test
 public void testAddEscapes_NormalCase() {
 String result = TokenMgrError.addEscapes(ADD_ESCAPES_INPUT);
 String expected = "abc\\nabcd\\t123\\nefg\\rfhij\\rklm\\nopqr\\bstu\\ftuv\\bwxyz\\n";
 assertEquals(expected, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddEscapes_StringBufferOperationNotAllowed() {
 StringBuffer stringBuffer = new StringBuffer();
 TokenMgrError.addEscapes(stringBuffer);
 }

 @Test
 public void testLexicalError_NormalCase() {
 String errorMessage = TokenMgrError.LexicalError(false, 0, 1, 1, "abc", 'a');
 String expected = "Lexical error at line 1, column 1. Encountered: \"a\" (97, ) after : \"abc\"";
 assertEquals(expected, errorMessage);
 }

 @Test
 public void testLexicalError_EOFSeenCase() {
 String errorMessage = TokenMgrError.LexicalError(true, 0, 1, 1, "", (char)0);
 String expected = "Lexical error at line 1, column 1. Encountered: <EOF> after : \"\"";
 assertEquals(expected, errorMessage);
 }

 @Test
 public void testLexicalError_NegativeErrorLineCase() {
 String errorMessage = TokenMgrError.LexicalError(false, 0, -1, 1, "abc", 'a');
 String expected = "Lexical error at line -1, column 1. Encountered: \"a\" (97, ) after : \"abc\"";
 assertTrue(errorMessage.contains(expected));
 }

 @Test
 public void testLexicalError_NegativeErrorColumnCase() {
 String errorMessage = TokenMgrError.LexicalError(false, 0, 1, -1, "abc", 'a');
 String expected = "Lexical error at line 1, column -1. Encountered: \"a\" (97, ) after : \"abc\"";
 assertTrue(errorMessage.contains(expected));
 }

 @Test
 public void testLexicalError_NullErrorAfterCase() {
 String errorMessage = TokenMgrError.LexicalError(false, 0, 1, 1, null, 'a');
 String expected = "Lexical error at line 1, column 1. Encountered: \"a\" (97, ) after : null";
 assertEquals(expected, errorMessage);
 }

 @Test(expected = NullPointerException.class)
 public void testLexicalError_NullCurCharCase() {
 String errorMessage = TokenMgrError.LexicalError(false, 0, 1, 1, "abc", null, 0);
 }
}