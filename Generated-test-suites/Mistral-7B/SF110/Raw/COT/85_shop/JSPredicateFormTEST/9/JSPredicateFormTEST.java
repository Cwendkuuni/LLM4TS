package umd.cs.shop.test;

import static org.junit.Assert.*;

import org.junit.Test;

import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSParserError;
import umd.cs.shop.StreamTokenizer;
import umd.cs.shop.StringReader;

public class JSPredicateFormTest {

 private static final int JSParserError_EXPECTED_LEFT_PAREN = JSJshopVars.leftPar;
 private static final int JSParserError_READING_PREDICATE = -2;
 private static final int JSParserError_UNEXPECTED_RIGHT_PAREN = JSJshopVars.rightPar;
 private static final int JSParserError_OP_NAME_EXPECTED = -3;

 @Test
 public void testConstructorNull() {
 JSPredicateForm predicateForm = new JSPredicateForm();
 assertNotNull(predicateForm);
 }

 @Test(expected = JSParserError.class)
 public void testConstructorNullText() {
 new JSPredicateForm(null);
 }

 @Test
 public void testConstructorText() {
 JSPredicateForm predicateForm = new JSPredicateForm("(not X)");
 assertNotNull(predicateForm);
 }

 @Test(expected = JSParserError.class)
 public void testConstructorInvalidInput() {
 StringReader reader = new StringReader("(not %%%");
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 new JSPredicateForm(tokenizer);
 }

 @Test
 public void testJSPredicateFormInit() {
 StringReader reader = new StringReader("(not (or X Y))");
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm predicateForm = new JSPredicateForm(tokenizer);
 assertEquals("not", predicateForm.elementAt(0));
 JSPredicateForm notX = (JSPredicateForm) predicateForm.elementAt(1);
 assertEquals("or", notX.elementAt(0));
 JSTerm x = notX.elementAt(1);
 assertEquals("X", x.toString());
 JSTerm y = notX.elementAt(2);
 assertEquals("Y", y.toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitInvalidOperator() {
 StringReader reader = new StringReader("(not (not X))");
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 new JSPredicateForm(tokenizer);
 }

 @Test
 public void testJSPredicateFormInitCall() {
 StringReader reader = new StringReader("(call F X)");
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm predicateForm = new JSPredicateForm(tokenizer);
 assertEquals("call", predicateForm.elementAt(0));
 assertEquals("F", predicateForm.elementAt(1).toString());
 JSTerm x = (JSTerm) predicateForm.elementAt(2);
 assertEquals("X", x.toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitNoRightParenthesis() {
 StringReader reader = new StringReader("(not (or X Y)");
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 new JSPredicateForm(tokenizer);
 }

 @Test
 public void testPrint() {
 JSPredicateForm predicateForm = new JSPredicateForm("(not (or X Y))");
 predicateForm.print();
 assertEquals("(not (or X Y))", JSUtil.readLine());
 }

 @Test
 public void testToString() {
 JSPredicateForm predicateForm = new JSPredicateForm("(not (or X Y))");
 assertEquals("(not (or X Y))", predicateForm.toStr().toString());
 }

 @Test
 public void testClonePF() {
 JSPredicateForm originalPredicateForm = new JSPredicateForm("(not (or X Y))");
 JSPredicateForm clonedPredicateForm = originalPredicateForm.clonePF();
 assertEquals(originalPredicateForm.size(), clonedPredicateForm.size());
 for (int i = 0; i < originalPredicateForm.size(); i++) {
 JSTerm originalTerm = originalPredicateForm.elementAt(i);
 JSTerm clonedTerm = clonedPredicateForm.elementAt(i);
 assertEquals(originalTerm.toString(), clonedTerm.toString());
 }
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm predicateForm = new JSPredicateForm("(or (= X A) (= X B))");
 JSSubstitution alpha = new JSSubstitution();
 JSTerm a = new JSTerm("A");
 JSTerm b = new JSTerm("B");
 alpha.addBindings(new int[] { 1, a.getVariableNumber() }, a);
 alpha.addBindings(new int[] { 1, b.getVariableNumber() }, b);
 JSPredicateForm result = predicateForm.applySubstitutionPF(alpha);
 JSPredicateForm expectedResult = new JSPredicateForm("(or (= A A) (= A B))");
 assertEquals(expectedResult.size(), result.size());
 for (int i = 0; i < expectedResult.size(); i++) {
 JSTerm expectedTerm = expectedResult.elementAt(i);
 JSTerm resultTerm = result.elementAt(i);
 if (expectedTerm instanceof JSTerm && resultTerm instanceof JSTerm) {
 assertEquals(expectedTerm.toString(), resultTerm.toString());
 }
 }
 }

 @Test
 public void testMatches() {
 JSTerm a = new JSTerm("A");
 JSTerm b = new JSTerm("B");
 JSTerm c = new JSTerm("C");
 JSPredicateForm predicateForm1 = new JSPredicateForm("(or (= X A) (= X B))");
 JSPredicateForm predicateForm2 = new JSPredicateForm("(or (= X A) (= X C))");
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = new JSSubstitution();
 alpha.addBindings(new int[] { 1, a.getVariableNumber() }, a);
 alpha.addBindings(new int[] { 1, b.getVariableNumber() }, b);
 beta = predicateForm1.matches(predicateForm2, alpha);
 assertTrue(beta.isEmpty());

 alpha.clear();
 alpha.addBindings(new int[] { 1, c.getVariableNumber() }, c);
 beta = predicateForm1.matches(predicateForm2, alpha);
 assertTrue(beta.isEmpty());

 alpha.clear();
 alpha.addBindings(new int[] { 1, a.getVariableNumber() }, a);
 alpha.addBindings(new int[] { 2, b.getVariableNumber() }, c);
 beta = predicateForm1.matches(predicateForm2, alpha);
 assertFalse(beta.isEmpty());
 assertEquals(2, beta.size());
 assertEquals(a, beta.getBinding(0));
 assertEquals(c, beta.getBinding(1));
 }

 @Test
 public void testEquals() {
 JSTerm a = new JSTerm("A");
 JSTerm b = new JSTerm("B");
 JSPredicateForm predicateForm1 = new JSPredicateForm("(or (= X A) (= X B))");
 JSPredicateForm predicateForm2 = new JSPredicateForm("(or (= X A) (= X B))");
 JSPredicateForm predicateForm3 = new JSPredicateForm("(or (= X B) (= X A))");
 assertTrue(predicateForm1.equals(predicateForm2));
 assertFalse(predicateForm1.equals(predicateForm3));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSTerm a = new JSTerm("A");
 JSPredicateForm predicateForm1 = new JSPredicateForm("(not (or (= X A) (= X A)))");
 JSPredicateForm standardizedPredicateForm1 = predicateForm1.standarizerPredicateForm();
 assertEquals("(not (or (= A A) (= A A)))", standardizedPredicateForm1.toStr().toString());

 JSPredicateForm predicateForm2 = new JSPredicateForm("(or (= X A) (= X A))");
 JSPredicateForm standardizedPredicateForm2 = predicateForm2.standarizerPredicateForm();
 assertEquals("(or (= A A) (= A A))", standardizedPredicateForm2.toStr().toString());
 }
}