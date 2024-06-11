package com.werken.saxpath;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XPathLexerTest {

private XPathLexer lexer;

@Before
public void setup() {
lexer = new XPathLexer("");
}

@Test
public void testNextToken_EmptyXPath() {
lexer.setXPath("");
assertEquals(new Token(-2, "", 0, 0), lexer.nextToken());
}

@Test
public void testNextToken_Identifier() {
lexer.setXPath("myIdentifier");
assertEquals(new Token(15, "", 0, 11), lexer.nextToken());
}

@Test
public void testNextToken_Operator() {
lexer.setXPath("myIdentifier and anotherIdentifier");
assertEquals(new Token(28, "", 11, 13), lexer.nextToken());
}

@Test
public void testNextToken_Whitespace() {
lexer.setXPath(" myIdentifier ");
assertEquals(new Token(-2, "", 0, 0), lexer.nextToken());
assertEquals(new Token(15, "", 1, 10), lexer.nextToken());
}

@Test
public void testNextToken_Number() {
lexer.setXPath("123");
assertEquals(new Token(31, "", 0, 3), lexer.nextToken());
}

@Test
public void testNextToken_Dollar() {
lexer.setXPath("myIdentifier$");
assertEquals(new Token(26, "", 10, 11), lexer.nextToken());
}

@Test
public void testNextToken_Comma() {
lexer.setXPath("myIdentifier,anotherIdentifier");
assertEquals(new Token(32, "", 11, 12), lexer.nextToken());
}

@Test
public void testNextToken_Equals() {
lexer.setXPath("myIdentifier = anotherIdentifier");
assertEquals(new Token(21, "", 15, 18), lexer.nextToken());
}

@Test
public void testNextToken_Minus() {
lexer.setXPath("myIdentifier - anotherIdentifier");
assertEquals(new Token(6, "", 15, 18), lexer.nextToken());
}

@Test
public void testNextToken_Plus() {
lexer.setXPath("myIdentifier + anotherIdentifier");
assertEquals(new Token(5, "", 15, 18), lexer.nextToken());
}

@Test
public void testNextToken_Slash() {
lexer.setXPath("myIdentifier/anotherIdentifier");
assertEquals(new Token(11, "", 10, 15), lexer.nextToken());
}

@Test
public void testNextToken_Star() {
lexer.setXPath("myIdentifier*");
assertEquals(new Token(20, "", 10, 11), lexer.nextToken());
}

@Test
public void testNextToken_Dot() {
lexer.setXPath("myIdentifier..");
assertEquals(new Token(13, "", 10, 11), lexer.nextToken());
}

@Test
public void testNextToken_LeftBracket() {
lexer.setXPath("myIdentifier[1]");
assertEquals(new Token(3, "", 15, 16), lexer.nextToken());
}

@Test
public void testNextToken_RightBracket() {
lexer.setXPath("myIdentifier[1]");
assertEquals(new Token(4, "", 18, 19), lexer.nextToken());
}

@Test
public void testNextToken_LeftParen() {
lexer.setXPath("(myIdentifier)");
assertEquals(new Token(1, "", 11, 12), lexer.nextToken());
}

@Test
public void testNextToken_RightParen() {
lexer.setXPath("(myIdentifier)");
assertEquals(new Token(2, "", 15, 16), lexer.nextToken());
}

@Test
public void testNextToken_At() {
lexer.setXPath("@myIdentifier");
assertEquals(new Token(16, "", 10, 13), lexer.nextToken());
}

@Test
public void testNextToken_DoubleColon() {
lexer.setXPath("myIdentifier::anotherIdentifier");
assertEquals(new Token(19, "", 15, 22), lexer.nextToken());
}

@Test
public void testNextToken_Not() {
lexer.setXPath("not(myIdentifier)");
assertEquals(new Token(23, "", 11, 14), lexer.nextToken());
}

@Test
public void testNextToken_NotEquals() {
lexer.setXPath("not(myIdentifier) = anotherIdentifier");
assertEquals(new Token(22, "", 18, 25), lexer.nextToken());
}

@Test
public void testNextToken_RelationalOperator() {
lexer.setXPath("myIdentifier < anotherIdentifier");
assertEquals(new Token(7, "", 15, 18), lexer.nextToken());
}

@Test
public void testNextToken_IdentifierOrOperatorName() {
lexer.setXPath("myIdentifier or anotherIdentifier");
assertEquals(new Token(29, "", 15, 18), lexer.nextToken());
}

@Test(expected = IllegalStateException.class)
public void testNextToken_InvalidXPath() {
lexer.setXPath("myIdentifier");
assertEquals(new Token(15, "", 0, 11), lexer.nextToken());
lexer.nextToken(); // Should throw an exception
}
}