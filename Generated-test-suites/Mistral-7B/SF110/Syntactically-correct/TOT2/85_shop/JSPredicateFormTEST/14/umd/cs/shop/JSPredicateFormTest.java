import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertEquals(0, jspf.size());
 }

 @Test
 public void testStringConstructor() {
 JSPredicateForm jspf = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
 assertEquals(3, jspf.size());
 assertEquals("not", jspf.elementAt(0).toString());
 JSTerm not = (JSTerm) jspf.elementAt(1);
 assertEquals("or", not.elementAt(0).toString());
 JSTerm leftOr = (JSTerm) not.elementAt(1);
 assertEquals("=", leftOr.elementAt(0).toString());
 JSTerm left = (JSTerm) leftOr.elementAt(1);
 assertEquals("x", left.elementAt(0).toString());
 assertEquals(1, left.size());
 JSTerm right = leftOr.elementAt(2);
 assertEquals(1, right.size());
 assertEquals(1, right.elementAt(0).intValue());
 }

 @Test
 public void testStreamTokenizerConstructor() {
 String input = "(not (or (= x 1) (= x 2)))";
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(input)));
 assertEquals(3, jspf.size());
 // ... add similar assertions as in testStringConstructor()
 }

 @Test(expected = JSParserError.class)
 public void testUnexpectedCharacterInInput() {
 String input = "(not (or (= x 1) (= x 2) z)";
 JSPredicateForm jspf = new JSPredicateForm(new StreamTokenizer(new StringReader(input)));
 fail("Should have thrown JSParserError");
 }

 @Test
 public void testPrint() {
 JSPredicateForm jspf = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
 jspf.print();
 assertEquals("(not (or (= x 1) (= x 2)))", jspf.toStr().toString());
 }

 @Test
 public void testClonePF() {
 JSPredicateForm original = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
 JSPredicateForm cloned = original.clonePF();
 assertEquals(original.toStr().toString(), cloned.toStr().toString());
 }

 @Test
 public void testApplySubstitutionPF() {
 JSPredicateForm p1 = new JSPredicateForm("(or (= x 1) (= x 2))");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("1", new JSTerm("3"));
 JSPredicateForm p2 = p1.applySubstitutionPF(alpha);
 assertEquals("(or (= x 3) (= x 2))", p2.toStr().toString());
 }

 @Test
 public void testMatches() {
 JSPredicateForm p1 = new JSPredicateForm("(or (= x 1) (= x 2))");
 JSPredicateForm p2 = new JSPredicateForm("(or (= x 2) (= x 1))");
 JSSubstitution beta = p1.matches(p2, new JSSubstitution());
 assertTrue(beta.fail());
 JSPredicateForm p3 = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
 JSSubstitution gama = new JSSubstitution();
 gama.addBinding("x", new JSTerm("1"));
 JSSubstitution delta = p1.matches(p3, gama);
 assertTrue(delta.fail());
 }

 @Test
 public void testEquals() {
 JSPredicateForm p1 = new JSPredicateForm("(or (= x 1) (= x 2))");
 JSPredicateForm p2 = new JSPredicateForm("(or (= x 1) (= x 2))");
 assertTrue(p1.equals(p2));
 JSPredicateForm p3 = new JSPredicateForm("(not (= x 1) (= x 2))");
 assertFalse(p1.equals(p3));
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm p1 = new JSPredicateForm("(not (or (= x 1) (= x 2)))");
 JSPredicateForm p2 = p1.standarizerPredicateForm();
 assertEquals("(not (or (= x 1) (= x 2)))", p2.toStr().toString());
 }
}