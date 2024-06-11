import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSUtil;

public class JSTermTest {

 @Test
 public void testEmptyTermConstructor() {
 JSTerm term = new JSTerm();
 assertEquals("nil", term.toString());
 }

 @Test
 public void testTermWithConstantConstructor() {
 JSTerm term = new JSTerm("42");
 assertEquals("42", term.toString());
 }

 @Test
 public void testTermWithVariableConstructor() {
 JSTerm term = new JSTerm("x");
 assertEquals("x", term.toString());
 }

 @Test
 public void testTermWithFunctionConstructor() {
 JSTerm term = new JSTerm("(+ x 5)");
 assertEquals("(+ x 5)", term.toString());
 }

 @Test
 public void testTermWithListConstructor() {
 JSTerm term = new JSTerm("(+ 3 (/ 4 2))");
 assertEquals("(+ 3 (/ 4 2))", term.toString());
 }

 @Test(expected = JSParserError.class)
 public void testTermWithInvalidTokenizerInput() {
 JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("InvalidInput")));
 fail("Should throw JSParserError");
 }

 @Test
 public void testParseList() {
 JSTerm term = new JSTerm();
 term.parseList(new StreamTokenizer(new StringReader("(1 2 3)")));
 JSTerm expected = new JSTerm();
 expected.addElement("1");
 expected.addElement("2");
 expected.addElement("3");
 assertEquals(expected, term);
 }

 @Test
 public void testApplySubstitution() {
 JSTerm term = new JSTerm("x");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addBinding("x", new JSTerm("3"));
 JSTerm result = term.applySubstitution(substitution);
 assertEquals("3", result.toString());
 }

 @Test
 public void testMatches() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSTerm term3 = new JSTerm("x");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addBinding("x", term3);
 JSTerm result1 = term1.matches(term2);
 assertNull(result1);
 JSTerm result2 = term1.matches(term3, substitution);
 assertEquals(substitution, result2);
 }

 @Test
 public void testEquals() {
 JSTerm term1 = new JSTerm("(+ x 5)");
 JSTerm term2 = new JSTerm("(+ y 5)");
 JSTerm term3 = new JSTerm("(+ x 5)");
 assertTrue(term1.equals(term3));
 assertFalse(term1.equals(term2));
 }

 @Test
 public void testIsVariable() {
 JSTerm term = new JSTerm("x");
 assertTrue(term.isVariable());
 }

 @Test
 public void testIsConstant() {
 JSTerm term = new JSTerm("42");
 assertTrue(term.isConstant());
 }

 @Test
 public void testIsFunction() {
 JSTerm term = new JSTerm("(+ x y)");
 assertTrue(term.isFunction());
 }

 @Test
 public void testIsEval() {
 JSTerm term = new JSTerm("(+ x (eval y))");
 assertTrue(term.isEval());
 }

 @Test
 public void testMakeFunction() {
 JSTerm term = new JSTerm("x");
 term.makeFunction();
 assertFalse(term.isVariable());
 assertFalse(term.isConstant());
 assertTrue(term.isFunction());
 }

 @Test
 public void testMakeVariable() {
 JSTerm term = new JSTerm("x");
 term.makeVariable();
 assertTrue(term.isVariable());
 assertFalse(term.isConstant());
 assertFalse(term.isFunction());
 }

 @Test
 public void testMakeConstant() {
 JSTerm term = new JSTerm("42");
 term.makeConstant();
 assertTrue(term.isVariable());
 assertFalse(term.isConstant());
 assertFalse(term.isFunction());
 }

 @Test
 public void testMakeEval() {
 JSTerm term = new JSTerm("(+ x (eval y))");
 term.makeEval(true);
 assertFalse(term.isVariable());
 assertFalse(term.isConstant());
 assertFalse(term.isFunction());
 assertTrue(term.isEval());
 }

 @Test
 public void testToString() {
 JSTerm term = new JSTerm("(+ x (eval y))");
 assertEquals("(+ x (eval y))", term.toString());
 }

 @Test
 public void testIsGround() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("(+ x 5)");
 JSTerm term3 = new JSTerm("(+ x 5 3)");
 JSTerm term4 = new JSTerm("(+ 3 5 7)");
 assertTrue(term1.isGround());
 assertFalse(term2.isGround());
 assertFalse(term3.isGround());
 assertTrue(term4.isGround());
 }

 @Test
 public void testStandardizerTerm() {
 JSTerm term = new JSTerm("(+ x (eval y))");
 JSTerm expected = new JSTerm("(+ x Z3 Z4)");
 assertEquals(expected, term.standardizerTerm());
 }

 @Test
 public void testCall() {
 JSTerm term = new JSTerm("(+ x 5)");
 JSTerm result = term.call();
 assertEquals("6", result.toString());
 }

 @Test(expected = RuntimeException.class)
 public void testCallWithInvalidOperator() {
 JSTerm term = new JSTerm("(not x)");
 term.call();
 fail("Should throw RuntimeException");
 }

 @Test
 public void testCallWithUnaryOperator() {
 JSTerm term = new JSTerm("(not x)");
 JSTerm result = term.call();
 assertEquals("false", result.toString());
 }

 @Test
 public void testCallWithBinaryOperator() {
 JSTerm term = new JSTerm("(+ x y)");
 JSTerm term1 = new JSTerm("3");
 JSTerm term2 = new JSTerm("4");
 term.addElement(term1);
 term.addElement(term2);
 JSTerm result = term.call();
 assertEquals("7", result.toString());
 }

 @Test
 public void testCallWithListOperator() {
 JSTerm term = new JSTerm("(append x y)");
 JSTerm term1 = new JSTerm("(1 2 3)");
 JSTerm term2 = new JSTerm("(4 5 6)");
 term.addElement(term1);
 term.addElement(term2);
 JSTerm result = term.call();
 assertEquals("[1, 2, 3, 4, 5, 6]", result.toString());
 }
}