import static org.junit.Assert.*;
import static umd.cs.shop.JSUtil.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import umd.cs.shop.JSTerm;
import umd.cs.shop.JSTermParser;

public class JSTermTest {

 private JSTerm term;
 private JSTerm term1;
 private JSTerm term2;
 private JSTerm term3;
 private JSTerm term4;
 private JSTerm term5;
 private JSTerm term6;
 private JSTerm term7;
 private JSTerm term8;
 private JSTerm term9;
 private JSTerm term10;
 private StreamTokenizer tokenizer;
 private JSTermParser parser;

 @Before
 public void setUp() {
 tokenizer = new StreamTokenizer(new StringReader("(+ 1 2)"));
 parser = new JSTermParser();
 }

 @After
 public void tearDown() {
 tokenizer.close();
 }

 @Test
 public void testJSTermEmptyConstructor() {
 term = new JSTerm();
 assertTrue(term.isEmpty());
 assertFalse(term.isEval());
 assertFalse(term.isFunction());
 assertFalse(term.isVariable());
 assertFalse(term.isConstant());
 }

 @Test
 public void testJSTermWithTokenizerConstructor() {
 term = new JSTerm(tokenizer);
 assertFalse(term.isEmpty());
 assertFalse(term.isEval());
 assertFalse(term.isFunction());
 assertFalse(term.isVariable());
 assertFalse(term.isConstant());
 assertEquals("Expecting Term", term.elementAt(0).toString(), "(+");
 }

 @Test
 public void testIsVariableTrue() {
 term1 = new JSTerm();
 term1.addElement("x");
 term1.makeVariable();
 assertTrue(term1.isVariable());
 assertFalse(term1.isConstant());
 assertFalse(term1.isFunction());
 assertFalse(term1.isEval());
 }

 @Test
 public void testIsVariableFalse() {
 term2 = new JSTerm();
 term2.addElement("x");
 term2.makeConstant();
 assertFalse(term2.isVariable());
 assertTrue(term2.isConstant());
 assertFalse(term2.isFunction());
 assertFalse(term2.isEval());
 }

 @Test
 public void testIsFunctionTrue() {
 term3 = new JSTerm();
 term3.addElement("+");
 term3.addElement("1");
 term3.addElement("2");
 term3.makeFunction();
 assertTrue(term3.isFunction());
 assertFalse(term3.isVariable());
 assertFalse(term3.isConstant());
 assertFalse(term3.isEval());
 }

 @Test
 public void testIsEvalTrue() {
 term4 = new JSTerm();
 term4.addElement("call");
 term4.addElement("+");
 term4.addElement("1");
 term4.addElement("2");
 term4.makeEval(true);
 assertTrue(term4.isEval());
 assertFalse(term4.isFunction());
 assertFalse(term4.isVariable());
 assertFalse(term4.isConstant());
 }

 @Test
 public void testIsEvalFalse() {
 term5 = new JSTerm();
 term5.addElement("call");
 term5.addElement("+");
 term5.addElement("1");
 term5.addElement("2");
 term5.makeEval(false);
 assertFalse(term5.isEval());
 assertTrue(term5.isFunction());
 assertFalse(term5.isVariable());
 assertFalse(term5.isConstant());
 }

 @Test
 public void testSizeZero() {
 term6 = new JSTerm();
 assertEquals(0, term6.size());
 }

 @Test
 public void testSizeOne() {
 term7 = new JSTerm();
 term7.addElement("x");
 assertEquals(1, term7.size());
 }

 @Test
 public void testSizeMoreThanOne() {
 term8 = new JSTerm();
 term8.addElement("+");
 term8.addElement("1");
 term8.addElement("2");
 assertEquals(3, term8.size());
 }

 @Test
 public void testGetElementAtZero() {
 term9 = new JSTerm();
 term9.addElement("+");
 term9.addElement("1");
 term9.addElement("2");
 assertEquals("+", term9.elementAt(0).toString());
 }

 @Test
 public void testGetElementAtOne() {
 term10 = new JSTerm();
 term10.addElement("+");
 term10.addElement("1");
 term10.addElement("2");
 assertEquals("1", term10.elementAt(1).toString());
 }

