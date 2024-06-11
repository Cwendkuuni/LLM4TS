package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 @Test
 public void testEmptyForm() {
 JSPredicateForm form = new JSPredicateForm();
 assertEquals("Predicateform.print(): 0 elements", form.print().toString());
 }

 @Test
 public void testSimpleForm() {
 JSPredicateForm form = new JSPredicateForm("p(x)");
 assertEquals("(p x)", form.print().toString());
 }

 @Test
 public void testNegatedForm() {
 JSPredicateForm form = new JSPredicateForm("not p(x)");
 assertEquals("(not p x)", form.print().toString());

 JSPredicateForm negatedForm = new JSPredicateForm();
 negatedForm.addElement("not");
 negatedForm.addElement(new JSTerm("p"));
 negatedForm.addElement(new JSTerm("x"));
 assertEquals("(not p x)", negatedForm.print().toString());
 }

 @Test
 public void testNestedForm() {
 JSPredicateForm form = new JSPredicateForm("not (p(x) and q(y))");
 assertEquals("(not (p x) and not (q y))", form.print().toString());

 JSPredicateForm nestedForm = new JSPredicateForm();
 nestedForm.addElement("not");
 nestedForm.addElement(new JSTerm("("));
 nestedForm.addElement(new JSTerm("p"));
 nestedForm.addElement("x");
 nestedForm.addElement("and");
 nestedForm.addElement(new JSTerm("q"));
 nestedForm.addElement("y");
 nestedForm.addElement(new JSTerm(")"));
 assertEquals("(not (p x) and not (q y))", nestedForm.print().toString());
 }

 @Test(expected = JSParserError.class)
 public void testMissingLeftParenthesis() {
 JSPredicateForm form = new JSPredicateForm("p(x)q(y)");
 fail("Should have thrown JSParserError for missing left parenthesis");
 }

 @Test(expected = JSParserError.class)
 public void testMissingRightParenthesis() {
 JSPredicateForm form = new JSPredicateForm("not p(x)");
 form.addElement(new JSTerm("x"));
 fail("Should have thrown JSParserError for missing right parenthesis");
 }

 @Test(expected = JSParserError.class)
 public void testUnknownOperator() {
 JSPredicateForm form = new JSPredicateForm("not p(x) foo(y)");
 fail("Should have thrown JSParserError for unknown operator 'foo'");
 }

 @Test
 public void testClone() {
 JSPredicateForm original = new JSPredicateForm("not (p(x) and q(y))");
 JSPredicateForm cloned = original.clonePF();
 assertEquals("(not (p x) and not (q y))", cloned.print().toString());
 }

 @Test
 public void testApplySubstitution() {
 JSPredicateForm form = new JSPredicateForm("p(x)");
 final String x = "a";
 JSSubstitution substitution = new JSSubstitution();
 substitution.addMapping("x", x);
 JSPredicateForm transformed = form.applySubstitutionPF(substitution);
 assertEquals("p(" + x + ")", transformed.print().toString());
 }

 @Test
 public void testMatches() {
 JSPredicateForm form1 = new JSPredicateForm("p(x) and q(y)");
 JSPredicateForm form2 = new JSPredicateForm("r(a) and s(b)");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addMapping("x", "a");
 substitution.addMapping("y", "b");
 JSPredicateForm matched = form1.matches(form2, substitution);
 assertEquals("p(a) and q(b)", matched.print().toString());
 }

 @Test
 public void testMatchesFailure() {
 JSPredicateForm form1 = new JSPredicateForm("p(x) and q(y)");
 JSPredicateForm form2 = new JSPredicateForm("p(x) or q(y)");
 JSSubstitution substitution = new JSSubstitution();
 JSPredicateForm failure = form1.matches(form2, substitution);
 assertTrue(failure.fail());
 }

 @Test
 public void testEquals() {
 JSPredicateForm form1 = new JSPredicateForm("p(x)");
 JSPredicateForm form2 = new JSPredicateForm("p(x)");
 JSPredicateForm form3 = new JSPredicateForm("q(x)");
 assertTrue(form1.equals(form2));
 assertFalse(form1.equals(form3));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm form = new JSPredicateForm("not (p(x) or q(y))");
 JSPredicateForm standardized = form.standarizerPredicateForm();
 assertEquals("(not (or (p x) (q y)))", standardized.print().toString());
 }
}