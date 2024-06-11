import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;

public class JSTermTest {

 @Test
 public void testEmptyTerm() {
 JSTerm term = new JSTerm();
 assertTrue(term.isEmpty());
 assertEquals("()", term.toString());
 }

 @Test
 public void testConstantTerm() {
 JSTerm term = new JSTerm("true");
 assertTrue(term.isConstant());
 assertEquals("true", term.toString());
 }

 @Test
 public void testVariableTerm() {
 JSTerm term = new JSTerm("x");
 assertTrue(term.isVariable());
 assertEquals("x", term.toString());
 }

 @Test
 public void testFunctionTerm() {
 JSTerm term = new JSTerm("(+ x y)");
 assertTrue(term.isFunction());
 assertEquals("(+ x y)", term.toString());
 }

 @Test
 public void testTermWithListArgument() {
 JSTerm term = new JSTerm("(list x y z)");
 assertTrue(term.isFunction());
 assertTrue(term.elementAt(0).equals("list"));
 assertEquals(3, term.size());
 }

 @Test(expected = JSParserError.class)
 public void testMissingLeftParenthesis() {
 JSTerm term = new JSTerm("+ x y");
 fail("Expected JSParserError");
 }

 @Test(expected = JSParserError.class)
 public void testMissingRightParenthesis() {
 JSTerm term = new JSTerm("(+ x");
 fail("Expected JSParserError");
 }

 @Test(expected = JSParserError.class)
 public void testMissingTerm() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+)"));
 JSTerm term = new JSTerm(tokenizer);
 fail("Expected JSParserError");
 }

 @Test
 public void testTermCloning() {
 JSTerm term1 = new JSTerm("(+ x y)");
 JSTerm term2 = term1.cloneT();
 assertEquals(term1, term2);
 }

 @Test
 public void testTermSubstitution() {
 JSTerm term1 = new JSTerm("(+ x y)");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addBinding("x", new JSTerm("5"));
 JSTerm term2 = term1.applySubstitutionT(substitution);
 assertEquals("(+ 5 y)", term2.toString());
 }

 @Test
 public void testTermMatching() {
 JSTerm term1 = new JSTerm("(+ x y)");
 JSTerm term2 = new JSTerm("(+ z x y)");
 JSSubstitution substitution = term1.matches(term2);
 assertFalse(substitution.isSuccess());
 }

 @Test
 public void testTermMatchingWithSuccess() {
 JSTerm term1 = new JSTerm("(+ x y)");
 JSTerm term2 = new JSTerm("(+ a x b)");
 JSSubstitution substitution = term1.matches(term2);
 assertTrue(substitution.isSuccess());
 assertEquals(new JSPair("x", new JSTerm("a")), substitution.getFirstBinding());
 assertEquals(new JSPair("y", new JSTerm("b")), substitution.getSecondBinding());
 }

 @Test
 public void testTermStandardization() {
 JSTerm term1 = new JSTerm("(+ x y)");
 JSTerm term2 = term1.standardizerTerm();
 assertEquals("(+ X1 Y1)", term2.toString());
 }

 @Test
 public void testTermEvaluation() {
 JSTerm term1 = new JSTerm("(+ 1 2)");
 JSTerm term2 = new JSTerm("(+ 3 4)");
 JSTerm result = JSEvaluate.evaluateBinaryOperator("+", term1, term2);
 assertEquals(new JSTerm("7"), result);
 }
}