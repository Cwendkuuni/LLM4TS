import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;

public class JSTermTest {

 @org.junit.Before
 public void setUp() {
 // Initialize any state here if needed
 }

 @org.junit.After
 public void tearDown() {
 // Clean up any state here if needed
 }

 @org.junit.Test
 public void testEmptyConstructor() {
 JSTerm term = new JSTerm();
 assertFalse(term.isEval());
 assertFalse(term.isFunction());
 assertFalse(term.isVariable());
 assertFalse(term.isConstant());
 assertTrue(term.isEmpty());
 }

 @org.junit.Test
 public void testVariableConstructor() {
 JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("x")));
 assertTrue(term.isVariable());
 assertFalse(term.isConstant());
 assertFalse(term.isFunction());
 assertFalse(term.isEval());
 assertEquals("x", term.toString());
 }

 @org.junit.Test
 public void testConstantConstructor() {
 JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("3")));
 assertFalse(term.isVariable());
 assertTrue(term.isConstant());
 assertFalse(term.isFunction());
 assertFalse(term.isEval());
 assertEquals("3", term.toString());
 }

 @org.junit.Test
 public void testFunctionConstructor() {
 JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("(f x)")));
 assertTrue(term.isFunction());
 assertFalse(term.isVariable());
 assertFalse(term.isConstant());
 assertFalse(term.isEval());
 assertEquals("(f x)", term.toString());
 }

 @org.junit.Test
 public void testFunctionEvalConstructor() {
 JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("(call f x)")));
 assertTrue(term.isFunction());
 assertTrue(term.isEval());
 assertFalse(term.isVariable());
 assertFalse(term.isConstant());
 assertEquals("(call f x)", term.toString());
 }

 @org.junit.Test
 public void testParseList() {
 JSTerm term = new JSTerm();
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(a b c)"));
 term.parseList(tokenizer);
 assertEquals(3, term.size());
 assertEquals("a", term.get(0).toString());
 assertEquals("b", term.get(1).toString());
 assertEquals("c", term.get(2).toString());
 }

 @org.junit.Test
 public void testParseListWithEmptyList() {
 JSTerm term = new JSTerm();
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("()"));
 term.parseList(tokenizer);
 assertTrue(term.isEmpty());
 }

 @org.junit.Test
 public void testParseListWithSingleElement() {
 JSTerm term = new JSTerm();
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(a)"));
 term.parseList(tokenizer);
 assertEquals(1, term.size());
 assertEquals("a", term.get(0).toString());
 }

 @org.junit.Test
 public void testApplySubstitution() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("(x)")));
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(y)")));
 JSSubstitution substitution = new JSSubstitution();
 substitution.addBinding("x", term2);
 JSTerm result = term1.applySubstitution(substitution);
 assertEquals("(y)", result.toString());
 }

 @org.junit.Test
 public void testMatches() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("(x)")));
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(y)")));
 JSTerm term3 = new JSTerm(new StreamTokenizer(new StringReader("(x)")));
 JSSubstitution substitution = new JSSubstitution();
 substitution.addBinding("x", term3);
 assertTrue(term1.matches(term2, substitution).isEmpty());
 assertTrue(term1.matches(term1, substitution).isEmpty());
 }

 @org.junit.Test
 public void testMatchesFailure() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("(x)")));
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(y)")));
 JSTerm term3 = new JSTerm(new StreamTokenizer(new StringReader("(z)")));
 JSSubstitution substitution = new JSSubstitution();
 assertFalse(term1.matches(term2, substitution).isEmpty());
 assertFalse(term1.matches(term3, substitution).isEmpty());
 }

 @org.junit.Test
 public void testEquals() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("(x)")));
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(x)")));
 JSTerm term3 = new JSTerm(new StreamTokenizer(new StringReader("(y)")));
 assertTrue(term1.equals(term1));
 assertTrue(term1.equals(term2));
 assertFalse(term1.equals(term3));
 }

 @org.junit.Test
 public void testIsVariable() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("x")));
 assertTrue(term1.isVariable());
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(x)")));
 assertFalse(term2.isVariable());
 }

 @org.junit.Test
 public void testIsConstant() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("3")));
 assertTrue(term1.isConstant());
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(3)")));
 assertFalse(term2.isConstant());
 }

 @org.junit.Test
 public void testIsFunction() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("(f x)")));
 assertTrue(term1.isFunction());
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("x")));
 assertFalse(term2.isFunction());
 }

 @org.junit.Test
 public void testIsEval() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("(x)")));
 assertFalse(term1.isEval());
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(call x)")));
 assertTrue(term2.isEval());
 }

 @org.junit.Test
 public void testIsGround() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("3")));
 assertTrue(term1.isGround());
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("(x)")));
 assertFalse(term2.isGround());
 JSTerm term3 = new JSTerm(new StreamTokenizer(new StringReader("(f x)")));
 assertFalse(term3.isGround());
 JSTerm term4 = new JSTerm(new StreamTokenizer(new StringReader("(f 3)")));
 assertTrue(term4.isGround());
 }

 @org.junit.Test
 public void testStandardizerTerm() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("x")));
 JSTerm term2 = term1.standardizerTerm();
 assertEquals("x" + String.valueOf(JSJshopVars.VarCounter), term2.toString());
 JSTerm term3 = new JSTerm(new StreamTokenizer(new StringReader("(f x)")));
 JSTerm term4 = term3.standardizerTerm();
 assertEquals("(f " + String.valueOf(JSJshopVars.VarCounter) + ")", term4.toString());
 }

 @org.junit.Test
 public void testCall() {
 JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("(f x)")));
 JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("3")));
 JSTerm term3 = term1.call();
 assertEquals("(f 3)", term3.toString());
 }

}