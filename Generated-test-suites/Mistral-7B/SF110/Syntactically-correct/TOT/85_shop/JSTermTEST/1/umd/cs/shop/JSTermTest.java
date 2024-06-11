package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSTermTest {

 @Test
 public void testEmptyTerm() {
 JSTerm term = new JSTerm();
 assertTrue(term.isConstant());
 assertEquals("", term.toStr().toString());
 }

 @Test
 public void testVariableTerm() {
 JSTerm term = new JSTerm("x");
 assertTrue(term.isVariable());
 assertEquals("x ", term.toStr().toString());
 }

 @Test
 public void testConstantTerm() {
 JSTerm term = new JSTerm("true");
 assertTrue(term.isConstant());
 assertEquals("true ", term.toStr().toString());
 }

 @Test
 public void testFunctionTerm() {
 JSTerm term = new JSTerm("(+ 1 2)");
 assertTrue(term.isFunction());
 assertEquals("(+ 1 2)", term.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testInvalidTokenizerInput() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("Expecting Term"));
 new JSTerm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testInvalidLeftParenthesis() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("( call"));
 new JSTerm(tokenizer);
 }

 @Test
 public void testListTerm() {
 JSTerm term = new JSTerm();
 term.addElement(new JSTerm("x"));
 term.addElement(new JSTerm("y"));
 assertTrue(term.isFunction());
 assertEquals("( . x . y)", term.toStr().toString());
 }

 @Test
 public void testListTermWithEmptyList() {
 JSTerm term = new JSTerm();
 term.addElement(new JSTerm("nil"));
 assertTrue(term.isFunction());
 assertEquals("(nil)", term.toStr().toString());
 }

 @Test
 public void testParseList() {
 JSTerm term = new JSTerm();
 term.parseList(new StreamTokenizer(new StringReader("( a b c )")));
 JSTerm expected = new JSTerm();
 expected.addElement("a");
 expected.addElement("b");
 expected.addElement("c");
 expected.addElement("nil");
 assertEquals(expected, term);
 }

 @Test
 public void testApplySubstitution() {
 JSTerm a = new JSTerm("a");
 JSTerm b = new JSTerm("b");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement(new JSPairTerm("a", new JSTerm("c")));
 JSTerm c = new JSTerm("c");
 JSTerm term = new JSTerm("(+ a b)");
 JSTerm result = term.applySubstitution(alpha);
 JSTerm expected = new JSTerm();
 expected.addElement("(+ c b)");
 assertEquals(expected, result);
 }

 @Test
 public void testMatches() {
 JSTerm a = new JSTerm("a");
 JSTerm b = new JSTerm("b");
 JSTerm c = new JSTerm("c");
 JSTerm d = new JSTerm("d");
 JSTerm term1 = new JSTerm("(+ a b)");
 JSTerm term2 = new JSTerm("(+ c d)");
 JSSubstitution substitution = term1.matches(term2);
 assertTrue(substitution.isEmpty());

 term1 = new JSTerm("(+ a ( * b c ))");
 term2 = new JSTerm("(+ x ( * y z ))");
 substitution = term1.matches(term2);
 assertFalse(substitution.isEmpty());
 substitution.print();
 }

 @Test
 public void testEquals() {
 JSTerm a = new JSTerm("a");
 JSTerm b = new JSTerm("a");
 JSTerm c = new JSTerm("b");
 JSTerm d = new JSTerm("d");
 JSTerm e = new JSTerm("nil");
 JSTerm term1 = new JSTerm("(+ a b)");
 JSTerm term2 = new JSTerm("(+ a b)");
 JSTerm term3 = new JSTerm("(+ a c)");
 JSTerm term4 = new JSTerm("(+ c d)");JSTerm term5 = new JSTerm("nil");
 assertTrue(term1.equals(term2));
 assertFalse(term1.equals(term3));
 assertFalse(term1.equals(term4));
 assertFalse(term1.equals(term5));
 }

 @Test
 public void testGround() {
 JSTerm term1 = new JSTerm("(+ a b)");
 JSTerm term2 = new JSTerm("(+ a a)");
 JSTerm term3 = new JSTerm("(+ a b)").standardizerTerm();
 assertTrue(term2.isGround());
 assertTrue(term3.isGround());
 assertFalse(term1.isGround());
 }

 @Test
 public void testStandardizerTerm() {
 JSTerm term1 = new JSTerm("(+ x y)");
 JSTerm term2 = term1.standardizerTerm();
 JSTerm term3 = new JSTerm("(+ x1 y1)");
 assertTrue(term2.equals(term3));
 }

 @Test
 public void testCall() {
 JSTerm term1 = new JSTerm("(+ x y)");
 JSTerm term2 = term1.call();
 JSTerm expected = new JSTerm("nil");
 if (term1.isVariable()) {
 expected = term1.clone();
 }
 assertEquals(expected, term2);
 }
}