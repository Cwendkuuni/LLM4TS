import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

 @Test
 public void testJSPredicateFormEmpty() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertEquals("", jspf.toString());
 }

 @Test
 public void testJSPredicateFormInitWithText() {
 String text = "(! (and (or x y) z) (or (not x) y) z) )";
 JSPredicateForm jspf = new JSPredicateForm(text);
 String expected = "(! (and (or x y) z) (or (not x) y) z) )";
 assertEquals(expected, jspf.toString());
 }

 @Test
 public void testJSPredicateFormInitWithTokenizer() {
 String text = "(! (and (or x y) z) (or (not x) y) z) )";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 String expected = "(! (and (or x y) z) (or (not x) y) z) )";
 assertEquals(expected, jspf.toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithNullText() {
 JSPredicateForm jspf = new JSPredicateForm(null);
 fail("Expected JSParserError but got none.");
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithInvalidText() {
 String text = "(! ((()";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm jspf = null;
 try {
 jspf = new JSPredicateForm(tokenizer);
 } catch (JSParserError e) {
 // expected
 }
 assertEquals("Unexpected (", e.getMessage());
 }

 @Test
 public void testPrint() {
 String text = "(! (and (or x y) z) (or (not x) y) z) )";
 JSPredicateForm jspf = new JSPredicateForm(text);
 String expected = "(! (and (or x y) z) (or (not x) y) z) )";
 jspf.print();
 assertEquals(expected, JSUtil.getLastPrintedString());
 }

 @Test
 public void testToStr() {
 String text = "(! (and (or x y) z) (or (not x) y) z) )";
 JSPredicateForm jspf = new JSPredicateForm(text);
 String expected = "(! (and (or x y) z) (or (not x) y) z) )";
 String actual = jspf.toStr().toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testClonePF() {
 String text = "(! (and (or x y) z) (or (not x) y) z) )";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSPredicateForm cloned = jspf.clonePF();
 String expected = "(! (and (or x y) z) (or (not x) y) z) )";
 assertEquals(expected, cloned.toString());
 }

 @Test
 public void testApplySubstitutionPF() {
 String text = "(! (and (or x (f x)) z) (or (not x) y) z) )";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSSubstitution alpha = new JSSubstitution();
 alpha.add("f", new JSTerm("g"));
 JSPredicateForm result = jspf.applySubstitutionPF(alpha);
 String expected = "(! (and (or x g z) z) (or (not x) y) z) )";
 assertEquals(expected, result.toString());
 }

 @Test
 public void testMatches() {
 String text1 = "(! (and (or x y) z) (or (not x) y) z) )";
 String text2 = "(! (and (or a b) c) (or (not a) d) c) )";
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 JSSubstitution beta = jspf1.matches(jspf2);
 assertEquals(0, beta.size());
 }

 @Test
 public void testMatchesWithAlpha() {
 String text1 = "(! (and (or x (f x)) z) (or (not x) y) z) )";
 String text2 = "(! (and (or a b) c) (or (not a) d) c) )";
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 JSSubstitution alpha = new JSSubstitution();
 alpha.add("f", new JSTerm("g"));
 JSSubstitution beta = jspf1.matches(jspf2, alpha);
 String expected = "g z";
 assertEquals(expected, beta.toString());
 }

 @Test
 public void testEquals() {
 String text1 = "(! (and (or x y) z) (or (not x) y) z) )";
 String text2 = "(! (and (or x y) z) (or (not x) y) z) )";
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 assertTrue(jspf1.equals(jspf2));
 }

 @Test
 public void testNotEquals() {
 String text1 = "(! (and (or x y) z) (or (not x) y) z) )";
 String text2 = "(! (and (or x y) z) (or (not y) y) z) )";
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testStandarizerPredicateForm() {
 String text = "(! (and (or x y) z) (or (not x) y) z) )";
 JSPredicateForm jspf = new JSPredicateForm(text);
 String expected = "(and (or x y) z) (or (not y) y) z)";
 JSPredicateForm result = jspf.standarizerPredicateForm();
 assertEquals(expected, result.toString());
 }
}