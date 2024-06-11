import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.Vector;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSParserError;

public class JSPredicateFormTest {

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm jsf = new JSPredicateForm();
 assertEquals(0, jsf.size());
 }

 @Test
 public void testStringConstructor() {
 String text = "!P(x)";
 JSPredicateForm jsf = new JSPredicateForm(text);
 assertEquals("!P(x)", jsf.toStr().toString());
 }

 @Test
 public void testStreamTokenizerConstructor() {
 String text = "!P(x)";
 StringReader reader = new StringReader(text);
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm jsf = new JSPredicateForm(tokenizer);
 assertEquals("!P(x)", jsf.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testInvalidInput() {
 String text = "!P)";
 StringReader reader = new StringReader(text);
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm jsf = new JSPredicateForm(tokenizer);
 }

 @Test
 public void testPrintMethod() {
 JSPredicateForm jsf = new JSPredicateForm("!P(x)");
 jsf.print();
 }

 @Test
 public void testToStrMethod() {
 JSPredicateForm jsf = new JSPredicateForm("!P(x)");
 assertEquals("(!P(x))", jsf.toStr().toString());
 }

 @Test
 public void testClonePFMethod() {
 JSPredicateForm jsf = new JSPredicateForm("!P(x)");
 JSPredicateForm cloned = jsf.clonePF();
 assertEquals("(!P(x))", cloned.toStr().toString());
 }

 @Test
 public void testApplySubstitutionPFMethod() {
 JSSubstitution alpha = new JSSubstitution();
 JSTerm term1 = new JSTerm("x", "y");
 JSTerm term2 = new JSTerm("y", "z");
 JSPredicateForm jsf = new JSPredicateForm("P(x)");
 JSPredicateForm result = jsf.applySubstitutionPF(alpha);
 assertEquals("P(y)", result.toStr().toString());
 }

 @Test
 public void testMatchesMethod() {
 JSSubstitution alpha = new JSSubstitution();
 JSTerm term1 = new JSTerm("x", "y");
 JSTerm term2 = new JSTerm("y", "z");
 JSPredicateForm jsf1 = new JSPredicateForm("P(x)");
 JSPredicateForm jsf2 = new JSPredicateForm("P(y)");
 assertEquals(jsf1.matches(jsf2, new JSSubstitution()), jsf1.matches(jsf2, alpha));
 }

 @Test
 public void testEqualsMethod() {
 JSPredicateForm jsf1 = new JSPredicateForm("P(x)");
 JSPredicateForm jsf2 = new JSPredicateForm("P(x)");
 JSPredicateForm jsf3 = new JSPredicateForm("Q(x)");
 assertTrue(jsf1.equals(jsf2));
 assertFalse(jsf1.equals(jsf3));
 }

 @Test
 public void testStandarizerPredicateFormMethod() {
 JSPredicateForm jsf = new JSPredicateForm("!(not P(x) or Q(y))");
 JSPredicateForm result = jsf.standarizerPredicateForm();
 assertEquals("(not (or (not P(x)) Q(y)))", result.toStr().toString());
 }
}