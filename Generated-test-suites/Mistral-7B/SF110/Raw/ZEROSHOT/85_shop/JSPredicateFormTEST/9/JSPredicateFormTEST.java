package umd.cs.shop.test;

import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

public class JSPredicateFormTest {

 private String getTestData(String testName) {
 return "(" + testName + " (x (y z)))";
 }

 @Test
 public void testJSPredicateFormInit() {
 String testData = getTestData("Test1");
 JSPredicateForm jsp = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("Test1");
 expected.addElement(new JSTerm("x"));
 expected.addElement(new JSTerm("y"));
 expected.addElement(new JSTerm("z"));
 assertEquals(expected, jsp);
 }

 @Test
 public void testJSPredicateFormInitWithText() {
 String testData = getTestData("Test1");
 JSPredicateForm jsp = new JSPredicateForm(testData);
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("Test1");
 expected.addElement(new JSTerm("x"));
 expected.addElement(new JSTerm("y"));
 expected.addElement(new JSTerm("z"));
 assertEquals(expected, jsp);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitInvalidInput() {
 String testData = "(( x )";
 JSPredicateForm jsp = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 fail("Expected JSParserError but got no exception");
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitEmptyInput() {
 String testData = "()";
 JSPredicateForm jsp = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 fail("Expected JSParserError but got no exception");
 }

 @Test
 public void testJSPredicateFormPrint() {
 String testData = getTestData("Test1");
 JSPredicateForm jsp = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 jsp.print();
 String expected = "(Test1 (x (y z)))";
 assertEquals(expected, System.out.getStatusCode());
 }

 @Test
 public void testJSPredicateFormToString() {
 String testData = getTestData("Test1");
 JSPredicateForm jsp = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 String expected = "(Test1 (x (y z)))";
 assertEquals(expected, jsp.toStr().toString());
 }

 @Test
 public void testJSPredicateFormClonePF() {
 String testData = getTestData("Test1");
 JSPredicateForm jsp = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 JSPredicateForm cloned = jsp.clonePF();
 assertEquals(jsp, cloned);
 }

 @Test
 public void testJSPredicateFormApplySubstitutionPF() {
 String testData = getTestData("Test1");
 String substitutionData = "((x a) (y b) (z c))";
 JSPredicateForm jsp = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 JSSubstitution alpha = new JSSubstitution();
 alpha.addSubstitution("x", new JSTerm("a"));
 alpha.addSubstitution("y", new JSTerm("b"));
 alpha.addSubstitution("z", new JSTerm("c"));
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("Test1");
 expected.addElement(new JSTerm("a"));
 expected.addElement(new JSTerm("b"));
 expected.addElement(new JSTerm("c"));
 JSPredicateForm result = jsp.applySubstitutionPF(alpha);
 assertEquals(expected, result);
 }

 @Test
 public void testJSPredicateFormMatches() {
 String testData1 = getTestData("Test1");
 String testData2 = getTestData("Test2");
 JSPredicateForm jsp1 = new JSPredicateForm(new StreamTokenizer(new StringReader(testData1)));
 JSPredicateForm jsp2 = new JSPredicateForm(new StreamTokenizer(new StringReader(testData2)));
 JSSubstitution expected = new JSSubstitution();
 JSSubstitution result = jsp1.matches(jsp2);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testJSPredicateFormMatchesWithSubstitution() {
 String testData1 = getTestData("Test1");
 String testData2 = "((x a) (y b) (z c))";
 JSPredicateForm jsp1 = new JSPredicateForm(new StreamTokenizer(new StringReader(testData1)));
 JSSubstitution alpha = new JSSubstitution();
 alpha.addSubstitution("x", new JSTerm("a"));
 alpha.addSubstitution("y", new JSTerm("b"));
 alpha.addSubstitution("z", new JSTerm("c"));JSPredicateForm jsp2 = new JSPredicateForm(new StreamTokenizer(new StringReader(testData2)));
 JSSubstitution expected = new JSSubstitution();
 expected.addSubstitution("a", new JSTerm("x"));
 expected.addSubstitution("b", new JSTerm("y"));
 expected.addSubstitution("c", new JSTerm("z"));
 JSSubstitution result = jsp1.matches(jsp2, alpha);
 assertEquals(expected, result);
 }

 @Test
 public void testJSPredicateFormEquals() {
 String testData1 = getTestData("Test1");
 String testData2 = getTestData("Test1");
 JSPredicateForm jsp1 = new JSPredicateForm(new StreamTokenizer(new StringReader(testData1)));
 JSPredicateForm jsp2 = new JSPredicateForm(new StreamTokenizer(new StringReader(testData2)));
 assertTrue(jsp1.equals(jsp2));
 }

 @Test
 public void testJSPredicateFormNotEquals() {
 String testData1 = getTestData("Test1");
 String testData2 = getTestData("Test2");
 JSPredicateForm jsp1 = new JSPredicateForm(new StreamTokenizer(new StringReader(testData1)));
 JSPredicateForm jsp2 = new JSPredicateForm(new StreamTokenizer(new StringReader(testData2)));
 assertFalse(jsp1.equals(jsp2));
 }

 @Test
 public void testJSPredicateFormStandarizerPredicateForm() {
 String testData = getTestData("Test1");
 JSPredicateForm jsp = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("Test1");
 expected.addElement(new JSTerm("x"));
 expected.addElement(new JSTerm("y"));
 expected.addElement(new JSTerm("z"));
 JSPredicateForm result = jsp.standarizerPredicateForm();
 assertEquals(expected, result);
 }
}