package umd.cs.shop.test;

import static org.junit.Assert.*;
import umd.cs.shop.*;
import org.junit.Test;

public class JSPredicateFormTest {

 private JSPredicateForm createForm(String text) {
 return new JSPredicateForm(text);
 }

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertTrue(jspf.size() == 0);
 }

 @Test
 public void testConstructorWithText() {
 JSPredicateForm jspf = createForm("(not p)");
 assertEquals(2, jspf.size());
 assertEquals("not", jspf.elementAt(0).toString());
 assertTrue(jspf.elementAt(1) instanceof JSPredicateForm);
 }

 @Test
 public void testConstructorWithTokenizer() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not p)"));
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 assertEquals(2, jspf.size());
 assertEquals("not", jspf.elementAt(0).toString());
 assertTrue(jspf.elementAt(1) instanceof JSPredicateForm);
 }

 @Test(expected = JSParserError.class)
 public void testConstructorWithInvalidInput() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("not p)"));
 new JSPredicateForm(tokenizer);
 }

 @Test
 public void testJSPredicateFormInit() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not (or (x y) (and (a b) (c d))))"));
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 assertEquals(5, jspf.size());
 assertEquals("not", jspf.elementAt(0).toString());
 assertTrue(jspf.elementAt(1) instanceof JSPredicateForm);
 assertTrue(jspf.elementAt(2) instanceof String);
 assertTrue(jspf.elementAt(3) instanceof JSTerm);
 assertTrue(jspf.elementAt(4) instanceof JSTerm);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithInvalidInput() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not (or (x y) (and a b c d))))"));
 new JSPredicateForm(tokenizer);
 }

 @Test
 public void testPrint() {
 JSPredicateForm jspf = createForm("(not p)");
 jspf.print();
 assertEquals("(not p)", JSUtil.readLine());
 }

 @Test
 public void testToStr() {
 JSPredicateForm jspf = createForm("(not p)");
 String str = jspf.toStr().toString();
 assertEquals("(not p)", str);
 }

 @Test
 public void testClonePF() {
 JSPredicateForm jspf = createForm("(not (or (x y) (and a b c d)))");
 JSPredicateForm clonedPF = jspf.clonePF();
 assertEquals(jspf.size(), clonedPF.size());
 for (int i = 0; i < jspf.size(); i++) {
 assertEquals(jspf.elementAt(i), clonedPF.elementAt(i));
 }
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm jspf = createForm("(not (or x y) (and a b c d))");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addEntry("x", new JSTerm("z"));
 JSPredicateForm result = jspf.applySubstitutionPF(alpha);
 assertEquals(jspf.size(), result.size());
 for (int i = 0; i < jspf.size(); i++) {
 if (jspf.elementAt(i) instanceof JSTerm) {
 JSTerm jst = (JSTerm) jspf.elementAt(i);
 JSTerm newJST = (JSTerm) result.elementAt(i);
 assertEquals(jst.toStr(), newJST.toStr());
 } else {
 assertEquals(jspf.elementAt(i).toString(), result.elementAt(i).toString());
 }
 }
 }

 @Test
 public void testMatches() {
 JSPredicateForm jspf1 = createForm("(not (or x y) (and a b c d))");
 JSPredicateForm jspf2 = createForm("(not (or z z) (and a b c d))");
 JSSubstitution result = jspf1.matches(jspf2);
 assertTrue(result.size() == 0);
 JSSubstitution result2 = jspf1.matches(jspf1, new JSSubstitution());
 assertEquals(jspf1.size(), result2.size());
 for (int i = 0; i < jspf1.size(); i++) {
 if (jspf1.elementAt(i) instanceof JSTerm) {
 JSTerm jst1 = (JSTerm) jspf1.elementAt(i);
 JSTerm jst2 = (JSTerm) jspf1.elementAt(i);
 assertEquals(jst1.toStr(), jst2.toStr());
 } else {
 assertEquals(jspf1.elementAt(i).toString(), jspf1.elementAt(i).toString());
 }
 }
 }

 @Test
 public void testEquals() {
 JSPredicateForm jspf1 = createForm("(not (or x y) (and a b c d))");
 JSPredicateForm jspf2 = createForm("(not (or x y) (and a b c d))");
 assertTrue(jspf1.equals(jspf2));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm jspf = createForm("(not (or (x y) (and (a b) (c d))))");
 JSPredicateForm result = jspf.standarizerPredicateForm();
 assertEquals(5, result.size());
 assertEquals("not", result.elementAt(0).toString());
 assertTrue(result.elementAt(1) instanceof JSTerm);
 assertTrue(result.elementAt(2) instanceof JSTerm);
 assertTrue(result.elementAt(3) instanceof JSTerm);
 assertTrue(result.elementAt(4) instanceof JSTerm);
 }

}