package umd.cs.shop.test;

import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

public class JSPredicateFormTest {

 private StreamTokenizer createTokenizer(String text) {
 return new StreamTokenizer(new StringReader(text));
 }

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm pf = new JSPredicateForm();
 assertTrue(pf.isEmpty());
 }

 @Test
 public void testStringConstructor() {
 JSPredicateForm pf = new JSPredicateForm("(not p)");
 assertEquals("(not p)", pf.toString());
 }

 @Test
 public void testStreamTokenizerConstructor() {
 JSPredicateForm pf = new JSPredicateForm(createTokenizer("(p)"));
 assertEquals("(p)", pf.toString());
 }

 @Test
 public void testJSPredicateFormInit() {
 JSPredicateForm pf = new JSPredicateForm();
 pf.JSPredicateFormInit(createTokenizer("(not (or (= x 3) (= x 4)))"));
 assertEquals("(not (or (= x 3) (= x 4)))", pf.toString());
 }

 @Test(expected = JSParserError.class)
 public void testExpectingLeftParenthesis() {
 JSPredicateForm pf = new JSPredicateForm();
 pf.JSPredicateFormInit(createTokenizer("p"));
 fail("Should have thrown JSParserError");
 }

 @Test(expected = JSParserError.class)
 public void testUnexpectedToken() {
 JSPredicateForm pf = new JSPredicateForm();
 pf.JSPredicateFormInit(createTokenizer("(not %%%)"));
 fail("Should have thrown JSParserError");
 }

 @Test(expected = JSParserError.class)
 public void testNotOperatorWithoutPredicate() {
 JSPredicateForm pf = new JSPredicateForm();
 pf.JSPredicateFormInit(createTokenizer("(not)"));
 fail("Should have thrown JSParserError");
 }

 @Test
 public void testNotOperator() {
 JSPredicateForm pf = new JSPredicateForm();
 pf.JSPredicateFormInit(createTokenizer("(not p)"));
 JSPredicateForm notPF = pf.clonePF();
 JSPredicateForm expectedPF = new JSPredicateForm("p");
 assertTrue(pf.equals(expectedPF));
 assertTrue(notPF.equals(pf.negated()));
 }

 @Test
 public void testAndOperator() {
 JSPredicateForm pf = new JSPredicateForm();
 pf.JSPredicateFormInit(createTokenizer("(and (= x 3) (= x 4))"));
 JSTerm t1 = new JSTerm("=", "x", 3);
 JSTerm t2 = new JSTerm("=", "x", 4);
 JSTerm expectedT1 = t1.cloneT();
 JSTerm expectedT2 = t2.cloneT();
 Vector<JSTerm> expected = new Vector<>();
 expected.add(expectedT1);
 expected.add(expectedT2);
 assertEquals(expected, pf);
 }

 @Test(expected = JSParserError.class)
 public void testUnexpectedOperator() {
 JSPredicateForm pf = new JSPredicateForm();
 pf.JSPredicateFormInit(createTokenizer("(or x 3)"));
 fail("Should have thrown JSParserError");
 }

 @Test(expected = JSParserError.class)
 public void testMissingRightParenthesis() {
 JSPredicateForm pf = new JSPredicateForm();
 pf.JSPredicateFormInit(createTokenizer("(not p) "));
 fail("Should have thrown JSParserError");
 }

 @Test
 public void testPrint() {
 JSPredicateForm pf = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 System.out.println("Original: " + pf);
 pf.print();
 System.out.println("Printed: " + pf);
 assertEquals("(not (or (= x 3) (= x 4)))", pf.toString());
 }

 @Test
 public void testToString() {
 JSPredicateForm pf = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 String str = pf.toStr().toString();
 assertEquals("(not (or (= x 3) (= x 4)))", str);
 }

 @Test
 public void testClonePF() {
 JSPredicateForm pf = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 JSPredicateForm clonedPF = pf.clonePF();
 assertEquals("(not (or (= x 3) (= x 4)))", clonedPF.toString());
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm pf = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 JSSubstitution alpha = new JSSubstitution();
 alpha.put("x", new JSTerm("y"));
 JSPredicateForm expectedPF = new JSPredicateForm("(not (or (= y 3) (= y 4)))");
 JSPredicateForm resultPF = pf.applySubstitutionPF(alpha);
 assertEquals(expectedPF, resultPF);
 }

 @Test
 public void testMatches() {
 JSPredicateForm pf1 = new JSPredicateForm("(or (= x 3) (= x 4))");
 JSPredicateForm pf2 = new JSPredicateForm("(and (= x 3) (= x 4))");
 JSPredicateForm pf3 = new JSPredicateForm("(not (= x 3) (= x 4))");
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = new JSSubstitution();
 JSSubstitution gamma = new JSSubstitution();
 assertEquals(beta, pf1.matches(pf1, alpha));
 assertEquals(gamma, pf1.matches(pf2, alpha));
 assertEquals(gamma, pf1.matches(pf3, alpha));
 }

 @Test
 public void testEquals() {
 JSPredicateForm pf1 = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 JSPredicateForm pf2 = new JSPredicateForm("(and (= x 3) (= x 4))");
 JSPredicateForm pf3 = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 assertEquals(pf1, pf3);
 assertNotEquals(pf1, pf2);
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm pf = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 JSPredicateForm expectedPF = new JSPredicateForm("(not (or (= x 3) (= x 4)))");
 assertEquals(expectedPF, pf.standarizerPredicateForm());
 }
}