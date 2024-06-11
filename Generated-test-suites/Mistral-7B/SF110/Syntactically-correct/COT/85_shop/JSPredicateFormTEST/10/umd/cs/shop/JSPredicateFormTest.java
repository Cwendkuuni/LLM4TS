package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 private JSPredicateForm createPredicateForm(String text) {
 return new JSPredicateForm(text);
 }

 @Test
 public void testJSPredicateFormEmptyConstructor() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertTrue(jspf.isEmpty());
 }

 @Test
 public void testJSPredicateFormWithTextConstructor() {
 JSPredicateForm jspf = createPredicateForm("(x > 5)");
 assertEquals("(x > 5)", jspf.toString());
 }

 @Test
 public void testJSPredicateFormWithStreamTokenizerConstructor() {
 JSPredicateForm jspf = createPredicateForm("(not (and (= x 5) (= y 3)))");
 assertEquals("(not (and (= x 5) (= y 3)))", jspf.toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithNullTokenizer() {
 JSPredicateForm jspf = new JSPredicateForm(null);
 fail("Should throw JSParserError");
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithInvalidTokenType() {
 StringReader sr = new StringReader("(not (and (= x 5) a))");
 StreamTokenizer st = new StreamTokenizer(sr);
 JSPredicateForm jspf = new JSPredicateForm(st);
 fail("Should throw JSParserError");
 }

 @Test
 public void testJSPredicateFormPrint() {
 JSPredicateForm jspf = createPredicateForm("(not (and (= x 5) (= y 3)))");
 jspf.print();
 assertEquals("(not (and (= x 5) (= y 3)))", jspf.toString());
 }

 @Test
 public void testJSPredicateFormToString() {
 JSPredicateForm jspf = createPredicateForm("(not (and (= x 5) (= y 3)))");
 assertEquals("(not (and (= x 5) (= y 3)))", jspf.toString());
 }

 @Test
 public void testJSPredicateFormClonePF() {
 JSPredicateForm jspf = createPredicateForm("(not (and (= x 5) (= y 3)))");
 JSPredicateForm clonedPF = jspf.clonePF();
 assertEquals(jspf.toString(), clonedPF.toString());
 }

 @Test
 public void testJSPredicateFormApplySubstitutionPF() {
 JSPredicateForm jspf = createPredicateForm("(not (and (= x 5) (= y 3)))");
 JSTerm term = new JSTerm("x", 5);
 JSSubstitution substitution = new JSSubstitution();
 substitution.addBinding("x", term);
 JSPredicateForm expected = new JSPredicateForm("(not (and (= 5 5) (= y 3)))");
 JSPredicateForm result = jspf.applySubstitutionPF(substitution);
 assertEquals(expected.toString(), result.toString());
 }

 @Test
 public void testJSPredicateFormMatches() {
 JSPredicateForm jspf1 = createPredicateForm("(and (= x 5) (= y 3))");
 JSPredicateForm jspf2 = createPredicateForm("(and (= x 5) (= z 3))");
 assertFalse(jspf1.matches(jspf2));
 JSSubstitution substitution = new JSSubstitution();
 substitution.addBinding("x", new JSTerm("y"));
 JSPredicateForm jspf3 = new JSPredicateForm("(and (= y 5) (= z 3))");
 JSPredicateForm expected = new JSPredicateForm("(and (= y 5) (= y 3))");
 JSPredicateForm result = jspf1.applySubstitutionPF(substitution);
 assertTrue(result.matches(jspf3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJSPredicateFormMatchesWithDifferentSize() {
 JSPredicateForm jspf1 = createPredicateForm("(and (= x 5) (= y 3))");
 JSPredicateForm jspf2 = createPredicateForm("(and (= x 5) (= y 3) (= z 3))");
 jspf1.matches(jspf2);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJSPredicateFormMatchesWithDifferentFunctor() {
 JSPredicateForm jspf1 = createPredicateForm("(and (= x 5) (= y 3))");
 JSPredicateForm jspf2 = createPredicateForm("(not (= x 5) (= y 3))");
 jspf1.matches(jspf2);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testJSPredicateFormEquals() {
 JSPredicateForm jspf1 = createPredicateForm("(and (= x 5) (= y 3))");
 JSPredicateForm jspf2 = createPredicateForm("(and (= x 5) (= y 3))");
 assertTrue(jspf1.equals(jspf2));
 }

 @Test
 public void testJSPredicateFormEqualsWithDifferentSize() {
 JSPredicateForm jspf1 = createPredicateForm("(and (= x 5) (= y 3))");
 JSPredicateForm jspf2 = createPredicateForm("(and (= x 5) (= y 3) (= z 3))");
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testJSPredicateFormEqualsWithDifferentFunctor() {
 JSPredicateForm jspf1 = createPredicateForm("(and (= x 5) (= y 3))");
 JSPredicateForm jspf2 = createPredicateForm("(not (= x 5) (= y 3))");
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testJSPredicateFormEqualsWithDifferentTerms() {
 JSPredicateForm jspf1 = createPredicateForm("(and (= x 5) (= y 3))");
 JSTerm term1 = new JSTerm("x", 5);
 JSTerm term2 = new JSTerm("y", 3);
 JSPredicateForm jspf2 = new JSPredicateForm();
 jspf2.addElement("x");
 jspf2.addElement(term1);
 jspf2.addElement("y");
 jspf2.addElement(term2);
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testJSPredicateFormStandarizerPredicateForm() {
 JSPredicateForm jspf = createPredicateForm("(not (and (= x 5) (= y 3)))");
 JSPredicateForm standardizedPF = jspf.standarizerPredicateForm();
 assertEquals("(not (and (= 5 x) (= 3 y)))", standardizedPF.toString());
 }
}