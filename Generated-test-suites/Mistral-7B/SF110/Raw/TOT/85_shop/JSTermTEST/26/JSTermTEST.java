package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSTermTest {

 @Test
 public void testEmptyTerm() {
 JSTerm term = new JSTerm();
 assertTrue(term.isFunction());
 assertTrue(term.size() == 0);
 }

 @Test
 public void testVariableTerm() {
 JSTerm term = new JSTerm("x");
 assertTrue(term.isVariable());
 assertEquals("x", term.elementAt(0));
 }

 @Test
 public void testConstantTerm() {
 JSTerm term = new JSTerm("2");
 assertTrue(term.isConstant());
 assertEquals("2", term.elementAt(0));
 }

 @Test
 public void testFunctionTermWithOneArgument() {
 JSTerm term = new JSTerm("+(x)");
 assertTrue(term.isFunction());
 assertEquals(2, term.size());
 assertEquals("+(", term.elementAt(0));
 assertTrue(term.elementAt(1) instanceof JSTerm);
 assertEquals("x", ((JSTerm)term.elementAt(1)).elementAt(0));
 }

 @Test
 public void testFunctionTermWithTwoArguments() {
 JSTerm term = new JSTerm("+(x y)");
 assertTrue(term.isFunction());
 assertEquals(3, term.size());
 assertEquals("+(", term.elementAt(0));
 assertTrue(term.elementAt(1) instanceof JSTerm);
 assertEquals("x", ((JSTerm)term.elementAt(1)).elementAt(0));
 assertTrue(term.elementAt(2) instanceof JSTerm);
 assertEquals("y", ((JSTerm)term.elementAt(2)).elementAt(0));
 }

 @Test(expected = JSParserError.class)
 public void testMissingLeftParenthesisDuringFunctionCall() {
 JSTerm term = new JSTerm("+(x");
 }

 @Test(expected = JSParserError.class)
 public void testMissingRightParenthesisDuringFunctionCall() {
 JSTerm term = new JSTerm("+(x)");
 term.addElement("y");
 }

 @Test(expected = JSParserError.class)
 public void testMissingTermDuringFunctionCall() {
 JSTerm term = new JSTerm();
 term.makeFunction();
 term.addElement("+");
 term.addElement("(");
 term.addElement("x");
 term.addElement(")");
 term.addElement(null);
 }

 @Test
 public void testListTerm() {
 JSTerm term = new JSTerm();
 term.makeFunction();
 term.addElement("list");
 term.addElement(".");
 term.addElement(new JSTerm("x"));
 term.addElement(new JSTerm("y"));
 term.addElement(new JSTerm("z"));
 assertTrue(term.isFunction());
 assertEquals(4, term.size());
 assertEquals("list", term.elementAt(0));
 assertTrue(term.elementAt(1).isVariable());
 assertEquals("x", term.elementAt(1).elementAt(0));
 assertTrue(term.elementAt(2).isVariable());
 assertEquals("y", term.elementAt(2).elementAt(0));
 assertTrue(term.elementAt(3).isVariable());
 assertEquals("z", term.elementAt(3).elementAt(0));
 }

 @Test
 public void testListTermWithEmptyList() {
 JSTerm term = new JSTerm();
 term.makeFunction();
 term.addElement("list");
 term.addElement("nil");
 assertTrue(term.isFunction());
 assertEquals(2, term.size());
 assertEquals("list", term.elementAt(0));
 assertTrue(term.elementAt(1).isConstant());
 assertEquals("nil", term.elementAt(1).toString());
 }

 @Test
 public void testListTermWithOneElement() {
 JSTerm term = new JSTerm();
 term.makeFunction();
 term.addElement("list");
 term.addElement(".");
 term.addElement(new JSTerm("x"));
 term.addElement("nil");
 assertTrue(term.isFunction());
 assertEquals(3, term.size());
 assertEquals("list", term.elementAt(0));
 assertTrue(term.elementAt(1).isVariable());
 assertEquals("x", term.elementAt(1).elementAt(0));
 assertTrue(term.elementAt(2).isConstant());
 assertEquals("nil", term.elementAt(2).toString());
 }

 @Test
 public void testListTermWithTwoElements() {
 JSTerm term = new JSTerm();
 term.makeFunction();
 term.addElement("list");
 term.addElement(".");
 term.addElement(new JSTerm("x"));
 term.addElement(".");
 term.addElement(new JSTerm("y"));
 term.addElement("nil");
 assertTrue(term.isFunction());
 assertEquals(4, term.size());
 assertEquals("list", term.elementAt(0));
 assertTrue(term.elementAt(1).isVariable());
 assertEquals("x", term.elementAt(1).elementAt(0));
 assertTrue(term.elementAt(2).isVariable());
 assertEquals(".", term.elementAt(2).elementAt(0));
 assertTrue(term.elementAt(3).isVariable());
 assertEquals("y", term.elementAt(3).elementAt(0));
 assertTrue(term.elementAt(4).isConstant());
 assertEquals("nil", term.elementAt(4).toString());
 }

 @Test
 public void testTermApplication() {
 JSTerm term1 = new JSTerm();
 term1.makeFunction();
 term1.addElement("call");
 term1.addElement(".");
 term1.addElement(new JSTerm("sin"));
 term1.addElement(".");
 term1.addElement(new JSTerm("x"));
 JSTerm term2 = new JSTerm();
 term2.makeVariable();
 term2.addElement("x");
 term2.makeConstant();
 term2.addElement("3.14");
 JSTerm term3 = term1.applySubstitutionT(new JSSubstitution().assign(term2, term2.cloneT()));
 assertTrue(term3.isFunction());
 assertEquals("call", term3.elementAt(0));
 assertEquals("sin", term3.elementAt(1).elementAt(0));
 assertEquals("3.14", term3.elementAt(2).elementAt(0));
 }
}