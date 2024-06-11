import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm pf = new JSPredicateForm();
 assertEquals("", pf.toStr().toString());
 }

 @Test
 public void testStringConstructor() {
 String text = "!(not (and a b))";
 JSPredicateForm pf = new JSPredicateForm(text);
 String expected = "((!(not (and a b))))";
 assertEquals(expected, pf.toStr().toString());
 }

 @Test
 public void testStreamTokenizerConstructor() {
 String text = "!(not (and a b))";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 String expected = "((!(not (and a b))))";
 assertEquals(expected, pf.toStr().toString());
 }

 @Test
 public void testInvalidConstructor() {
 String text = "!";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 try {
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 fail("Expected JSParserError");
 } catch (JSParserError e) {
 // expected
 }
 }

 @Test
 public void testPrint() {
 String text = "!(not (and a b))";
 JSPredicateForm pf = new JSPredicateForm(text);
 String expected = "((!(not (and a b))))";
 pf.print();
 String output = JSUtil.getOutput();
 assertEquals(expected, output);
 JSUtil.clearOutput();
 }

 @Test
 public void testToString() {
 String text = "!(not (and a b))";
 JSPredicateForm pf = new JSPredicateForm(text);
 String expected = "((!(not (and a b))))";
 String actual = pf.toStr().toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testClonePF() {
 String text = "!(not (and a b))";
 JSPredicateForm original = new JSPredicateForm(text);
 JSPredicateForm clone = original.clonePF();
 String expected = "((!(not (and a b))))";
 String actual = clone.toStr().toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testApplySubstitutionPF() {
 String text = "(x = y)";
 JSPredicateForm pf = new JSPredicateForm(text);
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("x", new JSTerm("5"));
 JSPredicateForm result = pf.applySubstitutionPF(alpha);
 String expected = "(5 = y)";
 assertEquals(expected, result.toStr().toString());
 }

 @Test
 public void testMatches() {
 String text1 = "(x = y)";
 String text2 = "(z = y)";
 JSPredicateForm pf1 = new JSPredicateForm(text1);
 JSPredicateForm pf2 = new JSPredicateForm(text2);
 JSSubstitution result = pf1.matches(pf2);
 String expected = "{}";
 assertEquals(expected, result.toString());
 }

 @Test
 public void testMatchesWithBinding() {
 String text1 = "(x = y)";
 String text2 = "(z = y)";
 JSPredicateForm pf1 = new JSPredicateForm(text1);
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("x", new JSTerm("5"));
 JSPredicateForm pf2 = new JSPredicateForm(text2);
 JSSubstitution result = pf1.matches(pf2, alpha);
 String expected = "{x/5}";
 assertEquals(expected, result.toString());
 }

 @Test
 public void testEquals() {
 String text1 = "(x = y)";
 String text2 = "(x = y)";
 JSPredicateForm pf1 = new JSPredicateForm(text1);
 JSPredicateForm pf2 = new JSPredicateForm(text2);
 assertTrue(pf1.equals(pf2));
 }

 @Test
 public void testNotEquals() {
 String text1 = "(x = y)";
 String text2 = "(x = z)";
 JSPredicateForm pf1 = new JSPredicateForm(text1);
 JSPredicateForm pf2 = new JSPredicateForm(text2);
 assertFalse(pf1.equals(pf2));
 }

 @Test(expected = JSParserError.class)
 public void testUnexpectedToken() {
 String text = "!(not (and a b))";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testUnexpectedOperator() {
 String text = "!%%";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testMissingRightParenthesis() {
 String text = "!(not (and a b";
 StreamTokenizer tokenizer = new StringReader(text);
 JSPredicateForm pf = new JSPredicateForm(tokenizer);
 }
}