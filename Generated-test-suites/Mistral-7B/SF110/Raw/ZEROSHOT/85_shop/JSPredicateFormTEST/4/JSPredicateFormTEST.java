package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;

public class JSPredicateFormTest {

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertEquals("Expected an empty JSPredicateForm", 0, jspf.size());
 }

 @Test
 public void testStringConstructor() {
 JSPredicateForm jspf = new JSPredicateForm("(and (or a b) (not c))");
 assertEquals("Expected a JSPredicateForm with size 3", 3, jspf.size());
 assertEquals("Expected the first element to be 'and'", "and", jspf.elementAt(0).toString());
 }

 @Test
 public void testStreamTokenizerConstructor() {
 String input = "(and (or a b) (not c))";
 StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
 JSPredicateForm jspf = new JSPredicateForm(tokenizer);
 assertEquals("Expected a JSPredicateForm with size 3", 3, jspf.size());
 assertEquals("Expected the first element to be 'and'", "and", jspf.elementAt(0).toString());
 }

 @Test
 public void testClonePF() {
 JSPredicateForm original = new JSPredicateForm("(and (or a b) (not c))");
 JSPredicateForm cloned = original.clonePF();
 assertEquals("Expected the cloned JSPredicateForm to have the same elements as the original", original, cloned);
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm original = new JSPredicateForm("(and (or a b) (not c))");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addSubstitution("a", new JSTerm("d"));
 JSPredicateForm expected = new JSPredicateForm("(and (or d b) (not c))");
 JSPredicateForm result = original.applySubstitutionPF(alpha);
 assertEquals("Expected the result to be the expected JSPredicateForm", expected, result);
 }

 @Test
 public void testMatches() {
 JSPredicateForm p1 = new JSPredicateForm("(and (or a b) (not c))");
 JSPredicateForm p2 = new JSPredicateForm("(and (or d e) (not f))");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addSubstitution("a", new JSTerm("d"));
 alpha.addSubstitution("b", new JSTerm("e"));
 JSSubstitution beta = p1.matches(p2);
 assertTrue("Expected the substitution to be successful", beta.isSuccess());
 assertEquals("Expected the substitution to map 'a' to 'd' and 'b' to 'e'", 2, beta.size());
 }

 @Test
 public void testEquals() {
 JSPredicateForm p1 = new JSPredicateForm("(and (or a b) (not c))");
 JSPredicateForm p2 = new JSPredicateForm("(and (or a b) (not c))");
 JSPredicateForm p3 = new JSPredicateForm("(and (or d e) (not f))");
 assertTrue("Expected p1 and p2 to be equal", p1.equals(p2));
 assertFalse("Expected p1 and p3 to be equal", p1.equals(p3));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm jspf = new JSPredicateForm("(and (or a (not b)) (not (or c d)))");
 JSPredicateForm standard = jspf.standarizerPredicateForm();
 assertEquals("Expected the standardizerPredicateForm to be (and (not (or a b)) (not (or c d)))", "not (or a b) and not (or c d)", standard.toString());
 }
}