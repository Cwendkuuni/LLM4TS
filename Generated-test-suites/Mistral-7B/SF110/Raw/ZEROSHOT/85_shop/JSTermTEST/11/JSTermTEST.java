import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JSTermTest {

 private static final String TEST_DATA = "(% call (+ 1 2) (list 3 4) )";

 @Test
 public void testJSTermConstruction() {
 JSTerm term = new JSTerm();
 assertNotNull(term);
 JSTerm variableTerm = new JSTerm("x", false, false, false);
 assertNotNull(variableTerm);
 JSTerm constantTerm = new JSTerm("5", false, true, false);
 assertNotNull(constantTerm);
 JSTerm functionTerm = new JSTerm("*", true, false, false);
 assertNotNull(functionTerm);
 }

 @Test
 public void testJSTermParse() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(TEST_DATA));
 JSTerm term = new JSTerm(tokenizer);
 assertEquals("call", term.elementAt(0).toString());
 JSTerm callTerm = (JSTerm) term.elementAt(1);
 assertEquals("+", callTerm.elementAt(0).toString());
 JSTerm addTerm1 = (JSTerm) callTerm.elementAt(1);
 assertEquals("1", addTerm1.elementAt(0).toString());
 JSTerm addTerm2 = (JSTerm) callTerm.elementAt(2);
 assertEquals("2", addTerm2.elementAt(0).toString());
 JSTerm listTerm = (JSTerm) term.elementAt(2);
 assertEquals("list", listTerm.elementAt(0).toString());
 List<JSTerm> listElements = new ArrayList<>();
 listElements.add(new JSTerm("3", false, true, false));
 listElements.add(new JSTerm("4", false, true, false));
 JSTerm list = new JSTerm(listElements);
 assertEquals(list, listTerm.elementAt(1));
 }

 @Test
 public void testJSTermSizeAndGet() {
 JSTerm term = new JSTerm("x", false, false, false);
 term.addElement(new JSTerm("3", false, true, false));
 term.addElement(new JSTerm("4", false, true, false));
 assertEquals(2, term.size());
 assertEquals(term.elementAt(1), term.get(0));
 }

 @Test
 public void testJSTermToString() {
 JSTerm term = new JSTerm("x", false, false, false);
 term.addElement(new JSTerm("3", false, true, false));
 term.addElement(new JSTerm("4", false, true, false));
 assertEquals("(x 3 4)", term.toStr().toString());
 }

 @Test
 public void testJSTermIsVariable() {
 JSTerm variableTerm = new JSTerm("x", false, false, false);
 assertTrue(variableTerm.isVariable());
 JSTerm constantTerm = new JSTerm("5", false, true, false);
 assertFalse(constantTerm.isVariable());
 }

 @Test
 public void testJSTermIsConstant() {
 JSTerm constantTerm = new JSTerm("5", false, true, false);
 assertTrue(constantTerm.isConstant());
 JSTerm variableTerm = new JSTerm("x", false, false, false);
 assertFalse(variableTerm.isConstant());
 }

 @Test
 public void testJSTermIsFunction() {
 JSTerm functionTerm = new JSTerm("*", true, false, false);
 assertTrue(functionTerm.isFunction());
 JSTerm constantTerm = new JSTerm("5", false, true, false);
 assertFalse(constantTerm.isFunction());
 }

 @Test
 public void testJSTermIsEval() {
 JSTerm constantTerm = new JSTerm("5", false, true, false);
 assertFalse(constantTerm.isEval());
 JSTerm evalTerm = new JSTerm("eval", true, true, true);
 assertTrue(evalTerm.isEval());
 }

 @Test
 public void testJSTermPrint() {
 JSTerm term = new JSTerm("x", false, false, false);
 term.addElement(new JSTerm("3", false, true, false));
 term.addElement(new JSTerm("4", false, true, false));
 String expectedOutput = "(x 3 4)";
 assertEquals(expectedOutput, term.toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSTermParseError() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("( x 3 4 5 )"));
 new JSTerm(tokenizer);
 }
}