package com.google.javascript.rhino.test;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenStreamTest {
@Test
public void testIsKeyword() {
assertTrue(TokenStream.isKeyword("if"));
assertTrue(TokenStream.isKeyword("else"));
assertTrue(TokenStream.isKeyword("while"));
// Add more test cases for other keywords
assertFalse(TokenStream.isKeyword("myVariable"));
}

@Test
public void testLongKeyword() {
assertTrue(TokenStream.isKeyword("switchstatement"));
}

@Test
public void testKeywordWithSpecialCharacter() {
assertTrue(TokenStream.isKeyword("for_each"));
}

@Test(expected = IllegalArgumentException.class)
public void testEmptyString() {
String emptyString = "";
try {
TokenStream.isKeyword(emptyString);
} catch (IllegalArgumentException e) {
// expected exception
}
}

@Test(expected = IllegalArgumentException.class)
public void testNullInput() {
String nullInput = null;
try {
TokenStream.isKeyword(nullInput);
} catch (IllegalArgumentException e) {
// expected exception
}
}

@Test(expected = IllegalArgumentException.class)
public void testNonPrintableCharacter() {
String nonPrintableCharacter = "\u0000"; // replace with actual non-printable character
try {
TokenStream.isKeyword(nonPrintableCharacter);
} catch (IllegalArgumentException e) {
// expected exception
}
}
}