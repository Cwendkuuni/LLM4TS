import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

 @Test
 public void testEmptyFormConstruction() {
 JSPredicateForm form = new JSPredicateForm();
 assertEquals(0, form.size());
 }

 @Test
 public void testFormConstructionWithText() {
 JSPredicateForm form = new JSPredicateForm("(not (or (and a b) (and c d)))");
 assertEquals(4, form.size());
 assertEquals("not", form.get(0).toString());
 assertEquals("or", form.get(1).toString());
 assertEquals("and", form.get(2).toString());
 assertEquals("a", form.get(3).toString());
 assertEquals("b", form.get(4).toString());
 assertEquals("c", form.get(5).toString());
 assertEquals("d", form.get(6).toString());
 }

 @Test
 public void testFormConstructionWithTokenizer() {
 String text = "(not (or (and a b) (and c d)))";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm form = new JSPredicateForm(tokenizer);
 assertEquals(4, form.size());
 assertEquals("not", form.get(0).toString());
 assertEquals("or", form.get(1).toString());
 assertEquals("and", form.get(2).toString());
 assertEquals("a", form.get(3).toString());
 assertEquals("b", form.get(4).toString());
 assertEquals("c", form.get(5).toString());
 assertEquals("d", form.get(6).toString());
 }

 @Test(expected = JSParserError.class)
 public void testInvalidFormConstruction() {
 String text = "(not (or (and a b) (and c )";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm form = new JSPredicateForm(tokenizer);
 fail("Expected JSParserError but test passed");
 }

 @Test
 public void testPrint() {
 JSPredicateForm form = new JSPredicateForm("(not (or (and a b) (and c d)))");
 form.print();
 String expected = "(not (or (and a b) (and c d)))";
 String output = JSUtil.getOutput();
 assertEquals(expected, output);
 }

 @Test
 public void testToString() {
 JSPredicateForm form = new JSPredicateForm("(not (or (and a b) (and c d)))");
 String expected = "(not (or (and a b) (and c d)))";
 String output = form.toStr().toString();
 assertEquals(expected, output);
 }

 @Test
 public void testClonePF() {
 JSPredicateForm form = new JSPredicateForm("(not (or (and a b) (and c d)))");
 JSPredicateForm clone = form.clonePF();
 assertEquals(form, clone);
 }

 @Test
 public void testApplySubstitutionPF() {
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("a", new Term("X"));
 JSPredicateForm form = new JSPredicateForm("(or (and a b) (and c d))");
 JSPredicateForm result = form.applySubstitutionPF(alpha);
 assertEquals("(or (and X b) (and c d))", result.toStr().toString());
 }

 @Test
 public void testMatches() {
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("a", new Term("X"));
 JSPredicateForm form1 = new JSPredicateForm("(and a b)");
 JSPredicateForm form2 = new JSPredicateForm("(and X b)");
 JSSubstitution beta = form1.matches(form2, new JSSubstitution());
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testEquals() {
 JSPredicateForm form1 = new JSPredicateForm("(and a b)");
 JSPredicateForm form2 = new JSPredicateForm("(and a b)");
 JSPredicateForm form3 = new JSPredicateForm("(and c d)");
 assertTrue(form1.equals(form2));
 assertFalse(form1.equals(form3));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm form = new JSPredicateForm("(not (or (and a b) (and c d)))");
 JSPredicateForm standardized = form.standarizerPredicateForm();
 assertEquals("(not (and (or a b) (or c d)))", standardized.toStr().toString());
 }
}