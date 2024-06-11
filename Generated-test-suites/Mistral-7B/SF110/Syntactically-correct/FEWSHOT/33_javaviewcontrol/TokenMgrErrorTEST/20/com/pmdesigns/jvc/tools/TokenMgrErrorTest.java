package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TokenMgrErrorTest {

 @Test
 public void testAddEscapes() {
 String input = "Hello\tWorld\n\rWith \"backslash\\ and single'\'quote.";
 String expectedOutput = "Hello\\tWorld\\n\\rWith \\" + "backslash\\" + " and single\\'quote.";
 String result = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testLexicalError() {
 String errorMessage = TokenMgrError.LexicalError(true, 0, 1, 10, "", ';');
 String expectedOutput = "Lexical error at line 1, column 10. Encountered: <EOF> (0, ) after : ";
 assertEquals(expectedOutput, errorMessage);
 }

 @Test
 public void testConstructors() {
 TokenMgrError error1 = new TokenMgrError();
 assertNotNull(error1.getMessage());
 assertNull(error1.errorCode);

 TokenMgrError error2 = new TokenMgrError("Message", 1);
 assertEquals("Message", error2.getMessage());
 assertEquals(1, error2.errorCode);

 TokenMgrError error3 = new TokenMgrError(true, 0, 1, 10, "errorAfter", ';', 1);
 String expectedErrorMessage = "Lexical error at line 1, column 10. Encountered: \"';\" (59), ";
 assertEquals(expectedErrorMessage, error3.getMessage());
 assertEquals(1, error3.errorCode);
 }
}