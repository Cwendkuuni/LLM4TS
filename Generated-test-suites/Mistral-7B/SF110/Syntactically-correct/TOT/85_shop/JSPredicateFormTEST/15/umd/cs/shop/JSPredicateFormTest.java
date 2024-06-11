import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

 @Test
 public void testEmptyForm() {
 JSPredicateForm form = new JSPredicateForm();
 assertEquals(0, form.size());
 }

 @Test
 public void testSimpleForm() {
 JSPredicateForm form = new JSPredicateForm("(or (= x 5) (= x 10))");
 assertEquals(2, form.size());
 assertEquals("or", form.elementAt(0));
 JSTerm term1 = form.elementAt(1);
 assertEquals("=", term1.elementAt(0));
 assertEquals("x", term1.elementAt(1));
 assertEquals(5, term1.elementAt(2).intValue());
 JSTerm term2 = form.elementAt(2);
 assertEquals("=", term2.elementAt(0));
 assertEquals("x", term2.elementAt(1));
 assertEquals(10, term2.elementAt(2).intValue());
 }

 @Test
 public void testNotForm() {
 JSPredicateForm form = new JSPredicateForm("(not (= x 5))");
 assertEquals(2, form.size());
 assertEquals("not", form.elementAt(0));
 JSPredicateForm nestedForm = (JSPredicateForm) form.elementAt(1);
 assertEquals(1, nestedForm.size());
 assertEquals("=", nestedForm.elementAt(0).elementAt(0));
 assertEquals("x", nestedForm.elementAt(0).elementAt(1));
 assertEquals(5, ((JSTerm) nestedForm.elementAt(0).elementAt(2)).intValue());
 }

 @Test
 public void testNestedForms() {
 JSPredicateForm form = new JSPredicateForm("(not (or (= x 5) (= x 10)))");
 assertEquals(3, form.size());
 assertEquals("not", form.elementAt(0));
 JSPredicateForm orForm = (JSPredicateForm) form.elementAt(1);
 assertEquals(2, orForm.size());
 assertEquals("or", orForm.elementAt(0));
 JSTerm xIsFive = orForm.elementAt(1);
 assertEquals("=", xIsFive.elementAt(0));
 assertEquals("x", xIsFive.elementAt(1));
 assertEquals(5, ((JSTerm) xIsFive.elementAt(2)).intValue());
 JSTerm xIsTen = form.elementAt(2);
 assertEquals("=", xIsTen.elementAt(0));
 assertEquals("x", xIsTen.elementAt(1));
 assertEquals(10, ((JSTerm) xIsTen.elementAt(2)).intValue());
 }

 @Test(expected = JSParserError.class)
 public void testMalformedInput() {
 new JSPredicateForm("(not (= x )");
 }

 @Test(expected = JSParserError.class)
 public void testMissingRightParenthesis() {
 new JSPredicateForm("(or (= x 5)");
 }

 @Test(expected = JSParserError.class)
 public void testInvalidOperator() {
 new JSPredicateForm("(and (= x 5) (+ x 2))");
 }

 @Test
 public void testToString() {
 JSPredicateForm form = new JSPredicateForm("(or (= x 5) (= x 10))");
 assertEquals("(or (= x 5) (= x 10))", form.toStr().toString());
 }

 @Test
 public void testClone() {
 JSPredicateForm form = new JSPredicateForm("(or (= x 5) (= x 10))");
 JSPredicateForm cloned = form.clonePF();
 assertEquals(form.toStr(), cloned.toStr());
 }

 @Test
 public void testMatches() {
 JSPredicateForm form1 = new JSPredicateForm("(= x 5)");
 JSPredicateForm form2 = new JSPredicateForm("(= x 5)");
 JSSubstitution substitution = form1.matches(form2);
 assertTrue(substitution.isEmpty());
 form1 = new JSPredicateForm("(= x 5)");
 form2 = new JSPredicateForm("(= x 10)");
 substitution = form1.matches(form2);
 assertFalse(substitution.isEmpty());
 }

 @Test
 public void testEquals() {
 JSPredicateForm form1 = new JSPredicateForm("(= x 5)");
 JSPredicateForm form2 = new JSPredicateForm("(= x 5)");
 assertTrue(form1.equals(form2));
 form1 = new JSPredicateForm("(= x 5)");
 form2 = new JSPredicateForm("(= x 10)");
 assertFalse(form1.equals(form2));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm form = new JSPredicateForm("(not (or (= x 5) (= x 10)))");
 JSPredicateForm standardized = form.standarizerPredicateForm();
 assertEquals("(not (or (not (= x 5)) (not (= x 10))))", standardized.toStr().toString());
 }
}