import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

 @Test
 public void testEmptyFormInit() {
 JSPredicateForm form = new JSPredicateForm();
 assertEquals(0, form.size());
 }

 @Test
 public void testFormInitWithText() {
 String text = "!(not (or (and x y) z) )";
 JSPredicateForm form = new JSPredicateForm(text);
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("!");
 expected.addElement("not");
 expected.addElement("(");
 expected.addElement("and");
 expected.addElement("x");
 expected.addElement("y");
 expected.addElement(")");
 expected.addElement("z");
 assertEquals(expected, form);
 }

 @Test
 public void testFormInitWithTokenizer() {
 String text = "(and (or x y) z)";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm form = new JSPredicateForm(tokenizer);
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("and");
 expected.addElement("(");
 expected.addElement("or");
 expected.addElement("x");
 expected.addElement("y");
 expected.addElement(")");
 expected.addElement("z");
 expected.addElement(")");
 assertEquals(expected, form);
 }

 @Test(expected = JSParserError.class)
 public void testUnexpectedCharacterDuringParsing() {
 String text = "(and (or x y) z)%";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 new JSPredicateForm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testExpectedLeftParenthesis() {
 String text = "!not";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 new JSPredicateForm(tokenizer);
 }

 @Test
 public void testPrintMethod() {
 JSPredicateForm form = new JSPredicateForm("!(not (or (and x y) z) )");
 form.print();
 assertEquals("(!(not (or (and x y) z) ) )", System.getProperty("line.separator") + System.getProperty("line.separator") + System.console().readLine());
 }

 @Test
 public void testToStrMethod() {
 JSPredicateForm form = new JSPredicateForm("!(not (or (and x y) z) )");
 assertEquals("(!(not (or (and x y) z) ) )", form.toStr().toString());
 }

 @Test
 public void testClonePFMethod() {
 JSPredicateForm form = new JSPredicateForm("!(not (or (and x y) z) )");
 JSPredicateForm cloned = form.clonePF();
 assertEquals(form, cloned);
 }

 @Test
 public void testApplySubstitutionPFMethod() {
 JSPredicateForm form = new JSPredicateForm("!(not (or (and x y) z) )");
 JSSubstitution alpha = new JSSubstitution();
 JSPredicateForm result = form.applySubstitutionPF(alpha);
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("!");
 expected.addElement("not");
 expected.addElement("(");
 expected.addElement("and");
 expected.addElement("x'"); // ' is used to represent a variable substituted by alpha
 expected.addElement("y'");
 expected.addElement(")");
 expected.addElement("z");
 expected.addElement(")");
 assertEquals(expected, result);
 }

 @Test
 public void testMatchesMethodWithEmptySubstitution() {
 JSPredicateForm form1 = new JSPredicateForm("!(not (or (and x y) z) )");
 JSPredicateForm form2 = new JSPredicateForm("(and (or x y) w)");
 JSSubstitution beta = form1.matches(form2, new JSSubstitution());
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testMatchesMethodWithSubstitution() {
 JSPredicateForm form1 = new JSPredicateForm("!(not (or (and x y) z) )");
 JSPredicateForm form2 = new JSPredicateForm("!(not (or (and x' y) z) )");
 JSSubstitution beta = form1.matches(form2, new JSSubstitution());
 assertEquals("x'", beta.get("x"));
 }

 @Test
 public void testMatchesMethodWithFailure() {
 JSPredicateForm form1 = new JSPredicateForm("!(not (or (and x y) z) )");
 JSPredicateForm form2 = new JSPredicateForm("!(not (or (and x y) w) )");
 JSSubstitution beta = form1.matches(form2, new JSSubstitution());
 assertFalse(beta.isEmpty());
 }

 @Test
 public void testEqualsMethod() {
 JSPredicateForm form1 = new JSPredicateForm("!(not (or (and x y) z) )");
 JSPredicateForm form2 = new JSPredicateForm("!(not (or (and x y) z) )");
 assertEquals(form1, form2);
 }

 @Test
 public void testEqualsMethodWithDifferentFormSize() {
 JSPredicateForm form1 = new JSPredicateForm("!(not (or (and x y) z) )");
 JSPredicateForm form2 = new JSPredicateForm("!(not (or (and x y) z) a) ");
 assertFalse(form1.equals(form2));
 }

 @Test
 public void testEqualsMethodWithDifferentFormFunctor() {
 JSPredicateForm form1 = new JSPredicateForm("!(not (or (and x y) z) )");
 JSPredicateForm form2 = new JSPredicateForm("and(or x y z) ");
 assertFalse(form1.equals(form2));
 }

 @Test
 public void testEqualsMethodWithDifferentFormElements() {
 JSPredicateForm form1 = new JSPredicateForm("!(not (or (and x y) z) )");
 JSTerm term1 = new JSTerm("x");
 JSTerm term2 = new JSTerm("w");
 JSPredicateForm form2 = new JSPredicateForm();
 form2.addElement("!");
 form2.addElement("not");
 form2.addElement("(");
 form2.addElement("and");
 form2.addElement(term1);
 form2.addElement(term2);
 form2.addElement(")");
 form2.addElement("z");
 form2.addElement(")");
 assertFalse(form1.equals(form2));
 }

 @Test
 public void testStandarizerPredicateFormMethod() {
 JSPredicateForm form = new JSPredicateForm("!(not (or (and x y) z) )");
 JSPredicateForm standardized = form.standarizerPredicateForm();
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("!");
 expected.addElement("not");
 expected.addElement("(");
 expected.addElement("and");
 expected.addElement("x");
 expected.addElement("y");
 expected.addElement(")");
 expected.addElement("z");
 expected.addElement(")");
 assertEquals(expected, standardized);
 }

}