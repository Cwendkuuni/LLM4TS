package com.werken.saxpath;

import org.junit.Assert;
import org.junit.Test;

public class XPathLexerTest {

 private XPathLexer lexer;

 @Test
 public void testNextToken_EmptyString() {
  lexer = new XPathLexer("");
  Assert.assertEquals(new Token(-1, "", 0, 0), lexer.nextToken());
 }

 @Test
 public void testNextToken_Whitespace() {
  lexer = new XPathLexer("   /a[b]   ");
  Assert.assertEquals(new Token(-2, "", 0, 0), lexer.nextToken());
  Assert.assertEquals(new Token(11, "   /a[b]", 0, 1), lexer.nextToken());
 }

 @Test
 public void testNextToken_SingleCharacter() {
  lexer = new XPathLexer("/a");
  Assert.assertEquals(new Token(11, "/a", 0, 1), lexer.nextToken());
 }

 @Test
 public void testNextToken_Number() {
  lexer = new XPathLexer("/a[1]");
  Assert.assertEquals(new Token(11, "/a[1]", 0, 2), lexer.nextToken());
  Assert.assertEquals(new Token(31, "", 2, 3), lexer.nextToken());
 }

 @Test
 public void testNextToken_Identifier() {
  lexer = new XPathLexer("/a[id]");
  Assert.assertEquals(new Token(11, "/a[id]", 0, 4), lexer.nextToken());
  Assert.assertEquals(new Token(15, "", 4, 5), lexer.nextToken());
 }

 @Test
 public void testNextToken_Operator() {
  lexer = new XPathLexer("/a[id='b' and c=2]");
  Assert.assertEquals(new Token(11, "/a[id='b' and c=2]", 0, 1), lexer.nextToken());
  Assert.assertEquals(new Token(7, "", 21, 23), lexer.nextToken());
  Assert.assertEquals(new Token(5, "", 25, 26), lexer.nextToken());
 }

 @Test
 public void testNextToken_RelationalOperator() {
  lexer = new XPathLexer("/a[b < c]");
  Assert.assertEquals(new Token(11, "/a[b < c]", 0, 1), lexer.nextToken());
  Assert.assertEquals(new Token(7, "", 6, 8), lexer.nextToken());
  Assert.assertEquals(new Token(9, "", 9, 10), lexer.nextToken());
 }

 @Test
 public void testNextToken_Dollar() {
  lexer = new XPathLexer("/a[$foo]");
  Assert.assertEquals(new Token(11, "/a[$foo]", 0, 1), lexer.nextToken());
  Assert.assertEquals(new Token(26, "", 3, 4), lexer.nextToken());
  Assert.assertEquals(new Token(15, "", 4, 5), lexer.nextToken());
 }

 @Test
 public void testNextToken_Literal() {
  lexer = new XPathLexer("/a[starts-with(string(.),'foo')]");
  Assert.assertEquals(new Token(11, "/a[starts-with(string(.),'foo')]", 0, 1), lexer.nextToken());
  Assert.assertEquals(new Token(27, "", 5, 11), lexer.nextToken());
  Assert.assertEquals(new Token(15, "", 11, 12), lexer.nextToken());
 }

 @Test
 public void testNextToken_Comment() {
  lexer = new XPathLexer("/a[comment()='-- this is a comment']");
  Assert.assertEquals(new Token(11, "/a[comment()='-- this is a comment']", 0, 1), lexer.nextToken());
  Assert.assertEquals(new Token(-2, "", 38, 39), lexer.nextToken());
  Assert.assertEquals(new Token(11, "/a[comment()]", 0, 42), lexer.nextToken());
 }

 @Test(expected = IllegalStateException.class)
 public void testNextToken_InvalidCharacter() {
  lexer = new XPathLexer("/a[1+]");
  lexer.nextToken(); // consume "["
  lexer.nextToken(); // consume "1"
  lexer.nextToken(); // consume "+"
  lexer.nextToken(); // expected to throw an exception
 }

 @Test(expected = IllegalStateException.class)
 public void testNextToken_MissingRightParenthesis() {
  lexer = new XPathLexer("/a[(id='b']");
  lexer.nextToken(); // consume "/a"
  lexer.nextToken(); // consume "(id='b'"
  lexer.nextToken(); // consume "id"
  lexer.nextToken(); // consume "'b'"
  lexer.nextToken(); // expected to throw an exception
 }
}