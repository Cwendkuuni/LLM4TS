package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for TokenMgrError.
 */
public class TokenMgrErrorTest {

  @Test
  public void testAddEscapes() {
    String input = "abc\ndef\n012\b\t\n\r\f\"'\\\\";
    String expected = "abc\\nabcdef\n000b\t\n\r\n\r\n\f\\\"'\b\\\\";
    String output = TokenMgrError.addEscapes(input);
    assertEquals(expected, output);
  }

  @Test
  public void testLexicalError_EOFSeen() {
    boolean eofSeen = true;
    int lexState = 0;
    int errorLine = 1;
    int errorColumn = 1;
    String errorAfter = "abc";
    char curChar = '\0';
    String errorMessage = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
    String expected = "Lexical error at line 1, column 1.  Encountered: <EOF> ";
    assertEquals(expected, errorMessage);
  }

  @Test
  public void testLexicalError_NotEOF() {
    boolean eofSeen = false;
    int lexState = 0;
    int errorLine = 1;
    int errorColumn = 1;
    String errorAfter = "\"abcd\"";
    char curChar = 'e';
    String errorMessage = TokenMgrError.LexicalError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
    String expected = "Lexical error at line 1, column 1.  Encountered: \"" + "abcd" + "\" (" + (int)'e' + ")";
    assertEquals(expected, errorMessage);
  }

  @Test(expected = TokenMgrError.class)
  public void testConstructors_InvalidArguments() {
    TokenMgrError error = new TokenMgrError();
    TokenMgrError error1 = new TokenMgrError("", -1);
    TokenMgrError error2 = new TokenMgrError(true, 0, 1, 1, "", 'a', 0);
    TokenMgrError error3 = new TokenMgrError(false, 0, 1, 1, "", '\u0000', 1);
  }

  @Test
  public void testConstructors_ValidArguments() {
    String errorMessage = "Lexical error at line 1, column 1.  Encountered: <EOF> ";
    TokenMgrError error = new TokenMgrError(true, 0, 1, 1, "", '\0', 0);
    assertEquals(errorMessage, error.getMessage());

    errorMessage = "Lexical error at line 1, column 1.  Encountered: \"abcdef\" (90) after : \"ghijk\"";
    error = new TokenMgrError(false, 0, 1, 1, "ghijk", 'f', 1);
    assertEquals(errorMessage, error.getMessage());

    errorMessage = "Lexical error at line 1, column 1.  Encountered: <EOF> ";
    error = new TokenMgrError(true, 0, 1, 1, "", '\0', 2);
    assertEquals(errorMessage, error.getMessage());

    errorMessage = "Lexical error at line 1, column 1.  Encountered: \"\\u0061\\u0062\\u0063\" (97 98 99) after : \"\\u0065\\u006e\\u0063\"";
    error = new TokenMgrError(false, 0, 1, 1, "\\u0065\\u006e\\u0063", '\u0061', 3);
    assertEquals(errorMessage, error.getMessage());
  }

  @Test
  public void testGetMessage() {
    String errorMessage = "Lexical error at line 1, column 1.  Encountered: <EOF> ";
    TokenMgrError error = new TokenMgrError(true, 0, 1, 1, "", '\0', 0);
    assertEquals(errorMessage, error.getMessage());

    errorMessage = "Lexical error at line 1, column 1.  Encountered: \"abcdef\" (90 97 98 99 98 97) after : \"ghijk\"";
    error = new TokenMgrError(false, 0, 1, 1, "ghijk", 'f', 1);
    assertEquals(errorMessage, error.getMessage());
  }
}