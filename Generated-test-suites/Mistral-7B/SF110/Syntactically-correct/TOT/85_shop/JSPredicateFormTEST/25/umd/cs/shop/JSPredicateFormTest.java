package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {

 @Test
 public void testEmptyJSPredicateForm() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertTrue(jspf.size() == 0);
 }

 @Test
 public void testJSPredicateFormInitFromString() {
 String text = "!(p1)";
 JSPredicateForm jspf = new JSPredicateForm(text);
 assertEquals("!p1", jspf.elementAt(0).toString());
 assertEquals(1, jspf.size());
 }

 @Test
 public void testJSPredicateFormInitFromStreamTokenizer_validInput() {
 String text = "!(not (or p1 p2) p3)";
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(text)));
 assertEquals("!not", jspf.elementAt(0).toString());
 assertEquals("or", jspf.elementAt(1).toString());
 assertEquals("p1", jspf.elementAt(2).toString());
 assertEquals("p2", jspf.elementAt(3).toString());
 assertEquals("p3", jspf.elementAt(5).toString());
 assertEquals(6, jspf.size());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitFromStreamTokenizer_invalidInput_missingLeftParenthesis() {
 String text = "not p1)";
 try {
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(text)));
 fail("Expected JSParserError");
 } catch (JSParserError e) {
 // Expected
 }
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitFromStreamTokenizer_invalidInput_missingOperatorAfterNot() {
 String text = "!(p1";
 try {
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(text)));
 fail("Expected JSParserError");
 } catch (JSParserError e) {
 // Expected
 }
 }

 @Test
 public void testJSPredicateFormPrint() {
 String text = "!(not (or p1 p2) p3)";
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(text)));
 String expected = "((!not) (or p1 p2) p3)";
 assertEquals(expected, jspf.print());
 }

 @Test
 public void testToString() {
 String text = "!(not (or p1 p2) p3)";
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(text)));
 String expected = "((!not) (or p1 p2) p3)";
 assertEquals(expected, jspf.toStr().toString());
 }

 @Test
 public void testClonePF() {
 String text = "!(not (or p1 p2) p3)";
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(text)));
 JSPredicateForm cloned = jspf.clonePF();
 assertFalse(jspf == cloned);
 assertTrue(jspf.equals(cloned));
 }

 @Test
 public void testApplySubstitutionPF() {
 String text = "!(not (or p1 p2) p3)";
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(text)));
 JSSubstitution alpha = new JSSubstitution();
 alpha.assign("p1", new JSTerm("q1"));
 JSPredicateForm result = jspf.applySubstitutionPF(alpha);
 String expected = "!(not (or q1 q2 p3) p3)";
 assertEquals(expected, result.toStr().toString());
 }

 @Test
 public void testMatches_identicalPredicates() {
 JSPredicateForm p1 = new JSPredicateForm(new StreamTokenizer(new StringReader("!(not (or p1 p2) p3)")));
 JSPredicateForm p2 = new JSPredicateForm(new StreamTokenizer(new StringReader("!(not (or p1 p2) p3)")));
 JSSubstitution beta = p1.matches(p2);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testMatches_differentPredicates() {
 JSPredicateForm p1 = new JSPredicateForm(new StreamTokenizer(new StringReader("!(not (or p1 p2) p3)")));
 JSPredicateForm p2 = new JSPredicateForm(new StreamTokenizer(new StringReader("!(or p3 p1 p2) p4)")));
 JSSubstitution beta = p1.matches(p2);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testMatches_matchingPredicates() {
 JSPredicateForm p1 = new JSPredicateForm(new StreamTokenizer(new StringReader("!(not (or p1 p2) p3)")));
 JSPredicateForm p2 = new JSPredicateForm(new StreamTokenizer(new StringReader("!(not (or p2 p1 p3) p3)")));
 JSSubstitution beta = p1.matches(p2);
 JSSubstitution expected = new JSSubstitution();
 expected.assign("p1", "p2");
 expected.assign("p2", "p1");
 assertTrue(beta.equals(expected));
 }

 @Test
 public void testEquals() {
 JSPredicateForm p1 = new JSPredicateForm(new StreamTokenizer(new StringReader("!(not (or p1 p2) p3)")));
 JSPredicateForm p2 = new JSPredicateForm(new StreamTokenizer(new StringReader("!(not (or p1 p2) p3)")));
 assertTrue(p1.equals(p2));
 }

 @Test
 public void testEquals_differentPredicates() {
 JSPredicateForm p1 = new JSPredicateForm(new StreamTokenizer(new StringReader("!(not (or p1 p2) p3)")));
 JSPredicateForm p2 = new JSPredicateForm(new StreamTokenizer(new StringReader("!(or p2 p1 p3) p4)")));
 assertFalse(p1.equals(p2));
 }

 @Test
 public void testStandarizerPredicateForm() {
 String text = "!(not (or p1 p2) p3)";
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(text)));
 JSPredicateForm result = jspf.standarizerPredicateForm();
 String expected = "(!not) (or p1 p2) p3";
 assertEquals(expected, result.toStr().toString());
 }
}