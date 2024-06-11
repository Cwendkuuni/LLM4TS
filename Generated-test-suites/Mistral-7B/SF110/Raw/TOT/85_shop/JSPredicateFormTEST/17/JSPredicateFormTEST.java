import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm predicateForm = new JSPredicateForm();
 assertEquals(0, predicateForm.size());
 }

 @Test
 public void testStringConstructor() {
 JSPredicateForm predicateForm = new JSPredicateForm("(not (or a b) (and c d)));");
 assertEquals(3, predicateForm.size());
 assertEquals("not", predicateForm.elementAt(0).toString());
 assertEquals("or", predicateForm.elementAt(1).toStr().toString());
 assertEquals("a", predicateForm.elementAt(2).toStr().toString());
 assertEquals("b", predicateForm.elementAt(3).toStr().toString());
 assertEquals("and", predicateForm.elementAt(4).toString());
 assertEquals("c", predicateForm.elementAt(5).toStr().toString());
 assertEquals("d", predicateForm.elementAt(6).toStr().toString());
 }

 @Test
 public void testStreamTokenizerConstructor() {
 String input = "(not (or a b) (and c d)));";
 JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader(input)));
 assertEquals(3, predicateForm.size());
 assertEquals("not", predicateForm.elementAt(0).toString());
 assertEquals("or", predicateForm.elementAt(1).toStr().toString());
 assertEquals("a", predicateForm.elementAt(2).toStr().toString());
 assertEquals("b", predicateForm.elementAt(3).toStr().toString());
 assertEquals("and", predicateForm.elementAt(4).toString());
 assertEquals("c", predicateForm.elementAt(5).toStr().toString());
 assertEquals("d", predicateForm.elementAt(6).toStr().toString());
 }

 @Test
 public void testJSPredicateFormInitWithInvalidInput() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("this is not valid input"));
 JSPredicateForm predicateForm = new JSPredicateForm(tokenizer);
 fail("Should have thrown JSParserError");
 }

 @Test
 public void testPrintMethod() {
 JSPredicateForm predicateForm = new JSPredicateForm("(not (or a b) (and c d)));");
 predicateForm.print();
 String expectedOutput = "(not (or a b) (and c d)) ";
 String actualOutput = JSUtil.getTextFromOutput();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testToStrMethod() {
 JSPredicateForm predicateForm = new JSPredicateForm("(not (or a b) (and c d)));");
 String expectedOutput = "(\\(not \\(or a b)\\) \\(and c d\\))";
 String actualOutput = predicateForm.toStr().toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testClonePFMethod() {
 JSPredicateForm predicateForm = new JSPredicateForm("(not (or a b) (and c d)));");
 JSPredicateForm clonedPredicateForm = predicateForm.clonePF();
 assertEquals(predicateForm, clonedPredicateForm);
 }

 @Test
 public void testApplySubstitutionPFMethod() {
 JSPredicateForm predicateForm = new JSPredicateForm("(not (or a b) (and c d)));");
 JSSubstitution alpha = new JSSubstitution();
 alpha.add("a", new Term("x"));
 JSPredicateForm substitutedPredicateForm = predicateForm.applySubstitutionPF(alpha);
 String expectedOutput = "(\\(not \\(or x x)\\) \\(and c d\\))";
 String actualOutput = substitutedPredicateForm.toStr().toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testMatchesMethod() {
 JSPredicateForm predicateForm1 = new JSPredicateForm("(or a b)");
 JSPredicateForm predicateForm2 = new JSPredicateForm("(and a b)");
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = predicateForm1.matches(predicateForm2, alpha);
 fail("Should have returned empty substitution, as the predicates are not equal");
 }

 @Test
 public void testEqualsMethod() {
 JSPredicateForm predicateForm1 = new JSPredicateForm("(or a b)");
 JSPredicateForm predicateForm2 = new JSPredicateForm("(or a b)");
 assertTrue(predicateForm1.equals(predicateForm2));
 }

 @Test
 public void testEqualsMethodWithDifferentPredicates() {
 JSPredicateForm predicateForm1 = new JSPredicateForm("(or a b)");
 JSPredicateForm predicateForm2 = new JSPredicateForm("(and a b)");
 assertFalse(predicateForm1.equals(predicateForm2));
 }

 @Test
 public void testStandarizerPredicateFormMethod() {
 JSPredicateForm predicateForm = new JSPredicateForm("(not (or a b) (and c d)));");
 JSPredicateForm standardizedPredicateForm = predicateForm.standarizerPredicateForm();
 String expectedOutput = "(\\(not \\(or \\(not a) \\(not b)\\) \\(and c d\\))";
 String actualOutput = standardizedPredicateForm.toStr().toString();
 assertEquals(expectedOutput, actualOutput);
 }
}