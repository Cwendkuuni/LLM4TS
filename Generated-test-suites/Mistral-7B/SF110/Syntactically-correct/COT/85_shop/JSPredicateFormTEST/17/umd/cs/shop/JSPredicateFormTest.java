package umd.cs.shop.test;

import static org.junit.Assert.*;
import org.junit.Test;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;

public class JSPredicateFormTest {

 private static final String TEST_CASE_1 = "(! p (x, y))";
 private static final String TEST_CASE_2 = "((not p) (x, y))";
 private static final String TEST_CASE_3 = "(call p (x, y))";
 private static final String TEST_CASE_4 = "(p x y)";
 private static final String TEST_CASE_5 = "(p x (not y))";
 private static final String TEST_CASE_6 = "(not (p x y))";

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm pf = new JSPredicateForm();
 assertEquals(0, pf.size());
 }

 @Test
 public void testStringConstructor() {
 JSPredicateForm pf = new JSPredicateForm(TEST_CASE_1);
 assertEquals(1, pf.size());
 assertEquals("!", pf.elementAt(0).toString());
 }

 @Test
 public void testStreamTokenizerConstructor() {
 JSPredicateForm pf = new JSPredicateForm(new StreamTokenizer(new StringReader(TEST_CASE_1)));
 assertEquals(1, pf.size());
 assertEquals("!", pf.elementAt(0).toString());
 }

 @Test
 public void testJSPredicateFormInit() {
 JSPredicateForm tokenizedPF = new JSPredicateForm(new StreamTokenizer(new StringReader(TEST_CASE_1)));
 JSPredicateForm expectedPF = new JSPredicateForm("! p (x, y)");
 assertEquals(expectedPF, tokenizedPF);
 }

 @Test(expected = JSParserError.class)
 public void testUnexpectedCharacter() {
 JSPredicateForm pf = new JSPredicateForm(new StreamTokenizer(new StringReader("!( a b ) c")));
 fail("Unexpected JSParserError was not thrown");
 }

 @Test
 public void testPrint() {
 JSPredicateForm pf = new JSPredicateForm(TEST_CASE_1);
 String actualResult = pf.print();
 String expectedResult = "((! p) (x, y))";
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testToStr() {
 JSPredicateForm pf = new JSPredicateForm(TEST_CASE_1);
 String actualResult = pf.toStr().toString();
 String expectedResult = "(! p (x, y))";
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testClonePF() {
 JSPredicateForm pf = new JSPredicateForm(TEST_CASE_1);
 JSPredicateForm clonedPF = pf.clonePF();
 assertEquals(pf, clonedPF);
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm pf = new JSPredicateForm(TEST_CASE_4);
 JSSubstitution alpha = new JSSubstitution();
 alpha.add("x", new JSTerm("a"));
 JSPredicateForm expectedPF = new JSPredicateForm("(p a y)");
 JSPredicateForm actualPF = pf.applySubstitutionPF(alpha);
 assertEquals(expectedPF, actualPF);
 }

 @Test(expected = JSParserError.class)
 public void testMatchesFailureDifferentSize() {
 JSPredicateForm pf1 = new JSPredicateForm(TEST_CASE_4);
 JSPredicateForm pf2 = new JSPredicateForm(TEST_CASE_5);
 pf1.matches(pf2);
 fail("Unexpected JSParserError was not thrown");
 }

 @Test(expected = JSParserError.class)
 public void testMatchesFailureDifferentFunctor() {
 JSPredicateForm pf1 = new JSPredicateForm(TEST_CASE_4);
 JSPredicateForm pf2 = new JSPredicateForm(TEST_CASE_6);
 pf1.matches(pf2);
 fail("Unexpected JSParserError was not thrown");
 }

 @Test
 public void testMatchesSuccess() {
 JSPredicateForm pf1 = new JSPredicateForm(TEST_CASE_4);
 JSPredicateForm pf2 = new JSPredicateForm(TEST_CASE_5);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = pf1.matches(pf2, alpha);
 assertEquals(0, beta.size());
 }

 @Test
 public void testMatchesWithSubstitution() {
 JSPredicateForm pf1 = new JSPredicateForm(TEST_CASE_4);
 JSPredicateForm pf2 = new JSPredicateForm(TEST_CASE_5);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = pf1.matches(pf2, alpha);
 alpha.add("y", new JSTerm("b"));
 JSPredicateForm expectedPF = new JSPredicateForm("(p x (not b))");
 JSPredicateForm actualPF = new JSPredicateForm(beta.toString());
 assertEquals(expectedPF, actualPF);
 }

 @Test
 public void testEquals() {
 JSPredicateForm pf1 = new JSPredicateForm(TEST_CASE_4);
 JSPredicateForm pf2 = new JSPredicateForm(TEST_CASE_4);
 assertEquals(pf1, pf2);
 }

 @Test
 public void testEqualsDifferentObjects() {
 JSPredicateForm pf1 = new JSPredicateForm(TEST_CASE_4);
 JSPredicateForm pf2 = new JSPredicateForm(TEST_CASE_2);
 assertNotEquals(pf1, pf2);
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm pf = new JSPredicateForm(TEST_CASE_1);
 JSPredicateForm standardizedPF = pf.standarizerPredicateForm();
 String actualResult = standardizedPF.toStr().toString();
 String expectedResult = "(! p (x y))";
 assertEquals(expectedResult, actualResult);
 }
}