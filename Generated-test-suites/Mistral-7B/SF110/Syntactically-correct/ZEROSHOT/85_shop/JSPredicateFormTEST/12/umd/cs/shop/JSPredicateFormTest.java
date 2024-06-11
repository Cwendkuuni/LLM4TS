import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.io.StringReader;
import java.util.Vector;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 private String testData = "(\\p (or (not (and a b)) (and c d)))";
 private String testDataNot = "(! (and a b))";
 private String testDataCall = "((call f (a b c)))";
 private String testDataFunc = "((f x))";

 @Test
 public void testJSPredicateFormInit() {
 JSPredicateForm jspf = new JSPredicateForm(new StringReader(testData));
 Vector<String> expected = new Vector<>();
 expected.add("p");
 expected.add("or");
 expected.add("not");
 expected.add("and");
 expected.add("a");
 expected.add("b");
 expected.add("c");
 expected.add("d");
 assertEquals(expected, jspf);
 }

 @Test
 public void testJSPredicateFormInit_NullData() {
 assertThrows(JSParserError.class, () -> new JSPredicateForm(null));
 }

 @Test
 public void testJSPredicateFormInit_EmptyData() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertEquals(0, jspf.size());
 }

 @Test
 public void testJSPredicateFormInit_EmptyDataWithText() {
 JSPredicateForm jspf = new JSPredicateForm(" ");
 assertEquals(0, jspf.size());
 }

 @Test
 public void testJSPredicateFormInit_InvalidData() {
 assertThrows(JSParserError.class, () -> new JSPredicateForm(new StringReader("(a")));
 }

 @Test
 public void testJSPredicateFormInit_NotOperator() {
 JSPredicateForm jspf = new JSPredicateForm(new StringReader(testDataNot));
 Vector<String> expected = new Vector<>();
 expected.add("not");
 expected.add("and");
 expected.add("a");
 expected.add("b");
 assertEquals(expected, jspf);
 }

 @Test
 public void testJSPredicateFormInit_CallOperator() {
 JSPredicateForm jspf = new JSPredicateForm(new StringReader(testDataCall));
 Vector<String> expected = new Vector<>();
 expected.add("call");
 expected.add("f");
 expected.add("a");
 expected.add("b");
 expected.add("c");
 assertEquals(expected, jspf);
 }

 @Test
 public void testJSPredicateFormInit_FuncOperator() {
 JSPredicateForm jspf = new JSPredicateForm(new StringReader(testDataFunc));
 Vector<String> expected = new Vector<>();
 expected.add("f");
 expected.add("x");
 assertEquals(expected, jspf);
 }

 @Test
 public void testPrint() {
 JSPredicateForm jspf = new JSPredicateForm(new StringReader(testData));
 jspf.print();
 }

 @Test
 public void testToString() {
 JSPredicateForm jspf = new JSPredicateForm(new StringReader(testData));
 String expected = "((p (or (not (and a b)) (and c d))))";
 assertEquals(expected, jspf.toStr().toString());
 }

 @Test
 public void testClonePF() {
 JSPredicateForm jspf = new JSPredicateForm(new StringReader(testData));
 JSPredicateForm cloned = jspf.clonePF();
 assertEquals(jspf, cloned);
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm jspf = new JSPredicateForm(new StringReader(testData));
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = jspf.applySubstitutionPF(alpha);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testMatches_Success() {
 JSPredicateForm jspf = new JSPredicateForm(new StringReader(testData));
 JSPredicateForm t = new JSPredicateForm(new StringReader(testData));
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = jspf.matches(t, alpha);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testMatches_Failure() {
 JSPredicateForm jspf = new JSPredicateForm(new StringReader("(p a)"));
 JSPredicateForm t = new JSPredicateForm(new StringReader(testData));
 assertFalse(jspf.matches(t, new JSSubstitution()).isEmpty());
 }

 @Test
 public void testEquals() {
 JSPredicateForm jspf1 = new JSPredicateForm(new StringReader(testData));
 JSPredicateForm jspf2 = new JSPredicateForm(new StringReader(testData));
 assertTrue(jspf1.equals(jspf2));
 JSPredicateForm jspf3 = new JSPredicateForm(new StringReader("(p a)"));
 assertFalse(jspf1.equals(jspf3));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm jspf = new JSPredicateForm(new StringReader(testData));
 JSPredicateForm standardized = jspf.standarizerPredicateForm();
 Vector<String> expected = new Vector<>();
 expected.add("p");
 expected.add("or");
 expected.add("not");
 expected.add("and");
 expected.add("a");
 expected.add("b");
 expected.add("c");
 expected.add("d");
 assertEquals(expected, standardized);
 }
}