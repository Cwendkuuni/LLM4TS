import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSTerm;

public class JSTermTest {

 @Test
 public void testJSTermInstance() {
 JSTerm jsterm = new JSTerm();
 assertFalse(jsterm.isVariable());
 assertFalse(jsterm.isConstant());
 assertFalse(jsterm.isFunction());
 assertFalse(jsterm.isEval());

 try {
 jsterm.parseList(new StringReader(""));
 fail("Should throw JSParserError exception");
 } catch (JSParserError e) {
 // Expected
 }
 }

 @Test
 public void testJSTermVariable() {
 JSTerm jsterm = new JSTerm(new StringReader("x"));
 assertTrue(jsterm.isVariable());
 assertFalse(jsterm.isConstant());
 assertFalse(jsterm.isFunction());
 assertFalse(jsterm.isEval());
 assertEquals("?x", jsterm.toString());
 }

 @Test
 public void testJSTermConstant() {
 JSTerm jsterm = new JSTerm(new StringReader("5"));
 assertFalse(jsterm.isVariable());
 assertTrue(jsterm.isConstant());
 assertFalse(jsterm.isFunction());
 assertFalse(jsterm.isEval());
 assertEquals("5", jsterm.toString());
 }

 @Test
 public void testJSTermFunction() {
 JSTerm jsterm = new JSTerm(new StringReader("(add x 5)"));
 assertTrue(jsterm.isFunction());
 assertFalse(jsterm.isVariable());
 assertFalse(jsterm.isConstant());
 assertFalse(jsterm.isEval());
 assertEquals("(add x 5)", jsterm.toString());
 }

 @Test
 public void testJSTermEval() {
 JSTerm jsterm = new JSTerm(new StringReader("(eval (add x 5))"));
 assertTrue(jsterm.isEval());
 assertTrue(jsterm.isFunction());
 assertFalse(jsterm.isVariable());
 assertFalse(jsterm.isConstant());
 assertEquals("(eval (add x 5))", jsterm.toString());
 }

 @Test
 public void testJSTermParseList() {
 JSTerm jsterm = new JSTerm(new StringReader("(+ 3 4)"));
 assertTrue(jsterm.isFunction());
 assertFalse(jsterm.isVariable());
 assertFalse(jsterm.isConstant());
 assertFalse(jsterm.isEval());
 assertEquals("(+ 3 4)", jsterm.toString());

 JSTerm list = jsterm.elementAt(0);
 assertEquals("+", list.toString());
 list = jsterm.elementAt(1);
 assertTrue(list.isNumber());
 assertEquals("3", list.toString());
 list = jsterm.elementAt(2);
 assertTrue(list.isNumber());
 assertEquals("4", list.toString());
 }

 @Test
 public void testJSTermGround() {
 JSTerm jsterm = new JSTerm(new StringReader("(+ x 5)"));
 assertFalse(jsterm.isGround());

 JSTerm jsterm2 = new JSTerm(new StringReader("(+ 3 4)"));
 assertTrue(jsterm2.isGround());
 }

 @Test
 public void testJSTermEquals() {
 JSTerm jsterm1 = new JSTerm(new StringReader("(+ x 5)"));
 JSTerm jsterm2 = new JSTerm(new StringReader("(+ x 5)"));
 assertTrue(jsterm1.equals(jsterm2));

 JSTerm jsterm3 = new JSTerm(new StringReader("(+ y 5)"));
 assertFalse(jsterm1.equals(jsterm3));
 }

 @Test
 public void testJSTermToStr() {
 JSTerm jsterm = new JSTerm(new StringReader("(+ x 5)"));
 assertEquals("(+ x 5)", jsterm.toString());
 }

 @Test
 public void testJSTermStandardizerTerm() {
 JSTerm jsterm = new JSTerm(new StringReader("(+ x 5)"));
 JSTerm expected = new JSTerm(new StringReader("(+ ?x5)"));
 JSTerm result = jsterm.standardizerTerm();
 assertTrue(expected.equals(result));
 }

 @Test
 public void testJSTermCall() {
 JSTerm jsterm = new JSTerm(new StringReader("(+ x 5)"));
 JSTerm result = jsterm.call();
 JSTerm expected = new JSTerm(new StringReader("5"));
 assertTrue(expected.equals(result));
 }
}