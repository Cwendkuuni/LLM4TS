package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

 private XPathLexer lexer;

 @Test
 public void testNextToken_EmptyXPath() {
     lexer = new XPathLexer("");
     Token token = lexer.nextToken();
     assertEquals(-2, token.getTokenType());
 }

 @Test
 public void testNextToken_Whitespace() {
     lexer = new XPathLexer("   xpath/foo[bar]   ");
     Token token = lexer.nextToken();
     assertEquals(-2, token.getTokenType());
 }

 @Test
 public void testNextToken_Identifier() {
     lexer = new XPathLexer("xpath/foo[bar]");
     Token token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     assertEquals("xpath", token.getText());
 }

 @Test
 public void testNextToken_OperatorName() {
     lexer = new XPathLexer("xpath/foo[bar]");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(28, token.getTokenType());
 }

 @Test
 public void testNextToken_RelationalOperator() {
     lexer = new XPathLexer("xpath/foo[bar < baz]");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(7, token.getTokenType());
 }

 @Test
 public void testNextToken_Equals() {
     lexer = new XPathLexer("xpath/foo[bar = baz]");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(21, token.getTokenType());
 }

 @Test
 public void testNextToken_NotEquals() {
     lexer = new XPathLexer("xpath/foo[bar != baz]");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(22, token.getTokenType());
 }

 @Test
 public void testNextToken_Number() {
     lexer = new XPathLexer("xpath/foo[bar = 123]");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(31, token.getTokenType());
 }

 @Test
 public void testNextToken_NumberWithDecimal() {
     lexer = new XPathLexer("xpath/foo[bar = 123.45]");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(30, token.getTokenType());
 }

 @Test
 public void testNextToken_Dot() {
     lexer = new XPathLexer("xpath/foo[bar . baz]");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(14, token.getTokenType());
 }

 @Test
 public void testNextToken_Dollar() {
     lexer = new XPathLexer("xpath/foo[$bar]");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(26, token.getTokenType());
 }

 @Test
 public void testNextToken_LeftParen() {
     lexer = new XPathLexer("xpath/foo[(bar)]");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(1, token.getTokenType());
 }

 @Test
 public void testNextToken_RightParen() {
     lexer = new XPathLexer("xpath/foo[(bar)]");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(2, token.getTokenType());
 }

 @Test
 public void testNextToken_LeftBracket() {
     lexer = new XPathLexer("xpath/foo[bar[baz]]");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(3, token.getTokenType());
 }

 @Test
 public void testNextToken_RightBracket() {
     lexer = new XpathLexer("xpath/foo[bar[baz]]");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(4, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(3, token.getTokenType());
 }

 @Test
 public void testNextToken_Comma() {
     lexer = new XPathLexer("xpath/foo[(bar), (baz)]");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(32, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
 }

 @Test
 public void testNextToken_Slashes() {
     lexer = new XPathLexer("xpath/foo/bar");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(11, token.getTokenType());
 }

 @Test
 public void testNextToken_SlashesWithDot() {
     lexer = new XPathLexer("xpath/foo/bar/..");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(11, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(14, token.getTokenType());
 }

 @Test
 public void testNextToken_InvalidCharacter() {
     lexer = new XPathLexer("xpath/foo[bar = 123@]");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(13, token.getTokenType());
 }

 @Test
 public void testNextToken_InvalidSequence() {
     lexer = new XPathLexer("xpath/foo[bar/baz]");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(-2, token.getTokenType());
 }

 @Test
 public void testNextToken_InvalidSequence2() {
     lexer = new XPathLexer("xpath/foo[bar = baz]//");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(21, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(2, token.getTokenType());
 }

 @Test
 public void testNextToken_InvalidSequence3() {
     lexer = new XPathLexer("xpath/foo[bar/baz]//");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(-2, token.getTokenType());
 }

 @Test
 public void testNextToken_InvalidSequence4() {
     lexer = new XPathLexer("xpath/foo[bar/baz]//");
     token = lexer.nextToken();
     assertEquals(15, token.getTokenType());
     token = lexer.nextToken();
     assertEquals(-2, token.getTokenType());
 }
}