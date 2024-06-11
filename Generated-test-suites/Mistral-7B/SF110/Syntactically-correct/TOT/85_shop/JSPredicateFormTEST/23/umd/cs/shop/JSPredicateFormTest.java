import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 @Test
 public void testEmptyJSPredicateForm() {
 JSPredicateForm predicateForm = new JSPredicateForm();
 assertEquals("", predicateForm.toStr().toString());
 }

 @Test
 public void testJSPredicateFormWithSingleElement() {
 JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(a)")));
 assertEquals("(a)", predicateForm.toStr().toString());
 }

 @Test
 public void testJSPredicateFormWithNotOperator() {
 JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(not (a))")));
 assertEquals("(!a)", predicateForm.toStr().toString());
 }

 @Test
 public void testJSPredicateFormWithNestedNotOperator() {
 JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(not (not (a)))")));
 assertEquals("(a)", predicateForm.toStr().toString());
 }

 @Test
 public void testJSPredicateFormWithAndOperator() {
 JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("((a) (b))")));
 assertEquals("((a) (b))", predicateForm.toStr().toString());
 }

 @Test
 public void testJSPredicateFormWithOrOperator() {
 JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("((a) (or b c))")));
 assertEquals("((a) (or b c))", predicateForm.toStr().toString());
 }

 @Test
 public void testJSPredicateFormWithCallOperator() {
 JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("((call f (a))")));
 assertEquals("((call f (a)))", predicateForm.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormWithInvalidInput() {
 JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("invalidInput")));
 }

 @Test
 public void testJSPredicateFormEquals() {
 JSPredicateForm predicateForm1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(a)")));
 JSPredicateForm predicateForm2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(a)")));
 assertEquals(predicateForm1, predicateForm2);
 }

 @Test
 public void testJSPredicateFormNotEquals() {
 JSPredicateForm predicateForm1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(a)")));
 JSPredicateForm predicateForm2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(b)")));
 assertFalse(predicateForm1.equals(predicateForm2));
 }

 @Test
 public void testJSPredicateFormApplySubstitutionPF() {
 JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(a)")));
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("a", new JSTerm("b"));
 JSPredicateForm result = predicateForm.applySubstitutionPF(alpha);
 assertEquals("(b)", result.toStr().toString());
 }

 @Test
 public void testJSPredicateFormClonePF() {
 JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(a)")));
 JSPredicateForm clonedPF = predicateForm.clonePF();
 assertEquals(predicateForm, clonedPF);
 }

 @Test
 public void testJSPredicateFormMatches() {
 JSPredicateForm predicateForm1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(a)")));
 JSPredicateForm predicateForm2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(b)")));
 JSSubstitution result = predicateForm1.matches(predicateForm2);
 assertFalse(result.hasBindings());
 }

 @Test
 public void testJSPredicateFormMatchesWithBinding() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("a")));
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("b")));
 term1.assignBinding("a", term2);
 JSPredicateForm predicateForm1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(a)")));
 JSPredicateForm predicateForm2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(b)")));
 JSSubstitution result = predicateForm1.matches(predicateForm2, new JSSubstitution());
 assertTrue(result.hasBindings());
 assertEquals(term2, result.bindingAt("a"));
 }
}