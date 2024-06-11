package umd.cs.shop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 private JSPredicateForm predicateForm;
 private JSTerm term1;
 private JSTerm term2;
 private JSSubstitution substitution;

 @Before
 public void setUp() {
 this.predicateForm = new JSPredicateForm();
 this.term1 = new JSTerm();
 this.term2 = new JSTerm();
 this.substitution = new JSSubstitution();
 }

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm emptyForm = new JSPredicateForm();
 assertTrue(emptyForm.size() == 0);
 }

 @Test
 public void testConstructorWithText() {
 String text = "not ( p(x) )";
 JSPredicateForm form = new JSPredicateForm(text);
 assertEquals("not", form.elementAt(0));
 assertEquals(1, form.size());
 }

 @Test
 public void testConstructorWithTokenizer() {
 String text = "not ( p(x) )";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm form = new JSPredicateForm(tokenizer);
 assertEquals("not", form.elementAt(0));
 assertEquals(1, form.size());
 }

 @Test(expected = JSParserError.class)
 public void testInvalidConstructorWithNullText() {
 String text = null;
 JSPredicateForm form = new JSPredicateForm(text);
 fail("Should throw JSParserError exception");
 }

 @Test(expected = JSParserError.class)
 public void testInvalidConstructorWithMalformedText() {
 String text = "not ( p(x) ) ) ";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm form = new JSPredicateForm(tokenizer);
 fail("Should throw JSParserError exception");
 }

 @Test
 public void testJSPredicateFormInit() {
 term1.setTermType("p");
 term1.setTermVar("x");
 term2 = new JSTerm();
 term2.setTermType("e");
 term2.setTermVar("y");
 tokenizer.reset();
 JSUtil.init(tokenizer);
 JSUtil.pushBack(tokenizer, '(');
 JSUtil.writeString(tokenizer, "not");
 JSUtil.writeString(tokenizer, "(");
 JSUtil.writeString(tokenizer, "p(x)");
 JSUtil.writeString(tokenizer, ")");
 JSUtil.writeString(tokenizer, ")");
 JSUtil.expectTokenType(JSJshopVars.rightPar, tokenizer, "");

 JSPredicateForm form = new JSPredicateForm(tokenizer);
 assertEquals("not", form.elementAt(0));
 assertEquals(2, form.size());
 assertEquals("not", form.elementAt(0));
 assertEquals(term1, form.elementAt(1));
 }

 @Test
 public void testPrint() {
 String expected = "not (p(x))";
 this.predicateForm.addElement("not");
 this.predicateForm.addElement(this.term1);
 this.predicateForm.print();
 assertEquals(expected, JSUtil.getOutput());
 }

 @Test
 public void testToStr() {
 String expected = "not (p(x))";
 this.predicateForm.addElement("not");
 this.predicateForm.addElement(this.term1);
 assertEquals(expected, this.predicateForm.toStr().toString());
 }

 @Test
 public void testClonePF() {
 JSPredicateForm clonedForm = this.predicateForm.clonePF();
 assertEquals(this.predicateForm.size(), clonedForm.size());
 for (int i = 0; i < this.predicateForm.size(); i++) {
 if (this.predicateForm.elementAt(i) instanceof String) {
 assertEquals(this.predicateForm.elementAt(i), clonedForm.elementAt(i));
 } else {
 assertEquals(this.predicateForm.elementAt(i).hashCode(), clonedForm.elementAt(i).hashCode());
 }
 }
 }

 @Test
 public void testApplySubstitutionPF() {
 String variable = "x";
 String replacement = "y";
 term1.setTermVar(variable);
 term2.setTermVar(variable);
 term1.setTermType("p");
 term2.setTermType("e");
 this.predicateForm.addElement("not");
 this.predicateForm.addElement(this.term1);
 this.predicateForm.addElement(this.predicateForm);
 substitution.bind(variable, replacement);
 JSPredicateForm expectedResult = new JSPredicateForm();
 expectedResult.addElement("not");
 expectedResult.addElement(new JSTerm(replacement));
 expectedResult.addElement(this.term2);
 JSPredicateForm result = this.predicateForm.applySubstitutionPF(substitution);
 assertEquals(expectedResult.size(), result.size());
 for (int i = 0; i < expectedResult.size(); i++) {
 if (i == 0) {
 assertEquals("not", result.elementAt(i));
 } else if (i == 1) {
 assertEquals(replacement, result.elementAt(i).getTermVar());
 } else {
 assertEquals(expectedResult.elementAt(i).hashCode(), result.elementAt(i).hashCode());
 }
 }
 }

 @Test
 public void testMatches() {
 String variable = "x";
 String replacement = "y";
 term1.setTermVar(variable);
 term2.setTermVar(variable);
 term1.setTermType("p");
 term2.setTermType("e");
 this.predicateForm.addElement("not");
 this.predicateForm.addElement(this.term1);
 this.predicateForm.addElement(this.predicateForm);
 substitution.bind(variable, replacement);
 JSPredicateForm otherPredicateForm = new JSPredicateForm();
 otherPredicateForm.addElement("p");
 otherPredicateForm.addElement(this.term2);
 JSSubstitution result = this.predicateForm.matches(otherPredicateForm, substitution);
 assertTrue(result.isEmpty());

 term1.setTermType("e");
 otherPredicateForm = new JSPredicateForm();
 otherPredicateForm.addElement("p");
 otherPredicateForm.addElement(this.term1);
 otherPredicateForm.addElement(this.term2);
 result = this.predicateForm.matches(otherPredicateForm, substitution);
 assertFalse(result.isEmpty());
 assertEquals(2, result.size());
 assertEquals(replacement, result.getElementAt(0).getTermVar());
 assertEquals(this.term2.hashCode(), result.getElementAt(1).hashCode());
 }

 @Test
 public void testEquals() {
 JSPredicateForm otherPredicateForm = new JSPredicateForm();
 assertFalse(this.predicateForm.equals(otherPredicateForm));

 otherPredicateForm = new JSPredicateForm();
 otherPredicateForm.addElement("not");
 otherPredicateForm.addElement(this.term1);
 otherPredicateForm.addElement(this.predicateForm);
 assertTrue(this.predicateForm.equals(otherPredicateForm));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm standardizedForm = this.predicateForm.standarizerPredicateForm();
 assertEquals(this.predicateForm.size(), standardizedForm.size());
 for (int i = 0; i < this.predicateForm.size(); i++) {
 if (this.predicateForm.elementAt(i) instanceof String) {
 assertEquals(this.predicateForm.elementAt(i), standardizedForm.elementAt(i));
 } else {
 JSTerm term = (JSTerm) this.predicateForm.elementAt(i);
 JSTerm standardizedTerm = (JSTerm) standardizedForm.elementAt(i);
 assertEquals(term.getTermType(), standardizedTerm.getTermType());
 assertEquals(term.getTermVar(), standardizedTerm.getTermVar());
 }
 }
 }

 @Test
 public void testEmptyToString() {
 JSPredicateForm emptyForm = new JSPredicateForm();
 assertEquals("", emptyForm.toStr().toString());
 }
}