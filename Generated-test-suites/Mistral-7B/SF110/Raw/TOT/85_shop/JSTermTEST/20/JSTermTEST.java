package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSTermTest {

 @Test
 public void testEmptyTerm() {
 JSTerm term = new JSTerm();
 assertFalse(term.isFunction());
 assertFalse(term.isVariable());
 assertFalse(term.isConstant());
 assertFalse(term.isEval());
 assertEquals("", term.toStr().toString());
 }

 @Test
 public void testConstantTerm() {
 JSTerm term = new JSTerm("42");
 assertTrue(term.isConstant());
 assertFalse(term.isFunction());
 assertFalse(term.isVariable());
 assertFalse(term.isEval());
 assertEquals("42", term.toStr().toString());
 }

 @Test
 public void testVariableTerm() {
 JSTerm term = new JSTerm("x");
 assertTrue(term.isVariable());
 assertFalse(term.isFunction());
 assertFalse(term.isConstant());
 assertFalse(term.isEval());
 assertEquals("x", term.toStr().toString());
 }

 @Test
 public void testFunctionTerm() {
 JSTerm term = new JSTerm("call", new JSTerm("+"), new JSTerm("42"), new JSTerm("x"));
 assertTrue(term.isFunction());
 assertFalse(term.isVariable());
 assertFalse(term.isConstant());
 assertFalse(term.isEval());
 assertEquals("(call + 42 x)", term.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testInvalidTokenError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("42 x"));
 JSTerm term = new JSTerm(tokenizer);
 fail("JSParserError expected");
 }

 @Test(expected = JSParserError.class)
 public void testInvalidFunctionNameError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("call foo x"));
 JSTerm term = new JSTerm(tokenizer);
 fail("JSParserError expected");
 }

 @Test(expected = JSParserError.class)
 public void testMissingRightParenthesisError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("call x +"));
 JSTerm term = new JSTerm(tokenizer);
 fail("JSParserError expected");
 }

 @Test
 public void testListTermWithEmptyList() {
 JSTerm term = new JSTerm(".");
 JSTerm listTerm = new JSTerm(tokenizer -> {
 tokenizer.nextToken(); // .
 term.addElement(new JSTerm(tokenizer));
 tokenizer.nextToken(); // list
 tokenizer.nextToken(); // right parenthesis
 });
 assertTrue(term.isFunction());
 assertFalse(term.isVariable());
 assertFalse(term.isConstant());
 assertFalse(term.isEval());
 assertEquals("(call list)", term.toStr().toString());
 }

 @Test
 public void testListTermWithOneElement() {
 JSTerm term = new JSTerm(".");
 JSTerm listTerm = new JSTerm(tokenizer -> {
 tokenizer.nextToken(); // .
 term.addElement(new JSTerm(tokenizer));
 tokenizer.nextToken(); // list
 JSTerm element = new JSTerm(tokenizer);
 tokenizer.nextToken(); // right parenthesis
 term.addElement(element);
 });
 assertTrue(term.isFunction());
 assertFalse(term.isVariable());
 assertFalse(term.isConstant());
 assertFalse(term.isEval());
 assertEquals("(call list 42)", term.toStr().toString());
 }

 @Test
 public void testListTermWithTwoElements() {
 JSTerm term = new JSTerm(".");
 JSTerm listTerm = new JSTerm(tokenizer -> {
 tokenizer.nextToken(); // .
 term.addElement(new JSTerm(tokenizer));
 tokenizer.nextToken(); // list
 JSTerm el1 = new JSTerm(tokenizer);
 JSTerm el2 = new JSTerm(tokenizer);
 tokenizer.nextToken(); // right parenthesis
 term.addElement(el1);
 term.addElement(el2);
 });
 assertTrue(term.isFunction());
 assertFalse(term.isVariable());
 assertFalse(term.isConstant());
 assertFalse(term.isEval());
 assertEquals("(call list 42 x)", term.toStr().toString());
 }

 @Test
 public void testApplySubstitutionWithEmptySubstitution() {
 JSTerm term = new JSTerm("x");
 JSSubstitution substitution = new JSSubstitution();
 JSTerm result = term.applySubstitution(substitution);
 assertFalse(result.isFunction());
 assertFalse(result.isVariable());
 assertFalse(result.isConstant());
 assertFalse(result.isEval());
 assertEquals("x", result.toStr().toString());
 }

 @Test
 public void testApplySubstitutionWithVariableSubstitution() {
 JSTerm term = new JSTerm("x");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPairVarTerm("x", new JSTerm("y")));
 JSTerm result = term.applySubstitution(substitution);
 assertTrue(result.isVariable());
 assertFalse(result.isFunction());
 assertFalse(result.isConstant());
 assertFalse(result.isEval());
 assertEquals("y", result.toStr().toString());
 }

 @Test
 public void testApplySubstitutionWithFunctionSubstitution() {
 JSTerm term = new JSTerm("x");
 JSTerm functionTerm = new JSTerm("call", new JSTerm("+"), new JSTerm("x"), new JSTerm("3"));
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPairVarTerm("x", functionTerm));
 JSTerm result = term.applySubstitution(substitution);
 assertTrue(result.isFunction());
 assertFalse(result.isVariable());
 assertFalse(result.isConstant());
 assertFalse(result.isEval());
 assertEquals("(call + 3 3)", result.toStr().toString());
 }

 @Test
 public void testMatchesWithEmptySubstitution() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSSubstitution substitution = new JSSubstitution();
 JSSubstitution beta = term1.matches(term2, substitution);
 assertFalse(beta.fail());
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testMatchesWithVariableSubstitution() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSTerm term3 = new JSTerm("z");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPairVarTerm("x", term3));
 JSSubstitution beta = term1.matches(term2, substitution);
 assertFalse(beta.fail());
 assertTrue(beta.isEmpty());
 assertFalse(term1.equals(term2));
 }

 @Test
 public void testMatchesWithFunctionSubstitution() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("call", new JSTerm("+"), new JSTerm("x"), new JSTerm("3"));
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPairVarTerm("x", term1));
 JSSubstitution beta = term1.matches(term2, substitution);
 assertFalse(beta.fail());
 assertTrue(beta.isEmpty());
 assertTrue(term1.equals(term2));
 }

 @Test
 public void testMatchesWithFailure() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSSubstitution substitution = new JSSubstitution();
 JSSubstitution beta = term1.matches(term2, substitution);
 assertFalse(beta.fail());
 assertFalse(beta.isEmpty());
 }

 @Test
 public void testGroundTerm() {
 JSTerm term = new JSTerm("call", new JSTerm("+"), new JSTerm("42"), new JSTerm("x"));
 assertFalse(term.isGround());
 }

 @Test
 public void testGroundVariableTerm() {
 JSTerm term = new JSTerm("x");
 term.makeVariable();
 assertTrue(term.isGround());
 }

 @Test
 public void testGroundConstantTerm() {
 JSTerm term = new JSTerm("42");
 term.makeConstant();
 assertTrue(term.isGround());
 }

 @Test
 public void testGroundFunctionTerm() {
 JSTerm term = new JSTerm("call", new JSTerm("+"), new JSTerm("42"), new JSTerm("x"));
 term.makeFunction();
 assertFalse(term.isGround());
 }

 @Test
 public void testGroundListTerm() {
 JSTerm term = new JSTerm(".");
 JSTerm listTerm = new JSTerm(tokenizer -> {
 tokenizer.nextToken(); // .
 term.addElement(new JSTerm(tokenizer));
 tokenizer.nextToken(); // list
 JSTerm el1 = new JSTerm(tokenizer).makeConstant();
 JSTerm el2 = new JSTerm(tokenizer).makeConstant();
 tokenizer.nextToken(); // right parenthesis
 term.addElement(el1);
 term.addElement(el2);
 term.makeFunction();
 });
 assertTrue(term.isGround());
 }
}