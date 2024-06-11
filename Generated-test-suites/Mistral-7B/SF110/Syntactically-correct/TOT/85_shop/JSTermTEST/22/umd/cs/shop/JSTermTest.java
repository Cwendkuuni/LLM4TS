package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class JSTermTest {

 private static final String TEST_DATA = "(% call term1 term2)"; // Function call
 private static final String TEST_DATA_VAR = "(list . term1)"; // List with variable
 private static final String TEST_DATA_VAR_FUNCTION = "(call term1 term2)"; // Variable as function call
 private static final String TEST_DATA_CONST = "(+ 2 3)"; // Constant addition
 private static final String TEST_DATA_ERROR_MISSING_PAREN = "term1 term2"; // Missing opening parenthesis
 private static final String TEST_DATA_ERROR_MISSING_TERM = " (% 2)"; // Missing term after opening parenthesis

 @Test
 public void testJSTerm_ConstructorWithTokenizer() {
 JSTerm term;
 try (StringReader reader = new StringReader(TEST_DATA)) {
 term = new JSTerm(new StreamTokenizer(reader));
 }

 assertEquals("call", term.elementAt(0).toString());
 assertEquals(2, term.size());
 assertNotNull(term.elementAt(1));
 assertNotNull(term.elementAt(2));
 }

 @Test
 public void testJSTerm_ParseList() {
 JSTerm term = new JSTerm();
 term.addElement("list");
 term.addElement(new JSTerm());
 term.addElement(new JSTerm());

 assertEquals("list", term.elementAt(0).toString());
 assertEquals(3, term.size());
 }

 @Test
 public void testJSTerm_Print() {
 JSTerm term = new JSTerm();
 term.addElement("call");
 term.addElement(new JSTerm());
 term.addElement(new JSTerm());

 assertEquals("(call term term)", term.toStr().toString());
 }

 @Test
 public void testJSTerm_Equals() {
 JSTerm term1 = new JSTerm();
 term1.addElement("+");
 term1.addElement(new JSTerm());
 term1.addElement(new JSTerm());
 JSTerm term2 = new JSTerm();
 term2.addElement("+");
 term2.addElement(new JSTerm());
 term2.addElement(new JSTerm());

 assertTrue(term1.equals(term2));
 }

 @Test
 public void testJSTerm_NotEquals() {
 JSTerm term1 = new JSTerm();
 term1.addElement("+");
 term1.addElement(new JSTerm());
 term1.addElement(new JSTerm());
 JSTerm term2 = new JSTerm();
 term2.addElement("-");
 term2.addElement(new JSTerm());
 term2.addElement(new JSTerm());

 assertFalse(term1.equals(term2));
 }

 @Test
 public void testJSTerm_IsVariable() {
 JSTerm term = new JSTerm();
 term.addElement("term1");
 term.makeVariable();

 assertTrue(term.isVariable());
 }

 @Test
 public void testJSTerm_IsConstant() {
 JSTerm term = new JSTerm();
 term.addElement("2");
 term.makeConstant();

 assertTrue(term.isConstant());
 }

 @Test
 public void testJSTerm_IsFunction() {
 JSTerm term = new JSTerm();
 term.addElement("call");
 term.makeFunction();

 assertTrue(term.isFunction());
 }

 @Test
 public void testJSTerm_IsEval() {
 JSTerm term = new JSTerm();
 term.makeEval(true);

 assertTrue(term.isEval());
 }

 @Test
 public void testJSTerm_CloneT() {
 JSTerm term = new JSTerm();
 term.addElement("+");
 term.addElement(new JSTerm());
 term.addElement(new JSTerm());
 JSTerm clonedTerm = term.cloneT();

 assertEquals(term, clonedTerm);
 }

 @Test
 public void testJSTerm_ApplySubstitutionT() {
 JSTerm term = new JSTerm();
 term.addElement("x");
 term.makeVariable();
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPair("x", new JSTerm("3")));
 JSTerm result = term.applySubstitutionT(substitution);

 assertEquals("3", result.elementAt(0).toString());
 }

 @Test
 public void testJSTerm_Matches() {
 JSTerm term1 = new JSTerm();
 term1.addElement("x");
 term1.makeVariable();
 JSTerm term2 = new JSTerm();
 term2.addElement("y");
 term2.makeVariable();
 JSTerm term3 = new JSTerm();
 term3.addElement("x");
 term3.makeVariable();
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPair("x", term2));
 JSTerm expectedTerm = new JSTerm();
 expectedTerm.addElement("y");
 expectedTerm.makeVariable();

 JSTerm result = term1.matches(term3, substitution);

 assertEquals(expectedTerm, result);
 }

 @Test
 public void testJSTerm_ToString() {
 JSTerm term = new JSTerm();
 term.addElement("call");
 term.addElement(new JSTerm());
 term.addElement(new JSTerm());

 assertEquals("(call term term)", term.toStr().toString());
 }

 @Test
 public void testJSTerm_IsGround() {
 JSTerm term = new JSTerm();
 term.addElement("x");
 term.makeVariable();
 JSTerm groundTerm = new JSTerm();
 groundTerm.addElement("3");
 groundTerm.makeConstant();

 assertFalse(term.isGround());
 assertTrue(groundTerm.isGround());
 }

 @Test
 public void testJSTerm_StandardizerTerm() {
 JSTerm term = new JSTerm();
 term.addElement("x");
 term.makeVariable();
 JSTerm standardizedTerm = term.standardizerTerm();

 assertEquals("Var" + (JSJshopVars.VarCounter++), standardizedTerm.elementAt(0).toString());
 }

 @Test
 public void testJSTerm_Call() {
 JSTerm term1 = new JSTerm();
 term1.addElement("+");
 term1.addElement(new JSTerm());
 term1.addElement(new JSTerm());
 JSTerm term2 = new JSTerm();
 term2.addElement("2");
 term2.addElement(new JSTerm());
 term2.addElement(new JSTerm());
 term2.makeConstant();
 JSTerm result = term1.call();

 assertEquals(new JSTerm("2"), result);
 }

 @Test(expected = JSParserError.class)
 public void testJSTerm_UnexpectedTokenizerError_MissingParenthesis() {
 JSTerm term = new JSTerm();
 try (StringReader reader = new StringReader(TEST_DATA_ERROR_MISSING_PAREN)) {
 term = new JSTerm(new StreamTokenizer(reader));
 }
 }

 @Test(expected = JSParserError.class)
 public void testJSTerm_UnexpectedTokenizerError_MissingTerm() {
 JSTerm term = new JSTerm();
 try (StringReader reader = new StringReader(TEST_DATA_ERROR_MISSING_TERM)) {
 term = new JSTerm(new StreamTokenizer(reader));
 }
 }
}