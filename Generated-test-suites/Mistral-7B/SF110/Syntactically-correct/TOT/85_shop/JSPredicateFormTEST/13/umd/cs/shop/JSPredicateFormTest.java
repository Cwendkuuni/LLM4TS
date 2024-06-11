package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.Vector;

public class JSPredicateFormTest {

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm pf = new JSPredicateForm();
 assertEquals(0, pf.size());
 }

 @Test
 public void testStringConstructor() {
 String input = "(! p1)";
 JSPredicateForm pf = new JSPredicateForm(input);
 assertEquals(2, pf.size());
 String expectedStr = "(! p1)";
 assertEquals(expectedStr, pf.toStr().toString());
 }

 @Test
 public void testStreamTokenizerConstructor() {
 String input = "(! p1)";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 assertEquals(2, pf.size());
 String expectedStr = "(! p1)";
 assertEquals(expectedStr, pf.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testInvalidConstructor() {
 String input = "(! p1)(";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 fail("An exception should have been thrown.");
 }

 @Test
 public void testPrintMethod() {
 String input = "(! p1) (x > 10)";
 JSPredicateForm pf = new JSPredicateForm(input);
 StringBuilder expectedOutput = new StringBuilder();
 expectedOutput.append("(").append("(! p1) ").append("(x > 10) ")
 .append(")");
 StringBuilder actualOutput = new StringBuilder();
 pf.print();
 assertEquals(expectedOutput.toString(), actualOutput.toString());
 }

 @Test
 public void testClonePFMethod() {
 String input = "(! p1) (x > 10)";
 JSPredicateForm originalPF = new JSPredicateForm(input);
 JSPredicateForm clonedPF = originalPF.clonePF();
 assertEquals(originalPF, clonedPF);
 }

 @Test
 public void testApplySubstitutionPFMethod() {
 String input = "(! p1) (x > 10)";
 JSPredicateForm pf = new JSPredicateForm(input);
 JSSubstitution alpha = new JSSubstitution();
 alpha.assign("p1", "q1");
 JSPredicateForm expectedPF = new JSPredicateForm();
 expectedPF.addElement("(! q1)");
 expectedPF.addElement(new JSTerm("x > 11")); // 10 was replaced with 11 for testing Purposes
 JSPredicateForm actualPF = pf.applySubstitutionPF(alpha);
 assertEquals(expectedPF, actualPF);
 }

 @Test
 public void testMatchesMethod() {
 String input1 = "(! p1) (x > 10)";
 String input2 = "(! q1) (y > 11)";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution result = pf1.matches(pf2, alpha);
 assertTrue(result.isFailure());
 }

 @Test
 public void testMatchesMethodWithSuccess() {
 String input1 = "(! p1) (x > 10)";
 String input2 = "(! p1) (x > 10)";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution result = pf1.matches(pf2, alpha);
 assertEquals(pf1, pf2);
 }

 @Test
 public void testEqualsMethod() {
 String input1 = "(! p1) (x > 10)";
 String input2 = "(! p1) (x > 10)";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 assertTrue(pf1.equals(pf2));
 }

 @Test
 public void testEqualsMethodWithDifferentSize() {
 String input1 = "(! p1) (x > 10)";
 String input2 = "(! p1) (x > 10) (y > 11)";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 assertFalse(pf1.equals(pf2));
 }

 @Test
 public void testEqualsMethodWithDifferentFunctor() {
 String input1 = "(! p1) (x > 10)";
 String input2 = "! (x > 10)";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 assertFalse(pf1.equals(pf2));
 }

 @Test
 public void testEqualsMethodWithDifferentTerms() {
 String input1 = "(! p1) (x > 10)";
 String input2 = "(! p1) (x < 10)";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 assertFalse(pf1.equals(pf2));
 }

 @Test
 public void testStandarizerPredicateFormMethod() {
 String input = "(! p1) (x > 10)";
 JSPredicateForm pf = new JSPredicateForm(input);
 JSPredicateForm expectedPF = new JSPredicateForm();
 expectedPF.addElement("!not");
 expectedPF.addElement("p1");
 expectedPF.addElement("x");
 expectedPF.addElement(">");
 expectedPF.addElement("10");
 JSPredicateForm actualPF = pf.standarizerPredicateForm();
 assertEquals(expectedPF, actualPF);
 }

 @Test
 public void testStandarizerPredicateFormWithComplexPredicate() {
 String input = "(! p1) (x > 10) (y < 5)";
 JSPredicateForm pf = new JSPredicateForm(input);
 JSPredicateForm expectedPF = new JSPredicateForm();
 expectedPF.addElement("!not");
 expectedPF.addElement("p1");
 expectedPF.addElement("x");
 expectedPF.addElement(">");
 expectedPF.addElement("10");
 expectedPF.addElement("and");
 expectedPF.addElement(new JSTerm("y"));
 expectedPF.addElement("<");
 expectedPF.addElement("5");
 JSPredicateForm actualPF = pf.standarizerPredicateForm();
 assertEquals(expectedPF, actualPF);
 }
}