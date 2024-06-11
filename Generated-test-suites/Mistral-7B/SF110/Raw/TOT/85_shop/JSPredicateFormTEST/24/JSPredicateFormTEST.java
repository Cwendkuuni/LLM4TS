import org.junit.Test;
import org.junit.Assert.*;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm pf = new JSPredicateForm();
 Assert.assertEquals(0, pf.size());
 }

 @Test
 public void testStringConstructor() {
 JSPredicateForm pf = new JSPredicateForm("(not (or (and a b) (and c d)))");
 Assert.assertEquals(4, pf.size());
 Assert.assertEquals("not", pf.get(0).toString());
 Assert.assertEquals("or", pf.get(1).toString());
 Assert.assertEquals("and", pf.get(2).toString());
 Assert.assertEquals("a", pf.get(3).toString());
 Assert.assertEquals("b", pf.get(4).toString());
 Assert.assertEquals("and", pf.get(5).toString());
 Assert.assertEquals("c", pf.get(6).toString());
 Assert.assertEquals("d", pf.get(7).toString());
 }

 @Test
 public void testStreamTokenizerConstructor() {
 String testString = "(not (or (and a b) (and c d)))";
 JSPredicateForm pf = new JSPredicateForm(new StreamTokenizer(new StringReader(testString)));
 Assert.assertEquals(4, pf.size());
 // Add checks similar to testStringConstructor()
 }

 @Test(expected = JSParserError.class)
 public void testInvalidConstructor() {
 String testString = "((not))";
 JSPredicateForm pf = new JSPredicateForm(new StreamTokenizer(new StringReader(testString)));
 fail("Expected JSParserError but test passed.");
 }

 @Test
 public void testPrint() {
 JSPredicateForm pf = new JSPredicateForm("(not (or (and a b) (and c d)))");
 pf.print();
 String expected = "(not (or (and a b) (and c d)))";
 String actual = JSUtil.getLastPrinted();
 assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
 JSPredicateForm pf = new JSPredicateForm("(not (or (and a b) (and c d)))");
 String expected = "(not (or (and a b) (and c d)))";
 String actual = pf.toStr().toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testClonePF() {
 JSPredicateForm original = new JSPredicateForm("(not (or (and a b) (and c d)))");
 JSPredicateForm cloned = original.clonePF();
 Assert.assertEquals(original, cloned);
 }

 @Test
 public void testApplySubstitutionPF() {
 JSTerm termA = new JSTerm("a");
 JSTerm termB = new JSTerm("b");
 JSTerm termC = new JSTerm("c");
 JSTerm termD = new JSTerm("d");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("a", termA);
 alpha.addBinding("b", termB);
 alpha.addBinding("c", termC);
 alpha.addBinding("d", termD);
 JSPredicateForm predicate = new JSPredicateForm("(not (or (and a b) (and c d)))");
 JSPredicateForm expectedResult = new JSPredicateForm("(not (or (and " + termA + " " + termB + ") (and " + termC + " " + termD + ")))");
 JSPredicateForm result = predicate.applySubstitutionPF(alpha);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testMatches() {
 JSTerm termA = new JSTerm("a");
 JSTerm termB = new JSTerm("b");
 JSTerm termC = new JSTerm("c");
 JSTerm termD = new JSTerm("d");
 JSSubstitution alpha = new JSSubstitution();
 alpha.addBinding("a", termA);
 alpha.addBinding("b", termB);
 alpha.addBinding("c", termC);
 alpha.addBinding("d", termD);
 JSPredicateForm pf1 = new JSPredicateForm("(and a b)");
 JSPredicateForm pf2 = new JSPredicateForm("(not (or (and a b) (and c d)))");
 JSSubstitution matchesResult = pf1.matches(pf2, alpha);
 Assert.assertEquals(matchesResult, new JSSubstitution());
 }

 @Test
 public void testEquals() {
 JSPredicateForm pf1 = new JSPredicateForm("(not (or (and a b) (and c d)))");
 JSPredicateForm pf2 = new JSPredicateForm("(not (or (and a b) (and c d)))");
 Assert.assertEquals(pf1, pf2);
 JSPredicateForm pf3 = new JSPredicateForm("(not (or (and a b) (and e d)))");
 Assert.assertNotEquals(pf1, pf3);
 }

 @Test
 public void testStandarizerPredicateForm() {
 JSPredicateForm pf = new JSPredicateForm("(not (or (and a b) (and c d)))");
 JSPredicateForm standardized = pf.standarizerPredicateForm();
 Assert.assertEquals(standardized.toString(), "(not (or (and a b) (and c d)))");
 }
}