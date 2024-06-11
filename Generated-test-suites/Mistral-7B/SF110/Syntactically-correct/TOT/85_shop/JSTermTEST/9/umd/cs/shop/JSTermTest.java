package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSTermTest {

 private JSTerm createTerm(String input) {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
 return new JSTerm(tokenizer);
 }

 @Test
 public void testEmptyTerm() {
 JSTerm term = new JSTerm();
 assertTrue(term.isEmpty());
 }

 @Test
 public void testVariableTerm() {
 JSTerm term = createTerm("x");
 assertTrue(term.isVariable());
 assertEquals("x", term.elementAt(0));
 }

 @Test
 public void testConstantTerm() {
 JSTerm term = createTerm("3");
 assertTrue(term.isConstant());
 assertEquals("3", term.elementAt(0));
 }

 @Test
 public void testFunctionTerm() {
 JSTerm term = createTerm("(+ 1 2)");
 assertTrue(term.isFunction());
 assertEquals("+(1 2)", term.elementAt(0));
 assertEquals(2, term.size());
 }

 @Test
 public void testFunctionTermWithArguments() {
 JSTerm term = createTerm("(+ x 3)");
 assertTrue(term.isFunction());
 assertEquals("+(x 3)", term.elementAt(0));
 assertEquals(2, term.size());
 }

 @Test
 public void testListTerm() {
 JSTerm term = createTerm("(list x y z)");
 assertTrue(term.isFunction());
 assertEquals("list", term.elementAt(0));
 assertEquals(3, term.size());
 }

 @Test
 public void testListTermWithElements() {
 JSTerm term = createTerm("(list 1 2 3)");
 assertTrue(term.isFunction());
 assertEquals("list", term.elementAt(0));
 assertEquals(3, term.size());
 assertEquals(3, term.elementAt(1).size());
 }

 @Test
 public void testApplicationTerm() {
 JSTerm term = createTerm("(+ 1 (+ 2 3))");
 assertTrue(term.isFunction());
 assertEquals("+(1 (+ 2 3))", term.elementAt(0));
 assertEquals(3, term.size());
 }

 @Test
 public void testNestedApplicationTerm() {
 JSTerm term = createTerm("((+ 1) (+ 2 3))");
 assertTrue(term.isFunction());
 assertEquals("((+ 1) (+ 2 3))", term.elementAt(0));
 assertEquals(3, term.size());
 }

 @Test(expected = JSParserError.class)
 public void testMalformedInput() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("x ("));
 new JSTerm(tokenizer);
 fail("Expected JSParserError");
 }

 @Test(expected = JSParserError.class)
 public void testMissingTerm() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(list)"));
 new JSTerm(tokenizer);
 fail("Expected JSParserError");
 }

 @Test(expected = JSParserError.class)
 public void testMissingRightParenthesis() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(list x y"));
 new JSTerm(tokenizer);
 fail("Expected JSParserError");
 }

 @Test(expected = JSParserError.class)
 public void testExtraToken() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(list x y 5)"));
 new JSTerm(tokenizer);
 fail("Expected JSParserError");
 }

 @Test
 public void testParseList() {
 JSTerm term = createTerm("(list x y z)");
 JSTerm list = term.parseList(new StreamTokenizer(new StringReader("(x y z)")));
 assertEquals("x", list.elementAt(0).elementAt(0));
 assertEquals("y", list.elementAt(1).elementAt(0));
 assertEquals("z", list.elementAt(2).elementAt(0));
 }

 @Test
 public void testCloneTerm() {
 JSTerm term = createTerm("(+ 1 2)");
 JSTerm clonedTerm = term.cloneT();
 assertEquals(term, clonedTerm);
 }

 @Test
 public void testApplySubstitution() {
 JSTerm term = createTerm("(+ x y)");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPair("x", new JSTerm("3")));
 JSTerm result = term.applySubstitutionT(substitution);
 assertEquals("+(3 y)", result.toString());
 }

 @Test
 public void testMatchesTerm() {
 JSTerm term1 = createTerm("(+ x y)");
 JSTerm term2 = createTerm("(+ z x y)");
 JSSubstitution substitution = term1.matches(term2);
 assertEquals(2, substitution.size());
 assertEquals(new JSPair("x", new JSTerm("z")), substitution.elementAt(0));
 assertEquals(new JSPair("y", term2.elementAt(2)), substitution.elementAt(1));
 }

 @Test
 public void testEqualsTerm() {
 JSTerm term1 = createTerm("(+ x y)");
 JSTerm term2 = createTerm("(+ x y)");
 JSTerm term3 = createTerm("(+ z x y)");
 assertTrue(term1.equals(term2));
 assertFalse(term1.equals(term3));
 }

 @Test
 public void testIsGround() {
 JSTerm term = createTerm("(+ x 3)");
 assertFalse(term.isGround());
 JSTerm groundTerm = term.standardizerTerm();
 assertTrue(groundTerm.isGround());
 }

 @Test
 public void testStandardizerTerm() {
 JSTerm term = createTerm("(+ x 3)");
 JSTerm result = term.standardizerTerm();
 assertEquals("+(x1 3)", result.toString());
 }

 @Test
 public void testCallTerm() {
 JSTerm term = createTerm("(+ 1 2)");
 JSTerm result = term.call();
 assertEquals("3", result.toString());
 }

}