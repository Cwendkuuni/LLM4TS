import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.*;

public class JSPredicateFormTest {

 @Test
 public void testJSPredicateFormEmptyConstructor() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertEquals(0, jspf.size());
 }

 @Test
 public void testJSPredicateFormStringConstructor() {
 String text = "(! p1)";
 JSPredicateForm jspf = new JSPredicateForm(text);
 assertEquals(2, jspf.size());
 assertEquals("!", jspf.get(0).toString());
 assertEquals("p1", jspf.get(1).toString());
 }

 @Test
 public void testJSPredicateFormStreamTokenizerConstructor() {
 String text = "(! p1)";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 assertEquals(2, jspf.size());
 assertEquals("!", jspf.get(0).toString());
 assertEquals("p1", jspf.get(1).toString());
 }

 @Test
 public void testJSPredicateFormInit() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(! p1)"));
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.JSPredicateFormInit(tokenizer);
 assertEquals(2, jspf.size());
 assertEquals("!", jspf.get(0).toString());
 assertEquals("p1", jspf.get(1).toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitInvalidInput() {
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(p1)"));
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.JSPredicateFormInit(tokenizer);
 fail("JSParserError expected");
 }

 @Test
 public void testPrint() {
 JSPredicateForm jspf = new JSPredicateForm("(! p1)");
 jspf.print();
 assertEquals("( ! p1 )", JUnitUtils.getStringFromSystemOut());
 }

 @Test
 public void testToString() {
 JSPredicateForm jspf = new JSPredicateForm("(! p1)");
 assertEquals("( ! p1 )", jspf.toStr().toString());
 }

 @Test
 public void testClonePF() {
 JSPredicateForm jspf = new JSPredicateForm("(! p1)");
 JSPredicateForm cloned = jspf.clonePF();
 assertEquals(2, cloned.size());
 assertEquals("!", cloned.get(0).toString());
 assertEquals("p1", cloned.get(1).toString());
 }

 @Test
 public void testApplySubstitutionPF() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSPredicateForm jspf1 = new JSPredicateForm("(p1 x)");
 JSPredicateForm jspf2 = jspf1.applySubstitutionPF(new JSSubstitution().put("x", term2));
 assertEquals("(p1 y)", jspf2.toStr().toString());
 }

 @Test
 public void testMatches() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSPredicateForm jspf1 = new JSPredicateForm("(p1 x)");
 JSPredicateForm jspf2 = new JSPredicateForm("(p1 y)");
 JSSubstitution substitution = jspf1.matches(jspf2);
 assertTrue(substitution.isEmpty());
 }

 @Test
 public void testMatchesFailure() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSTerm term3 = new JSTerm("z");
 JSPredicateForm jspf1 = new JSPredicateForm("(p1 x)");
 JSPredicateForm jspf2 = new JSPredicateForm("(p1 y z)");
 JSSubstitution substitution = jspf1.matches(jspf2);
 assertFalse(substitution.isEmpty());
 }

 @Test
 public void testEquals() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSPredicateForm jspf1 = new JSPredicateForm("(p1 x)");
 JSPredicateForm jspf2 = new JSPredicateForm("(p1 y)");JSPredicateForm jspf3 = new JSPredicateForm("(p1 x)");
 assertTrue(jspf1.equals(jspf3));
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("y");
 JSPredicateForm jspf1 = new JSPredicateForm("(p1 (not x))");
 JSPredicateForm jspf2 = jspf1.standarizerPredicateForm();
 assertEquals("(not p1 x)", jspf2.toStr().toString());
 }
}