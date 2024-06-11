import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

 @Test
 public void testEmptyJSPredicateForm() {
 JSPredicateForm predicateForm = new JSPredicateForm();
 assertEquals("Predicateform.print(): 0 elements", predicateForm.print().toString());
 }

 @Test
 public void testJSPredicateFormWithNotOperatorAndSinglePredicate() {
 JSPredicateForm predicateForm = new JSPredicateForm("( not P )");
 String expected = "( (not P) )";
 assertEquals(expected, predicateForm.toStr().toString());
 }

 @Test
 public void testJSPredicateFormWithNotOperatorAndNestedPredicates() {
 JSPredicateForm predicateForm = new JSPredicateForm("( not ( not ( not P ) ) )");
 String expected = "( (not ( (not P) ) ) )";
 assertEquals(expected, predicateForm.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormWithMissingLeftParenthesis() {
 JSPredicateForm predicateForm = new JSPredicateForm("not P");
 fail("Expected JSParserError");
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormWithMissingRightParenthesis() {
 JSPredicateForm predicateForm = new JSPredicateForm("( not P");
 fail("Expected JSParserError");
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormWithInvalidOperator() {
 JSPredicateForm predicateForm = new JSPredicateForm("( not %%% )");
 fail("Expected JSParserError");
 }

 @Test
 public void testJSPredicateFormClone() {
 JSPredicateForm originalPredicateForm = new JSPredicateForm("( not ( and ( P ) ( Q ) ) )");
 JSPredicateForm clonedPredicateForm = originalPredicateForm.clonePF();
 assertEquals(originalPredicateForm.toStr().toString(), clonedPredicateForm.toStr().toString());
 }

 @Test
 public void testJSPredicateFormApplySubstitution() {
 JSSubstitution substitution = new JSSubstitution();
 JSPredicateForm predicateForm = new JSPredicateForm("( and ( = X 3 ) ( = X 4 ) )");
 JSTerm term = new JSTerm("X");
 JSTerm substitutedTerm = term.applySubstitutionT(substitution);
 substitution.addElement(new JSSubstitutionAssignment("X", new JSTerm(3)));
 JSPredicateForm expectedPredicateForm = new JSPredicateForm("( and ( = 3 3 ) ( = 3 4 ) )");
 JSPredicateForm actualPredicateForm = predicateForm.applySubstitutionPF(substitution);
 assertEquals(expectedPredicateForm.toStr().toString(), actualPredicateForm.toStr().toString());
 }
}