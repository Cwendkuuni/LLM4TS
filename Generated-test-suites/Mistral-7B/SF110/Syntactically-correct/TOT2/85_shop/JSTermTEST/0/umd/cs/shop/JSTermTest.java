import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;

public class JSTermTest {

 private StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(+ x 5)"));

 @Test
 public void testConstructorWithEmptyInput() {
 JSTerm jsterm = new JSTerm();
 assertTrue(jsterm.isEmpty());
 }

 @Test
 public void testConstructorWithStreamTokenizerInput() {
 JSTerm jsterm = new JSTerm(tokenizer);
 assertEquals("+(x 5)", jsterm.toString());
 }

 @Test
 public void testParseListWithEmptyInput() {
 JSTerm jsterm = new JSTerm();
 assertEquals("nil", jsterm.parseList(tokenizer).toString());
 }

 @Test
 public void testParseListWithSingleElement() {
 JSTerm jsterm = new JSTerm();
 tokenizer.reset();
 tokenizer.parseNumbers();
 tokenizer.nextToken();
 assertEquals("5", jsterm.parseList(tokenizer).elementAt(0).toString());
 }

 @Test
 public void testParseListWithMultipleElements() {
 JSTerm jsterm = new JSTerm();
 tokenizer.reset();
 tokenizer.parseNumbers();
 tokenizer.nextToken();
 tokenizer.nextToken();
 assertEquals("5", jsterm.parseList(tokenizer).elementAt(0).toString());
 assertEquals("3", jsterm.parseList(tokenizer).elementAt(1).toString());
 }

 @Test(expected = JSParserError.class)
 public void testParseListWithInvalidInput() {
 JSTerm jsterm = new JSTerm();
 tokenizer.reset();
 tokenizer.parseNumbers();
 tokenizer.nextToken();
 tokenizer.nextToken();
 tokenizer.nextToken();
 jsterm.parseList(tokenizer);
 fail("Should throw JSParserError");
 }

 @Test
 public void testPrintList() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement(new JSTerm("x"));
 jsterm.addElement(new JSTerm("y"));
 jsterm.addElement(new JSTerm("z"));
 assertEquals("(xyz)", jsterm.printList(jsterm).toString());
 }

 @Test
 public void testAddElementWithConstant() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("5");
 assertEquals("5", jsterm.toString());
 }

 @Test
 public void testAddElementWithVariable() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("x");
 assertEquals("x", jsterm.toString());
 }

 @Test
 public void testAddElementWithFunction() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("+(x y)");
 assertEquals("+(x y)", jsterm.toString());
 }

 @Test(expected = JSParserError.class)
 public void testAddElementWithInvalidInput() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("not x");
 fail("Should throw JSParserError");
 }

 @Test
 public void testIsVariable() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("x");
 assertTrue(jsterm.isVariable());
 }

 @Test
 public void testIsConstant() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("5");
 assertTrue(jsterm.isConstant());
 }

 @Test
 public void testIsFunction() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("+(x y)");
 assertTrue(jsterm.isFunction());
 }

 @Test
 public void testIsEval() {
 JSTerm jsterm = new JSTerm();
 jsterm.makeEval(true);
 assertTrue(jsterm.isEval());
 }

 @Test
 public void testMakeFunction() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("+(x y)");
 jsterm.makeFunction();
 assertEquals("(function (x y))", jsterm.toString());
 }

 @Test
 public void testMakeVariable() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("x");
 jsterm.makeVariable();
 assertEquals("(variable x)", jsterm.toString());
 }

 @Test
 public void testMakeConstant() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("5");
 jsterm.makeConstant();
 assertEquals("(constant 5)", jsterm.toString());
 }

 @Test
 public void testCloneT() {
 JSTerm jsterm1 = new JSTerm();
 jsterm1.addElement("+(x y)");
 JSTerm jsterm2 = jsterm1.cloneT();
 assertEquals(jsterm1.toString(), jsterm2.toString());
 }

 @Test
 public void testApplySubstitutionT() {
 JSTerm jsterm1 = new JSTerm();
 jsterm1.addElement("x");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement(new JSPair("x", new JSTerm("y")));
 JSTerm jsterm2 = jsterm1.applySubstitutionT(alpha);
 assertEquals("y", jsterm2.toString());
 }

 @Test
 public void testMatches() {
 JSTerm jsterm1 = new JSTerm();
 jsterm1.addElement("x");
 JSTerm jsterm2 = new JSTerm();
 jsterm2.addElement("y");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement(new JSPair("x", jsterm2));
 JSSubstitution beta = jsterm1.matches(jsterm2, alpha);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testMatchesWithFailure() {
 JSTerm jsterm1 = new JSTerm();
 jsterm1.addElement("x");
 JSTerm jsterm2 = new JSTerm();
 JSTerm jsterm3 = new JSTerm();
 jsterm3.addElement("y");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement(new JSPair("x", jsterm3));
 JSSubstitution beta = jsterm1.matches(jsterm2, alpha);
 assertFalse(beta.isEmpty());
 }

 @Test
 public void testEquals() {
 JSTerm jsterm1 = new JSTerm();
 jsterm1.addElement("x");
 JSTerm jsterm2 = new JSTerm();
 jsterm2.addElement("x");
 assertTrue(jsterm1.equals(jsterm2));
 }

 @Test
 public void testNotEquals() {
 JSTerm jsterm1 = new JSTerm();
 JSTerm jsterm2 = new JSTerm();
 jsterm2.makeConstant();
 jsterm2.addElement("5");
 assertFalse(jsterm1.equals(jsterm2));
 }

 @Test
 public void testIsGround() {
 JSTerm jsterm1 = new JSTerm();
 jsterm1.addElement("x");
 assertFalse(jsterm1.isGround());
 }

 @Test
 public void testIsGroundWithConstant() {
 JSTerm jsterm1 = new JSTerm();
 jsterm1.makeConstant();
 jsterm1.addElement("5");
 assertTrue(jsterm1.isGround());
 }

 @Test
 public void testIsGroundWithFunction() {
 JSTerm jsterm1 = new JSTerm();
 jsterm1.addElement("+(x y)");
 assertFalse(jsterm1.isGround());
 }

 @Test
 public void testStandardizerTerm() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("+(x y)");
 JSTerm standardizedJsterm = jsterm.standardizerTerm();
 assertEquals("(function (x y))", standardizedJsterm.toString());
 }

 @Test
 public void testCallWithConstant() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("5");
 JSTerm result = jsterm.call();
 assertEquals("5", result.toString());
 }

 @Test
 public void testCallWithVariable() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("x");
 JSTerm result = jsterm.call();
 assertEquals("x", result.toString());
 }

 @Test
 public void testCallWithFunction() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("+(x y)");
 JSTerm operant1 = new JSTerm();
 operant1.addElement("2");
 JSTerm operant2 = new JSTerm();
 operant2.addElement("3");
 JSTerm result = jsterm.call();
 assertEquals("(+ 2 3)", result.toString());
 }

 @Test
 public void testCallWithMultipleOperands() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("+(x y z)");
 JSTerm operant1 = new JSTerm();
 operant1.addElement("2");
 JSTerm operant2 = new JSTerm();
 operant2.addElement("3");
 JSTerm operant3 = new JSTerm();
 operant3.addElement("4");
 JSTerm result = jsterm.call();
 assertEquals("(+(2 3) 4)", result.toString());
 }

 @Test(expected = JSParserError.class)
 public void testCallWithInvalidInput() {
 JSTerm jsterm = new JSTerm();
 jsterm.addElement("not x");
 JSTerm result = jsterm.call();
 fail("Should throw JSParserError");
 }
}