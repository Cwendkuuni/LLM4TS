import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSTermTest {

 private StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x 5)"));

 @Test
 public void testJSTermConstructionFromSimpleTerm() {
 JSTerm term = new JSTerm(tokenizer);
 assertEquals("+", term.elementAt(0).toString());
 assertEquals("x", term.elementAt(1).toString());
 assertEquals("5", term.elementAt(2).toString());
 }

 @Test
 public void testJSTermConstructionFromVariable() {
 tokenizer.reset();
 tokenizer.nextToken(); // Expecting Term
 tokenizer.nextToken(); // x
 JSTerm term = new JSTerm(tokenizer);
 assertEquals(".", term.elementAt(0).toString());
 assertEquals("x", term.elementAt(1).toString());
 }

 @Test
 public void testJSTermConstructionFromFunctionCall() {
 tokenizer.reset();
 tokenizer.nextToken(); // (
 tokenizer.nextToken(); // +
 tokenizer.nextToken(); // x
 tokenizer.nextToken(); // 5
 tokenizer.nextToken(); // )
 JSTerm term = new JSTerm(tokenizer);
 assertEquals("call", term.elementAt(0).toString());
 assertEquals("+", term.elementAt(1).toString());
 assertFalse(term.elementAt(2).isConstant()); // Check that the operands are JSTerm objects
 }

 @Test(expected = JSParserError.class)
 public void testJSTermConstructionWithInvalidToken() {
 tokenizer.reset();
 tokenizer.nextToken(); // %%%
 JSTerm term = new JSTerm(tokenizer);
 fail("Should throw JSParserError exception");
 }

 @Test
 public void testJSTermClone() {
 JSTerm term = new JSTerm(tokenizer);
 JSTerm clonedTerm = term.cloneT();
 assertEquals(term, clonedTerm);
 }

 @Test
 public void testJSTermApplySubstitution() {
 JSTerm term = new JSTerm(tokenizer); // (x + 5)
 JSSubstitution alpha = new JSSubstitution();
 JSTerm result = term.applySubstitutionT(alpha);
 JSTerm expectedResult = new JSTerm(tokenizer); // (y + 5)
 assertEquals(expectedResult, result);
 }

 @Test
 public void testJSTermMatches() {
 JSTerm term1 = new JSTerm(tokenizer); // (x + 5)
 JSTerm term2 = new JSTerm(tokenizer); // (y + 5)
 JSSubstitution substitution = term1.matches(term2);
 assertEquals(term2, substitution.getInstance(term1));
 }

 @Test
 public void testJSTermIsGround() {
 JSTerm term = new JSTerm(tokenizer); // (x + 5)
 assertFalse(term.isGround());

 JSTerm groundTerm = new JSTerm(tokenizer); // (3 + 5)
 assertTrue(groundTerm.isGround());
 }

 @Test
 public void testJSTermToString() {
 JSTerm term = new JSTerm(tokenizer); // (+ x 5)
 assertEquals("(+ x 5)", term.toStr().toString());
 }
}