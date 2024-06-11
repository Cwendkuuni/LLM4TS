package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 private void basicTest(JSPredicateForm predicateForm) {
 if (predicateForm.size() == 0) {
 fail("PredicateForm is empty");
 }
 String expectedStr = predicateForm.toString();
 String actualStr = predicateForm.toStr().toString();
 assertEquals(expectedStr, actualStr);
 }

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm predicateForm = new JSPredicateForm();
 basicTest(predicateForm);
 }

 @Test
 public void testStringConstructor() {
 JSPredicateForm predicateForm = new JSPredicateForm("testPredicate");
 basicTest(predicateForm);
 }

 @Test
 public void testStreamTokenizerConstructor() {
 JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("testPredicate")));
 basicTest(predicateForm);
 }

 @Test
 public void testJSPredicateFormInit_Empty() {
 JSPredicateForm tokenizerPredicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("")));
 basicTest(tokenizerPredicateForm);
 }

 @Test
 public void testJSPredicateFormInit_OneElement() {
 JSPredicateForm tokenizerPredicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(test)")));
 basicTest(tokenizerPredicateForm);
 }

 @Test
 public void testJSPredicateFormInit_TwoElements() {
 JSPredicateForm tokenizerPredicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(not (test))")));
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("not");
 expected.addElement(new JSPredicateForm());
 JSTerm testTerm = new JSTerm(new StreamTokenizer(new StringReader("test")));
 expected.lastElement().addElement(testTerm);
 basicTest(tokenizerPredicateForm);
 assertEquals(expected, tokenizerPredicateForm);
 }

 @Test
 public void testJSPredicateFormInit_Call() {
 JSPredicateForm tokenizerPredicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(call test)")));
 JSTerm testTerm = new JSTerm(new StreamTokenizer(new StringReader("test")));
 tokenizerPredicateForm.addElement("call");
 tokenizerPredicateForm.addElement(testTerm);
 basicTest(tokenizerPredicateForm);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInit_UnexpectedCharacter() {
 JSPredicateForm tokenizerPredicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(test) %")));
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInit_UnexpectedWord() {
 JSPredicateForm tokenizerPredicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(test) call")));
 }

 @Test
 public void testPrint() {
 JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(not (test))")));
 predicateForm.print();
 }

 @Test
 public void testToStr() {
 JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(not (test))")));
 String expected = "((not (test)))";
 assertEquals(expected, predicateForm.toStr().toString());
 }

 @Test
 public void testClonePF() {
 JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(not (test))")));JSPredicateForm cloned = predicateForm.clonePF();
 JSTerm testTerm = new JSTerm(new StreamTokenizer(new StringReader("test")));
 assertEquals(predicateForm, cloned);
 assertEquals(predicateForm.elementAt(1), cloned.elementAt(1));
 assertNotEquals(testTerm, cloned.elementAt(1));
 testTerm = cloned.elementAt(1).cloneT();
 assertEquals(testTerm, cloned.elementAt(1));
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm predicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader("(test x)")));
 JSSubstitution alpha = new JSSubstitution();
 alpha.add(new JSSubstitution.Binding("x", new JSTerm("y")));
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("test");
 expected.addElement(new JSTerm("y"));
 JSPredicateForm result = predicateForm.applySubstitutionPF(alpha);
 basicTest(result);
 assertEquals(expected, result);
 }

 @Test
 public void testMatches() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("x")));
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("y")));
 JSPredicateForm predicateForm1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(x)")));
 JSPredicateForm predicateForm2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(y)")));
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = predicateForm1.matches(predicateForm2, alpha);
 assertTrue(beta.isEmpty());
 }

 @Test(expected = JSParserError.class)
 public void testMatches_UnevenSize() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("x")));
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("y")));
 JSPredicateForm predicateForm1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(x)")));
 JSPredicateForm predicateForm2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(x y)")));
 JSSubstitution alpha = new JSSubstitution();
 predicateForm1.matches(predicateForm2, alpha);
 }

 @Test(expected = JSParserError.class)
 public void testMatches_UnequalFunctor() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("x")));
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("y")));
 JSPredicateForm predicateForm1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(x)")));
 JSPredicateForm predicateForm2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(y)")));
 JSSubstitution alpha = new JSSubstitution();
 predicateForm1.matches(predicateForm2, alpha);
 }

 @Test
 public void testMatches_EqualFunctorAndTerms() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("x")));
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("x")));
 JSPredicateForm predicateForm1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(x)")));
 JSPredicateForm predicateForm2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(x)")));
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = predicateForm1.matches(predicateForm2, alpha);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testMatches_Substitution() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("x")));
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("y")));
 JSTerm term3 = new JSTerm(new StreamTokenizer(new StringReader("z")));
 JSPredicateForm predicateForm1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(x)")));
 JSPredicateForm predicateForm2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(y z)")));
 JSSubstitution alpha = new JSSubstitution();
 alpha.add(new JSSubstitution.Binding("x", term3));
 JSSubstitution beta = predicateForm1.matches(predicateForm2, alpha);
 assertEquals(beta, alpha);
 }

 @Test
 public void testEquals() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("x")));
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("y")));
 JSPredicateForm predicateForm1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(x)")));
 JSPredicateForm predicateForm2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(y)")));
 JSPredicateForm predicateForm3 = new JSPredicateForm(new StreamTokenizer(new StringReader("(x)")));
 assertTrue(predicateForm1.equals(predicateForm3));
 assertFalse(predicateForm1.equals(predicateForm2));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("x")));
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("y")));
 JSPredicateForm predicateForm1 = new JSPredicateForm(new StreamTokenizer(new StringReader("(x y)")));
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("and");
 expected.addElement(term1.standardizerTerm());
 expected.addElement(term2.standardizerTerm());
 JSPredicateForm result = predicateForm1.standarizerPredicateForm();
 assertEquals(expected, result);
 }
}