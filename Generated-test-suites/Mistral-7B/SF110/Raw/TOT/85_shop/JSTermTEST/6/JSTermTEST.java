package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;

public class JSTermTest {

 @Test
 public void testEmptyTerm() {
 JSTerm term = new JSTerm();
 assertTrue(term.isEval());
 assertTrue(term.isEmpty());
 }

 @Test
 public void testVariableTerm() {
 JSTerm term = new JSTerm(new StringReader("x"));
 assertTrue(term.isVariable());
 assertEquals("x", term.elementAt(0));
 }

 @Test
 public void testConstantTerm() {
 JSTerm term = new JSTerm(new StringReader("3"));
 assertTrue(term.isConstant());
 assertEquals("3", term.elementAt(0));
 }

 @Test
 public void testFunctionTerm() {
 JSTerm term = new JSTerm(new StringReader("(call list (cons 1 2))"));
 assertTrue(term.isFunction());
 assertEquals("call", term.elementAt(0));
 JSTerm arg1 = term.elementAt(1);
 assertEquals("list", arg1.elementAt(0));
 JSTerm arg2 = arg1.elementAt(1);
 assertTrue(arg2.isList());
 assertEquals(2, arg2.size());
 JSTerm firstElement = arg2.elementAt(0);
 assertEquals(1, firstElement.toInt());
 JSTerm secondElement = arg2.elementAt(1);
 assertTrue(secondElement.isVariable());
 assertEquals("2", secondElement.elementAt(0));
 }

 @Test(expected = JSParserError.class)
 public void testMalformedTerm() {
 JSTerm term = new JSTerm(new StringReader("(call list (cons 1 2) x)"));
 fail("This test should have thrown a JSParserError exception");
 }

 @Test(expected = JSParserError.class)
 public void testMalformedTerm2() {
 JSTerm term = new JSTerm(new StringReader("(call list (cons 1 2) 3)"));
 fail("This test should have thrown a JSParserError exception");
 }

 @Test(expected = JSParserError.class)
 public void testMalformedTerm3() {
 JSTerm term = new JSTerm(new StringReader("(call list (cons 1 2) +)"));
 fail("This test should have thrown a JSParserError exception");
 }

 @Test(expected = JSParserError.class)
 public void testMalformedTerm4() {
 JSTerm term = new JSTerm(new StringReader("(call list (cons 1 (call list 2 3)))"));
 fail("This test should have thrown a JSParserError exception");
 }

 @Test(expected = JSParserError.class)
 public void testMalformedTerm5() {
 JSTerm term = new JSTerm(new StringReader("(call list (cons (call % %) 2))"));
 fail("This test should have thrown a JSParserError exception");
 }

 @Test(expected = JSParserError.class)
 public void testMalformedTerm6() {
 JSTerm term = new JSTerm(new StringReader("(call list nil)"));
 fail("This test should have thrown a JSParserError exception");
 }

 @Test(expected = JSParserError.class)
 public void testMalformedTerm7() {
 JSTerm term = new JSTerm(new StringReader("(call nil (cons 1 2))"));
 fail("This test should have thrown a JSParserError exception");
 }

 @Test(expected = JSParserError.class)
 public void testMalformedTerm8() {
 JSTerm term = new JSTerm(new StringReader("(call list (cons 1 (call list 2 3))) 4"));
 fail("This test should have thrown a JSParserError exception");
 }

 @Test(expected = JSParserError.class)
 public void testMalformedTerm9() {
 JSTerm term = new JSTerm(new StringReader("(call list (cons 1 2) 3 +)"));
 fail("This test should have thrown a JSParserError exception");
 }

