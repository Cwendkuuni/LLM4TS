package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSTermTest {

 @Test
 public void testEmptyTerm() {
 JSTerm term = new JSTerm();
 assertEquals("", term.toStr().toString());
 assertFalse(term.isVariable());
 assertFalse(term.isConstant());
 assertFalse(term.isFunction());
 assertFalse(term.isEval());
 }

 @Test
 public void testConstantTerm() {
 JSTerm term = new JSTerm("3");
 assertEquals("3", term.toStr().toString());
 assertTrue(term.isConstant());
 assertFalse(term.isVariable());
 assertFalse(term.isFunction());
 assertFalse(term.isEval());
 }

 @Test
 public void testVariableTerm() {
 JSTerm term = new JSTerm("x");
 assertEquals("x", term.toStr().toString());
 assertTrue(term.isVariable());
 assertFalse(term.isConstant());
 assertFalse(term.isFunction());
 assertFalse(term.isEval());
 }

 @Test
 public void testFunctionTerm() {
 JSTerm term = new JSTerm("(+ x 3)");
 assertEquals("(+ x 3)", term.toStr().toString());
 assertTrue(term.isFunction());
 assertFalse(term.isVariable());
 assertFalse(term.isConstant());
 assertFalse(term.isEval());
 }

 @Test
 public void testEvalTerm() {
 JSTerm term = new JSTerm("(+ x 3)");
 term.makeEval(true);
 JSTerm evaluableTerm = new JSTerm("x");
 evaluableTerm.makeEval(true);
 evaluableTerm.addElement("3");
 JSTerm expectedResult = new JSTerm();
 expectedResult.addElement("+");
 expectedResult.addElement(evaluableTerm);
 expectedResult.makeFunction();
 expectedResult.addElement("nil");
 assertEquals(expectedResult.toStr().toString(), term.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testUnexpectedTokenParserError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 3 %%%"));
 JSTerm term = new JSTerm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testUnexpectedCharacterParserError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 3 abc"));
 JSTerm term = new JSTerm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testMissingLeftParenthesisParserError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 3"));
 JSTerm term = new JSTerm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testMissingRightParenthesisParserError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 3"));
 JSTerm term = new JSTerm(tokenizer);
 tokenizer.nextToken();
 }

 @Test(expected = JSParserError.class)
 public void testUnexpectedTokenTypeParserError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 3 4 +)"));
 JSTerm term = new JSTerm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testUnexpectedWordParserError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 3 call)"));
 JSTerm term = new JSTerm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testUnexpectedVariableNameParserError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 3 xyz)"));
 JSTerm term = new JSTerm(tokenizer);
 }

 @Test
 public void testListTerm() {
 JSTerm term = new JSTerm();
 term.addElement("(+ x 3)");
 term.addElement("(+ y 4)");
 term.addElement("nil");
 term.makeFunction();
 assertEquals("(+ x 3) (+ y 4)", term.toStr().toString());
 }

 @Test
 public void testListTermWithVariables() {
 JSTerm term = new JSTerm();
 term.addElement("(+ x 3)");
 term.addElement("(+ y 4)");
 term.addElement("(+ z 5)");
 term.addElement("nil");
 term.makeFunction();
 assertEquals("(+ x 3) (+ y 4) (+ z 5)", term.toStr().toString());
 }

 @Test
 public void testListTermWithVariablesAndNumbers() {
 JSTerm term = new JSTerm();
 term.addElement("(+ x 3)");
 term.addElement("5");
 term.addElement("(+ y 4)");
 term.addElement("nil");
 term.makeFunction();
 assertEquals("(+ x 3) 5 (+ y 4)", term.toStr().toString());
 }

 @Test
 public void testListTermWithFunctionCall() {
 JSTerm term = new JSTerm();
 term.addElement("(+ x 3)");
 term.addElement("(call sin x)");
 term.addElement("nil");
 term.makeFunction();
 assertEquals("(+ x 3) (call sin x)", term.toStr().toString());
 }
}