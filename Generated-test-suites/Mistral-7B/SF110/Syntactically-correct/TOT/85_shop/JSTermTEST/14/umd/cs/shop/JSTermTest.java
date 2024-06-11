package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSParserError;
import umd.cs.shop.JSUtil;

public class JSTermTest {

 @Test
 public void testEmptyTerm() {
 JSTerm term = new JSTerm();
 assertEquals("", term.toStr().toString());
 }

 @Test
 public void testConstantTerm() {
 JSTerm term = new JSTerm("3.0");
 assertEquals("3.0 ", term.toStr().toString());
 }

 @Test
 public void testVariableTerm() {
 JSTerm term = new JSTerm("x");
 assertEquals("x ", term.toStr().toString());
 }

 @Test
 public void testListTerm() {
 JSTerm term = new JSTerm("(+ (+ 1 2) 3)");
 assertEquals("(+(+(1 2) 3))", term.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testInvalidTokenError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("blah"));
 new JSTerm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testMissingTermError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+)"));
 new JSTerm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testMissingLeftParenthesisError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("call x"));
 new JSTerm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testMissingRightParenthesisError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 1)"));
 new JSTerm(tokenizer);
 }

 @Test
 public void testApplySubstitution() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPairTerm("x", term2));
 JSTerm result = term1.applySubstitution(substitution);
 assertEquals("y", result.elementAt(0).toString());
 }

 @Test
 public void testMatches() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSTerm term3 = new JSTerm("x");
 JSTerm term4 = new JSTerm("z");
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPairTerm("x", term3));
 JSTerm result1 = term1.matches(term2);
 assertNull(result1);
 JSTerm result2 = term1.matches(term3);
 assertEquals(substitution, result2.getSubstitution());
 JSTerm result3 = term1.matches(term4);
 assertNull(result3);
 }

 @Test
 public void testIsGround() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("(+ 1 1)");
 assertTrue(term2.isGround());
 assertFalse(term1.isGround());
 }

 @Test
 public void testStandardizerTerm() {
 JSTerm term1 = new JSTerm("(+ x 2) ");
 JSTerm term2 = term1.standardizerTerm();
 assertEquals("(+(?x (+(?x 2))))", term2.toStr().toString());
 }

 @Test
 public void testCallTerm() {
 JSTerm term1 = new JSTerm("(+ x 2)");
 JSTerm result = term1.call();
 assertEquals("3", result.elementAt(0).toString());
 }
}