package umd.cs.shop;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class JSTermTest {

 private StreamTokenizer mockTokenizer;
 private JSTerm termUnderTest;

 @Test
 public void testEmptyTermConstruction() {
 termUnderTest = new JSTerm();
 assertFalse(termUnderTest.isFunction());
 assertFalse(termUnderTest.isVariable());
 assertFalse(termUnderTest.isConstant());
 assertFalse(termUnderTest.isEval());
 assertEquals(0, termUnderTest.size());
 }

 @Test
 public void testVariableTermConstruction() {
 mockTokenizer = mock(StreamTokenizer.class);
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.variable);
 termUnderTest = new JSTerm(mockTokenizer);
 assertTrue(termUnderTest.isVariable());
 assertFalse(termUnderTest.isConstant());
 assertFalse(termUnderTest.isFunction());
 assertFalse(termUnderTest.isEval());
 assertEquals(1, termUnderTest.size());
 assertEquals("?x", termUnderTest.elementAt(0).toString());
 }

 @Test
 public void testConstantTermConstruction() {
 mockTokenizer = mock(StreamTokenizer.class);
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.number);
 when(mockTokenizer.sval).thenReturn("42");
 termUnderTest = new JSTerm(mockTokenizer);
 assertFalse(termUnderTest.isVariable());
 assertTrue(termUnderTest.isConstant());
 assertFalse(termUnderTest.isFunction());
 assertFalse(termUnderTest.isEval());
 assertEquals(1, termUnderTest.size());
 assertEquals("42", termUnderTest.elementAt(0).toString());
 }

 @Test
 public void testFunctionTermConstruction() {
 mockTokenizer = mock(StreamTokenizer.class);
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.leftPar);
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.leftPar);
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.percent);
 when(mockTokenizer.nextToken()).thenReturn("sin");
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.leftPar);
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.Term);
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.rightPar);
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.rightPar);
 termUnderTest = new JSTerm(mockTokenizer);
 assertTrue(termUnderTest.isFunction());
 assertFalse(termUnderTest.isVariable());
 assertFalse(termUnderTest.isConstant());
 assertFalse(termUnderTest.isEval());
 assertEquals(2, termUnderTest.size());
 assertEquals("sin", termUnderTest.elementAt(0).toString());
 }

 @Test
 public void testListTermConstruction() throws IOException {
 String input = "(* (sin x) (+ 2 3) (* 2 x))";
 mockTokenizer = new StreamTokenizer(new StringReader(input));
 termUnderTest = new JSTerm(mockTokenizer);
 assertFalse(termUnderTest.isVariable());
 assertFalse(termUnderTest.isConstant());
 assertFalse(termUnderTest.isFunction());
 assertTrue(termUnderTest.isEval());
 assertEquals(3, termUnderTest.size());
 assertEquals("call", termUnderTest.elementAt(0).toString());
 assertEquals(new JSTerm(), termUnderTest.elementAt(1));
 assertEquals(new JSTerm("sin", new JSTerm("x")), termUnderTest.elementAt(2));
 }

 @Test(expected = JSParserError.class)
 public void testInvalidTermConstruction() throws IOException {
 String input = "(* (sin x) (+ 2 3) (* 2 x) %%)";
 mockTokenizer = new StreamTokenizer(new StringReader(input));
 termUnderTest = new JSTerm(mockTokenizer);
 fail("Expected JSParserError");
 }

 @Test
 public void testPrintTerm() {
 termUnderTest = new JSTerm("x");
 assertEquals("x ", termUnderTest.print());
 termUnderTest = new JSTerm("(sin x)");
 assertEquals("(sin x) ", termUnderTest.print());
 termUnderTest = new JSTerm("(+ 2 3)");
 assertEquals("(+ 2 3) ", termUnderTest.print());
 termUnderTest = new JSTerm("(* 2 x)");
 assertEquals("(* 2 x) ", termUnderTest.print());
 termUnderTest = new JSTerm("(call sin (x))");
 assertEquals("(call sin (x)) ", termUnderTest.print());
 }

 @Test
 public void testCloneTerm() {
 termUnderTest = new JSTerm("x");
 JSTerm clonedTerm = termUnderTest.cloneT();
 assertEquals(termUnderTest, clonedTerm);
 termUnderTest = new JSTerm("(sin x)");
 clonedTerm = termUnderTest.cloneT();
 assertEquals(termUnderTest, clonedTerm);
 termUnderTest = new JSTerm("(+ 2 3)");
 clonedTerm = termUnderTest.cloneT();
 assertEquals(termUnderTest, clonedTerm);
 termUnderTest = new JSTerm("(* 2 x)");
 clonedTerm = termUnderTest.cloneT();
 assertEquals(termUnderTest, clonedTerm);
 termUnderTest = new JSTerm("(call sin (x))");
 clonedTerm = termUnderTest.cloneT();
 assertEquals(termUnderTest, clonedTerm);
 }

 @Test
 public void testApplySubstitutionTerm() {
 termUnderTest = new JSTerm("x");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPairTerm("x", new JSTerm("y")));
 JSTerm result = termUnderTest.applySubstitutionT(substitution);
 assertEquals(new JSTerm("y"), result);
 termUnderTest = new JSTerm("(sin x)");
 result = termUnderTest.applySubstitutionT(substitution);
 assertEquals(new JSTerm("(sin y)"), result);
 termUnderTest = new JSTerm("(+ 2 x)");
 result = termUnderTest.applySubstitutionT(substitution);
 assertEquals(new JSTerm("(+ 2 y)"), result);
 termUnderTest = new JSTerm("(* 2 x)");
 result = termUnderTest.applySubstitutionT(substitution);
 assertEquals(new JSTerm("(* 2 y)"), result);
 termUnderTest = new JSTerm("(call sin (x))");
 result = termUnderTest.applySubstitutionT(substitution);
 assertEquals(new JSTerm("(call sin (y))"), result);
 }

 @Test
 public void testMatchesTerm() {
 termUnderTest = new JSTerm("x");
 JSTerm t = new JSTerm("x");
 JSSubstitution substitution = termUnderTest.matches(t);
 assertTrue(substitution.isEmpty());
 termUnderTest = new JSTerm("(sin x)");
 t = new JSTerm("(cos x)");
 substitution = termUnderTest.matches(t);
 assertTrue(substitution.isEmpty());
 termUnderTest = new JSTerm("(sin x)");
 t = new JSTerm("(sin x)");
 substitution = termUnderTest.matches(t);
 assertTrue(substitution.isEmpty());
 termUnderTest = new JSTerm("(sin x)");
 t = new JSTerm("(sin y)");
 substitution = termUnderTest.matches(t);
 assertTrue(substitution.isEmpty());
 termUnderTest = new JSTerm("(sin x)");
 t = new JSTerm("(cos x)");
 substitution = termUnderTest.matches(t);
 assertFalse(substitution.isEmpty());
 termUnderTest = new JSTerm("(+ 2 x)");
 t = new JSTerm("(+ 2 y)");
 substitution = termUnderTest.matches(t);
 assertTrue(substitution.isEmpty());
 termUnderTest = new JSTerm("(* 2 x)");
 t = new JSTerm("(* 2 y)");
 substitution = termUnderTest.matches(t);
 assertTrue(substitution.isEmpty());
 termUnderTest = new JSTerm("(call sin (x))");
 t = new JSTerm("(call cos (x))");
 substitution = termUnderTest.matches(t);
 assertTrue(substitution.isEmpty());
 termUnderTest = new JSTerm("(call sin (x))");
 t = new JSTerm("(call sin (y))");
 substitution = termUnderTest.matches(t);
 assertTrue(substitution.isEmpty());
 termUnderTest = new JSTerm("(call sin (x))");
 t = new JSTerm("(call cos (x))");
 substitution = termUnderTest.matches(t);
 assertFalse(substitution.isEmpty());
 }

 @Test
 public void testEqualsTerm() {
 termUnderTest = new JSTerm("x");
 JSTerm t1 = new JSTerm("x");
 JSTerm t2 = new JSTerm("y");
 assertTrue(termUnderTest.equals(t1));
 assertFalse(termUnderTest.equals(t2));
 termUnderTest = new JSTerm("(sin x)");
 t1 = new JSTerm("(sin x)");
 t2 = new JSTerm("(sin y)");
 assertTrue(termUnderTest.equals(t1));
 assertFalse(termUnderTest.equals(t2));
 termUnderTest = new JSTerm("(sin x)");
 t1 = new JSTerm("(cos x)");
 t2 = new JSTerm("(sin y)");
 assertFalse(termUnderTest.equals(t1));
 assertFalse(termUnderTest.equals(t2));
 termUnderTest = new JSTerm("(+ 2 x)");
 t1 = new JSTerm("(+ 2 x)");
 t2 = new JSTerm("(+ 2 y)");
 assertTrue(termUnderTest.equals(t1));
 assertFalse(termUnderTest.equals(t2));
 termUnderTest = new JSTerm("(* 2 x)");
 t1 = new JSTerm("(+ 2 x)");
 t2 = new JSTerm("(+ 2 y)");
 assertFalse(termUnderTest.equals(t1));
 assertFalse(termUnderTest.equals(t2));
 termUnderTest = new JSTerm("(call sin (x))");
 t1 = new JSTerm("(call sin (x))");
 t2 = new JSTerm("(call sin (y))");
 assertTrue(termUnderTest.equals(t1));
 assertFalse(termUnderTest.equals(t2));
 termUnderTest = new JSTerm("(call sin (x))");
 t1 = new JSTerm("(call cos (x))");
 t2 = new JSTerm("(call sin (y))");
 assertFalse(termUnderTest.equals(t1));
 assertFalse(termUnderTest.equals(t2));
 }

 @Test
 public void testIsVariableTerm() {
 termUnderTest = new JSTerm("x");
 assertTrue(termUnderTest.isVariable());
 termUnderTest = new JSTerm("(sin x)");
 assertFalse(termUnderTest.isVariable());
 termUnderTest = new JSTerm("(+ 2 x)");
 assertFalse(termUnderTest.isVariable());
 termUnderTest = new JSTerm("(* 2 x)");
 assertFalse(termUnderTest.isVariable());
 termUnderTest = new JSTerm("(call sin (x))");
 assertFalse(termUnderTest.isVariable());
 }

 @Test
 public void testIsConstantTerm() {
 termUnderTest = new JSTerm("42");
 assertTrue(termUnderTest.isConstant());
 termUnderTest = new JSTerm("x");
 assertFalse(termUnderTest.isConstant());
 termUnderTest = new JSTerm("(sin x)");
 assertFalse(termUnderTest.isConstant());
 termUnderTest = new JSTerm("(+ 2 x)");
 assertFalse(termUnderTest.isConstant());
 termUnderTest = new JSTerm("(* 2 x)");
 assertFalse(termUnderTest.isConstant());
 termUnderTest = new JSTerm("(call sin (x))");
 assertFalse(termUnderTest.isConstant());
 }

 @Test
 public void testIsFunctionTerm() {
 termUnderTest = new JSTerm("x");
 assertFalse(termUnderTest.isFunction());
 termUnderTest = new JSTerm("(sin x)");
 assertTrue(termUnderTest.isFunction());
 termUnderTest = new JSTerm("(+ 2 x)");
 assertFalse(termUnderTest.isFunction());
 termUnderTest = new JSTerm("(* 2 x)");
 assertFalse(termUnderTest.isFunction());
 termUnderTest = new JSTerm("(call sin (x))");
 assertTrue(termUnderTest.isFunction());
 }

 @Test
 public void testIsEvalTerm() {
 termUnderTest = new JSTerm("x");
 assertFalse(termUnderTest.isEval());
 termUnderTest = new JSTerm("(sin x)");
 assertFalse(termUnderTest.isEval());
 termUnderTest = new JSTerm("(+ 2 x)");
 assertFalse(termUnderTest.isEval());
 termUnderTest = new JSTerm("(* 2 x)");
 assertFalse(termUnderTest.isEval());
 termUnderTest = new JSTerm("(call sin (x))");
 assertTrue(termUnderTest.isEval());
 }

 @Test
 public void testIsGroundTerm() {
 termUnderTest = new JSTerm("42");
 assertTrue(termUnderTest.isGround());
 termUnderTest = new JSTerm("x");
 assertFalse(termUnderTest.isGround());
 termUnderTest = new JSTerm("(sin x)");
 assertFalse(termUnderTest.isGround());
 termUnderTest = new JSTerm("(+ 2 x)");
 assertFalse(termUnderTest.isGround());
 termUnderTest = new JSTerm("(* 2 x)");
 assertFalse(termUnderTest.isGround());
 termUnderTest = new JSTerm("(call sin (x))");
 assertFalse(termUnderTest.isGround());
 termUnderTest = new JSTerm("(sin x)");
 termUnderTest.makeConstant();
 assertTrue(termUnderTest.isGround());
 termUnderTest = new JSTerm("(+ 2 x)");
 termUnderTest.makeConstant();
 assertTrue(termUnderTest.isGround());
 termUnderTest = new JSTerm("(* 2 x)");
 termUnderTest.makeConstant();
 assertTrue(termUnderTest.isGround());
 termUnderTest = new JSTerm("(call sin (x))");
 termUnderTest.makeConstant();
 assertTrue(termUnderTest.isGround());
 }

 @Test
 public void testStandardizerTerm() {
 termUnderTest = new JSTerm("x");
 JSTerm result = termUnderTest.standardizerTerm();
 assertEquals("?x", result.toString());
 termUnderTest = new JSTerm("(sin x)");
 result = termUnderTest.standardizerTerm();
 assertEquals("(sin ?x)", result.toString());
 termUnderTest = new JSTerm("(+ 2 x)");
 result = termUnderTest.standardizerTerm();
 assertEquals("(+ 2 ?x)", result.toString());
 termUnderTest = new JSTerm("(* 2 x)");
 result = termUnderTest.standardizerTerm();
 assertEquals("(+ (* 2 ?x) 0)", result.toString());
 termUnderTest = new JSTerm("(call sin (x))");
 result = termUnderTest.standardizerTerm();
 assertEquals("(call sin (?x))", result.toString());
 }

 @Test
 public void testCallTerm() {
 termUnderTest = new JSTerm("x");
 assertEquals(termUnderTest, termUnderTest.call());
 termUnderTest = new JSTerm("(sin x)");
 JSTerm result = termUnderTest.call();
 assertEquals("(sin x)", result.toString());
 termUnderTest = new JSTerm("(+ 2 x)");
 result = termUnderTest.call();
 assertEquals("2 + x", result.toString());
 termUnderTest = new JSTerm("(* 2 x)");
 result = termUnderTest.call();
 assertEquals("2 * x", result.toString());
 termUnderTest = new JSTerm("(call sin (x))");
 result = termUnderTest.call();
 assertEquals("sin(x)", result.toString());
 }
}