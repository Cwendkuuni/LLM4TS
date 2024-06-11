package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 private JSPredicateForm createForm(String text) {
 return new JSPredicateForm(text);
 }

 @Test
 public void testDefaultConstructor() {
 JSPredicateForm form = new JSPredicateForm();
 assertTrue(form.isEmpty());
 }

 @Test
 public void testConstructorWithText() {
 JSPredicateForm form = createForm("(not P)");
 assertEquals("(not P)", form.toString());
 }

 @Test
 public void testConstructorWithTokenizer() {
 JSPredicateForm form = createForm("(not P)");
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not P)"));
 form = new JSPredicateForm(tokenizer);
 assertEquals("(not P)", form.toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithNullTokenizer() {
 JSPredicateForm form = new JSPredicateForm();
 form.JSPredicateFormInit(null);
 fail("Should throw JSParserError");
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithInvalidTokenType() {
 StringReader reader = new StringReader("(not P)");
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 tokenizer.nextToken();
 JSPredicateForm form = new JSPredicateForm();
 form.JSPredicateFormInit(tokenizer);
 fail("Should throw JSParserError");
 }

 @Test
 public void testJSPredicateFormPrint() {
 JSPredicateForm form = createForm("(not (or (= x 1) (= x 2)))");
 form.print();
 assertEquals("(not (or (= x 1) (= x 2))) ", form.toString());
 }

 @Test
 public void testJSPredicateFormToString() {
 JSPredicateForm form = createForm("(not (or (= x 1) (= x 2)))");
 assertEquals("(not (or (= x 1) (= x 2)))", form.toStr().toString());
 }

 @Test
 public void testJSPredicateFormClonePF() {
 JSPredicateForm form = createForm("(not (or (= x 1) (= x 2)))");
 JSPredicateForm cloned = form.clonePF();
 assertNotSame(form, cloned);
 assertEquals(form.toString(), cloned.toString());
 }

 @Test
 public void testJSPredicateFormApplySubstitutionPF() {
 JSPredicateForm form = createForm("(not (or (= x 1) (= x 2)))");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("1", new JSTerm("2"));
 JSPredicateForm result = form.applySubstitutionPF(alpha);
 assertEquals("(not (or (= x 2) (= x 2)))", result.toString());
 }

 @Test
 public void testJSPredicateFormMatches() {
 JSPredicateForm form1 = createForm("(not (or (= x 1) (= x 2)))");
 JSPredicateForm form2 = createForm("(not (or (= x 2) (= x 1)))");
 JSSubstitution result = form1.matches(form2);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testJSPredicateFormMatchesWithSubstitution() {
 JSPredicateForm form1 = createForm("(not (or (= x 1) (= x 2)))");
 JSPredicateForm form2 = createForm("(not (or (= y 1) (= y 2)))");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("x", new JSTerm("y"));
 JSSubstitution result = form1.matches(form2, alpha);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testJSPredicateFormEquals() {
 JSPredicateForm form1 = createForm("(not (or (= x 1) (= x 2)))");
 JSPredicateForm form2 = createForm("(not (or (= x 2) (= x 1)))");
 assertFalse(form1.equals(form2));
}

 @Test
 public void testJSPredicateFormEqualsWithSameForm() {
 JSPredicateForm form = createForm("(not (or (= x 1) (= x 2)))");
 assertTrue(form.equals(form));
 }

 @Test
 public void testJSPredicateFormEqualsWithSameContent() {
 JSPredicateForm form1 = createForm("(not (or (= x 1) (= x 2)))");
 JSPredicateForm form2 = new JSPredicateForm();
 form2.addElement("not");
 form2.addElement(new JSTerm("or"));
 form2.addElement(new JSTerm("="));
 form2.addElement("x");
 form2.addElement(1);
 form2.addElement(new JSTerm("="));
 form2.addElement("x");
 form2.addElement(2);
 assertTrue(form1.equals(form2));
 }

 @Test
 public void testJSPredicateFormStandarizerPredicateForm() {
 JSPredicateForm form = createForm("(not (or (= x 1) (= x 2)))");
 JSPredicateForm result = form.standarizerPredicateForm();
 assertEquals("(not (or (= x 1) (= x 2)))", result.toString());
 }
}