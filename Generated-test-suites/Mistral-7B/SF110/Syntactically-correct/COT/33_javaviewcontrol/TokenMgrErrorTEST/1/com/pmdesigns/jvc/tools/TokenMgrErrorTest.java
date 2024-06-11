import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

 private TokenMgrError err;

 @Test
 public void testAddEscapes_NormalCase() {
 String input = "abcdef";
 String expected = "abcdef";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expected, output);
 }

 @Test
 public void testAddEscapes_SpecialCharacters() {
 String input = "\0\b\t\n\f\r\"\'\\";
 String expected = "\\0\\b\\t\\n\\f\\r\\\"\\'\\\\";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expected, output);
 }

 @Test
 public void testAddEscapes_CharOutsidePrintableRange() {
 char charToEscape = (char) 80;
 String input = Character.toString(charToEscape);
 String expected = "\\u0050";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expected, output);
 }

 @Test(expected = NullPointerException.class)
 public void testLexicalError_NullErrorMessage() {
 boolean EOFSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = null;
 char curChar = 'a';
 int reason = 0;
 err = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 }

 @Test
 public void testLexicalError_ValidParameters() {
 boolean EOFSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "some error after";
 char curChar = 'a';
 int reason = 0;
 String expectedMessage = "Lexical error at line 1, column 1. Encountered: \"a\" (97), after : \"some error after\"";
 err = new TokenMgrError(EFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 assertEquals(expectedMessage, err.getMessage());
 }

 @Test
 public void testLexicalError_EOFSeen_True() {
 boolean EOFSeen = true;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "";
 char curChar = (char) 0;
 int reason = 0;
 String expectedMessage = "Lexical error at line 1, column 1. Encountered: <EOF> ";
 err = new TokenMgrError(EFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 assertEquals(expectedMessage, err.getMessage());
 }

 @Test
 public void testLexicalError_Reason_Valid() {
 boolean EOFSeen = false;
 int lexState = 0;
 int errorLine = 1;
 int errorColumn = 1;
 String errorAfter = "some error after";
 char curChar = 'a';
 int reason = 1;
 String expectedMessage = "Lexical error at line 1, column 1. Encountered: \"a\" (97), after : some error after";
 err = new TokenMgrError(EFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 assertEquals(expectedMessage, err.getMessage());
 }
}