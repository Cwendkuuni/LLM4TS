package umd.cs.shop;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class JSTermTest {

 private StreamTokenizer mockTokenizer;
 private JSTerm termUnderTest;

 @Before
 public void setup() {
 mockTokenizer = mock(StreamTokenizer.class);
 termUnderTest = new JSTerm();
 }

 @Test
 public void testEmptyTermConstruction() {
 termUnderTest = new JSTerm();
 assertTrue(termUnderTest.isEmpty());
 assertFalse(termUnderTest.isVariable());
 assertFalse(termUnderTest.isConstant());
 assertFalse(termUnderTest.isFunction());
 assertFalse(termUnderTest.isEval());
 }

 @Test
 public void testTermConstructionWithValidInput() throws IOException {
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.leftPar);
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.Term);
 when(mockTokenizer.nextToken()).thenReturn("x");
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.rightPar);
 termUnderTest = new JSTerm(mockTokenizer);

 assertTrue(termUnderTest.isFunction());
 assertEquals("x", termUnderTest.elementAt(0));
 assertEquals(0, termUnderTest.size());
 }

 @Test(expected = JSParserError.class)
 public void testTermConstructionWithInvalidInput() throws IOException {
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.Term);
 when(mockTokenizer.nextToken()).thenReturn(-1);
 termUnderTest = new JSTerm(mockTokenizer);
 fail("Should have thrown JSParserError exception");
 }

 @Test
 public void testParseList() throws IOException {
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.leftPar);
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.Term);
 when(mockTokenizer.nextToken()).thenReturn(".");
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.Term);
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.rightPar);
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.Term);
 termUnderTest = new JSTerm();
 JSTerm listTerm = termUnderTest.parseList(mockTokenizer);

 assertEquals("nil", listTerm.elementAt(0).toString());
 assertEquals(0, listTerm.size());
 }

 @Test(expected = JSParserError.class)
 public void testParseListWithInvalidInput() throws IOException {
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.leftPar);
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.Term);
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.Term);
 when(mockTokenizer.nextToken()).thenReturn(JSJshopVars.rightPar);
 when(mockTokenizer.nextToken()).thenReturn(-1);
 termUnderTest = new JSTerm();
 JSTerm listTerm = termUnderTest.parseList(mockTokenizer);
 fail("Should have thrown JSParserError exception");
 }

 @Test
 public void testPrint() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("x");
 termUnderTest.addElement("y");
 termUnderTest.addElement("z");
 termUnderTest.makeFunction();
 termUnderTest.print();

 assertEquals("(x y z)", termUnderTest.toStr().toString());
 }

 @Test
 public void testClone() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("x");
 termUnderTest.addElement("y");
 termUnderTest.addElement("z");
 termUnderTest.makeFunction();
 JSTerm clonedTerm = termUnderTest.cloneT();

 assertEquals(termUnderTest.toString(), clonedTerm.toString());
 }

 @Test
 public void testApplySubstitution() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("x");termUnderTest.addElement("y");
 termUnderTest.addElement("z");
 termUnderTest.makeFunction();
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement(new JSPairVarTerm("x", new JSTerm("2")));
 JSTerm beta = termUnderTest.applySubstitutionT(alpha);

 assertEquals("(2 y z)", beta.toStr().toString());
 }

 @Test
 public void testMatches() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("x");termUnderTest.addElement("y");
 termUnderTest.addElement("z");
 termUnderTest.makeFunction();
 JSTerm termToMatch = new JSTerm();
 termToMatch.addElement("a");termToMatch.addElement("b");
 termToMatch.addElement("c");
 termToMatch.makeFunction();
 JSSubstitution beta = termUnderTest.matches(termToMatch);

 assertTrue(beta.isEmpty());
 }

 @Test
 public void testMatchesWithVariable() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("x");termUnderTest.addElement("y");
 termUnderTest.addElement("z");
 termUnderTest.makeFunction();
 JSTerm termToMatch = new JSTerm();
 termToMatch.addElement("a");termToMatch.addElement("y");
 termToMatch.addElement("c");
 termToMatch.makeFunction();
 JSSubstitution beta = termUnderTest.matches(termToMatch);

 assertEquals(1, beta.size());
 assertEquals(new JSPairVarTerm("x", new JSTerm("a")), beta.elementAt(0));
 }

 @Test
 public void testMatchesWithMatchingFunction() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("x");termUnderTest.addElement("y");
 termUnderTest.addElement("z");
 termUnderTest.makeFunction();
 JSTerm termToMatch = new JSTerm();
 termToMatch.addElement("x");termToMatch.addElement("y");
 termToMatch.addElement("z");
 termToMatch.makeFunction();
 JSSubstitution beta = termUnderTest.matches(termToMatch);

 assertTrue(beta.isEmpty());
 }

 @Test
 public void testMatchesWithDifferentFunctionSize() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("x");termUnderTest.addElement("y");
 termUnderTest.addElement("z");
 termUnderTest.makeFunction();
 JSTerm termToMatch = new JSTerm();
 termToMatch.addElement("x");termToMatch.addElement("y");
 termToMatch.addElement("z");
 termToMatch.addElement("w");
 termToMatch.makeFunction();
 JSSubstitution beta = termUnderTest.matches(termToMatch);

 assertTrue(beta.isEmpty());
 }

 @Test
 public void testEquals() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("x");termUnderTest.addElement("y");
 termUnderTest.addElement("z");
 termUnderTest.makeFunction();
 JSTerm termToCompare = new JSTerm();
 termToCompare.addElement("x");termToCompare.addElement("y");
 termToCompare.addElement("z");
 termToCompare.makeFunction();
 assertTrue(termUnderTest.equals(termToCompare));
 }

 @Test
 public void testEqualsWithDifferentTypes() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("x");termUnderTest.addElement("y");
 termUnderTest.addElement("z");
 termUnderTest.makeFunction();
 JSTerm termToCompare = new JSTerm();
 termToCompare.addElement("a");termToCompare.addElement("b");
 termToCompare.addElement("c");
 termToCompare.makeFunction();
 assertFalse(termUnderTest.equals(termToCompare));
 }

 @Test
 public void testEqualsWithDifferentFunctionOrder() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("x");termUnderTest.addElement("y");
 termUnderTest.addElement("z");
 termUnderTest.makeFunction();
 JSTerm termToCompare = new JSTerm();
 termToCompare.addElement("z");termToCompare.addElement("y");
 termToCompare.addElement("x");
 termToCompare.makeFunction();
 assertFalse(termUnderTest.equals(termToCompare));
 }

 @Test
 public void testToString() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("x");termUnderTest.addElement("y");
 termUnderTest.addElement("z");
 termUnderTest.makeFunction();
 assertEquals("(x y z)", termUnderTest.toStr().toString());
 }

 @Test
 public void testIsGround() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("x");termUnderTest.addElement("y");
 termUnderTest.addElement("z");
 termUnderTest.makeFunction();
 assertFalse(termUnderTest.isGround());
 }

 @Test
 public void testIsGroundWithConstantTerm() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("2");
 termUnderTest.makeConstant();
 assertTrue(termUnderTest.isGround());
 }

 @Test
 public void testIsGroundWithVariableTerm() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("x");
 termUnderTest.makeVariable();
 assertFalse(termUnderTest.isGround());
 }

 @Test
 public void testStandardizerTerm() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("x");termUnderTest.addElement("y");
 termUnderTest.addElement("z");
 termUnderTest.makeFunction();
 JSTerm standardizedTerm = termUnderTest.standardizerTerm();
 assertEquals("(x X1 Y1 Z1)", standardizedTerm.toStr().toString());
 }

 @Test
 public void testCall() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("not");termUnderTest.addElement("x");
 termUnderTest.makeEval(true);
 termUnderTest.makeFunction();
 JSTerm result = termUnderTest.call();
 assertEquals("nil", result.toStr().toString());
 }

 @Test
 public void testCallWithUnaryOperator() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("neg");termUnderTest.addElement("x");
 termUnderTest.makeEval(true);
 termUnderTest.makeFunction();
 JSTerm result = termUnderTest.call();
 assertEquals("-x", result.toStr().toString());
 }

 @Test
 public void testCallWithBinaryOperator() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("+");termUnderTest.addElement("x");termUnderTest.addElement("y");
 termUnderTest.makeEval(true);
 termUnderTest.makeFunction();
 JSTerm result = termUnderTest.call();
 assertEquals("x + y", result.toStr().toString());
 }

 @Test
 public void testCallWithTernaryOperator() {
 termUnderTest = new JSTerm();
 termUnderTest.addElement("if");termUnderTest.addElement("x");
 termUnderTest.addElement(":");termUnderTest.addElement("y");termUnderTest.addElement(":");termUnderTest.addElement("z");
 termUnderTest.makeEval(true);
 termUnderTest.makeFunction();
 JSTerm result = termUnderTest.call();
 assertEquals("if x then y else z", result.toStr().toString());
 }
}