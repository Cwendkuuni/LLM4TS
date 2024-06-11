package com.werken.saxpath;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class XPathLexerTest {

 private XPathLexer lexer;

 @Test
 public void testEmptyXPath() {
 lexer = new XPathLexer("");
 Token token = lexer.nextToken();
 assertNull(token);
 }

 @Test
 public void testSimpleXPath() {
 lexer = new XPathLexer("/a/b");
 Token token = lexer.nextToken();
 assertEquals("/", token.getValue());
 Token nextToken = lexer.nextToken();
 assertEquals("a", nextToken.getValue());
 Token nextToken2 = lexer.nextToken();
 assertEquals("/", nextToken2.getValue());
 Token nextToken3 = lexer.nextToken();
 assertEquals("b", nextToken3.getValue());
 assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithNumber() {
 lexer = new XPathLexer("/a[1]");
 Token token = lexer.nextToken();
 assertEquals("/", token.getValue());
 Token nextToken = lexer.nextToken();
 assertEquals("a", nextToken.getValue());
 Token nextToken3 = lexer.nextToken();
 assertEquals("[", nextToken3.getValue());
 Token nextToken4 = lexer.nextToken();
 assertEquals("1", nextToken4.getValue());
 assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithIdentifiers() {
 lexer = new XPathLexer("/a/b[@id='id']/c[name()='name']/d[@name='name']");
 Token token = lexer.nextToken();
 assertEquals("/", token.getValue());
 Token nextToken = lexer.nextToken();
 assertEquals("a", nextToken.getValue());
 Token nextToken2 = lexer.nextToken();
 assertEquals("/", nextToken2.getValue());
 Token nextToken3 = lexer.nextToken();
 assertEquals("b", nextToken3.getValue());
 Token nextToken4 = lexer.nextToken();
 assertEquals("[", nextToken4.getValue());
 Token nextToken5 = lexer.nextToken();
 assertEquals("@", nextToken5.getValue());
 Token nextToken6 = lexer.nextToken();
 assertEquals("id", nextToken6.getValue());
 Token nextToken7 = lexer.nextToken();
 assertEquals("]", nextToken7.getValue());
 Token nextToken8 = lexer.nextToken();
 assertEquals("/", nextToken8.getValue());
 Token nextToken9 = lexer.nextToken();
 assertEquals("c", nextToken9.getValue());
 Token nextToken10 = lexer.nextToken();
 assertEquals("[", nextToken10.getValue());
 Token nextToken11 = lexer.nextToken();
 assertEquals("name()", nextToken11.getValue());
 Token nextToken12 = lexer.nextToken();
 assertEquals("]", nextToken12.getValue());
 Token nextToken13 = lexer.nextToken();
 assertEquals("/", nextToken13.getValue());
 Token nextToken14 = lexer.nextToken();
 assertEquals("d", nextToken14.getValue());
 Token nextToken15 = lexer.nextToken();
 assertEquals("[", nextToken15.getValue());
 Token nextToken16 = lexer.nextToken();
 assertEquals("@", nextToken16.getValue());
 Token nextToken17 = lexer.nextToken();
 assertEquals("name", nextToken17.getValue());
 Token nextToken18 = lexer.nextToken();
 assertEquals("]", nextToken18.getValue());
 assertNull(lexer.nextToken());
 }

 @Test (expected = java.util.NoSuchElementException.class)
 public void testInvalidXPath() {
 lexer = new XPathLexer("/a[@/b");
 lexer.nextToken();
 }

 @Test
 public void testXPathWithSpecialChars() {
 lexer = new XPathLexer("/a[contains(.,'[) ]']");
 Token token = lexer.nextToken();
 assertEquals("/", token.getValue());
 Token nextToken = lexer.nextToken();
 assertEquals("a", nextToken.getValue());
 Token nextToken2 = lexer.nextToken();
 assertEquals("[", nextToken2.getValue());
 Token nextToken3 = lexer.nextToken();
 assertEquals("contains", nextToken3.getValue());
 Token nextToken4 = lexer.nextToken();
 assertEquals("(.", nextToken4.getValue());
 Token nextToken5 = lexer.nextToken();
 assertEquals("'", nextToken5.getValue());
 Token nextToken6 = lexer.nextToken();
 assertEquals("]", nextToken6.getValue());
 Token nextToken7 = lexer.nextToken();
 assertEquals("]", nextToken7.getValue());
 assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithSlash() {
 lexer = new XPathLexer("/a/b/c//d");
 Token token = lexer.nextToken();
 assertEquals("/", token.getValue());
 Token nextToken = lexer.nextToken();
 assertEquals("a", nextToken.getValue());
 Token nextToken2 = lexer.nextToken();
 assertEquals("/", nextToken2.getValue());
 Token nextToken3 = lexer.nextToken();
 assertEquals("b", nextToken3.getValue());
 Token nextToken4 = lexer.nextToken();
 assertEquals("/", nextToken4.getValue());
 Token nextToken5 = lexer.nextToken();
 assertEquals("c", nextToken5.getValue());
 Token nextToken6 = lexer.nextToken();
 assertEquals("//", nextToken6.getValue());
 Token nextToken7 = lexer.nextToken();
 assertEquals("d", nextToken7.getValue());
 assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithComments() {
 lexer = new XPathLexer("/a/*[comment()='comment']/b");
 Token token = lexer.nextToken();
 assertEquals("/", token.getValue());
 Token nextToken = lexer.nextToken();
 assertEquals("a", nextToken.getValue());
 Token nextToken2 = lexer.nextToken();
 assertEquals("/*", nextToken2.getValue());
 Token nextToken3 = lexer.nextToken();
 assertEquals("[", nextToken3.getValue());
 Token nextToken4 = lexer.nextToken();
 assertEquals("comment", nextToken4.getValue());
 Token nextToken5 = lexer.nextToken();
 assertEquals("]", nextToken5.getValue());
 Token nextToken6 = lexer.nextToken();
 assertEquals("/", nextToken6.getValue());
 Token nextToken7 = lexer.nextToken();
 assertEquals("b", nextToken7.getValue());
 assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithWhiteSpace() {
 lexer = new XPathLexer("/a/b [  ][   ]/c");
 Token token = lexer.nextToken();
 assertEquals("/", token.getValue());
 Token nextToken = lexer.nextToken();
 assertEquals("a", nextToken.getValue());
 Token nextToken2 = lexer.nextToken();
 assertEquals("/", nextToken2.getValue());
 Token nextToken3 = lexer.nextToken();
 assertEquals("b", nextToken3.getValue());
 Token nextToken4 = lexer.nextToken();
 assertEquals(" [", nextToken4.getValue());
 Token nextToken5 = lexer.nextToken();
 assertEquals("[", nextToken5.getValue());
 Token nextToken6 = lexer.nextToken();
 assertEquals("]", nextToken6.getValue());
 Token nextToken7 = lexer.nextToken();
 assertEquals("]", nextToken7.getValue());
 Token nextToken8 = lexer.nextToken();
 assertEquals("/", nextToken8.getValue());
 Token nextToken9 = lexer.nextToken();
 assertEquals("c", nextToken9.getValue());
 assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithDoubleColon() {
 lexer = new XPathLexer("/a/b::c");
 Token token = lexer.nextToken();
 assertEquals("/", token.getValue());
 Token nextToken = lexer.nextToken();
 assertEquals("a", nextToken.getValue());
 Token nextToken2 = lexer.nextToken();
 assertEquals("/", nextToken2.getValue());
 Token nextToken3 = lexer.nextToken();
 assertEquals("b", nextToken3.getValue());
 Token nextToken4 = lexer.nextToken();
 assertEquals("::", nextToken4.getValue());
 Token nextToken5 = lexer.nextToken();
 assertEquals("c", nextToken5.getValue());
 assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithDollarSign() {
 lexer = new XPathLexer("/a/b[@$prefix='prefix']");
 Token token = lexer.nextToken();
 assertEquals("/", token.getValue());
 Token nextToken = lexer.nextToken();
 assertEquals("a", nextToken.getValue());
 Token nextToken2 = lexer.nextToken();
 assertEquals("/", nextToken2.getValue());
 Token nextToken3 = lexer.nextToken();
 assertEquals("b", nextToken3.getValue());
 Token nextToken4 = lexer.nextToken();
 assertEquals("[@", nextToken4.getValue());
 Token nextToken5 = lexer.nextToken();
 assertEquals("$prefix", nextToken5.getValue());
 Token nextToken6 = lexer.nextToken();
 assertEquals("]", nextToken6.getValue());
 assertNull(lexer.nextToken());
 }

 @Test
 public void testXPathWithPipe() {
 lexer = new XPathLexer("/a/(b[1]|c)[d]");
 Token token = lexer.nextToken();
 assertEquals("/", token.getValue());
 Token nextToken = lexer.nextToken();
 assertEquals("a", nextToken.getValue());
 Token nextToken2 = lexer.nextToken();
 assertEquals("/", nextToken2.getValue());
 Token nextToken3 = lexer.nextToken();
 assertEquals("(", nextToken3.getValue());
 Token nextToken4 = lexer.nextToken();
 assertEquals("b[1]", nextToken4.getValue());
 Token nextToken5 = lexer.nextToken();
 assertEquals("|", nextToken5.getValue());
 Token nextToken6 = lexer.nextToken();
 assertEquals("c", nextToken6.getValue());
 Token nextToken7 = lexer.nextToken();
 assertEquals(")", nextToken7.getValue());
 Token nextToken8 = lexer.nextToken();
 assertEquals("/", nextToken8.getValue());
 Token nextToken9 = lexer.nextToken();
 assertEquals("d", nextToken9.getValue());
 assertNull(lexer.nextToken());
 }
}