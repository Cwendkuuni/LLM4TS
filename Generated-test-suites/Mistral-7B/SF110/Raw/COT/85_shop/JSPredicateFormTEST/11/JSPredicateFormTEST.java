package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

import java.io.StringReader;
import java.util.Vector;

public class JSPredicateFormTest {

 private StreamTokenizer createTokenizer(String text) {
 return new StreamTokenizer(new StringReader(text));
 }

 @Test
 public void testJSPredicateFormEmptyConstructor() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertEquals(0, jspf.size());
 }

 @Test
 public void testJSPredicateFormStringConstructor() {
 String text = "!p(x)";
 JSPredicateForm jspf = new JSPredicateForm(text);
 assertEquals(1, jspf.size());
 assertEquals("!p", jspf.elementAt(0).toString());
 }

 @Test
 public void testJSPredicateFormStreamTokenizerConstructor() {
 String text = "!p(x)";
 JSPredicateForm jspf = new JSPredicateForm(createTokenizer(text));
 assertEquals(1, jspf.size());
 assertEquals("!p", jspf.elementAt(0).toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInvalidInput1() {
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.JSPredicateFormInit(createTokenizer("!"));
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInvalidInput2() {
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.JSPredicateFormInit(createTokenizer("!p"));
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInvalidInput3() {
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.JSPredicateFormInit(createTokenizer("!p(x)("));
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInvalidInput4() {
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.JSPredicateFormInit(createTokenizer("!p(x)!p"));
 }

 @Test
 public void testJSPredicateFormBasic() {
 String text = "!p(x)";
 JSPredicateForm jspf = new JSPredicateForm(text);
 assertEquals(1, jspf.size());
 assertEquals("!p", jspf.elementAt(0).toString());
 }

 @Test
 public void testJSPredicateFormWithNot() {
 String text = "!not p(x)";
 JSPredicateForm jspf = new JSPredicateForm(text);
 assertEquals(2, jspf.size());
 assertEquals("!not", jspf.elementAt(0).toString());
 assertTrue(jspf.elementAt(1) instanceof JSPredicateForm);
 }

 @Test
 public void testJSPredicateFormWithCall() {
 String text = "!call f(x)";
 JSPredicateForm jspf = new JSPredicateForm(text);
 assertEquals(2, jspf.size());
 assertEquals("!call", jspf.elementAt(0).toString());
 assertTrue(jspf.elementAt(1) instanceof JSTerm);
 }

 @Test
 public void testJSPredicateFormPrint() {
 String text = "!p(x)";
 JSPredicateForm jspf = new JSPredicateForm(text);
 jspf.print();
 assertEquals("( !p(x) )", System.getProperty("line.separator"));
 }

 @Test
 public void testJSPredicateFormToString() {
 String text = "!p(x)";
 JSPredicateForm jspf = new JSPredicateForm(text);
 assertEquals("( !p(x) )", jspf.toStr().toString());
 }

 @Test
 public void testJSPredicateFormClonePF() {
 String text = "!p(x)";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSPredicateForm cloned = jspf.clonePF();
 assertEquals(jspf.size(), cloned.size());
 for (int i = 0; i < jspf.size(); i++) {
 assertEquals(jspf.elementAt(i), cloned.elementAt(i));
 }
 }

 @Test
 public void testJSPredicateFormApplySubstitutionPF() {
 String text = "!p(x)";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("x", new JSTerm("a"));
 JSPredicateForm np = jspf.applySubstitutionPF(alpha);
 assertEquals("( !p(a) )", np.toStr().toString());
 }

 @Test
 public void testJSPredicateFormMatches() {
 String text1 = "!p(x)";
 String text2 = "!q(y)";
 JSPredicateForm pf1 = new JSPredicateForm(text1);
 JSPredicateForm pf2 = new JSPredicateForm(text2);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = pf1.matches(pf2, alpha);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testJSPredicateFormMatchesWithBinding() {
 String text1 = "!p(x)";
 String text2 = "!q(y)";
 String text3 = "!p(a)";
 JSPredicateForm pf1 = new JSPredicateForm(text1);
 JSPredicateForm pf2 = new JSPredicateForm(text2);
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("x", new JSTerm("a"));
 JSSubstitution beta = pf1.matches(pf2, alpha);
 assertEquals(1, beta.size());
 assertEquals("x -> a", beta.getBinding("x").toString());
 }

 @Test
 public void testJSPredicateFormMatchesEqual() {
 String text1 = "!p(x)";
 String text2 = "!p(x)";
 JSPredicateForm pf1 = new JSPredicateForm(text1);
 JSPredicateForm pf2 = new JSPredicateForm(text2);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = pf1.matches(pf2, alpha);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testJSPredicateFormMatchesNotEqual() {
 String text1 = "!p(x)";
 String text2 = "!q(x)";
 JSPredicateForm pf1 = new JSPredicateForm(text1);
 JSPredicateForm pf2 = new JSPredicateForm(text2);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = pf1.matches(pf2, alpha);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testJSPredicateFormMatchesDifferentSize() {
 String text1 = "!p(x)";
 String text2 = "!p(x)q(y)";
 JSPredicateForm pf1 = new JSPredicateForm(text1);
 JSPredicateForm pf2 = new JSPredicateForm(text2);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = pf1.matches(pf2, alpha);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testJSPredicateFormEquals() {
 String text1 = "!p(x)";
 String text2 = "!p(x)";
 JSPredicateForm pf1 = new JSPredicateForm(text1);
 JSPredicateForm pf2 = new JSPredicateForm(text2);
 assertTrue(pf1.equals(pf2));
 }

 @Test
 public void testJSPredicateFormNotEquals() {
 String text1 = "!p(x)";
 String text2 = "!q(x)";
 JSPredicateForm pf1 = new JSPredicateForm(text1);
 JSPredicateForm pf2 = new JSPredicateForm(text2);
 assertFalse(pf1.equals(pf2));
 }

 @Test
 public void testJSPredicateFormStandarizerPredicateForm() {
 String text = "!p(x)";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSPredicateForm np = jspf.standarizerPredicateForm();
 assertEquals(1, np.size());
 assertEquals("!p", np.elementAt(0).toString());
 }

}