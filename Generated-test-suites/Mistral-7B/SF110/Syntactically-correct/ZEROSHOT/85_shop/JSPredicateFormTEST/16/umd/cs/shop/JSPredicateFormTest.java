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
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertEquals(0, jspf.size());
 }

 @Test
 public void testStringConstructor() {
 JSPredicateForm jspf = new JSPredicateForm("(not (or (and a b) (and c d)))");
 assertEquals(4, jspf.size());
 assertEquals("not", jspf.get(0).toString());
 JSTerm and1 = jspf.get(1);
 assertEquals("and", and1.toString());
 assertEquals("a", and1.get(0).toString());
 assertEquals("b", and1.get(1).toString());
 JSTerm or1 = jspf.get(2);
 assertEquals("or", or1.toString());
 JSTerm and2 = jspf.get(3);
 assertEquals("and", and2.toString());
 assertEquals("c", and2.get(0).toString());
 assertEquals("d", and2.get(1).toString());
 }

 @Test
 public void testStreamTokenizerConstructor() {
 String text = "(not (or (and a b) (and c d)))";
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(text)));
 assertEquals(4, jspf.size());
 // ... add similar assertions as in testStringConstructor() here
 }

 @Test
 public void testClonePF() {
 JSPredicateForm jspf = new JSPredicateForm("(not (or (and a b) (and c d)))");
 JSPredicateForm cloned = jspf.clonePF();
 assertEquals(jspf, cloned);
 }

 @Test
 public void testApplySubstitutionPF() {
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("a", new JSTerm("X"));
 JSPredicateForm jspf = new JSPredicateForm("(not (or (and a b) (and c d)))");
 JSPredicateForm expected = new JSPredicateForm("(not (or (and X b) (and c d)))");
 JSPredicateForm result = jspf.applySubstitutionPF(alpha);
 assertEquals(expected, result);
 }

 @Test
 public void testMatches_Failure() {
 JSPredicateForm jspf1 = new JSPredicateForm("(and a b)");
 JSPredicateForm jspf2 = new JSPredicateForm("(or a b c)");
 JSSubstitution beta = jspf1.matches(jspf2);
 assertFalse(beta.isSuccess());
 }

 @Test
 public void testMatches_Success() {
 JSPredicateForm jspf1 = new JSPredicateForm("(and a b)");
 JSPredicateForm jspf2 = new JSPredicateForm("(and a b c)");
 JSSubstitution beta = jspf1.matches(jspf2);
 assertTrue(beta.isSuccess());
 assertEquals(2, beta.size());
 assertEquals("a", beta.get(0).getName());
 assertEquals("b", beta.get(1).getName());
 }

 @Test
 public void testEquals() {
 JSPredicateForm jspf1 = new JSPredicateForm("(and a b)");
 JSPredicateForm jspf2 = new JSPredicateForm("(and a b)");
 JSPredicateForm jspf3 = new JSPredicateForm("(or a b)");
 assertTrue(jspf1.equals(jspf2));
 assertFalse(jspf1.equals(jspf3));
 }

 @Test(expected = JSParserError.class)
 public void testUnexpectedCharacter() {
 JSPredicateForm jspf = new JSPredicateForm("(not (or a b )");
 // the space after "b" is unexpected
 }

 @Test(expected = JSParserError.class)
 public void testMissingLeftParenthesis() {
 JSPredicateForm jspf = new JSPredicateForm("not (or a b c");
 // the missing "(" at the beginning
 }

 @Test(expected = JSParserError.class)
 public void testMissingRightParenthesis() {
 JSPredicateForm jspf = new JSPredicateForm("(not (or a b)");
 // the missing ")" at the end
 }

 @Test
 public void testPrint() {
 JSPredicateForm jspf = new JSPredicateForm("(not (or (and a b) (and c d)))");
 jspf.print();
 String expected = "(not (or (and a b) (and c d)))";
 String actual = JSUtil.readFromBuffer();
 assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
 JSPredicateForm jspf = new JSPredicateForm("(not (or (and a b) (and c d)))");
 String expected = "(not (or (and a b) (and c d) ))";
 String actual = jspf.toStr().toString();
 assertEquals(expected, actual);
 }
}