package com.werken.saxpath;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

 private XPathLexer lexer;

 @Before
 public void setUp() {
 lexer = new XPathLexer("");
 }

 @Test
 public void testEmptyString() {
 Token token = lexer.nextToken();
 assertEquals(-1, token.getTokenType());
 }

 @Test
 public void testSimpleIdentifier() {
 lexer.setXPath("test");
 Token token = lexer.nextToken();
 assertEquals(15, token.getTokenType());
 assertEquals("test", token.getValue());
 }

 @Test
 public void testSimpleOperator() {
 lexer.setXPath("test and test");
 Token token = lexer.nextToken();
 assertEquals(28, token.getTokenType());
 }

 @Test
 public void testSimpleNumber() {
 lexer.setXPath("123");
 Token token = lexer.nextToken();
 assertEquals(31, token.getTokenType());
 assertEquals("123", token.getValue());
 }

 @Test
 public void testSimpleWhitespace() {
 lexer.setXPath(" test ");
 lexer.nextToken(); // consume 't'
 Token token = lexer.nextToken();
 assertEquals(-2, token.getTokenType());
 }

 @Test
 public void testComplexXPath() {
 lexer.setXpath("//test[@class='testClass']/test[1]");
 // test for identifier
 Token identifier1 = lexer.nextToken();
 assertEquals(15, identifier1.getTokenType());
 assertEquals("//", identifier1.getValue());
 // test for at sign
 Token atSign1 = lexer.nextToken();
 assertEquals(16, atSign1.getTokenType());
 // test for identifier
 Token identifier2 = lexer.nextToken();
 assertEquals(15, identifier2.getTokenType());
 assertEquals("test", identifier2.getValue());
 // test for left bracket
 Token leftBracket1 = lexer.nextToken();
 assertEquals(3, leftBracket1.getTokenType());
 // test for equals
 Token equals1 = lexer.nextToken();
 assertEquals(21, equals1.getTokenType());
 // test for identifier
 Token identifier3 = lexer.nextToken();
 assertEquals(15, identifier3.getTokenType());
 assertEquals("class", identifier3.getValue());
 // test for single quote
 Token singleQuote1 = lexer.nextToken();
 assertEquals(27, singleQuote1.getTokenType());
 // test for identifier
 Token identifier4 = lexer.nextToken();
 assertEquals(15, identifier4.getTokenType());
 assertEquals("testClass", identifier4.getValue());
 // test for right bracket
 Token rightBracket1 = lexer.nextToken();
 assertEquals(4, rightBracket1.getTokenType());
 // test for slash
 Token slash1 = lexer.nextToken();
 assertEquals(11, slash1.getTokenType());
 // test for identifier
 Token identifier5 = lexer.nextToken();
 assertEquals(15, identifier5.getTokenType());
 assertEquals("test", identifier5.getValue());
 // test for left bracket
 Token leftBracket2 = lexer.nextToken();
 assertEquals(3, leftBracket2.getTokenType());
 // test for number
 Token number1 = lexer.nextToken();
 assertEquals(31, number1.getTokenType());
 assertEquals("1", number1.getValue());
 // test for right bracket
 Token rightBracket2 = lexer.nextToken();
 assertEquals(4, rightBracket2.getTokenType());
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidXPath() {
 lexer.setXPath("test%");
 lexer.nextToken();
 }
}