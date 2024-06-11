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
 JSTerm term = new JSTerm("(+ x y)");
 assertTrue(term.isFunction());
 assertEquals("(+ x y)", term.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testInvalidTerm1() throws JSParserError {
 final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(x"));
 new JSTerm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testInvalidTerm2() throws JSParserError {
 final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(x y)"));
 new JSTerm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testInvalidTerm3() throws JSParserError {
 final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(x +)"));
 new JSTerm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testInvalidTerm4() throws JSParserError {
 final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(x (+ y z))"));
 new JSTerm(tokenizer);
 }

 @Test
 public void testListTerm() {
 JSTerm term = new JSTerm();
 term.addElement(new JSTerm("x"));
 term.addElement(new JSTerm("y"));
 term.addElement(new JSTerm("z"));
 assertTrue(term.isFunction());
 assertEquals("(x y z)", term.toStr().toString());
 }

 @Test
 public void testApplySubstitution() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPairVarTerm("x", term2));
 JSTerm result = term1.applySubstitution(substitution);
 assertEquals(term2, result);
 }

 @Test
 public void testMatches() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSTerm term3 = new JSTerm("x");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPairVarTerm("x", term3));
 assertTrue(term1.matches(term3).equals(substitution));
 assertFalse(term1.matches(term2).isFailure());
 }

 @Test
 public void testEquals() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("x");
 JSTerm term3 = new JSTerm("y");
 assertTrue(term1.equals(term2));
 assertFalse(term1.equals(term3));
 }

 @Test
 public void testGround() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("(+ x y)");
 JSTerm term3 = new JSTerm("(+ x y)".replaceAll("y", "5"));
 assertTrue(term3.isGround());
 assertFalse(term1.isGround());
 assertFalse(term2.isGround());
 }

 @Test
 public void testStandardizerTerm() {
 JSTerm term1 = new JSTerm("(+ x y)");
 JSTerm term2 = term1.standardizerTerm();
 assertEquals("(+ x y)", term1.toStr().toString());
 assertEquals("(+ x %VAR_1)", term2.toStr().toString());
 }

 @Test
 public void testCall() {
 JSTerm term1 = new JSTerm("(+ x y)");
 JSTerm term2 = new JSTerm("(+ 1 2)");
 JSTerm result = term1.call();
 JSTerm expected = new JSTerm("(+ 1 2)");
 assertEquals(expected.toStr().toString(), result.toStr().toString());
 }

}