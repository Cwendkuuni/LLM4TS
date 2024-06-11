package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {

 @Test
 public void testEmptyForm() {
 JSPredicateForm pf = new JSPredicateForm();
 assertTrue(pf.size() == 0);
 }

 @Test
 public void testFormWithOneElement() {
 JSPredicateForm pf = new JSPredicateForm("(and a b)");
 assertEquals(2, pf.size());
 assertEquals("and", pf.elementAt(0).toString().toLowerCase());
 assertNotNull(pf.elementAt(1));
 }

 @Test
 public void testFormWithNotOperator() {
 JSPredicateForm pf = new JSPredicateForm("(not (and a b))");
 assertEquals(2, pf.size());
 assertEquals("not", pf.elementAt(0).toString().toLowerCase());
 assertNotNull(pf.elementAt(1));
 }

 @Test(expected = JSParserError.class)
 public void testFormWithInvalidLeftParenthesis() {
 JSPredicateForm pf = new JSPredicateForm("((a b)");
 fail("Should throw JSParserError exception");
 }

 @Test(expected = JSParserError.class)
 public void testFormWithUnexpectedRightParenthesis() {
 JSPredicateForm pf = new JSPredicateForm("(a b) )");
 fail("Should throw JSParserError exception");
 }

 @Test(expected = JSParserError.class)
 public void testFormWithInvalidOperator() {
 JSPredicateForm pf = new JSPredicateForm("(and x y z)");
 fail("Should throw JSParserError exception as 'z' is not a valid operator");
 }

 @Test(expected = JSParserError.class)
 public void testFormWithMissingRightParenthesis() {
 JSPredicateForm pf = new JSPredicateForm("(and a b");
 fail("Should throw JSParserError exception as missing right parenthesis");
 }

 @Test
 public void testFormPrinting() {
 JSPredicateForm pf = new JSPredicateForm("(and (not a) (or b c))");
 String expected = "(and (not a) (or b c))";
 assertEquals(expected, pf.toStr().toString());
 }

 @Test
 public void testFormCloning() {
 JSPredicateForm pf = new JSPredicateForm("(and (not a) (or b c))");
 JSPredicateForm cloned = pf.clonePF();
 assertEquals(pf.size(), cloned.size());
 for (int i = 0; i < pf.size(); i++) {
 assertEquals(pf.elementAt(i), cloned.elementAt(i));
 }
 }

 @Test
 public void testFormApplicationOfSubstitution() {
 JSPredicateForm pf = new JSPredicateForm("(and (not a) (or b c))");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("a", 1);
 JSPredicateForm pf1 = pf.applySubstitutionPF(alpha);
 String expected = "(and (not 1) (or b c))";
 assertEquals(expected, pf1.toStr().toString());
 }

 @Test
 public void testFormMatching() {
 JSPredicateForm pf = new JSPredicateForm("(and (not a) (or b c))");
 JSPredicateForm tf = new JSPredicateForm("(and (not b) (or c a))");
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = pf.matches(tf, alpha);
 assertFalse(beta.isFailure());
 }

 @Test
 public void testFormMatchingWithFailure() {
 JSPredicateForm pf = new JSPredicateForm("(and (not a) (or b c))");
 JSPredicateForm tf = new JSPredicateForm("(and (not c) (or a b))");
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = pf.matches(tf, alpha);
 assertTrue(beta.isFailure());
 }

 @Test
 public void testFormEquals() {
 JSPredicateForm pf1 = new JSPredicateForm("(and (not a) (or b c))");
 JSPredicateForm pf2 = new JSPredicateForm("(and (not a) (or b c))");
 JSPredicateForm pf3 = new JSPredicateForm("(and (not b) (or c a))");
 assertTrue(pf1.equals(pf2));
 assertFalse(pf1.equals(pf3));
 }

 @Test
 public void testFormStandardization() {
 JSPredicateForm pf = new JSPredicateForm("(and (not a) (or b (and c d))))");
 JSPredicateForm standardized = pf.standarizerPredicateForm();
 String expected = "(and (not a) (or (and c d) b))";
 assertEquals(expected, standardized.toStr().toString());
 }
}