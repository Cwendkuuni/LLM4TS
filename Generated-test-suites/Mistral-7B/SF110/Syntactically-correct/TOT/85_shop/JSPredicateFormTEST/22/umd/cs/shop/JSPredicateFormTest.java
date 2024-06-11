import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

 @Test
 public void testEmptyForm() {
 JSPredicateForm form = new JSPredicateForm();
 assertTrue(form.size() == 0);
 }

 @Test
 public void testFormWithOneElement() {
 JSPredicateForm form = new JSPredicateForm("(A)");
 assertEquals("(A)", form.toStr().toString());
 }

 @Test
 public void testFormWithNotOperator() {
 JSPredicateForm form = new JSPredicateForm("(not (A))");
 JSPredicateForm expectedForm = new JSPredicateForm();
 expectedForm.addElement("not");
 expectedForm.addElement(new JSTerm("A"));
 assertEquals(expectedForm, form);
 }

 @Test
 public void testFormWithNestedNotOperator() {
 JSPredicateForm form = new JSPredicateForm("(not (not A))");
 JSPredicateForm expectedForm = new JSPredicateForm();
 expectedForm.addElement("not");
 expectedForm.addElement(new JSTerm("not"));
 expectedForm.addElement(new JSTerm("A"));
 assertEquals(expectedForm, form);
 }

 @Test
 public void testFormWithCallOperator() {
 JSPredicateForm form = new JSPredicateForm("(call F(A))");
 JSTerm term = new JSTerm("F");
 term.setFormalParameters(new String[]{"A"});
 assertEquals(term, form.elementAt(1));
 }

 @Test(expected = JSParserError.class)
 public void testFormWithInvalidOperator() {
 JSPredicateForm form = new JSPredicateForm("(invalidOperator A)");
 fail("Should throw JSParserError exception");
 }

 @Test(expected = JSParserError.class)
 public void testFormWithMissingRightParenthesis() {
 JSPredicateForm form = new JSPredicateForm("(A)");
 form.addElement(new JSTerm("B"));
 fail("Should throw JSParserError exception");
 }

 @Test
 public void testFormWithEqualElements() {
 JSPredicateForm form1 = new JSPredicateForm("(A)");
 JSPredicateForm form2 = new JSPredicateForm("(A)");
 assertEquals(form1, form2);
 }

 @Test
 public void testFormWithNotEqualElements() {
 JSPredicateForm form1 = new JSPredicateForm("(A)");
 JSPredicateForm form2 = new JSPredicateForm("(B)");
 assertNotEquals(form1, form2);
 }

 @Test
 public void testFormWithDifferentSizes() {
 JSPredicateForm form1 = new JSPredicateForm("(A)");
 JSPredicateForm form2 = new JSPredicateForm("(A B)");
 assertNotEquals(form1, form2);
 }

 @Test
 public void testFormCloning() {
 JSPredicateForm form = new JSPredicateForm("(not (A))");
 JSPredicateForm clonedForm = form.clonePF();
 assertEquals(form, clonedForm);
 }

 @Test
 public void testFormSubstitution() {
 JSPredicateForm form = new JSPredicateForm("(A X)");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addBinding("X", "B");
 JSPredicateForm expectedForm = new JSPredicateForm("(A B)");
 JSPredicateForm resultForm = form.applySubstitutionPF(substitution);
 assertEquals(expectedForm, resultForm);
 }

 @Test
 public void testFormMatches() {
 JSPredicateForm form1 = new JSPredicateForm("(A X)");
 JSPredicateForm form2 = new JSPredicateForm("(A B)");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addBinding("X", "B");
 JSSubstitution resultSubstitution = form1.matches(form2, substitution);
 assertTrue(resultSubstitution.size() > 0);
 }

 @Test
 public void testFormDoesNotMatch() {
 JSPredicateForm form1 = new JSPredicateForm("(A X)");
 JSPredicateForm form2 = new JSPredicateForm("(B X)");
 JSSubstitution substitution = new JSSubstitution();
 JSSubstitution resultSubstitution = form1.matches(form2, substitution);
 assertFalse(resultSubstitution.size() > 0);
 }

 @Test
 public void testStandardizerPredicateForm() {
 JSPredicateForm form = new JSPredicateForm("(not (or (A) (B))); (not (and (A) (B))));");
 JSPredicateForm expectedForm = new JSPredicateForm();
 expectedForm.addElement("not");
 JSPredicateForm andForm = new JSPredicateForm();
 andForm.addElement("and");
 JSTerm aTerm = new JSTerm("A");
 JSTerm bTerm = new JSTerm("B");
 andForm.addElement(aTerm);
 andForm.addElement(bTerm);
 expectedForm.addElement(andForm);
 JSPredicateForm orForm = new JSPredicateForm();
 orForm.addElement("or");
 expectedForm.addElement(orForm);
 expectedForm.addElement(aTerm);
 expectedForm.addElement(bTerm);
 assertEquals(expectedForm, form.standarizerPredicateForm());
 }
}