 @Test
 public void testListTerm() {
 JSTerm term = new JSTerm(new StringReader("(call list (cons 1 (cons 2 3)))"));
 assertTrue(term.isEval());
 assertTrue(term.isFunction());
 assertTrue(term.isList());
 assertEquals(3, term.size());
 JSTerm firstElement = term.elementAt(0);
 assertTrue(firstElement.isFunction());
 assertEquals("cons", firstElement.elementAt(0));
 JSTerm secondElement = firstElement.elementAt(1);
 assertTrue(secondElement.isList());
 assertEquals(2, secondElement.size());
 JSTerm firstListElement = secondElement.elementAt(0);
 assertEquals(1, firstListElement.toInt());
 JSTerm secondListElement = secondElement.elementAt(1);
 assertTrue(secondListElement.isVariable());
 assertEquals("2", secondListElement.elementAt(0));
 JSTerm thirdElement = term.elementAt(1);
 assertTrue(thirdElement.isVariable());
 assertEquals("3", thirdElement.elementAt(0));
 JSTerm fourthElement = term.elementAt(2);
 assertTrue(fourthElement.isNil());
 }

 @Test
 public void testApplySubstitution() {
 JSTerm term1 = new JSTerm(new StringReader("(x + y)"));
 JSTerm term2 = new JSTerm(new StringReader("(z + y)"));
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement(new JSPairTerm("x", new JSTerm("z")));
 JSTerm result = term1.applySubstitution(alpha);
 assertEquals(term2, result);
 }

 @Test
 public void testMatches() {
 JSTerm term1 = new JSTerm(new StringReader("(x + y)"));
 JSTerm term2 = new JSTerm(new StringReader("(z + y)"));
 JSTerm term3 = new JSTerm(new StringReader("(x + w)"));
 JSTerm term4 = new JSTerm(new StringReader("(x + y)"));
 JSTerm term5 = new JSTerm(new StringReader("(z + w)"));
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPairTerm("x", new JSTerm("z")));
 JSTerm expected = new JSTerm(new StringReader("(z + y)"));
 assertEquals(expected, term1.matches(term2));
 assertNull(term1.matches(term3));
 assertTrue(term1.matches(term4).isEmpty());
 assertFalse(term1.matches(term5).isEmpty());
 }

 @Test
 public void testStandardizerTerm() {
 JSTerm term = new JSTerm(new StringReader("(x + y)"));
 JSTerm expected = new JSTerm(new StringReader("(x0 + y0)"));
 assertEquals(expected, term.standardizerTerm());
 }

 @Test
 public void testCall() {
 JSTerm term = new JSTerm(new StringReader("(+ 1 2)"));
 JSTerm expected = new JSTerm(new StringReader("3"));
 assertEquals(expected, term.call());
 }

 @Test
 public void testCallWithVariables() {
 JSTerm term = new JSTerm(new StringReader("(+ x 2)"));
 JSTerm expected = new JSTerm(new StringReader("(+ x 2)"));
 assertEquals(expected, term.call());
 }

 @Test
 public void testCallWithVariablesAndConstant() {
 JSTerm term = new JSTerm(new StringReader("(+ x 3)"));
 JSTerm expected = new JSTerm(new StringReader("(+ x 3)"));
 assertEquals(expected, term.call());
 }

 @Test
 public void testCallWithFunction() {
 JSTerm term = new JSTerm(new StringReader("(+ (+ x 1) 2)"));
 JSTerm expected = new JSTerm(new StringReader("(+ (+ x 1) 2)"));
 assertEquals(expected, term.call());
 }

 @Test
 public void testCallWithFunction2() {
 JSTerm term = new JSTerm(new StringReader("(+ (+ x 1) 2)"));
 JSTerm expected = new JSTerm(new StringReader("(+ (+ x 1) 2)"));
 assertEquals(expected, term.call());
 }

 @Test
 public void testCallWithFunction3() {
 JSTerm term = new JSTerm(new StringReader("(+(+ x 1) 2)"));
 JSTerm expected = new JSTerm(new StringReader("(+ (+ x 1) 2)"));
 assertEquals(expected, term.call());
 }

 @Test
 public void testCallWithFunction4() {
 JSTerm term = new JSTerm(new StringReader("(+(+ x 1) 2)"));
 JSTerm expected = new JSTerm(new StringReader("(+ (+ x 1) 2)"));
 assertEquals(expected, term.call());
 }
}