import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.*;

public class JSPredicateFormTest {

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm jspf = new JSPredicateForm();
 assertEquals(0, jspf.size());
 }

 @Test
 public void testStringConstructor() {
 String text = "!(p(x) & q(y))";
 JSPredicateForm jspf = new JSPredicateForm(text);
 assertEquals(3, jspf.size());
 assertEquals("!", jspf.elementAt(0).toString());
 assertEquals("p", jspf.elementAt(1).toString());
 assertEquals("x", ((JSTerm) jspf.elementAt(2)).getVariable());
 assertEquals("&", jspf.elementAt(3).toString());
 assertEquals("q", jspf.elementAt(4).toString());
 assertEquals("y", ((JSTerm) jspf.elementAt(5)).getVariable());
 }

 @Test
 public void testStreamTokenizerConstructor() {
 String text = "!(p(x) & q(y))";
 StreamTokenizer st = new StreamTokenizer(new StringReader(text));
 JSPredicateForm jspf = new JSPredicateForm(st);
 assertEquals(3, jspf.size());
 // ... add similar assertions as in testStringConstructor here
 }

 @Test
 public void testPrintMethod() {
 String text = "!(p(x) & q(y))";
 JSPredicateForm jspf = new JSPredicateForm(text);
 String expectedOutput = "((! p) x) & ((! q) y)";
 jspf.print();
 assertEquals(expectedOutput, JSUtil.readLine());
 }

 @Test
 public void testToStrMethod() {
 String text = "!(p(x) & q(y))";
 JSPredicateForm jspf = new JSPredicateForm(text);
 String expectedOutput = "((! p) x) & ((! q) y)";
 assertEquals(expectedOutput, jspf.toStr().toString());
 }

 @Test
 public void testClonePFMethod() {
 String text = "!(p(x) & q(y))";
 JSPredicateForm jspf = new JSPredicateForm(text);
 JSPredicateForm clonedPF = jspf.clonePF();
 assertEquals(jspf.size(), clonedPF.size());
 // ... add similar assertions as in testPrintMethod and testToStrMethod here
 }

 @Test
 public void testApplySubstitutionPFMethod() {
 String text = "!(p(x) & q(y))";
 JSPredicateForm jspf = new JSPredicateForm(text);
 String newVar = "z";
 JSSubstitution alpha = new JSSubstitution();
 alpha.addSubstitution(new Variable("x"), new Variable(newVar));
 JSPredicateForm expectedOutput = new JSPredicateForm("!(p(" + newVar + ") & q(y))");
 JSPredicateForm result = jspf.applySubstitutionPF(alpha);
 assertEquals(expectedOutput.size(), result.size());
 // ... add similar assertions as in testPrintMethod and testToStrMethod here
 }

 @Test
 public void testMatchesMethod() {
 String text1 = "!(p(x) & q(y))";
 String text2 = "!(r(z) & s(t))";
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 JSSubstitution expectedOutput = new JSSubstitution();
 // ... add assertions for successful matches here

 JSSubstitution alpha = new JSSubstitution();
 alpha.addSubstitution(new Variable("x"), new Variable("z"));
 alpha.addSubstitution(new Variable("y"), new Variable("t"));
 JSSubstitution beta = jspf1.matches(jspf2, alpha);
 assertEquals(expectedOutput, beta);
 }

 @Test
 public void testEqualsMethod() {
 String text1 = "!(p(x) & q(y))";
 String text2 = "!(p(x) & r(y))";
 JSPredicateForm jspf1 = new JSPredicateForm(text1);
 JSPredicateForm jspf2 = new JSPredicateForm(text2);
 assertFalse(jspf1.equals(jspf2));

 String text3 = "!(p(x) & q(x))";
 JSPredicateForm jspf3 = new JSPredicateForm(text3);
 assertTrue(jspf1.equals(jspf3));
 }

 @Test
 public void testStandarizerPredicateFormMethod() {
 String text = "!(p(x) & q(y))";
 JSPredicateForm jspf = new JSPredicateForm(text);
 String expectedOutput = "(! p x) & (! q y)";
 JSPredicateForm result = jspf.standarizerPredicateForm();
 assertEquals(expectedOutput.size(), result.size());
 // ... add similar assertions as in testPrintMethod and testToStrMethod here
 }
}