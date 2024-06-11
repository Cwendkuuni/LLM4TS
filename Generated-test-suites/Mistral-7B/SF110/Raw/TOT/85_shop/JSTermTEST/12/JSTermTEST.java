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
 assertEquals("x", term.toStr().toString());
 }

 @Test
 public void testConstantTerm() {
 JSTerm term = new JSTerm("5");
 assertTrue(term.isConstant());
 assertEquals("5", term.toStr().toString());
 }

 @Test
 public void testFunctionTerm() {
 JSTerm term = new JSTerm("+"); // Assuming '+' is defined as a function here
 assertTrue(term.isFunction());
 assertEquals("+", term.toStr().toString());
 }

 @Test
 public void testListTerm() {
 JSTerm term = new JSTerm("(+ x 5)");
 assertTrue(term.isFunction());
 assertEquals("(+ x 5)", term.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testInvalidTerm() {
 final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("InvalidTerm"));
 JSTerm term = new JSTerm(tokenizer);
 fail("JSTerm constructor should throw JSParserError for InvalidTerm input");
 }

 @Test(expected = JSParserError.class)
 public void testMissingTerm() {
 final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("( + )"));
 JSTerm term = new JSTerm(tokenizer);
 fail("JSTerm constructor should throw JSParserError for missing term error");
 }

 @Test(expected = JSParserError.class)
 public void testUnexpectedToken() {
 final StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("( % x )"));
 JSTerm term = new JSTerm(tokenizer);
 fail("JSTerm constructor should throw JSParserError for unexpected token error");
 }

 @Test
 public void testEqualsTerms() {
 JSTerm term1 = new JSTerm("5");
 JSTerm term2 = new JSTerm("5");
 assertTrue(term1.equals(term2));
 }

 @Test
 public void testNotEqualsTerms() {
 JSTerm term1 = new JSTerm("5");
 JSTerm term2 = new JSTerm("6");
 assertFalse(term1.equals(term2));
 }

 @Test
 public void testDeepEqualTerms() {
 JSTerm term1 = new JSTerm("(+ x (* 2 3))");
 JSTerm term2 = new JSTerm("(+ x (* 2 3))");
 assertTrue(term1.equals(term2));
 }

 @Test
 public void testDeepNotEqualTerms() {
 JSTerm term1 = new JSTerm("(+ x (* 2 3))");
 JSTerm term2 = new JSTerm("(+ y (* 2 3))");
 assertFalse(term1.equals(term2));
 }

 @Test
 public void testApplySubstitutionTerm() {
 JSTerm term = new JSTerm("x");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement(new JSPair("x", new JSTerm("5")));
 JSTerm result = term.applySubstitution(alpha);
 assertEquals("5", result.toStr().toString());
 }

 @Test
 public void testMatchesTerm() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSSubstitution beta = term1.matches(term2);
 assertFalse(beta.fail());
 assertEquals(0, beta.size());
 }

 @Test
 public void testMatchesTermWithSubstitution() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement(new JSPair("x", term1));
 JSSubstitution beta = term1.matches(term2, alpha);
 assertTrue(beta.fail());
 }

 @Test
 public void testMatchesTermWithSuccess() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("x");
 JSSubstitution beta = term1.matches(term2);
 assertTrue(beta.fail());
 assertEquals(1, beta.size());
 }

 @Test
 public void testIsGroundTerm() {
 JSTerm term = new JSTerm("5");
 assertTrue(term.isGround());
 }

 @Test
 public void testIsGroundVariableTerm() {
 JSTerm term = new JSTerm("x");
 assertFalse(term.isGround());
 }

 @Test
 public void testIsGroundListTerm() {
 JSTerm term = new JSTerm("(+ x 5)");
 assertFalse(term.isGround());
 }

 @Test
 public void testIsGroundListTermWithGroundElements() {
 JSTerm term = new JSTerm("(+ 5 5)");
 assertTrue(term.isGround());
 }

 @Test
 public void testStandardizerTerm() {
 JSTerm term = new JSTerm("x");
 JSTerm standardizedTerm = term.standardizerTerm();
 assertTrue(standardizedTerm.isVariable());
 assertEquals("x" + String.valueOf(JSJshopVars.VarCounter), standardizedTerm.toStr().toString());
 }

 @Test
 public void testCallTerm() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("5");
 JSTerm term3 = new JSTerm("+");
 JSTerm term4 = term3.call(term1, term2);
 assertEquals("5", term4.toStr().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCallTermWithInvalidNumberOfArguments() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("5");
 JSTerm term3 = new JSTerm("+");
 JSTerm term4 = term3.call(term1);
 fail("CallTerm method should throw IllegalArgumentException for invalid number of arguments");
 }

 @Test
 public void testCallTermWithNullTerms() {
 JSTerm term1 = null;
 JSTerm term2 = new JSTerm("5");
 JSTerm term3 = new JSTerm("+");
 JSTerm term4 = term3.call(term1, term2);
 fail("CallTerm method should throw NullPointerException for null term");
 }
}