 @Test
 public void testGetElementAtLast() {
 term = new JSTerm();
 term.addElement("+");
 term.addElement("1");
 term.addElement("2");
 assertEquals("2", term.elementAt(2).toString());
 }

 @Test
 public void testAddElement() {
 term = new JSTerm();
 term.addElement("+");
 term.addElement("1");
 term.addElement("2");
 term.addElement("3");
 assertEquals(4, term.size());
 assertEquals("3", term.elementAt(3).toString());
 }

 @Test
 public void testToString() {
 term = new JSTerm();
 term.addElement("+");
 term.addElement("1");
 term.addElement("2");
 assertEquals("(+ 1 2)", term.toStr().toString());
 }

 @Test
 public void testIsGround() {
 term = new JSTerm();
 term.addElement(new Double(2.0));
 assertTrue(term.isGround());
 term = new JSTerm();
 term.addElement("x");
 assertFalse(term.isGround());
 term = new JSTerm();
 term.addElement("+");
 term.addElement(new Double(2.0));
 term.addElement("x");
 assertFalse(term.isGround());
 term = new JSTerm();
 term.addElement("+");
 term.addElement("x");
 term.addElement(new Double(2.0));
 assertFalse(term.isGround());
 term = new JSTerm();
 term.addElement("+");
 term.addElement(new Double(2.0));
 term.addElement(new Double(2.0));
 assertTrue(term.isGround());
 }

 @Test
 public void testStandardizerTerm() {
 term = new JSTerm();
 term.addElement("+");
 term.addElement("x");
 term.addElement("x");
 term.makeFunction();
 assertEquals("(+(x x))", term.standardizerTerm().toStr().toString());
 term = new JSTerm();
 term.addElement("x");
 term.makeVariable();
 assertEquals("(Var" + (JSUtil.VarCounter++) + ")", term.standardizerTerm().toStr().toString());
 term = new JSTerm();
 term.addElement("1");
 term.makeConstant();
 assertEquals("1", term.standardizerTerm().toStr().toString());
 term = new JSTerm();
 term.addElement("not");
 term.addElement("x");
 term.makeFunction();
 assertEquals("(not (Var" + (JSUtil.VarCounter++) + "))", term.standardizerTerm().toStr().toString());
 }

 @Test
 public void testCall() {
 term = new JSTerm();
 term.addElement("+");
 term.addElement("1");
 term.addElement("2");
 term.makeFunction();
 assertEquals("(call + 1 2)", term.call().toStr().toString());
 term = new JSTerm();
 term.addElement("+");
 term.addElement("1");
 term.addElement("2");
 term.makeEval(true);
 assertEquals("1", term.call().toStr().toString());
 term = new JSTerm();
 term.addElement("not");
 term.addElement("not");
 term.makeFunction();
 assertEquals("(call not not)", term.call().toStr().toString());
 term = new JSTerm();
 term.addElement("not");
 term.addElement("+");
 term.addElement("1");
 term.addElement("2");
 term.makeFunction();
 assertEquals("(- 1 2)", term.call().toStr().toString());
 }

 @Test
 public void testApplySubstitutionT() {
 term = new JSTerm();
 term.addElement("x");
 term.makeVariable();
 term1 = new JSTerm();
 term1.addElement("y");
 term1.makeVariable();
 term2 = term.applySubstitutionT(new JSSubstitution().assign("x", term1));
 assertEquals(term1, term2);
 term = new JSTerm();
 term.addElement("x");
 term.makeVariable();
 term1 = new JSTerm();
 term1.addElement("y");
 term1.makeVariable();
 term2 = new JSTerm();
 term2.addElement("z");
 term2.makeVariable();
 term3 = term.applySubstitutionT(new JSSubstitution().assign("x", term1).assign("y", term2));
 assertEquals("(Var" + (JSUtil.VarCounter++) + ")", term3.elementAt(0).toString());
 term = new JSTerm();
 term.addElement("+");
 term.addElement("x");
 term.addElement("y");
 term.makeFunction();
 term1 = new JSTerm();
 term1.addElement("x");
 term1.makeVariable();
 term2 = new JSTerm();
 term2.addElement("y");
 term2.makeVariable();
 term3 = new JSTerm();
 term3.addElement("z");
 term3.makeVariable();
 term4 = new JSTerm();
 term4.addElement("+");
 term4.addElement(term1);
 term4.addElement(term2);
 term5 = term.applySubstitutionT(new JSSubstitution().assign("x", term1).assign("y", term2).assign("z", term3).assign("x", term4));
 assertEquals("(call + (Var" + (JSUtil.VarCounter++) + ") (Var" + (JSUtil.VarCounter++) + "))", term5.toStr().toString());
 }

