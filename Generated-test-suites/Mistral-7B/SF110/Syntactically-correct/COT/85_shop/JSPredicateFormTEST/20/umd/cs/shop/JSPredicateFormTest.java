package umd.cs.shop.test;

import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

public class JSPredicateFormTest {

 private static StreamTokenizer createTokenizer(String text) {
 return new StreamTokenizer(new StringReader(text));
 }

 @Test
 public void testDefaultConstructor() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertTrue(jspf.isEmpty());
 }

 @Test
 public void testConstructorWithNullText() {
 try {
 JSPredicateForm jspf = new JSPredicateForm(null);
 fail("Exception expected");
 } catch (JSParserError e) {
 // expected
 }
 }

 @Test
 public void testConstructorWithText() {
 String text = "not (p(x) v q(x))";
 JSPredicateForm jspf = new JSPredicateForm(text);
 assertEquals(2, jspf.size());
 assertEquals("not", jspf.elementAt(0));
 JSPredicateForm innerPredicate = (JSPredicateForm) jspf.elementAt(1);
 assertEquals(2, innerPredicate.size());
 assertEquals("p", innerPredicate.elementAt(0));
 assertEquals(1, innerPredicate.elementAt(1).size());
 assertEquals("x", innerPredicate.elementAt(1).elementAt(0).toString());
 }

 @Test
 public void testConstructorWithTokenizer() {
 String text = "not (p(x) v q(x))";
 StreamTokenizer tokenizer = createTokenizer(text);
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 assertEquals(2, jspf.size());
 assertEquals("not", jspf.elementAt(0));
 JSPredicateForm innerPredicate = (JSPredicateForm) jspf.elementAt(1);
 assertEquals(2, innerPredicate.size());
 assertEquals("p", innerPredicate.elementAt(0));
 assertEquals(1, innerPredicate.elementAt(1).size());
 assertEquals("x", innerPredicate.elementAt(1).elementAt(0).toString());
 }

 @Test
 public void testJSPredicateFormInit_NotOperator() {
 String text = "not p";
 StreamTokenizer tokenizer = createTokenizer(text);
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.JSPredicateFormInit(tokenizer);
 assertEquals(1, jspf.size());
 assertEquals("not", jspf.elementAt(0));
 }

 @Test
 public void testJSPredicateFormInit_NotWithPredicate() {
 String text = "not (p(x))";
 StreamTokenizer tokenizer = createTokenizer(text);
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.JSPredicateFormInit(tokenizer);
 assertEquals(2, jspf.size());
 assertEquals("not", jspf.elementAt(0));
 JSPredicateForm innerPredicate = (JSPredicateForm) jspf.elementAt(1);
 assertEquals(1, innerPredicate.size());
 assertEquals("p", innerPredicate.elementAt(0).toString());
 }

 @Test
 public void testJSPredicateFormInit_CallOperator() {
 String text = "call f(x)";
 StreamTokenizer tokenizer = createTokenizer(text);
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.JSPredicateFormInit(tokenizer);
 assertEquals(1, jspf.size());
 assertEquals("call", jspf.elementAt(0));
 JSTerm term = (JSTerm) jspf.elementAt(1);
 assertEquals("f", term.getFunctionName());
 assertEquals(1, term.size());
 assertEquals("x", term.elementAt(0).toString());
 }

 @Test
 public void testJSPredicateFormInit_NotWithPredicateForm() {
 String text = "not (p(x) v q(x))";
 StreamTokenizer tokenizer = createTokenizer(text);
 JSPredicateForm jspf1 = new JSPredicateForm();
 jspf1.JSPredicateFormInit(tokenizer);
 JSPredicateForm jspf2 = new JSPredicateForm();
 JSTerm term = new JSTerm(tokenizer);
 term.makeEval(true);
 jspf2.addElement(term);
 assertEquals(2, jspf1.size());
 assertEquals("not", jspf1.elementAt(0));
 JSPredicateForm innerPredicate = (JSPredicateForm) jspf1.elementAt(1);
 assertEquals(2, innerPredicate.size());
 assertEquals("p", innerPredicate.elementAt(0));
 assertEquals(1, innerPredicate.elementAt(1).size());
 assertEquals("x", innerPredicate.elementAt(1).elementAt(0).toString());
 assertEquals(2, jspf2.size());
 assertEquals("", jspf2.elementAt(0).toString());
 assertEquals(jspf1, jspf2.clonePF());
 }

 @Test
 public void testPrint() {
 JSPredicateForm jspf = new JSPredicateForm("not (p(x) v q(x))");
 jspf.print();
 assertEquals("(not (p(x) v q(x)))", jspf.toStr().toString());
 }

 @Test
 public void testToStr() {
 JSPredicateForm jspf = new JSPredicateForm("not (p(x) v q(x))");
 assertEquals("(not (p(x) v q(x)))", jspf.toStr().toString());
 }

 @Test
 public void testClonePF() {
 JSPredicateForm jspf = new JSPredicateForm("not (p(x) v q(x))");
 JSPredicateForm cloned = jspf.clonePF();
 assertEquals(jspf, cloned);
 }

 @Test
 public void testApplySubstitutionPF() {
 JSTerm term1 = new JSTerm("x", new Vector<String>(), true);
 term1.assignValue("a");
 JSPredicateForm jspf1 = new JSPredicateForm("p(x)");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement(new JSSubstitutionElement("x", term1));
 JSPredicateForm jspf2 = jspf1.applySubstitutionPF(alpha);
 assertEquals("p(a)", jspf2.toStr().toString());
 }

 @Test
 public void testMatches_EmptyPredicateForm() {
 JSPredicateForm jspf1 = new JSPredicateForm();
 JSPredicateForm jspf2 = new JSPredicateForm("p(x)");
 assertFalse(jspf1.matches(jspf2, new JSSubstitution()).isSuccess());
 }

 @Test
 public void testMatches_NotOperators() {
 JSPredicateForm jspf1 = new JSPredicateForm("not p");
 JSPredicateForm jspf2 = new JSPredicateForm("not q");
 JSSubstitution beta = jspf1.matches(jspf2, new JSSubstitution());
 assertTrue(beta.isFailure());
 }

 @Test
 public void testMatches_DifferentFunctions() {
 JSPredicateForm jspf1 = new JSPredicateForm("p(x)");
 JSPredicateForm jspf2 = new JSPredicateForm("q(x)");
 JSSubstitution beta = jspf1.matches(jspf2, new JSSubstitution());
 assertTrue(beta.isFailure());
 }

 @Test
 public void testMatches_DifferentTermsInSameFunction() {
 JSTerm term1 = new JSTerm("x", new Vector<String>(), true);
 term1.assignValue("a");
 JSTerm term2 = new JSTerm("y", new Vector<String>(), true);
 term2.assignValue("b");
 JSPredicateForm jspf1 = new JSPredicateForm("p(x)");
 JSPredicateForm jspf2 = new JSPredicateForm("p(y)");
 JSSubstitution beta = jspf1.matches(jspf2, new JSSubstitution());
 assertTrue(beta.isFailure());
 }

 @Test
 public void testMatches_SameTermsInSameFunction() {
 JSTerm term1 = new JSTerm("x", new Vector<String>(), true);
 term1.assignValue("a");
 JSTerm term2 = new JSTerm("x", new Vector<String>(), true);
 term2.assignValue("a");
 JSPredicateForm jspf1 = new JSPredicateForm("p(x)");
 JSPredicateForm jspf2 = new JSPredicateForm("p(x)");
 JSSubstitution beta = jspf1.matches(jspf2, new JSSubstitution());
 assertTrue(beta.isSuccess());
 assertEquals(jspf1, jspf2);
 }

 @Test
 public void testMatches_SamePredicateForm() {
 JSPredicateForm jspf1 = new JSPredicateForm("p(x)");
 JSPredicateForm jspf2 = new JSPredicateForm("p(x)");
 JSSubstitution beta = jspf1.matches(jspf2, new JSSubstitution());
 assertTrue(beta.isSuccess());
 assertEquals(jspf1, jspf2);
 }

 @Test
 public void testEquals_NullPredicateForm() {
 JSPredicateForm jspf1 = new JSPredicateForm("p(x)");
 assertFalse(jspf1.equals(null));
 }

 @Test
 public void testEquals_DifferentPredicateForms() {
 JSPredicateForm jspf1 = new JSPredicateForm("p(x)");
 JSPredicateForm jspf2 = new JSPredicateForm("q(x)");
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testEquals_SamePredicateForms() {
 JSPredicateForm jspf1 = new JSPredicateForm("p(x)");
 JSPredicateForm jspf2 = new JSPredicateForm("p(x)");
 assertTrue(jspf1.equals(jspf2));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm jspf = new JSPredicateForm("not p(x) v q(y)");
 JSPredicateForm expected = new JSPredicateForm("not (p(x) v q(y))");
 JSPredicateForm result = jspf.standarizerPredicateForm();
 assertEquals(expected, result);
 }

}