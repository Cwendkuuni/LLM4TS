import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;

public class JSTermTest {

 @Test
 public void testEmptyTermConstruction() {
 JSTerm term = new JSTerm();
 assertTrue(term.isEmpty());
 }

 @Test
 public void testVariableTermConstruction() {
 JSTerm term = new JSTerm("x");
 assertTrue(term.isVariable());
 assertEquals("x", term.toString());
 }

 @Test
 public void testConstantTermConstruction() {
 JSTerm term = new JSTerm("3");
 assertTrue(term.isConstant());
 assertEquals("3", term.toString());
 }

 @Test
 public void testFunctionTermConstruction() {
 JSTerm term = new JSTerm("(+ 2 3)");
 assertTrue(term.isFunction());
 assertEquals("(+ 2 3)", term.toString());
 }

 @Test
 public void testTermParseList() {
 JSTerm term = new JSTerm();
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x 3)"));
 term.parseList(tokenizer);
 assertEquals(3, term.size());
 assertEquals("x", term.elementAt(1).toString());
 assertEquals("3", term.elementAt(2).toString());
 }

 @Test(expected = JSParserError.class)
 public void testTermParseInvalid() {
 JSTerm term = new JSTerm();
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x 3 4)"));
 term.parseList(tokenizer);
 fail("Expected JSParserError to be thrown");
 }

 @Test
 public void testTermPrint() {
 JSTerm term = new JSTerm("(+ x 3)");
 term.print();
 assertEquals("(+ x 3 )", JSUtil.getOutputString());
 }

 @Test
 public void testTermClone() {
 JSTerm term = new JSTerm("(+ x 3)");
 JSTerm clonedTerm = term.cloneT();
 term.elementAt(1).makeConstant();
 assertEquals("(+ x 3)", term.toString());
 assertEquals("(+ x 3)", clonedTerm.toString());
 }

 @Test
 public void testTermApplySubstitution() {
 JSTerm term = new JSTerm("[x](y)");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPair("x", new JSTerm("z")));
 JSTerm result = term.applySubstitutionT(substitution);
 assertEquals("[z](y)", result.toString());
 }

 @Test
 public void testTermMatches() {
 JSTerm term1 = new JSTerm("(+ x 3)");
 JSTerm term2 = new JSTerm("(+ y 3)");
 JSSubstitution substitution = term1.matches(term2);
 assertFalse(substitution.isEmpty());
 assertEquals(1, substitution.size());
 assertEquals(new JSPair("x", new JSTerm("y")), substitution.elementAt(0));
 }

 @Test
 public void testTermEquals() {
 JSTerm term1 = new JSTerm("(+ x 3)");
 JSTerm term2 = new JSTerm("(+ x 3)");
 JSTerm term3 = new JSTerm("(+ y 3)");
 assertTrue(term1.equals(term2));
 assertFalse(term1.equals(term3));
 }

 @Test
 public void testTermIsGround() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("3");
 JSTerm term3 = new JSTerm("(+ x 3)");
 assertTrue(term2.isGround());
 assertFalse(term1.isGround());
 assertFalse(term3.isGround());
 }

 @Test
 public void testTermStandardizerTerm() {
 JSTerm term = new JSTerm("(+ x 3)");
 JSTerm standardizedTerm = term.standardizerTerm();
 assertEquals("[x](+ 3)", standardizedTerm.toString());
 }

 @Test
 public void testTermCall() {
 JSTerm term = new JSTerm("(+ x 3)");
 JSTerm result = term.call();
 assertEquals("3", result.toString());
 }

 @Test(expected = JSParserError.class)
 public void testTermCallInvalid() {
 JSTerm term = new JSTerm("(+ x)");
 term.makeEval(true);
 term.call();
 fail("Expected JSParserError to be thrown");
 }

}