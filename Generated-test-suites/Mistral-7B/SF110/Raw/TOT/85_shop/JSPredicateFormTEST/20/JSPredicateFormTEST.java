import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

 @Test
 public void testEmptyJSPredicateForm() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertEquals(0, jspf.size());
 }

 @Test
 public void testJSPredicateFormWithText() {
 String text = "!(p1 & not (q1 v r1))";
 JSPredicateForm jspf = new JSPredicateForm(text);
 assertEquals(4, jspf.size());
 assertEquals("!", jspf.elementAt(0).toString());
 assertEquals("p1", jspf.elementAt(1).toString());
 assertEquals("not", jspf.elementAt(2).toString());
 assertEquals("(q1 v r1)", jspf.elementAt(3).toString());
 }

 @Test
 public void testJSPredicateFormWithTokenizer() {
 String text = "!(p1 & not (q1 v r1))";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 assertEquals(4, jspf.size());
 assertEquals("!", jspf.elementAt(0).toString());
 assertEquals("p1", jspf.elementAt(1).toString());
 assertEquals("not", jspf.elementAt(2).toString());
 assertEquals("(q1 v r1)", jspf.elementAt(3).toString());
 }

 @Test
 public void testJSPredicateFormInitWithInvalidText() {
 String text = "!%&^%$";
 try {
 JSPredicateForm jspf = new JSPredicateForm(text);
 fail("Expected JSParserError to be thrown");
 } catch (JSParserError e) {
 // expected
 }
 }

 @Test
 public void testJSPredicateFormInitWithInvalidTokenizer() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
 try {
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 fail("Expected JSParserError to be thrown");
 } catch (JSParserError e) {
 // expected
 }
 }

 @Test
 public void testJSPredicateFormPrint() {
 String text = "!(p1 & not (q1 v r1))";
 JSPredicateForm jspf = new JSPredicateForm(text);
 String expected = "((! p1) not ((q1) v (r1)))";
 assertEquals(expected, jspf.print());
 }

 @Test
 public void testJSPredicateFormToStr() {
 String text = "!(p1 & not (q1 v r1))";
 JSPredicateForm jspf = new JSPredicateForm(text);
 String expected = "((!p1)not((q1)v(r1)))";
 assertEquals(expected, jspf.toStr().toString());
 }

 @Test
 public void testJSPredicateFormClonePF() {
 String text = "!(p1 & not (q1 v r1))";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSPredicateForm cloned = jspf.clonePF();
 assertEquals(jspf, cloned);
 }

 @Test
 public void testJSPredicateFormApplySubstitutionPF() {
 String text = "!(p1 & not (q1 v r1))";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("p1", new JSTerm("a"));
 JSPredicateForm result = jspf.applySubstitutionPF(alpha);
 String expected = "(!a & not ((q1) v (r1)))";
 assertEquals(expected, result.print());
 }

 @Test
 public void testJSPredicateFormMatches() {
 String text1 = "!(p1 & not (q1 v r1))";
 String text2 = "!(p1 & not (q2 v r1))";
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = jspf1.matches(jspf2, alpha);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testJSPredicateFormMatchesWithBinding() {
 String text1 = "!(p1 & not (q1 v r1))";
 String text2 = "!(p2 & not (q1 v r1))";
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("p1", new JSTerm("a"));
 JSSubstitution beta = jspf1.matches(jspf2, alpha);
 assertFalse(beta.isEmpty());
 beta.print();
 }

 @Test
 public void testJSPredicateFormEquals() {
 String text1 = "!(p1 & not (q1 v r1))";
 String text2 = "!(p1 & not (q1 v r1))";
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 assertTrue(jspf1.equals(jspf2));
 }

 @Test
 public void testJSPredicateFormNotEquals() {
 String text1 = "!(p1 & not (q1 v r1))";
 String text2 = "!(p1 & not (q2 v r1))";
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testJSPredicateFormStandarizerPredicateForm() {
 String text = "!(p1 & not (q1 v r1))";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSPredicateForm standardized = jspf.standarizerPredicateForm();
 String expected = "(!p1)not((q1)v(r1))";
 assertEquals(expected, standardized.print());
 }

}