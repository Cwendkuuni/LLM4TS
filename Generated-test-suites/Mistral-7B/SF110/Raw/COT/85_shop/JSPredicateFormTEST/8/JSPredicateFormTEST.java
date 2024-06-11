package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSTerm;

public class JSPredicateFormTest {

 private JSPredicateForm createJSPredicateForm(String text) {
 return new JSPredicateForm(text);
 }

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm jspredicateForm = new JSPredicateForm();
 assertEquals(0, jspredicateForm.size());
 }

 @Test
 public void testConstructorWithText() {
 String text = "not (p (q r))";
 JSPredicateForm jspredicateForm = createJSPredicateForm(text);
 assertEquals(3, jspredicateForm.size());
 assertEquals("not", jspredicateForm.elementAt(0).toString());
 assertEquals("p", jspredicateForm.elementAt(1).toString());
 assertEquals("(q r)", jspredicateForm.elementAt(2).toString());
 }

 @Test
 public void testConstructorWithTokenizer() {
 String text = "not (p (q r))";
 JSPredicateForm jspredicateForm = new JSPredicateForm(new StreamTokenizer(new StringReader(text)));
 assertEquals(3, jspredicateForm.size());
 assertEquals("not", jspredicateForm.elementAt(0).toString());
 assertEquals("p", jspredicateForm.elementAt(1).toString());
 assertEquals("(q r)", jspredicateForm.elementAt(2).toString());
 }

 @Test(expected = JSParserError.class)
 public void testInvalidConstructorWithNullText() {
 String text = null;
 JSPredicateForm jspredicateForm = createJSPredicateForm(text);
 fail("Should throw JSParserError when text is null.");
 }

 @Test(expected = JSParserError.class)
 public void testInvalidConstructorWithInvalidText() {
 String text = "not (p) r";
 JSPredicateForm jspredicateForm = createJSPredicateForm(text);
 fail("Should throw JSParserError when text is invalid.");
 }

 @Test
 public void testJSPredicateFormInit() {
 String text = "not (p (q r))";
 JSPredicateForm jspredicateForm = new JSPredicateForm();
 jspredicateForm.JSPredicateFormInit(new StreamTokenizer(new StringReader(text)));
 assertEquals(3, jspredicateForm.size());
 assertEquals("not", jspredicateForm.elementAt(0).toString());
 assertEquals("p", jspredicateForm.elementAt(1).toString());
 assertEquals("(q r)", jspredicateForm.elementAt(2).toString());
 }

 @Test(expected = JSParserError.class)
 public void testInvalidJSPredicateFormInitWithNullTokenizer() {
 JSPredicateForm jspredicateForm = new JSPredicateForm();
 try {
 jspredicateForm.JSPredicateFormInit(null);
 } catch (JSParserError e) {
 // expected
 }
 fail("Should throw JSParserError when tokenizer is null.");
 }

 @Test
 public void testPrint() {
 String text = "not (p (q r))";
 JSPredicateForm jspredicateForm = createJSPredicateForm(text);
 jspredicateForm.print();
 assertEquals("(not (p (q r)))", System.out.getHistory(1).split("\\s+")[0]);
 }

 @Test
 public void testToStr() {
 String text = "not (p (q r))";
 JSPredicateForm jspredicateForm = createJSPredicateForm(text);
 assertEquals("(not (p (q r)))", jspredicateForm.toStr().toString());
 }

 @Test
 public void testClonePF() {
 String text = "not (p (q r))";
 JSPredicateForm jspredicateForm1 = createJSPredicateForm(text);
 JSPredicateForm jspredicateForm2 = jspredicateForm1.clonePF();
 assertEquals(jspredicateForm1, jspredicateForm2);
 }

 @Test
 public void testApplySubstitutionPF() {
 String text1 = "p(x)";
 String text2 = "q(y)";
 String text3 = "not (p(x) (p(x)))";
 JSPredicateForm jspredicateForm1 = createJSPredicateForm(text1);
 JSPredicateForm jspredicateForm2 = createJSPredicateForm(text2);
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("x", new JSTerm("a"));
 alpha.addBinding("y", new JSTerm("b"));
 JSPredicateForm result = jspredicateForm1.applySubstitutionPF(alpha);
 JSPredicateForm expected = createJSPredicateForm("not (q(b) (q(b)))");
 assertEquals(result, expected);
 }

 @Test
 public void testMatches() {
 String text1 = "p(x)";
 String text2 = "p(x)";
 String text3 = "not (p(x) (p(x)))";
 JSPredicateForm jspredicateForm1 = createJSPredicateForm(text1);
 JSPredicateForm jspredicateForm2 = createJSPredicateForm(text2);
 JSPredicateForm jspredicateForm3 = createJSPredicateForm(text3);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = new JSSubstitution();
 beta = jspredicateForm1.matches(jspredicateForm2, alpha);
 assertTrue(beta.isEmpty());
 beta = jspredicateForm1.matches(jspredicateForm3, alpha);
 assertFalse(beta.isEmpty());
 assertEquals(2, beta.size());
 assertEquals("x", beta.elementAt(0).toString());
 assertEquals("x", beta.elementAt(1).toString());
 }

 @Test
 public void testEquals() {
 String text1 = "p(x)";
 String text2 = "p(x)";
 String text3 = "not (p(x) (p(x)))";
 JSPredicateForm jspredicateForm1 = createJSPredicateForm(text1);
 JSPredicateForm jspredicateForm2 = createJSPredicateForm(text1);
 JSPredicateForm jspredicateForm3 = createJSPredicateForm(text2);
 JSPredicateForm jspredicateForm4 = createJSPredicateForm(text3);
 assertTrue(jspredicateForm1.equals(jspredicateForm2));
 assertFalse(jspredicateForm1.equals(jspredicateForm3));
 assertFalse(jspredicateForm1.equals(jspredicateForm4));
 }

 @Test
 public void testStandarizerPredicateForm() {
 String text = "not (p (q r))";
 JSPredicateForm jspredicateForm = createJSPredicateForm(text);
 JSPredicateForm result = jspredicateForm.standarizerPredicateForm();
 assertEquals("not (p (q r))", result.toStr().toString());
 }

}