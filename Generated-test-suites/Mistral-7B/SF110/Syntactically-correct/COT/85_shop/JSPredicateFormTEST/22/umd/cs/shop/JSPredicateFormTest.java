package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 @Test
 public void testJSPredicateFormConstructorWithNullText() {
 JSPredicateForm predicateForm = new JSPredicateForm();
 assertNull(predicateForm);
 }

 @Test
 public void testJSPredicateFormConstructorWithText() {
 String text = "not (p(x) and q(x))";
 JSPredicateForm predicateForm = new JSPredicateForm(text);
 assertEquals("not", predicateForm.elementAt(0).toString());
 JSPredicateForm notPredicateForm = predicateForm.elementAt(1);
 assertEquals("p", notPredicateForm.elementAt(0).toString());
 JSTerm andTerm = notPredicateForm.elementAt(1);
 assertEquals("and", andTerm.toString());
 JSTerm pTerm = andTerm.elementAt(1);
 assertEquals("p", pTerm.toString());
 JSTerm xTerm = pTerm.elementAt(1);
 assertEquals("x", xTerm.toString());
 }

 @Test
 public void testJSPredicateFormConstructorWithStreamTokenizer() {
 String text = "not (p(x) and q(x))";
 JSPredicateForm tokenizerPredicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader(text)));
 assertEquals("not", tokenizerPredicateForm.elementAt(0).toString());
 JSPredicateForm notPredicateForm = tokenizerPredicateForm.elementAt(1);
 assertEquals("p", notPredicateForm.elementAt(0).toString());
 JSTerm andTerm = notPredicateForm.elementAt(1);
 assertEquals("and", andTerm.toString());
 JSTerm pTerm = andTerm.elementAt(1);
 assertEquals("p", pTerm.toString());
 JSTerm xTerm = pTerm.elementAt(1);
 assertEquals("x", xTerm.toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithNullTokenizer() {
 JSPredicateForm predicateForm = new JSPredicateForm();
 predicateForm.JSPredicateFormInit(null);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitUnexpectedLeftParenthesis() {
 String text = " not (p(x) and q(x))";
 JSPredicateForm predicateForm = new JSPredicateForm();
 predicateForm.JSPredicateFormInit(new StreamTokenizer(new StringReader(text)));
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitMissingPredicate() {
 String text = " (p(x))";
 JSPredicateForm predicateForm = new JSPredicateForm();
 predicateForm.JSPredicateFormInit(new StreamTokenizer(new StringReader(text)));
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitUnexpectedRightParenthesis() {
 String text = " not (p(x) )";
 JSPredicateForm predicateForm = new JSPredicateForm();
 predicateForm.JSPredicateFormInit(new StreamTokenizer(new StringReader(text)));
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitUnknownOperator() {
 String text = " not (p(x) %%% q(x))";
 JSPredicateForm predicateForm = new JSPredicateForm();
 predicateForm.JSPredicateFormInit(new StreamTokenizer(new StringReader(text)));
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitUnexpectedNotOperator() {
 String text = " not not (p(x) and q(x))";
 JSPredicateForm predicateForm = new JSPredicateForm();
 predicateForm.JSPredicateFormInit(new StreamTokenizer(new StringReader(text)));
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitMissingRightParenthesis() {
 String text = " not (p(x) and q(x)";
 JSPredicateForm predicateForm = new JSPredicateForm();
 predicateForm.JSPredicateFormInit(new StreamTokenizer(new StringReader(text)));
 }

 @Test
 public void testPrint() {
 JSPredicateForm predicateForm = new JSPredicateForm("not (p(x) and q(x))");
 predicateForm.print();
 String expectedOutput = "((not (p x)) and (q x)) ";
 assertEquals(expectedOutput, System.getProperty("line.separator"));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testPrintEmptyPredicateForm() {
 JSPredicateForm predicateForm = new JSPredicateForm();
 predicateForm.print();
 }

 @Test
 public void testToStr() {
 JSPredicateForm predicateForm = new JSPredicateForm("not (p(x) and q(x))");
 String expectedOutput = "((not p) and (q x))";
 assertEquals(expectedOutput, predicateForm.toStr().toString());
 }

 @Test
 public void testClonePF() {
 JSPredicateForm predicateForm = new JSPredicateForm("not (p(x) and q(x))");
 JSPredicateForm clonedPredicateForm = predicateForm.clonePF();
 assertEquals(predicateForm, clonedPredicateForm);
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm predicateForm = new JSPredicateForm("not (p(x) and q(y))");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addBinding("x", new JSTerm("z"));
 JSPredicateForm result = predicateForm.applySubstitutionPF(substitution);
 assertEquals("not (p(z) and q(y))", result.toStr().toString());
 }

 @Test
 public void testMatches() {
 JSPredicateForm predicateForm1 = new JSPredicateForm("not (p(x) and q(y))");
 JSPredicateForm predicateForm2 = new JSPredicateForm("p(z) or r(x)");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addBinding("x", new JSTerm("z"));
 JSPredicateForm result = predicateForm1.matches(predicateForm2, substitution);
 assertNull(result);
 }

 @Test
 public void testMatchesWithMatchingPredicates() {
 JSPredicateForm predicateForm1 = new JSPredicateForm("not (p(x) and q(y))");
 JSPredicateForm predicateForm2 = new JSPredicateForm("not (p(z) and q(z))");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addBinding("x", new JSTerm("z"));
 JSPredicateForm result = predicateForm1.matches(predicateForm2, substitution);
 assertNotNull(result);
 }

 @Test
 public void testEquals() {
 JSPredicateForm predicateForm1 = new JSPredicateForm("not (p(x) and q(y))");
 JSPredicateForm predicateForm2 = new JSPredicateForm("not (p(z) and q(z))");
 assertFalse(predicateForm1.equals(predicateForm2));
 JSPredicateForm predicateForm3 = new JSPredicateForm("not (p(x) and q(x))");
 assertTrue(predicateForm1.equals(predicateForm3));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm predicateForm = new JSPredicateForm("not (p(x) and q(y))");
 JSPredicateForm standarizerPredicateForm = predicateForm.standarizerPredicateForm();
 assertEquals("not ((p x) and (q y))", standarizerPredicateForm.toStr().toString());
 }
}