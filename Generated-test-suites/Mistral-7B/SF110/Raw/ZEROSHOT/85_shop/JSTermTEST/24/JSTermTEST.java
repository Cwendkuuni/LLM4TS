import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;

public class JSTermTest {

 @Test
 public void testEmptyConstructor() {
 JSTerm jsterm = new JSTerm();
 assertTrue(jsterm.isEmpty());
 }

 @Test
 public void testVariableConstructor() {
 JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(x)")));
 assertEquals("x", jsterm.elementAt(0));
 assertTrue(jsterm.isVariable());
 }

 @Test
 public void testConstantConstructor() {
 JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("3")));
 assertEquals("3", jsterm.elementAt(0));
 assertTrue(jsterm.isConstant());
 }

 @Test
 public void testFunctionConstructor() {
 JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
 assertEquals("+(1,2)", jsterm.toStr().toString());
 assertTrue(jsterm.isFunction());
 }

 @Test
 public void testParseList() {
 JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 (+ 2 3))")));
 assertEquals("+(1,(+(2,3)))", jsterm.toStr().toString());
 }

 @Test
 public void testApplySubstitution() {
 JSTerm jsterm1 = new JSTerm(new StreamTokenizer(new StringReader("(x)")));
 JSTerm jsterm2 = new JSTerm(new StreamTokenizer(new StringReader("(y)")));
 JSSubstitution substitution = new JSSubstitution();
 substitution.addElement(new JSPairTerm("x", "y"));
 JSTerm result = jsterm1.applySubstitution(substitution);
 assertEquals("y", result.elementAt(0).toString());
 }

 @Test
 public void testEquals() {
 JSTerm jsterm1 = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
 JSTerm jsterm2 = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
 JSTerm jsterm3 = new JSTerm(new StreamTokenizer(new StringReader("(- 1 2)")));
 assertTrue(jsterm1.equals(jsterm2));
 assertFalse(jsterm1.equals(jsterm3));
 }

 @Test
 public void testIsVariable() {
 JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("x")));
 assertTrue(jsterm.isVariable());
 }

 @Test
 public void testIsConstant() {
 JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("3")));
 assertTrue(jsterm.isConstant());
 }

 @Test
 public void testIsFunction() {
 JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
 assertTrue(jsterm.isFunction());
 }

 @Test
 public void testIsEval() {
 JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
 assertFalse(jsterm.isEval());
 JSTerm evalJsterm = new JSTerm(new StreamTokenizer(new StringReader("(eval (+ 1 2) 3)")));
 assertTrue(evalJsterm.isEval());
 }

 @Test
 public void testMakeFunction() {
 JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("x")));
 jsterm.makeFunction();
 assertEquals("(function x)", jsterm.toStr().toString());
 }

 @Test
 public void testMakeVariable() {
 JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("x")));
 jsterm.makeVariable();
 assertEquals("x", jsterm.elementAt(0).toString());
 }

 @Test
 public void testMakeConstant() {
 JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("3")));
 jsterm.makeConstant();
 assertEquals("3", jsterm.elementAt(0).toString());
 }

 @Test
 public void testMakeEval() {
 JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(eval (+ 1 2) 3)")));
 jsterm.makeEval(false);
 assertEquals("(eval (+ 1 2) 3)", jsterm.toStr().toString());
 jsterm.makeEval(true);
 assertEquals("4", jsterm.elementAt(0).toString());
 }

 @Test
 public void testStandardizerTerm() {
 JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(x (y z))")));
 assertEquals("(x (f1 y) (f2 z))", jsterm.standardizerTerm().toStr().toString());
 }

 @Test
 public void testCall() {
 JSTerm jsterm = new JSTerm(new StreamTokenizer(new StringReader("(+ 1 2)")));
 assertEquals("3", jsterm.call().elementAt(0).toString());
 }

}