 @Test
 public void testMatches() {
 term = new JSTerm();
 term1 = new JSTerm();
 term2 = new JSTerm();
 term3 = new JSTerm();
 term4 = new JSTerm();
 term5 = new JSTerm();
 term6 = new JSTerm();
 JSSubstitution substitution = new JSSubstitution();

 term.addElement("+");
 term.addElement("x");
 term.addElement("y");
 term.makeFunction();
 term1.addElement("+");
 term1.addElement("1");
 term1.addElement("2");
 term1.makeConstant();
 term2.addElement("x");
 term2.makeVariable();
 term3.addElement("x");
 term3.makeVariable();
 term4.addElement("y");
 term4.makeVariable();
 term5.addElement("x");
 term5.makeVariable();
 term6.addElement("y");
 term6.makeVariable();

 assertTrue(term.matches(term1, substitution).isEmpty());
 assertTrue(term.matches(term2, substitution).isEmpty());
 assertTrue(term.matches(term3, substitution).isEmpty());
 assertTrue(term.matches(term4, substitution).isEmpty());
 assertTrue(term.matches(term5, substitution).isEmpty());

 term.addElement("x");
 term.makeVariable();
 term1.addElement("x");
 term1.makeVariable();
 term2.addElement("y");
 term2.makeVariable();
 term3.addElement("x");
 term3.makeVariable();
 term4.addElement("y");
 term4.makeVariable();
 term5.addElement("z");
 term5.makeVariable();
 term6.addElement("y");
 term6.makeVariable();

 substitution.assign("x", term1);
 substitution.assign("y", term2);
 assertEquals(term3, term.matches(term5, substitution).instance(term3));
 substitution.clear();
 substitution.assign("x", term3);
 substitution.assign("y", term6);
 assertEquals(term2, term.matches(term5, substitution).instance(term2));
 }

 @Test
 public void testAddElementsFromAnotherTerm() {
 term = new JSTerm();
 term1 = new JSTerm();
 term2 = new JSTerm();
 term3 = new JSTerm();
 term4 = new JSTerm();
 term5 = new JSTerm();
 term6 = new JSTerm();
 term7 = new JSTerm();
 term8 = new JSTerm();
 term9 = new JSTerm();
 term10 = new JSTerm();

 term.addElement("+");
 term.addElement("x");
 term.addElement("y");
 term.makeFunction();
 term1.addElement("*");
 term1.addElement("x");
 term1.addElement("x");
 term1.makeFunction();
 term2.addElement("x");
 term2.makeVariable();
 term3.addElement("y");
 term3.makeVariable();
 term4.addElement("x");
 term4.makeVariable();
 term5.addElement("y");
 term5.makeVariable();
 term6.addElement("z");
 term6.makeVariable();
 term7.addElement("!");
 term7.addElement("x");
 term7.makeFunction();
 term8.addElement("x");
 term8.makeVariable();
 term9.addElement("y");
 term9.makeVariable();
 term10.addElement("+");
 term10.addElement("x");
 term10.addElement("y");
 term10.makeFunction();

 term.addElements(term1);
 assertEquals("(+ x y (* x x))", term.toStr().toString());
 term.addElements(term2, term3, term4, term5);
 assertEquals("(+ x y x y)", term.toStr().toString());
 term.addElement(term6);
 assertEquals("(+ x y x y z)", term.toStr().toString());
 term.addElement(term7);
 assertEquals("(+(+ x y x y) ! x)", term.toStr().toString());
 term.addElements(term8, term9);
 assertEquals("(+(+(+ x y x y) x) y)", term.toStr().toString());
 term.addElements(term10, term7, term8, term9);
 assertEquals("(+(+(+(+(+ x y x y) x) y) (+ x y) x) y)", term.toStr().toString());
 }
}