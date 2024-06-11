import static org.junit.Assert.*;
import static umd.cs.shop.JSUtil.*;

import org.junit.Before;
import org.junit.Test;

import umd.cs.shop.JSTerm;
import umd.cs.shop.JSJshopVars;
import umd.cs.shop.JSSubstitution;

public class JSTermTest {

 private JSTerm term1;
 private JSTerm term2;
 private JSTerm term3;
 private JSTerm term4;
 private JSTerm term5;
 private JSTerm term6;
 private JSTerm term7;
 private JSTerm term8;
 private JSTerm term9;
 private JSSubstitution substitution;

 @Before
 public void setUp() {
 term1 = new JSTerm();
 term1.addElement("x");
 term1.makeVariable();
 term2 = new JSTerm();
 term2.addElement("3");
 term2.makeConstant();
 term3 = new JSTerm();
 term3.addElement("(call (list x 2 3))");
 term3.makeFunction();
 term4 = new JSTerm();
 term4.addElement("(< x 5)");
 term4.makeFunction();
 term5 = new JSTerm();
 term5.addElement("%");
 term5.addElement("x");
 term5.makeFunction();
 term6 = new JSTerm();
 term6.addElement("(+ x 2)");
 term6.makeFunction();
 term7 = new JSTerm();
 term7.addElement("(nil)");
 term7.makeConstant();
 term8 = new JSTerm();
 term8.addElement("(call term5 term2)");
 term8.makeFunction();
 term9 = new JSTerm();
 term9.addElement("(call term6 term1)");
 term9.makeFunction();
 substitution = new JSSubstitution();
 }

 @Test
 public void testIsVariable() {
 assertTrue(term1.isVariable());
 assertFalse(term2.isVariable());
 }

 @Test
 public void testIsConstant() {
 assertFalse(term1.isConstant());
 assertTrue(term2.isConstant());
 }

 @Test
 public void testIsFunction() {
 assertFalse(term1.isFunction());
 assertFalse(term2.isFunction());
 assertTrue(term3.isFunction());
 }

 @Test
 public void testIsEval() {
 assertFalse(term1.isEval());
 assertFalse(term2.isEval());
 assertFalse(term3.isEval());
 assertTrue(term4.isEval());
 assertTrue(term5.isEval());
 assertTrue(term6.isEval());
 }

 @Test
 public void testAddElement() {
 term1.addElement("y");
 assertFalse(term1.isConstant());
 term1.addElement("3");
 assertFalse(term1.isVariable());
 term1.addElement("(");
 assertTrue(term1.isFunction());
 term1.addElement("nil");
 term1.makeConstant();
 assertEquals("(nil)", term1.toStr().toString());
 term1.addElement("x");
 term1.makeVariable();
 assertEquals("x", term1.elementAt(0).toString());
 }

