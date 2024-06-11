package umd.cs.shop.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSPairVarTerm;
import umd.cs.shop.JSParserError;

public class JSTermTest {

 private StreamTokenizer tokenizer;
 private JSTerm term;

 @Before
 public void setUp() {
 tokenizer = new StreamTokenizer(new StringReader("(+ a 3)"));
 }

 @Test
 public void testConstructors() {
 // Test default constructor
 JSTerm emptyTerm = new JSTerm();
 assertEquals("", emptyTerm.toString());

 // Test constructor with StreamTokenizer
 term = new JSTerm(tokenizer);
 assertEquals("+ a 3", term.toString());
 }

 @Test
 public void testIsVariable() {
 term = new JSTerm("x");
 assertTrue(term.isVariable());
 term = new JSTerm("(+ x 1)");
 assertFalse(term.isVariable());
 }

 @Test
 public void testIsConstant() {
 term = new JSTerm("3");
 assertTrue(term.isConstant());
 term = new JSTerm("x");
 assertFalse(term.isConstant());
 }

 @Test
 public void testIsFunction() {
 term = new JSTerm("+");
 assertTrue(term.isFunction());
 term = new JSTerm("x");
 assertFalse(term.isFunction());
 }

 @Test
 public void testIsEval() {
 term = new JSTerm("(+ x 1)");
 assertFalse(term.isEval());
 term = new JSTerm("(+ x 1)");
 term.makeEval(true);
 assertTrue(term.isEval());
 }

 @Test
 public void testPrint() {
 term = new JSTerm("(+ a 3)");
 assertEquals("(+ a 3)", term.print().toString());
 term.makeEval(true);
 assertEquals("call + a 3", term.print().toString());
 }

 @Test
 public void testParseList() {
 term = new JSTerm();
 term.addElement("nil");
 term.makeConstant();
 term.makeFunction();
 term.addElement(".");
 term.addElement(new JSTerm("x"));
 term.addElement(term.parseList(tokenizer));
 tokenizer.reset();
 tokenizer.nextToken(); // '('
 assertFalse(term.equals(new JSTerm(tokenizer).applySubstitutionT(new JSSubstitution())));
 }

 @Test
 public void testMatches() {
 term = new JSTerm("(+ a b)");
 JSTerm term1 = new JSTerm("(+ a c)");
 JSTerm term2 = new JSTerm("(+ a b)");
 JSTerm term3 = new JSTerm("(+ b a)");
 JSSubstitution substitution = term.matches(term2);
 assertEquals(2, substitution.size());
 assertEquals(new JSPairVarTerm("b", "a"), substitution.elementAt(0));
 substitution = term.matches(term3);
 assertFalse(substitution.fail());
 }

 @Test(expected = JSParserError.class)
 public void testParserError() {
 tokenizer.reset();
 tokenizer.nextToken(); // ')'
 new JSTerm(tokenizer);
 }
}