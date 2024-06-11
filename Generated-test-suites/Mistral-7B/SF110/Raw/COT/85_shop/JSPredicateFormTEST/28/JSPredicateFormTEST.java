package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;

public class JSPredicateFormTest {

 private JSPredicateForm createForm(String text) {
 return new JSPredicateForm(text);
 }

 @Test
 public void testJSPredicateFormEmptyConstructor() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertEquals(0, jspf.size());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormNullTextConstructor() {
 JSPredicateForm jspf = new JSPredicateForm(null);
 }

 @Test
 public void testJSPredicateFormSimpleTextConstructor() {
 JSPredicateForm jspf = createForm("p(x)");
 assertEquals(1, jspf.size());
 assertEquals("p", jspf.elementAt(0).toString().toLowerCase());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInvalidTextConstructor() {
 JSPredicateForm jspf = createForm("not ( p(x) ) x");
 }

 @Test
 public void testJSPredicateFormInitWithTokenizer() {
 JSPredicateForm jspf = new JSPredicateForm();
 String text = "not ( p(x) )";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 jspf.JSPredicateFormInit(tokenizer);
 assertEquals(2, jspf.size());
 assertEquals("not", jspf.elementAt(0).toString());
 JSTerm term = (JSTerm) jspf.elementAt(1);
 assertEquals("p(x)", term.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithInvalidTokenizer() {
 JSPredicateForm jspf = new JSPredicateForm();
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("not ( p(x) "));
 jspf.JSPredicateFormInit(tokenizer);
 }

 @Test
 public void testPrint() {
 JSPredicateForm jspf = createForm("not ( p(x) )");
 jspf.print();
 assertEquals("( not ( p(x) ) )", System.out.getHistory(0));
 }

 @Test
 public void testToStr() {
 JSPredicateForm jspf = createForm("not ( p(x) )");
 assertEquals("( not ( p(x) ) )", jspf.toStr().toString());
 }

 @Test
 public void testClonePF() {
 JSPredicateForm jspf = createForm("not ( p(x) )");
 JSPredicateForm cloned = jspf.clonePF();
 assertEquals(jspf, cloned);
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm jspf = createForm("p(x)");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement("x", new JSTerm("y"));
 JSPredicateForm result = jspf.applySubstitutionPF(alpha);
 assertEquals("p(y)", result.elementAt(0).toString());
 }

 @Test
 public void testMatches() {
 JSPredicateForm jspf1 = createForm("p(x)");
 JSPredicateForm jspf2 = createForm("p(y)");
 JSSubstitution beta = jspf1.matches(jspf2);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testMatchesWithSubstitution() {
 JSPredicateForm jspf1 = createForm("p(x)");
 JSPredicateForm jspf2 = createForm("p(y)");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addElement("x", new JSTerm("y"));
 JSSubstitution beta = jspf1.matches(jspf2, alpha);
 assertEquals("y", beta.elementAt(0).toString());
 assertEquals("p", beta.elementAt(1).toString());
 }

 @Test
 public void testEquals() {
 JSPredicateForm jspf1 = createForm("p(x)");
 JSPredicateForm jspf2 = createForm("p(x)");
 assertTrue(jspf1.equals(jspf2));
 }

 @Test
 public void testEqualsDifferentSize() {
 JSPredicateForm jspf1 = createForm("p(x)");
 JSPredicateForm jspf2 = createForm("p(x) p(y)");
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testEqualsDifferentFunctor() {
 JSPredicateForm jspf1 = createForm("p(x)");
 JSPredicateForm jspf2 = createForm("q(x)");
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testEqualsDifferentTerms() {
 JSPredicateForm jspf1 = createForm("p(x)");
 JSPredicateForm jspf2 = createForm("q(x)");
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSTerm term1Clone = term1.cloneT();
 JSTerm term2Clone = term2.cloneT();
 jspf1.setElementAt(term1Clone, 1);
 jspf2.setElementAt(term2Clone, 1);
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm jspf = createForm("p(x) or q(y) and not r(z)");
 JSPredicateForm result = jspf.standarizerPredicateForm();
 assertEquals("( ( or ( p(x) ) ( and ( q(y) ) ( not ( r(z) ) ) ) ) )", result.toStr().toString());
 }
}