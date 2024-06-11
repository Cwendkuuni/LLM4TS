package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSTermTest {

 private StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(call (list a b) (+ a b) nil)"));

 @Test
 public void testParseTerm() {
 JSTerm term = new JSTerm(tokenizer);
 assertTrue(term.isFunction());
 assertEquals("call", term.elementAt(0));
 assertEquals(2, term.size());
 assertFalse(term.isVariable());
 assertFalse(term.isConstant());

 JSTerm function = term.elementAt(1);
 assertTrue(function.isFunction());
 assertTrue(function.isEval());
 assertEquals("list", function.elementAt(0));
 assertEquals(2, function.size());

 JSTerm arg1 = function.elementAt(1);
 assertTrue(arg1.isVariable());
 assertEquals("a", arg1.elementAt(0));

 JSTerm arg2 = function.elementAt(2);
 assertTrue(arg2.isVariable());
 assertEquals("b", arg2.elementAt(0));
 }

 @Test
 public void testParseVariable() {
 tokenizer = new StreamTokenizer(new StringReader("(var x)"));
 JSTerm term = new JSTerm(tokenizer);
 assertTrue(term.isVariable());
 assertEquals("x", term.elementAt(0));
 }

 @Test
 public void testParseConstant() {
 tokenizer = new StreamTokenizer(new StringReader("5"));
 JSTerm term = new JSTerm(tokenizer);
 assertTrue(term.isConstant());
 assertEquals("5", term.elementAt(0));
 }

 @Test(expected = JSParserError.class)
 public void testParseError1() {
 tokenizer = new StreamTokenizer(new StringReader("(call (list a b) (+ a b) x)"));
 new JSTerm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testParseError2() {
 tokenizer = new StreamTokenizer(new StringReader("(call (list a b) (+ a b) 5)"));
 new JSTerm(tokenizer);
 }

 @Test
 public void testParseList() {
 tokenizer = new StreamTokenizer(new StringReader("(list a b c)"));
 JSTerm term = new JSTerm(tokenizer);
 assertTrue(term.isFunction());
 assertEquals("list", term.elementAt(0));
 assertEquals(3, term.size());

 JSTerm first = term.elementAt(1);
 assertTrue(first.isVariable());
 assertEquals("a", first.elementAt(0));

 JSTerm second = term.elementAt(2);
 assertTrue(second.isVariable());
 assertEquals("b", second.elementAt(0));

 JSTerm third = term.elementAt(3);
 assertTrue(third.isVariable());
 assertEquals("c", third.elementAt(0));
 }

 @Test
 public void testToString() {
 JSTerm term = new JSTerm(tokenizer);
 assertEquals("(call (list a b) (+ a b) nil)", term.toStr().toString());
 }

 @Test
 public void testIsGround() {
 JSTerm term = new JSTerm(tokenizer);
 assertFalse(term.isGround());

 JSTerm constantTerm = new JSTerm(tokenizer);
 constantTerm.makeConstant();
 assertTrue(constantTerm.isGround());
 }

 @Test
 public void testStandardizerTerm() {
 JSTerm term = new JSTerm(tokenizer);
 JSTerm standardizedTerm = term.standardizerTerm();
 assertEquals("(call (list x1 x2) (+ x1 x2) nil)", standardizedTerm.toStr().toString());
 }

 @Test
 public void testCall() {
 JSTerm term = new JSTerm(tokenizer);
 JSTerm result = term.call();
 assertEquals("(call (list a b) (+ a b) nil)", result.toStr().toString());
 }

}