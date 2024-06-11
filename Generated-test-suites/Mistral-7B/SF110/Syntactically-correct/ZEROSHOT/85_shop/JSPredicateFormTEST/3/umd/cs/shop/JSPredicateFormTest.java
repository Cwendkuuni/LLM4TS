import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.Vector;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 @Test
 public void testEmptyFormConstruction() {
 JSPredicateForm emptyForm = new JSPredicateForm();
 assertEquals(0, emptyForm.size());
 }

 @Test
 public void testEmptyFormWithTextConstruction() {
 JSPredicateForm emptyForm = new JSPredicateForm("");
 assertEquals(0, emptyForm.size());
 }

 @Test
 public void testFormWithTextConstruction() {
 String text = "!p(x)";
 JSPredicateForm form = new JSPredicateForm(text);
 assertEquals(1, form.size());
 assertEquals("!p", form.elementAt(0).toString());
 }

 @Test
 public void testFormWithTokenizerConstruction() {
 String text = "!p(x)";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm form = new JSPredicateForm(tokenizer);
 assertEquals(1, form.size());
 assertEquals("!p", form.elementAt(0).toString());
 }

 @Test
 public void testNestedFormConstruction() {
 String text = "!not(p(x) and q(y))";
 JSPredicateForm form = new JSPredicateForm(text);
 assertEquals(3, form.size());
 assertEquals("!not", form.elementAt(0).toString());
 assertNotNull(form.elementAt(1));
 assertEquals(2, form.elementAt(1).size());
 assertEquals("p", form.elementAt(1).elementAt(0).toString());
 assertEquals("x", ((JSTerm) form.elementAt(1).elementAt(1)).getName());
 assertNotNull(form.elementAt(2));
 assertEquals("q", form.elementAt(2).elementAt(0).toString());
 assertEquals("y", ((JSTerm) form.elementAt(2).elementAt(0)).getName());
 }

 @Test
 public void testFormWithCall() {
 String text = "!p(x) or call(f(x))";
 JSPredicateForm form = new JSPredicateForm(text);
 assertEquals(3, form.size());
 assertEquals("!", form.elementAt(0).toString());
 assertEquals("p", form.elementAt(1).toString());
 assertEquals("x", form.elementAt(1).size() > 0 ? ((JSTerm) form.elementAt(1).elementAt(0)).getName() : null);
 assertEquals("call", form.elementAt(2).toString());
 assertNull(form.elementAt(2).size());
 }

 @Test
 public void testFormPrinting() {
 String text = "!(p(x) and q(y))";
 JSPredicateForm form = new JSPredicateForm(text);
 form.print();
 String expected = "(! (p(x) and q(y) ) )";
 assertEquals(expected, JSUtil.getOutput());
 }

 @Test
 public void testFormToString() {
 String text = "!(p(x) and q(y))";
 JSPredicateForm form = new JSPredicateForm(text);
 String actual = form.toStr().toString();
 String expected = text;
 assertEquals(expected, actual);
 }

 @Test
 public void testFormCloning() {
 String text = "!(p(x) and q(y))";
 JSPredicateForm original = new JSPredicateForm(text);
 JSPredicateForm clone = original.clonePF();
 assertEquals(original, clone);
 }

 @Test
 public void testFormSubstitution() {
 String text = "p(x)";
 String variable = "z";
 JSPredicateForm form = new JSPredicateForm(text);
 JSSubstitution substitution = new JSSubstitution();
 substitution.addBinding(variable, new JSTerm("a"));
 JSPredicateForm substitutedForm = form.applySubstitutionPF(substitution);
 assertEquals(1, substitutedForm.size());
 assertEquals("p", substitutedForm.elementAt(0).toString());
 assertEquals("z", substitutedForm.elementAt(0).size() > 0 ? ((JSTerm) substitutedForm.elementAt(0).elementAt(0)).getName() : null);
 assertEquals("a", substitutedForm.elementAt(0).elementAt(1).toString());
 }

 @Test
 public void testFormEquality() {
 String text1 = "p(x)";
 String text2 = "p(x)";
 JSPredicateForm form1 = new JSPredicateForm(text1);
 JSPredicateForm form2 = new JSPredicateForm(text2);
 assertTrue(form1.equals(form2));
 }

 @Test
 public void testFormInequality() {
 String text1 = "p(x)";
 String text2 = "q(x)";
 JSPredicateForm form1 = new JSPredicateForm(text1);
 JSPredicateForm form2 = new JSPredicateForm(text2);
 assertFalse(form1.equals(form2));
 }

 @Test
 public void testFormStandarizer() {
 String text = "!(p(x) and q(y))";
 JSPredicateForm form = new JSPredicateForm(text);
 JSPredicateForm standardizedForm = form.standarizerPredicateForm();
 String expected = "! (not p(x) or not q(y))";
 assertEquals(expected, standardizedForm.toStr().toString());
 }
}