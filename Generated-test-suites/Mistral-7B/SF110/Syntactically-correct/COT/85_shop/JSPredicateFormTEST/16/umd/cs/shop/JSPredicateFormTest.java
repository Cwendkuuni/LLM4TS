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
 public void testEmptyForm() {
 JSPredicateForm form = new JSPredicateForm();
 assertTrue(form.size() == 0);
 }

 @Test
 public void testFormInitWithText() {
 String text = "not (A (not B) C)";
 JSPredicateForm form = createForm(text);
 assertEquals(3, form.size());
 String[] expected = new String[]{"not", "A", "not", "B", "C"};
 for (int i = 0; i < form.size(); i++) {
 assertEquals(expected[i], form.elementAt(i).toString());
 }
 }

 @Test
 public void testFormInitWithTokenizer() {
 String text = "not (A (not B) C)";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm form = new JSPredicateForm(tokenizer);
 assertEquals(3, form.size());
 String[] expected = new String[]{"not", "A", "not", "B", "C"};
 for (int i = 0; i < form.size(); i++) {
 assertEquals(expected[i], form.elementAt(i).toString());
 }
 }

 @Test(expected = JSParserError.class)
 public void testFormInitWithNullText() {
 createForm(null);
 }

 @Test(expected = JSParserError.class)
 public void testFormInitWithInvalidTextFormat() {
 String text = "not (A (not B C) D)";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 createForm(tokenizer);
 }

 @Test
 public void testPrintMethod() {
 String text = "not (A (not B) C)";
 JSPredicateForm form = createForm(text);
 form.print();
 String expected = "((not (A (not B) C))) ";
 assertEquals(expected, JSUtil.getTextFromOutput());
 }

 @Test
 public void testToStringMethod() {
 String text = "not (A (not B) C)";
 JSPredicateForm form = createForm(text);
 String expected = "((not (A (not B) C))) ";
 assertEquals(expected, form.toStr().toString());
 }

 @Test
 public void testClonePFMethod() {
 String text = "not (A (not B) C)";
 JSPredicateForm original = createForm(text);
 JSPredicateForm clone = original.clonePF();
 assertEquals(original.size(), clone.size());
 for (int i = 0; i < original.size(); i++) {
 if (original.elementAt(i) instanceof String) {
 assertEquals(original.elementAt(i), clone.elementAt(i));
 } else {
 JSTerm originalTerm = (JSTerm) original.elementAt(i);
 JSTerm cloneTerm = (JSTerm) clone.elementAt(i);
 assertEquals(originalTerm.toString(), cloneTerm.toString());
 }
 }
 }

 @Test
 public void testApplySubstitutionPFMethod() {
 String text = "not (A (not B) C)";
 JSPredicateForm form = createForm(text);
 JSSubstitution substitution = new JSSubstitution();
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("not");
 expected.addElement("A");
 expected.addElement("X");
 expected.addElement("not");
 expected.addElement("B");
 expected.addElement("X");
 expected.addElement("C");
 JSPredicateForm actual = form.applySubstitutionPF(substitution);
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.elementAt(i), actual.elementAt(i).toString());
 }
 }

 @Test
 public void testMatchesMethodWithNullArgument() {
 JSPredicateForm form = new JSPredicateForm();
 assertNull(form.matches(null));
 }

 @Test
 public void testMatchesMethodWithSameArgument() {
 String text = "not (A (not B) C)";
 JSPredicateForm form = createForm(text);
 JSSubstitution substitution = new JSSubstitution();
 JSPredicateForm expected = form;
 JSPredicateForm actual = form.matches(form, substitution);
 assertEquals(expected, actual);
 }

 @Test
 public void testMatchesMethodWithDifferentPredicateForm() {
 String text1 = "not (A (not B) C)";
 String text2 = "not (A (B D) E)";
 JSPredicateForm form1 = createForm(text1);
 JSPredicateForm form2 = createForm(text2);
 JSSubstitution substitution = new JSSubstitution();
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("not");
 expected.addElement("A");
 expected.addElement("not");
 expected.addElement("B");
 expected.addElement("D");
 expected.addElement("not");
 expected.addElement("C");
 JSPredicateForm actual = form1.matches(form2, substitution);
 assertNotEquals(expected, actual);
 assertNull(actual);
 }

 @Test(expected = JSParserError.class)
 public void testMatchesMethodWithInvalidArgument() {
 String text = "not (A (not B) C)";
 JSPredicateForm form = createForm(text);
 JSSubstitution substitution = new JSSubstitution();
 form.matches(null, substitution);
 }

 @Test
 public void testMatchesMethodWithEmptySubstitution() {
 String text = "not (A (not B) C)";
 JSPredicateForm form = createForm(text);
 JSSubstitution substitution = new JSSubstitution();
 JSPredicateForm expected = form;
 JSPredicateForm actual = form.matches(form, substitution);
 assertEquals(expected, actual);
 }

 @Test
 public void testEqualsMethodWithNullArgument() {
 JSPredicateForm form = new JSPredicateForm();
 assertFalse(form.equals(null));
 }

 @Test
 public void testEqualsMethodWithDifferentObjects() {
 JSPredicateForm form1 = new JSPredicateForm();
 JSPredicateForm form2 = new JSPredicateForm();
 assertFalse(form1.equals(form2));
 }

 @Test
 public void testEqualsMethodWithSameObjects() {
 String text = "not (A (not B) C)";
 JSPredicateForm form1 = createForm(text);
 String text2 = "not (A (not B) C)";
 JSPredicateForm form2 = createForm(text2);
 assertTrue(form1.equals(form2));
 }

 @Test
 public void testStandarizerPredicateFormMethod() {
 String text = "not (A (not B) C)";
 JSPredicateForm form = createForm(text);
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("not");
 expected.addElement("A");
 expected.addElement("not");
 expected.addElement("B");
 expected.addElement("B");
 expected.addElement("C");
 JSPredicateForm actual = form.standarizerPredicateForm();
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.elementAt(i), actual.elementAt(i).toString());
 }
 }

}