import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

 @Test
 public void testEmptyForm() {
 JSPredicateForm emptyForm = new JSPredicateForm();
 emptyForm.print();
 assertTrue(emptyForm.size() == 0);
 }

 @Test
 public void testFormWithSingleElement() {
 String text = "A";
 JSPredicateForm form = new JSPredicateForm(text);
 form.print();
 String expected = "(A)";
 assertEquals(expected, form.toStr().toString());
 }

 @Test
 public void testFormWithNotOperator() {
 String text = "not (A)";
 JSPredicateForm form = new JSPredicateForm(text);
 form.print();
 String expected = "(not (A))";
 assertEquals(expected, form.toStr().toString());
 }

 @Test
 public void testFormWithNestedNotOperator() {
 String text = "not (not (A))";
 JSPredicateForm form = new JSPredicateForm(text);
 form.print();
 String expected = "(not (not (A)))";
 assertEquals(expected, form.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testFormWithMissingLeftParenthesis() {
 String text = "A)";
 try {
 JSPredicateForm form = new JSPredicateForm(text);
 } catch (JSParserError e) {
 String message = e.getMessage();
 assertTrue(message.contains("expected '('"));
 }
 }

 @Test(expected = JSParserError.class)
 public void testFormWithMissingRightParenthesis() {
 String text = "not (A";
 try {
 JSPredicateForm form = new JSPredicateForm(text);
 } catch (JSParserError e) {
 String message = e.getMessage();
 assertTrue(message.contains("Unexpected ) while reading Predicate"));
 }
 }

 @Test(expected = JSParserError.class)
 public void testFormWithInvalidOperator() {
 String text = "not %%%";
 try {
 JSPredicateForm form = new JSPredicateForm(text);
 } catch (JSParserError e) {
 String message = e.getMessage();
 assertTrue(message.contains("Operator name expected"));
 }
 }

 @Test(expected = JSParserError.class)
 public void testFormWithInvalidKeyword() {
 String text = "not call";
 try {
 JSPredicateForm form = new JSPredicateForm(text);
 } catch (JSParserError e) {
 String message = e.getMessage();
 assertTrue(message.contains("PlanningDomain: error"));
 }
 }

 @Test(expected = JSParserError.class)
 public void testFormWithMissingPredicate() {
 String text = "()";
 try {
 JSPredicateForm form = new JSPredicateForm(text);
 } catch (JSParserError e) {
 String message = e.getMessage();
 assertTrue(message.contains("JSPredicateForm"));
 }
 }

 @Test
 public void testFormWithMultiplePredicates() {
 String text = "(A) (B)";
 JSPredicateForm form = new JSPredicateForm(text);
 form.print();
 String expected = "(A) (B)";
 assertEquals(expected, form.toStr().toString());
 }

 @Test
 public void testFormCloning() {
 String text = "(A) (B)";
 JSPredicateForm originalForm = new JSPredicateForm(text);
 JSPredicateForm clonedForm = originalForm.clonePF();
 assertEquals(originalForm.toStr().toString(), clonedForm.toStr().toString());
 }

 @Test
 public void testFormApplyingSubstitution() {
 String text = "(A(x))";
 JSPredicateForm form = new JSPredicateForm(text);
 JSSubstitution substitution = new JSSubstitution();
 substitution.bind("x", new JSTerm("B"));
 JSPredicateForm expectedForm = new JSPredicateForm();
 expectedForm.addElement("not");
 expectedForm.addElement(new JSTerm("A"));
 expectedForm.addElement(new JSTerm("B"));
 JSPredicateForm resultForm = form.applySubstitutionPF(substitution);
 assertEquals(expectedForm.toStr().toString(), resultForm.toStr().toString());
 }

 @Test
 public void testFormMatching() {
 String text1 = "(A(x))";
 String text2 = "(B(x))";
 JSPredicateForm form1 = new JSPredicateForm(text1);
 JSPredicateForm form2 = new JSPredicateForm(text2);
 JSSubstitution substitution = new JSSubstitution();
 substitution.bind("x", new JSTerm("B"));
 JSSubstitution result = form1.matches(form2, substitution);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testFormMatchingWithSuccess() {
 String text1 = "(A(x))";
 String text2 = "(A(x))";
 JSPredicateForm form1 = new JSPredicateForm(text1);
 JSPredicateForm form2 = new JSPredicateForm(text2);
 JSSubstitution substitution = new JSSubstitution();
 substitution.bind("x", new JSTerm("B"));
 JSSubstitution result = form1.matches(form2, substitution);
 assertEquals(substitution, result);
 }
}