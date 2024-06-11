package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 private JSPredicateForm createJSPredicateForm(String input) {
 return new JSPredicateForm(input);
 }

 @Test
 public void testJSPredicateFormInit() {
 // Test default constructor
 JSPredicateForm predicateForm = new JSPredicateForm();
 // Test constructor with null text
 assertThrows(JSParserError.class, () -> new JSPredicateForm(null));
 // Test constructor with valid text
 String validText = "not (and (or x y) z)";
 JSPredicateForm validPredicateForm = createJSPredicateForm(validText);
 // Test constructor with StreamTokenizer
 StringReader reader = new StringReader(validText);
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm validPredicateForm2 = new JSPredicateForm(tokenizer);
 // Check that both instances are equal
 assertEquals(validPredicateForm, validPredicateForm2);
 }

 @Test
 public void testJSPredicateFormPrint() {
 // Test default constructor
 JSPredicateForm predicateForm = new JSPredicateForm();
 predicateForm.print(); // Should print empty output
 // Test constructor with valid text
 String validText = "not (and (or x y) z)";
 JSPredicateForm validPredicateForm = createJSPredicateForm(validText);
 validPredicateForm.print(); // Should print "(not (and (or x y) z))"
 // Test constructor with StreamTokenizer
 StringReader reader = new StringReader(validText);
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 JSPredicateForm validPredicateForm2 = new JSPredicateForm(tokenizer);
 validPredicateForm2.print(); // Should print "(not (and (or x y) z))"
 }

 @Test
 public void testJSPredicateFormToStr() {
 // Test default constructor
 JSPredicateForm predicateForm = new JSPredicateForm();
 String result = predicateForm.toStr().toString(); // Should be empty string
 // Test constructor with valid text
 String validText = "not (and (or x y) z)";
 JSPredicateForm validPredicateForm = createJSPredicateForm(validText);
 String expectedResult = "((not (and (or x y) z)))";
 String result2 = validPredicateForm.toStr().toString();
 assertEquals(expectedResult, result2);
 }

 @Test
 public void testJSPredicateFormClonePF() {
 // Test default constructor
 JSPredicateForm predicateForm = new JSPredicateForm();
 JSPredicateForm clonedPredicateForm = predicateForm.clonePF();
 assertNotSame(predicateForm, clonedPredicateForm);
 // Test constructor with valid text
 String validText = "not (and (or x y) z)";
 JSPredicateForm validPredicateForm = createJSPredicateForm(validText);
 JSPredicateForm clonedValidPredicateForm = validPredicateForm.clonePF();
 assertNotSame(validPredicateForm, clonedValidPredicateForm);
 assertEquals(validPredicateForm, clonedValidPredicateForm);
 }

 @Test
 public void testJSPredicateFormApplySubstitutionPF() {
 // Test default constructor
 JSPredicateForm predicateForm = new JSPredicateForm();
 JSSubstitution result = predicateForm.applySubstitutionPF(new JSSubstitution());
 assertNotNull(result);
 // Test constructor with valid text
 String validText = "not (and (or x y) z)";
 JSPredicateForm validPredicateForm = createJSPredicateForm(validText);
 JSSubstitution emptySubstitution = new JSSubstitution();
 JSSubstitution expectedResult = new JSSubstitution();
 JSTerm xTerm = new JSTerm("x");
 JSTerm yTerm = new JSTerm("y");
 JSTerm zTerm = new JSTerm("z");
 expectedResult.addBinding("x", xTerm);
 expectedResult.addBinding("y", yTerm);
 expectedResult.addBinding("z", zTerm);
 JSSubstitution result2 = validPredicateForm.applySubstitutionPF(emptySubstitution);
 assertEquals(expectedResult, result2);
 }

 @Test
 public void testJSPredicateFormMatches() {
 // Test default constructor
 JSPredicateForm predicateForm = new JSPredicateForm();
 JSPredicateForm otherPredicateForm = new JSPredicateForm();
 JSSubstitution result = predicateForm.matches(otherPredicateForm);
 assertNull(result);
 // Test constructor with valid text
 String predicateText = "not (and (or x y) z)";
 String otherPredicateText = "not (and (or a b) w)";
 JSPredicateForm predicateForm1 = createJSPredicateForm(predicateText);
 JSPredicateForm otherPredicateForm1 = createJSPredicateForm(otherPredicateText);
 JSSubstitution expectedResult = new JSSubstitution();
 JSTerm xTerm = new JSTerm("x");
 JSTerm yTerm = new JSTerm("y");
 JSTerm zTerm = new JSTerm("z");
 JSTerm aTerm = new JSTerm("a");
 JSTerm bTerm = new JSTerm("b");
 JSTerm wTerm = new JSTerm("w");
 expectedResult.addBinding("x", xTerm);
 expectedResult.addBinding("y", yTerm);
 expectedResult.addBinding("z", zTerm);
 expectedResult.addBinding("a", aTerm);
 expectedResult.addBinding("b", bTerm);
 expectedResult.addBinding("w", wTerm);
 JSSubstitution result2 = predicateForm1.matches(otherPredicateForm1);
 assertEquals(expectedResult, result2);
 }

 @Test
 public void testJSPredicateFormEquals() {
 // Test default constructor
 JSPredicateForm predicateForm = new JSPredicateForm();
 JSPredicateForm otherPredicateForm = new JSPredicateForm();
 assertFalse(predicateForm.equals(otherPredicateForm));
 // Test constructor with valid text
 String predicateText = "not (and (or x y) z)";
 String otherPredicateText = "not (and (or a b) w)";
 JSPredicateForm predicateForm1 = createJSPredicateForm(predicateText);
 JSPredicateForm otherPredicateForm1 = createJSPredicateForm(otherPredicateText);
 assertFalse(predicateForm1.equals(otherPredicateForm1));
 // Test when both instances are equal
 String equalPredicateText = "not (and (or x y) z)";
 JSPredicateForm equalPredicateForm1 = createJSPredicateForm(equalPredicateText);
 JSPredicateForm equalPredicateForm2 = createJSPredicateForm(equalPredicateText);
 assertTrue(equalPredicateForm1.equals(equalPredicateForm2));
 }

 @Test
 public void testJSPredicateFormStandarizerPredicateForm() {
 // Test default constructor
 JSPredicateForm predicateForm = new JSPredicateForm();
 JSPredicateForm result = predicateForm.standarizerPredicateForm();
 assertNotNull(result);
 // Test constructor with valid text
 String validText = "not (and (or x y) z)";
 JSPredicateForm validPredicateForm = createJSPredicateForm(validText);
 JSPredicateForm expectedResult = new JSPredicateForm();
 JSTerm notTerm = new JSTerm("not");
 JSTerm andTerm = new JSTerm("and");
 JSTerm orTerm = new JSTerm("or");
 JSTerm xTerm = new JSTerm("x");
 JSTerm yTerm = new JSTerm("y");
 JSTerm zTerm = new JSTerm("z");
 expectedResult.addElement(notTerm);
 expectedResult.addElement(andTerm);
 expectedResult.addElement(xTerm);
 expectedResult.addElement(orTerm);
 expectedResult.addElement(yTerm);
 expectedResult.addElement(zTerm);
 JSPredicateForm result2 = validPredicateForm.standarizerPredicateForm();
 assertEquals(expectedResult, result2);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithInvalidText() {
 String invalidText = "not (and (or x y) z %%%)";
 createJSPredicateForm(invalidText);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithInvalidTokenizer() {
 StringReader reader = new StringReader("not (and (or x y) z)");
 StreamTokenizer tokenizer = new StreamTokenizer(reader);
 tokenizer.ordinaryChar('%');
 createJSPredicateForm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithMissingRightParenthesis() {
 String validText = "not (and (or x y) z";
 createJSPredicateForm(validText);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithMissingLeftParenthesis() {
 String validText = "not and (or x y) z)";
 createJSPredicateForm(validText);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithMissingOperator() {
 String validText = "not (and or x y) z";
 createJSPredicateForm(validText);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithDuplicateOperator() {
 String validText = "not (and (or x y) and x y) z";
 createJSPredicateForm(validText);
 }

}