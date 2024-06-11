package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

public class JSPredicateFormTest {

 JSTerm createTerm(StreamTokenizer tokenizer) {
 return new JSTerm(tokenizer);
 }

 @Test
 public void testEmptyForm() {
 JSPredicateForm form = new JSPredicateForm();
 JSPredicateForm expectedForm = new JSPredicateForm();
 assertEquals(expectedForm, form);
 }

 @Test(expected = JSParserError.class)
 public void testNullText() {
 new JSPredicateForm(null);
 }

 @Test
 public void testSimpleForm() {
 String text = " ( a ) ";
 JSPredicateForm form = new JSPredicateForm(text);
 JSPredicateForm expectedForm = new JSPredicateForm();
 expectedForm.addElement("a");
 assertEquals(expectedForm, form);
 }

 @Test
 public void testNotForm() {
 String text = " ( not ( a ) ) ";
 JSPredicateForm form = new JSPredicateForm(text);
 JSPredicateForm expectedForm = new JSPredicateForm();
 expectedForm.addElement("not");
 expectedForm.addElement(new JSPredicateForm());
 expectedForm.addElement(expectedForm.elementAt(0));
 assertEquals(expectedForm, form);
 }

 @Test
 public void testCallForm() {
 String text = " ( call f ( a b c ) ) ";
 JSPredicateForm form = new JSPredicateForm(text);
 JSPredicateForm expectedForm = new JSPredicateForm();
 expectedForm.addElement("call");
 expectedForm.addElement("f");
 JSTerm a = createTerm(new StreamTokenizer(new StringReader("a")));
 JSTerm b = createTerm(new StreamTokenizer(new StringReader("b")));
 JSTerm c = createTerm(new StreamTokenizer(new StringReader("c")));
 expectedForm.addElement(a);
 expectedForm.addElement(b);
 expectedForm.addElement(c);
 assertEquals(expectedForm, form);
 }

 @Test
 public void testComplexForm() {
 String text = " ( not ( ( a ) ( b ( not ( c ) ) ) ) ) ";
 JSPredicateForm form = new JSPredicateForm(text);
 JSPredicateForm expectedForm = new JSPredicateForm();
 expectedForm.addElement("not");
 expectedForm.addElement(new JSPredicateForm());
 expectedForm.addElement("not");
 expectedForm.addElement(new JSPredicateForm());
 expectedForm.addElement("a");
 expectedForm.addElement(new JSPredicateForm());
 expectedForm.addElement("b");
 JSPredicateForm bNot = new JSPredicateForm();
 bNot.addElement("not");
 bNot.addElement("c");
 expectedForm.addElement(bNot);
 assertEquals(expectedForm, form);
 }

 @Test
 public void testEquals() {
 JSPredicateForm form1 = new JSPredicateForm(" ( a ) ");
 JSPredicateForm form2 = new JSPredicateForm(" ( a ) ");
 JSPredicateForm form3 = new JSPredicateForm(" ( b ) ");
 JSPredicateForm form4 = new JSPredicateForm(" ( not ( a ) ) ");
 assertEquals(form1, form1);
 assertEquals(form1, form2);
 assertNotEquals(form1, form3);
 assertNotEquals(form1, form4);
 }

 @Test
 public void testClonePF() {
 JSPredicateForm form = new JSPredicateForm(" ( a b c ) ");
 JSPredicateForm clonedForm = form.clonePF();
 assertEquals(form, clonedForm);
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm form = new JSPredicateForm(" ( a x ) ");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addSubstitution("x", "b");
 JSPredicateForm expectedForm = new JSPredicateForm();
 expectedForm.addElement("a");
 expectedForm.addElement("b");
 JSPredicateForm resultForm = form.applySubstitutionPF(substitution);
 assertEquals(expectedForm, resultForm);
 }

 @Test
 public void testMatches() {
 JSPredicateForm form = new JSPredicateForm(" ( a x ) ");
 JSPredicateForm otherForm = new JSPredicateForm(" ( a b ) ");
 JSSubstitution substitution = new JSSubstitution();
 JSSubstitution result = form.matches(otherForm, substitution);
 assertNull(result);
 }

 @Test
 public void testMatchesWithSubstitution() {
 JSPredicateForm form = new JSPredicateForm(" ( a x ) ");
 JSPredicateForm otherForm = new JSPredicateForm(" ( a b ) ");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addSubstitution("x", "b");
 JSSubstitution result = form.matches(otherForm, substitution);
 assertEquals(substitution, result);
 }

 @Test
 public void testPrint() {
 JSPredicateForm form = new JSPredicateForm(" ( a ( b not c ) ) ");
 String expectedString = "([a]([b]([not]([c]))))";
 form.print();
 String resultString = JSUtil.printToMemory();
 assertEquals(expectedString, resultString);
 }

 @Test
 public void testToString() {
 JSPredicateForm form = new JSPredicateForm(" ( a ( b not c ) ) ");
 String expectedString = "([a]([b]([not]([c]))))";
 String resultString = form.toStr().toString();
 assertEquals(expectedString, resultString);
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm form = new JSPredicateForm(" ( not ( a ( b not c ) ) ) ");
 JSPredicateForm expectedForm = new JSPredicateForm();
 expectedForm.addElement("not");
 expectedForm.addElement(new JSPredicateForm());
 expectedForm.addElement("not");
 expectedForm.addElement("a");
 expectedForm.addElement(new JSPredicateForm());
 expectedForm.addElement("not");
 expectedForm.addElement("b");
 expectedForm.addElement("not");
 expectedForm.addElement("c");
 JSPredicateForm resultForm = form.standarizerPredicateForm();
 assertEquals(expectedForm, resultForm);
 }
}