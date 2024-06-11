package umd.cs.shop.test;

import org.junit.*;
import static org.junit.Assert.*;
import umd.cs.shop.*;

public class JSPredicateFormTest {

 @Test
 public void testEmptyJSPredicateForm() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertFalse("An empty JSPredicateForm should not be equal to any other predicateform", jspf.equals(new JSPredicateForm()));
 }

 @Test
 public void testJSPredicateFormInitWithNullText() {
 assertThrows(JSParserError.class, () -> new JSPredicateForm(null));
 }

 @Test
 public void testJSPredicateFormInitWithNullTokenizer() {
 assertThrows(JSParserError.class, () -> new JSPredicateForm(null));
 }

 @Test
 public void testJSPredicateFormInitWithText() {
 final String text = "not ( or ( a b ) ( c d ) )";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("not");
 expected.addElement(new JSPredicateForm());
 expected.get(1).addElement("or");
 expected.get(1).addElement(new JSTerm("a"));
 expected.get(1).addElement(new JSTerm("b"));
 expected.get(1).addElement(new JSPredicateForm());
 expected.get(1).get(1).addElement("c");
 expected.get(1).get(1).addElement("d");
 assertEquals("JSPredicateForm initialized correctly with provided text", expected, jspf);
 }

 @Test
 public void testJSPredicateFormInitWithTokenizer() {
 final String text = "not ( or ( a b ) ( c d ) )";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("not");
 expected.addElement(new JSPredicateForm());
 expected.get(1).addElement("or");
 expected.get(1).addElement(new JSTerm("a"));
 expected.get(1).addElement(new JSTerm("b"));
 expected.get(1).addElement(new JSPredicateForm());
 expected.get(1).get(1).addElement("c");
 expected.get(1).get(1).addElement("d");
 assertEquals("JSPredicateForm initialized correctly with provided tokenizer", expected, jspf);
 }

 @Test
 public void testJSPredicateFormPrint() {
 final String text = "not ( or ( a b ) ( c d ) )";
 JSPredicateForm jspf = new JSPredicateForm(text);
 jspf.print();
 String expected = "(\\(not (or (a b) (c d))))";
 assertEquals("JSPredicateForm print function returns expected string", expected, System.out.getHistory(0).substring(0, expected.length()));
 }

 @Test
 public void testJSPredicateFormToString() {
 final String text = "not ( or ( a b ) ( c d ) )";
 JSPredicateForm jspf = new JSPredicateForm(text);
 String expected = "(\\(not (or (a b) (c d))))";
 String result = jspf.toStr().toString();
 assertEquals("JSPredicateForm toString function returns expected string", expected, result);
 }

 @Test
 public void testJSPredicateFormClonePF() {
 final String text = "not ( or ( a b ) ( c d ) )";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSPredicateForm cloned = jspf.clonePF();
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("not");
 expected.addElement(new JSPredicateForm());
 expected.get(1).addElement("or");
 expected.get(1).addElement(new JSTerm("a"));
 expected.get(1).addElement(new JSTerm("b"));
 expected.get(1).addElement(new JSPredicateForm());
 expected.get(1).get(1).addElement("c");
 expected.get(1).get(1).addElement("d");
 assertEquals("JSPredicateForm clonePF function returns a deep copy", expected, cloned);
 }

 @Test
 public void testJSPredicateFormApplySubstitutionPF() {
 final String text1 = "not ( or ( a x ) ( c y ) )";
 final String text2 = "not ( or ( b z ) ( d w ) )";
 final JSSubstitution alpha = new JSSubstitution();
 alpha.addSubstitution("x", "b");
 alpha.addSubstitution("y", "w");
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("not");
 expected.addElement(new JSPredicateForm());
 expected.get(1).addElement("or");
 expected.get(1).addElement(new JSTerm("b"));
 expected.get(1).addElement(new JSTerm("w"));
 expected.get(1).addElement(new JSPredicateForm());
 expected.get(1).get(1).addElement("c");
 expected.get(1).get(1).addElement("d");
 JSPredicateForm result = jspf1.applySubstitutionPF(alpha);
 assertEquals("JSPredicateForm applySubstitutionPF function returns expected result", expected, result);
 }

 @Test
 public void testJSSubstitutionMatches() {
 final String text1 = "not ( or ( a x ) ( c y ) )";
 final String text2 = "not ( or ( b z ) ( d w ) )";
 final JSSubstitution alpha = new JSSubstitution();
 alpha.addSubstitution("x", "b");
 alpha.addSubstitution("y", "w");
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 JSSubstitution expected = new JSSubstitution();
 expected.addSubstitution("b", "a");
 expected.addSubstitution("w", "x");
 expected.addSubstitution("z", "c");
 expected.addSubstitution("d", "y");
 JSSubstitution result = jspf1.matches(jspf2);
 assertEquals("JSSubstitution matches function returns expected result", expected, result);
 }

 @Test
 public void testJSSubstitutionMatchesWithEmptySubstitution() {
 final String text1 = "not ( or ( a x ) ( c y ) )";
 final String text2 = "not ( or ( b z ) ( d w ) )";
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 JSSubstitution result = jspf1.matches(jspf2, new JSSubstitution());
 assertTrue("JSSubstitution matches function returns empty substitution when no matches found", result.isEmpty());
 }

 @Test
 public void testJSPredicateFormEquals() {
 final String text1 = "not ( or ( a x ) ( c y ) )";
 final String text2 = "not ( or ( a x ) ( c y ) )";
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 assertTrue("Two equal JSPredicateForm instances should be equal", jspf1.equals(jspf2));
 }

 @Test
 public void testJSPredicateFormNotEquals() {
 final String text1 = "not ( or ( a x ) ( c y ) )";
 final String text2 = "not ( or ( b z ) ( d w ) )";
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 assertFalse("Two unequal JSPredicateForm instances should not be equal", jspf1.equals(jspf2));
 }

 @Test
 public void testJSPredicateFormStandarizerPredicateForm() {
 final String text = "not ( or ( a x ) ( c y ) )";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("not");
 expected.addElement(new JSTerm("not"));
 expected.addElement("or");
 expected.addElement(new JSTerm("or"));
 expected.addElement("and");
 expected.addElement(new JSTerm("and"));
 expected.addElement(new JSTerm("a"));
 expected.addElement(new JSTerm("x"));
 expected.addElement(new JSTerm("c"));
 expected.addElement(new JSTerm("y"));
 JSPredicateForm result = jspf.standarizerPredicateForm();
 assertEquals("JSPredicateForm standarizerPredicateForm function returns expected result", expected, result);
 }
}