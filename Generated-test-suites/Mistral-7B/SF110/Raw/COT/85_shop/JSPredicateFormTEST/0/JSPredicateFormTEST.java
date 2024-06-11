package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 private static final String TEST_CASE_1 = "(! X = Y)";
 private static final String TEST_CASE_2 = "((not A) and B)";
 private static final String TEST_CASE_3 = "((not A) or B)";
 private static final String TEST_CASE_4 = "((not A) implies B)";
 private static final String TEST_CASE_5 = "(((A and B) or C) and D)";

 @Test
 public void testJSPredicateFormInitWithText() {
 JSPredicateForm pf = new JSPredicateForm(TEST_CASE_1);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithTextInvalidFormat() {
 JSPredicateForm pf = new JSPredicateForm("Invalid format");
 }

 @Test
 public void testJSPredicateFormInitWithTokenizer() {
 StringReader sr = new StringReader(TEST_CASE_1);
 StreamTokenizer st = new StreamTokenizer(sr);
 JSPredicateForm pf = new JSPredicateForm(st);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithTokenizerInvalidFormat() {
 StringReader sr = new StringReader("Invalid format");
 StreamTokenizer st = new StreamTokenizer(sr);
 JSPredicateForm pf = new JSPredicateForm(st);
 }

 @Test
 public void testPrint() {
 JSPredicateForm pf = new JSPredicateForm(TEST_CASE_1);
 pf.print();
 }

 @Test
 public void testToString() {
 JSPredicateForm pf = new JSPredicateForm(TEST_CASE_1);
 String expected = "((not X) = (not Y))";
 String result = pf.toStr().toString();
 assertEquals(expected, result);
 }

 @Test
 public void testClonePF() {
 JSPredicateForm original = new JSPredicateForm(TEST_CASE_2);
 JSPredicateForm cloned = original.clonePF();
 assertEquals(original, cloned);
 }

 @Test
 public void testApplySubstitutionPF() {
 JSTerm termA = new JSTerm("A");
 JSTerm termB = new JSTerm("B");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addSubstitution("A", termB);
 JSPredicateForm predicateForm = new JSPredicateForm(TEST_CASE_5);
 JSPredicateForm expected = new JSPredicateForm("(((B and B) or C) and D)");
 JSPredicateForm result = predicateForm.applySubstitutionPF(substitution);
 assertEquals(expected, result);
 }

 @Test
 public void testMatches() {
 JSPredicateForm pf1 = new JSPredicateForm(TEST_CASE_1);
 JSPredicateForm pf2 = new JSPredicateForm(TEST_CASE_1);
 JSSubstitution result = pf1.matches(pf2);
 assertTrue(result.isSuccess());
 }

 @Test
 public void testMatchesWithDifferentPredicateForms() {
 JSPredicateForm pf1 = new JSPredicateForm(TEST_CASE_1);
 JSPredicateForm pf2 = new JSPredicateForm(TEST_CASE_2);
 JSSubstitution result = pf1.matches(pf2);
 assertTrue(result.isFailure());
 }

 @Test
 public void testEquals() {
 JSPredicateForm pf1 = new JSPredicateForm(TEST_CASE_1);
 JSPredicateForm pf2 = new JSPredicateForm(TEST_CASE_1);
 assertTrue(pf1.equals(pf2));
 }

 @Test
 public void testEqualsWithDifferentPredicateForms() {
 JSPredicateForm pf1 = new JSPredicateForm(TEST_CASE_1);
 JSPredicateForm pf2 = new JSPredicateForm(TEST_CASE_2);
 assertFalse(pf1.equals(pf2));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm pf = new JSPredicateForm(TEST_CASE_3);
 JSPredicateForm expected = new JSPredicateForm("((not A) or B)");
 JSPredicateForm result = pf.standarizerPredicateForm();
 assertEquals(expected, result);
 }
}