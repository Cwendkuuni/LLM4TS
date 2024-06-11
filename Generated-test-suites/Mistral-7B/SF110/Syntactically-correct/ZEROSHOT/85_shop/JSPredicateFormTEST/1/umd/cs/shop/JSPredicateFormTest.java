package umd.cs.shop.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 private JSPredicateForm predicateForm;
 private JSPredicateForm predicateForm2;
 private JSTerm term;
 private JSTerm term2;
 private JSSubstitution substitution;

 @Before
 public void setUp() {
 predicateForm = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
 predicateForm2 = new JSPredicateForm("(not (or (= y 3) (= y 4)))");
 term = new JSTerm("(= x 1)");
 term2 = new JSTerm("(= y 3)");
 substitution = new JSSubstitution();
 }

 @Test
 public void testJSPredicateFormInit() {
 JSPredicateForm expected = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
 assertEquals(expected, predicateForm);
 }

 @Test
 public void testJSPredicateFormInitWithTokenizer() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not (or (= x 1) (= x 2)))"));
 JSPredicateForm expected = new JSPredicateForm(tokenizer);
 assertEquals(expected, predicateForm);
 }

 @Test
 public void testPrint() {
 predicateForm.print();
 String expected = "((not (or (= x 1) (= x 2))))";
 String actual = JSUtil.readLine();
 assertEquals(expected, actual);
 }

 @Test
 public void testToStr() {
 String expected = "((not (or (= x 1) (= x 2))))";
 String actual = predicateForm.toStr().toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testClonePF() {
 JSPredicateForm expected = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
 JSPredicateForm actual = predicateForm.clonePF();
 assertEquals(expected, actual);
 }

 @Test
 public void testApplySubstitutionPF() {
 term.substitute(substitution, "x", "z");
 term2.substitute(substitution, "y", "z");
 JSPredicateForm expected = new JSPredicateForm("(not (or (= z 1) (= z 2)))");
 JSPredicateForm actual = predicateForm.applySubstitutionPF(substitution);
 assertEquals(expected, actual);
 }

 @Test
 public void testMatches() {
 JSPredicateForm expected = new JSPredicateForm("(not (or (= z 1) (= z 2)))");
 JSSubstitution actual = predicateForm.matches(predicateForm2);
 assertEquals(expected, actual.getDomain());
 }

 @Test
 public void testEquals() {
 JSPredicateForm expected = new JSPredicateForm("(not (or (= y 3) (= y 4)))");
 assertTrue(predicateForm.equals(predicateForm2));
 assertFalse(predicateForm.equals(new JSPredicateForm("(not (or (= x 1) (= x 2)))")));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm expected = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
 JSPredicateForm actual = predicateForm.standarizerPredicateForm();
 assertEquals(expected, actual);
 }
}