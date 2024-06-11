import static org.junit.Assert.*;
import static umd.cs.shop.JSJshopVars.*;//assuming JSJshopVars is a static class with constants
import umd.cs.shop.*;
import org.junit.Test;

public class JSPredicateFormTest {

 private StreamTokenizer createTokenizer(String text) {
 return new StreamTokenizer(new StringReader(text));
 }

 @Test
 public void testJSPredicateFormInit() {
 // Testing constructor with null text
 try {
 new JSPredicateForm(null);
 fail("Expected JSParserError");
 } catch (JSParserError e) {
 // Expected
 }

 // Testing constructor with empty text
 JSPredicateForm form = new JSPredicateForm("");
 assertEquals(0, form.size());

 // Testing constructor with valid text
 String text = "not (p)";
 JSPredicateForm expectedForm = new JSPredicateForm("not");
 expectedForm.addElement("p");
 JSPredicateForm formFromText = new JSPredicateForm(text);
 assertEquals(expectedForm, formFromText);

 // Testing constructor with valid text from StreamTokenizer
 StreamTokenizer tokenizer = createTokenizer(text);
 JSPredicateForm formFromTokenizer = new JSPredicateForm(tokenizer);
 assertEquals(expectedForm, formFromTokenizer);
 }

 @Test
 public void testPrint() {
 JSPredicateForm form = new JSPredicateForm("not (p & q)");
 form.print();
 String expectedOutput = "([not (p)] & [q])) ";
 JSUtil.println(expectedOutput);
 assertEquals(expectedOutput, JSUtil.getPrintBuffer().toString());
 }

 @Test
 public void testToString() {
 JSPredicateForm form = new JSPredicateForm("not (p & q)");
 String expectedOutput = "([not (p)] & [q]))";
 assertEquals(expectedOutput, form.toStr().toString());
 }

 @Test
 public void testClonePF() {
 JSPredicateForm form = new JSPredicateForm("not (p & q)");
 JSPredicateForm clonedForm = form.clonePF();
 assertEquals(form, clonedForm);
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm form = new JSPredicateForm("not (p & q)");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("p", new JSTerm("R(X)"));
 JSPredicateForm expectedResult = new JSPredicateForm("not ((R(X) & q))");
 JSPredicateForm result = form.applySubstitutionPF(alpha);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testMatches() {
 JSPredicateForm form1 = new JSPredicateForm("not (p & q)");
 JSPredicateForm form2 = new JSPredicateForm("not (r & q)");
 JSSubstitution beta = form1.matches(form2);
 beta.print();
 String expectedOutput = "{}";
 assertEquals(expectedOutput, beta.toString());

 form1 = new JSPredicateForm("not (p & q)");
 form2 = new JSPredicateForm("not (p & r)");
 beta = form1.matches(form2);
 JSSubstitution expectedAlpha = new JSSubstitution();
 expectedAlpha.addBinding("r", new JSTerm("q"));
 beta.addElements(expectedAlpha);
 beta.print();
 String expectedOutput2 = "{{r=[q]}}";
 assertEquals(expectedOutput2, beta.toString());

 form1 = new JSPredicateForm("not ((p & r) & q)");
 form2 = new JSPredicateForm("not (p & q)");
 beta = form1.matches(form2);
 beta.print();
 String expectedOutput3 = "{{}}";
 assertEquals(expectedOutput3, beta.toString());
 }

 @Test
 public void testEquals() {
 JSPredicateForm form1 = new JSPredicateForm("not (p & q)");
 JSPredicateForm form2 = new JSPredicateForm("not (p & q)");
 JSPredicateForm form3 = new JSPredicateForm("not (p & r)");
 assertTrue(form1.equals(form2));
 assertFalse(form1.equals(form3));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm form = new JSPredicateForm("not ((p & r) & q)");
 JSPredicateForm expectedForm = new JSPredicateForm("not (p & (r & q))");
 JSPredicateForm result = form.standarizerPredicateForm();
 assertEquals(expectedForm, result);
 }
}