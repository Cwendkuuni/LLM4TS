package com.pmdesigns.jvc.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TokenMgrErrorTest {

 // Test constructor with no arguments
 @Test
 public void testEmptyConstructor() {
 TokenMgrError error = new TokenMgrError();
 assertNotNull(error);
 }

 // Test constructor with message and reason
 @Test
 public void testConstructorWithMessageAndReason() {
 String message = "Test message";
 int reason = 5;
 TokenMgrError error = new TokenMgrError(message, reason);
 assertNotNull(error);
 assertEquals(message, error.getMessage());
 assertEquals(reason, error.errorCode);
 }

 // Test constructor with all arguments
 @Test
 public void testConstructorWithAllArguments() {
 boolean eofSeen = true;
 int lexState = 1;
 int errorLine = 10;
 int errorColumn = 20;
 String errorAfter = "errorAfter";
 char curChar = 'a';
 int reason = 7;
 TokenMgrError error = new TokenMgrError(eofSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
 assertNotNull(error);
 String expectedMessage = "Lexical error at line 10, column 20. Encountered: \"a\" (97), after : \"errorAfter\"";
 assertEquals(expectedMessage, error.getMessage());
 assertEquals(reason, error.errorCode);
 }

 // Test addEscapes method with empty string
 @Test
 public void testAddEscapesWithEmptyString() {
 String input = "";
 String expectedOutput = "";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 // Test addEscapes method with single character
 @Test
 public void testAddEscapesWithSingleCharacter() {
 char character = 'a';
 String input = Character.toString(character);
 String expectedOutput = "\\" + Character.toString(character.codePointAt(0));
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 // Test addEscapes method with multiple characters
 @Test
 public void testAddEscapesWithMultipleCharacters() {
 String input = "hello world";
 String expectedOutput = "hello world";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

 // Test addEscapes method with special characters
 @Test
 public void testAddEscapesWithSpecialCharacters() {
 String input = "\ntest\r\f\bhello world";
 String expectedOutput = "\\n test\\r \\f \\bhello world";
 String output = TokenMgrError.addEscapes(input);
 assertEquals(expectedOutput, output);
 }

}