package umd.cs.shop.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

 JSTerm getTestTerm() {
 return new JSTerm(new StringReader("(a b)"), new StreamTokenizer());
 }

 @Test
 public void testJSPredicateFormInit() {
 JSPredicateForm pf = new JSPredicateForm();
 assertEquals(0, pf.size());

 String text = "(! a) (or (b c) d)";
 JSPredicateForm expected = new JSPredicateForm(text);
 JSPredicateForm actual = new JSPredicateForm(new StringReader(text), new StreamTokenizer());
 assertEquals(expected, actual);

 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = expected.matches(new JSPredicateForm(), alpha);
 assertEquals(expected, beta.getSource());
 }

 @Test
 public void testJSPredicateFormInitWithTokenizer() {
 JSPredicateForm pf = new JSPredicateForm(new StreamTokenizer(new StringReader("(! a) (or (b c) d)")));
 assertEquals(3, pf.size());

 String str0 = pf.elementAt(0);
 assertEquals("!", str0);
 JSPredicateForm subForm1 = (JSPredicateForm) pf.elementAt(1);
 assertEquals(2, subForm1.size());
 String str10 = subForm1.elementAt(0);
 assertEquals("a", str10);
 JSTerm term11 = (JSTerm) subForm1.elementAt(1);
 assertEquals("or", term11.toString());
 JSPredicateForm subForm22 = (JSPredicateForm) term11.elementAt(1);
 assertEquals(2, subForm22.size());
 String str20 = subForm22.elementAt(0);
 assertEquals("b", str20);
 JSTerm term21 = (JSTerm) subForm22.elementAt(1);
 assertEquals("c", term21.toString());
 JSTerm term22 = (JSTerm) pf.elementAt(2);
 assertEquals("d", term22.toString());
 }

 @Test
 public void testPrint() {
 JSPredicateForm pf = new JSPredicateForm("(! a) (or (b c) d)");
 pf.print();
}

 @Test
 public void testToStr() {
 JSPredicateForm pf = new JSPredicateForm("(! a) (or (b c) d)");
 String expected = "(! a) (or (b c) d)";
 String actual = pf.toStr().toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testClonePF() {
 JSPredicateForm pf = new JSPredicateForm("(! a) (or (b c) d)");
 JSPredicateForm clone = pf.clonePF();
 assertEquals(pf, clone);
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm pf = new JSPredicateForm("(! a) (or (b c) d)");
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = pf.applySubstitutionPF(alpha);
 assertEquals(pf, beta.getSource());
 }

 @Test
 public void testMatches() {
 JSPredicateForm pf1 = new JSPredicateForm("(and (or a b) c) (or d e) f");
 JSPredicateForm pf2 = new JSPredicateForm("(not (and (or a b) c) d) (not (or e f) c)");
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = pf1.matches(pf2, alpha);
 assertEquals(pf1, beta.getSource());
 JSTerm term1 = (JSTerm) pf1.elementAt(0);
 assertEquals("and", term1.toString());
 JSTerm term2 = (JSTerm) pf1.elementAt(1);
 assertEquals("or", term2.toString());
 JSTerm term3 = (JSTerm) term2.elementAt(0);
 assertEquals("a", term3.toString());
 JSTerm term4 = (JSTerm) term2.elementAt(1);
 assertEquals("b", term4.toString());
 JSTerm term5 = (JSTerm) pf2.elementAt(0);
 assertEquals("not", term5.toString());
 JSTerm term6 = (JSTerm) term5.elementAt(1);
 assertEquals("and", term6.toString());
 JSTerm term7 = (JSTerm) term6.elementAt(0);
 assertEquals("or", term7.toString());
 JSTerm term8 = (JSTerm) term7.elementAt(0);
 assertEquals("a", term8.toString());
 JSTerm term9 = (JSTerm) term7.elementAt(1);
 assertEquals("b", term9.toString());
 JSTerm term10 = (JSTerm) pf2.elementAt(1);
 assertEquals("not", term10.toString());
 JSTerm term11 = (JSTerm) term10.elementAt(1);
 assertEquals("or", term11.toString());
 JSTerm term12 = (JSTerm) term11.elementAt(0);
 assertEquals("e", term12.toString());
 JSTerm term13 = (JSTerm) term11.elementAt(1);
 assertEquals("f", term13.toString());
 }

 @Test
 public void testEquals() {
 JSPredicateForm pf1 = new JSPredicateForm("(and (or a b) c) (or d e) f");
 JSPredicateForm pf2 = new JSPredicateForm("(not (and (or a b) c) d) (not (or e f) c)");
 JSPredicateForm pf3 = new JSPredicateForm("(and (or a b) c) (or d e) f");
 JSPredicateForm pf4 = new JSPredicateForm("(not (and (or a b) c) d) (not (or e f) c)");
 JSPredicateForm pf5 = new JSPredicateForm("(and (or a b) c) (or d e) g");
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = pf1.matches(pf2, alpha);
 assertTrue(pf1.equals(pf3));
 assertFalse(pf1.equals(pf4));
 assertFalse(pf1.equals(pf5));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm pf = new JSPredicateForm("(! a) (or (b c) d)");
 JSPredicateForm standard = pf.standarizerPredicateForm();
 JSTerm term0 = standard.elementAt(0);
 assertEquals("not", term0.toString());
 JSPredicateForm subForm1 = (JSPredicateForm) term0.elementAt(1);
 assertEquals(2, subForm1.size());
 String str10 = subForm1.elementAt(0).toString();
 assertEquals("a", str10);
 JSPredicateForm subForm2 = (JSPredicateForm) subForm1.elementAt(1);
 assertEquals(2, subForm2.size());
 String str20 = subForm2.elementAt(0).toString();
 assertEquals("or", str20);
 JSPredicateForm subForm3 = (JSPredicateForm) subForm2.elementAt(1);
 assertEquals(2, subForm3.size());
 String str30 = subForm3.elementAt(0).toString();
 assertEquals("b", str30);
 JSTerm term31 = (JSTerm) subForm3.elementAt(1);
 assertEquals("c", term31.toString());
 JSTerm term32 = (JSTerm) standard.elementAt(1);
 assertEquals("d", term32.toString());
 }
}