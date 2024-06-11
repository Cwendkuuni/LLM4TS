import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSParserError;

public class JSPredicateFormTest {

 @Test
 public void testEmptyJSPredicateForm() {
 JSPredicateForm jspf = new JSPredicateForm();
 JSPredicateForm expected = new JSPredicateForm();
 assertEquals(expected, jspf);
 }

 @Test
 public void testJSPredicateFormWithText() {
 String text = "not ( a and b )";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("not");
 expected.addElement("a");
 expected.addElement("and");
 expected.addElement("b");
 assertEquals(expected, jspf);
 }

 @Test
 public void testJSPredicateFormWithStreamTokenizer() {
 String text = "not ( a and b )";
 StreamTokenizer st = new StreamTokenizer(new StringReader(text));
 JSPredicateForm jspf = new JSPredicateForm(st);
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("not");
 expected.addElement("a");
 expected.addElement("and");
 expected.addElement("b");
 assertEquals(expected, jspf);
 }

 @Test(expected = JSParserError.class)
 public void testInvalidTextFormat() {
 String text = "not ( a and b ) )";
 JSPredicateForm jspf = new JSPredicateForm(text);
 assertNull(jspf);
 }

 @Test(expected = JSParserError.class)
 public void testInvalidStreamTokenizerFormat() {
 String text = "not ( a and b ) ";
 StreamTokenizer st = new StreamTokenizer(new StringReader(text));
 JSPredicateForm jspf = new JSPredicateForm(st);
 assertNull(jspf);
 }

 @Test(expected = JSParserError.class)
 public void testMissingLeftParenthesis() {
 String text = "not a and b ";
 StreamTokenizer st = new StreamTokenizer(new StringReader(text));
 JSPredicateForm jspf = new JSPredicateForm(st);
 assertNull(jspf);
 }

 @Test(expected = JSParserError.class)
 public void testMissingRightParenthesis() {
 String text = "not ( a and b";
 StreamTokenizer st = new StreamTokenizer(new StringReader(text));
 JSPredicateForm jspf = new JSPredicateForm(st);
 assertNull(jspf);
 }

 @Test(expected = JSParserError.class)
 public void testInvalidOperator() {
 String text = "not ( a %%% b )";
 StreamTokenizer st = new StreamTokenizer(new StringReader(text));
 JSPredicateForm jspf = new JSPredicateForm(st);
 assertNull(jspf);
 }

 @Test
 public void testPrintMethod() {
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.addElement("not");
 jspf.addElement("a");
 jspf.addElement("and");
 jspf.addElement("b");
 String expected = "((not a) and b)";
 JSUtil.println("Expected: " + expected);
 jspf.print();
 String actual = JSUtil.getCurrentOutput();
 JSUtil.println("Actual: " + actual);
 assertEquals(expected, actual);
 }

 @Test
 public void testToStringMethod() {
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.addElement("not");
 jspf.addElement("a");
 jspf.addElement("and");
 jspf.addElement("b");
 String expected = "((not a) and b)";
 String actual = jspf.toStr().toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testClonePFMethod() {
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.addElement("not");
 JSPredicateForm clone = jspf.clonePF();
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("not");
 assertEquals(expected, clone);
 }

 @Test
 public void testApplySubstitutionPFMethod() {
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.addElement("not");
 JSTerm t1 = new JSTerm("a");
 JSTerm t2 = new JSTerm("b");
 jspf.addElement(t1);
 jspf.addElement(t2);
 JSSubstitution substitution = new JSSubstitution();
 substitution.addBinding("a", new JSTerm("c"));
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("not");
 expected.addElement(new JSTerm("c"));
 expected.addElement(t2);
 JSPredicateForm result = jspf.applySubstitutionPF(substitution);
 assertEquals(expected, result);
 }

 @Test
 public void testMatchesMethod() {
 JSPredicateForm jspf1 = new JSPredicateForm();
 jspf1.addElement("not");
 JSTerm t1 = new JSTerm("a");
 JSTerm t2 = new JSTerm("b");
 jspf1.addElement(t1);
 jspf1.addElement(t2);
 JSPredicateForm jspf2 = new JSPredicateForm();
 JSTerm t3 = new JSTerm("c");
 JSTerm t4 = new JSTerm("d");
 jspf2.addElement("not");
 jspf2.addElement(t3);
 jspf2.addElement(t4);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = jspf1.matches(jspf2, alpha);
 JSSubstitution gamma = new JSSubstitution();
 gamma.addBinding("a", t3);
 gamma.addBinding("b", t4);
 assertEquals(gamma, beta);
 }

 @Test
 public void testEqualsMethod() {
 JSPredicateForm jspf1 = new JSPredicateForm();
 JSPredicateForm jspf2 = new JSPredicateForm();
 JSPredicateForm jspf3 = new JSPredicateForm();
 JSTerm t1 = new JSTerm("a");
 JSTerm t2 = new JSTerm("b");
 JSTerm t3 = new JSTerm("c");
 JSTerm t4 = new JSTerm("d");
 jspf1.addElement("not");
 jspf1.addElement(t1);
 jspf1.addElement(t2);
 jspf1.addElement(t1);
 jspf1.addElement(t2);
 jspf2.addElement("not");
 jspf2.addElement(t3);
 jspf2.addElement(t4);
 jspf3.addElement("not");
 jspf3.addElement(t1);
 jspf3.addElement(t2);
 jspf3.addElement(t1);
 jspf3.addElement(t2);
 assertEquals(jspf1, jspf3);
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testStandarizerPredicateFormMethod() {
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.addElement("not");
 jspf.addElement("a");
 jspf.addElement("and");
 jspf.addElement("b");
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("not");
 expected.addElement("a");
 expected.addElement("and");
 expected.addElement("b");
 JSPredicateForm result = jspf.standarizerPredicateForm();
 assertEquals(expected, result);
 }
}