package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;

import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSParserError;
import umd.cs.shop.JSPairVarTerm;
import umd.cs.shop.JSSubstitution;

public class JSTermTest {

 @Test
 public void testEmptyTermConstruction() {
 JSTerm term = new JSTerm();
 assertTrue(term.isEmpty());
 }

 @Test
 public void testTermVariableConstruction() {
 JSTerm term = new JSTerm("x");
 assertEquals("x", term.toString());
 }

 @Test
 public void testTermConstantConstruction() {
 JSTerm term = new JSTerm("5");
 assertEquals("5", term.toString());
 }

 @Test
 public void testTermListConstruction() {
 JSTerm term = new JSTerm("(+ x 3)");
 assertEquals("(+ x 3)", term.toString());
 }

 @Test
 public void testTermParseError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x"));
 try {
 JSTerm term = new JSTerm(tokenizer);
 fail("Expected JSParserError");
 } catch (JSParserError e) {
 // expected
 }
 }

 @Test
 public void testTermParseVariable() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(x)"));
 JSTerm term = new JSTerm(tokenizer);
 assertEquals("x", term.toString());
 }

 @Test
 public void testTermParseConstant() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(5)"));
 JSTerm term = new JSTerm(tokenizer);
 assertEquals("5", term.toString());
 }

 @Test
 public void testTermParseFunction() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+(x 3))"));
 JSTerm term = new JSTerm(tokenizer);
 assertEquals("(+(x 3))", term.toString());
 }

 @Test
 public void testTermParseFunctionCall() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+(x 3) 4)"));
 JSTerm term = new JSTerm(tokenizer);
 assertEquals("(+(x 3) 4)", term.toString());
 }

 @Test
 public void testTermParseList() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+(x 3) (+(x 2) 2))"));
 JSTerm term = new JSTerm(tokenizer);
 assertEquals("(+(x 3) (+(x 2) 2))", term.toString());
 }

 @Test
 public void testTermParseFailure() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(%%%)"));
 try {
 JSTerm term = new JSTerm(tokenizer);
 fail("Expected JSParserError");
 } catch (JSParserError e) {
 // expected
 }
 }

 @Test
 public void testTermEquals() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("x");
 JSTerm term3 = new JSTerm("y");
 assertTrue(term1.equals(term2));
 assertFalse(term1.equals(term3));
 }

 @Test
 public void testTermIsVariable() {
 JSTerm term = new JSTerm("x");
 assertTrue(term.isVariable());
 }

 @Test
 public void testTermIsConstant() {
 JSTerm term = new JSTerm("5");
 assertTrue(term.isConstant());
 }

 @Test
 public void testTermIsFunction() {
 JSTerm term = new JSTerm("+(x 3)");
 assertTrue(term.isFunction());
 }

 @Test
 public void testTermIsEval() {
 JSTerm term = new JSTerm("(+(x 3))");
 assertFalse(term.isEval());
 JSTerm term2 = new JSTerm("(+(x 3))");
 term2.makeEval(true);
 assertTrue(term2.isEval());
 }

 @Test
 public void testTermListPrint() {
 JSTerm term = new JSTerm("(+(x 3) (+(x 2) 2))");
 term.makeEval(true);
 JSUtil.println(term.toString());
 assertEquals("(+(x 3) (+(x 2) 2))", JSUtil.toString());
 }

 @Test
 public void testTermListClone() {
 JSTerm term = new JSTerm("(+(x 3) (+(x 2) 2))");
 term.makeEval(true);
 JSTerm term2 = term.cloneT();
 assertEquals(term.toString(), term2.toString());
 }

 @Test
 public void testTermListApplySubstitution() {
 JSTerm term = new JSTerm("(+(x 3) (+(x 2) 2))");
 term.makeEval(true);
 JSTerm term2 = new JSTerm("(+(y 3) (+(y 2) 2))");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement(new JSPairVarTerm("x", "y"));
 JSTerm term3 = term.applySubstitutionT(alpha);
 assertEquals(term2.toString(), term3.toString());
 }

 @Test
 public void testTermListMatches() {
 JSTerm term1 = new JSTerm("(+(x 3) (+(x 2) 2))");
 term1.makeEval(true);
 JSTerm term2 = new JSTerm("(+(y 3) (+(y 2) 2))");
 JSTerm term3 = new JSTerm("(+(x 3) (+(x 2) 2))");
 JSTerm term4 = new JSTerm("(+(x 3) 5)");
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = term1.matches(term2);
 assertEquals(0, beta.size());
 beta = term1.matches(term3);
 JSPairVarTerm p1 = (JSPairVarTerm)beta.elementAt(0);
 assertEquals("x", p1.getVarTerm().toString());
 assertEquals("y", p1.getValueTerm().toString());
 beta = term1.matches(term4);
 assertFalse(beta.fail());
 }
}