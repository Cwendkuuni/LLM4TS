package umd.cs.shop.test;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.StringReader;
import java.util.Vector;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSParserError;
import umd.cs.shop.JSTerm;

public class JSPredicateFormTest {

 private Vector createPredicateForm(String text) {
 return new JSPredicateForm(text);
 }

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm pf = new JSPredicateForm();
 assertEquals(0, pf.size());
 }

 @Test
 public void testConstructorWithText() {
 String text = "not (a b)";
 JSPredicateForm pf = createPredicateForm(text);
 assertEquals(2, pf.size());
 assertEquals("not", pf.elementAt(0).toString());
 assertTrue(pf.elementAt(1) instanceof JSPredicateForm);
 }

 @Test
 public void testConstructorWithTokenizer() {
 String text = "not (a b)";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 assertEquals(2, pf.size());
 assertEquals("not", pf.elementAt(0).toString());
 assertTrue(pf.elementAt(1) instanceof JSPredicateForm);
 }

 @Test
 public void testJSPredicateFormInitWithNullTokenizer() {
 assertThrows(JSParserError.class, () -> new JSPredicateForm(null));
 }

 @Test
 public void testJSPredicateFormInitLeftParenthesisMissing() {
 assertThrows(JSParserError.class, () -> new JSPredicateForm(new StreamTokenizer(new StringReader("a"))));
 }

 @Test
 public void testJSPredicateFormInitRightParenthesisMissing() {
 assertThrows(JSParserError.class, () -> new JSPredicateForm(new StreamTokenizer(new StringReader("not a"))));
 }

 @Test
 public void testJSPredicateFormInitWithNotNot() {
 assertThrows(JSParserError.class, () -> new JSPredicateForm(new StreamTokenizer(new StringReader("not not (a b)"))));
 }

 @Test
 public void testJSPredicateFormInitWithNotCall() {
 assertThrows(JSParserError.class, () -> new JSPredicateForm(new StreamTokenizer(new StringReader("not call (a b)"))));
 }

 @Test
 public void testJSPredicateFormInitWithNotNotCall() {
 assertThrows(JSParserError.class, () -> new JSPredicateForm(new StreamTokenizer(new StringReader("not not call (a b)"))));
 }

 @Test
 public void testPrint() {
 String text = "not (a b)";
 JSPredicateForm pf = createPredicateForm(text);
 pf.print();
 String expected = "((not) (a) (b)) ";
 assertEquals(expected, System.out.getHistory()[0]);
 }

 @Test
 public void testToStr() {
 String text = "not (a b)";
 JSPredicateForm pf = createPredicateForm(text);
 assertEquals("((not) (a) (b)))", pf.toStr().toString());
 }

 @Test
 public void testClonePF() {
 String text = "not (a (b c) d))";
 JSPredicateForm pf = createPredicateForm(text);
 JSPredicateForm clonedPF = pf.clonePF();
 assertEquals(pf.size(), clonedPF.size());
 for (int i = 0; i < pf.size(); i++) {
 if (pf.elementAt(i) instanceof String) {
 assertEquals(pf.elementAt(i), clonedPF.elementAt(i));
 } else {
 assertEquals(pf.elementAt(i).hashCode(), clonedPF.elementAt(i).hashCode());
 }
 }
 }

 @Test
 public void testApplySubstitutionPF() {
 String text = "not (a (b c) d))";
 JSPredicateForm pf = createPredicateForm(text);
 JSSubstitution alpha = new JSSubstitution();
 JSPredicateForm result = pf.applySubstitutionPF(alpha);
 assertEquals(pf.size(), result.size());
 for (int i = 0; i < pf.size(); i++) {
 if (pf.elementAt(i) instanceof String) {
 assertEquals(pf.elementAt(i), result.elementAt(i));
 }
 }
 }

 @Test
 public void testMatches() {
 String text1 = "not (a (b c) d))";
 String text2 = "not (a (x y) e))";
 JSPredicateForm pf1 = createPredicateForm(text1);
 JSPredicateForm pf2 = createPredicateForm(text2);
 JSSubstitution result = pf1.matches(pf2);
 assertFalse(result.isEmpty());
 assertEquals("b", result.getValue("c").toString());
 assertEquals("x", result.getValue("b").toString());
 }

 @Test
 public void testMatchesWithEmptySubstitution() {
 String text1 = "not (a (b c) d))";
 String text2 = "not (a (b b) d))";
 JSPredicateForm pf1 = createPredicateForm(text1);
 JSPredicateForm pf2 = createPredicateForm(text2);
 JSSubstitution result = pf1.matches(pf2, new JSSubstitution());
 assertFalse(result.isEmpty());
 assertEquals("b", result.getValue("b").toString());
 }

 @Test
 public void testEquals() {
 String text1 = "not (a (b c) d))";
 String text2 = "not (a (b c) d))";
 JSPredicateForm pf1 = createPredicateForm(text1);
 JSPredicateForm pf2 = createPredicateForm(text2);
 assertTrue(pf1.equals(pf2));
 }

 @Test
 public void testEqualsWithDifferentSize() {
 String text1 = "not (a (b c) d))";
 String text2 = "not (a (b c) d) e)";
 JSPredicateForm pf1 = createPredicateForm(text1);
 JSPredicateForm pf2 = createPredicateForm(text2);
 assertFalse(pf1.equals(pf2));
 }

 @Test
 public void testEqualsWithDifferentElementAtIndex0() {
 String text1 = "not (a (b c) d))";
 String text2 = "not (a (b b) d))";
 JSPredicateForm pf1 = createPredicateForm(text1);
 JSPredicateForm pf2 = createPredicateForm(text2);
 assertFalse(pf1.equals(pf2));
 }

 @Test
 public void testEqualsWithDifferentElementAtIndex1() {
 String text1 = "not (a (b c) d))";
 String text2 = "not (a (b d) d))";
 JSPredicateForm pf1 = createPredicateForm(text1);
 JSPredicateForm pf2 = createPredicateForm(text2);
 assertFalse(pf1.equals(pf2));
 }

 @Test
 public void testStandarizerPredicateForm() {
 String text = "not (a (b (c d) e) f))";
 JSPredicateForm pf = createPredicateForm(text);
 JSPredicateForm standardizedPF = pf.standarizerPredicateForm();
 assertEquals(5, standardizedPF.size());
 assertEquals("not", standardizedPF.elementAt(0).toString());
 assertTrue(standardizedPF.elementAt(1) instanceof JSTerm);
 }

}