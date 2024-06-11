package umd.cs.shop.test;

import static org.junit.Assert.*;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSPredicateForm;
import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

public class JSPredicateFormTest {

 private StreamTokenizer createTokenizer(String text) {
 return new StreamTokenizer(new StringReader(text));
 }

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm emptyForm = new JSPredicateForm();
 assertEquals(0, emptyForm.size());
 }

 @Test
 public void testStringConstructor() {
 String text = "not (p(x) => q(x))";
 JSPredicateForm form = new JSPredicateForm(text);
 assertEquals(2, form.size());
 assertEquals("not", form.elementAt(0).toString());
 JSPredicateForm notForm = (JSPredicateForm) form.elementAt(1);
 assertEquals(2, notForm.size());
 assertEquals("p", notForm.elementAt(0).toString());
 JSTerm pTerm = (JSTerm) notForm.elementAt(1);
 assertEquals("x", pTerm.toString());
 JSTerm qTerm = (JSTerm) notForm.elementAt(2);
 assertEquals("x", qTerm.toString());
 }

 @Test
 public void testStreamTokenizerConstructor() {
 String text = "not (p(x) => q(x))";
 StreamTokenizer tokenizer = createTokenizer(text);
 JSPredicateForm form = new JSPredicateForm(tokenizer);
 assertEquals(2, form.size());
 // ... other assertions as for String constructor
 }

 @Test(expected = JSParserError.class)
 public void testInvalidInput() {
 String text = "not (p(x) <- q(x))";
 StreamTokenizer tokenizer = createTokenizer(text);
 JSPredicateForm form = new JSPredicateForm(tokenizer);
 fail(); // should throw JSParserError
 }

 @Test
 public void testPrintMethod() {
 String text = "not (p(x) => q(x))";
 JSPredicateForm form = new JSPredicateForm(text);
 String expected = "((not) (p x) (q x))";
 assertEquals(expected, form.toStr().toString());
 }

 @Test
 public void testClonePFMethod() {
 String text = "not (p(x) => q(x))";
 JSPredicateForm originalForm = new JSPredicateForm(text);
 JSPredicateForm clonedForm = originalForm.clonePF();
 // size and elements should be the same
 assertEquals(originalForm.size(), clonedForm.size());
 for (int i = 0; i < originalForm.size(); i++) {
 assertEquals(originalForm.elementAt(i), clonedForm.elementAt(i));
 }
 }

 @Test
 public void testApplySubstitutionPFMethod() {
 String text1 = "p(x)";
 String text2 = "q(y)";
 JSPredicateForm form1 = new JSPredicateForm(text1);
 JSTerm term1 = new JSTerm("x");
 JSSubstitution alpha = new JSSubstitution();
 alpha.assign("x", term1);
 JSPredicateForm expectedForm = new JSPredicateForm("q(y)");
 JSPredicateForm resultForm = form1.applySubstitutionPF(alpha);
 // size and elements should be the same
 assertEquals(expectedForm.size(), resultForm.size());
 for (int i = 0; i < expectedForm.size(); i++) {
 assertEquals(expectedForm.elementAt(i), resultForm.elementAt(i));
 }
 }

 @Test
 public void testMatchesMethod() {
 String text1 = "p(x)";
 String text2 = "q(x)";
 JSPredicateForm form1 = new JSPredicateForm(text1);
 JSPredicateForm form2 = new JSPredicateForm(text2);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = form1.matches(form2, alpha);
 // no bindings should be added
 assertEquals(0, alpha.size());
 // elements should be the same
 JSTerm term1 = (JSTerm) form1.elementAt(0);
 JSTerm term2 = (JSTerm) form2.elementAt(0);
 assertEquals(term1.toString(), term2.toString());
 }

 @Test
 public void testMatchesMethodWithBindings() {
 String text1 = "p(x)";
 String text2 = "q(y)";
 JSPredicateForm form1 = new JSPredicateForm(text1);
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSSubstitution alpha = new JSSubstitution();
 alpha.assign("x", term1);
 alpha.assign("y", term2);
 JSSubstitution beta = form1.matches(new JSPredicateForm(text2), alpha);
 // one binding should be added
 assertEquals(1, beta.size());
 assertEquals("y", beta.variableNames().get(0));
 assertEquals(term2, beta.getValue(beta.variableNames().get(0)));
 }

 @Test
 public void testEqualsMethod() {
 String text1 = "p(x)";
 String text2 = "p(x)";
 JSPredicateForm form1 = new JSPredicateForm(text1);
 JSPredicateForm form2 = new JSPredicateForm(text2);
 assertTrue(form1.equals(form2));
 }

 @Test
 public void testNotEqualsMethod() {
 String text1 = "p(x)";
 String text2 = "q(x)";
 JSPredicateForm form1 = new JSPredicateForm(text1);
 JSPredicateForm form2 = new JSPredicateForm(text2);
 assertFalse(form1.equals(form2));
 }

 @Test
 public void testStandarizerPredicateFormMethod() {
 String text = "not (p(x) => q(x))";
 JSPredicateForm form = new JSPredicateForm(text);
 String expectedText = "((not) (not) (p x) (q x))";
 JSPredicateForm expectedForm = new JSPredicateForm(expectedText);
 JSPredicateForm resultForm = form.standarizerPredicateForm();
 // size and elements should be the same
 assertEquals(expectedForm.size(), resultForm.size());
 for (int i = 0; i < expectedForm.size(); i++) {
 assertEquals(expectedForm.elementAt(i), resultForm.elementAt(i));
 }
 }

}