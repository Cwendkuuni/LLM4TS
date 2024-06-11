package umd.cs.shop.test;

import org.junit.Assert;
import org.junit.Test;

import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;

public class JSTermTest {

 @Test
 public void testEmptyTerm() {
 JSTerm term = new JSTerm();
 Assert.assertEquals("", term.toStr().toString());
 }

 @Test
 public void testVariableTerm() {
 JSTerm term = new JSTerm("x");
 Assert.assertEquals("x ", term.toStr().toString());
 }

 @Test
 public void testConstantTerm() {
 JSTerm term = new JSTerm("3");
 Assert.assertEquals("3 ", term.toStr().toString());
 }

 @Test
 public void testFunctionTerm() {
 JSTerm term = new JSTerm("(+ 1 2)");
 Assert.assertEquals("(+ 1 2)", term.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testParseErrorVariableMissing() {
 JSUtil.parse("Expecting Term");
 }

 @Test(expected = JSParserError.class)
 public void testParseErrorFunctionMissingLeftParenthesis() {
 JSUtil.parse("+( 1 2)");
 }

 @Test(expected = JSParserError.class)
 public void testParseErrorFunctionMissingRightParenthesis() {
 JSUtil.parse("(+ 1 ");
 }

 @Test
 public void testArithmeticOperations() {
 JSTerm term1 = new JSTerm("(+ 1 2)");
 JSTerm term2 = new JSTerm("(* 3 4)");
 JSTerm term3 = new JSTerm("(- 5 3)");
 JSTerm term4 = new JSTerm("(/ 10 2)");

 Assert.assertEquals("3", term2.toStr().toString());
 Assert.assertEquals("2", term3.toStr().toString());
 Assert.assertEquals("5.0", term4.toStr().toString());

 JSTerm result = new JSTerm();
 result.makeEval(true);
 result.addElement(term1);
 result.addElement(term2);
 result.addElement(term3);
 result.addElement(term4);

 Assert.assertEquals("3 + 3 * 2 - 5.0", result.toStr().toString());
 Assert.assertEquals(12.0, JSEvaluate.evaluate(result), 0.001);
 }

 @Test
 public void testListTerm() {
 JSTerm term1 = new JSTerm("(list 1 2 3)");
 JSTerm term2 = new JSTerm("(list (list 4 5) (list 6 7))");

 Assert.assertEquals("(list 1 2 3)", term1.toStr().toString());
 Assert.assertEquals("(list (list 4 5) (list 6 7))", term2.toStr().toString());

 JSTerm result = new JSTerm();
 result.makeEval(true);
 result.addElement(term1);
 result.addElement(term2);

 Assert.assertEquals("(list 1 2 3) + (list (list 4 5) (list 6 7))", result.toStr().toString());

 Assert.assertEquals(1 + 2 + 4 + 5 + 6 + 7, JSEvaluate.evaluate(result), 0.001);
 }

 @Test
 public void testParseErrorVariableWrongType() {
 JSUtil.parse("(+ x 2)");
 }

 @Test
 public void testParseErrorFunctionWrongName() {
 JSUtil.parse("(%%% 1 2)");
 }

 @Test
 public void testParseErrorFunctionCallMissingOperator() {
 JSUtil.parse("(call 1 nil)");
 }

 @Test
 public void testParseErrorFunctionCallMissingTerm() {
 JSUtil.parse("(call nil 1)");
 }

 @Test
 public void testParseErrorFunctionCallMissingLeftParenthesis() {
 JSUtil.parse("(call (+ 1 2) 3)");
 }

 @Test
 public void testParseErrorFunctionCallMissingRightParenthesis() {
 JSUtil.parse("(call (+ 1 2) 3 )");
 }

 @Test
 public void testParseErrorFunctionCallInvalidTerm() {
 JSUtil.parse("(call (+ 1 2) x)");
 }

 @Test
 public void testParseErrorFunctionCallInvalidArity() {
 JSUtil.parse("(call (+ x 1) 3 4 5)");
 }

 @Test
 public void testTermEquality() {
 JSTerm term1 = new JSTerm("(+ 1 2)");
 JSTerm term2 = new JSTerm("(+ 1 2)");
 JSTerm term3 = new JSTerm("(+ 1 3)");

 Assert.assertTrue(term1.equals(term2));
 Assert.assertFalse(term1.equals(term3));
 }

 @Test
 public void testSubstitutionMatching() {
 JSTerm term1 = new JSTerm("(+ x 1)");
 JSTerm term2 = new JSTerm("(+ y 1)");
 JSSubstitution substitution = term1.matches(term2);

 Assert.assertFalse(substitution.empty());
 Assert.assertEquals(new JSPair("x", "y"), substitution.first());
 Assert.assertEquals(1, substitution.size());
 }

 @Test
 public void testTermCloning() {
 JSTerm term = new JSTerm("(+ 1 (+ 2 3))");
 JSTerm clonedTerm = term.cloneT();

 Assert.assertEquals(term.toStr().toString(), clonedTerm.toStr().toString());
 }

 @Test
 public void testTermApplyingSubstitution() {
 JSTerm term = new JSTerm("(+ x 1)");
 JSTerm termInst = new JSTerm("x");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPair("x", termInst));

 JSTerm result = term.applySubstitutionT(substitution);

 Assert.assertTrue(result.isVariable());
 Assert.assertEquals("1", result.elementAt(0).toString());
 }
}