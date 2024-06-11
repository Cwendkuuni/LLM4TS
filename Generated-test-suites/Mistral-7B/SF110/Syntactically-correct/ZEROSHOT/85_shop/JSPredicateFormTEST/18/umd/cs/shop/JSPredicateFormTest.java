package umd.cs.shop.test;

import static org.junit.Assert.*;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSPredicateForm;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

public class JSPredicateFormTest {

 @Test
 public void testJSPredicateFormEmptyConstruction() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertEquals("Expected empty JSPredicateForm", 0, jspf.size());
 }

 @Test
 public void testJSPredicateFormStringConstruction() {
 String input = "not (A(x) v B(x))";
 JSPredicateForm jspf = new JSPredicateForm(input);
 assertEquals("Expected correct JSPredicateForm size", 3, jspf.size());
 }

 @Test
 public void testJSPredicateFormStreamTokenizerConstruction() {
 String input = "not (A(x) v B(x))";
 StringReader reader = new StringReader(input);
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 assertEquals("Expected correct JSPredicateForm size", 3, jspf.size());
 }

 @Test
 public void testJSPredicateFormInit() {
 String input = "not (A(x) v B(x))";
 StringReader reader = new StringReader(input);
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.JSPredicateFormInit(tokenizer);
 assertEquals("Expected correct JSPredicateForm size", 3, jspf.size());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitInvalidInput() {
 String input = "not (A(x) v B(x) %%%)";
 StringReader reader = new StringReader(input);
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.JSPredicateFormInit(tokenizer);
 fail("Expected JSParserError");
 }

 @Test
 public void testPrint() {
 String input = "not (A(x) v B(x))";
 JSPredicateForm jspf = new JSPredicateForm(input);
 String expectedOutput = "([not] [(A(x))] [v] [(B(x))]) ";
 jspf.print();
 assertEquals("Expected correct print output", expectedOutput, System.out.getOutputStream().toString());
 }

 @Test
 public void testToString() {
 String input = "not (A(x) v B(x))";
 JSPredicateForm jspf = new JSPredicateForm(input);
 String expectedOutput = "([not] [(A(x))] [v] [(B(x))])";
 String actualOutput = jspf.toStr().toString();
 assertEquals("Expected correct toString output", expectedOutput, actualOutput);
 }

 @Test
 public void testClonePF() {
 String input = "not (A(x) v B(x))";
 JSPredicateForm original = new JSPredicateForm(input);
 JSPredicateForm cloned = original.clonePF();
 assertEquals("Expected correct cloned JSPredicateForm size", original.size(), cloned.size());
 }

 @Test
 public void testApplySubstitutionPF() {
 String input = "not (A(x) v B(x))";
 JSPredicateForm original = new JSPredicateForm(input);
 JSSubstitution alpha = new JSSubstitution();
 JSPredicateForm result = original.applySubstitutionPF(alpha);
 JSPredicateForm expected = new JSPredicateForm(new StringReader("(%%% (A(y)) v B(x))"));
 assertEquals("Expected correct applySubstitutionPF output", expected, result);
 }

 @Test
 public void testMatches() {
 String input1 = "not (A(x) v B(x))";
 String input2 = "not (B(x) v A(x))";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 JSSubstitution result = pf1.matches(pf2);
 assertTrue("Expected matches to return true", result.isEmpty());
 }

 @Test
 public void testMatchesWithSubstitution() {
 String input1 = "not (A(x) v B(x))";
 String input2 = "not (B(x) v A(x))";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution result = pf1.matches(pf2, alpha);
 assertTrue("Expected matches to return an empty substitution", result.isEmpty());
 }

 @Test
 public void testMatchesWithDifferentSize() {
 String input1 = "not (A(x) v B(x))";
 String input2 = "not (A(x) v B(x) v C(x))";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 JSSubstitution result = pf1.matches(pf2);
 assertFalse("Expected matches to return false", result.isEmpty());
 }

 @Test
 public void testMatchesWithDifferentFunctor() {
 String input1 = "not (A(x) v B(x))";
 String input2 = "not (C(x) v D(x))";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 JSSubstitution result = pf1.matches(pf2);
 assertFalse("Expected matches to return false", result.isEmpty());
 }

 @Test
 public void testMatchesWithDifferentTerms() {
 String input1 = "not (A(x) v B(x))";
 String input2 = "not (A(y) v C(x))";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 JSSubstitution result = pf1.matches(pf2);
 assertFalse("Expected matches to return false", result.isEmpty());
 }

 @Test
 public void testEquals() {
 String input1 = "not (A(x) v B(x))";
 String input2 = "not (B(x) v A(x))";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 assertFalse("Expected equals to return false", pf1.equals(pf2));
 }

 @Test
 public void testEqualsWithSameObjects() {
 String input = "not (A(x) v B(x))";
 JSPredicateForm pf1 = new JSPredicateForm(input);
 JSPredicateForm pf2 = pf1;
 assertTrue("Expected equals to return true", pf1.equals(pf2));
 }

 @Test
 public void testEqualsWithDifferentSize() {
 String input1 = "not (A(x) v B(x))";
 String input2 = "not (A(x) v B(x) v C(x))";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 assertFalse("Expected equals to return false", pf1.equals(pf2));
 }

 @Test
 public void testEqualsWithDifferentFunctor() {
 String input1 = "not (A(x) v B(x))";
 String input2 = "not (C(x) v D(x))";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 assertFalse("Expected equals to return false", pf1.equals(pf2));
 }

 @Test
 public void testEqualsWithDifferentTerms() {
 String input1 = "not (A(x) v B(x))";
 String input2 = "not (A(y) v C(x))";
 JSPredicateForm pf1 = new JSPredicateForm(input1);
 JSPredicateForm pf2 = new JSPredicateForm(input2);
 assertFalse("Expected equals to return false", pf1.equals(pf2));
 }

 @Test
 public void testStandarizerPredicateForm() {
 String input = "not (A(x) v (and B(x) C(x)))";
 JSPredicateForm jspf = new JSPredicateForm(input);
 JSPredicateForm expected = new JSPredicateForm(new StringReader("(not [(and [A(x)] [B(x)] [C(x)])])"));
 JSPredicateForm result = jspf.standarizerPredicateForm();
 assertEquals("Expected correct standarizerPredicateForm output", expected, result);
 }
}