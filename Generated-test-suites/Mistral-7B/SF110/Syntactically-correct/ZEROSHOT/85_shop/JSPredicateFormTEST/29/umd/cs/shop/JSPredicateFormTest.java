package umd.cs.shop.test;

import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

public class JSPredicateFormTest {

 private String testData = "(and (or (not (= x y)) (not (= y x))) (= x z))";

 @Test
 public void testJSPredicateFormInit() {
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 Vector<String> expected = new Vector<String>() {{
 add("and");
 add("or");
 add("not");
 add("not");
 add("not");
 add("=");
 add("x");
 add("y");
 add("y");
 add("x");
 add("z");
 }};
 assertEquals(expected.size(), jspf.size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), jspf.elementAt(i).toString());
 }
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitInvalidData() {
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader("invalid data")));
 fail("Parsing error expected");
 }

 @Test
 public void testJSPredicateFormPrint() {
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 jspf.print();
 String expected = "(and (or (not (= x y)) (not (= y x))) (= x z))";
 assertEquals(expected, System.getProperty("line.separator") + System.getProperty("line.separator") + JSUtil.out.toString());
 }

 @Test
 public void testJSPredicateFormToStr() {
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 String expected = "(and (or (not (= x y)) (not (= y x))) (= x z))";
 assertEquals(expected, jspf.toStr().toString());
 }

 @Test
 public void testJSPredicateFormClonePF() {
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 JSPredicateForm cloned = jspf.clonePF();
 assertEquals(jspf, cloned);
 }

 @Test
 public void testJSPredicateFormApplySubstitutionPF() {
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("x", new JSTerm("3"));
 alpha.addBinding("y", new JSTerm("4"));
 alpha.addBinding("z", new JSTerm("5"));
 JSPredicateForm result = jspf.applySubstitutionPF(alpha);
 Vector<String> expected = new Vector<String>() {{
 add("and");
 add("or");
 add("not");
 add("not");
 add("not");
 add("=");
 add("3");
 add("4");
 add("4");
 add("3");
 add("5");
 }};
 assertEquals(expected.size(), result.size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), result.elementAt(i).toString());
 }
 }

 @Test
 public void testJSPredicateFormMatches() {
 JSPredicateForm jspf1 = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 JSPredicateForm jspf2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(and (or (not (= x z)) (not (= z x))) (= x y))")));
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = jspf1.matches(jspf2);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testJSPredicateFormMatchesWithBinding() {
 JSPredicateForm jspf1 = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 JSPredicateForm jspf2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(and (or (not (= x z)) (not (= z x))) (= z y))")));
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("x", new JSTerm("3"));
 alpha.addBinding("y", new JSTerm("4"));
 alpha.addBinding("z", new JSTerm("5"));
 JSSubstitution beta = jspf1.matches(jspf2, alpha);
 Vector<String> expected = new Vector<String>() {{
 add("x");
 add("5");
 }};
 assertEquals(expected.size(), beta.size());
 for (int i = 0; i < expected.size(); i++) {
 assertEquals(expected.get(i), beta.getVariable(i).toString());
 }
 }

 @Test
 public void testJSPredicateFormEquals() {
 JSPredicateForm jspf1 = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 JSPredicateForm jspf2 = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 assertTrue(jspf1.equals(jspf2));
 }

 @Test
 public void testJSPredicateFormEqualsDifferentSize() {
 JSPredicateForm jspf1 = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 JSPredicateForm jspf2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(and (= x y))")));
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testJSPredicateFormEqualsDifferentFunctor() {
 JSPredicateForm jspf1 = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 JSPredicateForm jspf2 = new JSPredicateForm(new StreamTokenizer(new StringReader("(or x y)")));
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testJSPredicateFormEqualsDifferentTerms() {
 JSPredicateForm jspf1 = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 JSPredicateForm jspf2 = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 JSTerm term1 = new JSTerm("3");
 JSTerm term2 = new JSTerm("4");
 jspf2.set(0, new JSTerm("not", term1, term2));
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testJSPredicateFormStandarizerPredicateForm() {
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(testData)));
 JSPredicateForm standard = jspf.standarizerPredicateForm();
 String expected = "(and (or (not (= x y)) (not (= y x))) (= x z))";
 assertEquals(expected, standard.toStr().toString());
 }
}