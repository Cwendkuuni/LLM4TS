import static org.junit.Assert.*;
import static umd.cs.shop.JSJshopVars.*;

import org.junit.Test;

import umd.cs.shop.JSTerm;
import umd.cs.shop.JSTermParser;

public class JSTermTest {

 private JSTermParser parser;

 @Before
 public void setUp() {
 parser = new JSTermParser();
 }

 @Test
 public void testEmptyTerm() {
 JSTerm term = new JSTerm();
 assertEquals("nil", term.toString());
 assertTrue(term.isGround());
 }

 @Test
 public void testVariableTerm() {
 JSTerm term = parser.parseTerm("x");
 assertEquals("x", term.toString());
 assertTrue(term.isVariable());
 }

 @Test
 public void testConstantTerm() {
 JSTerm term = parser.parseTerm("42");
 assertEquals("42", term.toString());
 assertTrue(term.isConstant());
 }

 @Test
 public void testFunctionTerm() {
 JSTerm term = parser.parseTerm("(call f x)");
 assertEquals("call f x", term.toString());
 assertTrue(term.isFunction());
 }

 @Test
 public void testListTerm() {
 JSTerm term = parser.parseTerm("(list 1 2 3)");
 assertEquals("1 . 2 . 3", term.toString());
 }

 @Test
 public void testComplexTerm() {
 JSTerm term = parser.parseTerm("(call f (list 1 (call g x) 2))");
 assertEquals("call f (1 . (call g x) . 2)", term.toString());
 }

 @Test
 public void testTermEquality() {
 JSTerm term1 = parser.parseTerm("x");
 JSTerm term2 = parser.parseTerm("x");
 assertTrue(term1.equals(term2));
 }

 @Test
 public void testTermInequality() {
 JSTerm term1 = parser.parseTerm("x");
 JSTerm term2 = parser.parseTerm("y");
 assertFalse(term1.equals(term2));
 }

 @Test
 public void testTermSubstitution() {
 JSTerm term = parser.parseTerm("(call f x)");
 JSSubstitution substitution = new JSSubstitution();
 substitution.add(new JSPairVarTerm("x", parser.parseTerm("42")));
 JSTerm expected = parser.parseTerm("(call f 42)");
 JSTerm actual = term.applySubstitution(substitution);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testTermCloning() {
 JSTerm term = parser.parseTerm("(call f x)");
 JSTerm clone = term.cloneT();
 assertEquals(term.toString(), clone.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTerm() {
 parser.parseTerm("bad");
 }

}