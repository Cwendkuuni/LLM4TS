import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;

public class JSTermTest {

 @Test
 public void testEmptyTerm() {
 JSTerm term = new JSTerm();
 assertTrue(term.isConstant());
 assertEquals("nil", term.toStr().toString());
 }

 @Test
 public void testVariableTerm() {
 JSTerm term = new JSTerm("x");
 assertTrue(term.isVariable());
 assertEquals("x", term.toStr().toString());
 }

 @Test
 public void testConstantTerm() {
 JSTerm term = new JSTerm("true");
 assertTrue(term.isConstant());
 assertEquals("true", term.toStr().toString());
 }

 @Test
 public void testFunctionTerm() {
 JSTerm term = new JSTerm("+");
 assertTrue(term.isFunction());
 assertEquals("+", term.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testInvalidTokenError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("x %"));
 JSTerm term = new JSTerm(tokenizer);
 fail("Parsing error expected but not thrown.");
 }

 @Test(expected = JSParserError.class)
 public void testMissingTermError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("( + )"));
 JSTerm term = new JSTerm(tokenizer);
 fail("Parsing error expected but not thrown.");
 }

 @Test
 public void testListTerm() {
 JSTerm term = new JSTerm();
 term.addElement(new JSTerm("x"));
 term.addElement(new JSTerm("y"));
 term.addElement(new JSTerm("z"));
 assertTrue(term.isFunction());
 assertEquals("( x y z )", term.toStr().toString());
 }

 @Test
 public void testApplySubstitution() {
 JSTerm alpha = new JSTerm("x");
 JSTerm beta = new JSTerm("y");
 JSTerm gamma = new JSTerm("z");
 JSTerm term = new JSTerm("( x y z )");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPairTerm(alpha, beta));
 substitution.addElement(new JSPairTerm(beta, gamma));
 JSTerm result = term.applySubstitution(substitution);
 assertTrue(result.equals(new JSTerm("( z z z )")));
 }

 @Test
 public void testMatches() {
 JSTerm alpha = new JSTerm("x");
 JSTerm beta = new JSTerm("y");
 JSTerm term = new JSTerm("( x y z )");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPairTerm(alpha, beta));
 assertTrue(term.matches(new JSTerm("( x y z )"), substitution).size() > 0);
 substitution.addElement(new JSPairTerm(beta, term));
 assertTrue(term.matches(new JSTerm("( y ( x y z ) )"), substitution).size() > 0);
 }

 @Test
 public void testEquals() {
 JSTerm alpha = new JSTerm("x");
 JSTerm beta = new JSTerm("x");
 JSTerm gamma = new JSTerm("y");
 JSTerm delta = new JSTerm("y");
 assertTrue(alpha.equals(beta));
 assertFalse(alpha.equals(gamma));
 assertFalse(alpha.equals(delta));
 }

 @Test
 public void testGroundTerm() {
 JSTerm term = new JSTerm("x + y * z");
 assertFalse(term.isGround());
 JSTerm term2 = new JSTerm("x + y * z").standardizerTerm();
 assertTrue(term2.isGround());
 }

 @Test
 public void testCallTerm() {
 JSTerm term = new JSTerm("not ( x < y)");
 assertFalse(term.call().isGround());
 JSTerm term2 = new JSTerm("not ( x < y )").standardizerTerm();
 assertTrue(term2.call().isGround());
 }

}