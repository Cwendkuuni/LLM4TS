package umd.cs.shop.test;

import static org.junit.Assert.*;

import org.junit.Test;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;

public class JSPredicateFormTest {

 private JSPredicateForm createJSPredicateForm(String text) {
 return new JSPredicateForm(text);
 }

 @Test
 public void testJSPredicateFormEmptyConstructor() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertTrue(jspf.size() == 0);
 }

 @Test
 public void testJSPredicateFormWithTextConstructor() {
 JSPredicateForm jspf = createJSPredicateForm("(not p)");
 assertEquals("not p", jspf.elementAt(0).toString());
 assertTrue(jspf.size() == 1);
 }

 @Test
 public void testJSPredicateFormWithTokenizerConstructor() {
 JSPredicateForm jspf = createJSPredicateForm("(not (or a b) p)");
 assertEquals("not", jspf.elementAt(0).toString());
 assertTrue(jspf.size() == 3);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithNullTokenizer() {
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.JSPredicateFormInit(null);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithMismatchedParentheses() {
 JSPredicateForm jspf = new JSPredicateForm();
 String text = "(not (p) )";
 jspf.JSPredicateFormInit(new StreamTokenizer(new StringReader(text)));
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithUnexpectedOperator() {
 JSPredicateForm jspf = new JSPredicateForm();
 String text = "(% %%%)";
 jspf.JSPredicateFormInit(new StreamTokenizer(new StringReader(text)));
 }

 @Test
 public void testPrintMethod() {
 JSPredicateForm jspf = createJSPredicateForm("(not (or a b) p)");
 jspf.print();
 assertEquals("(not (or a b) p)", JUtil.readLine());
 }

 @Test
 public void testToStringMethod() {
 JSPredicateForm jspf = createJSPredicateForm("(not (or a b) p)");
 String str = jspf.toStr().toString();
 assertEquals("(not (or a b) p)", str);
 }

 @Test
 public void testClonePFMethod() {
 JSPredicateForm jspf = createJSPredicateForm("(not (or a b) p)");
 JSPredicateForm clonedPF = jspf.clonePF();
 assertEquals(jspf, clonedPF);
 }

 @Test
 public void testApplySubstitutionPFMethod() {
 JSPredicateForm jspf = createJSPredicateForm("(not (or a b) p)");
 JSSubstitution alpha = new JSSubstitution();
 JSPredicateForm result = jspf.applySubstitutionPF(alpha);
 assertTrue(result.size() == 3);
 String expected = "(not (or a1 b1) p)";
 assertEquals(expected, result.toStr().toString());
 }

 @Test
 public void testMatchesMethodWithEmptySubstitution() {
 JSPredicateForm jspf1 = createJSPredicateForm("(not (or a b) p)");
 JSPredicateForm jspf2 = createJSPredicateForm("(not (and a b) q)");
 JSSubstitution matchesResult = jspf1.matches(jspf2);
 assertTrue(matchesResult.isEmpty());
 }

 @Test
 public void testMatchesMethodWithSubstitution() {
 JSPredicateForm jspf1 = createJSPredicateForm("(not (or a b) p)");
 JSPredicateForm jspf2 = createJSPredicateForm("(not (and a1 b1) q1)");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("a", new VariableTerm("a1"));
 alpha.addBinding("b", new VariableTerm("b1"));
 JSSubstitution beta = jspf1.matches(jspf2, alpha);
 assertEquals(2, beta.size());
 assertEquals("a1", beta.getValue("a").toString());
 assertEquals("b1", beta.getValue("b").toString());
 }

 @Test
 public void testEqualsMethod() {
 JSPredicateForm jspf1 = createJSPredicateForm("(not (or a b) p)");
 JSPredicateForm jspf2 = createJSPredicateForm("(not (or a b) p)");
 assertTrue(jspf1.equals(jspf2));
 }

 @Test
 public void testEqualsMethodWithDifferentSize() {
 JSPredicateForm jspf1 = createJSPredicateForm("(not (or a b) p)");
 JSPredicateForm jspf2 = createJSPredicateForm("(not (or a b c) p)");
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testEqualsMethodWithDifferentFunctor() {
 JSPredicateForm jspf1 = createJSPredicateForm("(not (or a b) p)");
 JSPredicateForm jspf2 = createJSPredicateForm("(and (or a b) p)");
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testEqualsMethodWithDifferentElements() {
 JSPredicateForm jspf1 = createJSPredicateForm("(not (or a b) p)");
 JSPredicateForm jspf2 = createJSPredicateForm("(not (or c d) p)");
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testStandarizerPredicateFormMethod() {
 JSPredicateForm jspf = createJSPredicateForm("(not (or a (not b) c) d)");
 JSPredicateForm standardizedJSPF = jspf.standarizerPredicateForm();
 assertEquals("(not (or (not b) (or a c) d)", standardizedJSPF.toStr().toString());
 }
}