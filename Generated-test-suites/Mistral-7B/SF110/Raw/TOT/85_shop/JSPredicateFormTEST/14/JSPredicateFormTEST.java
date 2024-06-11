import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 @Test
 public void testEmptyForm() {
 JSPredicateForm emptyForm = new JSPredicateForm();
 emptyForm.print();
 assertEquals("Predicateform.print(): 0 elements", emptyForm.toStr().toString());
 }

 @Test
 public void testSingleElementForm() {
 JSPredicateForm singleElementForm = new JSPredicateForm("A");
 singleElementForm.print();
 assertEquals("(A)", singleElementForm.toStr().toString());
 }

 @Test
 public void testNotForm() {
 JSPredicateForm notForm = new JSPredicateForm("not", new JSPredicateForm("A"));
 notForm.print();
 assertEquals("(not A)", notForm.toStr().toString());
 }

 @Test
 public void testNestedForm() {
 JSPredicateForm nestedForm = new JSPredicateForm("not", new JSPredicateForm("A", new JSPredicateForm("B", "C")));
 nestedForm.print();
 assertEquals("(not (A (B C)))", nestedForm.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testMissingLeftParenthesis() {
 JSPredicateForm missingLPForm = new JSPredicateForm("not A");
 }

 @Test(expected = JSParserError.class)
 public void testMissingRightParenthesis() {
 JSPredicateForm missingRPForm = new JSPredicateForm("not (A");
 }

 @Test(expected = JSParserError.class)
 public void testMissingOperator() {
 JSPredicateForm missingOperatorForm = new JSPredicateForm("not A B");
 }

 @Test(expected = JSParserError.class)
 public void testInvalidOperator() {
 JSPredicateForm invalidOperatorForm = new JSPredicateForm("not %%%");
 }

 @Test(expected = JSParserError.class)
 public void testInvalidWord() {
 JSPredicateForm invalidWordForm = new JSPredicateForm("not invalid");
 }

 @Test
 public void testClonePF() {
 JSPredicateForm originalForm = new JSPredicateForm("not", new JSPredicateForm("A"));
 JSPredicateForm clonedForm = originalForm.clonePF();
 assertEquals(originalForm.toStr().toString(), clonedForm.toStr().toString());
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm originalForm = new JSPredicateForm("x = A", new JSTerm("y"));
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("A", new JSTerm("B"));
 JSPredicateForm substitutedForm = originalForm.applySubstitutionPF(alpha);
 assertEquals("x = B", substitutedForm.elementAt(0).toString());
 }

 @Test
 public void testMatches() {
 JSPredicateForm p1 = new JSPredicateForm("x = A", new JSTerm("y"));
 JSPredicateForm p2 = new JSPredicateForm("x = A", new JSTerm("B"));
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("A", new JSTerm("B"));
 JSSubstitution beta = p1.matches(p2, alpha);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testMatchesWithFailure() {
 JSPredicateForm p1 = new JSPredicateForm("x = A", new JSTerm("y"));
 JSPredicateForm p2 = new JSPredicateForm("x = B", new JSTerm("z"));
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("A", new JSTerm("B"));
 JSSubstitution beta = p1.matches(p2, alpha);
 assertFalse(beta.isEmpty());
 }

 @Test
 public void testEquals() {
 JSPredicateForm p1 = new JSPredicateForm("x = A", new JSTerm("y"));
 JSPredicateForm p2 = new JSPredicateForm("x = A", new JSTerm("y"));
 assertTrue(p1.equals(p2));
 }

 @Test
 public void testNotEquals() {
 JSPredicateForm p1 = new JSPredicateForm("x = A", new JSTerm("y"));JSPredicateForm p2 = new JSPredicateForm("x = B", new JSTerm("y")); assertFalse(p1.equals(p2)); }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm form = new JSPredicateForm("not (x = A) or y = B", new JSTerm("x"), new JSTerm("A"), new JSTerm("B"), new JSTerm("y")); JSPredicateForm standardizedForm = form.standarizerPredicateForm(); assertEquals("(not (= x A)) or (= y B)", standardizedForm.toStr().toString()); }
}