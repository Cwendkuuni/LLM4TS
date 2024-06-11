package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;

public class JSPredicateFormTest {

 private static final String VALID_JSPREDICATE_FORM = "((x < 5) or (not (y > 10)))";
 private static final String INVALID_JSPREDICATE_FORM_MISSING_LEFT_PAREN = "x < 5 or (y > 10)";
 private static final String INVALID_JSPREDICATE_FORM_MISSING_RIGHT_PAREN = "((x < 5) or y > 10)";
 private static final String INVALID_JSPREDICATE_FORM_INVALID_OPERATOR = "((x < 5) xor y > 10)";

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertTrue(jspf.size() == 0);
 }

 @Test
 public void testStringConstructorValid() {
 JSPredicateForm jspf = new JSPredicateForm(VALID_JSPREDICATE_FORM);
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("(");
 expected.addElement("x < 5");
 expected.addElement("or");
 expected.addElement("not");
 expected.addElement("y");
 expected.addElement(">");
 expected.addElement("10");
 expected.addElement(")");
 JSSubstitution beta = jspf.matches(expected);
 assertTrue(beta.isEmpty());
 }

 @Test(expected = JSParserError.class)
 public void testStringConstructorInvalidFormatMissingLeftParen() {
 new JSPredicateForm(INVALID_JSPREDICATE_FORM_MISSING_LEFT_PAREN);
 }

 @Test(expected = JSParserError.class)
 public void testStringConstructorInvalidFormatMissingRightParen() {
 new JSPredicateForm(INVALID_JSPREDICATE_FORM_MISSING_RIGHT_PAREN);
 }

 @Test(expected = JSParserError.class)
 public void testStringConstructorInvalidFormatInvalidOperator() {
 new JSPredicateForm(INVALID_JSPREDICATE_FORM_INVALID_OPERATOR);
 }

 @Test
 public void testStreamTokenizerConstructorValid() {
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(VALID_JSPREDICATE_FORM)));
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("(");
 expected.addElement("x < 5");
 expected.addElement("or");
 expected.addElement("not");
 expected.addElement("y");
 expected.addElement(">");
 expected.addElement("10");
 expected.addElement(")");
 JSSubstitution beta = jspf.matches(expected);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testPrint() {
 JSPredicateForm jspf = new JSPredicateForm(VALID_JSPREDICATE_FORM);
 final String expected = "((x < 5) or (not (y > 10)))";
 assertEquals(expected, jspf.toStr().toString());
 }

 @Test
 public void testClonePF() {
 JSPredicateForm jspf = new JSPredicateForm(VALID_JSPREDICATE_FORM);
 JSPredicateForm cloned = jspf.clonePF();
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("(");
 expected.addElement("x < 5");
 expected.addElement("or");
 expected.addElement("not");
 expected.addElement("y");
 expected.addElement(">");
 expected.addElement("10");
 expected.addElement(")");
 JSSubstitution beta = jspf.matches(expected);
 assertTrue(beta.isEmpty());
 assertEquals(jspf, cloned);
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm jspf = new JSPredicateForm(VALID_JSPREDICATE_FORM);
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = jspf.applySubstitutionPF(alpha);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testMatchesValid() {
 JSPredicateForm jspf = new JSPredicateForm(VALID_JSPREDICATE_FORM);
 JSPredicateForm t = new JSPredicateForm(VALID_JSPREDICATE_FORM);
 JSSubstitution beta = jspf.matches(t);
 assertTrue(beta.isEmpty());
 }

 @Test
 public void testMatchesInvalidSize() {
 JSPredicateForm jspf = new JSPredicateForm(VALID_JSPREDICATE_FORM);
 JSPredicateForm t = new JSPredicateForm();
 JSSubstitution beta = jspf.matches(t);
 assertFalse(beta.isEmpty());
 assertTrue(beta.getSize() > 0);
 }

 @Test
 public void testMatchesDifferentFunctors() {
 JSPredicateForm jspf = new JSPredicateForm("(x < 5) or (y > 10)");
 JSPredicateForm t = new JSPredicateForm("(z > 5) or (c < 10)");
 JSSubstitution beta = jspf.matches(t);
 assertFalse(beta.isEmpty());
 }

 @Test
 public void testMatchesMismatchedTerms() {
 JSPredicateForm jspf = new JSPredicateForm("(x < 5) or (y > 10)");
 JSTerm t = new JSTerm("x + 5");
 JSSubstitution beta = jspf.matches(t);
 assertFalse(beta.isEmpty());
 }

 @Test
 public void testEqualsValid() {
 JSPredicateForm jspf1 = new JSPredicateForm(VALID_JSPREDICATE_FORM);
 JSPredicateForm jspf2 = new JSPredicateForm(VALID_JSPREDICATE_FORM);
 assertTrue(jspf1.equals(jspf2));
 }

 @Test
 public void testEqualsInvalid() {
 JSPredicateForm jspf1 = new JSPredicateForm(VALID_JSPREDICATE_FORM);
 JSPredicateForm jspf2 = new JSPredicateForm("((x < 5) or y > 10)");
 assertFalse(jspf1.equals(jspf2));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm jspf = new JSPredicateForm(VALID_JSPREDICATE_FORM);
 JSPredicateForm standardized = jspf.standarizerPredicateForm();
 JSPredicateForm expected = new JSPredicateForm();
 expected.addElement("(");
 expected.addElement("not");
 expected.addElement("y");
 expected.addElement(">");
 expected.addElement("10");
 expected.addElement("or");
 expected.addElement("x");
 expected.addElement("<");
 expected.addElement("5");
 expected.addElement(")");
 JSSubstitution beta = jspf.matches(expected);
 assertTrue(beta.isEmpty());
 assertEquals(standardized, jspf);
 }
}