package com.werken.saxpath;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XPathLexerTest {

private XPathLexer lexer;
private String xpath;

@Before
public void setUp() {
xpath = "//a[@id='test']/b[1]/c[1]/d[1]/e[1]/f[1]";
lexer = new XPathLexer(xpath);
}

@After
public void tearDown() {
lexer = null;
xpath = null;
}

@Test
public void testNextToken_EmptyXPath() {
lexer = new XPathLexer("");
assertEquals(new Token(-2, "", 0, 0), lexer.nextToken());
}

@Test
public void testNextToken_XPathWithIdentifiersAndOperators() {
assertEquals(new Token(15, xpath, 0, 2), lexer.nextToken()); // "//"
assertEquals(new Token(18, xpath, 2, 4), lexer.nextToken()); // "a"
assertEquals(new Token(16, xpath, 4, 5), lexer.nextToken()); // "@"
assertEquals(new Token(1, xpath, 5, 6), lexer.nextToken()); // "/"
assertEquals(new Token(15, xpath, 6, 10), lexer.nextToken()); // "a"
assertEquals(new Token(18, xpath, 10, 12), lexer.nextToken()); // "@"
assertEquals(new Token(5, xpath, 12, 13), lexer.nextToken()); // "/"
assertEquals(new Token(15, xpath, 13, 17), lexer.nextToken()); // "b"
assertEquals(new Token(13, xpath, 17, 18), lexer.nextToken()); // "["
assertEquals(new Token(1, xpath, 18, 19), lexer.nextToken()); // "/"
assertEquals(new Token(15, xpath, 19, 22), lexer.nextToken()); // "c"
assertEquals(new Token(13, xpath, 22, 23), lexer.nextToken()); // "["
assertEquals(new Token(1, xpath, 23, 24), lexer.nextToken()); // "/"
assertEquals(new Token(15, xpath, 24, 27), lexer.nextToken()); // "d"
assertEquals(new Token(13, xpath, 27, 28), lexer.nextToken()); // "["
assertEquals(new Token(1, xpath, 28, 29), lexer.nextToken()); // "/"
assertEquals(new Token(15, xpath, 29, 32), lexer.nextToken()); // "e"
assertEquals(new Token(13, xpath, 32, 33), lexer.nextToken()); // "["
assertEquals(new Token(1, xpath, 33, 34), lexer.nextToken()); // "/"
assertEquals(new Token(15, xpath, 34, 37), lexer.nextToken()); // "f"
assertEquals(new Token(13, xpath, 37, 38), lexer.nextToken()); // "["
assertEquals(new Token(-1, xpath, 38, 41), lexer.nextToken()); // EOF
}

@Test
public void testNextToken_XPathWithLiterals() {
assertEquals(new Token(15, xpath, 0, 2), lexer.nextToken()); // "//"
assertEquals(new Token(13, xpath, 2, 4), lexer.nextToken()); // "["
assertEquals(new Token(27, xpath, 4, 7), lexer.nextToken()); // "'test'"
assertEquals(new Token(13, xpath, 7, 8), lexer.nextToken()); // "]"
assertEquals(new Token(1, xpath, 8, 9), lexer.nextToken()); // "/"
assertEquals(new Token(15, xpath, 9, 13), lexer.nextToken()); // "b"
assertEquals(new Token(13, xpath, 13, 14), lexer.nextToken()); // "["
assertEquals(new Token(5, xpath, 14, 15), lexer.nextToken()); // "/"
assertEquals(new Token(15, xpath, 15, 19), lexer.nextToken()); // "c"
assertEquals(new Token(13, xpath, 19, 20), lexer.nextToken()); // "["
assertEquals(new Token(5, xpath, 20, 21), lexer.nextToken()); // "/"
assertEquals(new Token(15, xpath, 21, 25), lexer.nextToken()); // "d"
assertEquals(new Token(13, xpath, 25, 26), lexer.nextToken()); // "["
assertEquals(new Token(5, xpath, 26, 27), lexer.nextToken()); // "/"
assertEquals(new Token(15, xpath, 27, 31), lexer.nextToken()); // "e"
assertEquals(new Token(13, xpath, 31, 32), lexer.nextToken()); // "["
assertEquals(new Token(5, xpath, 32, 33), lexer.nextToken()); // "/"
assertEquals(new Token(15, xpath, 33, 37), lexer.nextToken()); // "f"
assertEquals(new Token(13, xpath, 37, 38), lexer.nextToken()); // "]"
assertEquals(new Token(-1, xpath, 38, 41), lexer.nextToken()); // EOF
}

@Test
public void testNextToken_XPathWithWhitespace() {
assertEquals(new Token(15, xpath, 0, 2), lexer.nextToken()); // "//"
assertEquals(new Token(13, xpath, 2, 5), lexer.nextToken()); // " "
assertEquals(new Token(13, xpath, 5, 6), lexer.nextToken()); // " "
assertEquals(new Token(13, xpath, 6, 7), lexer.nextToken()); // " "
assertEquals(new Token(15, xpath, 7, 10), lexer.nextToken()); // "a"
assertEquals(new Token(16, xpath, 10, 11), lexer.nextToken()); // "@"
assertEquals(new Token(1, xpath, 11, 12), lexer.nextToken()); // "/"
assertEquals(new Token(15, xpath, 12, 16), lexer.nextToken()); // "b"
assertEquals(new Token(13, xpath, 16, 17), lexer.nextToken()); // "["
assertEquals(new Token(1, xpath, 17, 18), lexer.nextToken()); // "/"
assertEquals(new Token(15, xpath, 18, 21), lexer.nextToken()); // "c"
assertEquals(new Token(13, xpath, 21, 22), lexer.nextToken()); // "["
assertEquals(new Token(1, xpath, 22, 23), lexer.nextToken()); // "/"
assertEquals(new Token(15, xpath, 23, 26), lexer.nextToken()); // "d"
assertEquals(new Token(13, xpath, 26, 27), lexer.nextToken()); // "["
assertEquals(new Token(1, xpath, 27, 28), lexer.nextToken()); // "/"
assertEquals(new Token(15, xpath, 28, 31), lexer.nextToken()); // "e"
assertEquals(new Token(13, xpath, 31, 32), lexer.nextToken()); // "["
assertEquals(new Token(1, xpath, 32, 33), lexer.nextToken()); // "/"
assertEquals(new Token(15, xpath, 33, 36), lexer.nextToken()); // "f"
assertEquals(new Token(13, xpath, 36, 37), lexer.nextToken()); // "]"
assertEquals(new Token(-1, xpath, 37, 41), lexer.nextToken()); // EOF
}

@Test(expected = AssertionError.class)
public void testNextToken_UnexpectedToken() {
lexer = new XPathLexer("//a[1]z");
assertEquals(new Token(15, "//a[1]z", 0, 2), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 2, 3), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 3, 4), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 4, 5), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 5, 6), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 6, 7), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 7, 8), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 8, 9), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 9, 10), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 10, 11), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 11, 12), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 12, 13), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 13, 14), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 14, 15), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 15, 16), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 16, 17), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 17, 18), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 18, 19), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 19, 20), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 20, 21), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 21, 22), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 22, 23), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 23, 24), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 24, 25), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 25, 26), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 26, 27), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 27, 28), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 28, 29), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 29, 30), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 30, 31), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 31, 32), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 32, 33), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 33, 34), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 34, 35), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 35, 36), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 36, 37), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 37, 38), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 38, 39), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 39, 40), lexer.nextToken());
assertEquals(new Token(15, "//a[1]z", 40, 41), lexer.nextToken());
lexer.nextToken(); // expected AssertionError here
}
}