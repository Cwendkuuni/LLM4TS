package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.Vector;

public class JSPredicateFormTest {

 @Test
 public void testEmptyJSPredicateForm() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertTrue(jspf.size() == 0);
 }

 @Test
 public void testJSPredicateFormInitWithText() throws JSParserError {
 String text = " ( x > 5 ) ";
 JSPredicateForm jspf = new JSPredicateForm(text);
 assertEquals("( x > 5 ) ", jspf.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithInvalidText() throws JSParserError {
 String text = " ( x > 5 ) ) ";
 new JSPredicateForm(text);
 }

 @Test
 public void testJSPredicateFormInitWithStreamTokenizer() throws JSParserError {
 String text = " ( x > 5 ) ";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 assertEquals("( x > 5 ) ", jspf.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testJSPredicateFormInitWithNullTokenizer() {
 new JSPredicateForm(null);
 }

 @Test
 public void testPrint() {
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.addElement("( x > 5 ) ");
 jspf.addElement("not");
 jspf.addElement(new JSTerm("x", 5));
 jspf.addElement("( y < 10 ) ");
 jspf.print();
 assertEquals("( ( x > 5 ) not ( y < 10 ) ) ", jspf.toStr().toString());
 }

 @Test
 public void testClonePF() {
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.addElement("( x > 5 ) ");
 jspf.addElement("not");
 JSPredicateForm cloned = jspf.clonePF();
 assertEquals(jspf.toString(), cloned.toString());
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.addElement("( x > 5 ) ");
 JSTerm term = new JSTerm("x", 5);
 jspf.addElement(term);
 jspf.addElement("( y < 10 ) ");
 JSSubstitution alpha = new JSSubstitution();
 alpha.assign(term.getVariableName(), new JSTerm("z", 10));
 JSPredicateForm result = jspf.applySubstitutionPF(alpha);
 JSTerm newTerm = result.elementAt(1);
 assertEquals("z", newTerm.getVariableName());
 assertEquals(10, newTerm.getValue());
 }

 @Test
 public void testMatches() {
 JSPredicateForm p1 = new JSPredicateForm();
 p1.addElement("( x > 5 ) ");
 p1.addElement("( y < 10 ) ");
 JSPredicateForm p2 = new JSPredicateForm();
 p2.addElement("( z > 5 ) ");
 p2.addElement("( w < 10 ) ");
 JSSubstitution alpha = new JSSubstitution();
 JSSubstitution beta = p1.matches(p2, alpha);
 assertTrue(beta.isEmpty());

 p1 = new JSPredicateForm();
 p1.addElement("( x > 5 ) ");
 p1.addElement("( y < 10 ) ");
 p2 = new JSPredicateForm();
 p2.addElement("( x > 5 ) ");
 p2.addElement("( y < 10 ) ");
 alpha = new JSSubstitution();
 beta = p1.matches(p2, alpha);
 assertTrue(beta.isEmpty());

 p2 = new JSPredicateForm();
 p2.addElement("( x > 5 ) ");
 p2.addElement("( y < 10 ) ");
 p1 = new JSPredicateForm();
 p1.addElement("( z > 5 ) ");
 p1.addElement("( w < 10 ) ");
 alpha = new JSSubstitution();
 alpha.assign("x", new JSTerm("z", 5));
 alpha.assign("y", new JSTerm("w", 10));
 beta = p1.matches(p2, alpha);
 assertTrue(beta.isEmpty());

 p1 = new JSPredicateForm();
 p1.addElement("( x > 5 ) ");
 p1.addElement("( y < 10 ) ");
 p2 = new JSPredicateForm();
 p2.addElement("( x > 5 ) ");
 p2.addElement("( z < 10 ) ");
 alpha = new JSSubstitution();
 beta = p1.matches(p2, alpha);
 assertFalse(beta.isEmpty());
 }

 @Test
 public void testEquals() {
 JSPredicateForm p1 = new JSPredicateForm();
 p1.addElement("( x > 5 ) ");
 p1.addElement("( y < 10 ) ");
 JSPredicateForm p2 = new JSPredicateForm();
 p2.addElement("( x > 5 ) ");
 p2.addElement("( y < 10 ) ");
 assertTrue(p1.equals(p2));

 p1 = new JSPredicateForm();
 p1.addElement("( x > 5 ) ");
 p1.addElement("( y < 10 ) ");
 p2 = new JSPredicateForm();
 p2.addElement("( x > 5 ) ");
 p2.addElement("( z < 10 ) ");
 assertFalse(p1.equals(p2));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm jspf = new JSPredicateForm();
 jspf.addElement("( x > 5 and y < 10 ) ");
 jspf.addElement("not");
 jspf.addElement("( z > 5 ) ");
 jspf.addElement("( y > 10 ) ");
 JSPredicateForm standardized = jspf.standarizerPredicateForm();
 assertEquals("( and (not (x > 5 )) (not (z > 5 )) ) (or (not (y < 10 )) (not (y > 10 )))", standardized.toStr().toString());
 }
}