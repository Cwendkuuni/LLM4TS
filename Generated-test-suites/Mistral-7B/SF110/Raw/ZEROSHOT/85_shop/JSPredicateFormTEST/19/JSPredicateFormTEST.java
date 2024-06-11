package umd.cs.shop.test;

import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

public class JSPredicateFormTest {

 private StreamTokenizer createTokenizer(String input) {
 return new StreamTokenizer(new StringReader(input));
 }

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertEquals(0, jspf.size());
 }

 @Test
 public void testStringConstructor() {
 String input = "!(p(x))";
 JSPredicateForm jspf = new JSPredicateForm(input);
 assertEquals(1, jspf.size());
 assertEquals("!", jspf.elementAt(0));
 JSPredicateForm not = (JSPredicateForm) jspf.elementAt(0);
 assertEquals(1, not.size());
 assertEquals("p", not.elementAt(0));
 JSPredicateForm p = (JSPredicateForm) not.elementAt(0);
 assertEquals(1, p.size());
 assertEquals("x", p.elementAt(0));
 }

 @Test
 public void testTokenizerConstructor() {
 String input = "!(p(x))";
 StreamTokenizer tokenizer = createTokenizer(input);
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 assertEquals(1, jspf.size());
 assertEquals("!", jspf.elementAt(0));
 JSPredicateForm not = (JSPredicateForm) jspf.elementAt(0);
 assertEquals(1, not.size());
 assertEquals("p", not.elementAt(0));
 JSPredicateForm p = (JSPredicateForm) not.elementAt(0);
 assertEquals(1, p.size());
 assertEquals("x", p.elementAt(0));
 }

 @Test
 public void testPrint() {
 String input = "!(p(x))";
 JSPredicateForm jspf = new JSPredicateForm(input);
 String expected = "((!)(p(x)))";
 jspf.print();
 assertEquals(expected, JSUtil.output);
 }

 @Test
 public void testToString() {
 String input = "!(p(x))";
 JSPredicateForm jspf = new JSPredicateForm(input);
 String expected = "((!)(p(x)))";
 assertEquals(expected, jspf.toStr().toString());
 }

 @Test
 public void testClonePF() {
 String input = "!(p(x) & q(y))";
 JSPredicateForm jspf = new JSPredicateForm(input);
 JSPredicateForm cloned = jspf.clonePF();
 assertEquals(jspf, cloned);
 }

 @Test
 public void testApplySubstitutionPF() {
 String input = "!(p(x) & q(y))";
 JSPredicateForm jspf = new JSPredicateForm(input);
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("x", new JSTerm("z"));
 JSPredicateForm result = jspf.applySubstitutionPF(alpha);
 String expected = "!(p(z) & q(y))";
 assertEquals(expected, result.toStr().toString());
 }

 @Test
 public void testMatches() {
 String input1 = "!(p(x) & q(y))";
 String input2 = "!(p(z) & r(w))";
 JSPredicateForm jspf1 = new JSPredicateForm(input1);
 JSPredicateForm jspf2 = new JSPredicateForm(input2);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = jspf1.matches(jspf2);
 assertFalse(beta.isEmpty());
 assertEquals("x", beta.getElementAt(0).getName());
 assertEquals("z", beta.getElementAt(1).getName());
 }

 @Test
 public void testApplySubstitutionPFWithEmptySubstitution() {
 String input = "!(p(x) & q(y))";
 JSPredicateForm jspf = new JSPredicateForm(input);
 JSSubstitution alpha = new JSSubstitution();
 JSPredicateForm result = jspf.applySubstitutionPF(alpha);
 assertEquals(jspf, result);
 }

 @Test
 public void testEquals() {
 String input1 = "!(p(x) & q(y))";
 String input2 = "!(p(x) & q(y))";
 String input3 = "!(p(x) & r(y))";
 JSPredicateForm jspf1 = new JSPredicateForm(input1);
 JSPredicateForm jspf2 = new JSPredicateForm(input2);
 JSPredicateForm jspf3 = new JSPredicateForm(input3);
 assertTrue(jspf1.equals(jspf2));
 assertFalse(jspf1.equals(jspf3));
 }

 @Test
 public void testStandarizerPredicateForm() {
 String input = "!(p(x) & q(y))";
 JSPredicateForm jspf = new JSPredicateForm(input);
 JSPredicateForm result = jspf.standarizerPredicateForm();
 String expected = "(!(p(x)) & !(q(y)) )";
 assertEquals(expected, result.toStr().toString());
 }

 @Test
 public void testStandarizerPredicateFormEmpty() {
 JSPredicateForm jspf = new JSPredicateForm();
 JSPredicateForm result = jspf.standarizerPredicateForm();
 assertEquals("()", result.toStr().toString());
 }
}