package umd.cs.shop.test;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 private static final int JSParserError = -1;
 private static final int leftPar = 1;
 private static final int rightPar = 2;
 private static final int exclamation = 3;
 private static final String not = "not";
 private static final String call = "call";

 @Test
 public void testJSPredicateFormEmptyConstructor() {
 JSPredicateForm pf = new JSPredicateForm();
 assertEquals(0, pf.size());
 }

 @Test
 public void testJSPredicateFormStringConstructor() {
 String text = "not ( p1 (q1) )";
 JSPredicateForm pf = new JSPredicateForm(text);
 assertEquals(3, pf.size());
 assertEquals("not", pf.elementAt(0).toString());
 JSPredicateForm el = (JSPredicateForm) pf.elementAt(1);
 assertEquals(1, el.size());
 assertEquals("p1", el.elementAt(0).toString());
 JSTerm term = (JSTerm) el.elementAt(0);
 assertEquals("q1", term.toString());
 }

 @Test
 public void testJSPredicateFormStreamTokenizerConstructor() {
 String text = "not ( p1 (q1) )";
 StringReader reader = new StringReader(text);
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 assertEquals(3, pf.size());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInvalidInput() {
 String text = "not %%%";
 StringReader reader = new StringReader(text);
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 fail();
 }

 @Test
 public void testJSPredicateFormPrint() {
 String text = "not ( p1 (q1) )";
 StringReader reader = new StringReader(text);
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 pf.print();
 assertEquals("(not (p1 (q1)))", JSUtil.readLine());
 }

 @Test
 public void testJSPredicateFormToString() {
 String text = "not ( p1 (q1) )";
 StringReader reader = new StringReader(text);
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 assertEquals("(not (p1 (q1)))", pf.toStr().toString());
 }

 @Test
 public void testJSPredicateFormClonePF() {
 String text = "not ( p1 (q1) )";
 StringReader reader = new StringReader(text);
 StreamTokenizer tokenizer = new StringReader(text);
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 JSPredicateForm clonedPF = pf.clonePF();
 assertEquals("not (p1 (q1))", clonedPF.toStr().toString());
 }

 @Test
 public void testJSPredicateFormApplySubstitutionPF() {
 String text = "not ( p1 (q1) )";
 StringReader reader1 = new StringReader(text);
 StreamTokenizer tokenizer1 = new StreamTokenizer(reader1);
 JSPredicateForm pf = new JSPredicateForm(tokenizer1);
 String text2 = "p1(x)";
 StringReader reader2 = new StringReader(text2);
 StreamTokenizer tokenizer2 = new StreamTokenizer(reader2);
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("p1", new JSTerm("p1x"));
 JSPredicateForm np = pf.applySubstitutionPF(alpha);
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("not");
 expected.addElement(new JSTerm("p1x"));
 assertEquals("not (p1x)", np.toStr().toString());
 }

 @Test
 public void testJSPredicateFormMatches() {
 String text = "not ( p1 (q1) )";
 StringReader reader = new StringReader(text);
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 String text2 = "not ( p1 (q2) )";
 StringReader reader2 = new StringReader(text2);
 StreamTokenizer tokenizer2 = new StreamTokenizer(reader2);
 JSPredicateForm pf2 = new JSPredicateForm(tokenizer2);
 JSSubstitution alpha = new JSSubstitution();
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("not");
 expected.addElement(new JSTerm("p1"));
 expected.addElement(new JSTerm("q2"));
 JSSubstitution beta = pf.matches(pf2, alpha);
 assertEquals(expected, beta);
 }

 @Test
 public void testJSPredicateFormMatchesFailure() {
 String text = "not ( p1 (q1) )";
 StringReader reader = new StringReader(text);
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 String text2 = "not ( q1 (p1) )";
 StringReader reader2 = new StringReader(text2);
 StreamTokenizer tokenizer2 = new StreamTokenizer(reader2);
 JSPredicateForm pf2 = new JSPredicateForm(tokenizer2);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = pf.matches(pf2, alpha);
 assertFalse(beta.isSuccess());
 }

 @Test
 public void testJSPredicateFormEquals() {
 String text = "not ( p1 (q1) )";
 StringReader reader = new StringReader(text);
 StreamTokenizer tokenizer = new StringReader(text);
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 String text2 = "not ( p1 (q2) )";
 StringReader reader2 = new StringReader(text2);
 StreamTokenizer tokenizer2 = new StringReader(text2);
 JSPredicateForm pf2 = new JSPredicateForm(tokenizer2);
 JSPredicateForm pf3 = pf.clonePF();
 assertTrue(pf.equals(pf));
 assertFalse(pf.equals(pf2));
 assertTrue(pf.equals(pf3));
 }

 @Test
 public void testJSPredicateFormStandarizerPredicateForm() {
 String text = "not ( p1 (q1) )";
 StringReader reader = new StringReader(text);
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 JSPredicateForm standardizedPF = pf.standarizerPredicateForm();
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("not");
 expected.addElement(new JSTerm("p1"));
 expected.addElement(new JSTerm("q1"));
 assertEquals(expected, standardizedPF);
 }

}