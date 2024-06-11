package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import java.io.StringReader;
import org.junit.Test;

public class JSPredicateFormTest {

 JSTerm createTerm(StreamTokenizer tokenizer) {
 return new JSTerm(tokenizer);
 }

 @Test
 public void testEmptyJSPredicateForm() {
 JSPredicateForm jspf = new JSPredicateForm();
 JSPredicateForm clone = jspf.clonePF();
 assertEquals("", jspf.toStr().toString());
 assertEquals("", clone.toStr().toString());
 }

 @Test
 public void testJSPredicateFormWithOneElement() {
 String text = "not ( x = y )";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSPredicateForm clone = jspf.clonePF();
 assertEquals("not ( x = y )", jspf.toStr().toString());
 assertEquals("not ( x = y )", clone.toStr().toString());
 }

 @Test
 public void testJSPredicateFormWithNotOperator() {
 String text = "not (( x = y ) and ( z = w ))";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSPredicateForm clone = jspf.clonePF();
 assertEquals("not (( x = y ) and ( z = w ))", jspf.toStr().toString());
 assertEquals("not (( x = y ) and ( z = w ))", clone.toStr().toString());
 }

 @Test
 public void testJSPredicateFormWithCallOperator() {
 String text = "call max ( x, y )";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSPredicateForm clone = jspf.clonePF();
 assertEquals("call max ( x, y )", jspf.toStr().toString());
 assertEquals("call max ( x, y )", clone.toStr().toString());
 }

 @Test
 public void testJSPredicateFormWithSubstitution() {
 String text = "not (( x = y ) and ( z = w ))";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("x", 3);
 alpha.addBinding("y", 4);
 alpha.addBinding("z", 5);
 alpha.addBinding("w", 6);
 JSPredicateForm jspf2 = jspf.applySubstitutionPF(alpha);
 JSPredicateForm clone = jspf2.clonePF();
 assertEquals("not (( 3 = 4 ) and ( 5 = 6 ))", jspf2.toStr().toString());
 assertEquals("not (( 3 = 4 ) and ( 5 = 6 ))", clone.toStr().toString());
 }

 @Test
 public void testJSPredicateFormEquals() {
 String text1 = "not (( x = y ) and ( z = w ))";
 String text2 = "not (( x = y ) and ( z = w ))";
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 JSPredicateForm clone1 = jspf1.clonePF();
 JSPredicateForm clone2 = jspf2.clonePF();
 assertTrue(jspf1.equals(jspf2));
 assertTrue(jspf1.equals(clone1));
 assertTrue(jspf2.equals(clone2));
 }

 @Test
 public void testJSPredicateFormNotEquals() {
 String text1 = "not (( x = y ) and ( z = w ))";
 String text2 = "not (( x = z ) and ( y = w ))";
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 JSPredicateForm clone1 = jspf1.clonePF();
 JSPredicateForm clone2 = jspf2.clonePF();
 assertFalse(jspf1.equals(jspf2));
 assertFalse(jspf1.equals(clone2));
 assertFalse(jspf2.equals(clone1));
 }

 @Test
 public void testJSPredicateFormMatches() {
 String text1 = "not (( x = y ) and ( z = w ))";
 String text2 = "not (( x = z ) and ( y = w ))";
 String text3 = "not (( x = x ) and ( y = y ))";
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 JSPredicateForm jspf3 = new JSPredicateForm(text3);JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = new JSSubstitution();
 assertEquals(jspf1, jspf1.matches(jspf1, alpha));
 assertEquals(jspf1, jspf1.matches(jspf3, alpha));
 assertNull(jspf1.matches(jspf2, alpha));
 }

 @Test
 public void testJSPredicateFormStandarizerPredicateForm() {
 String text = "not (( not ( x = y ) ) and ( z = w ))";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSPredicateForm clone = jspf.clonePF();
 assertEquals("not ( not ( x = y ) ) and ( z = w )", jspf.toStr().toString());
 assertEquals("not ( not ( x = y ) ) and ( z = w )", clone.toStr().toString());
 JSPredicateForm standarized = jspf.standarizerPredicateForm();
 JSPredicateForm cloneStandarized = standarized.clonePF();
 assertEquals("not ( x != y ) and ( z = w )", standarized.toStr().toString());
 assertEquals("not ( x != y ) and ( z = w )", cloneStandarized.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormException() {
 String text = "not ( x )";
 JSPredicateForm jspf = new JSPredicateForm(text);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormExceptionInvalidToken() {
 String text = "not ( x = )";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormExceptionInvalidOperator() {
 String text = "not ( x = y ) not";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormExceptionMissingRightParenthesis() {
 String text = "not ( x = y";
 StreamTokenizer tokenizer = new StringReader(text);
 tokenizer.nextToken(); // consume '('
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormExceptionUnexpectedToken() {
 String text = "not ( x = y + )";
 StreamTokenizer tokenizer = new StringReader(text);
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormExceptionUnexpectedEndOfInput() {
 String text = "not ( x = y ";
 StreamTokenizer tokenizer = new StringReader(text);
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 }

 @Test(expected = NullPointerException.class)
 public void testJSPredicateFormNullArgument() {
 String text = "not ( x = y )";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm jspf = new JSPredicateForm(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testJSTermNullArgument() {
 JSTerm term = new JSTerm(null);
 term.print();
 }
}