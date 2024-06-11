package umd.cs.shop.test;

import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

public class JSPredicateFormTest {

 private String testData = "((x > 5) and (y < 3))";

 @Test
 public void testJSPredicateFormInitWithText() {
 JSPredicateForm jspf = new JSPredicateForm(testData);
 Vector<String> expected = new Vector<>();
 expected.add("x > 5");
 expected.add("y < 3");
 assertEquals(expected, jspf);
 }

 @Test
 public void testJSPredicateFormInitWithStreamTokenizer() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(testData));
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 Vector<String> expected = new Vector<>();
 expected.add("x > 5");
 expected.add("y < 3");
 assertEquals(expected, jspf);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithNullTokenizer() {
 new JSPredicateForm(null);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithWrongText() {
 String wrongText = "((x > 5) and 3 < y)";
 new JSPredicateForm(wrongText);
 }

 @Test
 public void testPrintMethod() {
 JSPredicateForm jspf = new JSPredicateForm(testData);
 String expectedPrint = "((x > 5) and (y < 3))";
 String actualPrint = jspf.print();
 assertEquals(expectedPrint, actualPrint);
 }

 @Test
 public void testToStringMethod() {
 JSPredicateForm jspf = new JSPredicateForm(testData);
 String expectedToString = "((x > 5) and (y < 3))";
 String actualToString = jspf.toStr().toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testClonePFMethod() {
 JSPredicateForm jspf = new JSPredicateForm(testData);
 JSPredicateForm clonedPF = jspf.clonePF();
 assertEquals(jspf, clonedPF);
 }

 @Test
 public void testApplySubstitutionPFMethod() {
 JSPredicateForm jspf = new JSPredicateForm(testData);
 JSSubstitution alpha = new JSSubstitution();
 alpha.bind("x", new JSTerm("3"));
 JSPredicateForm expected = new JSPredicateForm("((3 > 5) and (y < 3))");
 JSPredicateForm actual = jspf.applySubstitutionPF(alpha);
 assertEquals(expected, actual);
 }

 @Test
 public void testMatchesMethodWithNullArgument() {
 JSPredicateForm jspf = new JSPredicateForm(testData);
 assertFalse(jspf.matches(null));
 }

 @Test
 public void testMatchesMethodWithDifferentSizePredicates() {
 JSPredicateForm jspf = new JSPredicateForm("((x > 5) and (y < 3))");
 JSPredicateForm other = new JSPredicateForm("((x > 5) and (y < 3)) and z < 3");
 assertFalse(jspf.matches(other));
 }

 @Test
 public void testMatchesMethodWithDifferentFunctors() {
 JSPredicateForm jspf = new JSPredicateForm("((x > 5) and (y < 3))");
 JSPredicateForm other = new JSPredicateForm("((z > 5) and (y < 3))");
 assertFalse(jspf.matches(other));
 }

 @Test
 public void testMatchesMethodWithDifferentTerms() {
 JSPredicateForm jspf = new JSPredicateForm("((x > 5) and (y < 3))");
 JSPredicateForm other = new JSPredicateForm("((x > 5) and (z < 3))");
 assertFalse(jspf.matches(other));
 }

 @Test
 public void testMatchesMethodWithIdenticalPredicates() {
 JSPredicateForm jspf = new JSPredicateForm("((x > 5) and (y < 3))");
 JSPredicateForm other = new JSPredicateForm("((x > 5) and (y < 3))");
 assertTrue(jspf.matches(other));
 }

 @Test
 public void testEqualsMethodWithNullArgument() {
 JSPredicateForm jspf = new JSPredicateForm("((x > 5) and (y < 3))");
 assertFalse(jspf.equals(null));
 }

 @Test
 public void testEqualsMethodWithDifferentSizePredicates() {
 JSPredicateForm jspf = new JSPredicateForm("((x > 5) and (y < 3))");
 JSPredicateForm other = new JSPredicateForm("((x > 5) and (y < 3)) and z < 3");
 assertFalse(jspf.equals(other));
 }

 @Test
 public void testEqualsMethodWithDifferentFunctors() {
 JSPredicateForm jspf = new JSPredicateForm("((x > 5) and (y < 3))");
 JSPredicateForm other = new JSPredicateForm("((z > 5) and (y < 3))");
 assertFalse(jspf.equals(other));
 }

 @Test
 public void testEqualsMethodWithDifferentTerms() {
 JSPredicateForm jspf = new JSPredicateForm("((x > 5) and (y < 3))");
 JSPredicateForm other = new JSPredicateForm("((x > 5) and (z < 3))");
 assertFalse(jspf.equals(other));
 }

 @Test
 public void testEqualsMethodWithIdenticalPredicates() {
 JSPredicateForm jspf = new JSPredicateForm("((x > 5) and (y < 3))");
 JSPredicateForm other = new JSPredicateForm("((x > 5) and (y < 3))");
 assertTrue(jspf.equals(other));
 }

 @Test
 public void testStandarizerPredicateFormMethod() {
 JSPredicateForm jspf = new JSPredicateForm("((x > 5) and (y < 3))");
 JSPredicateForm expected = new JSPredicateForm("(> x 5) and (< y 3)");
 JSPredicateForm actual = jspf.standarizerPredicateForm();
 assertEquals(expected, actual);
 }

}