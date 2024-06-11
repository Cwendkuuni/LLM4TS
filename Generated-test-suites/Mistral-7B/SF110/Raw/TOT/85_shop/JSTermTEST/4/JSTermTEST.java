import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSParserError;
import umd.cs.shop.JSSubstitution;

public class JSTermTest {

 @Test
 public void testEmptyTerm() {
 JSTerm term = new JSTerm();
 assertEquals("()", term.toStr().toString());
 }

 @Test
 public void testConstantTerm() {
 JSTerm term = new JSTerm("x");
 term.makeConstant();
 term.addElement("x");
 assertEquals("x", term.toStr().toString());
 }

 @Test
 public void testVariableTerm() {
 JSTerm term = new JSTerm();
 term.makeVariable();
 term.addElement("x");
 assertEquals("x", term.toStr().toString());
 }

 @Test
 public void testFunctionTerm() {
 JSTerm term = new JSTerm();
 term.makeFunction();
 term.addElement("+");
 term.addElement(new JSTerm());
 term.addElement(new JSTerm());
 assertEquals("(+ nil nil)", term.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testMissingTerm() throws Exception {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+)"));
 JSTerm term = new JSTerm(tokenizer);
 fail("Expected JSParserError");
 }

 @Test(expected = JSParserError.class)
 public void testUnexpectedEndOfInput() throws Exception {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x"));
 JSTerm term = new JSTerm(tokenizer);
 fail("Expected JSParserError");
 }

 @Test(expected = JSParserError.class)
 public void testVariableMissingName() throws Exception {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ %)"));
 JSTerm term = new JSTerm(tokenizer);
 fail("Expected JSParserError");
 }

 @Test
 public void testParseList() {
 JSTerm term = new JSTerm();
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 1 2 3 4)"));
 term.addElement(new JSTerm(tokenizer));
 tokenizer.nextToken(); // ')'
 tokenizer.nextToken(); // ';'
 term.addElement(term.parseList(tokenizer));
 assertEquals("(+ 1 2 3 4 nil)", term.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testParseListMismatchedParentheses() throws Exception {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 1 2 3)"));
 JSTerm term = new JSTerm();
 term.addElement(new JSTerm(tokenizer));
 term.parseList(tokenizer);
 fail("Expected JSParserError");
 }

 @Test(expected = JSParserError.class)
 public void testParseListUnexpectedToken() throws Exception {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ 1 2 %)"));
 JSTerm term = new JSTerm();
 term.addElement(new JSTerm(tokenizer));
 term.parseList(tokenizer);
 fail("Expected JSParserError");
 }

 @Test
 public void testMatches() {
 JSTerm term1 = new JSTerm();
 term1.addElement("x");
 term1.makeVariable();
 JSTerm term2 = new JSTerm();
 term2.addElement("x");
 term2.makeVariable();
 JSSubstitution substitution = term1.matches(term2);
 assertTrue(substitution.isEmpty());

 term1 = new JSTerm();
 term1.addElement("x");
 term1.makeVariable();
 term2 = new JSTerm();
 term2.addElement("y");
 term2.makeVariable();
 substitution = term1.matches(term2);
 assertFalse(substitution.isEmpty());
 assertEquals(1, substitution.size());
 assertEquals(new JSPairVarTerm("x", "y"), substitution.get(0));

 term1 = new JSTerm();
 term1.addElement("x");
 term1.makeVariable();
 term2 = new JSTerm();
 term2.addElement("x");
 term2.makeConstant();
 term2.addElement("10");
 substitution = term1.matches(term2);
 assertFalse(substitution.isEmpty());
 assertEquals(1, substitution.size());
 assertEquals(new JSPairVarTerm("x", "10"), substitution.get(0));

 term1 = new JSTerm();
 term1.addElement("+");
 term1.makeFunction();
 term1.addElement("x");
 term1.addElement("y");
 term2 = new JSTerm();
 term2.addElement("+");
 term2.makeFunction();
 term2.addElement("z");
 term2.addElement("w");
 substitution = term1.matches(term2);
 assertFalse(substitution.isEmpty());
 assertEquals(2, substitution.size());
 assertEquals(new JSPairVarTerm("x", "z"), substitution.get(0));
 assertEquals(new JSPairVarTerm("y", "w"), substitution.get(1));
 }

 @Test(expected = JSParserError.class)
 public void testMatchesInvalidTerm1() throws Exception {
 JSTerm term1 = new JSTerm();
 term1.addElement("x");
 term1.makeVariable();
 JSTerm term2 = new JSTerm();
 term2.addElement("+");
 term2.makeFunction();
 term2.addElement("x");
 term2.addElement("y");
 JSSubstitution substitution = term1.matches(term2);
 fail("Expected JSParserError");
 }

 @Test(expected = JSParserError.class)
 public void testMatchesInvalidTerm2() throws Exception {
 JSTerm term1 = new JSTerm();
 term1.addElement("+");
 term1.makeFunction();
 term1.addElement("x");
 term1.addElement("y");
 JSTerm term2 = new JSTerm();
 term2.addElement("x");
 term2.makeVariable();
 JSSubstitution substitution = term1.matches(term2);
 fail("Expected JSParserError");
 }

 @Test
 public void testCall() {
 JSTerm term = new JSTerm();
 term.addElement("+");
 term.makeFunction();
 term.addElement("1");
 term.addElement("2");
 assertEquals("3", term.call().toStr().toString());
 }

 @Test
 public void testCallWithUnaryOperator() {
 JSTerm term = new JSTerm();
 term.addElement("not");
 term.makeFunction();
 term.addElement(new JSTerm());
 term.addElement(new JSTerm());
 term.addElement("true");
 assertEquals("false", term.call().toStr().toString());
 }

 @Test
 public void testCallWithBinaryOperator() {
 JSTerm term = new JSTerm();
 term.addElement("*");
 term.makeFunction();
 term.addElement("1");
 term.addElement("2");
 assertEquals("2", term.call().toStr().toString());
 }

 @Test
 public void testCallWithList() {
 JSTerm term = new JSTerm();
 term.addElement("+");
 term.makeFunction();
 term.addElement("1");
 term.addElement(new JSTerm());
 term.addElement(new JSTerm());
 term.addElement("3");
 term.addElement("2");
 assertEquals("4", term.call().toStr().toString());
 }

 @Test
 public void testCallWithNestedList() {
 JSTerm term = new JSTerm();
 term.addElement("+");
 term.makeFunction();
 term.addElement("1");
 term.addElement(new JSTerm());
 term.addElement(new JSTerm());
 term.addElement("*");
 term.addElement("2");
 term.addElement("3");
 term.addElement("4");
 assertEquals("7", term.call().toStr().toString());
 }
}