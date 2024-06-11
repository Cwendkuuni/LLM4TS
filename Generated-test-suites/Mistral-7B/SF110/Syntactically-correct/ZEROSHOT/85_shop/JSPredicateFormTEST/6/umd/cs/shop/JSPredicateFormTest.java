package umd.cs.shop.test;

import static org.junit.Assert.*;
import org.junit.Test;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 private static final String TEST_DATA = "(%not (or (= x 1) (= x 2)))";

 @Test
 public void testJSPredicateFormInit() {
 JSPredicateForm jspf = new JSPredicateForm(TEST_DATA);
 assertEquals(jspf.size(), 4);
 assertEquals(jspf.elementAt(0), "not");
 assertEquals(jspf.elementAt(1).getClass(), JSPredicateForm.class);
 assertEquals(jspf.elementAt(1).size(), 2);
 assertEquals(jspf.elementAt(1).elementAt(0), "or");
 assertEquals(jspf.elementAt(1).elementAt(1).size(), 2);
 assertEquals(jspf.elementAt(1).elementAt(1).elementAt(0), "=");
 assertEquals(jspf.elementAt(1).elementAt(1).elementAt(1), "x");
 assertEquals(jspf.elementAt(1).elementAt(1).elementAt(2), "1");
 }

 @Test
 public void testJSPredicateFormPrint() {
 JSPredicateForm jspf = new JSPredicateForm(TEST_DATA);
 String expected = "((not (or (= x 1) (= x 2))))";
 assertEquals(jspf.print(), expected);
 }

 @Test
 public void testJSPredicateFormToString() {
 JSPredicateForm jspf = new JSPredicateForm(TEST_DATA);
 String expected = "((not (or (= x 1) (= x 2))))";
 assertEquals(jspf.toStr().toString(), expected);
 }

 @Test
 public void testJSPredicateFormClonePF() {
 JSPredicateForm jspf = new JSPredicateForm(TEST_DATA);
 JSPredicateForm cloned = jspf.clonePF();
 assertEquals(jspf, cloned);
 }

 @Test
 public void testJSPredicateFormApplySubstitutionPF() {
 JSPredicateForm jspf = new JSPredicateForm(TEST_DATA);
 JSSubstitution alpha = new JSSubstitution();
 alpha.put("x", new JSTerm("y"));
 JSPredicateForm result = jspf.applySubstitutionPF(alpha);
 String expected = "((not (or (= y 1) (= y 2))))";
 assertEquals(result.print(), expected);
 }

 @Test(expected = AssertionError.class)
 public void testJSPredicateFormMismatchedSizes() {
 JSPredicateForm jspf1 = new JSPredicateForm("(= x 1)");
 JSPredicateForm jspf2 = new JSPredicateForm(TEST_DATA);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution matches = jspf1.matches(jspf2, alpha);
 fail("matches should have thrown an AssertionError");
 }

 @Test
 public void testJSPredicateFormNotMatches() {
 JSPredicateForm jspf1 = new JSPredicateForm("(= x 1)");
 JSPredicateForm jspf2 = new JSPredicateForm(TEST_DATA);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution matches = jspf1.matches(jspf2, alpha);
 assertFalse(matches.isSuccess());
 }

 @Test
 public void testJSPredicateFormMatches() {
 JSPredicateForm jspf1 = new JSPredicateForm("(= x 1)");
 JSPredicateForm jspf2 = new JSPredicateForm(TEST_DATA);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = jspf1.matches(jspf2, alpha);
 assertEquals(beta.size(), 0);
 }

 @Test
 public void testJSPredicateFormEquals() {
 JSPredicateForm jspf1 = new JSPredicateForm(TEST_DATA);
 JSPredicateForm jspf2 = new JSPredicateForm(TEST_DATA);
 assertTrue(jspf1.equals(jspf2));
 }

 @Test
 public void testJSPredicateFormNotEquals() {
 JSPredicateForm jspf1 = new JSPredicateForm(TEST_DATA);
 JSPredicateForm jspf2 = new JSPredicateForm("(= x 1)");
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testJSPredicateFormStandarizerPredicateForm() {
 JSPredicateForm jspf = new JSPredicateForm(TEST_DATA);
 JSPredicateForm standard = jspf.standarizerPredicateForm();
 String expected = "((not (or (= x 1) (= x 2))));";
 assertEquals(standard.print(), expected);
 }
}