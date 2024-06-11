package umd.cs.shop.test;

import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

public class JSPredicateFormTest {

 @Test
 public void testJSPredicateFormEmptyConstructor() {
 JSPredicateForm jsf = new JSPredicateForm();
 assertEquals(0, jsf.size());
 }

 @Test
 public void testJSPredicateFormStringConstructor() {
 String input = "(! p)";
 JSPredicateForm jsf = new JSPredicateForm(input);
 assertEquals(2, jsf.size());
 assertEquals("!", jsf.get(0).toString());
 assertEquals("p", jsf.get(1).toString());
 }

 @Test
 public void testJSPredicateFormStreamTokenizerConstructor() {
 String input = "(! p)";
 StringReader reader = new StringReader(input);
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm jsf = new JSPredicateForm(tokenizer);
 assertEquals(2, jsf.size());
 assertEquals("!", jsf.get(0).toString());
 assertEquals("p", jsf.get(1).toString());
 }

 @Test(expected = JSParserError.class)
 public void testInvalidInputJSPredicateFormConstructor() {
 String input = "(! p) %%%";
 StringReader reader = new StringReader(input);
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm jsf = new JSPredicateForm(tokenizer);
 fail("Expected JSParserError");
 }

 @Test
 public void testPrintMethod() {
 String input = "(! not (= x 5))";
 JSPredicateForm jsf = new JSPredicateForm(input);
 String expectedOutput = "((! not) (= x 5))";
 jsf.print();
 assertEquals(expectedOutput, System.getProperty("line.separator") + System.out.getBuffer().toString());
 }

 @Test
 public void testToStringMethod() {
 String input = "(! not (= x 5))";
 JSPredicateForm jsf = new JSPredicateForm(input);
 String expectedOutput = "((! not) (= x 5))";
 assertEquals(expectedOutput, jsf.toStr().toString());
 }

 @Test
 public void testClonePFMethod() {
 String input = "(! not (= x 5))";
 JSPredicateForm jsf = new JSPredicateForm(input);
 JSPredicateForm clonedPF = jsf.clonePF();
 assertEquals(2, clonedPF.size());
 assertEquals("!", clonedPF.get(0).toString());
 assertEquals("not", clonedPF.get(1).toString());
 assertEquals(2, clonedPF.get(1).size());
 assertEquals("not", clonedPF.get(1).get(0).toString());
 assertEquals("=", clonedPF.get(1).get(1).toString());
 assertEquals("x", clonedPF.get(1).get(2).toString());
 assertEquals(5, clonedPF.get(1).get(3).intValue());
 }

 @Test
 public void testApplySubstitutionPFMethod() {
 String input = "(! not (= x 5))";
 JSPredicateForm jsf = new JSPredicateForm(input);
 JSSubstitution alpha = new JSSubstitution();
 alpha.bind("x", new JSTerm("y"));
 JSPredicateForm expectedResult = new JSPredicateForm("(! not (= y 5))");
 JSPredicateForm result = jsf.applySubstitutionPF(alpha);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testMatchesMethod() {
 String input1 = "(! not (= x 5))";
 String input2 = "(! (= y 5))";
 JSPredicateForm jsf1 = new JSPredicateForm(input1);
 JSPredicateForm jsf2 = new JSPredicateForm(input2);
 JSSubstitution expectedSubstitution = new JSSubstitution();
 expectedSubstitution.bind("x", new JSTerm("y"));
 JSSubstitution actualSubstitution = jsf1.matches(jsf2);
 assertEquals(expectedSubstitution, actualSubstitution);
 }

 @Test
 public void testEqualsMethod() {
 String input1 = "(! not (= x 5))";
 String input2 = "(! not (= x 5))";
 JSPredicateForm jsf1 = new JSPredicateForm(input1);
 JSPredicateForm jsf2 = new JSPredicateForm(input2);
 assertTrue(jsf1.equals(jsf2));
 }

 @Test
 public void testEqualsDifferentSizeMethod() {
 String input1 = "(! not (= x 5))";
 String input2 = "(! not (= x 5) (= y 10))";
 JSPredicateForm jsf1 = new JSPredicateForm(input1);
 JSPredicateForm jsf2 = new JSPredicateForm(input2);
 assertFalse(jsf1.equals(jsf2));
 }

 @Test
 public void testEqualsDifferentFunctorMethod() {
 String input1 = "(! not (= x 5))";
 String input2 = "(! or (= x 5) (= x 10))";
 JSPredicateForm jsf1 = new JSPredicateForm(input1);
 JSPredicateForm jsf2 = new JSPredicateForm(input2);
 assertFalse(jsf1.equals(jsf2));
 }

 @Test
 public void testEqualsDifferentElementsMethod() {
 String input1 = "(! not (= x 5))";
 String input2 = "(! not (= x 5) (= y 10))";
 JSPredicateForm jsf1 = new JSPredicateForm(input1);
 JSPredicateForm jsf2 = new JSPredicateForm(input2);
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSTerm term3 = new JSTerm("5");
 JSTerm term4 = new JSTerm("10");
 Vector<JSTerm> vector1 = new Vector<JSTerm>();
 vector1.add(term1);
 vector1.add(term3);
 JSPredicateForm expectedJSPredicateForm = new JSPredicateForm(vector1);
 assertTrue(jsf1.equals(expectedJSPredicateForm));
 assertFalse(jsf1.equals(jsf2));
 }

 @Test
 public void testStandarizerPredicateFormMethod() {
 String input = "(! not (= x 5) (= x 10))";
 JSPredicateForm jsf = new JSPredicateForm(input);
 String expectedOutput = "((not) ((not) (= x 5)) ((= x 10)))\n";
 JSPredicateForm result = jsf.standarizerPredicateForm();
 assertEquals(expectedOutput, result.toStr().toString());
 }

}