 @Test
 public void testParseTerm() {
 term1 = new JSTerm();
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("x"));
 term1.parseTerm(tokenizer);
 assertEquals("x", term1.elementAt(0).toString());
 term1 = new JSTerm();
 tokenizer.reset();
 tokenizer.tts = JSJshopVars.leftPar;
 term1.parseTerm(tokenizer);
 assertFalse(term1.isConstant());
 assertFalse(term1.isVariable());
 assertTrue(term1.isFunction());
 term1.addElement("x");
 term1.makeVariable();
 term1.addElement("(");
 term1.addElement(term2);
 term1.addElement(")");
 term1.makeFunction();
 assertEquals("(x 3)", term1.toStr().toString());
 }

 @Test
 public void testPrint() {
 term1.print();
 assertEquals("x ", System.out.toString());
 term2.print();
 assertEquals("3 ", System.out.toString());
 term3.print();
 assertEquals("(call (list x 2 3)) ", System.out.toString());
 term4.print();
 assertEquals("(< x 5)", System.out.toString());
 term5.print();
 assertEquals("%x ", System.out.toString());
 term6.print();
 assertEquals("(+ x 2)", System.out.toString());
 term7.print();
 assertEquals("nil ", System.out.toString());
 term8.print();
 assertEquals("(call %x 3)", System.out.toString());
 term9.print();
 assertEquals("(call (+ x 2))", System.out.toString());
 }

 @Test
 public void testClone() {
 JSTerm clonedTerm1 = term1.cloneT();
 assertEquals("x", clonedTerm1.elementAt(0).toString());
 JSTerm clonedTerm2 = term2.cloneT();
 assertEquals("3", clonedTerm2.elementAt(0).toString());
 JSTerm clonedTerm3 = term3.cloneT();
 assertEquals("(call (list x 2 3))", clonedTerm3.toStr().toString());
 }

 @Test
 public void testApplySubstitution() {
 term1.applySubstitution(substitution);
 assertEquals("y", term1.elementAt(0).toString());
 term2.applySubstitution(substitution);
 assertEquals("3", term2.elementAt(0).toString());
 term3.applySubstitution(substitution);
 assertEquals("(call (list y 2 3))", term3.toStr().toString());
 }

 @Test
 public void testMatches() {
 term1.matches(term1);
 assertTrue(term1.matches(term1, substitution).fail());
 term2.matches(term2);
 assertTrue(term2.matches(term2, substitution).fail());
 term1.matches(term2);
 assertFalse(term1.matches(term2, substitution).fail());
 term2.matches(term1);
 assertFalse(term2.matches(term1, substitution).fail());
 term3.matches(term3);
 assertTrue(term3.matches(term3, substitution).fail());
 term3.matches(term4);
 assertFalse(term3.matches(term4, substitution).fail());
 term4.matches(term3);
 assertFalse(term4.matches(term3, substitution).fail());
 term3.matches(term5);
 assertFalse(term3.matches(term5, substitution).fail());
 term5.matches(term3);
 assertFalse(term5.matches(term3, substitution).fail());
 term5.matches(term6);
 assertFalse(term5.matches(term6, substitution).fail());
 term6.matches(term5);
 assertFalse(term6.matches(term5, substitution).fail());
 term5.matches(term9);
 assertFalse(term5.matches(term9, substitution).fail());
 term9.matches(term5);
 assertFalse(term9.matches(term5, substitution).fail());
 term9.matches(term8);
 assertTrue(term9.matches(term8, substitution).fail());
 term8.matches(term9);
 assertTrue(term8.matches(term9, substitution).fail());
 }

 @Test
 public void testEquals() {
 assertEquals(term1, term1);
 assertNotEquals(term1, term2);
 assertEquals(term2, term2);
 assertNotEquals(term1, term2);
 assertEquals(term3, term3);
 assertEquals(term3, term3);
 assertEquals(term3, term3);
 assertEquals(term4, term4);
 assertEquals(term4, term4);
 assertEquals(term4, term4);
 assertEquals(term5, term5);
 assertEquals(term5, term5);
 assertEquals(term5, term5);
 assertEquals(term6, term6);
 assertEquals(term6, term6);
 assertEquals(term6, term6);
 assertEquals(term7, term7);
 assertEquals(term7, term7);
 assertEquals(term7, term7);
 assertEquals(term8, term8);
 assertEquals(term8, term8);
 assertEquals(term8, term8);
 assertEquals(term9, term9);
 assertEquals(term9, term9);
 assertEquals(term9, term9);
 }

 @Test
 public void testSize() {
 assertEquals(0, term7.size());
 assertEquals(1, term1.size());
 assertEquals(3, term3.size());
 assertEquals(1, term4.size());
 assertEquals(2, term8.size());
 assertEquals(2, term9.size());
 }

 @Test
 public void testElementAt() {
 assertEquals("x", term1.elementAt(0).toString());
 assertEquals("3", term2.elementAt(0).toString());
 assertEquals("x", term3.elementAt(0).toString());
 assertEquals("<", term4.elementAt(0).toString());
 assertEquals("%", term5.elementAt(0).toString());
 assertEquals("+", term6.elementAt(0).toString());
 assertEquals("nil", term7.elementAt(0).toString());
 assertEquals("call", term8.elementAt(0).toString());
 assertEquals("list", term8.elementAt(1).toString());
 assertEquals("x", term8.elementAt(2).toString());
 assertEquals("2", term8.elementAt(3).toString());
 assertEquals("3", term8.elementAt(4).toString());
 assertEquals("call", term9.elementAt(0).toString());
 assertEquals("(", term9.elementAt(1).toString());
 assertEquals("+", term9.elementAt(2).toString());
 assertEquals("x", term9.elementAt(3).toString());
 assertEquals(")", term9.elementAt(4).toString());
 }
}