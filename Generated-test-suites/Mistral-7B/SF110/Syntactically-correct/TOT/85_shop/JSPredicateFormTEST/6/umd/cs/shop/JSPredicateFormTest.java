import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

 @Test
 public void testEmptyForm() {
 JSPredicateForm predicateForm = new JSPredicateForm();
 assertEquals(0, predicateForm.size());
 }

 @Test
 public void testFormWithSingleElement() {
 JSPredicateForm predicateForm = new JSPredicateForm("(a)");
 assertEquals(1, predicateForm.size());
 assertEquals("a", predicateForm.elementAt(0).toString());
 }

 @Test
 public void testFormWithNegation() {
 JSPredicateForm predicateForm = new JSPredicateForm("(not (a))");
 assertEquals(1, predicateForm.size());
 assertEquals("not", predicateForm.elementAt(0).toString());
 JSPredicateForm subForm = predicateForm.elementAt(1);
 assertEquals(1, subForm.size());
 assertEquals("a", subForm.elementAt(0).toString());
 }

 @Test
 public void testFormWithAnd() {
 JSPredicateForm predicateForm = new JSPredicateForm("( (a) (b) )");
 assertEquals(2, predicateForm.size());
 JSTerm firstElement = predicateForm.elementAt(0);
 assertEquals(1, firstElement.size());
 assertEquals("a", firstElement.elementAt(0).toString());
 JSTerm secondElement = predicateForm.elementAt(1);
 assertEquals(1, secondElement.size());
 assertEquals("b", secondElement.elementAt(0).toString());
 }

 @Test(expected = JSParserError.class)
 public void testFormWithMissingLeftParenthesis() {
 JSPredicateForm predicateForm = new JSPredicateForm("a)");
 // This test should throw a JSParserError exception
 }

 @Test(expected = JSParserError.class)
 public void testFormWithMissingRightParenthesis() {
 JSPredicateForm predicateForm = new JSPredicateForm("(a");
 // This test should throw a JSParserError exception
 }

 @Test(expected = JSParserError.class)
 public void testFormWithInvalidOperator() {
 JSPredicateForm predicateForm = new JSPredicateForm("(z x)");
 // This test should throw a JSParserError exception
 }

 @Test
 public void testFormToString() {
 JSPredicateForm predicateForm = new JSPredicateForm("( (not (a)) (b) )");
 assertEquals("( (not a) b )", predicateForm.toStr().toString());
 }

 @Test
 public void testFormClone() {
 JSPredicateForm predicateForm = new JSPredicateForm("( (not (a)) (b) )");
 JSPredicateForm clonedForm = predicateForm.clonePF();
 assertEquals(predicateForm, clonedForm);
 }

 @Test
 public void testFormApplySubstitution() {
 JSPredicateForm predicateForm = new JSPredicateForm("(x y)");
 JSSubstitution substitution = new JSSubstitution();
 substitution.put("x", new JSTerm("a"));
 substitution.put("y", new JSTerm("b"));
 JSPredicateForm expectedForm = new JSPredicateForm("(a b)");
 JSPredicateForm actualForm = predicateForm.applySubstitutionPF(substitution);
 assertEquals(expectedForm, actualForm);
 }

 @Test
 public void testFormMatches() {
 JSPredicateForm predicateForm1 = new JSPredicateForm("(x y)");
 JSPredicateForm predicateForm2 = new JSPredicateForm("(y x)");
 JSSubstitution substitution = new JSSubstitution();
 JSPredicateForm matchedForm = predicateForm1.matches(predicateForm2, substitution);
 assertFalse(matchedForm.isEmpty());
 assertEquals("x -> y", substitution.toString());
 }

 @Test
 public void testFormEquals() {
 JSPredicateForm predicateForm1 = new JSPredicateForm("(x y)");
 JSPredicateForm predicateForm2 = new JSPredicateForm("(x y)");
 assertTrue(predicateForm1.equals(predicateForm2));
 }

 @Test
 public void testFormNotEqual() {
 JSPredicateForm predicateForm1 = new JSPredicateForm("(x y)");
 JSPredicateForm predicateForm2 = new JSPredicateForm("(y x)");
 assertFalse(predicateForm1.equals(predicateForm2));
 }

 @Test
 public void testFormStandarizerPredicateForm() {
 JSPredicateForm predicateForm = new JSPredicateForm("( (not (a)) (b) (c) )");
 JSPredicateForm standardizedForm = predicateForm.standarizerPredicateForm();
 assertEquals("(not (a)) (b) (c)", standardizedForm.toStr().toString());
 }

}